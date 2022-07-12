package com.fphoenixcorneae.common.annotation

import android.graphics.drawable.GradientDrawable
import androidx.annotation.Keep

/**
 * @desc：渐变类型
 * @date：2022/04/28 16:03
 */
@Keep
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FUNCTION)
@kotlin.annotation.Retention(AnnotationRetention.BINARY)
annotation class GradientType {
    companion object {
        /** Gradient is linear (default.) */
        const val LINEAR_GRADIENT = GradientDrawable.LINEAR_GRADIENT

        /** Gradient is circular. */
        const val RADIAL_GRADIENT = GradientDrawable.RADIAL_GRADIENT

        /** Gradient is a sweep. */
        const val SWEEP_GRADIENT = GradientDrawable.SWEEP_GRADIENT
    }
}