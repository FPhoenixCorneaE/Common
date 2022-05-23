package com.fphoenixcorneae.common.ext

import android.content.res.Resources
import android.util.TypedValue


/**
 * Int 类型： dp 转换为 px
 */
val Int.dp: Int
    get() =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            Resources.getSystem().displayMetrics
        ).toInt()

/**
 * Int 类型： dp 转换为 px
 */
val Int.Dp: Float
    get() =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            Resources.getSystem().displayMetrics
        )

/**
 * Float 类型： dp 转换为 px
 */
val Float.dp: Int
    get() =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this,
            Resources.getSystem().displayMetrics
        ).toInt()

/**
 * Float 类型： dp 转换为 px
 */
val Float.Dp: Float
    get() =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this,
            Resources.getSystem().displayMetrics
        )

/**
 * Double 类型： dp 转换为 px
 */
val Double.dp: Int
    get() =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            Resources.getSystem().displayMetrics
        ).toInt()

/**
 * Double 类型： dp 转换为 px
 */
val Double.Dp: Float
    get() =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            Resources.getSystem().displayMetrics
        )

/**
 * Int 类型： sp 转换为 px
 */
val Int.sp: Int
    get() =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            this.toFloat(),
            Resources.getSystem().displayMetrics
        ).toInt()

/**
 * Float 类型： sp 转换为 px
 */
val Float.sp: Int
    get() =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            this,
            Resources.getSystem().displayMetrics
        ).toInt()

/**
 * Double 类型： sp 转换为 px
 */
val Double.sp: Int
    get() =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            this.toFloat(),
            Resources.getSystem().displayMetrics
        ).toInt()

/**
 * Int 类型： px 转换为 dp
 */
fun Int.toDp(): Float = run {
    val scale = Resources.getSystem().displayMetrics.density
    this@toDp / scale + 0.5f
}

/**
 * Float 类型： px 转换为 dp
 */
fun Float.toDp(): Float = run {
    val scale = Resources.getSystem().displayMetrics.density
    this@toDp / scale + 0.5f
}

/**
 * Double 类型： px 转换为 dp
 */
fun Double.toDp(): Float = run {
    val scale = Resources.getSystem().displayMetrics.density
    this@toDp / scale + 0.5f
}.toFloat()

/**
 * Int 类型： px 转换为 sp
 */
fun Int.toSp(): Float = run {
    val fontScale = Resources.getSystem().displayMetrics.scaledDensity
    this@toSp / fontScale + 0.5f
}

/**
 * Float 类型： px 转换为 sp
 */
fun Float.toSp(): Float = run {
    val fontScale = Resources.getSystem().displayMetrics.scaledDensity
    this@toSp / fontScale + 0.5f
}

/**
 * Double 类型： px 转换为 sp
 */
fun Double.toSp(): Float = run {
    val fontScale = Resources.getSystem().displayMetrics.scaledDensity
    this@toSp / fontScale + 0.5f
}.toFloat()

