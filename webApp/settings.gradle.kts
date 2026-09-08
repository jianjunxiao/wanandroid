// CPF 当前没有发布 Compose Web 变体，因此浏览器应用保留独立的 JetBrains 构建。
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    // Kotlin/Wasm 会额外注册 Node.js 下载仓库，不限制为 Settings 中的仓库。
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "wanandroid-web"
