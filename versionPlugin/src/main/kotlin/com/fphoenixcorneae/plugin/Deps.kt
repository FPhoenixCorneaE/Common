package com.fphoenixcorneae.plugin

object Deps {
    /** Android */
    object Android {
        const val compileSdkVersion = 30
        const val buildToolsVersion = "30.0.3"
        const val minSdkVersion = 21
        const val targetSdkVersion = 30
        const val versionCode = 115
        const val versionName = "1.1.5"
    }

    /** BuildType */
    object BuildType {
        const val Debug = "debug"
        const val Release = "release"
    }

    /** Kotlin */
    object Kotlin {
        private const val version = "1.5.0"
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib:$version"
    }

    /** AndroidX */
    object AndroidX {
        const val appcompat = "androidx.appcompat:appcompat:1.2.0"
        const val coreKtx = "androidx.core:core-ktx:1.3.2"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.0.4"
        const val activityKtx = "androidx.activity:activity-ktx:1.2.2"
        const val fragmentKtx = "androidx.fragment:fragment-ktx:1.3.3"
        const val viewpager2 = "androidx.viewpager2:viewpager2:1.0.0"
        const val paletteKtx = "androidx.palette:palette-ktx:1.0.0"
    }

    /** Lifecycle */
    object Lifecycle {
        private const val version = "2.3.1"
        const val runtimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:$version"
        const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
    }

    /** Navigation */
    object Navigation {
        private const val version = "2.3.5"
        const val commonKtx = "androidx.navigation:navigation-common-ktx:$version"
        const val fragmentKtx = "androidx.navigation:navigation-fragment-ktx:$version"
        const val runtimeKtx = "androidx.navigation:navigation-runtime-ktx:$version"
        const val uiKtx = "androidx.navigation:navigation-ui-ktx:$version"
    }

    /** Coroutines */
    object Coroutines {
        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.3"
        const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.3"
    }

    /** Kotlin Coroutines Image Loader */
    object Coil {
        private const val version = "1.2.1"
        const val coil = "io.coil-kt:coil:$version"
        const val gif = "io.coil-kt:coil-gif:$version"
        const val svg = "io.coil-kt:coil-svg:$version"
        const val video = "io.coil-kt:coil-video:$version"
    }

    /** CoilTransformations */
    object CoilTransformations {
        private const val version = "1.0.0"
        const val transformations =
            "com.github.Commit451.coil-transformations:transformations:$version"
        const val transformationsGpu =
            "com.github.Commit451.coil-transformations:transformations-gpu:$version"
        const val transformationsFaceDetection =
            "com.github.Commit451.coil-transformations:transformations-face-detection:$version"
    }

    /** Okhttp3 */
    object Okhttp3 {
        const val okhttp = "com.squareup.okhttp3:okhttp:3.12.13"
        const val okio = "com.squareup.okio:okio:2.10.0"
    }

    /** Log */
    object Log {
        const val logger = "com.orhanobut:logger:2.2.0"
    }

    /** Gson */
    object Gson {
        const val gson = "com.google.code.gson:gson:2.8.5"
    }

    /** Eventbus */
    object Eventbus {
        const val eventbus = "org.greenrobot:eventbus:3.2.0"
    }

    object Test {
        const val junit = "junit:junit:4.13.2"
        const val junitExt = "androidx.test.ext:junit:1.1.2"
        const val espresso = "androidx.test.espresso:espresso-core:3.3.0"
    }
}