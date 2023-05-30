import com.fphoenixcorneae.plugin.Deps

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("kotlin-parcelize")
    id("com.FPhoenixCorneaE.plugin")
    `maven-publish`
}

android {
    compileSdk = Deps.Android.compileSdkVersion

    defaultConfig {
        minSdk = Deps.Android.minSdkVersion
        targetSdk = Deps.Android.targetSdkVersion
        consumerProguardFile("consumer-rules.pro")
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

    packagingOptions {
        resources.excludes += "DebugProbesKt.bin"
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
    }

    lint {
    }

    configurations.all {
        resolutionStrategy {
        }
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    // FPhoenixCorneaE
    api(Deps.FPhoenixCorneaE.coroutinesPermissions)
    // androidX
    implementation(Deps.AndroidX.appcompat)
    implementation(Deps.AndroidX.constraintLayout)
    implementation(Deps.AndroidX.coreKtx)
    implementation(Deps.AndroidX.activityKtx)
    implementation(Deps.AndroidX.fragmentKtx)
    implementation(Deps.AndroidX.viewpager2)
    implementation(Deps.AndroidX.paletteKtx)
    // lifecycle
    implementation(Deps.Lifecycle.runtimeKtx)
    implementation(Deps.Lifecycle.viewModelKtx)
    // navigation
    implementation(Deps.Navigation.commonKtx)
    implementation(Deps.Navigation.runtimeKtx)
    implementation(Deps.Navigation.fragmentKtx)
    implementation(Deps.Navigation.uiKtx)
    // startup
    implementation(Deps.Startup.runtime)
    // coil
    implementation(Deps.Coil.coil)
    implementation(Deps.Coil.gif)
    implementation(Deps.Coil.svg)
    implementation(Deps.Coil.video)
    // coil transformations
    implementation(Deps.CoilTransformations.transformations)
    implementation(Deps.CoilTransformations.transformationsGpu)
    implementation(Deps.CoilTransformations.transformationsFaceDetection)
    // coroutines
    implementation(Deps.Coroutines.core)
    implementation(Deps.Coroutines.android)
    // eventbus
    implementation(Deps.Eventbus.eventbus)
    // gson
    implementation(Deps.Gson.gson)

    // test
    testImplementation(Deps.Test.junit)
    testImplementation(Deps.Test.core)
    androidTestImplementation(Deps.Test.runner)
    androidTestImplementation(Deps.Test.rules)
    androidTestImplementation(Deps.Test.junitExt)
    androidTestImplementation(Deps.Test.espresso)
}

// MavenPublication 配置-------------------------------------------------------------

afterEvaluate {
    publishing {
        publications {
            // Creates a Maven publication called "release".
            create<MavenPublication>(Deps.BuildType.Release) {
                from(components[Deps.BuildType.Release])
                groupId = "com.github.FPhoenixCorneaE"
                artifactId = project.name.toLowerCase()
                version = project.version.toString()
            }
        }
    }
}