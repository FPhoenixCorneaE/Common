package com.fphoenixcorneae.common.ext

import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.fphoenixcorneae.common.R

fun Fragment.navigate(
    @IdRes resId: Int,
    args: Bundle? = null,
    navOptions: NavOptions? = navOptionsBuilder.build(),
    navigatorExtras: Navigator.Extras? = null
) {
    return findNavController()
        .navigateTo(resId, args, navOptions, navigatorExtras)
}

fun Fragment.navigateUp(): Boolean {
    return findNavController()
        .navigateUp()
}

fun Fragment.popBackStack(): Boolean {
    return findNavController()
        .popBackStack()
}

fun Fragment.popBackStack(
    @IdRes destinationId: Int,
    inclusive: Boolean = false
): Boolean {
    return findNavController()
        .popBackStack(destinationId, inclusive)
}

fun View.navigate(
    @IdRes resId: Int,
    args: Bundle? = null,
    navOptions: NavOptions? = navOptionsBuilder.build(),
    navigatorExtras: Navigator.Extras? = null
) {
    return findNavController()
        .navigateTo(resId, args, navOptions, navigatorExtras)
}

fun View.navigateUp(): Boolean {
    return findNavController()
        .navigateUp()
}

fun View.popBackStack(): Boolean {
    return findNavController()
        .popBackStack()
}

fun View.popBackStack(
    @IdRes destinationId: Int,
    inclusive: Boolean = false
): Boolean {
    return findNavController()
        .popBackStack(destinationId, inclusive)
}

var lastNavTime = 0L
var lastResId = 0

/**
 * 防止短时间内多次快速跳转 Fragment 出现的 bug
 * @param resId     跳转的action Id
 * @param bundle    传递的参数
 * @param interval  多少毫秒内不可重复点击 默认 0.5 秒
 */
fun NavController.navigateTo(
    resId: Int,
    bundle: Bundle? = null,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null,
    interval: Long = 500
) {
    val currentTime = System.currentTimeMillis()
    if (resId == lastResId && currentTime - lastNavTime < interval) {
        return
    }

    try {
        navigate(resId, bundle, navOptions, navigatorExtras)
    } catch (ignore: Exception) {
        // 防止出现 当 fragment 中 action 的 duration设置为 0 时，连续点击两个不同的跳转会导致如下崩溃 #issue53
        ignore.printStackTrace()
    } finally {
        lastNavTime = currentTime
        lastResId = resId
    }
}

val navOptionsBuilder = NavOptions.Builder()
    .setEnterAnim(R.anim.nav_default_enter_anim)
    .setExitAnim(R.anim.nav_default_exit_anim)
    .setPopEnterAnim(R.anim.nav_default_pop_enter_anim)
    .setPopExitAnim(R.anim.nav_default_pop_exit_anim)
    .setLaunchSingleTop(true)