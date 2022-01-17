package com.fphoenixcorneae.common.util

import android.content.res.AssetFileDescriptor
import android.content.res.ColorStateList
import android.content.res.XmlResourceParser
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.fphoenixcorneae.common.ext.appContext
import com.fphoenixcorneae.common.ext.appPackageName
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

/**
 * @desc: 资源工具类
 */
class ResourceUtil private constructor() {

    init {
        throw UnsupportedOperationException("U can't initialize me...")
    }

    /**
     * Get raw text file, ui/raw/text
     */
    fun getRawText(@RawRes resId: Int): String? {
        try {
            val inputReader = InputStreamReader(getRaw(resId))
            val bufReader = BufferedReader(inputReader)
            var line: String?
            val result = StringBuilder()
            while (
                run {
                    line = bufReader.readLine()
                    line
                } != null
            ) {
                result.append(line)
            }
            return result.toString()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }

    companion object {

        fun getLayoutId(name: String): Int {
            return appContext.resources.getIdentifier(
                name, "layout",
                appPackageName
            )
        }

        fun getStringId(name: String): Int {
            return appContext.resources.getIdentifier(
                name, "string",
                appPackageName
            )
        }

        fun getDrawableId(name: String): Int {
            return appContext.resources.getIdentifier(
                name, "drawable",
                appPackageName
            )
        }

        fun getMipmapId(name: String): Int {
            return appContext.resources.getIdentifier(
                name, "mipmap",
                appPackageName
            )
        }

        fun getStyleId(name: String): Int {
            return appContext.resources.getIdentifier(
                name, "style",
                appPackageName
            )
        }

        fun getId(name: String): Int {
            return appContext.resources.getIdentifier(
                name, "id",
                appPackageName
            )
        }

        fun getColorId(name: String): Int {
            return appContext.resources.getIdentifier(
                name, "color",
                appPackageName
            )
        }

        fun getArrayId(name: String): Int {
            return appContext.resources.getIdentifier(
                name, "array",
                appPackageName
            )
        }

        /**
         * Get raw file, ui/raw/file
         */
        fun getRaw(@RawRes resId: Int): InputStream {
            return appContext.resources.openRawResource(resId)
        }

        /**
         * Get raw file descriptor, ui/raw/file. This function only works for resources that are stored in the package as
         * uncompressed data, which typically includes things like mp3 files and png images.
         */
        fun getRawFd(@RawRes resId: Int): AssetFileDescriptor {
            return appContext.resources.openRawResourceFd(resId)
        }

        /**
         * Get xml file, ui/xml/file
         */
        fun getXml(@XmlRes resId: Int): XmlResourceParser {
            return appContext.resources.getXml(resId)
        }

        /**
         * Get drawable, ui/drawable/file
         */
        fun getDrawable(@DrawableRes resId: Int): Drawable? {
            return ContextCompat.getDrawable(appContext, resId)
        }

        /**
         * Get string, ui/values/__picker_strings.xml
         */
        fun getString(@StringRes resId: Int): String {
            return appContext.resources.getString(resId)
        }

        /**
         * Get string array, ui/values/__picker_strings.xml
         */
        fun getStringArray(@ArrayRes resId: Int): Array<String> {
            return appContext.resources.getStringArray(resId)
        }

        /**
         * Get int array, ui/values/__picker_strings.xml
         */
        fun getIntArray(@ArrayRes resId: Int): IntArray {
            return appContext.resources.getIntArray(resId)
        }

        /**
         * Get color, ui/values/__picker_colors.xml
         */
        fun getColor(@ColorRes resId: Int): Int {
            return ContextCompat.getColor(appContext, resId)
        }

        /**
         * Get font
         */
        fun getFont(@FontRes resId: Int): Typeface? {
            return when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                    appContext.resources.getFont(resId)
                }
                else -> {
                    ResourcesCompat.getFont(appContext, resId)
                }
            }
        }

        /**
         * Get color state list, ui/values/__picker_colors.xml
         */
        fun getColorStateList(@ColorRes resId: Int): ColorStateList? {
            return ContextCompat.getColorStateList(appContext, resId)
        }

        /**
         * Get dimension, ui/values/dimens.xml
         *
         * @return View dimension value multiplied by the appropriate metric.
         * 获取某个dimen的值,如果是dp或sp的单位,将其乘以density,如果是px,则不乘   返回float
         */
        fun getDimension(@DimenRes resId: Int): Float {
            return appContext.resources.getDimension(resId)
        }

        /**
         * Get dimension, ui/values/dimens.xml
         *
         * @return View dimension value multiplied by the appropriate metric and truncated to integer pixels.
         * 获取某个dimen的值,如果是dp或sp的单位,将其乘以density,如果是px,则不乘  返回int
         */
        fun getDimensionPixelOffset(@DimenRes resId: Int): Int {
            return appContext.resources.getDimensionPixelOffset(resId)
        }

        /**
         * Get dimension, ui/values/dimens.xml
         *
         * @return View dimension value multiplied by the appropriate metric and truncated to integer pixels.
         * 不管写的是dp还是sp还是px,都会乘以density.
         */
        fun getDimensionPixelSize(@DimenRes resId: Int): Int {
            return appContext.resources.getDimensionPixelSize(resId)
        }
    }
}