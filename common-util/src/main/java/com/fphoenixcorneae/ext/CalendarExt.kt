package com.fphoenixcorneae.ext

import java.util.*

/**
 * 检查当前时间是否在某一时间段内
 */
fun checkCurrentTimeIsIn(
    startHourOfDay: Int,
    startMinute: Int,
    startSecond: Int,
    endHourOfDay: Int,
    endMinute: Int,
    endSecond: Int
): Boolean {
    val startCalendar = Calendar.getInstance()
    startCalendar.set(Calendar.HOUR_OF_DAY, startHourOfDay)
    startCalendar.set(Calendar.MINUTE, startMinute)
    startCalendar.set(Calendar.SECOND, startSecond)
    val endCalendar = Calendar.getInstance()
    endCalendar.set(Calendar.HOUR_OF_DAY, endHourOfDay)
    endCalendar.set(Calendar.MINUTE, endMinute)
    endCalendar.set(Calendar.SECOND, endSecond)
    return System.currentTimeMillis() in startCalendar.timeInMillis..endCalendar.timeInMillis
}