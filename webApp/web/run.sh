#!/bin/sh
set -eu

workspace_dir=$(CDPATH= cd -- "$(dirname -- "$0")/../.." && pwd)
cd "$workspace_dir"

if ! command -v node >/dev/null 2>&1; then
    printf '%s\n' '未找到 Node.js，请安装 Node.js 22+ 并配置 PATH。' >&2
    exit 1
fi

# Web 使用自身的 JetBrains Wrapper，避免进入移动端 CPF 构建。
./webApp/gradlew -p webApp wasmJsBrowserDistribution
exec node webApp/web/server.mjs
