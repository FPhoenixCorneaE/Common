package com.fphoenixcorneae.common.util

import com.fphoenixcorneae.common.ext.loge
import java.io.Closeable
import java.io.IOException

/**
 * IO关闭相关工具类
 * @date 2019/06/20 21:45
 */
class CloseUtil private constructor() {

    init {
        throw UnsupportedOperationException("U can't initialize me...")
    }

    companion object {

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
                    e.toString().loge()
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
    }
}
