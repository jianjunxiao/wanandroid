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
    }
}

include(":shared", ":androidApp", ":webApp")
rootProject.name = "wanandroid"
rootProject.buildFileName = "build.gradle.kts"
