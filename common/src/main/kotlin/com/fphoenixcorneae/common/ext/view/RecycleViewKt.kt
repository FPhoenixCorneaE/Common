package com.fphoenixcorneae.common.ext.view

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.fphoenixcorneae.common.ext.dp

fun RecyclerView.itemPadding(top: Int, bottom: Int, left: Int = 0, right: Int = 0) {
    addItemDecoration(PaddingItemDecoration(top, bottom, left, right))
}

class PaddingItemDecoration(
    top: Int,
    bottom: Int,
    left: Int,
    right: Int
) : RecyclerView.ItemDecoration() {

    private val mTop = top
    private val mBottom = bottom
    private val mLeft = left
    private val mRight = right

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect[mLeft.dp, mTop.dp, mRight.dp] = mBottom.dp
    }
}
