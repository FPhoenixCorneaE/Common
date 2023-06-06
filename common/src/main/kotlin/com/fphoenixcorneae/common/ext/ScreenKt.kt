package com.fphoenixcorneae.common.ext

import android.Manifest
import android.R
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Rect
import android.provider.Settings
import android.util.DisplayMetrics
import android.view.Surface
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.annotation.RequiresPermission
import kotlin.math.abs

/**
 * 获取屏幕可用的宽度（单位：px）
 */
val screenWidth: Int
    get() =
        Resources.getSystem().displayMetrics.widthPixels

/**
 * 获取屏幕实际的宽度（单位：px）
 */
val realScreenWidth: Int
    get() = run {
        val displayMetrics = DisplayMetrics()
        applicationContext.windowManager?.defaultDisplay?.getRealMetrics(displayMetrics)
        displayMetrics.widthPixels
    }

/**
 * 获取屏幕可用的高度（单位：px）
 */
val screenHeight: Int
    get() =
        Resources.getSystem().displayMetrics.heightPixels

/**
 * 获取屏幕实际的高度（单位：px）
 */
val realScreenHeight: Int
    get() = run {
        val displayMetrics = DisplayMetrics()
        applicationContext.windowManager?.defaultDisplay?.getRealMetrics(displayMetrics)
        displayMetrics.heightPixels
    }


/**
 * 获取屏幕密度
 */
val screenDensity: Float
    get() =
        Resources.getSystem().displayMetrics.density

/**
 * 获取屏幕密度dpi，屏幕密度表示为每英寸多少个像素点
 */
val screenDensityDpi: Int
    get() =
        Resources.getSystem().displayMetrics.densityDpi

/**
 * 判断是否横屏
 * `true`:  是
 * `false`: 否
 */
val isLandscape: Boolean
    get() = Resources.getSystem().configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

/**
 * 判断是否竖屏
 * `true`:  是
 * `false`: 否
 */
val isPortrait: Boolean
    get() = Resources.getSystem().configuration.orientation == Configuration.ORIENTATION_PORTRAIT

/**
 * 设置屏幕为横屏
 * 还有一种就是在Activity中加属性android:screenOrientation="landscape"
 * 1、不设置Activity的android:configChanges时，切屏会重新调用各个生命周期，切横屏时会执行一次，切竖屏时会执行两次
 * 2、设置Activity的android:configChanges="orientation"时，切屏还是会重新调用各个生命周期，切横、竖屏时只会执行一次
 * 3、设置Activity的android:configChanges="orientation|keyboardHidden|screenSize"（4.0以上必须带最后一个参数）时
 * 切屏不会重新调用各个生命周期，只会执行onConfigurationChanged方法
 */
fun Activity.setLandscape() {
    if (requestedOrientation != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }
}

/**
 * 设置屏幕为竖屏
 */
@SuppressLint("SourceLockedOrientationActivity")
fun Activity.setPortrait() {
    if (requestedOrientation != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }
}

/**
 * 获取屏幕旋转角度
 */
fun getScreenRotation(): Int {
    return when (applicationContext.windowManager?.defaultDisplay?.rotation) {
        Surface.ROTATION_0 -> 0
        Surface.ROTATION_90 -> 90
        Surface.ROTATION_180 -> 180
        Surface.ROTATION_270 -> 270
        else -> 0
    }
}

/**
 * 判断是否锁屏
 * `true`:  是
 * `false`: 否
 */
val isScreenLock: Boolean
    get() =
        applicationContext.keyguardManager?.isKeyguardLocked == true

/**
 * 获取进入休眠时长
 * @return 进入休眠时长，报错返回 -123
 * 设置进入休眠时长
 * 需添加权限 `<uses-permission android:name="android.permission.WRITE_SETTINGS" />`
 */
@get:RequiresPermission(Manifest.permission.WRITE_SETTINGS)
var sleepDuration: Int
    get() =
        runCatching {
            Settings.System.getInt(
                applicationContext.contentResolver,
                Settings.System.SCREEN_OFF_TIMEOUT
            )
        }.onFailure {
            it.printStackTrace()
        }.getOrDefault(-123)
    set(duration) {
        Settings.System.putInt(
            applicationContext.contentResolver,
            Settings.System.SCREEN_OFF_TIMEOUT,
            duration
        )
    }

