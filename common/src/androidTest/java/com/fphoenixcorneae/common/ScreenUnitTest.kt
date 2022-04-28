package com.fphoenixcorneae.common

import android.content.res.Configuration
import android.graphics.Point
import android.util.DisplayMetrics
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.fphoenixcorneae.common.ext.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class ScreenUnitTest {

    @Test
    fun getScreenWidth() {
        print("//==============================getScreenWidth=======================================//")
        println()
        print("Resources.getSystem().displayMetrics.widthPixels: $screenWidth")
        println()
        val display = applicationContext.windowManager?.defaultDisplay
        val size = Point()
        display?.getSize(size)
        print("Display.getSize: ${size.x}")
        println()
        print("Display.getWidth: ${applicationContext.windowManager?.defaultDisplay?.width}")
        println()
        val displayMetrics = DisplayMetrics()
        applicationContext.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        print("displayMetrics.widthPixels: ${displayMetrics.widthPixels}")
        println()
        print("//==============================getScreenWidth=======================================//")
        println()
    }

    @Test
    fun getScreenHeight() {
        print("//==============================getScreenHeight=======================================//")
        println()
        print("Resources.getSystem().displayMetrics.heightPixels: $screenHeight")
        println()
        val display = applicationContext.windowManager?.defaultDisplay
        val size = Point()
        display?.getSize(size)
        print("Display.getSize: ${size.y}")
        println()
        print("Display.getHeight: ${applicationContext.windowManager?.defaultDisplay?.height}")
        println()
        val displayMetrics = DisplayMetrics()
        applicationContext.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        print("displayMetrics.heightPixels: ${displayMetrics.heightPixels}")
        println()
        print("//==============================getScreenHeight=======================================//")
        println()
    }

    /**
     * 测试测得不准。实际运行是准确的。
     */
    @Test
    fun landscape_portrait() {
        print("//==============================landscape_portrait=======================================//")
        println()
        print("isLandscape: $isLandscape")
        println()
        print("isLandscape: ${applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE}")
        println()
        print("isPortrait: $isPortrait")
        println()
        print("isPortrait: ${applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT}")
        println()
        print("//==============================landscape_portrait=======================================//")
        println()
    }

    /**
     * 测试拿不到结果
     */
    @Test
    fun screenRotation() {
        print("//==============================getScreenRotation=======================================//")
        println()
        print("screenRotation: ${getScreenRotation()}")
        println()
        print("//==============================getScreenRotation=======================================//")
        println()
    }
}