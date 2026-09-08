import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    // AGP 8 不内置 Kotlin，宿主与共享库必须使用同一 CPF 编译器。
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.xiaojianjun.wanandroid"
    compileSdk = libs.versions.compile.sdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.min.sdk.get().toInt()
        targetSdk = libs.versions.target.sdk.get().toInt()
        applicationId = "com.xiaojianjun.wanandroid"
        versionCode = 20260514
        versionName = "1.0.6"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            storeFile = file("wanandroid.jks")
            storePassword = "1qaz2wsx"
            keyAlias = "wanandroid"
            keyPassword = "1qaz2wsx"
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = this@android.signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("debug") {
            isMinifyEnabled = false
            isShrinkResources = false
            signingConfig = this@android.signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    lint {
        checkReleaseBuilds = false
        abortOnError = false
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    val dimensionsFlavors = mapOf(
        "channel" to listOf(
            "enterprise",
            "huawei",
            "xiaomi",
            "oneplus",
            "oppo",
            "vivo",
            "meizu",
            "googleplay"
        ),
        "environment" to listOf(
            "alpha",
            "beta",
            "production"
        )
    )

    flavorDimensions += dimensionsFlavors.keys

    productFlavors {
        dimensionsFlavors.forEach { (dimension, flavors) ->
            flavors.forEach { flavor ->
                create(flavor) {
                    this.dimension = dimension
                    this.buildConfigField("String", dimension, "\"$flavor\"")
                    this.addManifestPlaceholders(mapOf(dimension to flavor))
                }
            }
        }
    }
}

androidComponents {
    onVariants { variant ->
        val variantName = variant.productFlavors.joinToString("_") { it.second }
        variant.outputs.forEach { output ->
            // AGP 8.11 尚未在 VariantOutput 公共接口暴露文件名属性。
            // 将版本相关实现限制在命名处，继续保留渠道 APK 名称及 IDE 的产物元数据。
            (output as com.android.build.api.variant.impl.VariantOutputImpl).outputFileName.set(
                "wandroid_${variantName}_v${output.versionName.get()}_${output.versionCode.get()}.apk"
            )
        }
    }
}

ksp {
    arg("room.schemaLocation", "$projectDir/build/schemas")
    arg("room.incremental", "true")
    arg("room.expandProjection", "true")
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_11
    }
}

dependencies {
    implementation(project(":shared"))
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlinx.coroutines.android)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.test.espresso.core)

    implementation(libs.androidx.core.ktx)

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.activity.compose)

    implementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.tooling)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
}
