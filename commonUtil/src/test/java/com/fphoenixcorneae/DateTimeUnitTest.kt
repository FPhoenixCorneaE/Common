package com.fphoenixcorneae

import com.fphoenixcorneae.ext.*
import org.junit.Test
import java.util.concurrent.TimeUnit

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class DateTimeUnitTest {

    @Test
    fun getStartOfDay() {
        print("//==============================getStartOfDay=======================================//")
        println()
        print(currentDate().getStartOfDay(day = -1).date2String(yyyyMMddHHmmssSSSFormat.get()!!))
        println()
        print(currentDate().getStartOfDay(day = 0).date2String(yyyyMMddHHmmssSSSFormat.get()!!))
        println()
        print(currentDate().getStartOfDay(day = 1).date2String(yyyyMMddHHmmssSSSFormat.get()!!))
        println()
        print("//==============================getStartOfDay=======================================//")
        println()
    }

    @Test
    fun getEndOfDay() {
        print("//==============================getEndOfDay=======================================//")
        println()
        print(currentDate().getEndOfDay(day = -1).date2String(yyyyMMddHHmmssSSSFormat.get()!!))
        println()
        print(currentDate().getEndOfDay(day = 0).date2String(yyyyMMddHHmmssSSSFormat.get()!!))
        println()
        print(currentDate().getEndOfDay(day = 1).date2String(yyyyMMddHHmmssSSSFormat.get()!!))
        println()
        print("//==============================getEndOfDay=======================================//")
        println()
    }

    @Test
    fun daysBeforeOrAfter() {
        print("//==============================daysBeforeOrAfter=======================================//")
        println()
        print(currentDate().daysBeforeOrAfter(day = -1).date2String(yyyyMMddHHmmssSSSFormat.get()!!))
        println()
        print(currentDate().daysBeforeOrAfter(day = 0).date2String(yyyyMMddHHmmssSSSFormat.get()!!))
        println()
        print(currentDate().daysBeforeOrAfter(day = 1).date2String(yyyyMMddHHmmssSSSFormat.get()!!))
        println()
        print("//==============================daysBeforeOrAfter=======================================//")
        println()
    }

    @Test
    fun monthsBeforeOrAfter() {
        print("//==============================monthsBeforeOrAfter=======================================//")
        println()
        print(currentDate().monthsBeforeOrAfter(month = -1).date2String(yyyyMMddHHmmssSSSFormat.get()!!))
        println()
        print(currentDate().monthsBeforeOrAfter(month = 0).date2String(yyyyMMddHHmmssSSSFormat.get()!!))
        println()
        print(currentDate().monthsBeforeOrAfter(month = 1).date2String(yyyyMMddHHmmssSSSFormat.get()!!))
        println()
        print("//==============================monthsBeforeOrAfter=======================================//")
        println()
    }

    @Test
    fun getTimeSpan() {
        print("//==============================getTimeSpan=======================================//")
        println()
        print("2021-12-06 14:46".getTimeSpan("2021-12-06 15:46", yyyyMMddHHmmFormat.get()!!, TimeUnit.DAYS).toString())
        println()
        print("2021-12-06 14:46".getTimeSpan("2021-12-06 15:46", yyyyMMddHHmmFormat.get()!!, TimeUnit.HOURS).toString())
        println()
        print(
            "2021-12-06 14:46".getTimeSpan("2021-12-06 15:46", yyyyMMddHHmmFormat.get()!!, TimeUnit.MINUTES).toString()
        )
        println()
        print(
            "2021-12-06 14:46".getTimeSpan("2021-12-06 15:46", yyyyMMddHHmmFormat.get()!!, TimeUnit.SECONDS).toString()
        )
        println()
        print(
            "2021-12-06 14:46".getTimeSpan("2021-12-06 15:46", yyyyMMddHHmmFormat.get()!!, TimeUnit.MILLISECONDS)
                .toString()
        )
        println()
        print(
            "2021-12-06 14:46".getTimeSpan("2021-12-06 15:46", yyyyMMddHHmmFormat.get()!!, TimeUnit.MICROSECONDS)
                .toString()
        )
        println()
        print(
            "2021-12-06 14:46".getTimeSpan("2021-12-06 15:46", yyyyMMddHHmmFormat.get()!!, TimeUnit.NANOSECONDS)
                .toString()
        )
        println()
        print("//==============================getTimeSpan=======================================//")
        println()
    }

    @Test
    fun getTimeSpanByNow() {
        print("//==============================getTimeSpanByNow=======================================//")
        println()
        print("2021-12-02 15:46".string2Date(yyyyMMddHHmmFormat.get()!!).getTimeSpanByNow(TimeUnit.DAYS))
        println()
        print("2021-12-05 15:46".string2Millis(yyyyMMddHHmmFormat.get()!!).getTimeSpanByNow(TimeUnit.HOURS))
        println()
        print("2021-12-18 15:46".getTimeSpanByNow(yyyyMMddHHmmFormat.get()!!, TimeUnit.HOURS))
        println()
        print("//==============================getTimeSpanByNow=======================================//")
        println()
    }

    @Test
    fun getFriendlyTimeSpanByNow() {
        print("//==============================getFriendlyTimeSpanByNow=======================================//")
        println()
        print("2021-12-06 17:15:40".string2Millis(yyyyMMddHHmmssFormat.get()!!).getFriendlyTimeSpanByNow())
        println()
        print("2021-12-06 00:00:00".string2Millis(yyyyMMddHHmmssFormat.get()!!).getFriendlyTimeSpanByNow())
        println()
        print("2021-12-05 23:59:59".string2Millis(yyyyMMddHHmmssFormat.get()!!).getFriendlyTimeSpanByNow())
        println()
        print("2021-12-01 17:07:00".string2Millis(yyyyMMddHHmmssFormat.get()!!).getFriendlyTimeSpanByNow())
        println()
        print("2023-12-06 17:07:00".string2Millis(yyyyMMddHHmmssFormat.get()!!).getFriendlyTimeSpanByNow())
        println()
        print("2021-12-06 17:07:00".string2Millis(yyyyMMddHHmmssFormat.get()!!).getFriendlyTimeSpanByNow())
        println()
        print("2021-12-06 17:07:00".string2Millis(yyyyMMddHHmmssFormat.get()!!).getFriendlyTimeSpanByNow())
        println()
        print("//==============================getFriendlyTimeSpanByNow=======================================//")
        println()
    }

    @Test
    fun getFuzzyTimeSpanByNow() {
        print("//==============================getFuzzyTimeSpanByNow=======================================//")
        println()
        print("2021-12-06 17:15:40".string2Millis(yyyyMMddHHmmssFormat.get()!!).getFuzzyTimeSpanByNow())
        println()
        print("2021-12-06 12:00:00".string2Millis(yyyyMMddHHmmssFormat.get()!!).getFuzzyTimeSpanByNow())
        println()
        print("2021-12-05 23:59:59".string2Millis(yyyyMMddHHmmssFormat.get()!!).getFuzzyTimeSpanByNow())
        println()
        print("2021-12-01 17:07:00".string2Millis(yyyyMMddHHmmssFormat.get()!!).getFuzzyTimeSpanByNow())
        println()
        print("2023-12-06 17:07:00".string2Millis(yyyyMMddHHmmssFormat.get()!!).getFuzzyTimeSpanByNow())
        println()
        print("2021-11-01 17:07:00".string2Millis(yyyyMMddHHmmssFormat.get()!!).getFuzzyTimeSpanByNow())
        println()
        print("2020-12-06 17:07:00".string2Millis(yyyyMMddHHmmssFormat.get()!!).getFuzzyTimeSpanByNow())
        println()
        print("//==============================getFuzzyTimeSpanByNow=======================================//")
        println()
    }

    @Test
    fun getAgeByBirthday() {
        print("//==============================getAgeByBirthday=======================================//")
        println()
        print("1991-12-08 17:15:40".getAgeByBirthday(yyyyMMddHHmmssFormat.get()!!))
        println()
        print("2091-12-06 17:15:40".string2Millis(yyyyMMddHHmmssFormat.get()!!).getAgeByBirthday())
        println()
        print("//==============================getAgeByBirthday=======================================//")
        println()
    }

    @Test
    fun isToday() {
        print("//==============================isToday=======================================//")
        println()
        print("2021-12-06 23:59:59".string2Millis(yyyyMMddHHmmssFormat.get()!!).isToday())
        println()
        print("2021-12-07 23:59:59".string2Millis(yyyyMMddHHmmssFormat.get()!!).isToday())
        println()
        print("2021-12-08 00:00:00".string2Millis(yyyyMMddHHmmssFormat.get()!!).isToday())
        println()
        print("//==============================isToday=======================================//")
        println()
    }

    @Test
    fun yearMonthDayWeekIndex() {
        print("//==============================yearMonthDayWeekIndex=======================================//")
        println()
        print("1800-12-06 23:59:59".string2Date(yyyyMMddHHmmssFormat.get()!!).year())
        println()
        print("2021-10-07 23:59:59".string2Date(yyyyMMddHHmmssFormat.get()!!).month())
        println()
        print("2021-12-01 00:00:00".string2Date(yyyyMMddHHmmssFormat.get()!!).day())
        println()
        print("2022-01-08 00:00:00".string2Date(yyyyMMddHHmmssFormat.get()!!).weekIndex())
        println()
        print("//==============================yearMonthDayWeekIndex=======================================//")
        println()
    }

    @Test
    fun getChineseZodiac() {
        print("//==============================getChineseZodiac=======================================//")
        println()
        print("2012-12-06 23:59:59".string2Date(yyyyMMddHHmmssFormat.get()!!).getChineseZodiac())
        println()
        print("2018-12-07 23:59:59".string2Date(yyyyMMddHHmmssFormat.get()!!).getChineseZodiac())
        println()
        print("2021-12-08 00:00:00".string2Date(yyyyMMddHHmmssFormat.get()!!).getChineseZodiac())
        println()
        print("//==============================getChineseZodiac=======================================//")
        println()
    }

    @Test
    fun getHoroscope() {
        print("//==============================getHoroscope=======================================//")
        println()
        print("2012-01-20 23:59:59".string2Date(yyyyMMddHHmmssFormat.get()!!).getHoroscope())
        println()
        print("2018-02-19 23:59:59".string2Date(yyyyMMddHHmmssFormat.get()!!).getHoroscope())
        println()
        print("2021-03-21 00:00:00".string2Date(yyyyMMddHHmmssFormat.get()!!).getHoroscope())
        println()
        print("2021-04-21 00:00:00".string2Date(yyyyMMddHHmmssFormat.get()!!).getHoroscope())
        println()
        print("2021-05-21 00:00:00".string2Date(yyyyMMddHHmmssFormat.get()!!).getHoroscope())
        println()
        print("2021-06-22 00:00:00".string2Date(yyyyMMddHHmmssFormat.get()!!).getHoroscope())
        println()
        print("2021-07-23 00:00:00".string2Date(yyyyMMddHHmmssFormat.get()!!).getHoroscope())
        println()
        print("2021-08-23 00:00:00".string2Date(yyyyMMddHHmmssFormat.get()!!).getHoroscope())
        println()
        print("2021-09-23 00:00:00".string2Date(yyyyMMddHHmmssFormat.get()!!).getHoroscope())
        println()
        print("2021-10-24 00:00:00".string2Date(yyyyMMddHHmmssFormat.get()!!).getHoroscope())
        println()
        print("2021-11-23 00:00:00".string2Date(yyyyMMddHHmmssFormat.get()!!).getHoroscope())
        println()
        print("2021-12-22 00:00:00".string2Date(yyyyMMddHHmmssFormat.get()!!).getHoroscope())
        println()
        print("//==============================getChineseZodiac=======================================//")
        println()
    }
}