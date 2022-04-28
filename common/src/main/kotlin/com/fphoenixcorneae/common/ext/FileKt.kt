package com.fphoenixcorneae.common.ext

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.StatFs
import android.text.TextUtils
import java.io.*
import java.net.URL
import java.nio.charset.Charset
import java.security.DigestInputStream
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import javax.net.ssl.HttpsURLConnection
import kotlin.experimental.and

val LINE_SEPARATOR: String? = System.getProperty("line.separator")
val FILE_SEPARATOR: String? = System.getProperty("file.separator")

/**
 * 根据文件路径获取文件
 *
 * @param filePath 文件路径
 * @return 文件
 */
fun getFileByPath(filePath: String?): File? {
    return if (filePath.isSpace()) {
        null
    } else {
        File(filePath)
    }
}

/**
 * 判断文件是否存在
 *
 * @param filePath 文件路径
 * @return `true`: 存在<br></br>`false`: 不存在
 */
fun isFileExists(filePath: String?): Boolean {
    return isFileExists(getFileByPath(filePath))
}

/**
 * 判断文件是否存在
 *
 * @param file 文件
 * @return `true`: 存在<br></br>`false`: 不存在
 */
fun isFileExists(file: File?): Boolean {
    return file != null && (file.exists() || isFileExistsApi29(file.absolutePath))
}

private fun isFileExistsApi29(filePath: String): Boolean {
    if (Build.VERSION.SDK_INT >= 29) {
        try {
            val uri = Uri.parse(filePath)
            val cr: ContentResolver = applicationContext.contentResolver
            val afd = cr.openAssetFileDescriptor(uri, "r") ?: return false
            try {
                afd.close()
            } catch (ignore: IOException) {
            }
        } catch (e: FileNotFoundException) {
            return false
        }
        return true
    }
    return false
}

/**
 * 重命名文件
 *
 * @param filePath 文件路径
 * @param newName  新名称
 * @return `true`: 重命名成功<br></br>`false`: 重命名失败
 */
fun rename(filePath: String, newName: String): Boolean {
    return rename(getFileByPath(filePath), newName)
}

/**
 * 重命名文件
 *
 * @param file    文件
 * @param newName 新名称
 * @return `true`: 重命名成功<br></br>`false`: 重命名失败
 */
fun rename(file: File?, newName: String): Boolean {
    // 文件为空返回false
    if (file == null) {
        return false
    }
    // 文件不存在返回false
    if (!file.exists()) {
        return false
    }
    // 新的文件名为空返回false
    if (newName.isSpace()) {
        return false
    }
    // 如果文件名没有改变返回true
    if (newName == file.name) {
        return true
    }
    val newFile = File(file.parent + File.separator + newName)
    // 如果重命名的文件已存在返回false
    return !newFile.exists() && file.renameTo(newFile)
}

/**
 * 判断是否是目录
 *
 * @param dirPath 目录路径
 * @return `true`: 是<br></br>`false`: 否
 */
fun isDir(dirPath: String): Boolean {
    return isDir(getFileByPath(dirPath))
}

/**
 * 判断是否是目录
 *
 * @param file 文件
 * @return `true`: 是<br></br>`false`: 否
 */
fun isDir(file: File?): Boolean {
    return isFileExists(file) && file!!.isDirectory
}

/**
 * 判断是否是文件
 *
 * @param filePath 文件路径
 * @return `true`: 是<br></br>`false`: 否
 */
fun isFile(filePath: String): Boolean {
    return isFile(getFileByPath(filePath))
}

/**
 * 判断是否是文件
 *
 * @param file 文件
 * @return `true`: 是<br></br>`false`: 否
 */
fun isFile(file: File?): Boolean {
    return isFileExists(file) && file!!.isFile
}

/**
 * 判断目录是否存在，不存在则判断是否创建成功
 *
 * @param dirPath 目录路径
 * @return `true`: 存在或创建成功<br></br>`false`: 不存在或创建失败
 */
fun createOrExistsDir(dirPath: String): Boolean {
    return createOrExistsDir(getFileByPath(dirPath))
}

/**
 * 判断目录是否存在，不存在则判断是否创建成功
 *
 * @param file 文件
 * @return `true`: 存在或创建成功<br></br>`false`: 不存在或创建失败
 */
fun createOrExistsDir(file: File?): Boolean {
    // 如果存在，是目录则返回true，是文件则返回false，不存在则返回是否创建成功
    return file != null && if (file.exists()) file.isDirectory else file.mkdirs()
}

/**
 * 判断文件是否存在，不存在则判断是否创建成功
 *
 * @param filePath 文件路径
 * @return `true`: 存在或创建成功<br></br>`false`: 不存在或创建失败
 */
fun createOrExistsFile(filePath: String): Boolean {
    return createOrExistsFile(getFileByPath(filePath))
}

/**
 * 判断文件是否存在，不存在则判断是否创建成功
 *
 * @param file 文件
 * @return `true`: 存在或创建成功<br></br>`false`: 不存在或创建失败
 */
fun createOrExistsFile(file: File?): Boolean {
    if (file == null) {
        return false
    }
    // 如果存在，是文件则返回true，是目录则返回false
    if (file.exists()) {
        return file.isFile
    }
    if (!createOrExistsDir(file.parentFile)) {
        return false
    }
    return try {
        file.createNewFile()
    } catch (e: IOException) {
        e.printStackTrace()
        false
    }
}

/**
 * 判断文件是否存在，存在则在创建之前删除
 *
 * @param filePath 文件路径
 * @return `true`: 创建成功<br></br>`false`: 创建失败
 */
fun createFileByDeleteOldFile(filePath: String): Boolean {
    return createFileByDeleteOldFile(getFileByPath(filePath))
}

/**
 * 判断文件是否存在，存在则在创建之前删除
 *
 * @param file 文件
 * @return `true`: 创建成功<br></br>`false`: 创建失败
 */
