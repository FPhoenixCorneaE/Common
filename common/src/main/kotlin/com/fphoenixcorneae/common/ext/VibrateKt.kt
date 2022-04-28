package com.fphoenixcorneae.common.ext

import android.Manifest
import android.os.Build
import android.os.VibrationEffect
import androidx.annotation.RequiresPermission

/**
 * 震动
 * 注意:
 * 1、震动为一直震动的话，如果不取消震动，就算退出，也会一直震动;
 */

/**
 * Vibrate.
 *
 * Must hold `<uses-permission android:name="android.permission.VIBRATE" />`
 *
 * @param milliseconds The number of milliseconds to vibrate.
 */
@RequiresPermission(Manifest.permission.VIBRATE)
fun vibrate(milliseconds: Long) {
    val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        applicationContext.vibratorManager?.defaultVibrator
    } else {
        applicationContext.vibrator
    }
    // 判断手机硬件是否有振动器
    if (vibrator?.hasVibrator() == true) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    milliseconds,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        } else {
            vibrator.vibrate(milliseconds)
        }
    }
}

/**
 * Vibrate.
 *
 * Must hold `<uses-permission android:name="android.permission.VIBRATE" />`
 *
 * @param pattern An array of longs of times for which to turn the vibrator on or off.
 * @param repeat  The index into pattern at which to repeat, or -1 if you don't want to repeat.
 */
@RequiresPermission(Manifest.permission.VIBRATE)
fun vibrate(pattern: LongArray?, repeat: Int) {
    val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        applicationContext.vibratorManager?.defaultVibrator
    } else {
        applicationContext.vibrator
    }
    // 判断手机硬件是否有振动器
    if (vibrator?.hasVibrator() == true) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createWaveform(pattern, repeat))
        } else {
            vibrator.vibrate(pattern, repeat)
        }
    }
}

/**
 * Cancel vibrate.
 *
 * Must hold `<uses-permission android:name="android.permission.VIBRATE" />`
 */
@RequiresPermission(Manifest.permission.VIBRATE)
fun cancel() {
    val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        applicationContext.vibratorManager?.defaultVibrator
    } else {
        applicationContext.vibrator
    }
    // 判断手机硬件是否有振动器
    if (vibrator?.hasVibrator() == true) {
        vibrator.cancel()
    }
}