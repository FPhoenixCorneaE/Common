package com.fphoenixcorneae.common.ext

import com.fphoenixcorneae.common.ext.gson.toJson
import com.fphoenixcorneae.common.ext.gson.toObject

/**
 * Deep clone.
 * @param classOfT The type.
 * @param <T>  The value type.
 * @return The object of cloned.
 */
fun <T> T?.deepClone(classOfT: Class<T>): T? =
    runCatching {
        toJson().toObject(classOfT)
    }.onFailure {
        it.printStackTrace()
    }.getOrNull()