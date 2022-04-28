package com.fphoenixcorneae.common.ext

import android.content.res.AssetFileDescriptor
import android.content.res.ColorStateList
import android.content.res.Resources
import android.content.res.XmlResourceParser
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import java.io.InputStream


val appResources: Resources
    get() = applicationContext.resources

fun getLayoutIdByName(name: String): Int {
    return appResources.getIdentifier(name, "layout", appPackageName)
}

fun getStringIdByName(name: String): Int {
    return appResources.getIdentifier(name, "string", appPackageName)
}

fun getDrawableIdByName(name: String): Int {
    return appResources.getIdentifier(name, "drawable", appPackageName)
}

fun getMipmapIdByName(name: String): Int {
    return appResources.getIdentifier(name, "mipmap", appPackageName)
}

fun getStyleId(name: String): Int {
    return appResources.getIdentifier(name, "style", appPackageName)
}

fun getIdByName(name: String): Int {
    return appResources.getIdentifier(name, "id", appPackageName)
}

fun getColorIdByName(name: String): Int {
    return appResources.getIdentifier(name, "color", appPackageName)
}

fun getArrayIdByName(name: String): Int {
    return appResources.getIdentifier(name, "array", appPackageName)
}

fun getAnimIdByName(name: String): Int {
    return appResources.getIdentifier(name, "anim", appPackageName)
}

fun getMenuIdByName(name: String): Int {
    return appResources.getIdentifier(name, "menu", appPackageName)
}

/**
 * Get raw file, ui/raw/file
 */
fun getRaw(@RawRes resId: Int): InputStream {
    return appResources.openRawResource(resId)
}

/**
 * Get raw file descriptor, ui/raw/file. This function only works for resources that are stored in the package as
 * uncompressed data, which typically includes things like mp3 files and png images.
 */
fun getRawFd(@RawRes resId: Int): AssetFileDescriptor {
    return appResources.openRawResourceFd(resId)
}

/**
 * Get xml file, ui/xml/file
 */
fun getXml(@XmlRes resId: Int): XmlResourceParser {
    return appResources.getXml(resId)
}

/**
 * Get drawable, ui/drawable/file
 */
fun getDrawable(@DrawableRes resId: Int): Drawable? {
    return ContextCompat.getDrawable(applicationContext, resId)
}

/**
 * Get string, ui/values/__picker_strings.xml
 */
fun getString(@StringRes resId: Int): String {
    return appResources.getString(resId)
}

/**
 * Get string array, ui/values/__picker_strings.xml
 */
fun getStringArray(@ArrayRes resId: Int): Array<String> {
    return appResources.getStringArray(resId)
}

/**
 * Get int array, ui/values/__picker_strings.xml
 */
fun getIntArray(@ArrayRes resId: Int): IntArray {
    return appResources.getIntArray(resId)
}

/**
 * Get color, ui/values/__picker_colors.xml
 */
fun getColor(@ColorRes resId: Int): Int {
    return ContextCompat.getColor(applicationContext, resId)
}

/**
 * Get font
 */
fun getFont(@FontRes resId: Int): Typeface? {
    return runCatching {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            appResources.getFont(resId)
        } else {
            ResourcesCompat.getFont(applicationContext, resId)
        }
    }.onFailure {
        it.printStackTrace()
    }.getOrNull()
}

/**
 * Get color state list, ui/values/__picker_colors.xml
 */
fun getColorStateList(@ColorRes resId: Int): ColorStateList? {
    return ContextCompat.getColorStateList(applicationContext, resId)
}

/**
 * Get dimension, ui/values/dimens.xml
 *
 * @return View dimension value multiplied by the appropriate metric.
 * 获取某个dimen的值,如果是dp或sp的单位,将其乘以density,如果是px,则不乘   返回float
 */
fun getDimension(@DimenRes resId: Int): Float {
    return appResources.getDimension(resId)
}

/**
 * Get dimension, ui/values/dimens.xml
 *
 * @return View dimension value multiplied by the appropriate metric and truncated to integer pixels.
 * 获取某个dimen的值,如果是dp或sp的单位,将其乘以density,如果是px,则不乘  返回int
 */
fun getDimensionPixelOffset(@DimenRes resId: Int): Int {
    return appResources.getDimensionPixelOffset(resId)
}

/**
 * Get dimension, ui/values/dimens.xml
 *
 * @return View dimension value multiplied by the appropriate metric and truncated to integer pixels.
 * 不管写的是dp还是sp还是px,都会乘以density.
 */
fun getDimensionPixelSize(@DimenRes resId: Int): Int {
    return appResources.getDimensionPixelSize(resId)
}
