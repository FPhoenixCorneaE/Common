# CommonUtil
通用工具类库：Kotlin 扩展、Kotlin-Dsl（animation、layout）、权限申请、各种工具类。


### How to include it in your project:
**Step 1.** Add the JitPack repository to your build file

```groovy
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

**Step 2.** Add the dependency

```groovy
dependencies {
	implementation("com.github.FPhoenixCorneaE:CommonUtil:1.1.0")
}
```

**Step 3.**Add the dependencies you need

```groovy
dependencies {
    // lifecycle
    implementation "androidx.lifecycle:lifecycle-runtime-ktx:2.2.0"
    implementation "androidx.lifecycle:lifecycle-livedata-ktx:2.2.0"
    implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0"
    implementation "androidx.lifecycle:lifecycle-extensions:2.2.0"
    // coroutines
    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.3"
    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.3"
    // navigation
	implementation "androidx.navigation:navigation-common-ktx:$navigation_version"
    implementation "androidx.navigation:navigation-fragment-ktx:$navigation_version"
    implementation "androidx.navigation:navigation-runtime-ktx:$navigation_version"
    implementation "androidx.navigation:navigation-ui-ktx:$navigation_version"
    // Gson
    implementation "com.google.code.gson:gson:2.8.5"
    // eventbus
    implementation "org.greenrobot:eventbus:3.2.0"
    // coil
    implementation "io.coil-kt:coil:1.2.0"
    // coil-gif
    implementation "io.coil-kt:coil-gif:1.2.0"
    // coil-svg
    implementation "io.coil-kt:coil-svg:1.2.0"
    // coil-video
    implementation "io.coil-kt:coil-video:1.2.0"
    // coil-transformations
    implementation "com.github.Commit451.coil-transformations:transformations:1.0.0"
    implementation "com.github.Commit451.coil-transformations:transformations-gpu:1.0.0"
    implementation "com.github.Commit451.coil-transformations:transformations-face-detection:1.0.0"
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