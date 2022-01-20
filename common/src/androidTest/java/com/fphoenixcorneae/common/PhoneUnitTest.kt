package com.fphoenixcorneae.common

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.fphoenixcorneae.common.ext.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class PhoneUnitTest {

    @Test
    fun phoneInfo() {
        print("//==============================phoneInfo=======================================//")
        println()
        print("isPhone: $isPhone")
        println()
        print("deviceId: $deviceId")
        println()
        print("serial: $serial")
        println()
        print("iMEI: $iMEI")
        println()
        print("mEID: $mEID")
        println()
        print("iMSI: $iMSI")
        println()
        print("phoneType: $phoneType")
        println()
        print("isSimCardReady: $isSimCardReady")
        println()
        print("simOperatorName: $simOperatorName")
        println()
        print("simOperatorByMnc: $simOperatorByMnc")
        println()
        print("getPhoneStatus(): ${getPhoneStatus()}")
        println()
        print("//==============================phoneInfo=======================================//")
        println()
    }
}