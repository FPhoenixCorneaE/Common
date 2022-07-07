package com.fphoenixcorneae.common.util

import android.content.Intent
import android.net.Uri
import com.fphoenixcorneae.common.annotation.FileType
import com.fphoenixcorneae.common.ext.applicationContext
import java.io.File
import java.util.*

/**
 * 打开文件工具类
 *
 * @date 2019-12-04 14:58
 */
object OpenFileUtil {

    /**
     * 打开文件
     *
     * @param file
     */
    @JvmStatic
    fun openFile(file: File) {
        if (!file.exists()) {
            return
        }
        // 取得文件扩展名
        val end =
            file.name.substring(file.name.lastIndexOf(".") + 1, file.name.length)
                .lowercase(Locale.getDefault())
        when (end) {
            "3gp", "mp4" -> openVideoFileIntent(file, FileType.VIDEO)
            "m4a", "mp3", "mid", "xmf", "ogg", "wav" -> openAudioFileIntent(
                file,
                FileType.AUDIO
            )
            "doc", "docx" -> commonOpenFileWithType(file, FileType.WORD)
            "xls", "xlsx" -> commonOpenFileWithType(file, FileType.EXCEL)
            "jpg", "gif", "png", "jpeg", "bmp" -> commonOpenFileWithType(
                file,
                FileType.IMAGE
            )
            "txt" -> commonOpenFileWithType(file, FileType.TXT)
            "htm", "html" -> commonOpenFileWithType(file, FileType.HTML)
            "apk" -> commonOpenFileWithType(file, FileType.APK)
            "ppt" -> commonOpenFileWithType(file, FileType.PPT)
            "pdf" -> commonOpenFileWithType(file, FileType.PDF)
            "chm" -> commonOpenFileWithType(file, FileType.CHM)
            else -> commonOpenFileWithType(file, FileType.ALL)
        }
    }

    /**
     * 打开文件夹目录
     * @param file 文件
     * @param type 文件类型
     */
    @JvmStatic
    fun openFileDirectory(file: File, @FileType type: String) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.setDataAndType(Uri.parse(file.parent), type)
        applicationContext.startActivity(intent)
    }

    /**
     * Android传入type打开文件
     *
     * @param file
     * @param type
     */
    private fun commonOpenFileWithType(file: File, @FileType type: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        FileProviderUtil.setIntentDataAndType(intent, type, file, true)
        applicationContext.startActivity(intent)
    }

    /**
     * Android打开Video文件
     *
     * @param file
     */
    private fun openVideoFileIntent(file: File, @FileType type: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("oneshot", 0)
        intent.putExtra("configchange", 0)
        FileProviderUtil.setIntentDataAndType(intent, type, file, false)
        applicationContext.startActivity(intent)
    }

    /**
     * Android打开Audio文件
     *
     * @param file
     */
    private fun openAudioFileIntent(file: File, @FileType type: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("oneshot", 0)
        intent.putExtra("configchange", 0)
        FileProviderUtil.setIntentDataAndType(intent, type, file, false)
        applicationContext.startActivity(intent)
    }
}