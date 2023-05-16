package com.fphoenixcorneae.common

import com.fphoenixcorneae.common.ext.unZip
import com.fphoenixcorneae.common.ext.zip
import org.junit.Test
import java.io.File

class ZipUnitTest {
    @Test
    fun zipFile() {
        File("F:\\测试压缩方法").zip("F:\\测试压缩方法.zip")
    }

    @Test
    fun unZipFile() {
        File("F:\\测试压缩方法.zip").unZip("F:\\")
    }
}