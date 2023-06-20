package com.fphoenixcorneae.common.ext.algorithm

import android.os.Build
import androidx.annotation.RequiresApi
import com.fphoenixcorneae.common.ext.hexString2Bytes
import com.fphoenixcorneae.common.ext.toHexString
import java.security.Key
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * 3DES对称加密(加/解密必须使用同一个Key):
 * Triple DES、DESede，进行了三重DES加密的算法，对称加密算法
 */

/** 算法类型：用于指定生成 DESede 的密钥 */
private const val DES3_ALGORITHM = "DESede"

private const val SLAT_KEY_LENGTH = 24
private const val VECTOR_KEY_LENGTH = 8

/**
 * 获取加密的密匙，传入的slatKey
 * @param slatKey 密码长度要是24的倍数
 */
private fun getSlatKey(slatKey: String): Key? = runCatching {
    if (slatKey.length != SLAT_KEY_LENGTH) {
        throw Exception("slatKey is null or slatKey is not at $SLAT_KEY_LENGTH-bytes.")
    }
    SecretKeySpec(slatKey.toByteArray(), DES3_ALGORITHM)
}.onFailure {
    it.printStackTrace()
}.getOrNull()

/**
 * 获取加密的向量
 * @param vectorKey 密码长度要是8的倍数
 */
private fun getVectorKey(vectorKey: String): IvParameterSpec = runCatching {
    if (vectorKey.length != VECTOR_KEY_LENGTH) {
        throw Exception("vectorKey is null or vectorKey is not at $VECTOR_KEY_LENGTH-bytes.")
    }
    IvParameterSpec(vectorKey.toByteArray())
}.onFailure {
    it.printStackTrace()
}.getOrThrow()

private fun initCipher(
    mode: Int,
    slatKey: String,
    vectorKey: String,
    cipherAlgorithm: Des3Cipher,
): Cipher {
    val cipher = Cipher.getInstance(cipherAlgorithm.transformation)
    if (Des3Cipher.CBC_NO_PADDING == cipherAlgorithm || Des3Cipher.CBC_PKCS5PADDING == cipherAlgorithm) {
        cipher.init(mode, getSlatKey(slatKey), getVectorKey(vectorKey))
    } else {
        cipher.init(mode, getSlatKey(slatKey))
    }
    return cipher
}

/**
 * DES 加密
 */
@RequiresApi(Build.VERSION_CODES.O)
fun String.des3EncryptBase64(
    slatKey: String,
    vectorKey: String,
    cipherAlgorithm: Des3Cipher = Des3Cipher.CBC_PKCS5PADDING,
): String = runCatching {
    val cipher = initCipher(Cipher.ENCRYPT_MODE, slatKey, vectorKey, cipherAlgorithm)
    val plainText = if (Des3Cipher.CBC_NO_PADDING == cipherAlgorithm || Des3Cipher.ECB_NO_PADDING == cipherAlgorithm) {
        handleNoPaddingEncryptFormat(cipher, this)
    } else {
        toByteArray()
    }
    // 加密
    val result = cipher.doFinal(plainText)
    // 使用BASE64对加密后的二进制数组进行编码
    Base64.getEncoder().encodeToString(result)
}.onFailure {
    it.printStackTrace()
}.getOrDefault("")

/**
 * DES 加密
 */
fun String.des3EncryptHex(
    slatKey: String,
    vectorKey: String,
    cipherAlgorithm: Des3Cipher = Des3Cipher.CBC_PKCS5PADDING,
): String = runCatching {
    val cipher = initCipher(Cipher.ENCRYPT_MODE, slatKey, vectorKey, cipherAlgorithm)
    val plainText = if (Des3Cipher.CBC_NO_PADDING == cipherAlgorithm || Des3Cipher.ECB_NO_PADDING == cipherAlgorithm) {
        handleNoPaddingEncryptFormat(cipher, this)
    } else {
        toByteArray()
    }
    // 加密
    val result = cipher.doFinal(plainText)
    // 将加密后的二进制数组转化为十六进制
    result.toHexString()
}.onFailure {
    it.printStackTrace()
}.getOrDefault("")

/**
 * DES 解密
 */
@RequiresApi(Build.VERSION_CODES.O)
fun String.des3DecryptBase64(
    slatKey: String,
    vectorKey: String,
    cipherAlgorithm: Des3Cipher = Des3Cipher.CBC_PKCS5PADDING,
): String {
    val cipher = initCipher(Cipher.DECRYPT_MODE, slatKey, vectorKey, cipherAlgorithm)
    return String(cipher.doFinal(Base64.getDecoder().decode(this)))
}

/**
 * DES 解密
 */
fun String.des3DecryptHex(
    slatKey: String,
    vectorKey: String,
    cipherAlgorithm: Des3Cipher = Des3Cipher.CBC_PKCS5PADDING,
): String {
    val cipher = initCipher(Cipher.DECRYPT_MODE, slatKey, vectorKey, cipherAlgorithm)
    return hexString2Bytes()?.let {
        String(cipher.doFinal(it))
    }.orEmpty()
}

/**
 * @desc：
 * @date：2023/06/20 15:44
 */
enum class Des3Cipher(val transformation: String) {
    /**
     * 有向量加密模式, 不足8位用0补足8位, 需代码给加密内容添加0, 如{65,65,65,0,0,0,0,0}
     */
    CBC_NO_PADDING("DESede/CBC/NoPadding"),

    /**
     * 有向量加密模式, 不足8位用余位数补足8位, 如{65,65,65,5,5,5,5,5}或{97,97,97,97,97,97,2,2}; 刚好8位补8位8
     */
    CBC_PKCS5PADDING("DESede/CBC/PKCS5Padding"),

    /**
     * 无向量加密模式, 不足8位用0补足8位, 需代码给加密内容添加0
     */
    ECB_NO_PADDING("DESede/ECB/NoPadding"),

    /**
     * 无向量加密模式, 不足8位用余位数补足8位
     */
    ECB_PKCS5PADDING("DESede/ECB/PKCS5Padding"),
}

