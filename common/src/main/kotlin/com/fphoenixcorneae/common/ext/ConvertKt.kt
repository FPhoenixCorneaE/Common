package com.fphoenixcorneae.common.ext

import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.PixelFormat
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Parcel
import android.os.Parcelable
import com.fphoenixcorneae.common.annotation.MemoryUnit
import kotlinx.parcelize.Parceler
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.OutputStream
import java.io.Serializable
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import java.util.Locale

/**
 * 十六进制数字
 */
val HEX_DIGITS_UPPER = charArrayOf(
    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
)
val HEX_DIGITS_LOWER = charArrayOf(
    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
)

/**
 * Output stream to input stream.
 */
fun OutputStream?.toInputStream(): ByteArrayInputStream? =
    this?.run {
        ByteArrayInputStream((this as ByteArrayOutputStream).toByteArray())
    }

/**
 * Output stream to bytes.
 */
fun OutputStream?.toBytes(): ByteArray? =
    this?.run {
        (this as ByteArrayOutputStream).toByteArray()
    }

/**
 * Output stream to string.
 * @param charset  The charset.
 */
fun OutputStream?.toString(
    charset: Charset,
): String =
    this?.run {
        runCatching {
            String(toBytes()!!, charset)
        }.onFailure {
            it.printStackTrace()
        }.getOrDefault("")
    } ?: ""

/**
 * Bytes to bits.
 */
fun ByteArray?.toBits(): String {
    if (this == null || this.isEmpty()) {
        return ""
    }
    val sb = StringBuilder()
    for (aByte in this) {
        for (j in 7 downTo 0) {
            sb.append(if (aByte.toInt().shr(j) and 0x01 == 0) '0' else '1')
        }
    }
    return sb.toString()
}

/**
 * Bytes to chars.
 */
fun ByteArray?.toChars(): CharArray? {
    if (this == null) {
        return null
    }
    val len = size
    if (len <= 0) {
        return null
    }
    val chars = CharArray(len)
    for (i in 0 until len) {
        chars[i] = (this[i].toInt() and 0xff).toChar()
    }
    return chars
}

/**
 * Bytes to hex string.
 * e.g. bytes2HexString(new byte[] { 0, (byte) 0xa8 }) returns "00A8"
 */
fun ByteArray?.toHexString(): String =
    this?.run {
        val len = this.size
        if (len <= 0) {
            ""
        } else {
            val ret = CharArray(len shl 1)
            var i = 0
            var j = 0
            while (i < len) {
                ret[j++] = HEX_DIGITS_UPPER[this[i].toInt() shr 4 and 0x0f]
                ret[j++] = HEX_DIGITS_UPPER[this[i].toInt() and 0x0f]
                i++
            }
            String(ret)
        }
    } ?: ""

/**
 * Bytes to input stream.
 */
fun ByteArray?.toInputStream(): InputStream? =
    if (this == null || this.isEmpty()) {
        null
    } else {
        ByteArrayInputStream(this)
    }

/**
 * Bytes to output stream.
 */
fun ByteArray?.toOutputStream(): OutputStream? {
    if (this == null || this.isEmpty()) {
        return null
    }
    var os: ByteArrayOutputStream? = null
    return try {
        os = ByteArrayOutputStream()
        os.write(this)
        os
    } catch (e: IOException) {
        e.printStackTrace()
        null
    } finally {
        os?.closeSafely()
    }
}

/**
 * Input stream to output stream.
 */
fun InputStream?.toOutputStream(): ByteArrayOutputStream? {
    return if (this == null) {
        null
    } else {
        try {
            val os = ByteArrayOutputStream()
            val b = ByteArray(MemoryUnit.KB.toInt())
            var len: Int
            while (read(b, 0, MemoryUnit.KB.toInt()).also { len = it } != -1) {
                os.write(b, 0, len)
            }
            os
        } catch (e: IOException) {
            e.printStackTrace()
            null
        } finally {
            closeSafely()
        }
    }
}

/**
 * Input stream to bytes.
 */
fun InputStream?.toBytes(): ByteArray? =
    toOutputStream()?.toByteArray()

/**
 * Input stream to string.
 * @param charset  The charset.
 */
fun InputStream?.toString(
    charset: Charset,
): String =
    this?.run {
        runCatching {
            toOutputStream()?.toString(charset)
        }.onFailure {
            it.printStackTrace()
        }.getOrDefault("")
    } ?: ""

/**
 * String to input stream.
 * @param charset  The charset.
 */
fun String?.toInputStream(
    charset: Charset,
): InputStream? =
    this?.run {
        runCatching {
            ByteArrayInputStream(toByteArray(charset))
        }.onFailure {
            it.printStackTrace()
        }.getOrNull()
    }

/**
 * String to output stream.
 * @param charset  The charset.
 */
