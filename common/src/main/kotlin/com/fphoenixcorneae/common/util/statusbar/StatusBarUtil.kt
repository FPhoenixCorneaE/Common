package com.fphoenixcorneae.common.util.statusbar

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import com.fphoenixcorneae.common.ext.isMeizu
import com.fphoenixcorneae.common.ext.isOppo
import com.fphoenixcorneae.common.ext.isXiaomi

/**
 * 状态栏工具类
 */
object StatusBarUtil {

    @SuppressLint("ObsoleteSdkInt")
    fun supportTransparentStatusBar(): Boolean {
        return (isXiaomi
                || isMeizu
                || (isOppo && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                || Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
    }

    /**
     * 设置状态栏透明
     *
     * @param window
     */
    @SuppressLint("ObsoleteSdkInt")
    fun transparentStatusBar(window: Window) {
        when {
            isXiaomi || isMeizu -> {
                when {
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {
                        transparentStatusBarAbove21(window)
                    }
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT -> {
                        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                    }
                }
            }
            isOppo && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {
                transparentStatusBarAbove21(window)
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                transparentStatusBarAbove21(window)
            }
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun transparentStatusBarAbove21(window: Window) {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
        window.statusBarColor = Color.TRANSPARENT
    }

    /**
     * 设置状态栏图标白色主题
     *
     * @param window
     */
    fun setLightMode(window: Window) {
        when {
            isXiaomi -> setMIUIStatusBarDarkMode(window, false)
            isMeizu -> setFlymeStatusBarDarkMode(window, false)
            isOppo -> setOppoStatusBarDarkMode(window, false)
            else -> setStatusBarDarkMode(window, false)
        }
    }

    /**
     * 设置状态栏图片黑色主题
     *
     * @param window
     */
    fun setDarkMode(window: Window) {
        when {
            isXiaomi -> setMIUIStatusBarDarkMode(window, true)
            isMeizu -> setFlymeStatusBarDarkMode(window, true)
            isOppo -> setOppoStatusBarDarkMode(window, true)
            else -> setStatusBarDarkMode(window, true)
        }
    }

    private fun setStatusBarDarkMode(
        window: Window,
        darkMode: Boolean,
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = if (darkMode) {
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            } else {
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            }
        }
    }

    @SuppressLint("PrivateApi")
    private fun setMIUIStatusBarDarkMode(
        window: Window,
        darkMode: Boolean,
    ) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            val clazz: Class<out Window> = window.javaClass
            try {
                val layoutParams =
                    Class.forName("android.view.MiuiWindowManager\$LayoutParams")
                val field =
                    layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
                val darkModeFlag = field.getInt(layoutParams)
                val extraFlagField = clazz.getMethod(
                    "setExtraFlags",
                    Int::class.javaPrimitiveType,
                    Int::class.javaPrimitiveType
                )
                extraFlagField.invoke(window, if (darkMode) darkModeFlag else 0, darkModeFlag)
            } catch (e: Exception) {
            }
        }
        setStatusBarDarkMode(window, darkMode)
    }

    private fun setFlymeStatusBarDarkMode(
        window: Window,
        darkMode: Boolean,
    ) {
        FlymeStatusBarUtil.setStatusBarDarkIcon(window, darkMode)
    }

    private const val SYSTEM_UI_FLAG_OP_STATUS_BAR_TINT = 0x00000010

    @SuppressLint("ObsoleteSdkInt")
    private fun setOppoStatusBarDarkMode(
        window: Window,
        darkMode: Boolean,
    ) {
        var vis = window.decorView.systemUiVisibility
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                vis = if (darkMode) {
                    vis or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                } else {
                    vis and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
                }
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {
                vis = if (darkMode) {
                    vis or SYSTEM_UI_FLAG_OP_STATUS_BAR_TINT
                } else {
                    vis and SYSTEM_UI_FLAG_OP_STATUS_BAR_TINT.inv()
                }
            }
        }
        window.decorView.systemUiVisibility = vis
    }

