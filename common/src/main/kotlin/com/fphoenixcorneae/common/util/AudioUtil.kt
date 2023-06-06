package com.fphoenixcorneae.common.util

import android.content.Intent
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.media.AudioTrack.STATE_UNINITIALIZED
import android.net.Uri
import android.os.Build
import android.os.SystemClock
import android.provider.Settings
import com.fphoenixcorneae.common.ext.appPackageName
import com.fphoenixcorneae.common.ext.applicationContext
import com.fphoenixcorneae.common.ext.audioManager
import java.io.IOException
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.Executors

/**
 * @desc：音频工具类
 * @date：2021/5/2 12:39
 */
object AudioUtil {

    private val soundBytes: HashMap<String, ByteArray> = HashMap()
    private val audioTrackMap: HashMap<String, AudioTrack?> = HashMap()
    private val audioTrackUsingerList: ConcurrentLinkedQueue<AudioTrack> = ConcurrentLinkedQueue()
    private lateinit var knobAudioTrack: AudioTrack
    private val mAssetManager by lazy { applicationContext.assets }

    /**
     * 音频服务 Manager
     */
    private val mAudioManager by lazy { applicationContext.audioManager }

    /**
     * 线程池
     */
    val mThreadPool by lazy { Executors.newFixedThreadPool(100) }

    init {
        initAudioTrack()
    }

    private fun initAudioTrack() {
        mThreadPool.execute {
            mAssetManager?.list("sound")?.forEach {
                val readBytes = mAssetManager?.open("sound/${it}")!!.readBytes()
                val fromIndex = resolveHeader(readBytes).dataIndex
                soundBytes[it] = readBytes.copyOfRange(fromIndex + 4, readBytes.size)
            }

            val simpleRate = 48000
            val channelConfig = AudioFormat.CHANNEL_OUT_STEREO
            val audioFormat = AudioFormat.ENCODING_PCM_16BIT
            val minBufferSize = AudioTrack.getMinBufferSize(simpleRate, channelConfig, audioFormat)
            knobAudioTrack = AudioTrack(
                AudioManager.STREAM_MUSIC,
                simpleRate,
                channelConfig,
                audioFormat,
                minBufferSize,
                AudioTrack.MODE_STATIC
            )
            knobAudioTrack.setVolume(0.5f)
        }
    }

    private fun muteSystemTouch() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(applicationContext)) {
                val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
                intent.data = Uri.parse("package:$appPackageName")
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                applicationContext.startActivity(intent)
            } else {
                Settings.System.putInt(
                    applicationContext.contentResolver,
                    Settings.System.SOUND_EFFECTS_ENABLED,
                    0
                )
                mAudioManager?.unloadSoundEffects()
            }
        } else {
            Settings.System.putInt(
                applicationContext.contentResolver,
                Settings.System.SOUND_EFFECTS_ENABLED,
                0
            )
            mAudioManager?.unloadSoundEffects()
        }
    }


    fun playAudio(soundName: String) {
        muteSystemTouch()
        playByAudioTrack(soundName)
    }

    fun stopPlay(soundName: String) {
        if (audioTrackMap[soundName]?.playState != STATE_UNINITIALIZED) {
            audioTrackMap[soundName]?.stop()
            audioTrackMap[soundName]?.release()
        }
    }

    private fun playByAudioTrack(filePath: String) {
        mThreadPool.execute {
            if (filePath.contains("common_knob_turn")) {
                if (audioTrackMap[filePath] == null) {
                    knobAudioTrack.write(soundBytes[filePath]!!, 0, soundBytes[filePath]!!.size)
                    knobAudioTrack.play()
                    audioTrackMap[filePath] = knobAudioTrack
                } else {
                    audioTrackMap[filePath]?.stop()
                    val reloadStaticData = audioTrackMap[filePath]?.reloadStaticData()
                    if (reloadStaticData == AudioTrack.SUCCESS) {
                        audioTrackMap[filePath]?.play()
                    }
                }
            } else {
                if (audioTrackUsingerList.size > 10) {
                    audioTrackUsingerList.remove().release()
                    SystemClock.sleep(10)
                }
                val simpleRate = 48000
                val channelConfig = AudioFormat.CHANNEL_OUT_STEREO
                val audioFormat = AudioFormat.ENCODING_PCM_16BIT
                val minBufferSize =
                    AudioTrack.getMinBufferSize(simpleRate, channelConfig, audioFormat)
                val audioTrack = AudioTrack(
                    AudioManager.STREAM_MUSIC,
                    simpleRate,
                    channelConfig,
                    audioFormat,
                    minBufferSize,
                    AudioTrack.MODE_STREAM
                )
                audioTrackUsingerList.add(audioTrack)
                audioTrackMap[filePath] = audioTrack
                try {
                    audioTrack.play()
                    audioTrack.setVolume(1.0f)
                    audioTrack.write(soundBytes[filePath]!!, 0, soundBytes[filePath]!!.size)
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    audioTrack.release()
                    audioTrackUsingerList.remove(audioTrack)
                    audioTrackMap[filePath] = null
                }
            }
        }
    }


    /**
     * 解析头部，并获得文件指针指向数据开始位置的 InputStream，记得使用后需要关闭
     */
    @Throws(IOException::class)
    private fun resolveHeader(bytes: ByteArray): HeaderInfo {
        val indexOf = bytes.indexOf(0x64617461.toByte())
        // 每位数据
        val bitPerSample = bytes2Int(bytes.copyOfRange(indexOf - 3, indexOf - 1))
        // 字节对齐
        val blockAlign = bytes2Int(bytes.copyOfRange(indexOf - 5, indexOf - 3))
        // 每秒的数据
        val bytePerSec = bytes2Int(bytes.copyOfRange(indexOf - 9, indexOf - 5))
        // 采样率
        val sampleRate = bytes2Int(bytes.copyOfRange(indexOf - 13, indexOf - 9))
        // 通道数
        val channel = bytes2Int(bytes.copyOfRange(indexOf - 15, indexOf - 13))
        // 格式 tag
        val formatTag = bytes2Int(bytes.copyOfRange(indexOf - 17, indexOf - 15))
        // fps
        val bits = bytes2Int(bytes.copyOfRange(indexOf - 21, indexOf - 17))
        return HeaderInfo(
            bitPerSample = bitPerSample,
            blockAlign = blockAlign,
            bytePerSec = bytePerSec,
            sampleRate = sampleRate,
            channelFormat = when (channel) {
                1 -> AudioFormat.CHANNEL_OUT_MONO
                2 -> AudioFormat.CHANNEL_OUT_STEREO
                else -> AudioFormat.CHANNEL_OUT_STEREO
            },
            bitFormat = when (bits) {
                8 -> AudioFormat.ENCODING_PCM_8BIT
                16 -> AudioFormat.ENCODING_PCM_16BIT
                else -> AudioFormat.ENCODING_DEFAULT
            },
            dataIndex = indexOf + 3,
        )
    }

    data class HeaderInfo(
        val bitPerSample: Int,
        val blockAlign: Int,
        val bytePerSec: Int,
        val sampleRate: Int,
        val channelFormat: Int,
        val bitFormat: Int,
        val dataIndex: Int,
    )

    private fun bytes2Int(bytes: ByteArray): Int {
        var result = 0
        // 将每个byte依次搬运到int相应的位置
        bytes.forEachIndexed { index, byte ->
            result = result or (byte.toInt() and 0xff shl index * 8)
        }
        return result
    }
}