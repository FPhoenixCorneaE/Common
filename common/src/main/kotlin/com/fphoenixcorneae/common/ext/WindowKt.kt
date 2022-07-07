package com.fphoenixcorneae.common.ext

import android.app.Activity
import android.app.Dialog
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

/**
 * 隐藏状态栏
 */
fun Activity?.hideStatusBars() {
    this?.window?.decorView?.let {
        hideStatusBars(it)
    }
}

/**
 * 隐藏虚拟导航栏
 */
fun Activity?.hideNavigationBars() {
    this?.window?.decorView?.let {
        hideNavigationBars(it)
    }
}

/**
 * 隐藏状态栏和虚拟导航栏
 */
fun Activity?.hideSystemBars() {
    this?.window?.decorView?.let {
        hideSystemBars(it)
    }
}

/**
 * 隐藏状态栏
 */
fun Dialog?.hideStatusBars() {
    this?.window?.decorView?.let {
        hideStatusBars(it)
    }
}

/**
 * 隐藏虚拟导航栏
 */
fun Dialog?.hideNavigationBars() {
    this?.window?.decorView?.let {
        hideNavigationBars(it)
    }
}

/**
 * 隐藏状态栏和虚拟导航栏
 */
fun Dialog?.hideSystemBars() {
    this?.window?.decorView?.let {
        hideSystemBars(it)
    }
}

private fun hideStatusBars(decorView: View) {
    val controller = ViewCompat.getWindowInsetsController(decorView)
    controller?.hide(WindowInsetsCompat.Type.statusBars())
    controller?.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
}

private fun hideNavigationBars(decorView: View) {
    val controller = ViewCompat.getWindowInsetsController(decorView)
    controller?.hide(WindowInsetsCompat.Type.navigationBars())
    controller?.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
}

private fun hideSystemBars(decorView: View) {
    val controller = ViewCompat.getWindowInsetsController(decorView)
    controller?.hide(WindowInsetsCompat.Type.systemBars())
    controller?.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
}