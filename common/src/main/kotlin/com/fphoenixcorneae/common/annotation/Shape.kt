package com.fphoenixcorneae.common.annotation

import android.graphics.drawable.GradientDrawable
import androidx.annotation.Keep

/**
 * @desc：形状类型
 * @date：2022/04/28 16:03
 */
@Keep
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FUNCTION)
@kotlin.annotation.Retention(AnnotationRetention.BINARY)
annotation class Shape {
    companion object {
        /** Shape is a rectangle, possibly with rounded corners */
        const val RECTANGLE = GradientDrawable.RECTANGLE

        /** Shape is an ellipse */
        const val OVAL = GradientDrawable.OVAL

        /** Shape is a line */
        const val LINE = GradientDrawable.LINE

        /** Shape is a ring. */
        const val RING = GradientDrawable.RING
    }
}