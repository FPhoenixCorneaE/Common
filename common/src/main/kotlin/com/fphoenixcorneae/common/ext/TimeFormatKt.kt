package com.fphoenixcorneae.common.ext

import com.fphoenixcorneae.common.annotation.DatePattern
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

/**
 * 数据流量格式化
 */
fun Long.formatDataTraffic(): String {
    val speedReal: Int = (this / (1024)).toDouble().roundToInt()
    return when {
        speedReal < 512 -> "$speedReal KB"
        else -> {
            val speed = speedReal / 1024f
            "${(speed * 100).roundToInt() / 100f} MB"
        }
    }
}

/**
 * 持续时间(秒)格式化：HH:mm
 */
fun Long.formatSecondTime(): String {
    val minute = this / 60
    val second = this % 60
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
 * 毫秒值时间格式化，可格式化成持续时间（HH:mm:ss/mm:ss）
 * 注意：格式化成持续时间 HH:mm:ss 时，timeZoneId 需要传"UT+08:00"，否则会多出8个小时
 * @param pattern 时间格式
 */
fun Long.formatMillisecondTime(
    pattern: String = DatePattern.yyyyMMddHHmmss,
    timeZoneId: String = TimeZone.getDefault().id
): String {
    val simpleDateFormat = SimpleDateFormat(pattern, Locale.getDefault())
    simpleDateFormat.timeZone = TimeZone.getTimeZone(timeZoneId)
    return simpleDateFormat.format(this)
}

/**
 * 将日期转换到指定格式的字符串，如果转换失败将返回 null。
 *
 * @param pattern 日期正则表达式，例如：yyyy-MM-dd HH:mm:ss
 * @param locale  具体时区、国家等相关信息
 *
 * @return 目标字符串
 */
fun Date?.toDateStringOrNull(pattern: String, locale: Locale = Locale.getDefault()): String? {
    val formatter = SimpleDateFormat(pattern, locale)
    return this?.runCatching {
        formatter.format(this)
    }?.onFailure {
        it.toString().loge()
    }?.getOrNull()
}

/**
 * 将日期转换到指定格式的字符串，如果转换失败将返回null。
 *
 * @param pattern 日期正则表达式，例如：yyyy-MM-dd HH:mm:ss
 * @param locale  具体时区、国家等相关信息
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