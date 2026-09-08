import { ArkUIViewController } from '@cpf-kmp-cmp/compose/src/main/cpp/types/libcompose_arkui_utils';

/** 共享 Compose 页面需要的宿主能力，网络请求由 Kotlin/Native 内的 Ktor 引擎处理。 */
export interface HostBridge {
  dark: () => void;
  light: () => void;
  exit: () => void;
}

export const createController: (filesDir: string, cacheDir: string, bridge: HostBridge) => ArkUIViewController;
