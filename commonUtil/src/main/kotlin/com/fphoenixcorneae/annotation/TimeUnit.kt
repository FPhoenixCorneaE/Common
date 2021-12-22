package com.fphoenixcorneae.annotation

import androidx.annotation.Keep
import com.fphoenixcorneae.ext.*

/**
 * @desc：时间单位
 * @date：2021/12/20 17:11
 */
@Keep
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE, AnnotationTarget.VALUE_PARAMETER)
@kotlin.annotation.Retention(AnnotationRetention.BINARY)
annotation class TimeUnit {
    companion object {
        /** 1毫秒的毫秒值 */
        val MILLISECOND: Long = 1.MILLISECONDS

        /** 1秒的毫秒值 */
        val SECOND: Long = 1.SECONDS

        /** 1分的毫秒值 */
        val MINUTE: Long = 1.MINUTES

        /** 1时的毫秒值 */
        val HOUR: Long = 1.HOURS

        /** 1天的毫秒值 */
        val DAY: Long = 1.DAYS

        /** 1月的毫秒值 */
        val MONTH: Long = 1.MONTHS

        /** 1年的毫秒值 */
        val YEAR: Long = 1.YEARS
    }
}
