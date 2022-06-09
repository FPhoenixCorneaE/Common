package com.fphoenixcorneae.common.demo

import android.os.SystemClock
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.fphoenixcorneae.common.ext.view.getTintDrawable
import com.fphoenixcorneae.common.ext.view.load

@BindingAdapter(value = ["android:layout_marginTop"], requireAll = false)
fun setMargins(view: View, marginTop: Int) {
    if (view.layoutParams is ViewGroup.MarginLayoutParams) {
        (view.layoutParams as ViewGroup.MarginLayoutParams).apply {
            topMargin = marginTop
        }
    }
}

@BindingAdapter(value = ["tint"], requireAll = false)
fun setTintColor(imageView: ImageView, tintColor: Int) {
    if (tintColor != 0) {
        imageView.setImageDrawable(getTintDrawable(tintColor, imageView.drawable))
    }
}

/**
 * 加载图片
 */
@BindingAdapter(value = ["imgUrl", "isCircle"], requireAll = false)
fun setImgUrl(imageView: ImageView, imgUrl: Any?, isCircle: Boolean) {
    imageView.load(imgUrl, isCircle = isCircle)
}

@BindingAdapter("visible")
fun isVisible(view: View, isVisible: Boolean) {
    view.isVisible = isVisible
}

@BindingAdapter("selected")
fun isSelected(view: View, selected: Boolean) {
    view.isSelected = selected
}

@BindingAdapter(value = ["onSingleClick"], requireAll = false)
fun setOnSingleClick(
    view: View,
    onClickListener: View.OnClickListener
) {
    val hits = LongArray(2)
    view.setOnClickListener {
        System.arraycopy(hits, 1, hits, 0, hits.size - 1)
        hits[hits.size - 1] = SystemClock.uptimeMillis()
        if (hits[0] < SystemClock.uptimeMillis() - 500) {
            onClickListener.onClick(it)
        }
    }
}

/**
 * 设置多次点击监听
 * @param onClickListener 点击监听
 * @param clickTimes      点击次数
 * @param duration        有效时间
 */
@BindingAdapter(
    value = [
        "onMultiClickTimes",
        "onMultiClickValidDuration",
        "onMultiClick",
    ],
    requireAll = false
)
fun setOnMultiClick(
    view: View,
    clickTimes: Int,
    duration: Long,
    onClickListener: View.OnClickListener
) {
    var tempClickTimes = clickTimes
    if (tempClickTimes <= 2) {
        tempClickTimes = 2
    }
    var tempDuration = duration
    if (tempDuration <= 1_000) {
        tempDuration = 1_000
    }
    var hits = LongArray(tempClickTimes)
    view.setOnClickListener {
        // 将hits数组内的所有元素左移一个位置
        System.arraycopy(hits, 1, hits, 0, hits.size - 1)
        // 获得当前系统已经启动的时间
        hits[hits.size - 1] = SystemClock.uptimeMillis()
        if (hits[0] >= SystemClock.uptimeMillis() - tempDuration) {
            // 相关逻辑操作
            onClickListener.onClick(it)
            // 初始化点击次数
            hits = LongArray(tempClickTimes)
        }
    }
}