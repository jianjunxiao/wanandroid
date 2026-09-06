import { webview } from '@kit.ArkWeb';

interface WebParameters {
  url: string;
  textZoom: number;
}

@Component
struct ArticleWeb {
  @Require @Prop params: WebParameters;
  private controller: webview.WebviewController = new webview.WebviewController();

  build() {
    Web({ src: this.params.url, controller: this.controller })
      .javaScriptAccess(true)
      .domStorageAccess(true)
      .textZoomRatio(this.params.textZoom)
      .width('100%')
      .height('100%')
  }
}

@Builder
export function articleWebBuilder(params: object) {
  ArticleWeb({ params: params as WebParameters })
}
