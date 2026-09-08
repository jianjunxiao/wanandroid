import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        outputModuleName.set("wanandroid")
        browser {
            commonWebpackConfig { outputFileName = "wanandroid.js" }
        }
        binaries.executable()
    }

    sourceSets {
        // 共享文件只保留在 shared；独立编译避免将两套 Kotlin 插件装入同一次构建。
        commonMain {
            kotlin.srcDir("../shared/src/commonMain/kotlin")
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)
                implementation("org.jetbrains.androidx.lifecycle:lifecycle-viewmodel-compose:${libs.versions.lifecycle.multiplatform.get()}")
                implementation("org.jetbrains.androidx.lifecycle:lifecycle-runtime-compose:${libs.versions.lifecycle.multiplatform.get()}")
                implementation("org.jetbrains.androidx.lifecycle:lifecycle-viewmodel-navigation3:${libs.versions.lifecycle.multiplatform.get()}")
                implementation("androidx.navigation3:navigation3-runtime:${libs.versions.navigation3.multiplatform.get()}")
                implementation("org.jetbrains.androidx.navigation3:navigation3-ui:${libs.versions.navigation3.multiplatform.get()}")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${libs.versions.coroutines.get()}")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${libs.versions.serialization.get()}")
                implementation("io.ktor:ktor-client-core:${libs.versions.ktor.get()}")
                implementation("io.coil-kt.coil3:coil-compose:${libs.versions.coil.multiplatform.get()}")
                implementation("io.coil-kt.coil3:coil-network-ktor3:${libs.versions.coil.multiplatform.get()}")
                implementation("io.coil-kt.coil3:coil-svg:${libs.versions.coil.multiplatform.get()}")
            }
        }
        wasmJsMain {
            kotlin.srcDir("../shared/src/wasmJsMain/kotlin")
            dependencies {
                implementation("io.ktor:ktor-client-js:${libs.versions.ktor.get()}")
            }
        }
    }
}

compose.resources {
    // 业务资源与 Web 字体生成同一个 Res，公共页面无须感知构建入口的变化。
    publicResClass = true
    packageOfResClass = "com.xiaojianjun.wanandroid.resources"
    generateResClass = always
    customDirectory(
        sourceSetName = "commonMain",
        directoryProvider = provider { layout.projectDirectory.dir("../shared/src/commonMain/composeResources") },
    )
}
