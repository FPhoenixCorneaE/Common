package com.fphoenixcorneae.common.ext

import android.annotation.SuppressLint
import android.os.Build
import android.text.Html
import android.util.Base64
import java.io.*
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.Charset

/**
 * Return the urlencoded string.
 * @param charset     The charset.
 */
fun String?.urlEncode(charset: Charset = UTF_8): String {
    return if (this == null || this.isEmpty()) {
        ""
    } else {
        try {
            URLEncoder.encode(this, charset.name())
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
            ""
        }
    }
}

/**
 * Return the string of decode urlencoded string.
 * @param charset     The charset.
 */
fun String?.urlDecode(charset: Charset = UTF_8): String {
    return if (this == null || this.isEmpty()) {
        ""
    } else try {
        val safeInput = this.replace("%(?![0-9a-fA-F]{2})".toRegex(), "%25").replace("\\+".toRegex(), "%2B")
        URLDecoder.decode(safeInput, charset.name())
    } catch (e: UnsupportedEncodingException) {
        e.printStackTrace()
        ""
    }
}

/**
 * Return Base64-encode bytes.
 */
fun String?.base64Encode2Bytes(): ByteArray =
    this?.toByteArray()?.base64Encode2Bytes() ?: ByteArray(0)

/**
 * Return Base64-encode bytes.
 */
fun ByteArray?.base64Encode2Bytes(): ByteArray {
    return if (this == null || this.isEmpty()) {
        ByteArray(0)
    } else {
        Base64.encode(this, Base64.NO_WRAP)
    }
}

/**
 * Return Base64-encode string.
 */
fun ByteArray?.base64Encode2String(): String {
    return if (this == null || this.isEmpty()) {
        ""
    } else {
        Base64.encodeToString(this, Base64.NO_WRAP)
    }
}

/**
 * Return Base64-encode string.
 */
fun File?.base64Encode2String(): String {
    return if (this == null || !this.exists()) {
        ""
    } else {
        var `is`: InputStream? = null
        val data: ByteArray?
        var result = ""
        try {
            `is` = FileInputStream(this)
            // 创建一个字符流大小的数组。
            data = ByteArray(`is`.available())
            // 写入数组
            `is`.read(data)
            // 用默认的编码格式进行编码
            result = Base64.encodeToString(data, Base64.NO_WRAP)
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } finally {
            closeIOQuietly(`is`)
        }
        result
    }
}

/**
 * Return the bytes of decode Base64-encode string.
 */
fun String?.base64Decode(): ByteArray {
    return if (this == null || this.isEmpty()) {
        ByteArray(0)
    } else {
        Base64.decode(this, Base64.NO_WRAP)
    }
}

/**
 * Return the bytes of decode Base64-encode bytes.
 */
fun ByteArray?.base64Decode(): ByteArray {
    return if (this == null || this.isEmpty()) {
        ByteArray(0)
    } else {
        Base64.decode(this, Base64.NO_WRAP)
    }
}

/**
 * Return html-encode string.
 */
fun CharSequence?.htmlEncode(): String {
    if (this == null || this.isEmpty()) {
        return ""
    }
    val sb = StringBuilder()
    var c: Char
    var i = 0
    val len = this.length
    while (i < len) {
        c = this[i]
        when (c) {
            '<' -> sb.append("&lt;") //$NON-NLS-1$
            '>' -> sb.append("&gt;") //$NON-NLS-1$
            '&' -> sb.append("&amp;") //$NON-NLS-1$
            '\'' ->                     //http://www.w3.org/TR/xhtml1
                // The named character reference &apos; (the apostrophe, U+0027) was
                // introduced in XML 1.0 but does not appear in HTML. Authors should
                // therefore use &#39; instead of &apos; to work as expected in HTML 4
                // user agents.
                sb.append("&#39;") //$NON-NLS-1$
            '"' -> sb.append("&quot;") //$NON-NLS-1$
            else -> sb.append(c)
        }
        i++
    }
    return sb.toString()
}

/**
 * Return the string of decode html-encode string.
 */
@SuppressLint("ObsoleteSdkInt")
fun String?.htmlDecode(): CharSequence {
    if (this == null || this.isEmpty()) {
        return ""
    }
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
    } else {
        Html.fromHtml(this)
    }
}

/**
 * Return the binary encoded string padded with one space
 */
fun String?.binaryEncode(): String {
    if (this == null || this.isEmpty()) {
        return ""
    }
    val sb = StringBuilder()
    for (i in this.toCharArray()) {
        sb.append(Integer.toBinaryString(i.code)).append(" ")
    }
    return sb.deleteCharAt(sb.length - 1).toString()
}

/**
 * Return UTF-8 String from binary
 */
fun String?.binaryDecode(): String {
    if (this == null || this.isEmpty()) {
        return ""
    }
    val splits = this.split(" ").toTypedArray()
    val sb = StringBuilder()
    for (split in splits) {
        sb.append(split.toInt(2).toChar())
    }
    return sb.toString()
}