fun createFileByDeleteOldFile(file: File?): Boolean {
    if (file == null) {
        return false
    }
    try {
        // 文件存在并且删除失败返回false
        if (file.exists() && file.isFile && !file.delete()) {
            return false
        }
        // 创建目录失败返回false
        return if (!createOrExistsDir(file.parentFile)) {
            false
        } else {
            file.createNewFile()
        }
    } catch (e: Exception) {
        e.printStackTrace()
        return false
    }

}

/**
 * Copy the directory or file.
 *
 * @param srcPath  The path of source.
 * @param destPath The path of destination.
 * @return `true`: success<br></br>`false`: fail
 */
fun copy(
    srcPath: String?,
    destPath: String?,
): Boolean {
    return copy(
        getFileByPath(srcPath),
        getFileByPath(destPath),
    )
}

/**
 * Copy the directory or file.
 *
 * @param src      The source.
 * @param dest     The destination.
 * @return `true`: success<br></br>`false`: fail
 */
fun copy(
    src: File?,
    dest: File?,
): Boolean {
    if (src == null) {
        return false
    }
    return if (src.isDirectory) {
        copyDir(src, dest)
    } else {
        copyFile(src, dest)
    }
}

/**
 * 复制或移动目录
 *
 * @param srcDirPath  源目录路径
 * @param destDirPath 目标目录路径
 * @param isMove      是否移动
 * @return `true`: 复制或移动成功<br></br>`false`: 复制或移动失败
 */
private fun copyOrMoveDir(
    srcDirPath: String,
    destDirPath: String,
    isMove: Boolean
): Boolean {
    return copyOrMoveDir(getFileByPath(srcDirPath), getFileByPath(destDirPath), isMove)
}

/**
 * 复制或移动目录
 *
 * @param srcDir  源目录
 * @param destDir 目标目录
 * @param isMove  是否移动
 * @return `true`: 复制或移动成功<br></br>`false`: 复制或移动失败
 */
private fun copyOrMoveDir(srcDir: File?, destDir: File?, isMove: Boolean): Boolean {
    if (srcDir == null || destDir == null) {
        return false
    }
    // 如果目标目录在源目录中则返回false，看不懂的话好好想想递归怎么结束
    // srcPath : F:\\MyGithub\\AndroidUtilCode\\utilcode\\src\\test\\res
    // destPath: F:\\MyGithub\\AndroidUtilCode\\utilcode\\src\\test\\res1
    // 为防止以上这种情况出现出现误判，须分别在后面加个路径分隔符
    val srcPath = srcDir.path + File.separator
    val destPath = destDir.path + File.separator
    if (destPath.contains(srcPath)) {
        return false
    }
    // 源文件不存在或者不是目录则返回false
    if (!srcDir.exists() || !srcDir.isDirectory) {
        return false
    }
    // 目标目录不存在返回false
    if (!createOrExistsDir(destDir)) {
        return false
    }
    val files = srcDir.listFiles()
    for (file in files!!) {
        val oneDestFile = File(destPath + file.name)
        if (file.isFile) {
            // 如果操作失败返回false
            if (!copyOrMoveFile(file, oneDestFile, isMove)) {
                return false
            }
        } else if (file.isDirectory) {
            // 如果操作失败返回false
            if (!copyOrMoveDir(file, oneDestFile, isMove)) {
                return false
            }
        }
    }
    return !isMove || deleteDir(srcDir)
}

/**
 * 复制或移动文件
 *
 * @param srcFilePath  源文件路径
 * @param destFilePath 目标文件路径
 * @param isMove       是否移动
 * @return `true`: 复制或移动成功<br></br>`false`: 复制或移动失败
 */
private fun copyOrMoveFile(
    srcFilePath: String,
    destFilePath: String,
    isMove: Boolean
): Boolean {
    return copyOrMoveFile(getFileByPath(srcFilePath), getFileByPath(destFilePath), isMove)
}

/**
 * 复制或移动文件
 *
 * @param srcFile  源文件
 * @param destFile 目标文件
 * @param isMove   是否移动
 * @return `true`: 复制或移动成功<br></br>`false`: 复制或移动失败
 */
private fun copyOrMoveFile(srcFile: File?, destFile: File?, isMove: Boolean): Boolean {
    if (srcFile == null || destFile == null) {
        return false
    }
    // 源文件不存在或者不是文件则返回false
    if (!srcFile.exists() || !srcFile.isFile) {
        return false
    }
    // 目标文件存在且是文件则返回false
    if (destFile.exists() && destFile.isFile) {
        return false
    }
    // 目标目录不存在返回false
    if (!createOrExistsDir(destFile.parentFile)) {
        return false
    }
    return try {
        writeFileFromIS(
            destFile,
            FileInputStream(srcFile),
            false
        ) && !(isMove && !deleteFile(srcFile))
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
        false
    }

}

/**
 * 复制目录
 *
 * @param srcDirPath  源目录路径
 * @param destDirPath 目标目录路径
 * @return `true`: 复制成功<br></br>`false`: 复制失败
 */
fun copyDir(srcDirPath: String, destDirPath: String): Boolean {
    return copyDir(getFileByPath(srcDirPath), getFileByPath(destDirPath))
}

/**
 * 复制目录
 *
 * @param srcDir  源目录
 * @param destDir 目标目录
 * @return `true`: 复制成功<br></br>`false`: 复制失败
 */
fun copyDir(srcDir: File?, destDir: File?): Boolean {
    return copyOrMoveDir(srcDir, destDir, false)
}

/**
 * 复制文件
 *
 * @param srcFilePath  源文件路径
 * @param destFilePath 目标文件路径
 * @return `true`: 复制成功<br></br>`false`: 复制失败
 */
fun copyFile(srcFilePath: String, destFilePath: String): Boolean {
    return copyFile(getFileByPath(srcFilePath), getFileByPath(destFilePath))
}

/**
 * 复制文件
 *
 * @param srcFile  源文件
 * @param destFile 目标文件
 * @return `true`: 复制成功<br></br>`false`: 复制失败
 */
