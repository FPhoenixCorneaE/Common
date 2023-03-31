package com.fphoenixcorneae.common.ext

import java.util.Calendar

/**
 * Adds the specified number of days to this calendar instance.
 */
fun Calendar.addDays(days: Int): Calendar {
    val newCalendar = this.clone() as Calendar
    newCalendar.add(Calendar.DAY_OF_YEAR, days)
    return newCalendar
}

/**
 * Adds the specified number of months to this calendar instance.
 */
fun Calendar.addMonths(months: Int): Calendar {
    val newCalendar = this.clone() as Calendar
    newCalendar.add(Calendar.MONTH, months)
    return newCalendar
}

/**
 * Adds the specified number of years to this calendar instance.
 */
fun Calendar.addYears(years: Int): Calendar {
    val newCalendar = this.clone() as Calendar
    newCalendar.add(Calendar.YEAR, years)
    return newCalendar
}

/**
 * Returns the day of the month for this calendar instance.
 */
fun Calendar.getDayOfMonth(): Int {
    return this.get(Calendar.DAY_OF_MONTH)
}

/**
 * Returns the month for this calendar instance.
 */
fun Calendar.getMonth(): Int {
    return this.get(Calendar.MONTH)
}

/**
 * Returns the year for this calendar instance.
 */
fun Calendar.getYear(): Int {
    return this.get(Calendar.YEAR)
}

/**
 * Sets the day of the month for this calendar instance.
 */
fun Calendar.setDayOfMonth(day: Int) {
    this.set(Calendar.DAY_OF_MONTH, day)
}

/**
 * Sets the month for this calendar instance.
 */
fun Calendar.setMonth(month: Int) {
    this.set(Calendar.MONTH, month)
}

/**
 * Sets the year for this calendar instance.
 */
fun Calendar.setYear(year: Int) {
    this.set(Calendar.YEAR, year)
}


fun main() {
    val today = Calendar.getInstance()
    println("Calendar: 输出当前日期: $today") // 输出当前日期

    val tomorrow = today.addDays(1)
    println("Calendar: 输出明天的日期: $tomorrow") // 输出明天的日期

    val twoMonthsLater = today.addMonths(2)
    println("Calendar: 输出两个月后的日期: $twoMonthsLater") // 输出两个月后的日期

    val nextYear = today.addYears(1)
    println("Calendar: 输出明年的日期: $nextYear") // 输出明年的日期

    println("Calendar: 输出今天是本月的第几天: ${today.getDayOfMonth()}") // 输出今天是本月的第几天

    today.setDayOfMonth(15) // 将今天设置为本月的 15 号
    println("Calendar: 将今天设置为本月的 15 号: $today")

    today.setMonth(Calendar.AUGUST) // 将今天设置为 8 月份
    println("Calendar: 将今天设置为 8 月份: $today")

    today.setYear(2022) // 将今年设置为 2022 年
    println("Calendar: 将今年设置为 2022 年: $today")
}
