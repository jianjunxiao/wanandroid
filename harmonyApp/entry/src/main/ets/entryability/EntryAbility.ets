import { UIAbility } from '@kit.AbilityKit';
import { hilog } from '@kit.PerformanceAnalysisKit';
import { window } from '@kit.ArkUI';
import { registerComposeInteropBuilder } from '@cpf-kmp-cmp/compose/src/main/ets/compose/ArkUIView';
import { articleWebBuilder } from '../components/ArticleWeb';

export default class EntryAbility extends UIAbility {
  onWindowStageCreate(windowStage: window.WindowStage): void {
    registerComposeInteropBuilder('WanArticleWeb', articleWebBuilder);
    const mainWindow = windowStage.getMainWindowSync();
    AppStorage.setOrCreate('mainWindow', mainWindow);
    mainWindow.setWindowLayoutFullScreen(true).then(() => {
      windowStage.loadContent('pages/Index', (error) => {
        if (error.code) hilog.error(0, 'WanAndroid', '加载页面失败: %{public}s', error.message);
      });
    }).catch((error: Error) => {
      hilog.error(0, 'WanAndroid', '初始化窗口失败: %{public}s', error.message);
    });
  }

  onWindowStageDestroy(): void {
    AppStorage.delete('mainWindow');
  }
}
