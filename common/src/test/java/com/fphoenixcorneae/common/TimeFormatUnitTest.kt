package com.fphoenixcorneae.common

import com.fphoenixcorneae.common.annotation.DatePattern
import com.fphoenixcorneae.common.ext.*
import org.junit.Test

class TimeFormatUnitTest {

    @Test
    fun timeFormatTest() {
        println("//==============================timeFormatTest=======================================//")
        println("formatTimeDuration: ${5200L.formatSecondTime()}")
        println("formatDataTraffic: ${123456789L.formatDataTraffic()}")
        println("formatMillisecondTime: 持续时间 ${1036000L.formatMillisecondTime(DatePattern.HHmmss,"UT+08:00")}")
        println("formatMillisecondTime: 默认时区 ${1503544630000L.formatMillisecondTime(DatePattern.yyyyMMddHHmmss)}")
        println("formatMillisecondTime: 北京时间 ${1503544630000L.formatMillisecondTime(DatePattern.yyyyMMddHHmmss, "Asia/Shanghai")}")
        println("formatMillisecondTime: 北京时间 ${1503544630000L.formatMillisecondTime(DatePattern.yyyyMMddHHmmss, "Etc/GMT-8")}")
        println("formatMillisecondTime: 东京时间 ${1503544630000L.formatMillisecondTime(DatePattern.yyyyMMddHHmmss, "Asia/Tokyo")}")
        println("formatMillisecondTime: 伦敦时间 ${1503544630000L.formatMillisecondTime(DatePattern.yyyyMMddHHmmss, "Europe/London")}")
        println("toDateStringOrNull: ${136000L.toDate().toDateStringOrNull(DatePattern.yyyyMMddHHmmss)}")
        println("toDate: ${136000L.toDate()}")
        println("toCalendar: ${136000L.toCalendar()}")
        println("//==============================timeFormatTest=======================================//")
    }
}