fun copyFile(srcFile: File?, destFile: File?): Boolean {
    return copyOrMoveFile(srcFile, destFile, false)
}

/**
 * Move the directory or file.
 *
 * @param src      The source.
 * @param dest     The destination.
 * @return `true`: success<br></br>`false`: fail
 */
fun move(
    src: File?,
    dest: File?,
): Boolean {
    if (src == null) {
        return false
    }
    return if (src.isDirectory) {
        moveDir(src, dest)
    } else {
        moveFile(src, dest)
    }
}

/**
 * 移动目录
 *
 * @param srcDirPath  源目录路径
 * @param destDirPath 目标目录路径
 * @return `true`: 移动成功<br></br>`false`: 移动失败
 */
fun moveDir(srcDirPath: String, destDirPath: String): Boolean {
    return moveDir(getFileByPath(srcDirPath), getFileByPath(destDirPath))
}

/**
 * 移动目录
 *
 * @param srcDir  源目录
 * @param destDir 目标目录
 * @return `true`: 移动成功<br></br>`false`: 移动失败
 */
fun moveDir(srcDir: File?, destDir: File?): Boolean {
    return copyOrMoveDir(srcDir, destDir, true)
}

/**
 * 移动文件
 *
 * @param srcFilePath  源文件路径
 * @param destFilePath 目标文件路径
 * @return `true`: 移动成功<br></br>`false`: 移动失败
 */
fun moveFile(srcFilePath: String, destFilePath: String): Boolean {
    return moveFile(getFileByPath(srcFilePath), getFileByPath(destFilePath))
}

/**
 * 移动文件
 *
 * @param srcFile  源文件
 * @param destFile 目标文件
 * @return `true`: 移动成功<br></br>`false`: 移动失败
 */
fun moveFile(srcFile: File?, destFile: File?): Boolean {
    return copyOrMoveFile(srcFile, destFile, true)
}

/**
 * Delete the directory.
 *
 * @param filePath The path of file.
 * @return `true`: success<br></br>`false`: fail
 */
fun delete(filePath: String?): Boolean {
    return delete(getFileByPath(filePath))
}

/**
 * Delete the directory.
 *
 * @param file The file.
 * @return `true`: success
 *         `false`: fail
 */
fun delete(file: File?): Boolean {
    if (file == null) return false
    return if (file.isDirectory) {
        deleteDir(file)
    } else {
        deleteFile(file)
    }
}

/**
 * 删除目录
 *
 * @param dirPath 目录路径
 * @return `true`: 删除成功<br></br>`false`: 删除失败
 */
fun deleteDir(dirPath: String): Boolean {
    return deleteDir(getFileByPath(dirPath))
}

/**
 * 删除目录
 *
 * @param dir 目录
 * @return `true`: 删除成功<br></br>`false`: 删除失败
 */
fun deleteDir(dir: File?): Boolean {
    if (dir == null) {
        return false
    }
    // 目录不存在返回true
    if (!dir.exists()) {
        return true
    }
    // 不是目录返回false
    if (!dir.isDirectory) {
        return false
    }
    // 现在文件存在且是文件夹
    val files = dir.listFiles()
    if (files != null && files.isNotEmpty()) {
        for (file in files) {
            if (file.isFile) {
                if (!deleteFile(file)) {
                    return false
                }
            } else if (file.isDirectory) {
                if (!deleteDir(file)) {
                    return false
                }
            }
        }
    }
    return dir.delete()
}

/**
 * 删除文件
 *
 * @param srcFilePath 文件路径
 * @return `true`: 删除成功<br></br>`false`: 删除失败
 */
fun deleteFile(srcFilePath: String): Boolean {
    return deleteFile(getFileByPath(srcFilePath))
}

/**
 * 删除文件
 *
 * @param file 文件
 * @return `true`: 删除成功<br></br>`false`: 删除失败
 */
fun deleteFile(file: File?): Boolean {
    return file != null && (!file.exists() || file.isFile && file.delete())
}

/**
 * Delete the all in directory.
 *
 * @param dirPath The path of directory.
 * @return `true`: success<br></br>`false`: fail
 */
fun deleteAllInDir(dirPath: String?): Boolean {
    return deleteAllInDir(getFileByPath(dirPath))
}

/**
 * Delete the all in directory.
 *
 * @param dir The directory.
 * @return `true`: success<br></br>`false`: fail
 */
fun deleteAllInDir(dir: File?): Boolean {
    return deleteFilesInDirWithFilter(dir) { true }
}

/**
 * 删除目录下的所有文件
 *
 * @param dirPath 目录路径
 * @return `true`: 删除成功
 *         `false`: 删除失败
 */
fun deleteFilesInDir(dirPath: String): Boolean {
    return deleteFilesInDir(getFileByPath(dirPath))
}

/**
 * 删除目录下的所有文件
 *
 * @param dir 目录
 * @return `true`: 删除成功<br></br>`false`: 删除失败
 */
fun deleteFilesInDir(dir: File?): Boolean {
    return deleteFilesInDirWithFilter(dir, FileFilter {
        return@FileFilter it.isFile
    })
}

/**
 * Delete all files that satisfy the filter in directory.
 *
 * @param dirPath The path of directory.
 * @param filter  The filter.
 * @return `true`: success
 *         `false`: fail
 */
fun deleteFilesInDirWithFilter(
    dirPath: String?,
    filter: FileFilter?
): Boolean {
    return deleteFilesInDirWithFilter(getFileByPath(dirPath), filter)
}

/**
 * Delete all files that satisfy the filter in directory.
 *
 * @param dir    The directory.
 * @param filter The filter.
 * @return `true`: success
 *         `false`: fail
 */
