package com.fphoenixcorneae.common.ext

import android.R
import android.annotation.SuppressLint
import android.app.Activity
import android.os.*
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import com.fphoenixcorneae.common.ext.view.setFocus

private const val TAG_ON_GLOBAL_LAYOUT_LISTENER = -8
private const val TAG_FOCUS_VIEW = "TAG_KEYBOARD_FOCUS_VIEW"
typealias OnSoftInputChangedListener = (height: Int) -> Unit

/**
 * 软键盘是否可见
 */
val Activity.isSoftInputVisible: Boolean
    get() =
        getDecorViewInvisibleHeight() > 0

/**
 * 显示软键盘
 */
fun showSoftInput() =
    applicationContext.inputMethodManager?.run {
        toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

/**
 * 显示软键盘
 * @param flags Provides additional operating flags.  Currently may be 0
 * or have the [InputMethodManager.SHOW_IMPLICIT] bit set.
 */
fun View.showSoftInput(flags: Int = 0) =
    applicationContext.inputMethodManager?.run {
        setFocus(true)
        showSoftInput(this@showSoftInput, flags, object : ResultReceiver(Handler(Looper.getMainLooper())) {
            override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
                if (resultCode == InputMethodManager.RESULT_UNCHANGED_HIDDEN
                    || resultCode == InputMethodManager.RESULT_HIDDEN
                ) {
                    toggleSoftInput()
                }
            }
        })
        toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

/**
 * Hide the soft input.
 */
fun Activity.hideSoftInput() {
    var view = window.currentFocus
    if (view == null) {
        val decorView = window.decorView
        val focusView = decorView.findViewWithTag<View>(TAG_FOCUS_VIEW)
        if (focusView == null) {
            view = EditText(window.context)
            view.setTag(TAG_FOCUS_VIEW)
            (decorView as ViewGroup).addView(view, 0, 0)
        } else {
            view = focusView
        }
        view.requestFocus()
    }
    view.hideSoftInput()
}

/**
 * Hide the soft input.
 */
fun View.hideSoftInput() {
    applicationContext.inputMethodManager?.hideSoftInputFromWindow(windowToken, 0)
}

/**
 * 延迟隐藏虚拟键盘
 */
fun View.hideSoftInputDelayed(delayMillis: Long = 10) {
    postDelayed(
        { applicationContext.inputMethodManager?.hideSoftInputFromWindow(applicationWindowToken, 0) },
        delayMillis
    )
}

/**
 * 切换软键盘的状态
 * 如当前为收起变为弹出,若当前为弹出变为收起
 */
fun toggleSoftInput() {
    applicationContext.inputMethodManager?.toggleSoftInput(0, 0)
}

/**
 * Register soft input changed listener.
 * @param listener The soft input changed listener.
 */
fun Activity.registerSoftInputChangedListener(
    listener: OnSoftInputChangedListener
) {
    val flags = window.attributes.flags
    if (flags and WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS != 0) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }
    val contentView = window.findViewById<FrameLayout>(R.id.content)
    val decorViewInvisibleHeightPre = intArrayOf(getDecorViewInvisibleHeight())
    val onGlobalLayoutListener = OnGlobalLayoutListener {
        val height: Int = getDecorViewInvisibleHeight()
        if (decorViewInvisibleHeightPre[0] != height) {
            listener(height)
            decorViewInvisibleHeightPre[0] = height
        }
    }
    contentView.viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayoutListener)
    contentView.setTag(TAG_ON_GLOBAL_LAYOUT_LISTENER, onGlobalLayoutListener)
}

/**
 * Unregister soft input changed listener.
 */
@SuppressLint("ObsoleteSdkInt")
fun Activity.unregisterSoftInputChangedListener() {
    val contentView = window.findViewById<View>(R.id.content) ?: return
    val tag = contentView.getTag(TAG_ON_GLOBAL_LAYOUT_LISTENER)
    if (tag is OnGlobalLayoutListener) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            contentView.viewTreeObserver.removeOnGlobalLayoutListener(tag)
        }
    }
}

/**
 * Fix the bug of 5497 in Android.
 * It will clean the adjustResize
 */
fun Activity.fixAndroidBug5497() {
    val softInputMode = window.attributes.softInputMode
    window.setSoftInputMode(softInputMode and WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE.inv())
    val contentView = window.findViewById<FrameLayout>(R.id.content)
    val contentViewChild = contentView.getChildAt(0)
    val paddingBottom = contentViewChild.paddingBottom
    val contentViewInvisibleHeightPre5497 = intArrayOf(getContentViewInvisibleHeight())
    contentView.viewTreeObserver
        .addOnGlobalLayoutListener {
            val height: Int = getContentViewInvisibleHeight()
            if (contentViewInvisibleHeightPre5497[0] != height) {
                contentViewChild.setPadding(
                    contentViewChild.paddingLeft,
                    contentViewChild.paddingTop,
                    contentViewChild.paddingRight,
                    paddingBottom + getDecorViewInvisibleHeight()
                )
                contentViewInvisibleHeightPre5497[0] = height
            }
        }
}

/**
 * Fix the leaks of soft input.
 */
fun Activity.fixSoftInputLeaks() {
    val imm = applicationContext.inputMethodManager ?: return
    val leakViews = arrayOf("mLastSrvView", "mCurRootView", "mServedView", "mNextServedView")
    for (leakView in leakViews) {
        try {
            val leakViewField = InputMethodManager::class.java.getDeclaredField(leakView)
            if (!leakViewField.isAccessible) {
                leakViewField.isAccessible = true
            }
            val obj = leakViewField[imm] as? View ?: continue
            if (obj.rootView === window.decorView.rootView) {
                leakViewField[imm] = null
            }
        } catch (ignore: Throwable) {
            ignore.toString().logd()
        }
    }
}

/**
 * Click blank area to hide soft input(点击空白区域隐藏软键盘).
 * <p>Copy the following code in your activity.</p>
 */
fun clickBlankArea2HideSoftInput() {
    "Please refer to the following code.".logi()
    /*
    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText && !v.isTouchIntoArea(ev)) {
                hideSoftInput()
            }
        }
        return super.onTouchEvent(ev)
    }
    */
}

