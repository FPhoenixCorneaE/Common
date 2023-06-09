package com.fphoenixcorneae.common.ext

import java.text.DateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.abs
import kotlin.math.roundToLong

//=================================================时间计算Start========================================================//
/**
 * 获取日期的最早时间
 * <p>例如：今天日期是 2021-12-06 10:21:58，[day]=0，结果就是 2021-12-06 00:00:00:000</p>
 * <p>例如：今天日期是 2021-12-06 10:21:58，[day]=-1，结果就是 2021-12-05 00:00:00:000</p>
 * <p>例如：今天日期是 2021-12-06 10:21:58，[day]=1，结果就是 2021-12-07 00:00:00:000</p>
 * @param day 天数
 */
fun Date.getStartOfDay(
    day: Int = 0,
): Date =
    currentCalendar().run {
        time = this@getStartOfDay
        add(Calendar.DAY_OF_MONTH, day)
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
        time
    }

/**
 * 获取日期的最晚时间
 * <p>例如：今天日期是 2021-12-06 10:21:58，[day]=0，结果就是 2021-12-06 23:59:59:999</p>
 * <p>例如：今天日期是 2021-12-06 10:21:58，[day]=-1，结果就是 2021-12-05 23:59:59:999</p>
 * <p>例如：今天日期是 2021-12-06 10:21:58，[day]=1，结果就是 2021-12-07 23:59:59:999</p>
 * @param day 天数
 */
fun Date.getEndOfDay(
    day: Int = 0,
): Date =
    currentCalendar().run {
        time = this@getEndOfDay
        add(Calendar.DAY_OF_MONTH, day)
        set(Calendar.HOUR_OF_DAY, 23)
        set(Calendar.MINUTE, 59)
        set(Calendar.SECOND, 59)
        set(Calendar.MILLISECOND, 999)
        time
    }

/**
 * 获取日期n天前/后的日期
 * @param day 天数 【-1: 1天前, 1: 1天后】
 */
fun Date.daysBeforeOrAfter(
    day: Int,
): Date =
    currentCalendar().run {
        time = this@daysBeforeOrAfter
        add(Calendar.DAY_OF_MONTH, day)
        time
    }

/**
 * 获取日期n月前/后的日期
 * @param month 月数 【-1: 1月前, 1: 1月后】
 */
fun Date.monthsBeforeOrAfter(
    month: Int,
): Date =
    currentCalendar().run {
        time = this@monthsBeforeOrAfter
        add(Calendar.MONTH, month)
        time
    }

/**
 * 获取两个时间差(单位：[timeUnit])
 * @param timeUnit    返回单位 [TimeUnit]
 */
fun Date.getTimeSpan(
    anotherDate: Date,
    timeUnit: TimeUnit = TimeUnit.MILLISECONDS,
): Long =
    abs(date2Millis() - anotherDate.date2Millis()).run {
        timeUnit.convert(this, TimeUnit.MILLISECONDS)
    }

/**
 * 获取与当前时间差(单位：[timeUnit])
 * @param timeUnit    返回单位 [TimeUnit]
 */
fun Date.getTimeSpanByNow(
    timeUnit: TimeUnit = TimeUnit.MILLISECONDS,
): Long =
    getTimeSpan(currentDate(), timeUnit)

/**
 * 获取两个时间差(单位：[timeUnit])
 * @param timeUnit    返回单位 [TimeUnit]
 */
fun Long.getTimeSpan(
    anotherMillis: Long,
    timeUnit: TimeUnit = TimeUnit.MILLISECONDS,
): Long =
    abs(this - anotherMillis).run {
        timeUnit.convert(this, TimeUnit.MILLISECONDS)
    }

/**
 * 获取与当前时间差(单位：[timeUnit])
 * @param timeUnit    返回单位 [TimeUnit]
 */
fun Long.getTimeSpanByNow(
    timeUnit: TimeUnit = TimeUnit.MILLISECONDS,
): Long =
    getTimeSpan(System.currentTimeMillis(), timeUnit)

/**
 * 获取两个时间差(单位：[timeUnit])
 * <p>两个时间的格式都为[format]</p>
 * @param anotherTime 另一个时间
 * @param format      时间格式
 * @param timeUnit    返回单位 [TimeUnit]
 */
