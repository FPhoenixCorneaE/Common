package com.fphoenixcorneae.common

import com.fphoenixcorneae.common.ext.algorithm.*
import org.junit.Test

class AesUnitTest {

    @Test
    fun encryptAndDecrypt() {
        val slatKey = "testing1234"
        val vectorKey = "abcdefg0000"
        val origin = "Hello World!"
        val encryptedBase64 = origin.aesEncryptBase64(
            slatKey = slatKey,
            vectorKey = vectorKey,
            cipherAlgorithm = AesCipher.CBC_PKCS5PADDING,
        )
        println("Base64 encrypted: $encryptedBase64")
        val decryptedBase64 = encryptedBase64.aesDecryptBase64(
            slatKey = slatKey,
            vectorKey = vectorKey,
            cipherAlgorithm = AesCipher.CBC_PKCS5PADDING,
        )
        println("Base64 decrypted: $decryptedBase64")
        val encryptedHex = origin.aesEncryptHex(
            slatKey = slatKey,
            vectorKey = vectorKey,
            cipherAlgorithm = AesCipher.CBC_PKCS5PADDING,
        )
        println("Hex encrypted: $encryptedHex")
        val decryptedHex = encryptedHex.aesDecryptHex(
            slatKey = slatKey,
            vectorKey = vectorKey,
            cipherAlgorithm = AesCipher.CBC_PKCS5PADDING,
        )
        println("Hex decrypted: $decryptedHex")
    }
}