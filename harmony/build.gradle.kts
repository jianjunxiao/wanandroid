plugins {
    kotlin("multiplatform") version "2.2.21-1.0.0"
    kotlin("plugin.compose") version "2.2.21-1.0.0"
    kotlin("plugin.serialization") version "2.2.21-1.0.0"
    id("org.jetbrains.compose") version "1.9.2-1.0.0"
}

kotlin {
    ohosArm64 {
        binaries.sharedLib {
            baseName = "wanandroid"
            export("org.jetbrains.compose.export:export:1.9.2-1.0.0")
            linkerOpts("-lz")
        }
    }
    sourceSets {
        commonMain {
            kotlin.srcDir("../composeApp/src/commonMain/kotlin")
            dependencies {
                implementation("org.jetbrains.compose.runtime:runtime:1.9.2-1.0.0")
                implementation("org.jetbrains.compose.foundation:foundation:1.9.2-1.0.0")
                implementation("org.jetbrains.compose.material3:material3:1.9.2-1.0.0")
                implementation("org.jetbrains.compose.ui:ui:1.9.2-1.0.0")
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)
                implementation("org.jetbrains.androidx.lifecycle:lifecycle-runtime-compose:2.9.4-1.0.0")
                implementation("org.jetbrains.androidx.lifecycle:lifecycle-viewmodel-compose:2.9.4-1.0.0")
                implementation("org.jetbrains.androidx.navigation3:navigation3-runtime:1.9.2-1.0.0")
                implementation("org.jetbrains.androidx.navigation3:navigation3-ui:1.9.2-1.0.0")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2-1.0.0")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.1-1.0.0")
                implementation("io.ktor:ktor-client-core:3.3.3-1.0.0")
                implementation("io.coil-kt.coil3:coil-compose:3.3.0-1.0.0")
                implementation("io.coil-kt.coil3:coil-network-ktor3:3.3.0-1.0.0")
                implementation("io.coil-kt.coil3:coil-svg:3.3.0-1.0.0")
            }
        }
        val ohosArm64Main by getting {
            kotlin.srcDir("../composeApp/src/ohosMain/kotlin")
            dependencies {
                api("org.jetbrains.compose.export:export:1.9.2-1.0.0")
                implementation("org.jetbrains.kotlinx:atomicfu:0.31.0-1.0.0")
            }
        }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "com.xiaojianjun.wanandroid.resources"
    generateResClass = always
    customDirectory(
        sourceSetName = "commonMain",
        directoryProvider = provider { layout.projectDirectory.dir("../composeApp/src/commonMain/composeResources") },
    )
}

tasks.register<Copy>("publishDebugBinariesToHarmonyApp") {
    dependsOn("linkDebugSharedOhosArm64")
    into("../harmonyApp/entry")
    from("build/bin/ohosArm64/debugShared/libwanandroid.so") { into("libs/arm64-v8a") }
    from("build/bin/ohosArm64/debugShared/libwanandroid_api.h") { into("src/main/cpp/include") }
    from("build/generated/compose/resourceGenerator/preparedResources/commonMain/composeResources") {
        into("src/main/resources/rawfile/composeResources/com.xiaojianjun.wanandroid.resources")
    }
}