fun deleteFilesInDirWithFilter(dir: File?, filter: FileFilter?): Boolean {
    if (dir == null || filter == null) {
        return false
    }
    // dir doesn't exist then return true
    if (!dir.exists()) {
        return true
    }
    // dir isn't a directory then return false
    if (!dir.isDirectory) {
        return false
    }
    val files = dir.listFiles()
    if (files != null && files.isNotEmpty()) {
        for (file in files) {
            if (filter.accept(file)) {
                if (file.isFile) {
                    if (!file.delete()) {
                        return false
                    }
                } else if (file.isDirectory) {
                    if (!deleteDir(file)) {
                        return false
                    }
                }
            }
        }
    }
    return true
}

/**
 * 获取目录下所有文件
 *
 * @param dirPath     目录路径
 * @param isRecursive 是否递归进子目录
 * @return 文件链表
 */
fun listFilesInDir(dirPath: String, isRecursive: Boolean): List<File>? {
    return listFilesInDir(getFileByPath(dirPath), isRecursive)
}

/**
 * 获取目录下所有文件
 *
 * @param dir         目录
 * @param isRecursive 是否递归进子目录
 * @return 文件链表
 */
fun listFilesInDir(dir: File?, isRecursive: Boolean): List<File>? {
    if (!isDir(dir)) {
        return null
    }
    if (isRecursive) {
        return listFilesInDir(dir)
    }
    val list = ArrayList<File>()
    val files = dir!!.listFiles()
    if (files != null && files.isNotEmpty()) {
        Collections.addAll(list, *files)
    }
    return list
}

/**
 * 获取目录下所有文件包括子目录
 *
 * @param dirPath 目录路径
 * @return 文件链表
 */
fun listFilesInDir(dirPath: String): List<File>? {
    return listFilesInDir(getFileByPath(dirPath))
}

/**
 * 获取目录下所有文件包括子目录
 *
 * @param dir 目录
 * @return 文件链表{
 * if
 */
fun listFilesInDir(dir: File?): List<File>? {
    if (!isDir(dir)) {
        return null
    }
    val list = ArrayList<File>()
    val files = dir!!.listFiles()
    if (files != null && files.isNotEmpty()) {
        for (file in files) {
            list.add(file)
            if (file.isDirectory) {
                val fileList = listFilesInDir(file)
                if (fileList != null) {
                    list.addAll(fileList)
                }
            }
        }
    }
    return list
}

/**
 * 获取目录下所有后缀名为suffix的文件
 *
 * 大小写忽略
 *
 * @param dirPath     目录路径
 * @param suffix      后缀名
 * @param isRecursive 是否递归进子目录
 * @return 文件链表
 */
fun listFilesInDirWithFilter(
    dirPath: String,
    suffix: String,
    isRecursive: Boolean
): List<File>? {
    return listFilesInDirWithFilter(getFileByPath(dirPath), suffix, isRecursive)
}

/**
 * 获取目录下所有后缀名为suffix的文件
 *
 * 大小写忽略
 *
 * @param dir         目录
 * @param suffix      后缀名
 * @param isRecursive 是否递归进子目录
 * @return 文件链表
 */
fun listFilesInDirWithFilter(
    dir: File?,
    suffix: String,
    isRecursive: Boolean
): List<File>? {
    if (isRecursive) {
        return listFilesInDirWithFilter(dir, suffix)
    }
    if (dir == null || !isDir(dir)) {
        return null
    }
    val list = ArrayList<File>()
    val files = dir.listFiles()
    if (files != null && files.isNotEmpty()) {
        for (file in files) {
            if (file.name.uppercase(Locale.getDefault()).endsWith(suffix.uppercase(Locale.getDefault()))) {
                list.add(file)
            }
        }
    }
    return list
}

/**
 * 获取目录下所有后缀名为suffix的文件包括子目录
 *
 * 大小写忽略
 *
 * @param dirPath 目录路径
 * @param suffix  后缀名
 * @return 文件链表
 */
fun listFilesInDirWithFilter(dirPath: String, suffix: String): List<File>? {
    return listFilesInDirWithFilter(getFileByPath(dirPath), suffix)
}

/**
 * 获取目录下所有后缀名为suffix的文件包括子目录
 *
 * 大小写忽略
 *
 * @param dir    目录
 * @param suffix 后缀名
 * @return 文件链表
 */
fun listFilesInDirWithFilter(dir: File?, suffix: String): List<File>? {
    if (dir == null || !isDir(dir)) {
        return null
    }
    val list = ArrayList<File>()
    val files = dir.listFiles()
    if (files != null && files.isNotEmpty()) {
        for (file in files) {
            if (file.name.uppercase(Locale.getDefault()).endsWith(suffix.uppercase(Locale.getDefault()))) {
                list.add(file)
            }
            if (file.isDirectory) {
                list.addAll(listFilesInDirWithFilter(file, suffix)!!)
            }
        }
    }
    return list
}

/**
 * 获取目录下所有符合filter的文件
 *
 * @param dirPath     目录路径
 * @param filter      过滤器
 * @param isRecursive 是否递归进子目录
 * @return 文件链表
 */
fun listFilesInDirWithFilter(
    dirPath: String,
    filter: FilenameFilter,
    isRecursive: Boolean
): List<File>? {
    return listFilesInDirWithFilter(getFileByPath(dirPath), filter, isRecursive)
}

/**
 * 获取目录下所有符合filter的文件
 *
 * @param dir         目录
 * @param filter      过滤器
 * @param isRecursive 是否递归进子目录
 * @return 文件链表
 */
fun listFilesInDirWithFilter(
    dir: File?,
    filter: FilenameFilter,
    isRecursive: Boolean
): List<File>? {
    if (isRecursive) {
        return listFilesInDirWithFilter(dir, filter)
    }
    if (dir == null || !isDir(dir)) {
        return null
    }
    val list = ArrayList<File>()
    val files = dir.listFiles()
    if (files != null && files.isNotEmpty()) {
        for (file in files) {
            if (filter.accept(file.parentFile, file.name)) {
                list.add(file)
            }
        }
    }
    return list
}

/**
 * 获取目录下所有符合filter的文件包括子目录
 *
 * @param dirPath 目录路径
 * @param filter  过滤器
 * @return 文件链表
 */
