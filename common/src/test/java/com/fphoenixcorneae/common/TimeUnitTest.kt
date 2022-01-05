package com.fphoenixcorneae.common

import com.fphoenixcorneae.common.annotation.TimeUnit
import com.fphoenixcorneae.common.ext.*
import org.junit.Test

class TimeUnitTest {

    @Test
    fun time2Milliseconds() {
        print("//==============================time2Milliseconds=======================================//")
        println()
        print("MILLISECONDS: ${1.MILLISECONDS}")
        println()
        print("SECONDS: ${1.SECONDS}")
        println()
        print("MINUTES: ${1.MINUTES}")
        println()
        print("HOURS: ${1.HOURS}")
        println()
        print("DAYS: ${1.DAYS}")
        println()
        print("MONTHS: ${30.DAYS}")
        println()
        print("YEARS: ${365.DAYS}")
        println()
        print("MILLISECONDS: ${TimeUnit.MILLISECOND}")
        println()
        print("SECONDS: ${TimeUnit.SECOND}")
        println()
        print("MINUTES: ${TimeUnit.MINUTE}")
        println()
        print("HOURS: ${TimeUnit.HOUR}")
        println()
        print("DAYS: ${TimeUnit.DAY}")
        println()
        print("MONTHS: ${TimeUnit.MONTH}")
        println()
        print("YEARS: ${TimeUnit.YEAR}")
        println()
        print("//==============================time2Milliseconds=======================================//")
        println()
    }
}