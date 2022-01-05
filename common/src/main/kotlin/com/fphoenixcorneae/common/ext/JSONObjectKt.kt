package com.fphoenixcorneae.common.ext

import com.fphoenixcorneae.common.ext.gson.toJson
import org.json.JSONArray
import org.json.JSONObject

/**
 * get Int from JSONObject
 */
fun JSONObject.getIntValue(
    key: String,
    defaultValue: Int = 0
): Int =
    runCatching {
        getInt(key)
    }.onFailure {
        it.printStackTrace()
    }.getOrDefault(defaultValue)

/**
 * get Long from JSONObject
 */
fun JSONObject.getLongValue(
    key: String,
    defaultValue: Long = 0
): Long =
    runCatching {
        getLong(key)
    }.onFailure {
        it.printStackTrace()
    }.getOrDefault(defaultValue)

/**
 * get Double from JSONObject
 */
fun JSONObject.getDoubleValue(
    key: String,
    defaultValue: Double = 0.0
): Double =
    runCatching {
        getDouble(key)
    }.onFailure {
        it.printStackTrace()
    }.getOrDefault(defaultValue)

/**
 * get Boolean from JSONObject
 */
fun JSONObject.getBooleanValue(
    key: String,
    defaultValue: Boolean = false
): Boolean =
    runCatching {
        getBoolean(key)
    }.onFailure {
        it.printStackTrace()
    }.getOrDefault(defaultValue)

/**
 * get String from JSONObject
 */
fun JSONObject.getStringValue(
    key: String,
    defaultValue: String = ""
): String =
    runCatching {
        getString(key)
    }.onFailure {
        it.printStackTrace()
    }.getOrDefault(defaultValue)

/**
 * get JSONObject from JSONObject
 */
fun JSONObject.getJSONObjectValue(
    key: String,
    defaultValue: JSONObject = JSONObject("{}")
): JSONObject =
    runCatching {
        getJSONObject(key)
    }.onFailure {
        it.printStackTrace()
    }.getOrDefault(defaultValue)

/**
 * get JSONArray from JSONObject
 */
fun JSONObject.getJSONArrayValue(
    key: String,
    defaultValue: JSONArray = JSONArray("[]")
): JSONArray =
    runCatching {
        getJSONArray(key)
    }.onFailure {
        it.printStackTrace()
    }.getOrDefault(defaultValue)

/**
 * parse key-value pairs to map.
 */
fun JSONObject.toMap(): HashMap<String, String> =
    run {
        val keyValueMap = hashMapOf<String, String>()
        val iterator: Iterator<String> = keys()
        while (iterator.hasNext()) {
            val key = iterator.next()
            keyValueMap[key] = getStringValue(key, "")
        }
        keyValueMap
    }

/**
 * parse map to key-value pairs.
 */
fun HashMap<String, String>.toJSONObject(): JSONObject =
    run {
        toJson().toJSONObject()
    }

/**
 * parse json string to key-value pairs.
 */
fun String.toJSONObject(): JSONObject =
    runCatching {
        JSONObject(this)
    }.onFailure {
        it.printStackTrace()
    }.getOrDefault(JSONObject("{}"))
