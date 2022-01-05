package com.fphoenixcorneae.common.ext

import java.io.File
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

val ISO_8859_1 = StandardCharsets.ISO_8859_1
val UTF_8 = StandardCharsets.UTF_8
val GBK = Charset.forName("GBK")

/**
 * 字符集名称转换为Charset对象
 */
fun String?.toCharset(): Charset =
    runCatching {
        if (isNullOrBlank()) {
            defaultCharset()
        } else {
            Charset.forName(this)
        }
    }.onFailure {
        it.printStackTrace()
    }.getOrDefault(defaultCharset())

/**
 * 转换字符串的字符集编码<br></br>
 * 当以错误的编码读取为字符串时，打印字符串将出现乱码。<br></br>
 * 此方法用于纠正因读取使用编码错误导致的乱码问题。<br></br>
 * 例如，在Servlet请求中客户端用GBK编码了请求参数，我们使用UTF-8读取到的是乱码，此时，使用此方法即可还原原编码的内容
 * <pre>
 * 客户端 -》 GBK编码 -》 Servlet容器 -》 UTF-8解码 -》 乱码
 * 乱码 -》 UTF-8编码 -》 GBK解码 -》 正确内容
 * </pre>
 * @param srcCharset  源字符集，默认ISO-8859-1
 * @param destCharset 目标字符集，默认UTF-8
 * @return 转换后的字符集
 */
fun String?.convert(
    srcCharset: Charset = ISO_8859_1,
    destCharset: Charset = UTF_8
): String? =
    if (isNullOrBlank() || srcCharset == destCharset) {
        this
    } else {
        String(toByteArray(srcCharset), destCharset)
    }

/**
 * 转换文件编码<br></br>
 * 此方法用于转换文件编码，读取的文件实际编码必须与指定的srcCharset编码一致，否则导致乱码
 * @param srcCharset  原文件的编码，必须与文件内容的编码保持一致
 * @param destCharset 转码后的编码
 * @return 被转换编码的文件
 */
fun File?.convert(
    srcCharset: Charset,
    destCharset: Charset
): File? {
    val str = readFile2String(this, srcCharset)
    writeFileFromString(this, str, false, destCharset)
    return this
}

/**
 * 系统字符集编码，如果是Windows，则默认为GBK编码，否则取[defaultCharset]
 */
fun systemCharset(): Charset =
    if (isWindows) GBK else defaultCharset()

/**
 * 系统默认字符集编码
 */
fun defaultCharset(): Charset =
    Charset.defaultCharset()

val isWindows: Boolean
    get() =
        File.separatorChar == '\\'
