package com.fphoenixcorneae.common.ext.algorithm

import com.fphoenixcorneae.common.ext.toHexString
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.nio.charset.Charset
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * @desc：MD5算法、SHA算法
 */
private fun hash(data: ByteArray, hash: Hash): ByteArray {
    val messageDigest = MessageDigest.getInstance(hash.algorithm)
    return messageDigest.digest(data)
}

fun ByteArray.hash(hash: Hash): String {
    return hash(this, hash).toHexString()
}

fun String.hash(hash: Hash, charset: Charset = Charsets.UTF_8): String {
    return toByteArray(charset).hash(hash)
}

/**
 * MD5
 */
fun ByteArray.md5Bytes(): ByteArray = hash(this, Hash.MD5)
fun ByteArray.md5(): String = hash(this, Hash.MD5).toHexString()
fun String.md5(charset: Charset = Charsets.UTF_8): String = toByteArray(charset).md5()

/**
 * SHA_1
 */
fun ByteArray.sha1Bytes(): ByteArray = hash(this, Hash.SHA_1)
fun ByteArray.sha1(): String = hash(this, Hash.SHA_1).toHexString()
fun String.sha1(charset: Charset = Charsets.UTF_8): String = toByteArray(charset).sha1()

/**
 * SHA_224
 */
fun ByteArray.sha224Bytes(): ByteArray = hash(this, Hash.SHA_224)
fun ByteArray.sha224(): String = hash(this, Hash.SHA_224).toHexString()
fun String.sha224(charset: Charset = Charsets.UTF_8): String = toByteArray(charset).sha224()

/**
 * SHA_256
 */
fun ByteArray.sha256Bytes(): ByteArray = hash(this, Hash.SHA_256)
fun ByteArray.sha256(): String = hash(this, Hash.SHA_256).toHexString()
fun String.sha256(charset: Charset = Charsets.UTF_8): String = toByteArray(charset).sha256()

/**
 * SHA_384
 */
fun ByteArray.sha384Bytes(): ByteArray = hash(this, Hash.SHA_384)
fun ByteArray.sha384(): String = hash(this, Hash.SHA_384).toHexString()
fun String.sha384(charset: Charset = Charsets.UTF_8): String = toByteArray(charset).sha384()

/**
 * SHA_512
 */
fun ByteArray.sha512Bytes(): ByteArray = hash(this, Hash.SHA_512)
fun ByteArray.sha512(): String = hash(this, Hash.SHA_512).toHexString()
fun String.sha512(charset: Charset = Charsets.UTF_8): String = toByteArray(charset).sha512()

fun File.hash(hash: Hash = Hash.SHA_1): String {
    if (!exists() || !isFile) return ""
    val fin: FileInputStream
    val messageDigest: MessageDigest
    val buffer = ByteArray(1024)
    var len: Int
    try {
        messageDigest = MessageDigest.getInstance(hash.algorithm)
        fin = FileInputStream(this)
        do {
            len = fin.read(buffer, 0, 1024)
            if (len != -1) messageDigest.update(buffer, 0, len)
        } while (len != -1)
        fin.close()
        val result = messageDigest.digest()
        return result.toHexString()
    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return ""
}

enum class Hash(val algorithm: String) {
    MD5("MD5"),         // MD5算法 不可逆（Message Digest,消息摘要算法）
    SHA_1("SHA-1"),     // SHA-1算法 不可逆（Secure Hash Algorithm，安全散列算法）
    SHA_224("SHA-224"), // SHA-224算法 不可逆（Secure Hash Algorithm，安全散列算法）
    SHA_256("SHA-256"), // SHA-256算法 不可逆（Secure Hash Algorithm，安全散列算法）
    SHA_384("SHA-384"), // SHA-384算法 不可逆（Secure Hash Algorithm，安全散列算法）
    SHA_512("SHA-512"), // SHA-512算法 不可逆（Secure Hash Algorithm，安全散列算法）
}