    /**
     * 沉浸式状态栏
     * 设置状态栏颜色和透明度
     * @param color 颜色
     * @param alpha 透明度
     */
    @SuppressLint("ObsoleteSdkInt")
    fun setStatusBarColor(
        window: Window,
        @ColorInt color: Int,
        @FloatRange(from = 0.0, to = 1.0) alpha: Float = 1f,
    ) {
        if (Build.VERSION.SDK_INT >= 21) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = calculateStatusColor(color, alpha)
            var systemUiVisibility = window.decorView.systemUiVisibility
            systemUiVisibility = systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            systemUiVisibility = systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.decorView.systemUiVisibility = systemUiVisibility
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            setTranslucentView(window.decorView as ViewGroup, color, alpha)
        }
    }

    /**
     * 创建假的透明栏
     */
    @SuppressLint("ObsoleteSdkInt")
    private fun setTranslucentView(
        container: ViewGroup,
        @ColorInt color: Int,
        @FloatRange(from = 0.0, to = 1.0) alpha: Float,
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val mixtureColor = calculateStatusColor(color, alpha)
            var translucentView: View? = container.findViewById(android.R.id.custom)
            if (translucentView == null && mixtureColor != 0) {
                translucentView = View(container.context)
                translucentView.id = android.R.id.custom
                val lp = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(container.context)
                )
                container.addView(translucentView, lp)
            }
            translucentView?.setBackgroundColor(mixtureColor)
        }
    }

    /**
     * 增加View的高度以及paddingTop,增加的值为状态栏高度.一般是在沉浸式全屏给ToolBar用的
     */
    @SuppressLint("ObsoleteSdkInt")
    fun setSmartPadding(context: Context, view: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val lp = view.layoutParams
            if (lp != null && lp.height > 0) {
                // 增高
                lp.height += getStatusBarHeight(context)
            }
            view.setPadding(
                view.paddingLeft, view.paddingTop + getStatusBarHeight(context),
                view.paddingRight, view.paddingBottom
            )
        }
    }

    /**
     * 增加View上边距（MarginTop）一般是给高度为 WARP_CONTENT 的小控件用的
     */
    @SuppressLint("ObsoleteSdkInt")
    fun setSmartMargin(context: Context, view: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val lp = view.layoutParams
            if (lp is ViewGroup.MarginLayoutParams) {
                // 增高
                lp.topMargin += getStatusBarHeight(context)
            }
            view.layoutParams = lp
        }
    }

    /**
     * 计算状态栏颜色
     *
     * @param color color值
     * @param alpha alpha值
     * @return 最终的状态栏颜色
     */
    private fun calculateStatusColor(
        @ColorInt color: Int,
        @FloatRange(from = 0.0, to = 1.0) alpha: Float,
    ): Int {
        val a = when {
            color and -0x1000000 == 0 -> 0xff
            else -> color.ushr(24)
        }
        return color and 0x00ffffff or ((a * alpha).toInt() shl 24)
    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    @SuppressLint("InternalInsetResource", "DiscouragedApi")
    @JvmStatic
    fun getStatusBarHeight(context: Context): Int {
        val resources = context.applicationContext.resources
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return resources.getDimensionPixelSize(resourceId)
    }

    /**
     * 获取导航栏高度
     *
     * @param context
     * @return
     */
    @SuppressLint("InternalInsetResource", "DiscouragedApi")
    fun getNavigationBarHeight(context: Context): Int {
        val resources = context.applicationContext.resources
        val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return resources.getDimensionPixelSize(resourceId)
    }

    /**
     * 检测是否有虚拟导航栏
     *
     * @param context
     * @return
     */
    @SuppressLint("PrivateApi", "DiscouragedApi")
    fun checkDeviceHasNavigationBar(context: Context): Boolean {
        var hasNavigationBar = false
        val rs = context.resources
        val id = rs.getIdentifier("config_showNavigationBar", "bool", "android")
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id)
        }
        try {
            val systemPropertiesClass = Class.forName("android.os.SystemProperties")
            val m = systemPropertiesClass.getMethod("get", String::class.java)
            val navBarOverride = m.invoke(systemPropertiesClass, "qemu.hw.mainkeys") as String
            if ("1" == navBarOverride) {
                hasNavigationBar = false
            } else if ("0" == navBarOverride) {
                hasNavigationBar = true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return hasNavigationBar
    }
}