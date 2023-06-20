package com.fphoenixcorneae.common

import com.fphoenixcorneae.common.ext.algorithm.*
import org.junit.Test

class RsaUnitTest {

    @Test
    fun encryptAndDecrypt() {
        val slatKey = "testing1"
        val origin = "Hello World!"
        val encryptedBase64 = origin.rsaEncryptBase64(
            slatKey = slatKey,
            cipherAlgorithm = RsaCipher.ECB_PKCS1PADDING,
        )
        println("Base64 encrypted: $encryptedBase64")
        val decryptedBase64 = encryptedBase64.rsaDecryptBase64(
            slatKey = slatKey,
            cipherAlgorithm = RsaCipher.ECB_PKCS1PADDING,
        )
        println("Base64 decrypted: $decryptedBase64")
        val encryptedHex = origin.rsaEncryptHex(
            slatKey = slatKey,
            cipherAlgorithm = RsaCipher.ECB_PKCS1PADDING,
        )
        println("Hex encrypted: $encryptedHex")
        val decryptedHex = encryptedHex.rsaDecryptHex(
            slatKey = slatKey,
            cipherAlgorithm = RsaCipher.ECB_PKCS1PADDING,
        )
        println("Hex decrypted: $decryptedHex")
    }
}