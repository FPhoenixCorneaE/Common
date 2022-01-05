package com.fphoenixcorneae.common.ext.algorithm

import java.io.ByteArrayOutputStream
import java.nio.charset.Charset
import kotlin.math.ceil

/** The Constant base64EncodeChars.  */
private val BASE_64_ENCODE_CHARS = charArrayOf(
    'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I',
    'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
    'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
    'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
    'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
    's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'
)

/** The Constant base64DecodeChars.  */
private val BASE_64_DECODE_CHARS = byteArrayOf(
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62,
    -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58,
    59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0,
    1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13,
    14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24,
    25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29,
    30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
    41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51,
    -1, -1, -1, -1, -1
)

/**
 * base64编码加密
 */
fun String.encodeBase64(
    charset: Charset = Charset.defaultCharset(),
    width: Int = 0,
): String =
    runCatching {
        val data = toByteArray(charset = charset)
        val size = ceil(data.size * 1.36f).toInt()
        val splitSize = if (width > 0) size / width else 0
        val sb = StringBuffer(size + splitSize)
        val r = data.size % 3
        val length = data.size - r
        var i = 0
        var c: Int
        while (i < length) {
            c =
                (0x000000ff and data[i++].toInt()).shl(16)
                    .or((0x000000ff and data[i++].toInt()) shl 8)
                    .or(0x000000ff and data[i++].toInt())
            sb.append(BASE_64_ENCODE_CHARS[c shr 18])
            sb.append(BASE_64_ENCODE_CHARS[c shr 12 and 0x3f])
            sb.append(BASE_64_ENCODE_CHARS[c shr 6 and 0x3f])
            sb.append(BASE_64_ENCODE_CHARS[c and 0x3f])
        }
        if (r == 1) {
            c = 0x000000ff and data[i++].toInt()
            sb.append(BASE_64_ENCODE_CHARS[c shr 2])
            sb.append(BASE_64_ENCODE_CHARS[c and 0x03 shl 4])
            sb.append("==")
        } else if (r == 2) {
            c = 0x000000ff and data[i++].toInt() shl 8 or (0x000000ff and data[i++].toInt())
            sb.append(BASE_64_ENCODE_CHARS[c shr 10])
            sb.append(BASE_64_ENCODE_CHARS[c shr 4 and 0x3f])
            sb.append(BASE_64_ENCODE_CHARS[c and 0x0f shl 2])
            sb.append("=")
        }
        if (splitSize > 0) {
            val split = '\n'
            i = width
            while (i < sb.length) {
                sb.insert(i, split)
                i += width
                i++
            }
        }
        sb.toString()
    }.onFailure {
        it.printStackTrace()
    }.getOrDefault("")

/**
 * base64编码解密
 */
fun String.decodeBase64(
    charset: Charset = Charset.defaultCharset(),
): String =
    runCatching {
        val data = toByteArray(charset = charset)
        val len = data.size
        val buf = ByteArrayOutputStream((len * 0.67f).toInt())
        var i = 0
        var b1: Int
        var b2: Int
        var b3: Int
        var b4: Int
        while (i < len) {
            do {
                if (i >= len) {
                    b1 = -1
                    break
                }
                b1 = BASE_64_DECODE_CHARS[data[i++].toInt()].toInt()
            } while (i < len && b1 == -1)
            if (b1 == -1) {
                break
            }
            do {
                if (i >= len) {
                    b2 = -1
                    break
                }
                b2 = BASE_64_DECODE_CHARS[data[i++].toInt()].toInt()
            } while (i < len && b2 == -1)
            if (b2 == -1) {
                break
            }
            buf.write(b1 shl 2 or (b2 and 0x30 ushr 4))
            do {
                if (i >= len) {
                    b3 = -1
                    break
                }
                b3 = data[i++].toInt()
                if (b3 == 61) {
                    b3 = -1
                    break
                }
                b3 = BASE_64_DECODE_CHARS[b3].toInt()
            } while (i < len && b3 == -1)
            if (b3 == -1) {
                break
            }
            buf.write(b2 and 0x0f shl 4 or (b3 and 0x3c ushr 2))
            do {
                if (i >= len) {
                    b4 = -1
                    break
                }
                b4 = data[i++].toInt()
                if (b4 == 61) {
                    b4 = -1
                    break
                }
                b4 = BASE_64_DECODE_CHARS[b4].toInt()
            } while (b4 == -1)
            if (b4 == -1) {
                break
            }
            buf.write(b3 and 0x03 shl 6 or b4)
        }
        buf.toString(charset.name())
    }.onFailure {
        it.printStackTrace()
    }.getOrDefault("")