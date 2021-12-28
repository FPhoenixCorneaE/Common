package com.fphoenixcorneae.ext

import java.io.Closeable
import java.io.IOException

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
        try {
            closeable?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
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
