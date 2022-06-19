package com.fphoenixcorneae.common.annotation

import androidx.annotation.Keep

/**
 * @desc：文件类型
 */
@Keep
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE, AnnotationTarget.VALUE_PARAMETER)
@kotlin.annotation.Retention(AnnotationRetention.BINARY)
annotation class FileType {
    companion object {
        /**
         * 声明各种类型文件的dataType
         */
        const val APK = "application/vnd.android.package-archive"
        const val VIDEO = "video/*"
        const val AUDIO = "audio/*"
        const val HTML = "text/html"
        const val IMAGE = "image/*"
        const val PPT = "application/vnd.ms-powerpoint"
        const val EXCEL = "application/vnd.ms-excel"
        const val WORD = "application/msword"
        const val CHM = "application/x-chm"
        const val TXT = "text/plain"
        const val PDF = "application/pdf"
        /**
         * 未指定明确的文件类型，不能使用精确类型的工具打开，需要用户选择
         */
        const val ALL = "*/*"
    }
}