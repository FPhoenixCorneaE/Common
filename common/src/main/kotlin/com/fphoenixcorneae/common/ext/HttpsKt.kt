package com.fphoenixcorneae.common.ext

import java.io.DataOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

private const val CONNECT_TIMEOUT = 15_000
private const val READ_TIMEOUT = 20_000

/**
 * POST + JSON
 *
 * @param data send data
 * @param url  target url
 * @return data receive from server
 */
fun postJson(data: String, url: String): String? {
    return doHttpAction(url = url, data = data, json = true, post = true)
}

/**
 * POST + FORM
 *
 * @param data send data
 * @param url  target url
 * @return data receive from server
 */
fun postForm(data: String, url: String): String? {
    return doHttpAction(url = url, data = data, json = false, post = true)
}

/**
 * GET + JSON
 *
 * @param data send data
 * @param url  target url
 * @return data receive from server
 */
fun getJson(data: String, url: String): String? {
    return doHttpAction(url = url, data = data, json = true, post = false)
}

/**
 * GET + FORM
 *
 * @param data send data
 * @param url  target url
 * @return data receive from server
 */
fun getForm(data: String, url: String): String? {
    return doHttpAction(url = url, data = data, json = false, post = false)
}

/**
 * @param url  target url
 * @param data send data
 */
private fun doHttpAction(
    url: String,
    data: String,
    json: Boolean,
    post: Boolean
): String? {
    var connection: HttpURLConnection? = null
    var os: DataOutputStream? = null
    var `is`: InputStream? = null
    try {
        connection = URL(url).openConnection() as HttpURLConnection
        connection.connectTimeout = CONNECT_TIMEOUT
        connection.readTimeout = READ_TIMEOUT
        if (post) {
            connection.requestMethod = "POST"
        } else {
            connection.requestMethod = "GET"
        }
        //允许输入输出
        connection.doInput = true
        connection.doOutput = true
        // 是否使用缓冲
        connection.useCaches = false
        // 本次连接是否处理重定向，设置成true，系统自动处理重定向；
        // 设置成false，则需要自己从http reply中分析新的url自己重新连接。
        connection.instanceFollowRedirects = true
        // 设置请求头里的属性
        if (json) {
            connection.setRequestProperty("Content-Type", "application/json")
        } else {
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
            connection.setRequestProperty("Content-Length", "${data.length}")
        }
        connection.connect()
        os = DataOutputStream(connection.outputStream)
        os.write(data.toByteArray(), 0, data.toByteArray().size)
        os.flush()
        os.close()
        `is` = connection.inputStream
        val scan = Scanner(`is`)
        scan.useDelimiter("\\A")
        if (scan.hasNext()) {
            return scan.next()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        connection?.disconnect()
        os.closeSafely()
        `is`.closeSafely()
    }
    return null
}