package com.fphoenixcorneae.common.ext

import com.fphoenixcorneae.common.annotation.DatePattern
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.getOrSet

/**
 * HH:mm:ss
 */
val HHmmssFormat by lazy {
    ThreadLocal<DateFormat>().getOrSet {
        SimpleDateFormat(DatePattern.HHmmss, Locale.getDefault())
    }
}

/**
 * HH:mm
 */
val HHmmFormat by lazy {
    ThreadLocal<DateFormat>().getOrSet {
        SimpleDateFormat(DatePattern.HHmm, Locale.getDefault())
    }
}

/**
 * mm:ss
 */
val mmssFormat by lazy {
    ThreadLocal<DateFormat>().getOrSet {
        SimpleDateFormat(DatePattern.mmss, Locale.getDefault())
    }
}

/**
 * yyyy-MM-dd
 */
val yyyyMMddFormat by lazy {
    ThreadLocal<DateFormat>().getOrSet {
        SimpleDateFormat(DatePattern.yyyyMMdd, Locale.getDefault())
    }
}

/**
 * yyyyMMdd
 */
val yyyyMMddNoSepFormat by lazy {
    ThreadLocal<DateFormat>().getOrSet {
        SimpleDateFormat(DatePattern.yyyyMMddNoSep, Locale.getDefault())
    }
}

/**
 * yyyy-MM-dd HH:mm
 */
val yyyyMMddHHmmFormat by lazy {
    ThreadLocal<DateFormat>().getOrSet {
        SimpleDateFormat(DatePattern.yyyyMMddHHmm, Locale.getDefault())
    }
}

/**
 * yyyyMMdd HH:mm
 */
val yyyyMMddHHmmNoSepFormat by lazy {
    ThreadLocal<DateFormat>().getOrSet {
        SimpleDateFormat(DatePattern.yyyyMMddHHmmNoSep, Locale.getDefault())
    }
}

/**
 * yyyy-MM-dd HH:mm:ss
 */
val yyyyMMddHHmmssFormat by lazy {
    ThreadLocal<DateFormat>().getOrSet {
        SimpleDateFormat(DatePattern.yyyyMMddHHmmss, Locale.getDefault())
    }
}

/**
 * yyyyMMdd HH:mm:ss
 */
val yyyyMMddHHmmssNoSepFormat by lazy {
    ThreadLocal<DateFormat>().getOrSet {
        SimpleDateFormat(DatePattern.yyyyMMddHHmmssNoSep, Locale.getDefault())
    }
}

/**
 * yyyy-MM-dd HH:mm:ss:SSS
 */
val yyyyMMddHHmmssSSSFormat by lazy {
    ThreadLocal<DateFormat>().getOrSet {
        SimpleDateFormat(DatePattern.yyyyMMddHHmmssSSS, Locale.getDefault())
    }
}

//===============================================时间类型转化Start=======================================================//
/**
 * 时间戳转为时间字符串
 * @param format 时间格式
 */
fun Long.millis2String(
    format: DateFormat
): String =
    Date(this).date2String(format)

/**
 * 时间戳转为[Date]类型
 * @param timeUnit 时间单位（默认单位：毫秒）
 */
fun Long.millis2Date(
    timeUnit: TimeUnit = TimeUnit.MILLISECONDS
): Date =
    Date(timeUnit.toMillis(this))

/**
 * 时间戳转为[Calendar]类型
 * @param timeUnit 时间单位（默认单位：毫秒）
 */
fun Long.millis2Calendar(
    timeUnit: TimeUnit = TimeUnit.MILLISECONDS
): Calendar =
    currentCalendar().apply {
        timeInMillis = timeUnit.toMillis(this@millis2Calendar)
    }

/**
 * 时间字符串转为时间戳
 * @param format 时间格式
 */
fun String.string2Millis(
    format: DateFormat
): Long =
    string2Date(format).time

/**
 * 时间字符串转为[Date]类型
 * @param format 时间格式
 */
fun String.string2Date(
    format: DateFormat
): Date =
    runCatching {
        format.parse(this)
    }.onFailure {
        it.printStackTrace()
    }.getOrDefault(currentDate())

/**
 * 时间字符串转为[Calendar]类型
 * @param format 时间格式
 */
fun String.string2Calendar(
    format: DateFormat
): Calendar =
    string2Date(format).date2Calendar()

/**
 * 时间字符串转换日期格式
 * @param oldFormat 旧格式
 * @param newFormat 新格式
 */
fun String.transformDateFormat(
    oldFormat: DateFormat,
    newFormat: DateFormat
): String =
    string2Date(oldFormat).date2String(newFormat)

/**
 * [Date]类型转为时间字符串
 * @param format 时间格式
 */
fun Date.date2String(
    format: DateFormat
): String =
    format.format(this)

/**
 * [Date]类型转为时间戳
 */
fun Date.date2Millis(): Long =
    time

/**
 * [Date]类型转为[Calendar]类型
 */
fun Date.date2Calendar(): Calendar =
    currentCalendar().apply {
        time = this@date2Calendar
    }

/**
 * [Calendar]类型转为时间字符串
 * @param format 时间格式
 */
fun Calendar.calendar2String(
    format: DateFormat
): String =
    time.date2String(format)

/**
 * [Calendar]类型转为时间戳
 */
fun Calendar.calendar2Millis(): Long =
    time.date2Millis()

/**
 * [Calendar]类型转为[Date]类型
 */
fun Calendar.calendar2Date(): Date =
    time

//===============================================时间类型转化End=========================================================//