fun String?.toOutputStream(
    charset: Charset,
): OutputStream? {
    return if (this == null) {
        null
    } else try {
        toByteArray(charset).toOutputStream()
    } catch (e: UnsupportedEncodingException) {
        e.printStackTrace()
        null
    }
}

/**
 * Chars to bytes.
 */
fun CharArray?.toBytes(): ByteArray? {
    if (this == null || this.isEmpty()) {
        return null
    }
    val len = this.size
    val bytes = ByteArray(len)
    for (i in 0 until len) {
        bytes[i] = this[i].code.toByte()
    }
    return bytes
}

/**
 * Hex string to bytes.
 * e.g. hexString2Bytes("00A8") returns { 0, (byte) 0xA8 }
 */
fun String?.hexString2Bytes(): ByteArray? {
    var hexString = this
    if (hexString.isSpace()) {
        return null
    }
    var len = hexString!!.length
    if (len % 2 != 0) {
        hexString = "0$hexString"
        len += 1
    }
    val hexBytes = hexString.uppercase(Locale.getDefault()).toCharArray()
    val ret = ByteArray(len shr 1)
    var i = 0
    while (i < len) {
        ret[i shr 1] = (hexBytes[i].hexChar2Int() shl 4 or hexBytes[i + 1].hexChar2Int()).toByte()
        i += 2
    }
    return ret
}

/**
 * Hex char to Int
 */
fun Char.hexChar2Int(): Int {
    return when (this) {
        in '0'..'9' -> {
            this - '0'
        }
        in 'A'..'F' -> {
            this - 'A' + 10
        }
        else -> {
            throw IllegalArgumentException()
        }
    }
}

/**
 * Size of memory in unit to size of byte.
 * @param unit The unit of memory size.
 *  * [MemoryUnit.BYTE]
 *  * [MemoryUnit.KB]
 *  * [MemoryUnit.MB]
 *  * [MemoryUnit.GB]
 */
fun Long.memorySize2Byte(
    @MemoryUnit unit: Long,
): Long {
    return if (this < 0) {
        0
    } else {
        this * unit
    }
}

/**
 * Size of byte to size of memory in unit.
 * @param unit The unit of memory size.
 *  * [MemoryUnit.BYTE]
 *  * [MemoryUnit.KB]
 *  * [MemoryUnit.MB]
 *  * [MemoryUnit.GB]
 */
fun Long.byte2MemorySize(
    @MemoryUnit unit: Long,
): Double =
    if (this < 0) {
        0.0
    } else {
        toDouble() / unit
    }

/**
 * Size of byte to fit size of memory(字节数转合适内存大小).
 * to three decimal places(保留3位小数)
 */
fun Long.byte2FitMemorySize(): String =
    when {
        this < 0 -> {
            "shouldn't be less than zero!"
        }
        this < MemoryUnit.KB -> {
            String.format(Locale.getDefault(), "%.3fB", toDouble() + 0.0005)
        }
        this < MemoryUnit.MB -> {
            String.format(Locale.getDefault(), "%.3fKB", toDouble() / MemoryUnit.KB + 0.0005)
        }
        this < MemoryUnit.GB -> {
            String.format(Locale.getDefault(), "%.3fMB", toDouble() / MemoryUnit.MB + 0.0005)
        }
        else -> {
            String.format(Locale.getDefault(), "%.3fGB", toDouble() / MemoryUnit.GB + 0.0005)
        }
    }

/**
 * Time span in unit to milliseconds.
 * @param unit  The unit of time span.
 */
fun Long.timeSpan2Millis(unit: Long): Long {
    return this * unit
}

/**
 * Milliseconds to time span in unit.
 * @param unit   The unit of time span.
 */
fun Long.millis2TimeSpan(unit: Long): Long {
    return this / unit
}

/**
 * Milliseconds to fit time span.
 * @param precision The precision of time span.
 *  * precision = 0, return null
 *  * precision = 1, return 天
 *  * precision = 2, return 天, 小时
 *  * precision = 3, return 天, 小时, 分钟
 *  * precision = 4, return 天, 小时, 分钟, 秒
 *  * precision &gt;= 5，return 天, 小时, 分钟, 秒, 毫秒
 *
 * @return fit time span
 */
fun Long.millis2FitTimeSpan(precision: Int): String? {
    var millis = this
    var precision = precision
    if (millis <= 0 || precision <= 0) {
        return null
    }
    val sb = StringBuilder()
    val units =
        arrayOf("天", "小时", "分钟", "秒", "毫秒")
    val unitLen = longArrayOf(1.DAYS, 1.HOURS, 1.MINUTES, 1.SECONDS, 1.MILLISECONDS)
    precision = precision.coerceAtMost(5)
    for (i in 0 until precision) {
        if (millis >= unitLen[i]) {
            val mode = millis / unitLen[i]
            millis -= mode * unitLen[i]
            sb.append(mode).append(units[i])
        }
    }
    return sb.toString()
}

