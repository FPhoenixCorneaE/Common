package com.fphoenixcorneae.common.ext.algorithm

import android.os.Build
import androidx.annotation.RequiresApi
import com.fphoenixcorneae.common.ext.hexString2Bytes
import com.fphoenixcorneae.common.ext.toHexString
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.charset.Charset
import java.security.Key
import java.security.SecureRandom
import java.util.*
import javax.crypto.Cipher
import javax.crypto.CipherInputStream
import javax.crypto.KeyGenerator
import javax.crypto.spec.IvParameterSpec

/**
 * AES对称加密(加/解密必须使用同一个Key):
 * Advanced Encryption Standard，高级数据加密标准，AES算法可以有效抵制针对DES的攻击算法，对称加密算法
 */

/** 算法类型：用于指定生成 AES 的密钥 */
private const val AES_ALGORITHM = "AES"

private const val SECURE_RANDOM_ALGORITHM = "SHA1PRNG"

private const val SLAT_KEY_LENGTH = 128
private const val VECTOR_KEY_LENGTH = 128

/**
 * 获取加密的密匙，传入的slatKey可以是任意长度的，作为SecureRandom的随机种子，
 * 而在KeyGenerator初始化时设置密匙的长度128bit(16位byte)
 * @param size 128 192 256
 */
private fun getSlatKey(slatKey: String, size: Int): Key? = runCatching {
    val keyGen = KeyGenerator.getInstance(AES_ALGORITHM)
    val random = SecureRandom.getInstance(SECURE_RANDOM_ALGORITHM)
    random.setSeed(slatKey.toByteArray())
    keyGen.init(size, random)
    keyGen.generateKey()
}.onFailure {
    it.printStackTrace()
}.getOrNull()

/**
 * 获取加密的向量
 * @param size
 */
private fun getVectorKey(vectorKey: String, size: Int): IvParameterSpec = runCatching {
    val keyGen = KeyGenerator.getInstance(AES_ALGORITHM)
    val random = SecureRandom.getInstance(SECURE_RANDOM_ALGORITHM)
    random.setSeed(vectorKey.toByteArray())
    keyGen.init(size, random)
    IvParameterSpec(keyGen.generateKey().encoded)
}.onFailure {
    it.printStackTrace()
}.getOrThrow()

private fun initCipher(
    mode: Int,
    slatKey: String,
    vectorKey: String,
    cipherAlgorithm: AesCipher,
    slatKeySize: Int,
    vectorKeySize: Int,
): Cipher {
    val cipher = Cipher.getInstance(cipherAlgorithm.transformation)
    if (AesCipher.CBC_NO_PADDING == cipherAlgorithm || AesCipher.CBC_PKCS5PADDING == cipherAlgorithm) {
        cipher.init(mode, getSlatKey(slatKey, slatKeySize), getVectorKey(vectorKey, vectorKeySize))
    } else {
        cipher.init(mode, getSlatKey(slatKey, slatKeySize))
    }
    return cipher
}

/**
 *
 * NoPadding加密模式, 加密内容必须是 8byte的倍数, 不足8位则末位补足0
 *
 * 加密算法不提供该补码方式, 需要代码完成该补码方式
 * @param cipher
 * @param content 加密内容
 * @Param charset 指定的字符集
 * @return 符合加密的内容(byte[])
 */
fun handleNoPaddingEncryptFormat(
    cipher: Cipher,
    content: String,
    charset: Charset = Charset.defaultCharset(),
): ByteArray =
    run {
        val blockSize = cipher.blockSize
        val contentBytes = content.toByteArray(charset)
        var plainTextLength = contentBytes.size
        if (plainTextLength % blockSize != 0) {
            plainTextLength += (blockSize - plainTextLength % blockSize)
        }
        val plainText = ByteArray(plainTextLength)
        System.arraycopy(contentBytes, 0, plainText, 0, contentBytes.size)
        plainText
    }

/**
 * AES 加密
 */
