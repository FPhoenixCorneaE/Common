package com.fphoenixcorneae.common

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.fphoenixcorneae.common.ext.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class SharedPrefUnitTest {

    @Test
    fun putSp() {
        println("//==============================putSp=======================================//")
        println(putSP("testSPString" to "test put Sp String."))
        println(putSP("testSPBoolean" to true))
        println(putSP("testSPFloat" to 88.88f))
        println(putSP("testSPInt" to 188))
        println(putSP("testSPLong" to 988L))
        println(putSP("testHashMap" to hashMapOf("key1" to "value1", "key2" to "value2")))
        println("//==============================putSp=======================================//")
    }

    @Test
    fun getSp() {
        println("//==============================getSp=======================================//")
        println(getSP("testSPString", ""))
        println(getSP("testSPBoolean", false))
        println(getSP("testSPFloat", 0f))
        println(getSP("testSPInt", 0))
        println(getSP("testSPLong", 0L))
        println(getSP("testHashMap", hashMapOf<String, String>()))
        println("//==============================getSp=======================================//")
    }

    @Test
    fun getSpAll() {
        println("//==============================getSpAll=======================================//")
        println(getSPAll())
        println("//==============================getSpAll=======================================//")
    }

    @Test
    fun containsSp() {
        println("//==============================containsSp=======================================//")
        println(containsSP("testHashMap"))
        println("//==============================containsSp=======================================//")
    }

    @Test
    fun removeSp() {
        println("//==============================removeSp=======================================//")
        println(removeSP("testHashMap"))
        println("//==============================removeSp=======================================//")
    }

    @Test
    fun clearSp() {
        println("//==============================clearSp=======================================//")
        println(clearSP())
        println("//==============================clearSp=======================================//")
    }
}