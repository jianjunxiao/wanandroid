pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    // Kotlin/Wasm 会为 Node.js 工具链注册专用下载仓库。
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

include(":app")
include(":composeApp")
rootProject.name = "wanandroid"
rootProject.buildFileName = "build.gradle.kts"
