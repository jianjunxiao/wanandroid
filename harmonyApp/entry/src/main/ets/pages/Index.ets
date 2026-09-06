import { ArkUIViewController, Compose } from '@cpf-kmp-cmp/compose';
import { common } from '@kit.AbilityKit';
import { hilog } from '@kit.PerformanceAnalysisKit';
import { window } from '@kit.ArkUI';
import nativeApi, { HostBridge } from 'libentry.so';
import { HttpService } from '../network/HttpService';

@Entry
@Component
struct Index {
  private controller?: ArkUIViewController;
  private http: HttpService = new HttpService((id: number, response: string) => nativeApi.completeHttp(id, response));
  private context?: common.UIAbilityContext;
  private hostWindow?: window.Window;

  aboutToAppear(): void {
    this.context = this.getUIContext().getHostContext() as common.UIAbilityContext;
    this.hostWindow = AppStorage.get<window.Window>('mainWindow');
    const bridge: HostBridge = {
      request: (id: number, request: string) => this.http.request(id, request),
      cancelRequest: (id: number) => this.http.cancel(id),
      dark: () => this.setSystemBars(true),
      light: () => this.setSystemBars(false),
      exit: () => this.exit()
    };
    this.controller = nativeApi.createController(this.context.filesDir, this.context.cacheDir, bridge);
  }

  private exit(): void {
    this.context?.terminateSelf().catch((error: Error) => {
      hilog.error(0, 'WanAndroid', '退出失败: %{public}s', error.message);
    });
  }

  private setSystemBars(dark: boolean): void {
    this.hostWindow?.setWindowSystemBarProperties({
      statusBarColor: '#00000000',
      navigationBarColor: '#00000000',
      statusBarContentColor: dark ? '#FFFFFFFF' : '#FF000000',
      navigationBarContentColor: dark ? '#FFFFFFFF' : '#FF000000'
    }).catch((error: Error) => {
      hilog.error(0, 'WanAndroid', '更新系统栏失败: %{public}s', error.message);
    });
  }

  build() {
    Column() {
      if (this.controller) {
        Compose({
          controller: this.controller,
          libraryName: 'entry',
          onBackPressed: () => {
            this.exit();
            return true;
          }
        })
      }
    }
    .width('100%')
    .height('100%')
    .expandSafeArea([SafeAreaType.SYSTEM], [SafeAreaEdge.TOP, SafeAreaEdge.BOTTOM])
  }

  aboutToDisappear(): void { this.http.close(); }

  onPageHide(): void { this.controller?.onPageHide(); }
  onPageShow(): void { this.controller?.onPageShow(); }
  onBackPress(): boolean { return this.controller?.onBackPress() ?? false; }
}