fun String.getTimeSpan(
    anotherTime: String,
    format: DateFormat,
    timeUnit: TimeUnit = TimeUnit.MILLISECONDS,
): Long =
    abs(string2Millis(format) - anotherTime.string2Millis(format)).run {
        timeUnit.convert(this, TimeUnit.MILLISECONDS)
    }

/**
 * 获取与当前时间差(单位：[timeUnit])
 * @param format      时间格式
 * @param timeUnit    返回单位 [TimeUnit]
 */
fun String.getTimeSpanByNow(
    format: DateFormat,
    timeUnit: TimeUnit = TimeUnit.MILLISECONDS,
): Long =
    string2Date(format).getTimeSpanByNow(timeUnit)

/**
 * 根据时间字符串获取友好型与当前时间的差
 * @param format      时间格式
 */
fun String.getFriendlyTimeSpanByNow(
    format: DateFormat,
): String =
    string2Millis(format).getFriendlyTimeSpanByNow()

/**
 * 根据时间字符串获取模糊型与当前时间的差
 * @param format      时间格式
 */
fun String.getFuzzyTimeSpanByNow(
    format: DateFormat,
): String =
    string2Millis(format).getFuzzyTimeSpanByNow()

/**
 * 根据出生日期时间字符串获取年龄
 * @param format      时间格式
 */
fun String.getAgeByBirthday(
    format: DateFormat,
): Int =
    string2Millis(format).getAgeByBirthday()

/**
 * 根据时间戳获取友好型与当前时间的差
 *  * 如果在 10 秒钟内，显示 刚刚
 *  * 如果在 01 分钟内，显示 XXX秒前
 *  * 如果在 01 小时内，显示 XXX分钟前
 *  * 如果在 01 小时外的今天内，显示 今天15:32
 *  * 如果是昨天的，显示 昨天15:32
 *  * 其余显示 yyyy-MM-dd
 *  * 时间不合法的情况全部日期和时间信息，如周一 12月 06 17:15:40 CST 2021
 */
fun Long.getFriendlyTimeSpanByNow(): String {
    val currentTimeMillis = System.currentTimeMillis()
    val timeMillisSpan = currentTimeMillis - this
    when {
        timeMillisSpan < 0 -> {
            return String.format(locale = Locale.getDefault(), format = "%tc", this)
        }
        timeMillisSpan < TimeUnit.SECONDS.toMillis(10) -> {
            return "刚刚"
        }
        timeMillisSpan < TimeUnit.MINUTES.toMillis(1) -> {
            return String.format(
                locale = Locale.getDefault(),
                format = "%d秒前",
                timeMillisSpan / TimeUnit.SECONDS.toMillis(1)
            )
        }
        timeMillisSpan < TimeUnit.HOURS.toMillis(1) -> {
            return String.format(
                locale = Locale.getDefault(),
                format = "%d分钟前",
                timeMillisSpan / TimeUnit.MINUTES.toMillis(1)
            )
        }
        else -> {
            // 获取当天 00:00
            val startOfDay: Long = currentDate().getStartOfDay().date2Millis()
            return when {
                this >= startOfDay -> {
                    String.format(locale = Locale.getDefault(), format = "今天%tR", this)
                }
                this >= startOfDay - TimeUnit.DAYS.toMillis(1) -> {
                    String.format(locale = Locale.getDefault(), format = "昨天%tR", this)
                }
                else -> {
                    String.format(locale = Locale.getDefault(), format = "%tF", this)
                }
            }
        }
    }
}

/**
 * 根据时间戳获取模糊型与当前时间的差
 *  * 如果在 01 分钟内或者时间是未来的时间，显示 刚刚
 *  * 如果在 01 小时内，显示 XXX分钟前
 *  * 如果在 01 天内，显示 XXX小时前
 *  * 如果在 01 月内，显示 XXX天前
 *  * 如果在 01 年内，显示 XXX月前
 *  * 如果在 01 年外，显示 XXX年前
 */