fun listFilesInDirWithFilter(dirPath: String, filter: FilenameFilter): List<File>? {
    return listFilesInDirWithFilter(getFileByPath(dirPath), filter)
}

/**
 * 获取目录下所有符合filter的文件包括子目录
 *
 * @param dir    目录
 * @param filter 过滤器
 * @return 文件链表
 */
fun listFilesInDirWithFilter(dir: File?, filter: FilenameFilter): List<File>? {
    if (dir == null || !isDir(dir)) {
        return null
    }
    val list = ArrayList<File>()
    val files = dir.listFiles()
    if (files != null && files.isNotEmpty()) {
        for (file in files) {
            if (filter.accept(file.parentFile, file.name)) {
                list.add(file)
            }
            if (file.isDirectory) {
                list.addAll(listFilesInDirWithFilter(file, filter)!!)
            }
        }
    }
    return list
}

/**
 * 获取目录下指定文件名的文件包括子目录
 *
 * 大小写忽略
 *
 * @param dirPath  目录路径
 * @param fileName 文件名
 * @return 文件链表
 */
fun searchFileInDir(dirPath: String, fileName: String): List<File>? {
    return searchFileInDir(getFileByPath(dirPath), fileName)
}

/**
 * 获取目录下指定文件名的文件包括子目录
 *
 * 大小写忽略
 *
 * @param dir      目录
 * @param fileName 文件名
 * @return 文件链表
 */
fun searchFileInDir(dir: File?, fileName: String): List<File>? {
    if (dir == null || !isDir(dir)) {
        return null
    }
    val list = ArrayList<File>()
    val files = dir.listFiles()
    if (files != null && files.isNotEmpty()) {
        for (file in files) {
            if (file.name.uppercase(Locale.getDefault()) == fileName.uppercase(Locale.getDefault())) {
                list.add(file)
            }
            if (file.isDirectory) {
                list.addAll(searchFileInDir(file, fileName)!!)
            }
        }
    }
    return list
}

/**
 * 将输入流写入文件
 *
 * @param filePath 路径
 * @param is       输入流
 * @param append   是否追加在文件末
 * @return `true`: 写入成功<br></br>`false`: 写入失败
 */
fun writeFileFromIS(filePath: String, `is`: InputStream, append: Boolean): Boolean {
    return writeFileFromIS(getFileByPath(filePath), `is`, append)
}

/**
 * 将输入流写入文件
 *
 * @param file   文件
 * @param is     输入流
 * @param append 是否追加在文件末
 * @return `true`: 写入成功<br></br>`false`: 写入失败
 */
fun writeFileFromIS(file: File?, `is`: InputStream?, append: Boolean): Boolean {
    if (file == null || `is` == null) {
        return false
    }
    if (!createOrExistsFile(file)) {
        return false
    }
    var os: OutputStream? = null
    return try {
        os = BufferedOutputStream(FileOutputStream(file, append))
        val data = ByteArray(1024)
        var len: Int
        while (
            run {
                len = `is`.read(data, 0, 1024)
                len
            } != -1
        ) {
            os.write(data, 0, len)
        }
        true
    } catch (e: IOException) {
        e.printStackTrace()
        false
    } finally {
        closeIO(`is`, os)
    }
}

/**
 * 将字符串写入文件
 *
 * @param filePath    文件路径
 * @param content     写入内容
 * @param append      是否追加在文件末
 * @param charset     编码格式
 * @return `true`: 写入成功<br></br>`false`: 写入失败
 */
fun writeFileFromString(
    filePath: String,
    content: String,
    append: Boolean,
    charset: Charset
): Boolean {
    return writeFileFromString(getFileByPath(filePath), content, append, charset)
}

/**
 * 将字符串写入文件
 *
 * @param file        文件
 * @param content     写入内容
 * @param append      是否追加在文件末
 * @param charset     编码格式
 * @return `true`: 写入成功<br></br>`false`: 写入失败
 */
fun writeFileFromString(
    file: File?,
    content: String?,
    append: Boolean,
    charset: Charset
): Boolean {
    if (file == null || content == null) {
        return false
    }
    if (!createOrExistsFile(file)) {
        return false
    }
    var bw: BufferedWriter? = null
    return try {
        val write = OutputStreamWriter(FileOutputStream(file, append), charset)
        bw = BufferedWriter(write)
        bw.write(content)
        true
    } catch (e: IOException) {
        e.printStackTrace()
        false
    } finally {
        closeIO(bw)
    }
}

/**
 * 指定编码按行读取文件到链表中
 *
 * @param filePath    文件路径
 * @param charsetName 编码格式
 * @return 文件行链表
 */
fun readFile2List(filePath: String, charsetName: String): List<String>? {
    return readFile2List(getFileByPath(filePath), charsetName)
}

/**
 * 指定编码按行读取文件到链表中
 *
 * @param file        文件
 * @param charsetName 编码格式
 * @return 文件行链表
 */
fun readFile2List(file: File?, charsetName: String): List<String>? {
    return readFile2List(file, 0, 0x7FFFFFFF, charsetName)
}

/**
 * 指定编码按行读取文件到链表中
 *
 * @param filePath    文件路径
 * @param st          需要读取的开始行数
 * @param end         需要读取的结束行数
 * @param charsetName 编码格式
 * @return 包含制定行的list
 */
fun readFile2List(filePath: String, st: Int, end: Int, charsetName: String): List<String>? {
    return readFile2List(getFileByPath(filePath), st, end, charsetName)
}

/**
 * 指定编码按行读取文件到链表中
 *
 * @param file        文件
 * @param st          需要读取的开始行数
 * @param end         需要读取的结束行数
 * @param charsetName 编码格式
 * @return 包含从start行到end行的list
 */
