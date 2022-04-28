package com.fphoenixcorneae.common.ext

import java.io.Closeable
import java.io.IOException

/**
 * 安静的关闭IO
 */
fun Closeable?.closeQuietly() = runCatching {
    this?.close()
}.onFailure {
    // do nothing
}

/**
 * 安全的关闭IO
 */
fun Closeable?.closeSafely() = runCatching {
    this?.close()
}.onFailure {
    it.printStackTrace()
}

/**
 * 关闭IO
 *
 * @param closeables closeables
 */
fun closeIO(vararg closeables: Closeable?) {
    for (closeable in closeables) {
        closeable.closeSafely()
    }
}

/**
 * 安静关闭IO
 *
 * @param closeables closeables
 */
fun closeIOQuietly(vararg closeables: Closeable?) {
    for (closeable in closeables) {
        try {
            closeable?.close()
        } catch (e: IOException) {
            // do nothing
        }
    }
}
