package com.fphoenixcorneae.common.ext.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.view.*
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.PopupWindow
import androidx.annotation.ColorRes
import androidx.annotation.Px
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.graphics.applyCanvas
import androidx.core.view.ViewCompat
import com.fphoenixcorneae.common.ext.dp
import kotlin.math.hypot

fun View.visible() = run { visibility = View.VISIBLE }

fun View.invisible() = run { visibility = View.INVISIBLE }

fun View.gone() = run { visibility = View.GONE }

var View.isVisible: Boolean
    get() = visibility == View.VISIBLE
    set(value) = if (value) visible() else gone()

var View.isInvisible: Boolean
    get() = visibility == View.INVISIBLE
    set(value) = if (value) invisible() else visible()

var View.isGone: Boolean
    get() = visibility == View.GONE
    set(value) = if (value) gone() else visible()

/**
 * 设置某个View的margins
 * @param isDp   需要设置的数值是否为DP
 * @param left   左边距
 * @param top    上边距
 * @param right  右边距
 * @param bottom 下边距
 */
fun View.setMargins(
    isDp: Boolean,
    left: Float,
    top: Float,
    right: Float,
    bottom: Float
): ViewGroup.LayoutParams {
    // 获取view的margin设置参数
    val marginParams: ViewGroup.MarginLayoutParams =
        when (val params = layoutParams) {
            is ViewGroup.MarginLayoutParams -> params
            // 不存在时创建一个新的参数
            else -> ViewGroup.MarginLayoutParams(params)
        }
    // 根据DP与PX转换计算值
    val leftPx: Int
    val rightPx: Int
    val topPx: Int
    val bottomPx: Int
    when {
        isDp -> {
            leftPx = left.dp
            topPx = top.dp
            rightPx = right.dp
            bottomPx = bottom.dp
        }
        else -> {
            leftPx = left.toInt()
            rightPx = right.toInt()
            topPx = top.toInt()
            bottomPx = bottom.toInt()
        }
    }
    // 设置 margins
    marginParams.setMargins(leftPx, topPx, rightPx, bottomPx)
    layoutParams = marginParams
    return marginParams
}

/**
 * 设置内边距
 * @param size 大小,单位：px
 */
fun View.setPadding(@Px size: Int) {
    setPadding(size, size, size, size)
}

/**
 * 设置某个View的padding
 * @param isDp   需要设置的数值是否为DP
 * @param left   左边距
 * @param top    上边距
 * @param right  右边距
 * @param bottom 下边距
 */
fun View.setPadding(
    isDp: Boolean,
    left: Float,
    top: Float,
    right: Float,
    bottom: Float
) {
    // 根据DP与PX转换计算值
    val leftPx: Int
    val rightPx: Int
    val topPx: Int
    val bottomPx: Int
    when {
        isDp -> {
            leftPx = left.dp
            topPx = top.dp
            rightPx = right.dp
            bottomPx = bottom.dp
        }
        else -> {
            leftPx = left.toInt()
            rightPx = right.toInt()
            topPx = top.toInt()
            bottomPx = bottom.toInt()
        }
    }
    // 设置padding
    setPadding(leftPx, topPx, rightPx, bottomPx)
}

inline fun View.postDelayed(delayInMillis: Long, crossinline action: () -> Unit): Runnable {
    val runnable = Runnable { action() }
    postDelayed(runnable, delayInMillis)
    return runnable
}

/**
 * 生成位图
 * @param config 配置,默认：[Bitmap.Config.ARGB_8888]
 */
fun View.createBitmap(config: Bitmap.Config = Bitmap.Config.ARGB_8888): Bitmap {
    if (!ViewCompat.isLaidOut(this)) {
        throw IllegalStateException("View needs to be laid out before calling toBitmap()")
    }
    return Bitmap.createBitmap(width, height, config).applyCanvas {
        translate(-scrollX.toFloat(), -scrollY.toFloat())
        draw(this)
    }
}

var lastClickTime = 0L

/**
 * 防止重复点击事件 默认 1 秒内不可重复点击
 * @param interval 时间间隔 默认：1 秒
 * @param action   执行方法
 */
fun View.clickNoRepeat(
    interval: Long = 1000,
    action: (view: View) -> Unit
) {
    setOnClickListener {
        val currentTime = System.currentTimeMillis()
        if (lastClickTime != 0L && currentTime - lastClickTime < interval) {
            return@setOnClickListener
        }
        lastClickTime = currentTime
        action(it)
    }
}

/**
 * 设置防止重复点击事件
 * @param views 需要设置点击事件的 view 集合
 * @param interval 时间间隔 默认：1 秒
 * @param onClick 点击触发的方法
 */
fun setOnClickNoRepeat(
    vararg views: View?,
    interval: Long = 1000,
    onClick: (View) -> Unit
) {
    views.forEach {
        it?.clickNoRepeat(interval) { view ->
            onClick.invoke(view)
        }
    }
}

/**
 * @desc：揭露动画监听器
 * @date：2020-06-28 17:22
 */
interface OnRevealAnimationListener {
    fun onRevealHide() {}

    fun onRevealShow() {}
}

