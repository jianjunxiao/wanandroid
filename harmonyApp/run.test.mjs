import test from 'node:test';
import assert from 'node:assert/strict';
import { spawnSync } from 'node:child_process';
import { copyFileSync, mkdirSync, mkdtempSync, readFileSync, rmSync, writeFileSync } from 'node:fs';
import { tmpdir } from 'node:os';
import path from 'node:path';

/** 在含空格的独立目录中模拟根 Wrapper 和设备工具，验证部署顺序与错误传播。 */
function runFixture(t, options = {}, input = '') {
  const root = mkdtempSync(path.join(tmpdir(), 'wanandroid-harmony-run-'));
  t.after(() => rmSync(root, { recursive: true, force: true }));
  const appDir = path.join(root, 'harmony app');
  const binDir = path.join(root, 'bin');
  const outputDir = path.join(appDir, 'entry/build/default/outputs/default');
  mkdirSync(appDir, { recursive: true });
  mkdirSync(binDir);
  mkdirSync(outputDir, { recursive: true });
  copyFileSync(new URL('./run.sh', import.meta.url), path.join(appDir, 'run.sh'));
  for (const kind of options.staleArtifacts ?? []) {
    writeFileSync(path.join(outputDir, `entry-default-${kind}.hap`), '旧产物');
  }

  const tool = `#!/usr/bin/env node
const fs = require('node:fs');
const path = require('node:path');
const options = JSON.parse(process.env.RUN_TEST_OPTIONS);
const command = path.basename(process.argv[1]);
const args = process.argv.slice(2);
fs.appendFileSync(process.env.RUN_TEST_LOG, JSON.stringify({ command, args }) + '\\n');
if (command === 'gradlew') {
  process.exit(options.gradleExit ?? 0);
} else if (command === 'hvigorw') {
  if (options.buildExit) process.exit(options.buildExit);
  if (options.artifact !== 'none') {
    const kind = options.artifact ?? 'signed';
    fs.writeFileSync('entry/build/default/outputs/default/entry-default-' + kind + '.hap', '新产物');
  }
} else if (command === 'hdc') {
  if (args[0] === 'list') {
    if (options.listExit) process.exit(options.listExit);
    for (const device of options.devices ?? [{ id: 'phone', state: 'Connected' }]) {
      console.log(device.id + '\\tUSB\\t' + device.state + '\\tlocalhost\\thdc');
    }
  } else if (args[2] === 'install') {
    console.log(options.installOutput ?? '[Info] msg:install bundle successfully.\\nAppMod finish');
    process.exit(options.installExit ?? 0);
  } else if (args[3] === 'aa') {
    console.log(options.startOutput ?? 'start ability successfully.');
    process.exit(options.startExit ?? 0);
  } else if (args[3] === 'param') {
    console.log('测试设备 ' + args[1]);
  }
}
`;
  for (const command of ['java', 'ohpm', 'hvigorw', 'hdc']) {
    writeFileSync(path.join(binDir, command), tool, { mode: 0o755 });
  }
  writeFileSync(path.join(root, 'gradlew'), tool, { mode: 0o755 });
  const logFile = path.join(root, 'commands.jsonl');
  writeFileSync(logFile, '');
  const env = {
    ...process.env,
    PATH: `${binDir}${path.delimiter}${process.env.PATH}`,
    OHOS_SDK_HOME: root,
    DEVECO_SDK_HOME: root,
    RUN_TEST_OPTIONS: JSON.stringify(options),
    RUN_TEST_LOG: logFile,
  };
  delete env.HDC_DEVICE_ID;
  if (options.target) env.HDC_DEVICE_ID = options.target;
  const result = spawnSync('/bin/sh', [path.join(appDir, 'run.sh')], {
    cwd: root, env, input, encoding: 'utf8', timeout: 10000,
  });
  assert.ifError(result.error);
  const commands = readFileSync(logFile, 'utf8').trim().split('\n').filter(Boolean).map(JSON.parse);
  return { ...result, commands, output: result.stdout + result.stderr };
}

function installCommands(result) {
  return result.commands.filter(({ command, args }) => command === 'hdc' && args[2] === 'install');
}

function startCommands(result) {
  return result.commands.filter(({ command, args }) => command === 'hdc' && args[3] === 'aa');
}

test('多设备允许输入序号，并排除离线设备', t => {
  const result = runFixture(t, { devices: [
    { id: 'offline', state: 'Offline' },
    { id: 'emulator', state: 'Connected' },
    { id: 'phone', state: 'Connected' },
  ] }, '无效\n3\n2\n');
  assert.equal(result.status, 0, result.output);
  assert.match(result.stderr, /设备序号无效/);
  assert.equal(installCommands(result)[0].args[1], 'phone');
  assert.equal(startCommands(result)[0].args[1], 'phone');
});

