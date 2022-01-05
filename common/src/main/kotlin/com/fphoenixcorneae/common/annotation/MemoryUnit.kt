package com.fphoenixcorneae.common.annotation

import androidx.annotation.Keep
import java.math.BigDecimal

/**
 * @desc 内存单位
 */
@Keep
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE, AnnotationTarget.VALUE_PARAMETER)
@kotlin.annotation.Retention(AnnotationRetention.BINARY)
annotation class MemoryUnit {
    companion object {

        /** Byte与Byte的倍数 */
        const val BYTE: Long = 1

        /** KB与Byte的倍数 */
        const val KB: Long = BYTE * 1024

        /** MB与Byte的倍数 */
        const val MB: Long = KB * 1024

        /** GB与Byte的倍数 */
        const val GB: Long = MB * 1024

        /** TB与Byte的倍数 */
        const val TB: Long = GB * 1024

        /** PB与Byte的倍数 */
        const val PB: Long = TB * 1024

        /** EB与Byte的倍数 */
        const val EB: Long = PB * 1024

        /** ZB与Byte的倍数 */
        val ZB: BigDecimal = BigDecimal(EB).multiply(BigDecimal(1024))

        /** YB与Byte的倍数 */
        val YB: BigDecimal = ZB.multiply(BigDecimal(1024))

        /** BB与Byte的倍数 */
        val BB: BigDecimal = YB.multiply(BigDecimal(1024))

        /** NB与Byte的倍数 */
        val NB: BigDecimal = BB.multiply(BigDecimal(1024))

        /** DB与Byte的倍数 */
        val DB: BigDecimal = NB.multiply(BigDecimal(1024))

        /** CB与Byte的倍数 */
        val CB: BigDecimal = DB.multiply(BigDecimal(1024))

        /** XB与Byte的倍数 */
        val XB: BigDecimal = CB.multiply(BigDecimal(1024))
    }
}
