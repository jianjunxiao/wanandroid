import { ArkUIViewController } from '@cpf-kmp-cmp/compose/src/main/cpp/types/libcompose_arkui_utils';

export interface HostBridge {
  request: (id: number, request: string) => void;
  cancelRequest: (id: number) => void;
  dark: () => void;
  light: () => void;
  exit: () => void;
}

export const createController: (filesDir: string, cacheDir: string, bridge: HostBridge) => ArkUIViewController;

export const completeHttp: (id: number, response: string) => void;
