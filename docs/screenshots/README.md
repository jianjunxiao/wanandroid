# 三端运行截图

本页展示 2026-09-06 迁移回归时采集的 Android、iOS、HarmonyOS 实际运行画面，应用版本为 `1.0.6`，每一行对应相同功能。截图通过应用正常操作采集，网络内容来自 WanAndroid 真实 API；没有绘制替代 UI，也没有用模拟账号数据填充页面。当前目录与依赖配置见[项目 README](../../README.md#工程目录)。

[返回项目介绍](../../README.md) · [构建与验证记录](../multiplatform-migration.md)

## 采集信息

| 平台 | 设备 / 系统 | 图像 |
| --- | --- | --- |
| Android | Resizable_Experimental，Android 17 / API 37 | adb PNG |
| iOS | iPhone 17 Pro，iOS 26.5 Simulator | XcodeBuildMCP JPEG |
| HarmonyOS | nova 16，系统报告 OpenHarmony-7.0.0.105 | Emulator PNG |

- 版本：`1.0.6`，采集日期：`2026-09-06`。
- 共 20 组页面、60 张截图；显示宽度统一为 240，点击图片可单独查看。
- 列表内容、Banner、排行榜与时间来自实时接口，采集时刻不同可能出现内容差异；字体、安全区和系统栏随平台变化。
- 登录和注册页保持未提交状态。个人积分、收藏、分享等登录后页面尚未纳入真实账号截图，相关功能的隔离模拟验证另见迁移记录。
- 夜间画面通过应用设置切换；采集结束后恢复日间模式、100% 字号并返回首页。

## 页面导航

[首页 · 热门](#home) · [首页 · 最新](#latest) · [首页 · 广场](#plaza) · [首页 · 项目](#project) · [首页 · 公众号](#wechat) · [体系分类](#system) · [发现](#discovery) · [导航](#navigation) · [搜索入口与历史](#search) · [搜索结果](#search-results) · [文章详情](#article) · [我的](#profile) · [登录](#login) · [注册](#register) · [积分排行](#points-rank) · [阅读历史](#history) · [设置](#settings) · [字号设置弹窗](#font-dialog) · [夜间首页](#night-home) · [开源许可](#open-source)

<a id="home"></a>

## 首页 · 热门

<table>
<tr><th>Android</th><th>iOS</th><th>HarmonyOS</th></tr>
<tr>
<td><a href="android/home.png"><img src="android/home.png" alt="Android 首页 · 热门" width="240" /></a></td>
<td><a href="ios/home.jpg"><img src="ios/home.jpg" alt="iOS 首页 · 热门" width="240" /></a></td>
<td><a href="harmony/home.png"><img src="harmony/home.png" alt="HarmonyOS 首页 · 热门" width="240" /></a></td>
</tr>
</table>

<a id="latest"></a>

## 首页 · 最新

<table>
<tr><th>Android</th><th>iOS</th><th>HarmonyOS</th></tr>
<tr>
<td><a href="android/latest.png"><img src="android/latest.png" alt="Android 首页 · 最新" width="240" /></a></td>
<td><a href="ios/latest.jpg"><img src="ios/latest.jpg" alt="iOS 首页 · 最新" width="240" /></a></td>
<td><a href="harmony/latest.png"><img src="harmony/latest.png" alt="HarmonyOS 首页 · 最新" width="240" /></a></td>
</tr>
</table>

<a id="plaza"></a>

## 首页 · 广场

<table>
<tr><th>Android</th><th>iOS</th><th>HarmonyOS</th></tr>
<tr>
<td><a href="android/plaza.png"><img src="android/plaza.png" alt="Android 首页 · 广场" width="240" /></a></td>
<td><a href="ios/plaza.jpg"><img src="ios/plaza.jpg" alt="iOS 首页 · 广场" width="240" /></a></td>
<td><a href="harmony/plaza.png"><img src="harmony/plaza.png" alt="HarmonyOS 首页 · 广场" width="240" /></a></td>
</tr>
</table>

<a id="project"></a>

## 首页 · 项目

<table>
<tr><th>Android</th><th>iOS</th><th>HarmonyOS</th></tr>
<tr>
<td><a href="android/project.png"><img src="android/project.png" alt="Android 首页 · 项目" width="240" /></a></td>
<td><a href="ios/project.jpg"><img src="ios/project.jpg" alt="iOS 首页 · 项目" width="240" /></a></td>
<td><a href="harmony/project.png"><img src="harmony/project.png" alt="HarmonyOS 首页 · 项目" width="240" /></a></td>
</tr>
</table>

<a id="wechat"></a>

## 首页 · 公众号

<table>
<tr><th>Android</th><th>iOS</th><th>HarmonyOS</th></tr>
<tr>
<td><a href="android/wechat.png"><img src="android/wechat.png" alt="Android 首页 · 公众号" width="240" /></a></td>
<td><a href="ios/wechat.jpg"><img src="ios/wechat.jpg" alt="iOS 首页 · 公众号" width="240" /></a></td>
<td><a href="harmony/wechat.png"><img src="harmony/wechat.png" alt="HarmonyOS 首页 · 公众号" width="240" /></a></td>
</tr>
</table>

<a id="system"></a>

## 体系分类

<table>
<tr><th>Android</th><th>iOS</th><th>HarmonyOS</th></tr>
<tr>
<td><a href="android/system.png"><img src="android/system.png" alt="Android 体系分类" width="240" /></a></td>
<td><a href="ios/system.jpg"><img src="ios/system.jpg" alt="iOS 体系分类" width="240" /></a></td>
<td><a href="harmony/system.png"><img src="harmony/system.png" alt="HarmonyOS 体系分类" width="240" /></a></td>
</tr>
</table>

<a id="discovery"></a>

## 发现

<table>
<tr><th>Android</th><th>iOS</th><th>HarmonyOS</th></tr>
<tr>
<td><a href="android/discovery.png"><img src="android/discovery.png" alt="Android 发现" width="240" /></a></td>
<td><a href="ios/discovery.jpg"><img src="ios/discovery.jpg" alt="iOS 发现" width="240" /></a></td>
<td><a href="harmony/discovery.png"><img src="harmony/discovery.png" alt="HarmonyOS 发现" width="240" /></a></td>
</tr>
</table>

<a id="navigation"></a>

## 导航

<table>
<tr><th>Android</th><th>iOS</th><th>HarmonyOS</th></tr>
<tr>
<td><a href="android/navigation.png"><img src="android/navigation.png" alt="Android 导航" width="240" /></a></td>
<td><a href="ios/navigation.jpg"><img src="ios/navigation.jpg" alt="iOS 导航" width="240" /></a></td>
<td><a href="harmony/navigation.png"><img src="harmony/navigation.png" alt="HarmonyOS 导航" width="240" /></a></td>
</tr>
</table>

<a id="search"></a>

## 搜索入口与历史

<table>
<tr><th>Android</th><th>iOS</th><th>HarmonyOS</th></tr>
<tr>
<td><a href="android/search.png"><img src="android/search.png" alt="Android 搜索入口与历史" width="240" /></a></td>
<td><a href="ios/search.jpg"><img src="ios/search.jpg" alt="iOS 搜索入口与历史" width="240" /></a></td>
<td><a href="harmony/search.png"><img src="harmony/search.png" alt="HarmonyOS 搜索入口与历史" width="240" /></a></td>
</tr>
</table>

<a id="search-results"></a>

## 搜索结果

<table>
<tr><th>Android</th><th>iOS</th><th>HarmonyOS</th></tr>
<tr>
<td><a href="android/search-results.png"><img src="android/search-results.png" alt="Android 搜索结果" width="240" /></a></td>
<td><a href="ios/search-results.jpg"><img src="ios/search-results.jpg" alt="iOS 搜索结果" width="240" /></a></td>
<td><a href="harmony/search-results.png"><img src="harmony/search-results.png" alt="HarmonyOS 搜索结果" width="240" /></a></td>
</tr>
</table>

<a id="article"></a>

## 文章详情

<table>
<tr><th>Android</th><th>iOS</th><th>HarmonyOS</th></tr>
<tr>
<td><a href="android/article.png"><img src="android/article.png" alt="Android 文章详情" width="240" /></a></td>
<td><a href="ios/article.jpg"><img src="ios/article.jpg" alt="iOS 文章详情" width="240" /></a></td>
<td><a href="harmony/article.png"><img src="harmony/article.png" alt="HarmonyOS 文章详情" width="240" /></a></td>
</tr>
</table>

<a id="profile"></a>

## 我的

<table>
<tr><th>Android</th><th>iOS</th><th>HarmonyOS</th></tr>
<tr>
<td><a href="android/profile.png"><img src="android/profile.png" alt="Android 我的" width="240" /></a></td>
<td><a href="ios/profile.jpg"><img src="ios/profile.jpg" alt="iOS 我的" width="240" /></a></td>
<td><a href="harmony/profile.png"><img src="harmony/profile.png" alt="HarmonyOS 我的" width="240" /></a></td>
</tr>
</table>

<a id="login"></a>

## 登录

<table>
<tr><th>Android</th><th>iOS</th><th>HarmonyOS</th></tr>
<tr>
<td><a href="android/login.png"><img src="android/login.png" alt="Android 登录" width="240" /></a></td>
<td><a href="ios/login.jpg"><img src="ios/login.jpg" alt="iOS 登录" width="240" /></a></td>
<td><a href="harmony/login.png"><img src="harmony/login.png" alt="HarmonyOS 登录" width="240" /></a></td>
</tr>
</table>

<a id="register"></a>

## 注册

<table>
<tr><th>Android</th><th>iOS</th><th>HarmonyOS</th></tr>
<tr>
<td><a href="android/register.png"><img src="android/register.png" alt="Android 注册" width="240" /></a></td>
<td><a href="ios/register.jpg"><img src="ios/register.jpg" alt="iOS 注册" width="240" /></a></td>
<td><a href="harmony/register.png"><img src="harmony/register.png" alt="HarmonyOS 注册" width="240" /></a></td>
</tr>
</table>

<a id="points-rank"></a>

## 积分排行

<table>
<tr><th>Android</th><th>iOS</th><th>HarmonyOS</th></tr>
<tr>
<td><a href="android/points-rank.png"><img src="android/points-rank.png" alt="Android 积分排行" width="240" /></a></td>
<td><a href="ios/points-rank.jpg"><img src="ios/points-rank.jpg" alt="iOS 积分排行" width="240" /></a></td>
<td><a href="harmony/points-rank.png"><img src="harmony/points-rank.png" alt="HarmonyOS 积分排行" width="240" /></a></td>
</tr>
</table>

<a id="history"></a>

## 阅读历史

<table>
<tr><th>Android</th><th>iOS</th><th>HarmonyOS</th></tr>
<tr>
<td><a href="android/history.png"><img src="android/history.png" alt="Android 阅读历史" width="240" /></a></td>
<td><a href="ios/history.jpg"><img src="ios/history.jpg" alt="iOS 阅读历史" width="240" /></a></td>
<td><a href="harmony/history.png"><img src="harmony/history.png" alt="HarmonyOS 阅读历史" width="240" /></a></td>
</tr>
</table>

<a id="settings"></a>

## 设置

<table>
<tr><th>Android</th><th>iOS</th><th>HarmonyOS</th></tr>
<tr>
<td><a href="android/settings.png"><img src="android/settings.png" alt="Android 设置" width="240" /></a></td>
<td><a href="ios/settings.jpg"><img src="ios/settings.jpg" alt="iOS 设置" width="240" /></a></td>
<td><a href="harmony/settings.png"><img src="harmony/settings.png" alt="HarmonyOS 设置" width="240" /></a></td>
</tr>
</table>

<a id="font-dialog"></a>

## 字号设置弹窗

<table>
<tr><th>Android</th><th>iOS</th><th>HarmonyOS</th></tr>
<tr>
<td><a href="android/font-dialog.png"><img src="android/font-dialog.png" alt="Android 字号设置弹窗" width="240" /></a></td>
<td><a href="ios/font-dialog.jpg"><img src="ios/font-dialog.jpg" alt="iOS 字号设置弹窗" width="240" /></a></td>
<td><a href="harmony/font-dialog.png"><img src="harmony/font-dialog.png" alt="HarmonyOS 字号设置弹窗" width="240" /></a></td>
</tr>
</table>

<a id="night-home"></a>

## 夜间首页

<table>
<tr><th>Android</th><th>iOS</th><th>HarmonyOS</th></tr>
<tr>
<td><a href="android/night-home.png"><img src="android/night-home.png" alt="Android 夜间首页" width="240" /></a></td>
<td><a href="ios/night-home.jpg"><img src="ios/night-home.jpg" alt="iOS 夜间首页" width="240" /></a></td>
<td><a href="harmony/night-home.png"><img src="harmony/night-home.png" alt="HarmonyOS 夜间首页" width="240" /></a></td>
</tr>
</table>

<a id="open-source"></a>

## 开源许可

<table>
<tr><th>Android</th><th>iOS</th><th>HarmonyOS</th></tr>
<tr>
<td><a href="android/open-source.png"><img src="android/open-source.png" alt="Android 开源许可" width="240" /></a></td>
<td><a href="ios/open-source.jpg"><img src="ios/open-source.jpg" alt="iOS 开源许可" width="240" /></a></td>
<td><a href="harmony/open-source.png"><img src="harmony/open-source.png" alt="HarmonyOS 开源许可" width="240" /></a></td>
</tr>
</table>