//======================================Bitmap、Drawable、ByteArray相互转换Start=========================================//
/**
 * Bitmap to bytes.
 * @param format The format of bitmap.
 */
fun Bitmap?.toBytes(
    format: CompressFormat = CompressFormat.JPEG,
    quality: Int = 100,
): ByteArray? =
    this?.run {
        val baos = ByteArrayOutputStream()
        compress(format, quality, baos)
        baos.toByteArray()
    }

/**
 * Bitmap to drawable.
 */
fun Bitmap?.toDrawable(): Drawable? =
    this?.run {
        BitmapDrawable(applicationContext.resources, this)
    }

/**
 * Drawable to bitmap.
 */
fun Drawable?.toBitmap(): Bitmap? =
    this?.run {
        if (this is BitmapDrawable) {
            bitmap
        } else {
            val bitmap: Bitmap = if (intrinsicWidth <= 0 || intrinsicHeight <= 0) {
                Bitmap.createBitmap(
                    1, 1,
                    if (opacity != PixelFormat.OPAQUE) Bitmap.Config.ARGB_8888 else Bitmap.Config.RGB_565
                )
            } else {
                Bitmap.createBitmap(
                    intrinsicWidth, intrinsicHeight,
                    if (opacity != PixelFormat.OPAQUE) Bitmap.Config.ARGB_8888 else Bitmap.Config.RGB_565
                )
            }
            val canvas = Canvas(bitmap)
            setBounds(0, 0, canvas.width, canvas.height)
            draw(canvas)
            bitmap
        }
    }

/**
 * Drawable to bytes.
 * @param format   The format of bitmap.
 */
fun Drawable?.toBytes(
    format: CompressFormat = CompressFormat.JPEG,
    quality: Int = 100,
): ByteArray? =
    this?.toBitmap()?.toBytes(format, quality)

/**
 * Bytes to bitmap.
 */
fun ByteArray?.toBitmap(offset: Int = 0): Bitmap? =
    this?.run {
        runCatching {
            BitmapFactory.decodeByteArray(this, offset, this.size)
        }.onFailure {
            it.printStackTrace()
        }.getOrNull()
    }

/**
 * Bytes to drawable.
 */
fun ByteArray?.toDrawable(): Drawable? =
    this?.toBitmap()?.toDrawable()

//======================================Bitmap、Drawable、ByteArray相互转换End=========================================//

/**
 * Bytes to JSONObject.
 */
fun ByteArray?.toJSONObject(): JSONObject? = this?.runCatching {
    JSONObject(String(this))
}?.onFailure {
    it.printStackTrace()
}?.getOrNull()

/**
 * JSONObject to bytes.
 */
fun JSONObject?.toBytes(): ByteArray? = this?.toString()?.toByteArray()

/**
 * Bytes to JSONArray.
 */
fun ByteArray?.toJSONArray(): JSONArray? = this?.runCatching {
    JSONArray(String(this))
}?.onFailure {
    it.printStackTrace()
}?.getOrNull()

/**
 * JSONArray to bytes.
 */
fun JSONArray?.toBytes(): ByteArray? = this?.toString()?.toByteArray()

/**
 * Bytes to Parcelable
 */
fun <T> ByteArray?.toParcelable(
    creator: Parcelable.Creator<T>,
): T? =
    this?.run {
        val parcel = Parcel.obtain()
        parcel.unmarshall(this, 0, this.size)
        parcel.setDataPosition(0)
        val result = creator.createFromParcel(parcel)
        parcel.recycle()
        result
    }

/**
 * Bytes to Parcelable
 */
fun <T> ByteArray?.toParcelable(
    parceler: Parceler<T>,
): T? =
    this?.run {
        val parcel = Parcel.obtain()
        parcel.unmarshall(this, 0, this.size)
        parcel.setDataPosition(0)
        val result = parceler.create(parcel)
        parcel.recycle()
        result
    }

/**
 * Parcelable to bytes.
 */
fun Parcelable?.toBytes(): ByteArray? =
    this?.run {
        val parcel = Parcel.obtain()
        writeToParcel(parcel, 0)
        val bytes = parcel.marshall()
        parcel.recycle()
        bytes
    }

/**
 * Bytes to Serializable.
 */
fun ByteArray?.toObject(): Any? {
    if (this == null) {
        return null
    }
    var ois: ObjectInputStream? = null
    return try {
        ois = ObjectInputStream(ByteArrayInputStream(this))
        ois.readObject()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    } finally {
        ois.closeSafely()
    }
}

/**
 * Serializable to bytes.
 */
fun Serializable?.toBytes(): ByteArray? {
    if (this == null) {
        return null
    }
    var baos: ByteArrayOutputStream
    var oos: ObjectOutputStream? = null
    return try {
        oos = ObjectOutputStream(ByteArrayOutputStream().also { baos = it })
        oos.writeObject(this)
        baos.toByteArray()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    } finally {
        oos.closeSafely()
    }
}

