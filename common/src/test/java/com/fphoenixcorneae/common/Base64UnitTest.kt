package com.fphoenixcorneae.common

import com.fphoenixcorneae.common.ext.algorithm.decodeBase64
import com.fphoenixcorneae.common.ext.algorithm.encodeBase64
import org.junit.Test

class Base64UnitTest {

    @Test
    fun base64EncodeDecode() {
        print("//==============================base64EncodeDecode=======================================//")
        println()
        print("1234567890".encodeBase64())
        println()
        print("MTIzNDU2Nzg5MA==".decodeBase64())
        println()
        print("//==============================base64EncodeDecode=======================================//")
        println()
    }
}