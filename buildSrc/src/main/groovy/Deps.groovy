class Deps {
    static gradle_version = '4.2.0'
    static kotlin_version = '1.5.0'
    static navigation_version = '2.3.5'
    static android_maven_gradle_plugin = '2.1'

    static classpath = [
            gradle      : "com.android.tools.build:gradle:$gradle_version",
            kotlin      : "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version",
            androidMaven: "com.github.dcendents:android-maven-gradle-plugin:$android_maven_gradle_plugin",
    ]

    /** android */
    static android = [
            compileSdkVersion: 30,
            buildToolsVersion: "30.0.3",
            minSdkVersion    : 21,
            targetSdkVersion : 30,
            versionCode      : 109,
            versionName      : "1.0.9"
    ]

    /** androidX */
    static androidX = [
            appcompat            : "androidx.appcompat:appcompat:1.2.0",
            constraintLayout     : "androidx.constraintlayout:constraintlayout:2.0.4",
            activityKtx          : "androidx.activity:activity-ktx:1.2.2",
            fragmentKtx          : "androidx.fragment:fragment-ktx:1.3.3",
            lifecycleViewmodelKtx: "androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1",
            viewpager2           : "androidx.viewpager2:viewpager2:1.0.0",
            paletteKtx           : "androidx.palette:palette-ktx:1.0.0",
    ]

    /** navigation 扩展插件 */
    static navigation = [
            commonKtx  : "androidx.navigation:navigation-common-ktx:$navigation_version",
            fragmentKtx: "androidx.navigation:navigation-fragment-ktx:$navigation_version",
            runtimeKtx : "androidx.navigation:navigation-runtime-ktx:$navigation_version",
            uiKtx      : "androidx.navigation:navigation-ui-ktx:$navigation_version",
    ]

    /** kotlin */
    static kotlin = [
            coreKtx: "androidx.core:core-ktx:1.3.2",
            stdlib : "org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version",
    ]

    /** coroutines */
    static coroutines = [
            core   : "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.3",
            android: "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.3"
    ]

    /** rxJava2 */
    static rxJava2 = [
            rxJava   : "io.reactivex.rxjava2:rxjava:2.1.16",
            rxAndroid: "io.reactivex.rxjava2:rxandroid:2.1.0",
            rxKotlin : "io.reactivex.rxjava2:rxkotlin:2.2.0",
    ]

    /** Kotlin Coroutines Image Loader */
    static coil = [
            coil                        : "io.coil-kt:coil:1.2.0",
            gif                         : "io.coil-kt:coil-gif:1.2.0",
            svg                         : "io.coil-kt:coil-svg:1.2.0",
            video                       : "io.coil-kt:coil-video:1.2.0",
            transformations             : "com.github.Commit451.coil-transformations:transformations:1.0.0",
            transformationsGpu          : "com.github.Commit451.coil-transformations:transformations-gpu:1.0.0",
            transformationsFaceDetection: "com.github.Commit451.coil-transformations:transformations-face-detection:1.0.0",
    ]

    /** logger */
    static logger = "com.orhanobut:logger:2.2.0"
    /** 权限管理 */
    static rxPermissions = "com.github.tbruyelle:rxpermissions:0.10.2"
    /** Gson */
    static gson = "com.google.code.gson:gson:2.8.5"
    /** eventBus */
    static eventBus = "org.greenrobot:eventbus:3.2.0"

    static androidXLibs = androidX.values()
    static navigationLibs = navigation.values()
    static kotlinLibs = kotlin.values()
    static rxJava2Libs = rxJava2.values()
    static coilLibs = coil.values()
}