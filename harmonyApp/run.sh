#!/bin/sh
set -eu

harmony_app_dir=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
cd "$harmony_app_dir"

for required_tool in java node ohpm hvigorw hdc; do
    if ! command -v "$required_tool" >/dev/null 2>&1; then
        printf '未找到 %s，请安装 DevEco 工具链并配置 PATH。\n' "$required_tool" >&2
        exit 1
    fi
done

if [ -z "${OHOS_SDK_HOME:-}" ] || [ -z "${DEVECO_SDK_HOME:-}" ]; then
    printf '%s\n' '请先配置 OHOS_SDK_HOME 和 DEVECO_SDK_HOME。' >&2
    exit 1
fi

device_list=$(hdc list targets -v)
connected_targets=$(printf '%s\n' "$device_list" | awk '$3 == "Connected" { print $1 }')
if [ -z "$connected_targets" ]; then
    printf '%s\n' '没有已连接的鸿蒙设备，请启动模拟器或连接并授权真机 USB 调试。' >&2
    exit 1
fi

harmony_target=${HDC_DEVICE_ID:-}
if [ -z "$harmony_target" ]; then
    device_count=$(printf '%s\n' "$connected_targets" | awk 'END { print NR }')
    if [ "$device_count" -eq 1 ]; then
        harmony_target=$connected_targets
    else
        printf '%s\n' '已连接的鸿蒙设备：'
        device_index=0
        while IFS= read -r device_id; do
            device_index=$((device_index + 1))
            device_name=$(hdc -t "$device_id" shell param get const.product.name </dev/null 2>/dev/null | tr -d '\r')
            printf '  %s) %s  %s\n' "$device_index" "$device_id" "$device_name"
        done <<EOF
$connected_targets
EOF

        while [ -z "$harmony_target" ]; do
            printf '请输入设备序号（1-%s），或 q 取消：' "$device_count"
            if ! IFS= read -r selection; then
                printf '\n%s\n' '无法读取设备选择，请在运行配置中设置 HDC_DEVICE_ID 后重试。' >&2
                exit 1
            fi
            case "$selection" in
                q|Q) exit 130 ;;
                ''|*[!0-9]*) ;;
                *) harmony_target=$(printf '%s\n' "$connected_targets" | awk -v chosen="$selection" 'NR == chosen { print; exit }') ;;
            esac
            if [ -z "$harmony_target" ]; then
                printf '%s\n' '设备序号无效，请重新输入。' >&2
            fi
        done
    fi
fi

if ! printf '%s\n' "$connected_targets" | awk -v target="$harmony_target" '$0 == target { found = 1 } END { exit !found }'; then
    printf '设备 %s 尚未连接，请检查 hdc list targets。\n' "$harmony_target" >&2
    exit 1
fi
printf '运行设备：%s\n' "$harmony_target"

run_hdc_checked() (
    success_message=$1
    shift
    if ! command_output=$(hdc -t "$harmony_target" "$@" 2>&1); then
        printf '%s\n' "$command_output" >&2
        return 1
    fi
    printf '%s\n' "$command_output"
    # hdc 在设备端命令失败时也可能返回 0，必须核对实际执行结果。
    case "$command_output" in
        *"$success_message"*) return 0 ;;
        *10106102*) printf '%s\n' '手机处于锁屏状态，请手动解锁并保持亮屏后重新运行。' >&2 ;;
        *) printf '%s\n' '设备操作未成功，已停止运行。请检查上方 hdc 错误信息。' >&2 ;;
    esac
    return 1
)

ohpm install
# 共享库由根工程的 shared 编译，Hvigor 只负责下方的宿主打包与签名。
../gradlew -p .. :shared:publishDebugBinariesToHarmonyApp

signed_hap=entry/build/default/outputs/default/entry-default-signed.hap
unsigned_hap=entry/build/default/outputs/default/entry-default-unsigned.hap
# 重新生成安装产物，避免取消签名配置后误装上次构建的旧包。
rm -f "$signed_hap" "$unsigned_hap"
hvigorw --mode module -p product=default -p module=entry@default -p buildMode=debug assembleHap --no-daemon

if [ -f "$signed_hap" ]; then
    hap_file=$signed_hap
elif [ -f "$unsigned_hap" ]; then
    hap_file=$unsigned_hap
    printf '%s\n' \
        '本次未生成签名 HAP，未签名包仅用于支持未签名安装的模拟器。' \
        '真机请先用 DevEco Studio 打开 harmonyApp，在 File → Project Structure → Project → Signing Configs 中完成自动签名。' >&2
else
    printf '%s\n' '未找到本次构建的 HAP，请检查 Hvigor 构建输出。' >&2
    exit 1
fi

printf '安装产物：%s\n' "$hap_file"
run_hdc_checked 'install bundle successfully.' install -r "$hap_file"
run_hdc_checked 'start ability successfully.' shell aa start -a EntryAbility -b com.xiaojianjun.wanandroid