/**
 * 获取状态栏高度
 */
val statusBarHeight: Int
    @SuppressLint("InternalInsetResource", "DiscouragedApi")
    get() =
        Resources.getSystem().run {
            val resId = getIdentifier("status_bar_height", "dimen", "android")
            runCatching {
                getDimensionPixelSize(resId)
            }.onFailure {
                it.printStackTrace()
            }.getOrDefault(0)
        }

/**
 * 获取状态栏高度
 * Attention: cannot be used in onCreate(),onStart(),onResume(), unless it is called in the
 * post(Runnable).
 */
val Activity.statusBarHeight2: Int
    get() =
        run {
            val rect = Rect()
            window.decorView.getWindowVisibleDisplayFrame(rect)
            rect.top
        }

/**
 * 获取状态栏高度
 */
val Activity.invokeStatusBarHeight: Int
    @SuppressLint("PrivateApi")
    get() =
        run {
            if (statusBarHeight2 == 0) {
                runCatching {
                    val localClass = Class.forName("com.android.internal.R\$dimen")
                    val localObject = localClass.newInstance()
                    val id = Integer.parseInt(localClass.getField("status_bar_height").get(localObject)!!.toString())
                    resources.getDimensionPixelSize(id)
                }.onFailure {
                    it.printStackTrace()
                }.getOrDefault(0)
            } else {
                statusBarHeight2
            }
        }

/**
 * 获取工具栏高度
 */
val Context.toolbarHeight: Int
    get() {
        val styledAttributes = theme.obtainStyledAttributes(intArrayOf(android.R.attr.actionBarSize))
        val toolbarHeight = styledAttributes.getDimension(0, 0f).toInt()
        styledAttributes.recycle()
        return toolbarHeight
    }

/**
 * 获取标题栏高度
 * Attention: cannot be used in onCreate(),onStart(),onResume(), unless it is called in the
 * post(Runnable).
 */
val Activity.titleBarHeight: Int
    get() =
        run {
            val contentViewTop = window.findViewById<View>(Window.ID_ANDROID_CONTENT).top
            val titleBarHeight = contentViewTop - statusBarHeight2
            if (titleBarHeight < 0) {
                0
            } else {
                titleBarHeight
            }
        }

/**
 * 获取导航栏高度
 */
val navigationBarHeight: Int
    @SuppressLint("DiscouragedApi", "InternalInsetResource")
    get() =
        Resources.getSystem().run {
            val resId = getIdentifier("navigation_bar_height", "dimen", "android")
            runCatching {
                getDimensionPixelSize(resId)
            }.onFailure {
                it.printStackTrace()
            }.getOrDefault(0)
        }

private var sDecorViewDelta = 0

/**
 * 获取DecorView不可见高度
 */
fun Activity.getDecorViewInvisibleHeight(): Int {
    val decorView = window.decorView
    val outRect = Rect()
    decorView.getWindowVisibleDisplayFrame(outRect)
    val delta = abs(decorView.bottom - outRect.bottom)
    return if (delta <= navigationBarHeight + statusBarHeight2) {
        sDecorViewDelta = delta
        0
    } else {
        delta - sDecorViewDelta
    }
}

/**
 * 获取ContentView不可见高度
 */
fun Activity.getContentViewInvisibleHeight(): Int {
    val contentView = window.findViewById<View>(R.id.content) ?: return 0
    val outRect = Rect()
    contentView.getWindowVisibleDisplayFrame(outRect)
    val delta = abs(contentView.bottom - outRect.bottom)
    return if (delta <= navigationBarHeight + statusBarHeight2) {
        0
    } else {
        delta
    }
}

/**
 * 是否是全屏
 */
val Activity.isFullscreen: Boolean
    get() = run {
        val fullscreenFlag = WindowManager.LayoutParams.FLAG_FULLSCREEN
        window.attributes.flags and fullscreenFlag == fullscreenFlag
    }

/**
 * 设置屏幕为全屏
 */
fun Activity.setFullscreen() {
    window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
}

/**
 * 设置屏幕为非全屏
 */
fun Activity.setNonFullscreen() {
    window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
}

/**
 * 切换全屏
 */
fun Activity.toggleFullscreen() {
    if (isFullscreen) {
        setNonFullscreen()
    } else {
        setFullscreen()
    }
}


