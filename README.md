# Common

通用类库：Kotlin 扩展、Kotlin-DSL（animation、layout、代码构造Drawable）、动态权限申请、缓存（内存、磁盘、内存+磁盘）。

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
    implementation("com.github.FPhoenixCorneaE:Common:$latest")
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

#### 1.[Kotlin扩展属性、函数](https://github.com/FPhoenixCorneaE/Common/blob/main/READMEKT.md)

#### 2.若是有自定义 Startup Initializer，需要依赖`CommonInitializer`

```kotlin
override fun dependencies(): MutableList<Class<out Initializer<*>>> {
    return mutableListOf(CommonInitializer::class.java)
}
```

#### 3.动态权限申请

```kotlin
private fun requestPermissions() {
    requestPhonePermission {
        onGranted {
            "onGranted".logd("requestPermissions")
            // TODO
        }
        onDenied {
            "onDenied".logd("requestPermissions")
            requestPermissions()
        }
        onShowRationale {
            "onShowRationale".logd("requestPermissions")
            it.retry()
        }
        onNeverAskAgain {
            "onNeverAskAgain".logd("requestPermissions")
            IntentUtil.openApplicationDetailsSettings()
        }
    }
}
```

#### 4.代码构造Drawable

```kotlin
gradientDrawable(this) {
    shape(Shape.RECTANGLE)
    solidColor(Color.GRAY)
//            solidColor {
//                item {
//                    color(Color.RED)
//                    state(StatePressed)
//                }
//                item {
//                    color(Color.BLUE)
//                    minusState(StatePressed)
//                }
//            }
    corner {
//                radius(20f)
        radii(topLeftRadius = 5f, topRightRadius = 10f, bottomLeftRadius = 15f, bottomRightRadius = 20f)
    }
    stroke {
        width(3f)
        dashWidth(8f)
        dashGap(3f)
        color {
            item {
                color(Color.BLUE)
                state(StatePressed)
            }
            item {
                color(Color.RED)
                minusState(StatePressed)
            }
        }
    }
    padding {
        setPadding(left = 8f, top = 8f, right = 8f, bottom = 8f)
    }
    size(width = 100, height = 20)
//            gradient {
//                gradientCenter(0.5f, 0.5f)
//                useLevel(false)
//                gradientType(GradientType.LINEAR_GRADIENT)
//                orientation(GradientDrawable.Orientation.LEFT_RIGHT)
//                gradientRadius(10f)
//                gradientColors(intArrayOf(Color.TRANSPARENT, Color.BLACK))
//            }
}
```

```kotlin
stateListDrawable {
    item {
        drawable(ColorDrawable(Color.GRAY))
        minusState(StatePressed)
    }
    item {
        drawable(ColorDrawable(Color.GREEN))
        state(StatePressed)
    }
}
```

#### 5.自定义日志打印(v2.0.5加入)

> 默认使用`android.util.Log`打印，若要使用自定义日志打印或者第三方打印库，可实现接口`com.fphoenixcorneae.common.log.Printer`，然后在自定义`Application`的`attachBaseContext(base: Context?)`方法中初始化，并调用`AndroidLog.setPrinter(printer: Printer)`。
>
> 示例：
>
> ```kotlin
> package com.fphoenixcorneae.common.demo
> 
> import com.fphoenixcorneae.common.log.Printer
> import com.orhanobut.logger.Logger
> 
> /**
>  * @desc：CustomPrinter
>  * @date：2022/5/3 11:41
>  */
> class CustomPrinter : Printer {
>     override fun v(tag: String?, message: String, vararg args: Any?) {
>         Logger.t(tag).v(message, args)
>     }
> 
>     override fun d(tag: String?, message: String, vararg args: Any?) {
>         Logger.t(tag).d(message, args)
>     }
> 
>     override fun i(tag: String?, message: String, vararg args: Any?) {
>         Logger.t(tag).i(message, args)
>     }
> 
>     override fun w(tag: String?, message: String, vararg args: Any?) {
>         Logger.t(tag).w(message, args)
>     }
> 
>     override fun e(tag: String?, message: String, vararg args: Any?) {
>         Logger.t(tag).e(message, args)
>     }
> 
>     override fun wtf(tag: String?, message: String, vararg args: Any?) {
>         Logger.t(tag).wtf(message, args)
>     }
> 
>     override fun json(tag: String?, message: String?) {
>         Logger.t(tag).json(message)
>     }
> 
>     override fun xml(tag: String?, message: String?) {
>         Logger.t(tag).xml(message)
>     }
> }
> ```
>
> ```kotlin
> override fun attachBaseContext(base: Context?) {
>     super.attachBaseContext(base)
>     // 初始化日志打印配置
>     initLoggerConfig()
>     AndroidLog.setPrinter(CustomPrinter())
> }
> ```
