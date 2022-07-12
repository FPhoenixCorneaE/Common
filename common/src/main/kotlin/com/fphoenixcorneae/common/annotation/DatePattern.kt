package com.fphoenixcorneae.common.annotation

import androidx.annotation.Keep

/**
 * @desc：日期格式
 * @date：2021/11/26 15:32
 */
@Keep
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FUNCTION)
@kotlin.annotation.Retention(AnnotationRetention.BINARY)
annotation class DatePattern {
    companion object {
        const val HHmmss = "HH:mm:ss"
        const val HHmm = "HH:mm"
        const val mmss = "mm:ss"
        const val yyyyMMdd = "yyyy-MM-dd"
        const val yyyyMMddNoSep = "yyyyMMdd"
        const val yyyyMMddHHmm = "yyyy-MM-dd HH:mm"
        const val yyyyMMddHHmmNoSep = "yyyyMMdd HH:mm"
        const val yyyyMMddHHmmss = "yyyy-MM-dd HH:mm:ss"
        const val yyyyMMddHHmmssNoSep = "yyyyMMddHHmmss"
        const val yyyyMMddHHmmssSSS = "yyyy-MM-dd HH:mm:ss:SSS"
    }
}
