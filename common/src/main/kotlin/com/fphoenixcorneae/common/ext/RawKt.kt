package com.fphoenixcorneae.common.ext

import androidx.annotation.RawRes
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.StringWriter
import java.nio.charset.Charset

/**
 * 读取 res->raw 文件夹下的 .json 文件
 * @return Json String
 */
fun readRawToString(
    @RawRes id: Int,
    charset: Charset = Charset.defaultCharset()
): String {
    val `is` = applicationContext.resources.openRawResource(id)
    val writer = StringWriter()
    val buffer = CharArray(1024)
    `is`.use {
        val reader = BufferedReader(InputStreamReader(it, charset))
        var n: Int
        while (reader.read(buffer).also { n = it } != -1) {
            writer.write(buffer, 0, n)
        }
    }
    return writer.toString()
}