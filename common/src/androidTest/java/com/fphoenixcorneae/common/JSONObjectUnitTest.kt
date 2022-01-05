package com.fphoenixcorneae.common

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.fphoenixcorneae.common.ext.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class JSONObjectUnitTest {

    @Test
    fun getIntValue() {
        val jsonString = "{\"int\":1000}"
        print("//==============================getIntValue=======================================//")
        println()
        print(jsonString.toJSONObject().getIntValue("int", 0))
        println()
        print("//==============================getIntValue=======================================//")
        println()
    }

    @Test
    fun getLongValue() {
        val jsonString = "{\"long\":888}"
        print("//==============================getLongValue=======================================//")
        println()
        print(jsonString.toJSONObject().getLongValue("long", 0))
        println()
        print("//==============================getLongValue=======================================//")
        println()
    }

    @Test
    fun getDoubleValue() {
        val jsonString = "{\"double\":888}"
        print("//==============================getDoubleValue=======================================//")
        println()
        print(jsonString.toJSONObject().getDoubleValue("double", 0.0))
        println()
        print("//==============================getDoubleValue=======================================//")
        println()
    }

    @Test
    fun getBooleanValue() {
        val jsonString = "{\"boolean\":true}"
        print("//==============================getBooleanValue=======================================//")
        println()
        print(jsonString.toJSONObject().getBooleanValue("boolean", false))
        println()
        print("//==============================getBooleanValue=======================================//")
        println()
    }

    @Test
    fun getStringValue() {
        val jsonString = "{\"string\":0.0}"
        print("//==============================getStringValue=======================================//")
        println()
        print(jsonString.toJSONObject().getStringValue("string", "defaultValue"))
        println()
        print("//==============================getStringValue=======================================//")
        println()
    }

    @Test
    fun getJSONObjectValue() {
        val jsonString = "{\"JSONObject\": {\"string\":0.0}}"
        print("//==============================getJSONObjectValue=======================================//")
        println()
        print(jsonString.toJSONObject().getJSONObjectValue("JSONObject"))
        println()
        print("//==============================getJSONObjectValue=======================================//")
        println()
    }

    @Test
    fun getJSONArrayValue() {
        val jsonString = "{\"JSONArray\": [{\"string\":0.0}]}"
        print("//==============================getJSONArrayValue=======================================//")
        println()
        print(jsonString.toJSONObject().getJSONArrayValue("JSONArray"))
        println()
        print("//==============================getJSONArrayValue=======================================//")
        println()
    }

    @Test
    fun toMap() {
        val jsonString = "{\"JSONArray\": [{\"string\":0.0}]}"
        print("//==============================toMap=======================================//")
        println()
        print(jsonString.toJSONObject().toMap())
        println()
        print("//==============================toMap=======================================//")
        println()
    }

    @Test
    fun toJSONObject() {
        val map = hashMapOf("key1" to "value1", "key2" to "value2")
        print("//==============================toJSONObject=======================================//")
        println()
        print(map.toJSONObject())
        println()
        print("//==============================toJSONObject=======================================//")
        println()
    }
}