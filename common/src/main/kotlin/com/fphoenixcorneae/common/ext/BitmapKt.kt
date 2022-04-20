package com.fphoenixcorneae.common.ext

import android.graphics.Bitmap
import android.graphics.Matrix

/**
 * 位图缩放
 * @param scale 缩放比例
 */
fun Bitmap.scale(scale: Float): Bitmap {
    val bmpWidth = width
    val bmpHeight = height
    val matrix = Matrix()
    matrix.postScale(scale, scale)
    return Bitmap.createBitmap(this, 0, 0, bmpWidth, bmpHeight, matrix, true)
}