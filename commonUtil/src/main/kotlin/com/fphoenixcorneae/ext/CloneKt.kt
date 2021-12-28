package com.fphoenixcorneae.ext

import com.fphoenixcorneae.ext.gson.toJson
import com.fphoenixcorneae.ext.gson.toObject

/**
 * Deep clone.
 * @param classOfT The type.
 * @param <T>  The value type.
 * @return The object of cloned.
 */
fun <T> T?.deepClone(classOfT: Class<T>): T? {
    return try {
        toJson().toObject(classOfT)
    } catch (e: Exception) {
        e.logd()
        null
    }
}