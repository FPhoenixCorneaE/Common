package com.fphoenixcorneae.common

import com.fphoenixcorneae.common.ext.algorithm.*
import org.junit.Test

class DesUnitTest {

    @Test
    fun encryptAndDecrypt() {
        val slatKey = "testing1"
        val vectorKey = "abcdefg0"
        val origin = "Hello World!"
        val encryptedBase64 = origin.desEncryptBase64(
            slatKey = slatKey,
            vectorKey = vectorKey,
            cipherAlgorithm = DesCipher.CBC_PKCS5PADDING,
        )
        println("Base64 encrypted: $encryptedBase64")
        val decryptedBase64 = encryptedBase64.desDecryptBase64(
            slatKey = slatKey,
            vectorKey = vectorKey,
            cipherAlgorithm = DesCipher.CBC_PKCS5PADDING,
        )
        println("Base64 decrypted: $decryptedBase64")
        val encryptedHex = origin.desEncryptHex(
            slatKey = slatKey,
            vectorKey = vectorKey,
            cipherAlgorithm = DesCipher.CBC_PKCS5PADDING,
        )
        println("Hex encrypted: $encryptedHex")
        val decryptedHex = encryptedHex.desDecryptHex(
            slatKey = slatKey,
            vectorKey = vectorKey,
            cipherAlgorithm = DesCipher.CBC_PKCS5PADDING,
        )
        println("Hex decrypted: $decryptedHex")
    }
}