import com.android.build.gradle.internal.api.ApkVariantOutputImpl

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    kotlin("android.extensions")
}

android {
    compileSdkVersion(Config.compileSdkVersion)
    buildToolsVersion(Config.buildToolsVersion)

    defaultConfig {
        minSdkVersion(Config.minSdkVersion)
        targetSdkVersion(Config.targetSdkVersion)
        applicationId = "com.xiaojianjun.wanandroid"
        versionCode = 20201127
        versionName = "1.0.5"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        javaCompileOptions {
            annotationProcessorOptions {
                arguments(
                    mapOf(
                        "room.schemaLocation" to "$projectDir/build/schemas",
                        "room.incremental" to "true",
                        "room.expandProjection" to "true"
                    )
                )
            }
        }
    }

    signingConfigs {
        create("release") {
            storeFile = file("wanandroid.jks")
            storePassword = "1qaz2wsx"
            keyAlias = "wanandroid"
            keyPassword = "1qaz2wsx"
            isV1SigningEnabled = true
            isV2SigningEnabled = true
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            isZipAlignEnabled = true
            signingConfig = this@android.signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("debug") {
            isMinifyEnabled = false
            isShrinkResources = false
            isZipAlignEnabled = true
            signingConfig = this@android.signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        viewBinding = true
    }

    androidExtensions {
        isExperimental = true
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
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

    flavorDimensions(*dimensionsFlavors.keys.toTypedArray())

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

    applicationVariants.all {
        val variantName = this.productFlavors.fold("") { acc, productFlavor ->
            "${acc}_${productFlavor.name}".trim('_')
        }
        val variantVesionName = "v${this.versionName}"
        val variantVersionCode = this.versionCode
        this.outputs.filterIsInstance<ApkVariantOutputImpl>().forEach {
            it.outputFileName = "wandroid_${variantName}_${variantVesionName}_${variantVersionCode}.apk"
        }
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${Config.kotlinVersion}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Config.coroutinesVersion}")

    testImplementation("junit:junit:${Config.junitVersion}")
    androidTestImplementation("com.android.support.test:runner:${Config.runnerVersion}")
    androidTestImplementation("com.android.support.test.espresso:espresso-core:${Config.espressoVersion}")

    implementation("com.google.android.material:material:${Config.materialVersion}")

    implementation("androidx.core:core-ktx:${Config.coreVersion}")

    implementation("androidx.appcompat:appcompat:${Config.appCompatVersion}")
    implementation("androidx.activity:activity-ktx:${Config.activityVersion}")
    implementation("androidx.fragment:fragment-ktx:${Config.fragmentVersion}")

    implementation("androidx.constraintlayout:constraintlayout:${Config.constraintLayoutVersion}")
    implementation("androidx.coordinatorlayout:coordinatorlayout:${Config.coordinatorLayoutVersion}")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:${Config.swipeRefreshLayoutVersion}")
    implementation("androidx.recyclerview:recyclerview:${Config.recyclerViewVersion}")

    implementation("androidx.lifecycle:lifecycle-process:${Config.lifecycleVersion}")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:${Config.lifecycleVersion}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${Config.lifecycleVersion}")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:${Config.lifecycleVersion}")

    implementation("androidx.room:room-runtime:${Config.roomVersion}")
    implementation("androidx.room:room-ktx:${Config.roomVersion}")
    kapt("androidx.room:room-compiler:${Config.roomVersion}")

    implementation("com.squareup.okhttp3:okhttp:${Config.okHttpVersion}")
    implementation("com.squareup.okhttp3:logging-interceptor:${Config.okHttpVersion}")
    implementation("com.squareup.retrofit2:retrofit:${Config.retrofitVersion}")
    implementation("com.squareup.retrofit2:converter-moshi:${Config.retrofitVersion}")

    implementation("com.squareup.moshi:moshi:${Config.moshiVersion}")
    implementation("com.squareup.moshi:moshi-kotlin:${Config.moshiVersion}")

    implementation("io.coil-kt:coil:${Config.coilVersion}")
    implementation("io.coil-kt:coil-gif:${Config.coilVersion}")
    implementation("io.coil-kt:coil-svg:${Config.coilVersion}")
    implementation("io.coil-kt:coil-video:${Config.coilVersion}")

    implementation("com.github.CymChad:BaseRecyclerViewAdapterHelper:${Config.brvhaVersion}")

    implementation("com.hyman:flowlayout-lib:${Config.flowLayoutVersion}")
    implementation("com.just.agentweb:agentweb:${Config.agentWebVersion}")
    implementation("com.jeremyliao:live-event-bus-x:${Config.liveEventBusVersion}")
    implementation("com.github.franmontiel:PersistentCookieJar:${Config.PersistentCookieJarVersion}")
    implementation("com.youth.banner:banner:${Config.bannerVersion}")
}
