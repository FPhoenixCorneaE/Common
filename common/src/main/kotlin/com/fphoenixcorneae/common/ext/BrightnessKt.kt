package com.fphoenixcorneae.common.ext

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.provider.Settings
import android.view.Window
import androidx.annotation.IntRange
import androidx.annotation.RequiresPermission

/**
 * Return whether automatic brightness mode is enabled.
 *
 * @return `true`: yes<br></br>`false`: no
 */
val isAutoBrightnessEnabled: Boolean
    get() = runCatching {
        val mode = Settings.System.getInt(applicationContext.contentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE)
        mode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC
    }.onFailure {
        it.printStackTrace()
    }.getOrDefault(false)

/**
 * Enable or disable automatic brightness mode.
 *
 * Must hold `<uses-permission android:name="android.permission.WRITE_SETTINGS" />`
 * 并且需要打开允许修改系统设置
 *
 * @param enabled True to enabled, false otherwise.
 * @return `true`: success<br></br>`false`: fail
 */
@SuppressLint("NewApi")
fun setAutoBrightnessEnabled(enabled: Boolean): Boolean {
    return if (Settings.System.canWrite(applicationContext)) {
        Settings.System.putInt(
            applicationContext.contentResolver,
            Settings.System.SCREEN_BRIGHTNESS_MODE,
            if (enabled) {
                Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC
            } else {
                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL
            }
        )
    } else {
        false
    }
}

/**
 * 获取屏幕亮度
 *
 * @return 屏幕亮度 0-255
 */
val screenBrightness: Int
    get() = runCatching {
        Settings.System.getInt(applicationContext.contentResolver, Settings.System.SCREEN_BRIGHTNESS)
    }.onFailure {
        it.printStackTrace()
    }.getOrDefault(128)

/**
 * 设置屏幕亮度
 *
 * 需添加权限 `<uses-permission android:name="android.permission.WRITE_SETTINGS" />`
 * 并且需要打开允许修改系统设置
 *
 * @param brightness 亮度值
 */
@RequiresPermission(Manifest.permission.WRITE_SETTINGS)
fun setScreenBrightness(
    @IntRange(from = 0, to = 255)
    brightness: Int,
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        if (!Settings.System.canWrite(applicationContext)) {
            // 打开允许修改 Setting 权限的界面
            openApplicationManageWriteSettings()
            return
        }
    }
    setAutoBrightnessEnabled(false)
    val contentResolver = applicationContext.contentResolver
    Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS, brightness)
    contentResolver.notifyChange(Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS), null)
}

/**
 * 设置窗口亮度
 *
 * @param window     窗口
 * @param brightness 亮度值
 */
fun setWindowBrightness(
    window: Window,
    @IntRange(from = 0, to = 255)
    brightness: Int,
) {
    val lp = window.attributes
    lp.screenBrightness = brightness / 255f
    window.attributes = lp
}

/**
 * 获取窗口亮度
 * @return 屏幕亮度 0-255
 */
val Activity.windowBrightness: Int
    get() = run {
        val lp = window.attributes
        val brightness = lp.screenBrightness
        if (brightness < 0) {
            screenBrightness
        } else {
            (brightness * 255).toInt()
        }
    }