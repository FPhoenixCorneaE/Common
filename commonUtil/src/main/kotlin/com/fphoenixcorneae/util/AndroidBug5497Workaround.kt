package com.fphoenixcorneae.util

import android.R
import android.app.Activity
import android.content.res.Resources
import android.graphics.Rect
import android.os.Build
import android.view.View
import android.widget.FrameLayout

/**
 * 解决 WebView 内输入框被软键盘遮挡
 * @since 2020/12/4 17:40
 */
class AndroidBug5497Workaround private constructor(activity: Activity) {
    private val mChildOfContent: View
    private var usableHeightPrevious = 0
    private val frameLayoutParams: FrameLayout.LayoutParams
    private var contentHeight = 0
    private var isfirst = true
    private val activity: Activity
    private val statusBarHeight: Int

    //重新调整跟布局的高度
    private fun possiblyResizeChildOfContent() {
        val usableHeightNow = computeUsableHeight()

        //当前可见高度和上一次可见高度不一致 布局变动
        if (usableHeightNow != usableHeightPrevious) {
            //int usableHeightSansKeyboard2 = mChildOfContent.getHeight();//兼容华为等机型
            val usableHeightSansKeyboard = mChildOfContent.rootView.height
            val heightDifference = usableHeightSansKeyboard - usableHeightNow
            if (heightDifference > usableHeightSansKeyboard / 4) {
                // keyboard probably just became visible
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    //frameLayoutParams.height = usableHeightSansKeyboard - heightDifference;
                    frameLayoutParams.height = usableHeightSansKeyboard - heightDifference + statusBarHeight
                } else {
                    frameLayoutParams.height = usableHeightSansKeyboard - heightDifference
                }
            } else {
                frameLayoutParams.height = contentHeight
            }
            mChildOfContent.requestLayout()
            usableHeightPrevious = usableHeightNow
        }
    }

    /**
     * 计算mChildOfContent可见高度
     */
    private fun computeUsableHeight(): Int {
        val r = Rect()
        mChildOfContent.getWindowVisibleDisplayFrame(r)
        return r.bottom - r.top
    }

    companion object {
        fun assistActivity(activity: Activity) {
            AndroidBug5497Workaround(activity)
        }
    }

    init {
        // 获取状态栏的高度
        val resources = Resources.getSystem()
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        statusBarHeight = resources.getDimensionPixelSize(resourceId)
        this.activity = activity
        val content = activity.findViewById<View>(R.id.content) as FrameLayout
        mChildOfContent = content.getChildAt(0)

        //界面出现变动都会调用这个监听事件
        mChildOfContent.viewTreeObserver.addOnGlobalLayoutListener {
            if (isfirst) {
                contentHeight = mChildOfContent.height //兼容华为等机型
                isfirst = false
            }
            possiblyResizeChildOfContent()
        }
        frameLayoutParams = mChildOfContent.layoutParams as FrameLayout.LayoutParams
    }
}