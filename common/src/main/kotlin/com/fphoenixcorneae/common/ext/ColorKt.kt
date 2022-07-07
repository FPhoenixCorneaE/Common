package com.fphoenixcorneae.common.ext

import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import kotlin.math.roundToInt

/**
 * [ColorInt]矫正颜色的透明度
 */
@ColorInt
fun Int.adjustColorAlpha(
    @FloatRange(from = 0.0, to = 1.0)
    factor: Float
): Int =
    runCatching {
        val alpha = (Color.alpha(this@adjustColorAlpha) * factor).roundToInt()
        val red: Int = Color.red(this@adjustColorAlpha)
        val green: Int = Color.green(this@adjustColorAlpha)
        val blue: Int = Color.blue(this@adjustColorAlpha)
        Color.argb(alpha, red, green, blue)
    }.onFailure {
        it.printStackTrace()
    }.getOrDefault(0)

/**
 * [ColorInt]颜色基础上设置透明度
 * @param alpha 取值为[0,1]，0表示全透明，1表示不透明
 */
@ColorInt
fun Int.setColorAlpha(
    @FloatRange(from = 0.0, to = 1.0)
    alpha: Float,
): Int =
    runCatching {
        this@setColorAlpha and 0x00ffffff or ((alpha * 255f + 0.5f).toInt() shl 24)
    }.onFailure {
        it.printStackTrace()
    }.getOrDefault(0)

/**
 * [ColorInt]设置红色的alpha值
 * @param alpha 取值为[0,1]，0表示全透明，1表示不透明
 */
@ColorInt
fun Int.setRedAlpha(
    @FloatRange(from = 0.0, to = 1.0)
    alpha: Float
): Int =
    runCatching {
        this@setRedAlpha and -0xff0001 or ((alpha * 255f + 0.5f).toInt() shl 16)
    }.onFailure {
        it.printStackTrace()
    }.getOrDefault(0)

/**
 * [ColorInt]设置绿色的alpha值
 * @param alpha 取值为[0,1]，0表示全透明，1表示不透明
 */
@ColorInt
fun Int.setGreenAlpha(
    @FloatRange(from = 0.0, to = 1.0)
    alpha: Float
): Int =
    runCatching {
        this@setGreenAlpha and -0xff01 or ((alpha * 255f + 0.5f).toInt() shl 8)
    }.onFailure {
        it.printStackTrace()
    }.getOrDefault(0)

/**
 * [ColorInt]设置蓝色的alpha值
 * @param alpha 取值为[0,1]，0表示全透明，1表示不透明
 */
@ColorInt
fun Int.setBlueAlpha(
    @FloatRange(from = 0.0, to = 1.0)
    alpha: Float
): Int =
    runCatching {
        this@setBlueAlpha and -0x100 or (alpha * 255f + 0.5f).toInt()
    }.onFailure {
        it.printStackTrace()
    }.getOrDefault(0)

/**
 * 将[ColorInt]颜色值转换为十六进制字符串
 * @return `#AARRGGBB`
 */
fun Int.colorInt2HexString(): String =
    String.format("#%08X", this)

/**
 * 将十六进制字符串转换为[ColorInt]颜色值
 *
 * Supported formats are:
 * `#RRGGBB`
 * `#AARRGGBB`
 *
 * The following names are also accepted: `red`, `blue`,
 * `green`, `black`, `white`, `gray`,
 * `cyan`, `magenta`, `yellow`, `lightgray`,
 * `darkgray`, `grey`, `lightgrey`, `darkgrey`,
 * `aqua`, `fuchsia`, `lime`, `maroon`,
 * `navy`, `olive`, `purple`, `silver`,
 * and `teal`.
 */
@ColorInt
fun String.hexString2ColorInt(): Int =
    runCatching {
        Color.parseColor(this@hexString2ColorInt)
    }.onFailure {
        it.printStackTrace()
    }.getOrDefault(0)

/**
 * [ColorInt]加深颜色
 * @param factor The factor to darken the color.
 * @return darker version of specified color.
 */
@ColorInt
fun Int.darker(
    @FloatRange(from = 0.0, to = 1.0)
    factor: Float = 0.8f
): Int =
    runCatching {
        Color.argb(
            Color.alpha(this@darker),
            (Color.red(this@darker) * factor).toInt().coerceAtLeast(0),
            (Color.green(this@darker) * factor).toInt().coerceAtLeast(0),
            (Color.blue(this@darker) * factor).toInt().coerceAtLeast(0)
        )
    }.getOrDefault(0)

/**
 * [ColorInt]变浅颜色
 * @param factor The factor to lighten the color. 0 will make the color unchanged. 1 will make the color white.
 * @return lighter version of the specified color.
 */
@ColorInt
fun Int.lighter(
    factor: Float = 0.8f
): Int =
    runCatching {
        val red = ((Color.red(this@lighter) * (1 - factor) / 255 + factor) * 255).toInt()
        val green = ((Color.green(this@lighter) * (1 - factor) / 255 + factor) * 255).toInt()
        val blue = ((Color.blue(this@lighter) * (1 - factor) / 255 + factor) * 255).toInt()
        Color.argb(Color.alpha(this@lighter), red, green, blue)
    }.getOrDefault(0)

/**
 * [ColorInt]是否是深色的颜色
 */
fun Int.isDarkColor(): Boolean {
    val darkness =
        (1 - (0.299 * Color.red(this) + 0.587 * Color.green(this) + 0.114 * Color.blue(this)) / 255)
    return darkness >= 0.5
}

/**
 * Return the random color.
 *
 * @param supportAlpha True to support alpha, false otherwise.
 * @return the random color
 */
@ColorInt
fun getRandomColor(
    supportAlpha: Boolean = true
): Int {
    val high =
        when {
            supportAlpha -> (Math.random() * 0x100).toInt() shl 24
            else -> -0x1000000
        }
    return high or (Math.random() * 0x1000000).toInt()
}



