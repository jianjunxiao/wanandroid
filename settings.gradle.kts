pluginManagement {
    repositories {
        // CPF 插件使用独立版本后缀；普通插件继续从各自官方仓库解析。
        maven("https://maven.eazytec-cloud.com/nexus/repository/maven-public/") {
            content { includeVersionByRegex(".*", ".*", ".*-\\d+\\.\\d+\\.\\d+") }
        }
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        // CPF 配套库的传递依赖仍可能引用早期补丁版本，因此按数字后缀而非单一版本过滤。
        maven("https://maven.eazytec-cloud.com/nexus/repository/maven-public/") {
            content { includeVersionByRegex(".*", ".*", ".*-\\d+\\.\\d+\\.\\d+") }
        }
        google()
        mavenCentral()
    }
}

// Xcode 与 Hvigor 分别消费 shared 的原生产物；Web 在 webApp 中独立构建。
include(":shared", ":androidApp")
rootProject.name = "wanandroid"
rootProject.buildFileName = "build.gradle.kts"