test('HDC_DEVICE_ID 可固定选择设备并跳过交互', t => {
  const result = runFixture(t, { target: 'phone', devices: [
    { id: 'emulator', state: 'Connected' }, { id: 'phone', state: 'Connected' },
  ] });
  assert.equal(result.status, 0, result.output);
  assert.doesNotMatch(result.stdout, /请输入设备序号/);
  assert.equal(installCommands(result)[0].args[1], 'phone');
});

for (const [name, options, input] of [
  ['无设备', { devices: [] }, ''],
  ['仅有离线设备', { devices: [{ id: 'offline', state: 'Offline' }] }, ''],
  ['指定设备未连接', { target: 'missing' }, ''],
  ['设备列表读取失败', { listExit: 1 }, ''],
  ['多设备输入结束', { devices: [{ id: 'a', state: 'Connected' }, { id: 'b', state: 'Connected' }] }, ''],
  ['用户取消选择', { devices: [{ id: 'a', state: 'Connected' }, { id: 'b', state: 'Connected' }] }, 'q\n'],
]) {
  test(`${name}时在构建和安装前停止`, t => {
    const result = runFixture(t, options, input);
    assert.notEqual(result.status, 0, result.output);
    assert.equal(result.commands.some(({ command }) => command === 'ohpm'), false);
    assert.equal(installCommands(result).length, 0);
  });
}

for (const installExit of [0, 7]) {
  test(`安装失败且 hdc 返回 ${installExit} 时，不再启动应用`, t => {
    const result = runFixture(t, {
      installExit,
      installOutput: '[Info] msg:error: failed to install bundle. code:9568320 error: no signature file.\nAppMod finish',
    });
    assert.notEqual(result.status, 0, result.output);
    assert.match(result.output, /9568320/);
    assert.equal(startCommands(result).length, 0);
  });
}

test('启动失败且 hdc 返回 0 时，脚本仍以失败结束', t => {
  const result = runFixture(t, { startOutput: 'error: failed to start ability. Error Code:10104001' });
  assert.notEqual(result.status, 0, result.output);
  assert.match(result.output, /10104001/);
});

test('真机锁屏阻止启动时提示手动解锁，并以失败结束', t => {
  const result = runFixture(t, { startOutput: 'error: failed to start ability. Error Code:10106102' });
  assert.notEqual(result.status, 0, result.output);
  assert.match(result.stderr, /手动解锁并保持亮屏/);
});

test('单设备自动选择，并安装本次生成的已签名 HAP', t => {
  const result = runFixture(t);
  assert.equal(result.status, 0, result.output);
  assert.match(installCommands(result)[0].args[4], /entry-default-signed\.hap$/);
  assert.equal(startCommands(result).length, 1);
  // 鸿蒙运行入口必须调用根 shared，不能依赖已移除的嵌套工程。
  assert.deepEqual(result.commands.find(({ command }) => command === 'gradlew').args,
    ['-p', '..', ':shared:publishDebugBinariesToHarmonyApp']);
});

test('shared 编译失败时不继续打包、安装或启动宿主', t => {
  const result = runFixture(t, { gradleExit: 1 });
  assert.notEqual(result.status, 0, result.output);
  assert.equal(result.commands.some(({ command }) => command === 'hvigorw'), false);
  assert.equal(installCommands(result).length, 0);
  assert.equal(startCommands(result).length, 0);
});

test('取消签名配置后不会误装残留签名包，且说明真机签名要求', t => {
  const result = runFixture(t, { artifact: 'unsigned', staleArtifacts: ['signed'] });
  assert.equal(result.status, 0, result.output);
  assert.match(installCommands(result)[0].args[4], /entry-default-unsigned\.hap$/);
  assert.match(result.stderr, /真机请先用 DevEco Studio/);
});

test('构建未生成 HAP 时，即使存在旧产物也不安装', t => {
  const result = runFixture(t, { artifact: 'none', staleArtifacts: ['signed', 'unsigned'] });
  assert.notEqual(result.status, 0, result.output);
  assert.equal(installCommands(result).length, 0);
});

test('构建失败时不安装或启动应用', t => {
  const result = runFixture(t, { buildExit: 1, staleArtifacts: ['signed'] });
  assert.notEqual(result.status, 0, result.output);
  assert.equal(installCommands(result).length, 0);
  assert.equal(startCommands(result).length, 0);
});
