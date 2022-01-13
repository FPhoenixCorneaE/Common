package com.fphoenixcorneae.common.ext

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

/**
 * 序列化对象
 */
inline fun <reified T> T.serialize(): String {
    val byteArrayOutputStream = ByteArrayOutputStream()
    val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
    var serStr = ""
    try {
        objectOutputStream.writeObject(this)
        serStr = byteArrayOutputStream.toString(ISO_8859_1.name())
        serStr = java.net.URLEncoder.encode(serStr, UTF_8.name())
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        closeIOQuietly(objectOutputStream, byteArrayOutputStream)
    }
    return serStr
}

/**
 * 反序列化对象
 */
inline fun <reified T> String.deserialize(): T? {
    val redStr = java.net.URLDecoder.decode(this, UTF_8.name())
    val byteArrayInputStream = ByteArrayInputStream(redStr.toByteArray(charset(ISO_8859_1.name())))
    val objectInputStream = ObjectInputStream(byteArrayInputStream)
    var obj: Any? = null
    try {
        obj = objectInputStream.readObject()
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        closeIOQuietly(objectInputStream, byteArrayInputStream)
    }
    return obj as? T
}