import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.multiplatform.library)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

val ktorVersion = libs.versions.ktor.get()
require(ktorVersion == "3.3.3-1.0.0") {
    "升级 Ktor 前请复核 shared/thirdParty/ktor-curl 中的传输修复，并恢复直接依赖已修复的 CPF 引擎。"
}

// CPF 发布版存在取消调度、句柄清理和协议版本读取问题；只替换两个内部文件，其余源码从同版本发布包获取。
val cpfCurlSources by configurations.creating {
    isCanBeConsumed = false
    isCanBeResolved = true
    isTransitive = false
}
dependencies {
    cpfCurlSources("io.ktor:ktor-client-curl-ohosarm64:$ktorVersion:sources@jar")
}
val cpfCurlPatchedFiles = listOf("CurlMultiApiHandler.kt", "CurlProcessor.kt")
val prepareOhosCurlSources by tasks.registering(Sync::class) {
    group = "harmony"
    description = "提取 CPF Curl 引擎源码，使用本地已验证的传输修复。"
    // 将替换范围纳入增量构建输入，避免新增本地修复文件后仍编译上次提取的同名源码。
    inputs.property("patchedFiles", cpfCurlPatchedFiles)
    from(provider { zipTree(cpfCurlSources.singleFile) }) {
        include("desktopMain/**/*.kt")
        exclude(cpfCurlPatchedFiles.map { "**/$it" })
    }
    into(layout.buildDirectory.dir("generated/cpfCurl"))
}

kotlin {
    // 此模块统一编译移动三端；应用启动与签名打包仍由各端宿主负责。
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

    ohosArm64 {
        binaries.sharedLib {
            baseName = "wanandroid"
            export("org.jetbrains.compose.export:export:${libs.versions.compose.multiplatform.get()}")
            linkerOpts("-lz")
        }
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
            implementation("org.jetbrains.androidx.navigation3:navigation3-runtime:${libs.versions.navigation3.multiplatform.get()}")
            implementation("org.jetbrains.androidx.navigation3:navigation3-ui:${libs.versions.navigation3.multiplatform.get()}")
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${libs.versions.coroutines.get()}")
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${libs.versions.serialization.get()}")
            implementation("io.ktor:ktor-client-core:${libs.versions.ktor.get()}")
            implementation("io.coil-kt.coil3:coil-compose:${libs.versions.coil.multiplatform.get()}")
            implementation("io.coil-kt.coil3:coil-network-ktor3:${libs.versions.coil.multiplatform.get()}")
            implementation("io.coil-kt.coil3:coil-svg:${libs.versions.coil.multiplatform.get()}")
        }
        androidMain.dependencies {
            // Android 使用官方 Navigation 3 装饰器，Native 端使用共享的生命周期适配。
            implementation("androidx.lifecycle:lifecycle-viewmodel-navigation3:${libs.versions.lifecycle.navigation.android.get()}")
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.core.ktx)
            implementation("io.ktor:ktor-client-okhttp:${libs.versions.ktor.get()}")
            implementation("io.coil-kt.coil3:coil-gif:${libs.versions.coil.multiplatform.get()}")
            implementation("io.coil-kt.coil3:coil-video:${libs.versions.coil.multiplatform.get()}")
        }
        iosMain.dependencies {
            implementation("io.ktor:ktor-client-darwin:${libs.versions.ktor.get()}")
        }
        matching { it.name == "ohosMain" }.configureEach {
            // CPF 在建立默认层级时创建 ohosMain，延迟绑定以保持平台工厂与引擎处于同一源集。
            kotlin.srcDir(prepareOhosCurlSources)
            kotlin.srcDir("thirdParty/ktor-curl")
        }
        val ohosArm64Main by getting {
            dependencies {
                api("org.jetbrains.compose.export:export:${libs.versions.compose.multiplatform.get()}")
                implementation("org.jetbrains.kotlinx:atomicfu:0.31.0-1.0.0")
                implementation("io.ktor:ktor-http-cio:$ktorVersion")
                // 复用 CPF 已编译的 C interop、libcurl / OpenSSL，避免同时引入未修复的引擎实现。
                implementation("io.ktor:ktor-client-curl-ohosarm64:$ktorVersion:cinterop-libcurl@klib") {
                    isTransitive = false
                }
            }
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

// 保持宿主现有库名、C 接口和资源路径，使 ArkUI / CMake 无需维护另一份共享工程。
tasks.register<Copy>("publishDebugBinariesToHarmonyApp") {
    group = "harmony"
    description = "编译鸿蒙共享库，并将原生库、头文件和 Compose 资源发布给 Hvigor 宿主。"
    dependsOn("linkDebugSharedOhosArm64")
    into(rootProject.layout.projectDirectory.dir("harmonyApp/entry"))
    from(layout.buildDirectory.file("bin/ohosArm64/debugShared/libwanandroid.so")) { into("libs/arm64-v8a") }
    from(layout.buildDirectory.file("bin/ohosArm64/debugShared/libwanandroid_api.h")) { into("src/main/cpp/include") }
    from(layout.buildDirectory.dir("generated/compose/resourceGenerator/preparedResources/commonMain/composeResources")) {
        into("src/main/resources/rawfile/composeResources/com.xiaojianjun.wanandroid.resources")
    }
}
