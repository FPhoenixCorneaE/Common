package com.fphoenixcorneae.common.ext.algorithm

import android.os.Build
import androidx.annotation.RequiresApi
import com.fphoenixcorneae.common.ext.hexString2Bytes
import com.fphoenixcorneae.common.ext.toHexString
import java.security.*
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.crypto.Cipher

/**
 * <p>RSA加密工具类，使用公匙加密，私匙解密</p>
 * <p>该工具类使用slatKey作为SecureRandom的随机种子来创建公匙和私匙，此情况下只需记录下slatKey即可</p>
 * <p>若需要使用 new SecureRandom() 来创建公匙和私匙，则创建公匙和私匙时，需要记录下公匙和私匙</p>
 * <p>RSA加密算法，一般有：1)使用公匙加密，需要使用私匙解密；2)使用私匙加密，需要使用公匙解密</p>
 */

/** 算法类型：用于指定生成 RSA 的密钥 */
private const val RSA_ALGORITHM = "RSA"

/** 加密秘钥长度为1024/2048，2048加密比1024强 */
private const val SLAT_KEY_LENGTH = 1024
private const val SECURE_RANDOM_ALGORITHM = "SHA1PRNG"

/**
 * 根据slatKey获取公匙，传入的slatKey作为SecureRandom的随机种子
 * 若使用new SecureRandom()创建公匙，则需要记录下私匙，解密时使用
 */
private fun getPublicKey(slatKey: String): ByteArray? = runCatching {
    val keyPairGenerator = KeyPairGenerator.getInstance(RSA_ALGORITHM)
    val random = SecureRandom.getInstance(SECURE_RANDOM_ALGORITHM)
    random.setSeed(slatKey.toByteArray())
    keyPairGenerator.initialize(SLAT_KEY_LENGTH, random)
    val keyPair = keyPairGenerator.generateKeyPair()
    keyPair.public.encoded
}.onFailure {
    it.printStackTrace()
}.getOrNull()

/**
 * 根据slatKey获取私匙，传入的slatKey作为SecureRandom的随机种子
 */
private fun getPrivateKey(slatKey: String): ByteArray? = runCatching {
    val keyPairGenerator = KeyPairGenerator.getInstance(RSA_ALGORITHM)
    val random = SecureRandom.getInstance(SECURE_RANDOM_ALGORITHM)
    random.setSeed(slatKey.toByteArray())
    keyPairGenerator.initialize(SLAT_KEY_LENGTH, random)
    val keyPair = keyPairGenerator.generateKeyPair()
    keyPair.private.encoded
}.onFailure {
    it.printStackTrace()
}.getOrNull()

private fun initCipher(
    mode: Int,
    key: Key,
    cipherAlgorithm: RsaCipher,
): Cipher {
    val cipher = Cipher.getInstance(cipherAlgorithm.transformation)
    cipher.init(mode, key)
    return cipher
}

/**
 * RSA 加密
 */
@RequiresApi(Build.VERSION_CODES.O)
fun String.rsaEncryptBase64(
    slatKey: String,
    cipherAlgorithm: RsaCipher = RsaCipher.ECB_PKCS1PADDING,
): String = runCatching {
    val decoded = getPublicKey(slatKey)
    val publicKey = KeyFactory.getInstance(RSA_ALGORITHM).generatePublic(X509EncodedKeySpec(decoded)) as RSAPublicKey
    val cipher = initCipher(Cipher.ENCRYPT_MODE, publicKey, cipherAlgorithm)
    // 加密
    val result = cipher.doFinal(toByteArray())
    // 使用BASE64对加密后的二进制数组进行编码
    Base64.getEncoder().encodeToString(result)
}.onFailure {
    it.printStackTrace()
}.getOrDefault("")

/**
 * RSA 加密
 */
fun String.rsaEncryptHex(
    slatKey: String,
    cipherAlgorithm: RsaCipher = RsaCipher.ECB_PKCS1PADDING,
): String = runCatching {
    val decoded = getPublicKey(slatKey)
    val publicKey = KeyFactory.getInstance(RSA_ALGORITHM).generatePublic(X509EncodedKeySpec(decoded)) as RSAPublicKey
    val cipher = initCipher(Cipher.ENCRYPT_MODE, publicKey, cipherAlgorithm)
    // 加密
    val result = cipher.doFinal(toByteArray())
    // 将加密后的二进制数组转化为十六进制
    result.toHexString()
}.onFailure {
    it.printStackTrace()
}.getOrDefault("")

/**
 * RSA 解密
 */
@RequiresApi(Build.VERSION_CODES.O)
fun String.rsaDecryptBase64(
    slatKey: String,
    cipherAlgorithm: RsaCipher = RsaCipher.ECB_PKCS1PADDING,
): String {
    val decoded = getPrivateKey(slatKey)
    val privateKey = KeyFactory.getInstance(RSA_ALGORITHM).generatePrivate(PKCS8EncodedKeySpec(decoded)) as RSAPrivateKey
    val cipher = initCipher(Cipher.DECRYPT_MODE, privateKey, cipherAlgorithm)
    return String(cipher.doFinal(Base64.getDecoder().decode(this)))
}

/**
 * RSA 解密
 */
fun String.rsaDecryptHex(
    slatKey: String,
    cipherAlgorithm: RsaCipher = RsaCipher.ECB_PKCS1PADDING,
): String {
    val decoded = getPrivateKey(slatKey)
    val privateKey = KeyFactory.getInstance(RSA_ALGORITHM).generatePrivate(PKCS8EncodedKeySpec(decoded)) as RSAPrivateKey
    val cipher = initCipher(Cipher.DECRYPT_MODE, privateKey, cipherAlgorithm)
    return hexString2Bytes()?.let {
        String(cipher.doFinal(it))
    }.orEmpty()
}

enum class RsaCipher(val transformation: String) {
    /**
     * 无向量加密模式, PKCS1Padding模式填充
     */
    ECB_PKCS1PADDING("RSA/ECB/PKCS1Padding"),

    /**
     * 无向量加密模式, SHA-1摘要 + MGF1方式填充
     */
    ECB_OAEP_WITH_SHA1_AND_MGF_1PADDING("RSA/ECB/OAEPWithSHA-1AndMGF1Padding"),

    /**
     * 无向量加密模式, SHA-256摘要 + MGF1方式填充
     */
    ECB_OAEP_WITH_SHA256_AND_MGF_1PADDING("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
}

