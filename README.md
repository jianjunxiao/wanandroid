# WanAndroid · Compose Multiplatform

基于 [WanAndroid 开放 API](https://wanandroid.com/) 的 Kotlin 客户端，支持 **Android、iOS、HarmonyOS 和 Web**。文章浏览、分类、搜索、收藏、分享、积分和设置共用一套 Compose UI 与业务代码。

项目经历了 **View/XML → Jetpack Compose → Compose Multiplatform** 两次迁移，迁移实现、构建和本地回归由 Codex 执行。当前保留原有页面风格、导航与列表交互，以及 Android 的渠道配置。

[运行截图](docs/screenshots/README.md) · [快速开始](#快速开始) · [支持与验证](#支持与验证) · [迁移记录](docs/multiplatform-migration.md)

## 运行效果

以下为当前版本在三个模拟器上的实际运行画面。完整图库按相同页面并排展示，覆盖首页各 Tab、体系、发现、导航、搜索、文章、个人页面及夜间模式。

<table>
<tr><th>Android</th><th>iOS</th><th>HarmonyOS</th></tr>
<tr>
<td><img src="docs/screenshots/android/home.png" alt="Android 首页" width="240" /></td>
<td><img src="docs/screenshots/ios/home.jpg" alt="iOS 首页" width="240" /></td>
<td><img src="docs/screenshots/harmony/home.png" alt="HarmonyOS 首页" width="240" /></td>
</tr>
<tr>
<td><img src="docs/screenshots/android/project.png" alt="Android 项目分类" width="240" /></td>
<td><img src="docs/screenshots/ios/project.jpg" alt="iOS 项目分类" width="240" /></td>
<td><img src="docs/screenshots/harmony/project.png" alt="HarmonyOS 项目分类" width="240" /></td>
</tr>
<tr>
<td><img src="docs/screenshots/android/night-home.png" alt="Android 夜间首页" width="240" /></td>
<td><img src="docs/screenshots/ios/night-home.jpg" alt="iOS 夜间首页" width="240" /></td>
<td><img src="docs/screenshots/harmony/night-home.png" alt="HarmonyOS 夜间首页" width="240" /></td>
</tr>
</table>

**[查看三端完整截图与采集说明 →](docs/screenshots/README.md)**

<details>
<summary>Web 运行画面</summary>

Web 使用相同的 Compose 页面，通过 Wasm 运行；API 和图片请求由同源服务代理。

<img src="docs/verification/web-home.png" alt="Web 首页" width="280" />
<img src="docs/verification/web-project.png" alt="Web 项目分类" width="280" />

</details>

## 功能

| 模块 | 功能 |
| --- | --- |
| 首页 | 热门、最新、广场、项目、公众号；分类切换、刷新、分页、回顶 |
| 体系 | 一级/二级分类、分类选择、分类文章列表 |
| 发现与导航 | Banner、热搜、常用网站、分类站点、文章跳转 |
| 搜索与阅读 | 关键词搜索、搜索历史、结果分页、内嵌网页、阅读历史及长按删除 |
| 账号与个人 | 登录、注册、积分、排行、我的收藏、我的分享、分享提交与删除 |
| 设置 | 日夜间模式、文章字体大小、缓存清理、版本信息、关于和开源列表 |

## 快速开始

### 环境

| 平台 | 所需环境 |
| --- | --- |
| 公共构建 | JDK 17；使用仓库提供的 Gradle Wrapper |
| Android | Android SDK 36、模拟器或设备；在本机 `local.properties` 中设置 SDK 路径 |
| iOS | Apple Silicon Mac、Xcode、iOS Simulator；当前提供 `iosArm64` 和 `iosSimulatorArm64` |
| HarmonyOS | DevEco Studio、HarmonyOS SDK、ohpm、hvigor；配置 `OHOS_SDK_HOME` 与 `DEVECO_SDK_HOME` |
| Web | Node.js 22+、支持 Wasm GC 的浏览器 |

以下命令从仓库根目录执行。首次构建需要下载依赖；各平台 SDK 和签名属于本机配置。

### Android

```bash
./gradlew help
./gradlew :androidApp:assembleEnterpriseAlphaDebug
adb install -r androidApp/build/outputs/apk/enterpriseAlpha/debug/wandroid_enterprise_alpha_v1.0.6_20260514.apk
adb shell am start -n com.xiaojianjun.wanandroid/.ui.compose.MainComposeActivity
```

也可使用 Android Studio 运行 `androidApp`。原有渠道与环境 flavor 保留；当前版本不提供旧版持久化数据迁移。

### iOS

```bash
open iosApp/iosApp.xcodeproj
```

选择 `iosApp` scheme 和 iOS 模拟器运行。Xcode 构建阶段会自动生成、链接共享 framework 并同步 Compose 资源。模拟器无需开发团队签名；真机/归档需配置 `iosApp/Configuration/Config.xcconfig` 和有效签名。

### HarmonyOS

```bash
./harmonyApp/harmony/gradlew -p harmonyApp/harmony publishDebugBinariesToHarmonyApp
cd harmonyApp
ohpm install
hvigorw --mode module -p product=default -p module=entry@default -p buildMode=debug assembleHap --no-daemon
hdc install -r entry/build/default/outputs/default/entry-default-unsigned.hap
hdc shell aa start -a EntryAbility -b com.xiaojianjun.wanandroid
```

也可在生成共享库后，用 DevEco Studio 打开 `harmonyApp` 运行 `entry`。修改共享源码后，需重新生成共享库，再构建 HAP。上述 Debug HAP 已在模拟器安装验证；真机和发布包需另外配置证书。

### Web

```bash
./gradlew :webApp:wasmJsBrowserDistribution
node webApp/web/server.mjs
```

访问 [http://127.0.0.1:8080](http://127.0.0.1:8080)。服务默认读取 `webApp/build/dist/wasmJs/productionExecutable`，支持通过 `PORT`、`WANANDROID_WEB_DIST` 修改端口和产物目录。

API 代理负责跨域和 HttpOnly Cookie，不能直接以 `file://` 打开页面。公网部署需配置 HTTPS 反向代理；完整命令与配置说明见[构建文档](docs/multiplatform-migration.md#构建与运行)。

## 架构与技术栈

工程按 JetBrains 的[最新模块结构指导](https://kotlinlang.org/docs/multiplatform/multiplatform-project-recommended-structure.html)组织：`shared` 提供共享库，各平台应用模块负责启动与打包。四端共享 Compose UI 和业务逻辑，使用单个共享模块。

页面按功能组织，采用 ViewModel、StateFlow、Repository 的数据流。`model/repository` 负责数据访问，`di/AppContainer` 组装依赖并通过构造参数交给 ViewModel；导航条目负责 ViewModel 生命周期。Ktor 与 kotlinx.serialization 负责 API，Coil 3 负责图片，`expect/actual` 提供平台能力。

```text
shared/src/
├── commonMain/     Compose 页面、资源、主题、Navigation 3、ViewModel、Repository、API
├── commonTest/     状态逻辑、HTML、日期、API 契约及依赖注入回归
├── androidMain/    SharedPreferences、OkHttp、WebView、系统栏
├── iosMain/        NSUserDefaults、Darwin HTTP、WKWebView
├── ohosMain/       文件存储、NetworkKit 桥接、ArkUI 互操作
└── wasmJsMain/     浏览器存储、返回事件、iframe 等平台适配
androidApp/        Android 宿主与 Room 阅读历史存储
iosApp/            SwiftUI 宿主与 Xcode 工程
webApp/            Web 启动入口、静态文件、字体资源和 Wasm 应用打包
└── web/           Node.js 同源 API / 图片代理及测试
harmonyApp/        ArkUI 宿主、N-API、NetworkKit 与 ArkWeb
└── harmony/       CPF-KMP-CMP 独立 Gradle 构建
```

Android、iOS、Web 使用 Kotlin **2.3.20** / Compose Multiplatform **1.10.3**；HarmonyOS 使用 CPF-KMP-CMP 的 Kotlin **2.2.21-1.0.0** / Compose **1.9.2-1.0.0**。

`androidApp` 和 `webApp` 通过 Gradle 依赖 `shared`，`iosApp` 链接它导出的 framework。`shared` 的 Wasm 目标只生成库，浏览器入口和可执行产物由 `webApp` 提供；`webApp/web` 负责静态产物托管和同源代理服务，独立运行。

`harmonyApp/harmony` 直接引用同一份 `shared/src/commonMain` 源码与资源，并将原生库、头文件和资源交给 `harmonyApp/entry` 打包。它保留独立 Gradle 工具链；DevEco Studio 仍打开 `harmonyApp`。平台配套工程收在对应应用目录下是本项目的组织约定，并非官方强制目录。详细版本组合见[依赖说明](docs/multiplatform-migration.md#依赖组合)。

### 构建配置与工具链

四端都有各自的工具链。仓库保存版本声明、构建脚本和工程配置，JDK、SDK、编译器等由开发机安装或构建工具下载。

| 平台 | 仓库中的配置与构建流程 | 本机工具 |
| --- | --- | --- |
| Android | 根 Gradle 构建 `shared` 与 `androidApp`，生成 APK；配置位于各模块的 `build.gradle.kts` | JDK、Android SDK |
| iOS | `iosApp` 的 Xcode 构建阶段调用根 Gradle 的 `:shared:embedAndSignAppleFrameworkForXcode`，再由 Xcode 编译 SwiftUI 宿主并链接 framework | JDK、Kotlin/Native、Xcode 自带的 Swift 编译器和 iOS SDK |
| Web | 根 Gradle 将 `shared` 与 `webApp` 编译为 Wasm 应用；`webApp/web/server.mjs` 通过 Node.js 托管产物并提供同源代理 | JDK、构建插件管理的 Node.js 等依赖；运行代理服务另需本机 Node.js |
| HarmonyOS | `harmonyApp/harmony` 的独立 Gradle 编译共享源码，再由 `harmonyApp` 中的 Hvigor/ArkTS 工程生成 HAP | JDK、CPF Kotlin/Native、DevEco Studio、HarmonyOS SDK、ohpm、Hvigor |

Android、iOS、Web 的 Kotlin/Compose 构建使用同一套版本，由根目录的 [Gradle Wrapper](gradle/wrapper/gradle-wrapper.properties) 和 [版本目录](gradle/libs.versions.toml)管理，因此无需额外建立独立 Gradle 工程。iOS 的原生宿主仍有独立的 [Xcode 工程](iosApp/iosApp.xcodeproj/project.pbxproj)。

HarmonyOS 使用 CPF-KMP-CMP 的 OHOS 适配发行版，Kotlin/Compose 版本与主构建不同，Gradle 也分别使用 **8.14.3** 和 **9.4.1**。因此本项目在 [harmonyApp/harmony](harmonyApp/harmony/) 中隔离版本与构建配置。该目录不存放编译器本体；`webApp/web` 则是运行服务目录，Web 编译仍由根 Gradle 完成。

默认安装与缓存位置如下，实际路径以本机配置为准：

- **Gradle 与插件**：Wrapper 下载到 `~/.gradle/wrapper/dists`，插件和依赖缓存在 `~/.gradle/caches`；可通过 `GRADLE_USER_HOME` 更改根目录。
- **Kotlin/Native**：编译器及原生依赖通常位于 `~/.konan`，iOS 与 HarmonyOS 使用各自版本的发行包。
- **JDK 与 Android SDK**：由 IDE 或开发者安装；JDK 通过 `JAVA_HOME` 或 IDE 的 Gradle JDK 设置选择，Android SDK 通过本机 `local.properties` 的 `sdk.dir` 指定。
- **Xcode 工具链**：随 Xcode 安装；使用 `xcode-select -p` 查看当前开发者目录，Swift 编译器与 iOS SDK 位于该目录下。
- **HarmonyOS 工具链**：由 DevEco Studio/SDK 管理器安装；本项目构建使用 `OHOS_SDK_HOME`、`DEVECO_SDK_HOME` 和本机命令路径定位相关工具。

## 支持与验证

截至 **2026-09-07**，Android APK、iOS 模拟器应用、HarmonyOS 原生库/HAP 和 Web Wasm 产物均已构建并运行验证。公开页面使用真实 API；账号及服务端写操作目前仅完成浏览器隔离模拟接口回归，四端真实账号闭环尚待专用测试账号验证。

| 能力 | Android | iOS | HarmonyOS | Web |
| --- | --- | --- | --- | --- |
| 主页面、搜索、详情、历史、主题 | 已实测 | 已实测 | 已实测 | 已实测 |
| 文章字号 | 文字缩放 | 整页缩放 | 文字缩放 | 整页缩放 |
| 图片缓存清理 | 支持 | 支持 | 支持 | 支持应用可控缓存 |
| GIF 动画 / 视频帧解码 | 已接入 | 未接入 | 未接入 | 未接入 |
| 禁止 iframe 的文章 | 原生网页容器 | 原生网页容器 | 原生网页容器 | 外部打开 |

各端缓存统计与清理范围不同；Web 弹窗关闭后还存在上游无障碍树恢复问题。当前不宣称四端像素或能力完全一致，完整差异、实测设备和未验证范围见[验证记录](docs/multiplatform-migration.md#验证结果)。

回归命令：

```bash
./gradlew :shared:testAndroidHostTest :androidApp:testEnterpriseAlphaDebugUnitTest
node --test webApp/web/server.test.mjs
```

已通过 **35 项共享测试、1 项 Android 宿主测试、6 项 Web 代理测试**。构建和测试记录、平台差异以及模拟器回归范围均保留在迁移文档中。

## 迁移与维护

- [迁移架构、平台适配与复审记录](docs/multiplatform-migration.md)
- [三端截图、采集环境与页面范围](docs/screenshots/README.md)
- [依赖升级、版本同步与发布注意事项](docs/multiplatform-migration.md#维护与发布)

原 View/XML 时期的图片保留在 `images/`，当前 README 展示迁移后的运行画面。

## 致谢

- [WanAndroid](https://wanandroid.com/)：开放 API 与内容。
- [Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform)、[CPF-KMP-CMP](https://gitcode.com/CPF-KMP-CMP)：跨平台 UI 与鸿蒙适配。
- [Ktor](https://github.com/ktorio/ktor)、[Coil](https://github.com/coil-kt/coil)：网络与图片加载。
- [Noto CJK](https://github.com/notofonts/noto-cjk)、[Noto Emoji](https://github.com/googlefonts/noto-emoji)：Web 字体；对应 OFL 许可证随 [Web 资源](webApp/src/wasmJsMain/resources/licenses/) 一起提供。
