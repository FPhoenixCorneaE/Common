import com.fphoenixcorneae.plugin.Deps

plugins {
    id("com.android.application")
    kotlin("android")
    id("com.FPhoenixCorneaE.plugin")
}

android {
    compileSdk = Deps.Android.compileSdkVersion
    buildToolsVersion = Deps.Android.buildToolsVersion

    defaultConfig {
        applicationId = "com.fphoenixcorneae.util.demo"
        minSdk = Deps.Android.minSdkVersion
        targetSdk = Deps.Android.targetSdkVersion
        versionCode = Deps.Android.versionCode
        versionName = Deps.Android.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName(Deps.BuildType.Release) {
            // 执行proguard混淆
            isMinifyEnabled = false
            // 移除无用的resource文件
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName(Deps.BuildType.Debug) {
            // 执行proguard混淆
            isMinifyEnabled = false
            // 移除无用的resource文件
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    sourceSets {
        val main by getting
        main.java.srcDirs("src/main/kotlin")
    }

    compileOptions {
        targetCompatibility = JavaVersion.VERSION_11
        sourceCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }

    lint {
        isCheckReleaseBuilds = false
        isAbortOnError = false
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }

    configurations.all {
        resolutionStrategy {
            force(Deps.Kotlin.stdlib)
        }
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(Deps.Kotlin.stdlib)
    implementation(Deps.AndroidX.appcompat)
    implementation(Deps.AndroidX.constraintLayout)
    implementation(Deps.AndroidX.coreKtx)
    implementation(Deps.AndroidX.activityKtx)
    implementation(Deps.AndroidX.fragmentKtx)
    implementation(Deps.AndroidX.viewpager2)
    implementation(Deps.AndroidX.paletteKtx)
    implementation(Deps.Lifecycle.runtimeKtx)
    implementation(Deps.Lifecycle.viewModelKtx)
    implementation(Deps.Navigation.commonKtx)
    implementation(Deps.Navigation.runtimeKtx)
    implementation(Deps.Navigation.fragmentKtx)
    implementation(Deps.Navigation.uiKtx)
    implementation(Deps.Coil.coil)
    implementation(Deps.Coil.gif)
    implementation(Deps.Coil.svg)
    implementation(Deps.Coil.video)
    implementation(Deps.CoilTransformations.transformations)
    implementation(Deps.CoilTransformations.transformationsGpu)
    implementation(Deps.CoilTransformations.transformationsFaceDetection)
    implementation(Deps.Coroutines.core)
    implementation(Deps.Coroutines.android)
    implementation(Deps.Eventbus.eventbus)
    implementation(Deps.Gson.gson)
    implementation(Deps.Log.logger)
    implementation(project(mapOf("path" to ":commonUtil")))
    testImplementation(Deps.Test.junit)
    androidTestImplementation(Deps.Test.junitExt)
    androidTestImplementation(Deps.Test.espresso)
}