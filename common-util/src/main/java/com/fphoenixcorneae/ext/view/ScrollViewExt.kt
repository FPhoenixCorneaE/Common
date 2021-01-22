package com.fphoenixcorneae.ext.view

import android.widget.ScrollView

/**
 * 滚动到最低部
 */
fun ScrollView.smoothScrollToBottom() {
    val lastChild = getChildAt(childCount - 1)
    val bottom = lastChild.bottom + paddingBottom
    val delta = bottom - (scrollY + height)
    smoothScrollBy(0, delta)
}