fun readFile2List(file: File?, st: Int, end: Int, charsetName: String): List<String>? {
    if (file == null) {
        return null
    }
    if (st > end) {
        return null
    }
    var reader: BufferedReader? = null
    try {
        var line: String?
        var curLine = 1
        val list = ArrayList<String?>()
        reader = if (charsetName.isSpace()) {
            BufferedReader(FileReader(file))
        } else {
            BufferedReader(InputStreamReader(FileInputStream(file), charsetName))
        }
        while (
            run {
                line = reader.readLine()
                line
            } != null
        ) {
            if (curLine > end) {
                break
            }
            if (curLine in st..end) {
                list.add(line)
            }
            ++curLine
        }
        return list as List<String>?
    } catch (e: IOException) {
        e.printStackTrace()
        return null
    } finally {
        closeIO(reader)
    }
}

/**
 * 指定编码按行读取文件到字符串中
 *
 * @param filePath 文件路径
 * @param charset  编码格式
 * @return 字符串
 */
fun readFile2String(filePath: String, charset: Charset? = null): String? {
    return readFile2String(getFileByPath(filePath), charset)
}

/**
 * 指定编码按行读取文件到字符串中
 *
 * @param file        文件
 * @param charset 编码格式
 * @return 字符串
 */
fun readFile2String(file: File?, charset: Charset? = null): String? {
    if (file == null) {
        return null
    }
    var reader: BufferedReader? = null
    try {
        val sb = StringBuilder()
        reader = if (charset == null) {
            BufferedReader(InputStreamReader(FileInputStream(file)))
        } else {
            BufferedReader(InputStreamReader(FileInputStream(file), charset))
        }
        var line: String?
        while (
            run {
                line = reader.readLine()
                line
            } != null
        ) {
            // windows系统换行为\r\n，Linux为\n
            sb.append(line).append("\r\n")
        }
        // 要去除最后的换行符
        return if (sb.isNotEmpty()) {
            sb.delete(sb.length - 2, sb.length).toString()
        } else {
            sb.toString()
        }
    } catch (e: IOException) {
        e.printStackTrace()
        return null
    } finally {
        closeIO(reader)
    }
}

/**
 * 读取文件到字符数组中
 *
 * @param filePath 文件路径
 * @return 字符数组
 */
fun readFile2Bytes(filePath: String): ByteArray? =
    readFile2Bytes(getFileByPath(filePath))

/**
 * 读取文件到字符数组中
 *
 * @param file 文件
 * @return 字符数组
 */
fun readFile2Bytes(file: File?): ByteArray? =
    file?.run {
        runCatching {
            FileInputStream(file).toBytes()
        }.onFailure {
            it.printStackTrace()
        }.getOrNull()
    }

/**
 * 获取文件最后修改的毫秒时间戳
 *
 * @param filePath 文件路径
 * @return 文件最后修改的毫秒时间戳
 */
fun getFileLastModified(filePath: String): Long {
    return getFileLastModified(getFileByPath(filePath))
}

/**
 * 获取文件最后修改的毫秒时间戳
 *
 * @param file 文件
 * @return 文件最后修改的毫秒时间戳
 */
fun getFileLastModified(file: File?): Long {
    return file?.lastModified() ?: -1
}

/**
 * 简单获取文件编码格式
 *
 * @param filePath 文件路径
 * @return 文件编码
 */
fun getFileCharsetSimple(filePath: String): String {
    return getFileCharsetSimple(getFileByPath(filePath))
}

/**
 * 简单获取文件编码格式
 *
 * @param file 文件
 * @return 文件编码
 */
fun getFileCharsetSimple(file: File?): String {
    if (file == null) {
        return ""
    }
    if (isUtf8(file)) {
        return "UTF-8"
    }
    var p = 0
    var `is`: InputStream? = null
    try {
        `is` = BufferedInputStream(FileInputStream(file))
        p = (`is`.read() shl 8) + `is`.read()
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        `is`.closeSafely()
    }
    return when (p) {
        0xefbb -> "UTF-8"
        0xfffe -> "Unicode"
        0xfeff -> "UTF-16BE"
        else -> "GBK"
    }
}

/**
 * Return whether the charset of file is utf8.
 *
 * @param filePath The path of file.
 * @return `true`: yes<br></br>`false`: no
 */
fun isUtf8(filePath: String?): Boolean {
    return isUtf8(getFileByPath(filePath))
}

/**
 * Return whether the charset of file is utf8.
 *
 * @param file The file.
 * @return `true`: yes<br></br>`false`: no
 */
