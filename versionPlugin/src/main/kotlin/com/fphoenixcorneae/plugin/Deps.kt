package com.fphoenixcorneae.plugin

object Deps {
    /** Android */
    object Android {
        const val compileSdkVersion = 33
        const val minSdkVersion = 21
        const val targetSdkVersion = 33
        const val versionCode = 210
        const val versionName = "2.1.0"
    }

    /** BuildType */
    object BuildType {
        const val Debug = "debug"
        const val Release = "release"
    }

    object FPhoenixCorneaE {
        const val coroutinesPermissions = "com.github.FPhoenixCorneaE:CoroutinesPermissions:1.0.0"
    }

    /** AndroidX */
    object AndroidX {
        const val appcompat = "androidx.appcompat:appcompat:1.6.1"
        const val material = "com.google.android.material:material:1.6.1"
        const val coreKtx = "androidx.core:core-ktx:1.9.0"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.1.4"
        const val activityKtx = "androidx.activity:activity-ktx:1.6.1"
        const val fragmentKtx = "androidx.fragment:fragment-ktx:1.5.5"
        const val viewpager2 = "androidx.viewpager2:viewpager2:1.0.0"
        const val paletteKtx = "androidx.palette:palette-ktx:1.0.0"
    }

    /** Lifecycle */
    object Lifecycle {
        private const val version = "2.5.1"
        const val runtimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:$version"
        const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
    }

    /** Navigation */
    object Navigation {
        private const val version = "2.5.3"
        const val commonKtx = "androidx.navigation:navigation-common-ktx:$version"
        const val fragmentKtx = "androidx.navigation:navigation-fragment-ktx:$version"
        const val runtimeKtx = "androidx.navigation:navigation-runtime-ktx:$version"
        const val uiKtx = "androidx.navigation:navigation-ui-ktx:$version"
    }

    /** Startup */
    object Startup {
        const val runtime = "androidx.startup:startup-runtime:1.1.1"
    }

    /** Coroutines */
    object Coroutines {
        private const val version = "1.7.1"
        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
        const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
    }

    /** Kotlin Coroutines Image Loader: https://github.com/coil-kt/coil */
    /** 完整文档: https://coil-kt.github.io/coil/getting_started/ */
    object Coil {
        private const val version = "2.4.0"
        const val coil = "io.coil-kt:coil:$version"
        const val gif = "io.coil-kt:coil-gif:$version"
        const val svg = "io.coil-kt:coil-svg:$version"
        const val video = "io.coil-kt:coil-video:$version"
    }

    /** CoilTransformations: https://github.com/Commit451/coil-transformations */
    object CoilTransformations {
        private const val version = "2.0.2"
        const val transformations =
            "com.github.Commit451.coil-transformations:transformations:$version"
        const val transformationsGpu =
            "com.github.Commit451.coil-transformations:transformations-gpu:$version"
        const val transformationsFaceDetection =
            "com.github.Commit451.coil-transformations:transformations-face-detection:$version"
    }

    /** Log */
    object Log {
        const val logger = "com.orhanobut:logger:2.2.0"
    }

    /** Gson */
    object Gson {
        const val gson = "com.google.code.gson:gson:2.10.1"
    }

    /** Eventbus */
    object Eventbus {
        const val eventbus = "org.greenrobot:eventbus:3.3.1"
    }

    /** Test */
    object Test {
        const val junit = "junit:junit:4.13.2"
        const val core = "androidx.test:core:1.5.0"
        const val runner = "androidx.test:runner:1.5.2"
        const val rules = "androidx.test:rules:1.5.0"
        const val junitExt = "androidx.test.ext:junit:1.1.5"
        const val espresso = "androidx.test.espresso:espresso-core:3.5.1"
    }
}