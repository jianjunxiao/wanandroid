import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.multiplatform.library)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    androidLibrary {
        namespace = "com.xiaojianjun.wanandroid.shared"
        compileSdk = libs.versions.compile.sdk.get().toInt()
        minSdk = libs.versions.min.sdk.get().toInt()
        compilerOptions.jvmTarget.set(JvmTarget.JVM_11)
        androidResources.enable = true
        withHostTestBuilder {}.configure {}
    }

    listOf(iosArm64(), iosSimulatorArm64()).forEach { target ->
        target.binaries.framework {
            baseName = "WanAndroid"
            isStatic = true
        }
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        outputModuleName.set("wanandroid")
        browser {
            commonWebpackConfig { outputFileName = "wanandroid.js" }
        }
        binaries.executable()
    }

    sourceSets {
        commonMain.dependencies {
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
        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.core.ktx)
            implementation("io.ktor:ktor-client-okhttp:${libs.versions.ktor.get()}")
            implementation("io.coil-kt.coil3:coil-gif:${libs.versions.coil.multiplatform.get()}")
            implementation("io.coil-kt.coil3:coil-video:${libs.versions.coil.multiplatform.get()}")
        }
        iosMain.dependencies {
            implementation("io.ktor:ktor-client-darwin:${libs.versions.ktor.get()}")
        }
        wasmJsMain.dependencies {
            implementation("io.ktor:ktor-client-js:${libs.versions.ktor.get()}")
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${libs.versions.coroutines.get()}")
            implementation("io.ktor:ktor-client-mock:${libs.versions.ktor.get()}")
        }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "com.xiaojianjun.wanandroid.resources"
    generateResClass = always
}
