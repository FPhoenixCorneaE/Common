# Common

通用类库：Kotlin 扩展、Kotlin-Dsl（animation、layout）、权限申请、缓存（内存、磁盘、内存+磁盘）。

[![](https://jitpack.io/v/FPhoenixCorneaE/Common.svg)](https://jitpack.io/#FPhoenixCorneaE/Common)

### How to include it in your project:

**Step 1.** Add the JitPack repository to your build file

```kotlin
allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}
```

**Step 2.** Add the dependency

```kotlin
dependencies {
    implementation("com.github.FPhoenixCorneaE:common:$latest")
}
```

**Step 3.**Add the dependencies you need

```kotlin
dependencies {
    // lifecycle
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")
    // coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutines_version")
    // navigation
    implementation("androidx.navigation:navigation-common-ktx:$navigation_version")
    implementation("androidx.navigation:navigation-fragment-ktx:$navigation_version")
    implementation("androidx.navigation:navigation-runtime-ktx:$navigation_version")
    implementation("androidx.navigation:navigation-ui-ktx:$navigation_version")
    // Gson
    implementation("com.google.code.gson:gson:2.8.9")
    // eventbus
    implementation("org.greenrobot:eventbus:3.3.1")
    // coil
    implementation("io.coil-kt:coil:$coil_version")
    implementation("io.coil-kt:coil-gif:$coil_version")
    implementation("io.coil-kt:coil-svg:$coil_version")
    implementation("io.coil-kt:coil-video:$coil_version")
    // coil-transformations
    implementation("com.github.Commit451.coil-transformations:transformations:$coil_transformations_version")
    implementation("com.github.Commit451.coil-transformations:transformations-gpu:$coil_transformations_version")
    implementation("com.github.Commit451.coil-transformations:transformations-face-detection:$coil_transformations_version")
}
```

### How to use：[Kotlin扩展属性、函数](https://github.com/FPhoenixCorneaE/Common/blob/main/READMEKT.md)
