import http from 'node:http';
import https from 'node:https';
import fs from 'node:fs';
import path from 'node:path';
import { fileURLToPath } from 'node:url';

const workspace = path.resolve(path.dirname(fileURLToPath(import.meta.url)), '../..');
const publicDir = path.resolve(workspace, process.env.WANANDROID_WEB_DIST || 'webApp/build/dist/wasmJs/productionExecutable');
const port = Number(process.env.PORT || 8080);
const apiPaths = /^(?:article\/(?:listproject\/\d+|top|list\/\d+|query\/\d+)\/json|user_article\/list\/\d+\/json|tree\/json|project\/(?:tree|list\/\d+)\/json|wxarticle\/(?:chapters|list\/\d+\/\d+)\/json|navi\/json|banner\/json|hotkey\/json|friend\/json|user\/(?:login|register|logout\/json|lg\/private_articles\/\d+\/json)|lg\/(?:collect\/\d+\/json|uncollect_originId\/\d+\/json|coin\/userinfo\/json|coin\/list\/\d+\/json|collect\/list\/\d+\/json|user_article\/(?:add|delete\/\d+)\/json)|coin\/rank\/\d+\/json)$/;
const imageDomains = ['wanandroid.com', 'qpic.cn', 'gtimg.com', 'githubusercontent.com', 'csdnimg.cn', 'byteimg.com', 'myqcloud.com'];
const mimeTypes = { '.html': 'text/html; charset=utf-8', '.js': 'text/javascript; charset=utf-8', '.mjs': 'text/javascript; charset=utf-8', '.wasm': 'application/wasm', '.css': 'text/css', '.json': 'application/json', '.png': 'image/png', '.jpg': 'image/jpeg', '.svg': 'image/svg+xml', '.ttf': 'font/ttf', '.otf': 'font/otf', '.woff2': 'font/woff2', '.xml': 'application/xml' };

function error(res, status, message) {
  res.writeHead(status, { 'Content-Type': 'application/json; charset=utf-8' });
  res.end(JSON.stringify({ errorCode: status, errorMsg: message, data: null }));
}

function sameOrigin(req) {
  return !req.headers.origin || req.headers.origin === `http://${req.headers.host}` || req.headers.origin === `https://${req.headers.host}`;
}

function localCookie(cookie, req) {
  let value = cookie.replace(/;\s*Domain=[^;]+/gi, '').replace(/;\s*Path=[^;]+/gi, '; Path=/');
  if (!/;\s*Path=/i.test(value)) value += '; Path=/';
  // 开发服务器只监听回环地址；HTTPS 反向代理通过此请求头保留 Secure。
  const secure = req.socket.encrypted || req.headers['x-forwarded-proto'] === 'https';
  if (!secure) value = value.replace(/;\s*Secure\b/gi, '');
  return value;
}

function proxy(req, res, target, image = false) {
  const headers = { 'User-Agent': 'WanAndroid-Multiplatform', Accept: image ? 'image/*' : 'application/json' };
  if (!image && req.headers.cookie) headers.Cookie = req.headers.cookie;
  if (!image && req.headers['content-type']) headers['Content-Type'] = req.headers['content-type'];
  const upstream = https.request(target, { method: image ? 'GET' : req.method, headers, timeout: 15000 }, response => {
    const responseHeaders = { 'Content-Type': response.headers['content-type'] || 'application/octet-stream', 'X-Content-Type-Options': 'nosniff' };
    if (response.headers['content-encoding']) responseHeaders['Content-Encoding'] = response.headers['content-encoding'];
    if (!image && response.headers['set-cookie']) {
      // 同源代理 Cookie 只属于本站；HttpOnly、SameSite 等标记保持生效。
      responseHeaders['Set-Cookie'] = response.headers['set-cookie'].map(cookie => localCookie(cookie, req));
    }
    res.writeHead(response.statusCode || 502, responseHeaders);
    response.on('error', () => res.destroy());
    response.pipe(res);
  });
  upstream.on('timeout', () => upstream.destroy(new Error('upstream timeout')));
  upstream.on('error', () => { if (!res.headersSent) error(res, 502, '网络请求失败'); else res.destroy(); });
  req.on('aborted', () => upstream.destroy());
  if (image) upstream.end(); else req.pipe(upstream);
}

export function createServer({ staticDir = publicDir } = {}) {
  staticDir = path.resolve(staticDir);
  return http.createServer((req, res) => {
    let url;
    try { url = new URL(req.url, 'http://localhost'); } catch { return error(res, 400, '请求地址无效'); }
    if (url.pathname.startsWith('/api/')) {
      if (!sameOrigin(req)) return error(res, 403, '请求来源无效');
      if (!['GET', 'POST'].includes(req.method)) return error(res, 405, '请求方法无效');
      const route = url.pathname.slice(5);
      if (route === 'session/clear' && req.method === 'POST') {
        const names = (req.headers.cookie || '').split(';').map(cookie => cookie.trim().split('=')[0]).filter(name => /^[\w-]+$/.test(name));
        res.writeHead(200, { 'Content-Type': 'application/json', 'Set-Cookie': names.map(name => `${name}=; Path=/; Max-Age=0; HttpOnly; SameSite=Lax`) });
        return res.end('{"errorCode":0,"errorMsg":"","data":null}');
      }
      if (!apiPaths.test(route)) return error(res, 404, '接口不存在');
      return proxy(req, res, new URL(`https://wanandroid.com/${route}${url.search}`));
    }
    if (url.pathname === '/image') {
      if (req.method !== 'GET') return error(res, 405, '请求方法无效');
      let image;
      try { image = new URL(url.searchParams.get('url')); } catch { return error(res, 400, '图片地址无效'); }
      if (!['http:', 'https:'].includes(image.protocol) || image.username || image.password || image.port ||
          !imageDomains.some(domain => image.hostname === domain || image.hostname.endsWith(`.${domain}`))) return error(res, 403, '图片地址不受支持');
      image.protocol = 'https:';
      return proxy(req, res, image, true);
    }
    if (!['GET', 'HEAD'].includes(req.method)) return error(res, 405, '请求方法无效');
    let pathname;
    try { pathname = decodeURIComponent(url.pathname); } catch { return error(res, 400, '请求地址无效'); }
    if (pathname.includes('\0')) return error(res, 400, '请求地址无效');
    const target = path.resolve(staticDir, `.${pathname === '/' ? '/index.html' : pathname}`);
    if (!target.startsWith(staticDir + path.sep)) return error(res, 403, '请求地址无效');
    fs.stat(target, (err, stat) => {
      if (err || !stat.isFile()) return error(res, 404, '文件不存在');
      res.writeHead(200, { 'Content-Type': mimeTypes[path.extname(target)] || 'application/octet-stream', 'Content-Length': stat.size, 'X-Content-Type-Options': 'nosniff' });
      if (req.method === 'HEAD') return res.end();
      fs.createReadStream(target).on('error', () => res.destroy()).pipe(res);
    });
  });
}

if (process.argv[1] && path.resolve(process.argv[1]) === fileURLToPath(import.meta.url)) {
  createServer().listen(port, '127.0.0.1', () => console.log(`玩安卓：http://127.0.0.1:${port}`));
}
