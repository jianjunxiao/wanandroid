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
        versionCode = 20200930
        versionName = "1.0.2"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        javaCompileOptions {
            annotationProcessorOptions {
                arguments = mapOf(
                    "room.schemaLocation" to "$projectDir/build/schemas",
                    "room.incremental" to "true",
                    "room.expandProjection" to "true"
                )
            }
        }
    }

    signingConfigs {
        create("release") {
            storeFile = (file("wanandroid.jks"))
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
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("debug") {
            isMinifyEnabled = false
            isShrinkResources = false
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
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

    applicationVariants.all {
        outputs.all {
            if (this is ApkVariantOutputImpl) {
                outputFileName = "wandroid_v${defaultConfig.versionName}_${defaultConfig.versionCode}.apk"
            }
        }
    }
}

dependencies {
    implementation(
        fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar")))
    )
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
    implementation("com.squareup.retrofit2:retrofit:${Config.retrofitVersion}")
    implementation("com.squareup.retrofit2:converter-gson:${Config.gsonVersion}")

    implementation("com.github.bumptech.glide:glide:${Config.glideVersion}")
    implementation("jp.wasabeef:glide-transformations:${Config.glideTransformations}")

    implementation("com.github.CymChad:BaseRecyclerViewAdapterHelper:${Config.brvhaVersion}")

    implementation("com.hyman:flowlayout-lib:${Config.flowLayoutVersion}")
    implementation("com.just.agentweb:agentweb:${Config.agentWebVersion}")
    implementation("com.jeremyliao:live-event-bus-x:${Config.liveEventBusVersion}")
    implementation("com.github.franmontiel:PersistentCookieJar:${Config.PersistentCookieJarVersion}")
    implementation("com.youth.banner:banner:${Config.bannerVersion}")
}
