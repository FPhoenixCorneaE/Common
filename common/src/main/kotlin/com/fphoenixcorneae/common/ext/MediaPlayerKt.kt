package com.fphoenixcorneae.common.ext

import android.media.MediaPlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

/**
 * 媒体播放器
 */
private var mMediaPlayer: MediaPlayer? = null

/**
 * @param path 本地路径
 */
fun playMedia(path: String) {
    runCatching {
        mMediaPlayer = MediaPlayer().apply {
            setDataSource(path)
            prepare()
            start()
            setOnCompletionListener {
                closeMedia()
            }
        }
    }.onFailure {
        it.printStackTrace()
    }
}

/**
 * @param rawId res->raw 文件夹下的原始音频 id
 */
fun playMedia(rawId: Int) {
    CoroutineScope(Dispatchers.IO).launch {
        kotlin.runCatching {
            mMediaPlayer = MediaPlayer.create(applicationContext, rawId).apply {
                // prepareAsync called in state 8 异常
                // MediaPlayer.create(context,resId)这个方式配置数据源后，就完成了初始化，
                // 所以不用prepare()可以直接start()了
                // prepare()
                start()
                setOnCompletionListener {
                    closeMedia()
                }
            }
        }.onFailure {
            it.printStackTrace()
        }
    }
}

/**
 * @param data      音频字节数组
 * @param audioType 音频类型
 */
fun playMedia(data: ByteArray, audioType: String) {
    runCatching {
        if (data.isNotEmpty() && audioType.isNotEmpty()) {
            val tempMp3 = File(applicationContext.cacheDir, "temp$audioType")
            tempMp3.deleteOnExit()
            val fos = FileOutputStream(tempMp3)
            fos.write(data)
            fos.close()
            val fis = FileInputStream(tempMp3)
            closeMedia()
            mMediaPlayer = MediaPlayer().apply {
                setDataSource(fis.fd)
                prepare()
                start()
                setOnCompletionListener {
                    closeMedia()
                }
            }
            fis.close()
        }
    }.onFailure {
        it.printStackTrace()
    }
}

fun closeMedia() {
    runCatching {
        mMediaPlayer?.apply {
            if (isPlaying) {
                stop()
            }
            reset()
            release()
        }
        mMediaPlayer = null
    }.onFailure {
        it.printStackTrace()
    }
}