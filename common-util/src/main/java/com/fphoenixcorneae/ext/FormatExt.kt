package com.fphoenixcorneae.ext

import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

/**
 * 持续时间格式化
 */
fun durationFormat(duration: Long): String {
    val minute = duration / 60
    val second = duration % 60
    return when {
        minute <= 9 -> when {
            second <= 9 -> "0$minute:0$second"
            else -> "0$minute:$second"
        }
        else -> when {
            second <= 9 -> "$minute:0$second"
            else -> "$minute:$second"
        }
    }
}

/**
 * 数据流量格式化
 */
fun dataFormat(total: Long): String {
    val speedReal: Int = (total / (1024)).toInt()
    return when {
        speedReal < 512 -> "$speedReal KB"
        else -> {
            val mSpeed = speedReal / 1024.0
            ((mSpeed * 100).roundToInt() / 100.0).toString() + " MB"
        }
    }
}

/**
 * 时间格式化
 * @param pattern     时间格式
 * @param msecsValue  毫秒值
 */
fun timeFormat(pattern: String, msecsValue: Long): String {
    val simpleDateFormat = SimpleDateFormat(pattern, Locale.getDefault())
    // 设置时区，否则会有时差
    simpleDateFormat.timeZone = TimeZone.getTimeZone("UT+08:00")
    return simpleDateFormat.format(msecsValue)
}

/**
 * 将日期转换到指定格式的字符串，如果转换失败将返回null。
 *
 * @param pattern 日期正则表达式，例如：yyyy-MM-dd HH:mm:ss
 * @param locale 具体时区、国家等相关信息
 *
 * @return 目标字符串
 */
fun Date?.toDateStringOrNull(pattern: String, locale: Locale = Locale.getDefault()): String? {
    val formatter = SimpleDateFormat(pattern, locale)
    return this?.run {
        try {
            formatter.format(this)
        } catch (ex: Exception) {
            null
        }
    }
}

/**
 * 将日期转换到指定格式的字符串，如果转换失败将返回null。
 *
 * @param pattern 日期正则表达式，例如：yyyy-MM-dd HH:mm:ss
 * @param locale 具体时区、国家等相关信息
 *
 * @return 目标字符串
 */
fun Calendar?.toDateStringOrNull(pattern: String, locale: Locale = Locale.getDefault()): String? {
    return this?.time.toDateStringOrNull(pattern, locale)
}

/**
 * 将整型数据作为时间戳，转换到具体的[Date]类型实例。
 *
 * @param timeUnit 时间单位（默认单位：毫秒）
 */
fun Long.toDate(timeUnit: TimeUnit = TimeUnit.MILLISECONDS) = Date(timeUnit.toMillis(this))

/**
 * 将整型数据作为时间戳，转换到具体的[Date]类型实例。
 *
 * @param timeUnit 时间单位（默认单位：毫秒）
 */
fun Long.toCalendar(timeUnit: TimeUnit = TimeUnit.MILLISECONDS): Calendar {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = timeUnit.toMillis(this)
    return calendar
}