# Common

通用类库：Kotlin 扩展、Kotlin-Dsl（animation、layout）、权限申请、缓存（内存、磁盘、内存+磁盘）。

[![](https://jitpack.io/v/FPhoenixCorneaE/CommonUtil.svg)](https://jitpack.io/#FPhoenixCorneaE/CommonUtil)

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
    implementation("com.github.FPhoenixCorneaE:CommonUtil:$latest")
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

### How to use：

- ##### AesExt：

  Aes 算法加密、解密

- ##### HashExt

  MD5 算法、SHA 算法，都是不可逆算法

- ##### GsonExt

  Gson 解析

  `Any?.toJson(includeNulls: Boolean = true): String`

  `String?.toObject(type: Class<T>): T?`

- ##### SharedPreferencesExt

  轻量级的存储

- ##### EditTextExt

  EditText 扩展方法、属性

- ##### ImageViewExt

  ImageView 扩展方法、属性、加载图片、获取位图

- ##### RecycleViewExt

  RecycleView 扩展方法、属性

- ##### ScrollViewExt

  ScrollView 扩展方法、属性

- ##### SearchViewExt

  SearchView 扩展方法、属性

- ##### SeekBarExt

  SeekBar 扩展方法、属性

- ##### TextViewExt

  TextView 扩展方法、属性

- ##### ViewExt

  View 扩展方法、属性
