#include "libwanandroid_api.h"
#include "napi/native_api.h"
#include <vector>

static bool ReadString(napi_env env, napi_value value, std::vector<char>& result) {
    size_t length = 0;
    if (napi_get_value_string_utf8(env, value, nullptr, 0, &length) != napi_ok) return false;
    result.resize(length + 1);
    return napi_get_value_string_utf8(env, value, result.data(), result.size(), &length) == napi_ok;
}

static napi_value CreateController(napi_env env, napi_callback_info info) {
    size_t argc = 3;
    napi_value args[3];
    napi_get_cb_info(env, info, &argc, args, nullptr, nullptr);
    std::vector<char> filesDir;
    std::vector<char> cacheDir;
    napi_valuetype bridgeType;
    if (argc != 3 || !ReadString(env, args[0], filesDir) || !ReadString(env, args[1], cacheDir) ||
        napi_typeof(env, args[2], &bridgeType) != napi_ok || bridgeType != napi_object) {
        napi_throw_type_error(env, nullptr, "filesDir、cacheDir 和宿主桥接对象不能为空");
        return nullptr;
    }
    return reinterpret_cast<napi_value>(WanMainArkUIViewController(env, filesDir.data(), cacheDir.data(), args[2]));
}

EXTERN_C_START
/** 注册 Compose 互操作及控制器入口；HTTP 请求由共享库中的 Ktor 引擎直接执行。 */
static napi_value Init(napi_env env, napi_value exports) {
    androidx_compose_ui_arkui_init(env, exports);
    napi_property_descriptor desc[] = {
        {"createController", nullptr, CreateController, nullptr, nullptr, nullptr, napi_default, nullptr},
    };
    napi_define_properties(env, exports, sizeof(desc) / sizeof(desc[0]), desc);
    return exports;
}
EXTERN_C_END

static napi_module entryModule = {
    .nm_version = 1,
    .nm_flags = 0,
    .nm_filename = nullptr,
    .nm_register_func = Init,
    .nm_modname = "entry",
    .nm_priv = nullptr,
    .reserved = { 0 },
};

extern "C" __attribute__((constructor)) void RegisterEntryModule() {
    napi_module_register(&entryModule);
}