@RequiresApi(Build.VERSION_CODES.O)
fun String.aesEncryptBase64(
    slatKey: String,
    vectorKey: String,
    cipherAlgorithm: AesCipher = AesCipher.CBC_PKCS5PADDING,
    slatKeySize: Int = SLAT_KEY_LENGTH,
    vectorKeySize: Int = VECTOR_KEY_LENGTH,
): String = runCatching {
    val cipher = initCipher(Cipher.ENCRYPT_MODE, slatKey, vectorKey, cipherAlgorithm, slatKeySize, vectorKeySize)
    val plainText = if (AesCipher.CBC_NO_PADDING == cipherAlgorithm || AesCipher.ECB_NO_PADDING == cipherAlgorithm) {
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
 * AES 加密
 */
fun String.aesEncryptHex(
    slatKey: String,
    vectorKey: String,
    cipherAlgorithm: AesCipher = AesCipher.CBC_PKCS5PADDING,
    slatKeySize: Int = SLAT_KEY_LENGTH,
    vectorKeySize: Int = VECTOR_KEY_LENGTH,
): String = runCatching {
    val cipher = initCipher(Cipher.ENCRYPT_MODE, slatKey, vectorKey, cipherAlgorithm, slatKeySize, vectorKeySize)
    val plainText = if (AesCipher.CBC_NO_PADDING == cipherAlgorithm || AesCipher.ECB_NO_PADDING == cipherAlgorithm) {
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
 * AES 解密
 */
@RequiresApi(Build.VERSION_CODES.O)
fun String.aesDecryptBase64(
    slatKey: String,
    vectorKey: String,
    cipherAlgorithm: AesCipher = AesCipher.CBC_PKCS5PADDING,
    slatKeySize: Int = SLAT_KEY_LENGTH,
    vectorKeySize: Int = VECTOR_KEY_LENGTH,
): String {
    val cipher = initCipher(Cipher.DECRYPT_MODE, slatKey, vectorKey, cipherAlgorithm, slatKeySize, vectorKeySize)
    return String(cipher.doFinal(Base64.getDecoder().decode(this)))
}

/**
 * AES 解密
 */
fun String.aesDecryptHex(
    slatKey: String,
    vectorKey: String,
    cipherAlgorithm: AesCipher = AesCipher.CBC_PKCS5PADDING,
    slatKeySize: Int = SLAT_KEY_LENGTH,
    vectorKeySize: Int = VECTOR_KEY_LENGTH,
): String {
    val cipher = initCipher(Cipher.DECRYPT_MODE, slatKey, vectorKey, cipherAlgorithm, slatKeySize, vectorKeySize)
    return hexString2Bytes()?.let {
        String(cipher.doFinal(it))
    }.orEmpty()
}

fun File.aesEncrypt(
    destFilePath: String,
    slatKey: String,
    vectorKey: String,
    cipherAlgorithm: AesCipher = AesCipher.CBC_PKCS5PADDING,
    slatKeySize: Int = SLAT_KEY_LENGTH,
    vectorKeySize: Int = VECTOR_KEY_LENGTH,
): File? =
    handleFile(
        mode = Cipher.ENCRYPT_MODE,
        sourceFilePath = path,
        destFilePath = destFilePath,
        slatKey = slatKey,
        vectorKey = vectorKey,
        cipherAlgorithm = cipherAlgorithm,
        slatKeySize = slatKeySize,
        vectorKeySize = vectorKeySize
    )

fun File.aesDecrypt(
    destFilePath: String,
    slatKey: String,
    vectorKey: String,
    cipherAlgorithm: AesCipher = AesCipher.CBC_PKCS5PADDING,
    slatKeySize: Int = SLAT_KEY_LENGTH,
    vectorKeySize: Int = VECTOR_KEY_LENGTH,
): File? =
    handleFile(
        mode = Cipher.DECRYPT_MODE,
        sourceFilePath = path,
        destFilePath = destFilePath,
        slatKey = slatKey,
        vectorKey = vectorKey,
        cipherAlgorithm = cipherAlgorithm,
        slatKeySize = slatKeySize,
        vectorKeySize = vectorKeySize
    )

private fun handleFile(
    mode: Int,
    sourceFilePath: String,
    destFilePath: String,
    slatKey: String,
    vectorKey: String,
    cipherAlgorithm: AesCipher,
    slatKeySize: Int,
    vectorKeySize: Int,
): File? {
    val sourceFile = File(sourceFilePath)
    val destFile = File(destFilePath)

    if (sourceFile.exists() && sourceFile.isFile) {
        if (destFile.parentFile?.exists() == false) {
            destFile.parentFile?.mkdirs()
        }
        destFile.createNewFile()

        val inputStream = FileInputStream(sourceFile)
        val outputStream = FileOutputStream(destFile)
        val cipher = initCipher(mode, slatKey, vectorKey, cipherAlgorithm, slatKeySize, vectorKeySize)
        val cin = CipherInputStream(inputStream, cipher)

        try {
            val b = ByteArray(1024)
            var read: Int
            do {
                read = cin.read(b)
                if (read > 0) {
                    outputStream.write(b, 0, read)
                }
            } while (read > 0)
            outputStream.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cin.close()
            inputStream.close()
            outputStream.close()
        }
        return destFile
    }
    return null
}

/**
 * @desc：
 * @date：2023/06/20 11:10
 */
enum class AesCipher(val transformation: String) {
    /**
     * 有向量加密模式, 不足8位用0补足8位, 需代码给加密内容添加0, 如{65,65,65,0,0,0,0,0}
     */
    CBC_NO_PADDING("AES/CBC/NoPadding"),

    /**
     * 有向量加密模式, 不足8位用余位数补足8位, 如{65,65,65,5,5,5,5,5}或{97,97,97,97,97,97,2,2}; 刚好8位补8位8
     */
    CBC_PKCS5PADDING("AES/CBC/PKCS5Padding"),

    /**
     * 无向量加密模式, 不足8位用0补足8位, 需代码给加密内容添加0
     */
    ECB_NO_PADDING("AES/ECB/NoPadding"),

    /**
     * 无向量加密模式, 不足8位用余位数补足8位
     */
    ECB_PKCS5PADDING("AES/ECB/PKCS5Padding"),
}

