package com.fphoenixcorneae.common

import com.fphoenixcorneae.common.ext.algorithm.*
import org.junit.Test

class Des3UnitTest {

    @Test
    fun encryptAndDecrypt() {
        val slatKey = "testing1testing1testing1"
        val vectorKey = "abcdefg0"
        val origin = "Hello World!"
        val encryptedBase64 = origin.des3EncryptBase64(
            slatKey = slatKey,
            vectorKey = vectorKey,
            cipherAlgorithm = Des3Cipher.CBC_PKCS5PADDING,
        )
        println("Base64 encrypted: $encryptedBase64")
        val decryptedBase64 = encryptedBase64.des3DecryptBase64(
            slatKey = slatKey,
            vectorKey = vectorKey,
            cipherAlgorithm = Des3Cipher.CBC_PKCS5PADDING,
        )
        println("Base64 decrypted: $decryptedBase64")
        val encryptedHex = origin.des3EncryptHex(
            slatKey = slatKey,
            vectorKey = vectorKey,
            cipherAlgorithm = Des3Cipher.CBC_PKCS5PADDING,
        )
        println("Hex encrypted: $encryptedHex")
        val decryptedHex = encryptedHex.des3DecryptHex(
            slatKey = slatKey,
            vectorKey = vectorKey,
            cipherAlgorithm = Des3Cipher.CBC_PKCS5PADDING,
        )
        println("Hex decrypted: $decryptedHex")
    }
}