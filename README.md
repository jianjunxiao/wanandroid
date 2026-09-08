# WanAndroid · Compose Multiplatform

基于 [WanAndroid 开放 API](https://wanandroid.com/) 的 Kotlin 客户端，支持 **Android、iOS、HarmonyOS 和 Web**。文章浏览、分类、搜索、收藏、分享、积分和设置共用一套 Compose UI 与业务代码。

项目经历了 **View/XML → Jetpack Compose → Compose Multiplatform** 两次迁移，迁移实现、构建和本地回归由 Codex 执行。当前保留原有页面风格、导航与列表交互，以及 Android 的渠道配置。

[工程目录](#工程目录) · [运行截图](docs/screenshots/README.md) · [快速开始](#快速开始) · [支持与验证](#支持与验证) · [迁移记录](docs/multiplatform-migration.md)

## 运行效果

以下为 2026-09-06 迁移回归时在三个模拟器上采集的实际运行画面，应用版本为 `1.0.6`。完整图库按相同页面并排展示，覆盖首页各 Tab、体系、发现、导航、搜索、文章、个人页面及夜间模式。

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

移动三端统一构建后的 2026-09-08 项目页截图与回归结果见[本次验证记录](docs/multiplatform-migration.md#移动三端统一构建2026-09-08)。

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
| HarmonyOS | DevEco Studio 26.0.0、配套 HarmonyOS SDK、ohpm、hvigor；配置 `OHOS_SDK_HOME` 与 `DEVECO_SDK_HOME` |
| Web | Node.js 22+、支持 Wasm GC 的浏览器 |

以下命令从仓库根目录执行。首次构建需要下载依赖；各平台 SDK 和签名属于本机配置。

### Android Studio 运行入口

首次打开工程时，在 **Android Studio → Settings → Build, Execution, Deployment → Build Tools → Gradle → Gradle JDK** 中选择本机 **JDK 17**。根工程使用 Gradle 8.14.3，不能使用 JDK 25；如果 Android Studio 自带的 JBR 已升级到 25，需要单独安装或选择 JDK 17。

本项目通过 `GRADLE_LOCAL_JAVA_HOME` 读取 `.gradle/config.properties` 中的 `java.home`，将其设为本机 JDK 17 的安装目录后执行 **Sync Project with Gradle Files**。该文件属于本机配置，不提交绝对路径。命令行构建则通过 `JAVA_HOME` 选择同一套 JDK 17；IDE 设置不会自动改变终端环境变量。配置机制见 [Android 官方 JDK 说明](https://developer.android.com/build/jdks#gradle-jdk)，版本支持范围见 [Gradle Java 兼容表](https://docs.gradle.org/current/userguide/compatibility.html#java_runtime)。

同步工程后，可在顶部运行配置中选择对应应用：

| 入口 | 行为 |
| --- | --- |
| `androidApp` | 构建并运行 Android 应用；使用 Android Studio 的设备选择器 |
| `iosApp` | 通过 Kotlin Multiplatform 插件与 Xcode 工程运行 iOS；也可直接使用 Xcode |
| `webApp` | 构建 Wasm 应用并启动静态页面、API 与图片代理服务；点击控制台中的本地网址访问 |
| `harmonyApp` | 安装 OHPM 依赖、编译共享库、构建 HAP，然后通过 hdc 安装并启动鸿蒙应用 |

Web、鸿蒙入口保存于 [.run](.run/)，使用 IDE 自带的 Shell Script 配置。当前默认 `/bin/zsh -l`，会加载 macOS 的 `~/.zprofile`，因此 SDK 环境变量与工具的 `PATH` 应在该文件配置；其他开发环境可在 **Run → Edit Configurations** 调整解释器。若入口未出现，确认 **Shell Script** 插件已启用，并重新打开运行配置列表。保存方式见 [JetBrains 运行配置说明](https://www.jetbrains.com/help/idea/run-debug-configuration.html)。

`webApp` 的服务在 Run 窗口持续运行，点击 Stop 即可停止；共享代码改动后重新运行会重新构建。默认端口为 8080，可在运行配置的环境变量中设置 `PORT`。

运行 `harmonyApp` 前先启动鸿蒙模拟器，或连接并授权真机 USB 调试。仅连接一个设备时自动选择；连接多个设备时，Run 控制台会列出设备编号、ID 和名称，点击控制台并输入编号后回车，输入 `q` 取消。也可在 **Run → Edit Configurations → harmonyApp → Environment variables** 中设置 `HDC_DEVICE_ID` 固定设备，值取自 `hdc list targets -v` 中处于 `Connected` 状态的设备。

`harmonyApp` 是 Shell Script 入口，不能使用 Android Studio 的 Android 设备下拉框。若需要原生鸿蒙设备选择器或 ArkTS/C++ 断点调试，用 DevEco Studio 打开 `harmonyApp` 并运行 `entry`。

### Android

```bash
./gradlew help
./gradlew :androidApp:assembleEnterpriseAlphaDebug
adb install -r androidApp/build/outputs/apk/enterpriseAlpha/debug/wandroid_enterprise_alpha_v1.0.6_20260514.apk
adb shell am start -n com.xiaojianjun.wanandroid/.ui.compose.MainComposeActivity
```

也可使用 Android Studio 运行 `androidApp`。原有渠道与环境 flavor 保留；当前版本不提供旧版持久化数据迁移。

从旧目录结构更新后，先执行 **Sync Project with Gradle Files**，再在顶部运行配置中选择 `androidApp`，并在 **Build Variants** 中选择 `enterpriseAlphaDebug`。若仍报 `project 'app' not found`，说明 IDE 沿用了旧运行配置：在 **Run → Edit Configurations** 中将 Android 配置的 Module 改为同步后的 `wanandroid.androidApp`，或使用新生成的 `androidApp` 配置。

### iOS

```bash
open iosApp/iosApp.xcodeproj
```

选择 `iosApp` scheme 和 iOS 模拟器运行。Xcode 构建阶段会自动生成共享 framework、同步 Compose 资源，并按内容将静态库同步为 `libWanAndroid.a` 参与链接，确保共享代码改动后重新生成应用。模拟器无需开发团队签名；真机/归档需配置 `iosApp/Configuration/Config.xcconfig` 和有效签名。

### HarmonyOS

```bash
./harmonyApp/run.sh
```

脚本会生成共享库、构建 HAP，并安装到所选设备。优先安装本次生成的 `entry-default-signed.hap`；未配置签名时生成的 `unsigned.hap` 仅适用于允许未签名安装的模拟器。安装失败会立即停止；安装、启动失败均返回非零退出码。

真机首次运行需要按照[华为官方自动签名流程](https://developer.huawei.com/consumer/cn/doc/harmonyos-guides/ide-signing-auto)完成配置：

1. 连接真机并授权 USB 调试，用 DevEco Studio 打开 `harmonyApp`。
2. 进入 **File → Project Structure → Project → Signing Configs**，勾选 **Automatically generate signature**，按提示登录华为开发者账号并完成签名。
3. 确认 `build-profile.json5` 中 `app.products` 的 `default` 产品绑定 `signingConfig: "default"`，名称与 `app.signingConfigs` 中生成的签名方案一致。
4. 解锁真机并保持亮屏，再运行 `harmonyApp`。新增调试设备后，需重新自动签名以更新 Profile 中的设备列表。

签名材料和 `build-profile.json5` 中生成的本机签名字段保留在本地，不提交个人证书、密码和绝对路径。发布包需要另外配置发布证书。工程显式设置 `targetSdkVersion` 为 `26.0.0`，与当前配套 SDK 及此前产物的实际目标版本一致。

也可先执行 `./gradlew :shared:publishDebugBinariesToHarmonyApp`，再在 DevEco Studio 运行 `entry`；修改共享源码后需重新生成共享库。启动脚本的设备选择、签名产物选择和错误处理可运行 `node --test harmonyApp/run.test.mjs` 验证。

### Web

```bash
./webApp/web/run.sh
```

脚本先执行 `./webApp/gradlew -p webApp wasmJsBrowserDistribution`，再启动 `webApp/web/server.mjs`。访问 [http://127.0.0.1:8080](http://127.0.0.1:8080)；服务默认读取 `webApp/build/dist/wasmJs/productionExecutable`，支持通过 `PORT`、`WANANDROID_WEB_DIST` 修改端口和产物目录。

API 代理负责跨域和 HttpOnly Cookie，不能直接以 `file://` 打开页面。公网部署需配置 HTTPS 反向代理；完整命令与配置说明见[构建文档](docs/multiplatform-migration.md#构建与运行)。

## 架构与技术栈

移动三端使用 CPF-KMP-CMP 配套发行版，Android、iOS、HarmonyOS 的共享目标全部配置在 [shared/build.gradle.kts](shared/build.gradle.kts)。各端宿主保留启动和打包职责，符合 JetBrains 的[共享库与应用入口分离指导](https://kotlinlang.org/docs/multiplatform/multiplatform-project-recommended-structure.html)。

四端共用 Compose 页面、资源、主题、ViewModel 和 Repository。CPF 当前发布的 Compose UI 没有 JS/Wasm 变体，因此 Web 在 `webApp` 独立使用 JetBrains 工具链，直接编译同一份 `shared/src/commonMain` 与 `shared/src/wasmJsMain`，不复制业务代码。

页面按功能组织，采用 ViewModel、StateFlow、Repository 的数据流。`model/repository` 负责数据访问，`di/AppContainer` 通过构造参数组装依赖；Navigation 3 管理页面和 ViewModel 生命周期。Ktor 与 kotlinx.serialization 负责 API，Coil 3 负责图片，`expect/actual` 提供平台能力。

移动三端共用 Ktor 请求配置、Cookie 持久化和超时策略，在 `shared` 中分别选择 Android OkHttp、iOS Darwin、HarmonyOS CPF Curl 引擎。鸿蒙使用 `ktor-client-curl:3.3.3-1.0.0` 自带的 OHOS 原生库执行 HTTPS，保留默认的证书链和主机名校验；API 与 Coil 图片复用同一客户端，无需 ArkTS HTTP 服务或 N-API 网络回调。引擎实现见 [CPF Ktor 鸿蒙仓库](https://gitcode.com/CPF-KMP-CMP/ktor/tree/main-3.3.3-OH/ktor-client/ktor-client-curl)。

CPF 当前 Curl 发布版存在取消请求后重复释放句柄的问题，已在模拟器复现关闭客户端崩溃。项目从同版本发布包提取引擎源码，仅替换两个内部文件的[传输与资源清理实现](shared/thirdParty/ktor-curl/README.md)，同时修正响应体阶段的取消监听和 HTTP 版本读取；原生库继续使用 CPF 发布产物。该修复与其余移动目标统一由 `shared/build.gradle.kts` 管理，后续发布版修复后可恢复直接依赖。构建、12 项网络检查及页面截图见[本次验证记录](docs/multiplatform-migration.md#鸿蒙切换-curl2026-09-08)。

### 工程目录

主要源码、宿主与构建入口如下：

```text
wanandroid/
├── shared/
│   ├── build.gradle.kts           Android Library、iOS framework、OHOS 原生库
│   ├── thirdParty/ktor-curl/      CPF Curl 传输修复及来源说明
│   └── src/
│       ├── commonMain/            四端共享业务、Compose 页面与资源
│       ├── commonTest/            共享状态、API、存储与依赖注入测试
│       ├── nativeMain/            iOS / OHOS 共用的导航生命周期适配
│       ├── androidMain/           SharedPreferences、OkHttp、WebView、系统栏
│       ├── iosMain/               iOS 控制器、NSUserDefaults、Darwin、WKWebView
│       ├── ohosMain/              鸿蒙控制器、文件存储、Curl、ArkUI 互操作
│       └── wasmJsMain/            由 Web 独立构建读取的浏览器平台适配
├── androidApp/
│   ├── build.gradle.kts           APK、渠道、环境、签名与 Room 配置
│   └── src/                       Application、Activity、Room 阅读历史及宿主测试
├── iosApp/
│   ├── Configuration/             Xcode 版本、包名和签名配置
│   ├── iosApp/                    SwiftUI 宿主、Info.plist、应用图标
│   └── iosApp.xcodeproj/          Xcode 工程及共享 framework 构建阶段
├── harmonyApp/
│   ├── AppScope/                  鸿蒙应用信息和全局资源
│   ├── entry/                     ArkUI、N-API、ArkWeb 与 HAP 模块
│   ├── hvigor/                    Hvigor 配置
│   ├── hvigorfile.ts              鸿蒙宿主构建入口
│   ├── build-profile.json5        产品、SDK 与签名方案
│   ├── oh-package.json5           OHPM 依赖
│   ├── run.sh                     调用根 shared、选择设备、打包、安装和启动
│   └── run.test.mjs               设备选择与部署错误处理测试
├── webApp/
│   ├── build.gradle.kts           引用共享源码、Wasm 可执行目标与资源打包
│   ├── settings.gradle.kts        Web 独立构建的仓库与插件配置
│   ├── gradle.properties          Web 构建参数
│   ├── gradle/                    Web 版本目录与独立 Gradle Wrapper
│   ├── gradlew                    Web Gradle 入口，另有 gradlew.bat
│   ├── kotlin-js-store/           Wasm 构建的 Yarn 锁文件
│   ├── src/wasmJsMain/            浏览器入口、HTML、字体和许可证
│   └── web/                       本地运行脚本、同源代理服务及测试
├── .run/                          Web、HarmonyOS 共享 IDE 运行配置
├── gradle/
│   ├── libs.versions.toml         移动三端的依赖与插件版本
│   └── wrapper/                   移动端 Gradle Wrapper 配置
├── build.gradle.kts               根插件声明与 Android Coil 解析规则
├── settings.gradle.kts            注册 shared 与 androidApp
├── gradle.properties              移动端构建参数及鸿蒙渲染配置
├── gradlew                        移动端 Gradle 入口，另有 gradlew.bat
├── docs/                          迁移记录、运行截图和验证记录
├── images/                        原 View/XML 版本的历史图片
└── README.md
```

根 [settings.gradle.kts](settings.gradle.kts) 只注册 `:shared`、`:androidApp`。`shared` 声明 `androidLibrary`、`iosArm64`、`iosSimulatorArm64` 和 `ohosArm64`；鸿蒙源码由 CPF 默认层级的 `ohosMain` 接入 `ohosArm64Main`，不再保留 `harmonyApp/harmony` 独立工程。

Android Studio 打开仓库根目录；Xcode 打开 `iosApp/iosApp.xcodeproj`；DevEco Studio 打开 `harmonyApp`。Web 通过共享 Shell Script 入口运行，也可单独打开 `webApp` 为 Gradle 工程。不要将 `webApp` 以普通 `include(...)` 加入移动端 Settings，以免混用两套 Kotlin/Compose 插件。

### 当前版本配置

| 组件 | Android / iOS / HarmonyOS | Web |
| --- | --- | --- |
| Gradle | 8.14.3 | 9.7.1 |
| Kotlin | 2.2.21-1.0.0 | 2.4.10 |
| Compose Multiplatform | 1.9.2-1.0.0 | 1.12.0 |

Android 使用 AGP **8.11.1**，`compileSdk` / `targetSdk` 为 **36**，`minSdk` 为 **23**。统一 CPF 编译器时调整了 Android 配套构建版本，安装到更新系统的设备仍需按正常兼容性流程验证。

移动端版本见根 [版本目录](gradle/libs.versions.toml)和 [Wrapper](gradle/wrapper/gradle-wrapper.properties)；Web 版本见 [Web 版本目录](webApp/gradle/libs.versions.toml)和 [Web Wrapper](webApp/gradle/wrapper/gradle-wrapper.properties)。Android 沿用官方 Coil 3.3.0，以保留 JVM 11 与最低 API 23；iOS、OHOS 使用 CPF Coil。完整依赖对照见[依赖组合](docs/multiplatform-migration.md#依赖组合)。

### 构建配置与工具链

| 平台 | 构建流程 | 本机工具 |
| --- | --- | --- |
| Android | 根 Gradle 编译 `shared` 与 `androidApp`，生成 APK | JDK、Android SDK |
| iOS | Xcode 调用根 `:shared:embedAndSignAppleFrameworkForXcode`，再编译 SwiftUI 宿主并链接 framework | JDK、CPF Kotlin/Native、Xcode 与 iOS SDK |
| HarmonyOS | 根 `:shared:publishDebugBinariesToHarmonyApp` 输出原生库、头文件与资源到 `harmonyApp/entry`，Hvigor 生成 HAP | JDK、CPF Kotlin/Native、DevEco Studio、HarmonyOS SDK、ohpm、Hvigor |
| Web | `webApp` 的独立 Gradle 编译共享源码与浏览器入口，Node.js 托管产物并代理 API/图片 | JDK、构建插件管理的 Node.js；运行服务另需本机 Node.js |

仓库保存版本声明、构建脚本和工程配置，不存放完整编译器。`webApp/web` 保存 Node.js 服务，与 `webApp/gradle` 中的编译配置职责不同。默认安装与缓存位置如下，实际路径以本机配置为准：

- **Gradle 与插件**：Wrapper 下载到 `~/.gradle/wrapper/dists`，插件和依赖缓存在 `~/.gradle/caches`；可通过 `GRADLE_USER_HOME` 更改根目录。
- **Kotlin/Native**：编译器及原生依赖通常位于 `~/.konan`，由移动端统一的 CPF 版本管理。
- **JDK 与 Android SDK**：JDK 通过 `JAVA_HOME` 或 IDE 的 Gradle JDK 设置选择；Android SDK 通过本机 `local.properties` 的 `sdk.dir` 指定。
- **Xcode 工具链**：随 Xcode 安装；用 `xcode-select -p` 查看开发者目录，Swift 编译器与 iOS SDK 位于该目录下。
- **HarmonyOS 工具链**：由 DevEco Studio/SDK 管理器安装，通过 `OHOS_SDK_HOME`、`DEVECO_SDK_HOME` 和本机命令路径定位。

## 支持与验证

**2026-09-08** 已完成移动三端统一后的构建与模拟器回归：项目图文、真实搜索、原生文章详情和返回状态正常；独立 Web 构建、首页与项目页正常。当前版本、截图、签名差异及验证边界见[本次验证记录](docs/multiplatform-migration.md#移动三端统一构建2026-09-08)。下表综合此前迁移与本次回归结果，不表示本轮重跑了所有功能。

公开页面使用真实 API；账号及服务端写操作目前仅完成浏览器隔离模拟接口回归，四端真实账号闭环尚待专用测试账号验证。

| 能力 | Android | iOS | HarmonyOS | Web |
| --- | --- | --- | --- | --- |
| 主页面、搜索、详情、历史、主题 | 已实测 | 已实测 | 已实测 | 已实测 |
| 文章字号 | 文字缩放 | 整页缩放 | 文字缩放 | 整页缩放 |
| 图片缓存清理 | 支持 | 支持 | 支持 | 支持应用可控缓存 |
| GIF 动画 / 视频帧解码 | 已接入 | 未接入 | 未接入 | 未接入 |
| 禁止 iframe 的文章 | 原生网页容器 | 原生网页容器 | 原生网页容器 | 外部打开 |

各端缓存统计与清理范围不同，列表仍采用平台默认的 overscroll 手感；Web 弹窗关闭后还存在上游无障碍树恢复问题。完整差异、实测设备和未验证范围见[验证记录](docs/multiplatform-migration.md#验证结果)。

回归命令：

```bash
./gradlew :shared:testAndroidHostTest :androidApp:testEnterpriseAlphaDebugUnitTest
node --test webApp/web/server.test.mjs
node --test harmonyApp/run.test.mjs
```

本次 **35 项共享测试、1 项 Android 宿主测试、6 项 Web 代理测试、17 项鸿蒙启动脚本测试**通过。真机、最低系统版本、Release 与真实账号闭环仍需单独回归；此前鸿蒙签名安装到 Mate 60 的结果保留在[历史运行验证](docs/multiplatform-migration.md#运行入口修复验证2026-09-07)。

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