/**
 * 揭露动画显示
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun View.animateRevealShow(
    startRadius: Int,
    @ColorRes color: Int,
    onRevealAnimationListener: OnRevealAnimationListener? = null
) {
    val cx = (left + right) / 2
    val cy = (top + bottom) / 2

    val finalRadius = hypot(width.toDouble(), height.toDouble()).toFloat()

    // 设置圆形显示动画
    val anim = ViewAnimationUtils.createCircularReveal(
        this,
        cx,
        cy,
        startRadius.toFloat(),
        finalRadius
    )
    anim.duration = 300
    anim.interpolator = AccelerateDecelerateInterpolator()
    anim.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            super.onAnimationEnd(animation)
            visible()
            onRevealAnimationListener?.onRevealShow()
        }

        override fun onAnimationStart(animation: Animator) {
            super.onAnimationStart(animation)
            setBackgroundColor(ContextCompat.getColor(context, color))
        }
    })
    anim.start()
}

/**
 * 圆圈凝聚效果
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
fun View.animateRevealHide(
    finalRadius: Int,
    @ColorRes color: Int,
    onRevealAnimationListener: OnRevealAnimationListener? = null
) {
    val cx = (left + right) / 2
    val cy = (top + bottom) / 2
    val initialRadius = width
    // 与入场动画的区别就是圆圈起始和终止的半径相反
    val anim = ViewAnimationUtils.createCircularReveal(
        this,
        cx,
        cy,
        initialRadius.toFloat(),
        finalRadius.toFloat()
    )
    anim.duration = 300
    anim.interpolator = AccelerateDecelerateInterpolator()
    anim.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationStart(animation: Animator) {
            super.onAnimationStart(animation)
            setBackgroundColor(ContextCompat.getColor(context, color))
        }

        override fun onAnimationEnd(animation: Animator) {
            super.onAnimationEnd(animation)
            setBackgroundColor(Color.TRANSPARENT)
            onRevealAnimationListener?.onRevealHide()
            invisible()
        }
    })
    anim.start()
}

/**
 * 获取 view 的上下文
 * @return Activity
 */
fun View.getActivity(): Activity {
    var context = context
    while (context is ContextWrapper) {
        if (context is Activity) {
            return context
        }
        context = context.baseContext
    }
    throw IllegalStateException("View $this is not attached to an Activity.")
}

/**
 * 视图请求焦点
 */
fun View.setFocus(b: Boolean) {
    isFocusable = b
    isFocusableInTouchMode = b
    if (b) {
        requestFocus()
    } else {
        clearFocus()
    }
}

/**
 * 触摸区域是否在View里边
 */
fun View.touchInside(motionEvent: MotionEvent): Boolean =
    run {
        val outLocation = intArrayOf(0, 0)
        getLocationOnScreen(outLocation)
        val left = outLocation[0]
        val top = outLocation[1]
        val right = left + width
        val bottom = top + height
        val motionX = motionEvent.rawX
        val motionY = motionEvent.rawY
        motionX >= left && motionX <= right && motionY >= top && motionY <= bottom
    }

/**
 * 把自身从父View中移除
 */
fun View.removeSelfFromParent() {
    val parent = parent
    if (parent is ViewGroup) {
        parent.removeView(this)
    }
}

/**
 * 要求父View layout
 */
fun View.requestLayoutParent(isAll: Boolean) {
    var parent = parent
    while (parent is View) {
        if (!parent.isLayoutRequested()) {
            parent.requestLayout()
            if (!isAll) {
                break
            }
        }
        parent = parent.getParent()
    }
}

/**
 * 测量view
 */
fun View.measure() {
    var p = layoutParams
    if (p == null) {
        p = ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
    }
    val childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0, p.width)
    val lpHeight = p.height
    val childHeightSpec: Int = if (lpHeight > 0) {
        View.MeasureSpec.makeMeasureSpec(lpHeight, View.MeasureSpec.EXACTLY)
    } else {
        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    }
    measure(childWidthSpec, childHeightSpec)
}

/**
 * 获取View测量后的宽度
 */
val View.measureWidth: Int
    get() {
        measure()
        return measuredWidth
    }

/**
 * 获取View测量后的高度
 */
val View.measureHeight: Int
    get() {
        measure()
        return measuredHeight
    }

var popupWindow: PopupWindow? = null

/**
 * 以View为锚点显示PopupWindow
 * @param context
 * @param resId
 * @param paramsType
 */
fun View.showPopupWindow(
    context: Context,
    resId: Int,
    paramsType: Int
): View {
    val popupView: View = LayoutInflater.from(context).inflate(resId, null)
    popupWindow = when (paramsType) {
        1 -> PopupWindow(popupView, MATCH_PARENT, MATCH_PARENT, true)
        2 -> PopupWindow(popupView, MATCH_PARENT, WRAP_CONTENT, true)
        3 -> PopupWindow(popupView, WRAP_CONTENT, MATCH_PARENT, true)
        4 -> PopupWindow(popupView, WRAP_CONTENT, WRAP_CONTENT, true)
        else -> PopupWindow(popupView, MATCH_PARENT, MATCH_PARENT, true)
    }
    popupWindow!!.isFocusable = true
    popupWindow!!.isOutsideTouchable = true
    popupWindow!!.isTouchable = true
    popupWindow!!.setBackgroundDrawable(BitmapDrawable())
    popupWindow!!.showAsDropDown(this)
    return popupView
}

/**
 * 关闭PopupWindow
 */
fun dismissPopup() {
    if (popupWindow?.isShowing == true) {
        popupWindow?.dismiss()
        popupWindow = null
    }
}