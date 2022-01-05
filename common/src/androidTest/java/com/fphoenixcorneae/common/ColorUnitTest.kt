package com.fphoenixcorneae.common

import android.graphics.Color
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.fphoenixcorneae.common.ext.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class ColorUnitTest {

    @Test
    fun getColor() {
        print("//==============================getColor=======================================//")
        println()
        print(getColor(android.R.color.holo_blue_bright))
        println()
        print((-16720385).colorInt2HexString())
        println()
        print("//==============================getColor=======================================//")
        println()
    }

    @Test
    fun adjustColorAlpha() {
        print("//==============================adjustColorAlpha=======================================//")
        println()
        print(getColor(android.R.color.holo_blue_bright).adjustColorAlpha(0.6f))
        println()
        print((-1719612476).colorInt2HexString())
        println()
        print("//==============================adjustColorAlpha=======================================//")
        println()
    }

    @Test
    fun setColorAlpha() {
        print("//==============================setColorAlpha=======================================//")
        println()
        print(Color.BLACK.setColorAlpha(0.6f))
        println()
        print((-1728053248).colorInt2HexString())
        println()
        print(Color.BLACK.setRedAlpha(0.6f))
        println()
        print((10027008).colorInt2HexString())
        println()
        print(Color.BLACK.setGreenAlpha(0.6f))
        println()
        print((39168).colorInt2HexString())
        println()
        print(Color.BLACK.setBlueAlpha(0.6f))
        println()
        print((-16777063).colorInt2HexString())
        println()
        print("//==============================setColorAlpha=======================================//")
        println()
    }

    @Test
    fun transfersBetweenColorIntAndHexString() {
        print("//==============================transfersBetweenColorIntAndHexString=======================================//")
        println()
        print("#123456".hexString2ColorInt())
        println()
        print((-15584170).colorInt2HexString())
        println()
        print("red".hexString2ColorInt())
        println()
        print((-65536).colorInt2HexString())
        println()
        print("darkgray".hexString2ColorInt())
        println()
        print((-12303292).colorInt2HexString())
        println()
        print("//==============================transfersBetweenColorIntAndHexString=======================================//")
        println()
    }

    @Test
    fun isDarkColor() {
        print("//==============================isDarkColor=======================================//")
        println()
        print(Color.BLACK.isDarkColor())
        println()
        print(Color.WHITE.isDarkColor())
        println()
        print(Color.RED.isDarkColor())
        println()
        print(Color.GREEN.isDarkColor())
        println()
        print(Color.BLUE.isDarkColor())
        println()
        print(Color.GRAY.isDarkColor())
        println()
        print("//==============================isDarkColor=======================================//")
        println()
    }
}