fun isUtf8(file: File?): Boolean {
    if (file == null) return false
    var `is`: InputStream? = null
    try {
        val bytes = ByteArray(24)
        `is` = BufferedInputStream(FileInputStream(file))
        val read = `is`.read(bytes)
        return if (read != -1) {
            val readArr = ByteArray(read)
            System.arraycopy(bytes, 0, readArr, 0, read)
            isUtf8(readArr) == 100
        } else {
            false
        }
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        try {
            `is`?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    return false
}

/**
 * UTF-8编码方式
 * ----------------------------------------------
 * 0xxxxxxx
 * 110xxxxx 10xxxxxx
 * 1110xxxx 10xxxxxx 10xxxxxx
 * 11110xxx 10xxxxxx 10xxxxxx 10xxxxxx
 */
private fun isUtf8(raw: ByteArray): Int {
    var utf8 = 0
    var ascii = 0
    if (raw.size > 3) {
        if (raw[0] == 0xEF.toByte() && raw[1] == 0xBB.toByte() && raw[2] == 0xBF.toByte()) {
            return 100
        }
    }
    val len = raw.size
    var child = 0
    var i = 0
    while (i < len) {
        // UTF-8 byte shouldn't be FF and FE
        if ((raw[i] and 0xFF.toByte()) == 0xFF.toByte()
            || (raw[i] and 0xFE.toByte()) == 0xFE.toByte()
        ) {
            return 0
        }
        if (child == 0) {
            // ASCII format is 0x0*******
            if (raw[i] and 0x7F.toByte() == raw[i] && raw[i] != 0.toByte()) {
                ascii++
            } else if ((raw[i] and 0xC0.toByte()) == 0xC0.toByte()) {
                // 0x11****** maybe is UTF-8
                for (bit in 0..7) {
                    child = if (((0x80 shr bit).toByte() and raw[i]) == (0x80 shr bit).toByte()) {
                        bit
                    } else {
                        break
                    }
                }
                utf8++
            }
            i++
        } else {
            child = if (raw.size - i > child) child else raw.size - i
            var currentNotUtf8 = false
            for (children in 0 until child) {
                // format must is 0x10******
                if ((raw[i + children] and 0x80.toByte()) != 0x80.toByte()) {
                    if ((raw[i + children] and 0x7F.toByte()) == raw[i + children] && raw[i] != 0.toByte()) {
                        // ASCII format is 0x0*******
                        ascii++
                    }
                    currentNotUtf8 = true
                }
            }
            if (currentNotUtf8) {
                utf8--
                i++
            } else {
                utf8 += child
                i += child
            }
            child = 0
        }
    }
    // UTF-8 contains ASCII
    return if (ascii == len) {
        100
    } else {
        (100 * ((utf8 + ascii).toFloat() / len.toFloat())).toInt()
    }
}

/**
 * 获取文件行数
 *
 * @param filePath 文件路径
 * @return 文件行数
 */
fun getFileLines(filePath: String): Int {
    return getFileLines(getFileByPath(filePath))
}

/**
 * 获取文件行数
 *
 * @param file 文件
 * @return 文件行数
 */
fun getFileLines(file: File?): Int {
    var count = 1
    var `is`: InputStream? = null
    try {
        `is` = BufferedInputStream(FileInputStream(file!!))
        val buffer = ByteArray(1024)
        var readChars: Int
        if (LINE_SEPARATOR?.endsWith("\n") == true) {
            while (run {
                    readChars = `is`.read(buffer, 0, 1024)
                    readChars
                } != -1) {
                for (i in 0 until readChars) {
                    if (buffer[i] == '\n'.code.toByte()) {
                        ++count
                    }
                }
            }
        } else {
            while (run {
                    readChars = `is`.read(buffer, 0, 1024)
                    readChars
                } != -1) {
                for (i in 0 until readChars) {
                    if (buffer[i] == '\r'.code.toByte()) {
                        ++count
                    }
                }
            }
        }
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        closeIO(`is`)
    }
    return count
}

/**
 * Return the size.
 *
 * @param filePath The path of file.
 * @return the size
 */
fun getSize(filePath: String?): String {
    return getSize(getFileByPath(filePath))
}

/**
 * Return the size.
 *
 * @param file The directory.
 * @return the size
 */
fun getSize(file: File?): String {
    if (file == null) {
        return ""
    }
    return if (file.isDirectory) {
        getDirSize(file)
    } else {
        getFileSize(file)
    }
}

/**
 * 获取目录大小
 *
 * @param dirPath 目录路径
 * @return 文件大小
 */
fun getDirSize(dirPath: String?): String {
    return getDirSize(getFileByPath(dirPath))
}

/**
 * 获取目录大小
 *
 * @param dir 目录
 * @return 文件大小
 */
fun getDirSize(dir: File?): String {
    val len = getDirLength(dir)
    return if (len == (-1).toLong()) "" else len.byte2FitMemorySize()
}

/**
 * 获取文件大小
 *
 * @param filePath 文件路径
 * @return 文件大小
 */
fun getFileSize(filePath: String?): String {
    return getFileSize(getFileByPath(filePath))
}

/**
 * 获取文件大小
 *
 * @param file 文件
 * @return 文件大小
 */
fun getFileSize(file: File?): String {
    val len = getFileLength(file)
    return if (len == (-1).toLong()) "" else len.byte2FitMemorySize()
}

/**
 * Return the length.
 *
 * @param filePath The path of file.
 * @return the length
 */
fun getLength(filePath: String?): Long {
    return getLength(getFileByPath(filePath))
}

/**
 * Return the length.
 *
 * @param file The file.
 * @return the length
 */
fun getLength(file: File?): Long {
    if (file == null) {
        return 0
    }
    return if (file.isDirectory) {
        getDirLength(file)
    } else {
        getFileLength(file)
    }
}

/**
 * 获取目录长度
 *
 * @param dirPath 目录路径
 * @return 目录长度
 */
fun getDirLength(dirPath: String?): Long {
    return getDirLength(getFileByPath(dirPath))
}

/**
 * 获取目录长度
 *
 * @param dir 目录
 * @return 目录长度
 */
fun getDirLength(dir: File?): Long {
    if (!isDir(dir)) {
        return -1
    }
    var len: Long = 0
    val files = dir!!.listFiles()
    if (files != null && files.isNotEmpty()) {
        for (file in files) {
            len += if (file.isDirectory) {
                getDirLength(file)
            } else {
                file.length()
            }
        }
    }
    return len
}

/**
 * 获取文件长度
 *
 * @param filePath 文件路径
 * @return 文件长度
 */
fun getFileLength(filePath: String?): Long {
    val isURL = filePath?.matches(Regex("[a-zA-z]+://[^\\s]*"))
    if (isURL == true) {
        try {
            val conn = URL(filePath).openConnection() as HttpsURLConnection
            conn.setRequestProperty("Accept-Encoding", "identity")
            conn.connect()
            return if (conn.responseCode == 200) {
                conn.contentLength.toLong()
            } else {
                -1
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    return getFileLength(getFileByPath(filePath))
}

/**
 * 获取文件长度
 *
 * @param file 文件
 * @return 文件长度
 */
fun getFileLength(file: File?): Long {
    return if (!isFile(file)) {
        -1
    } else {
        file!!.length()
    }
}

/**
 * 获取文件的MD5校验码
 *
 * @param filePath 文件路径
 * @return 文件的MD5校验码
 */
fun getFileMD5ToString(filePath: String): String? {
    val file = if (filePath.isSpace()) null else File(filePath)
    return getFileMD5ToString(file)
}

/**
 * 获取文件的MD5校验码
 *
 * @param filePath 文件路径
 * @return 文件的MD5校验码
 */
fun getFileMD5(filePath: String): ByteArray? {
    val file = if (filePath.isSpace()) null else File(filePath)
    return getFileMD5(file)
}

/**
 * 获取文件的MD5校验码
 *
 * @param file 文件
 * @return 文件的MD5校验码
 */
fun getFileMD5ToString(file: File?): String? {
    return getFileMD5(file).toHexString()
}

/**
 * 获取文件的MD5校验码
 *
 * @param file 文件
 * @return 文件的MD5校验码
 */
fun getFileMD5(file: File?): ByteArray? {
    if (file == null) {
        return null
    }
    var dis: DigestInputStream? = null
    try {
        val fis = FileInputStream(file)
        var md = MessageDigest.getInstance("MD5")
        dis = DigestInputStream(fis, md)
        val buffer = ByteArray(1024 * 256)
        while (true) {
            if (dis.read(buffer) <= 0) {
                break
            }
        }
        md = dis.messageDigest
        return md.digest()
    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        dis.closeSafely()
    }
    return null
}

/**
 * 获取全路径中的最长目录
 *
 * @param file 文件
 * @return filePath最长目录
 */
fun getDirName(file: File?): String? =
    if (file == null) {
        null
    } else {
        getDirName(file.path)
    }

/**
 * 获取全路径中的最长目录
 *
 * @param filePath 文件路径
 * @return filePath最长目录
 */
fun getDirName(filePath: String): String? {
    if (filePath.isSpace()) {
        return filePath
    }
    val lastSep = filePath.lastIndexOf(File.separator)
    return if (lastSep == -1) {
        ""
    } else {
        filePath.take(lastSep + 1)
    }
}

/**
 * 获取全路径中的文件名
 *
 * @param file 文件
 * @return 文件名
 */
fun getFileName(file: File?): String? =
    if (file == null) {
        null
    } else {
        getFileName(file.path)
    }

/**
 * 获取全路径中的文件名
 *
 * @param filePath 文件路径
 * @return 文件名
 */
fun getFileName(filePath: String): String? {
    if (filePath.isSpace()) {
        return filePath
    }
    val lastSep = filePath.lastIndexOf(File.separator)
    return if (lastSep == -1) {
        filePath
    } else {
        filePath.substring(lastSep + 1)
    }
}

/**
 * 获取全路径中的不带拓展名的文件名
 *
 * @param file 文件
 * @return 不带拓展名的文件名
 */
fun getFileNameNoExtension(file: File?): String? {
    return if (file == null) {
        null
    } else {
        getFileNameNoExtension(file.path)
    }
}

/**
 * 获取全路径中的不带拓展名的文件名
 *
 * @param filePath 文件路径
 * @return 不带拓展名的文件名
 */
fun getFileNameNoExtension(filePath: String?): String {
    if (filePath.isSpace()) {
        return ""
    }
    val lastPoi = filePath!!.lastIndexOf('.')
    val lastSep = filePath.lastIndexOf(File.separator)
    if (lastSep == -1) {
        return if (lastPoi == -1) filePath else filePath.substring(0, lastPoi)
    }
    return if (lastPoi == -1 || lastSep > lastPoi) {
        filePath.substring(lastSep + 1)
    } else {
        filePath.substring(lastSep + 1, lastPoi)
    }
}

/**
 * 获取全路径中的文件拓展名
 *
 * @param file 文件
 * @return 文件拓展名
 */
fun getFileExtension(file: File?): String? {
    return if (file == null) {
        null
    } else {
        getFileExtension(file.path)
    }
}

/**
 * 获取全路径中的文件拓展名
 * @param filePath 文件路径
 * @return 文件拓展名
 */
fun getFileExtension(filePath: String): String {
    if (filePath.isSpace()) {
        return filePath
    }
    val lastPoi = filePath.lastIndexOf('.')
    val lastSep = filePath.lastIndexOf(File.separator)
    return if (lastPoi == -1 || lastSep >= lastPoi) {
        ""
    } else {
        filePath.substring(lastPoi + 1)
    }
}

/**
 * Notify system to scan the file.
 *
 * @param filePath The path of file.
 */
fun notifySystemToScan(filePath: String?) {
    notifySystemToScan(getFileByPath(filePath))
}

/**
 * Notify system to scan the file.
 *
 * @param file The file.
 */
fun notifySystemToScan(file: File?) {
    if (file == null || !file.exists()) return
    val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
    intent.data = Uri.parse("file://" + file.absolutePath)
    applicationContext.sendBroadcast(intent)
}

/**
 * Return the total size of file system.
 *
 * @param anyPathInFs Any path in file system.
 * @return the total size of file system
 */
@SuppressLint("ObsoleteSdkInt")
fun getFsTotalSize(anyPathInFs: String?): Long {
    if (TextUtils.isEmpty(anyPathInFs)) return 0
    val statFs = StatFs(anyPathInFs)
    val blockSize: Long
    val totalSize: Long
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
        blockSize = statFs.blockSizeLong
        totalSize = statFs.blockCountLong
    } else {
        blockSize = statFs.blockSize.toLong()
        totalSize = statFs.blockCount.toLong()
    }
    return blockSize * totalSize
}

/**
 * Return the available size of file system.
 *
 * @param anyPathInFs Any path in file system.
 * @return the available size of file system
 */
@SuppressLint("ObsoleteSdkInt")
fun getFsAvailableSize(anyPathInFs: String?): Long {
    if (TextUtils.isEmpty(anyPathInFs)) return 0
    val statFs = StatFs(anyPathInFs)
    val blockSize: Long
    val availableSize: Long
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
        blockSize = statFs.blockSizeLong
        availableSize = statFs.availableBlocksLong
    } else {
        blockSize = statFs.blockSize.toLong()
        availableSize = statFs.availableBlocks.toLong()
    }
    return blockSize * availableSize
}
