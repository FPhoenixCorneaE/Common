package com.fphoenixcorneae.common.ext.view

import android.widget.ScrollView
import androidx.core.widget.NestedScrollView

/**
 * 滚动到最低部
 */
fun ScrollView.smoothScrollToBottom() {
    val lastChild = getChildAt(childCount - 1)
    val bottom = lastChild.bottom + paddingBottom
    val delta = bottom - (scrollY + height)
    smoothScrollBy(0, delta)
}

fun NestedScrollView.smoothScrollToBottom() {
    val lastChild = getChildAt(childCount - 1)
    val bottom = lastChild.bottom + paddingBottom
    val delta = bottom - (scrollY + height)
    smoothScrollBy(0, delta)
}