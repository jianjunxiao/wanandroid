import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType

plugins {
    base
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    // AGP 8 的 Android 宿主显式使用与 shared 相同的 CPF Kotlin 编译器。
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.android.multiplatform.library) apply false
    alias(libs.plugins.compose.multiplatform) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false
}

// CPF Coil 的 Android AAR 使用 JVM 18，且 GIF 模块引入 minSdk 24 的 Skiko。
// Android 沿用官方 3.3.0，统一覆盖共享库和宿主的传递依赖；Native 仍解析 CPF 版本。
val androidCoilVersion = libs.versions.coil.android.get()
allprojects {
    configurations.configureEach {
        val configuration = this
        resolutionStrategy.eachDependency {
            if (requested.group == "io.coil-kt.coil3" &&
                configuration.attributes.getAttribute(KotlinPlatformType.attribute) == KotlinPlatformType.androidJvm
            ) {
                useVersion(androidCoilVersion)
                because("Android 保持原 Coil 实现、JVM 11 与 minSdk 23，不引入 Native 图片后端")
            }
        }
    }
}

tasks.named<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
}
