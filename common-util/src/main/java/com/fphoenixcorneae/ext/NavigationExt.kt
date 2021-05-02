package com.fphoenixcorneae.ext

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.Navigator
import androidx.navigation.fragment.NavHostFragment
import com.fphoenixcorneae.util.R

fun Activity.navigate(
    @IdRes viewId: Int,
    @IdRes resId: Int,
    args: Bundle? = null,
    navOptions: NavOptions? = navOptionsBuilder.build(),
    navigatorExtras: Navigator.Extras? = null
) {
    return Navigation.findNavController(this, viewId)
        .navigateTo(resId, args, navOptions, navigatorExtras)
}

fun Activity.navigateUp(
    @IdRes viewId: Int
): Boolean {
    return Navigation.findNavController(this, viewId)
        .navigateUp()
}

fun Fragment.navigate(
    @IdRes resId: Int,
    args: Bundle? = null,
    navOptions: NavOptions? = navOptionsBuilder.build(),
    navigatorExtras: Navigator.Extras? = null
) {
    return NavHostFragment.findNavController(this)
        .navigateTo(resId, args, navOptions, navigatorExtras)
}

fun Fragment.navigateUp(): Boolean {
    return NavHostFragment.findNavController(this)
        .navigateUp()
}

fun Fragment.popBackStack(): Boolean {
    return NavHostFragment.findNavController(this)
        .popBackStack()
}

fun View.navigate(
    @IdRes resId: Int,
    args: Bundle? = null,
    navOptions: NavOptions? = navOptionsBuilder.build(),
    navigatorExtras: Navigator.Extras? = null
) {
    return Navigation.findNavController(this).navigateTo(resId, args, navOptions, navigatorExtras)
}

fun View.navigateUp(): Boolean {
    return Navigation.findNavController(this)
        .navigateUp()
}

var lastNavTime = 0L

/**
 * 防止短时间内多次快速跳转Fragment出现的bug
 * @param resId     跳转的action Id
 * @param bundle    传递的参数
 * @param interval  多少毫秒内不可重复点击 默认0.5秒
 */
fun NavController.navigateTo(
    resId: Int,
    bundle: Bundle? = null,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null,
    interval: Long = 500
) {
    val currentTime = System.currentTimeMillis()
    if (currentTime >= lastNavTime + interval) {
        lastNavTime = currentTime
        try {
            navigate(resId, bundle, navOptions, navigatorExtras)
        } catch (ignore: Exception) {
            //防止出现 当 fragment 中 action 的 duration设置为 0 时，连续点击两个不同的跳转会导致如下崩溃 #issue53
            ignore.logd()
        }
    }
}

val navOptionsBuilder = NavOptions.Builder()
    .setEnterAnim(R.anim.fragment_fade_enter)
    .setExitAnim(R.anim.fragment_fade_exit)
    .setPopEnterAnim(R.anim.fragment_fade_enter)
    .setPopExitAnim(R.anim.fragment_fade_exit)
    .setLaunchSingleTop(true)