fun Long.getFuzzyTimeSpanByNow(): String {
    val currentTime = System.currentTimeMillis()
    // 与现在时间相差毫秒数
    val timeMillisSpan = currentTime - this
    var span: Long
    return when {
        (timeMillisSpan.toDouble() / TimeUnit.DAYS.toMillis(365)).roundToLong().also { span = it } > 0 -> {
            // 一年以上
            "${span}年前"
        }
        (timeMillisSpan.toDouble() / TimeUnit.DAYS.toMillis(30)).roundToLong().also { span = it } > 0 -> {
            // 1月-1年
            "${span}个月前"
        }
        (timeMillisSpan.toDouble() / TimeUnit.DAYS.toMillis(1)).roundToLong().also { span = it } > 0 -> {
            // 1天-1月
            "${span}天前"
        }
        (timeMillisSpan.toDouble() / TimeUnit.HOURS.toMillis(1)).roundToLong().also { span = it } > 0 -> {
            // 1小时-24小时
            "${span}小时前"
        }
        (timeMillisSpan.toDouble() / TimeUnit.MINUTES.toMillis(1)).roundToLong().also { span = it } > 0 -> {
            // 1分钟-59分钟
            "${span}分钟前"
        }
        else -> {
            // 1秒钟-59秒钟
            "刚刚"
        }
    }
}

/**
 * 根据出生日期时间戳获取年龄
 */
fun Long.getAgeByBirthday(): Int =
    runCatching {
        currentCalendar().run {
            val birthday = millis2Date()
            if (birthday.after(time)) {
                throw IllegalArgumentException("The birthday is after now. It's unbelievable!")
            }
            val yearNow: Int = get(Calendar.YEAR)
            val monthNow: Int = get(Calendar.MONTH)
            val dayNow: Int = get(Calendar.DAY_OF_MONTH)
            time = birthday
            val yearBirth: Int = get(Calendar.YEAR)
            val monthBirth: Int = get(Calendar.MONTH)
            val dayBirth: Int = get(Calendar.DAY_OF_MONTH)
            var age = yearNow - yearBirth
            if (monthNow <= monthBirth) {
                if (monthNow == monthBirth) {
                    if (dayNow < dayBirth) {
                        age--
                    }
                } else {
                    age--
                }
            }
            age
        }
    }.onFailure {
        it.printStackTrace()
    }.getOrDefault(0)

/**
 * 判断是否是今天
 */
fun Long.isToday(): Boolean = run {
    val startOfToday = currentDate().getStartOfDay().date2Millis()
    this >= startOfToday && this < startOfToday + TimeUnit.DAYS.toMillis(1)
}

//=================================================时间计算End==========================================================//


//=================================================时间获取Start========================================================//
/**
 * 获取当前毫秒时间戳
 */
fun currentTimeMillis(): Long =
    System.currentTimeMillis()

/**
 * 获取当前时间字符串
 * @param format 时间格式
 */
fun currentTimeString(
    format: DateFormat,
): String =
    currentTimeMillis().millis2String(format)

/**
 * 获取当前[Date]类型时间
 */
fun currentDate(): Date =
    Date()

/**
 * 获取当前[Calendar]类型时间
 */
fun currentCalendar(): Calendar =
    Calendar.getInstance()

/**
 * 得到年
 */
fun Date.year(): Int =
    currentCalendar().run {
        time = this@year
        get(Calendar.YEAR)
    }

/**
 * 得到月
 */
fun Date.month(): Int =
    currentCalendar().run {
        time = this@month
        get(Calendar.MONTH) + 1
    }

/**
 * 得到日
 */
fun Date.day(): Int =
    currentCalendar().run {
        time = this@day
        get(Calendar.DAY_OF_MONTH)
    }

/**
 * 得到星期索引
 * <p>注意：周日的 Index 才是 1，周六为 7</p>
 */
fun Date.weekIndex(): Int =
    currentCalendar().run {
        time = this@weekIndex
        get(Calendar.DAY_OF_WEEK)
    }

//=================================================时间获取End==========================================================//


//=================================================获取生肖、星座Start===================================================//
val chineseZodiac = mutableListOf("猴", "鸡", "狗", "猪", "鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊")
val horoscope = mutableListOf(
    "水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "魔羯座"
)
val horoscopeFlags = mutableListOf(
    20, 19, 21, 21, 21, 22, 23, 23, 23, 24, 23, 22
)

/**
 * 获取生肖
 */
fun Date.getChineseZodiac(): String =
    chineseZodiac[year() % chineseZodiac.size]

/**
 * 获取星座
 */
fun Date.getHoroscope(): String =
    horoscope[
            if (day() >= horoscopeFlags[month() - 1]) {
                month() - 1
            } else {
                (month() + 10) % horoscope.size
            }
    ]
//=================================================获取生肖、星座End=====================================================//