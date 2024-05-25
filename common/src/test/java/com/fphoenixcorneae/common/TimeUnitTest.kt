package com.fphoenixcorneae.common

import com.fphoenixcorneae.common.ext.DAYS
import com.fphoenixcorneae.common.ext.HOURS
import com.fphoenixcorneae.common.ext.MILLISECONDS
import com.fphoenixcorneae.common.ext.MINUTES
import com.fphoenixcorneae.common.ext.SECONDS
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
        print("//==============================time2Milliseconds=======================================//")
        println()
    }
}