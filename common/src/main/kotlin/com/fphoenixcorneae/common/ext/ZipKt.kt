package com.fphoenixcorneae.common.ext

import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream


/**
 * 压缩文件
 * @param zipFilePath 被压缩后存放的路径
 */
fun File.zip(zipFilePath: String): File {
    var zos: ZipOutputStream? = null
    try {
        zos = ZipOutputStream(BufferedOutputStream(FileOutputStream(zipFilePath)))
        recursionZip(zos, name)
        zos.flush()
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        try {
            zos?.closeEntry()
            zos?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    return File(zipFilePath)
}

/**
 * 递归压缩方法
 * @param zos  zip输出流
 * @param name 压缩后的名称
 */
private fun File.recursionZip(
    zos: ZipOutputStream,
    name: String,
) {
    runCatching {
        val buf = ByteArray(1024)
        if (isFile) {
            zos.putNextEntry(ZipEntry(name))
            var len: Int
            val `in` = FileInputStream(this)
            while (`in`.read(buf).also { len = it } != -1) {
                zos.write(buf, 0, len)
            }
            // Complete the entry
            zos.closeEntry()
            `in`.close()
        } else {
            val listFiles = listFiles()
            if (listFiles == null || listFiles.isEmpty()) {
                zos.putNextEntry(ZipEntry("$name/"))
                zos.closeEntry()
            } else {
                for (file in listFiles) {
                    file.recursionZip(zos, name + "/" + file.name)
                }
            }
        }
    }.onFailure {
        it.printStackTrace()
    }
}

/**
 * 解压文件
 * @param unZipPath 解压路径
 */
fun File.unZip(unZipPath: String) {
    var bos: BufferedOutputStream? = null
    var zis: ZipInputStream? = null
    try {
        if (isDirectory) {
            throw RuntimeException("解压文件不合法!")
        }
        var unZipDir = unZipPath
        if (!unZipDir.endsWith(File.separator)) {
            unZipDir += File.separator
        }
        var filename: String
        val fis = FileInputStream(this)
        zis = ZipInputStream(BufferedInputStream(fis))
        var ze: ZipEntry? = null
        val buffer = ByteArray(1024)
        var count: Int
        while (zis.nextEntry?.also { ze = it } != null) {
            filename = ze!!.name
            if (ze!!.isDirectory) {
                createSubFolders(filename, unZipDir)
                continue
            }
            val fmd = File(unZipDir + filename)
            if (!fmd.exists()) {
                while (fmd.parentFile != null && !fmd.parentFile!!.exists()) {
                    fmd.parentFile?.mkdirs()
                }
                fmd.createNewFile()
            }
            val fos = FileOutputStream(fmd)
            bos = BufferedOutputStream(fos)
            while (zis.read(buffer).also { count = it } != -1) {
                bos.write(buffer, 0, count)
            }
            bos.flush()
            bos.close()
            fos.close()
        }
        fis.close()
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        try {
            zis?.closeEntry()
            zis?.close()
            bos?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}

private fun createSubFolders(filename: String, path: String) {
    val subFolders = filename.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    var pathNow = path
    for (element in subFolders) {
        pathNow = "$pathNow/$element/"
        val fmd = File(pathNow)
        if (fmd.exists()) {
            continue
        }
        fmd.mkdirs()
    }
}