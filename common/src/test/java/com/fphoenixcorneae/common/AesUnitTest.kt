package com.fphoenixcorneae.common

import com.fphoenixcorneae.common.ext.algorithm.AesCipher
import com.fphoenixcorneae.common.ext.algorithm.aesDecryptBase64
import com.fphoenixcorneae.common.ext.algorithm.aesEncryptBase64
import org.junit.Test

class AesUnitTest {

    @Test
    fun encryptAndDecrypt() {
        val slatKey = "testing5555"
        val vectorKey = "12344321"
        val origin = "1111222333444555"
        val encrypted = origin.aesEncryptBase64(slatKey, vectorKey,AesCipher.ECB_NO_PADDING)
        println("encrypted: $encrypted")
        val decrypted = encrypted.aesDecryptBase64(slatKey, vectorKey,AesCipher.ECB_NO_PADDING)
        println("decrypted: $decrypted")
    }
}