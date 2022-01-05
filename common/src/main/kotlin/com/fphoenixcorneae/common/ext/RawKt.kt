package com.fphoenixcorneae.common.ext

import androidx.annotation.RawRes
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.StringWriter

/**
 * 读取 res->raw 文件夹下的 .json 文件
 * @return Json String
 */
fun readFileFromRaw(@RawRes id: Int): String {
    val `is` = appContext.resources.openRawResource(id)
    val writer = StringWriter()
    val buffer = CharArray(1024)
    `is`.use {
        val reader = BufferedReader(InputStreamReader(it, Charsets.UTF_8))
        var n: Int
        while (reader.read(buffer).also { n = it } != -1) {
            writer.write(buffer, 0, n)
        }
    }
    return writer.toString()
}