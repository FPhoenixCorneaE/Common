package com.fphoenixcorneae.common.util

import android.media.MediaPlayer
import com.fphoenixcorneae.common.ext.appContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

/**
 * @desc：媒体播放器
 * @since：2021-05-10 17:14
 */
object MediaPlayerUtil {

    private var mediaPlayer: MediaPlayer? = null

    fun play(path: String) {
        try {
            mediaPlayer = MediaPlayer().apply {
                setDataSource(path)
                prepare()
                start()
                setOnCompletionListener {
                    close()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * @param rawId res->raw 文件夹下的原始音频 id
     */
    fun play(rawId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                mediaPlayer = MediaPlayer.create(appContext, rawId).apply {
                    // prepareAsync called in state 8 异常
                    // MediaPlayer.create(context,resId)这个方式配置数据源后，就完成了初始化，
                    // 所以不用prepare()可以直接start()了
                    // prepare()
                    start()
                    setOnCompletionListener {
                        close()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * @param data      音频字节数组
     * @param audioType 音频类型
     */
    fun play(data: ByteArray, audioType: String) {
        if (data.isNotEmpty() && audioType.isNotEmpty()) {
            val tempMp3 = File(appContext.cacheDir, "temp$audioType")
            tempMp3.deleteOnExit()
            val fos = FileOutputStream(tempMp3)
            fos.write(data)
            fos.close()
            val fis = FileInputStream(tempMp3)
            if (mediaPlayer != null) {
                close()
            }
            mediaPlayer = MediaPlayer().apply {
                setDataSource(fis.fd)
                prepare()
                start()
                setOnCompletionListener {
                    close()
                }
            }
            fis.close()
        }
    }

    fun close() {
        mediaPlayer?.apply {
            if (isPlaying) {
                stop()
            }
            reset()
            release()
        }
        mediaPlayer = null
    }
}