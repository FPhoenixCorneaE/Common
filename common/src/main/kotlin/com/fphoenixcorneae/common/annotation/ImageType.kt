package com.fphoenixcorneae.common.annotation

import androidx.annotation.Keep

/**
 * @desc：图片类型
 * @date：2022/07/11 15:30
 */
@Keep
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FUNCTION)
@kotlin.annotation.Retention(AnnotationRetention.BINARY)
annotation class ImageType {
    companion object {
        const val JPG = "jpg"
        const val PNG = "png"
        const val GIF = "gif"
        const val TIFF = "tiff"
        const val BMP = "bmp"
        const val WEBP = "webp"
        const val ICO = "ico"
        const val UNKNOWN = "unknown"
    }
}