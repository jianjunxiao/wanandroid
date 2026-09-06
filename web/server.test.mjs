import test from 'node:test';
import assert from 'node:assert/strict';
import https from 'node:https';
import { mkdtemp, rm, writeFile } from 'node:fs/promises';
import { tmpdir } from 'node:os';
import path from 'node:path';
import { Readable, Writable } from 'node:stream';
import { createServer } from './server.mjs';

async function fixture(t) {
  const staticDir = await mkdtemp(path.join(tmpdir(), 'wanandroid-web-'));
  await writeFile(path.join(staticDir, 'index.html'), '<title>玩安卓</title>');
  await writeFile(path.join(staticDir, 'app.wasm'), Buffer.from([0, 97, 115, 109]));
  const server = createServer({ staticDir });
  await new Promise(resolve => server.listen(0, '127.0.0.1', resolve));
  t.after(async () => {
    await new Promise(resolve => server.close(resolve));
    await rm(staticDir, { recursive: true, force: true });
  });
  return `http://127.0.0.1:${server.address().port}`;
}

function upstream(t, headers = {}) {
  const requests = [];
  t.mock.method(https, 'request', (url, options, callback) => {
    let body = '';
    const request = new Writable({ write(chunk, encoding, done) { body += chunk.toString(); done(); } });
    request.on('finish', () => {
      requests.push({ url: url.toString(), ...options, body });
      const response = Readable.from(['{"errorCode":0,"data":null,"errorMsg":""}']);
      response.statusCode = 200;
      response.headers = { 'content-type': 'application/json', ...headers };
      callback(response);
    });
    return request;
  });
  return requests;
}

test('静态页面、Wasm MIME、HEAD 与不存在的文件', async t => {
  const base = await fixture(t);
  assert.match(await (await fetch(base)).text(), /玩安卓/);
  const wasm = await fetch(`${base}/app.wasm`, { method: 'HEAD' });
  assert.equal(wasm.headers.get('content-type'), 'application/wasm');
  assert.equal(wasm.headers.get('content-length'), '4');
  assert.equal(await wasm.text(), '');
  assert.equal((await fetch(`${base}/missing.js`)).status, 404);
  assert.equal((await fetch(`${base}/%2e%2e%2fsecret`)).status, 403);
  assert.equal((await fetch(`${base}/%ZZ`)).status, 400);
  assert.equal((await fetch(`${base}/%00`)).status, 400);
  assert.equal((await fetch(base)).status, 200);
});

test('上游响应中断只结束当前请求，服务器继续提供页面', async t => {
  t.mock.method(https, 'request', (url, options, callback) => {
    const request = new Writable({ write(chunk, encoding, done) { done(); } });
    request.on('finish', () => {
      const response = new Readable({
        read() {
          this.push('partial response');
          this.destroy(new Error('connection reset'));
        },
      });
      response.statusCode = 200;
      response.headers = { 'content-type': 'application/json' };
      callback(response);
    });
    return request;
  });
  const base = await fixture(t);
  await assert.rejects(async () => (await fetch(`${base}/api/article/top/json`)).text());
  assert.equal((await fetch(base)).status, 200);
});

test('拒绝非本站来源、未列入白名单的接口和无效方法', async t => {
  const base = await fixture(t);
  assert.equal((await fetch(`${base}/api/user/login`, { method: 'POST', headers: { Origin: 'https://other.example' } })).status, 403);
  assert.equal((await fetch(`${base}/api/admin/delete`)).status, 404);
  assert.equal((await fetch(`${base}/api/user/login`, { method: 'DELETE' })).status, 405);
  assert.equal((await fetch(`${base}/api/session/clear`)).status, 404);
  assert.equal((await fetch(base, { method: 'POST' })).status, 405);
});

test('表单、Cookie 和查询参数转发到固定 WanAndroid 服务', async t => {
  const requests = upstream(t, { 'set-cookie': ['loginUserName=qa; Domain=wanandroid.com; Path=/user; HttpOnly; Secure; SameSite=Lax'] });
  const base = await fixture(t);
  const body = new URLSearchParams({ username: '中文账号', password: 'test' }).toString();
  const response = await fetch(`${base}/api/user/login?source=qa`, {
    method: 'POST', headers: { Origin: base, Cookie: 'JSESSIONID=qa', 'Content-Type': 'application/x-www-form-urlencoded' }, body,
  });
  assert.equal(response.status, 200);
  assert.deepEqual(requests.map(r => [r.url, r.method, r.body, r.headers.Cookie]), [
    ['https://wanandroid.com/user/login?source=qa', 'POST', body, 'JSESSIONID=qa'],
  ]);
  assert.equal(response.headers.get('set-cookie'), 'loginUserName=qa; Path=/; HttpOnly; SameSite=Lax');
});

test('HTTPS 代理保留 Secure，退出清除所有当前会话 Cookie', async t => {
  upstream(t, { 'set-cookie': ['JSESSIONID=qa; Secure; HttpOnly'] });
  const base = await fixture(t);
  const login = await fetch(`${base}/api/user/login`, { method: 'POST', headers: { 'X-Forwarded-Proto': 'https' } });
  assert.match(login.headers.get('set-cookie'), /Secure; HttpOnly; Path=\/$/);
  const logout = await fetch(`${base}/api/session/clear`, { method: 'POST', headers: { Cookie: 'JSESSIONID=qa; loginUserName=qa' } });
  assert.equal(logout.status, 200);
  assert.equal(logout.headers.getSetCookie().length, 2);
  for (const cookie of logout.headers.getSetCookie()) assert.match(cookie, /Path=\/; Max-Age=0; HttpOnly; SameSite=Lax/);
});

test('图片代理限制域名、协议与端口，且不会向图片域名转发账号 Cookie', async t => {
  const requests = upstream(t, { 'content-type': 'image/png' });
  const base = await fixture(t);
  for (const url of ['file:///etc/passwd', 'https://127.0.0.1/a', 'https://wanandroid.com.evil.example/a', 'https://user@wanandroid.com/a', 'https://wanandroid.com:8443/a']) {
    assert.equal((await fetch(`${base}/image?url=${encodeURIComponent(url)}`)).status, 403, url);
  }
  const image = await fetch(`${base}/image?url=${encodeURIComponent('http://www.wanandroid.com/a.png')}`, { headers: { Cookie: 'JSESSIONID=qa' } });
  assert.equal(image.headers.get('content-type'), 'image/png');
  assert.equal(requests[0].url, 'https://www.wanandroid.com/a.png');
  assert.equal(requests[0].headers.Cookie, undefined);
  assert.equal((await fetch(`${base}/image?url=https://wanandroid.com/a`, { method: 'POST' })).status, 405);
});
