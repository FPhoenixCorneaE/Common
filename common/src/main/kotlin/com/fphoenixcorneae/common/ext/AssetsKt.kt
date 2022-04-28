package com.fphoenixcorneae.common.ext

import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.Charset

/**
 * 读取 assets 文件夹下 Json 文件
 * @param fileName 文件名称
 * @return Json    String
 */
fun readAssetsToString(
    fileName: String,
    charset: Charset = Charset.defaultCharset()
): String {
    val stringBuilder = StringBuilder()
    // 获得 assets 资源管理器
    // 使用 IO 流读取 json 文件内容
    var bufferedReader: BufferedReader? = null
    try {
        val `is` = applicationContext.assets.open(fileName)
        bufferedReader = BufferedReader(InputStreamReader(`is`, charset))
        var line: String?
        while (bufferedReader.readLine().also { line = it } != null) {
            stringBuilder.append(line)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        closeIOQuietly(bufferedReader)
    }
    return stringBuilder.toString()
}