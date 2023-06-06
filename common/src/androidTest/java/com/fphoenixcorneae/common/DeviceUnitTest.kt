package com.fphoenixcorneae.common

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.fphoenixcorneae.common.ext.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class DeviceUnitTest {

    @Test
    fun deviceInfo() {
        println("//==============================deviceInfo=======================================//")
        println("isDeviceRooted: $isDeviceRooted")
        println("isAdbEnabled: $isAdbEnabled")
        println("sdkVersionName: $sdkVersionName")
        println("sdkVersionCode: $sdkVersionCode")
        println("androidID: $androidID")
        println("macAddress: $macAddress")
        println("wifiEnabled: $wifiEnabled")
        println("manufacturer: $deviceManufacturer")
        println("deviceModel: $deviceModel")
        println("aBIs: $aBIs")
        println("isTablet: $isTablet")
        println("isEmulator: $isEmulator")
        println("isDevelopmentSettingsEnabled: $isDevelopmentSettingsEnabled")
        println("uniqueDeviceId: $uniqueDeviceId")
        println("isSameDevice: ${isSameDevice(uniqueDeviceId)}")
        println("//==============================deviceInfo=======================================//")
    }
}