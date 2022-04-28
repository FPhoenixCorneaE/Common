package com.fphoenixcorneae.common.ext

import android.media.AudioManager
import android.os.Build

/**
 * 获取当前媒体音量
 * @param streamType The stream type.
 * * [AudioManager.STREAM_SYSTEM]        系统声音
 * * [AudioManager.STREAM_VOICE_CALL]    通话声音
 * * [AudioManager.STREAM_RING]          铃声声音
 * * [AudioManager.STREAM_MUSIC]         媒体声音
 * * [AudioManager.STREAM_ALARM]         提示声音
 * * [AudioManager.STREAM_NOTIFICATION]  通知声音
 * * [AudioManager.STREAM_DTMF]          双音多频
 * * [AudioManager.STREAM_ACCESSIBILITY] 无障碍性
 */
fun getStreamVolume(
    streamType: Int = AudioManager.STREAM_MUSIC
): Int =
    applicationContext.audioManager?.getStreamVolume(streamType) ?: 0

/**
 * 设置音量
 * @param volumeValue The volume.
 * @param flags       The flags.
 * * [AudioManager.FLAG_SHOW_UI]
 * * [AudioManager.FLAG_ALLOW_RINGER_MODES]
 * * [AudioManager.FLAG_PLAY_SOUND]
 * * [AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE]
 * * [AudioManager.FLAG_VIBRATE]
 */
fun setStreamVolume(
    volumeValue: Int,
    streamType: Int = AudioManager.STREAM_MUSIC,
    flags: Int = 0
) =
    applicationContext.audioManager?.setStreamVolume(streamType, volumeValue, flags)

/**
 * 获取最大音量
 * @param streamType The stream type.
 * * [AudioManager.STREAM_SYSTEM]        系统声音
 * * [AudioManager.STREAM_VOICE_CALL]    通话声音
 * * [AudioManager.STREAM_RING]          铃声声音
 * * [AudioManager.STREAM_MUSIC]         媒体声音
 * * [AudioManager.STREAM_ALARM]         提示声音
 * * [AudioManager.STREAM_NOTIFICATION]  通知声音
 * * [AudioManager.STREAM_DTMF]          双音多频
 * * [AudioManager.STREAM_ACCESSIBILITY] 无障碍性
 */
fun getStreamMaxVolume(
    streamType: Int = AudioManager.STREAM_MUSIC
): Int =
    applicationContext.audioManager?.getStreamMaxVolume(streamType) ?: 0

/**
 * 获取最小音量
 * @param streamType The stream type.
 * * [AudioManager.STREAM_SYSTEM]        系统声音
 * * [AudioManager.STREAM_VOICE_CALL]    通话声音
 * * [AudioManager.STREAM_RING]          铃声声音
 * * [AudioManager.STREAM_MUSIC]         媒体声音
 * * [AudioManager.STREAM_ALARM]         提示声音
 * * [AudioManager.STREAM_NOTIFICATION]  通知声音
 * * [AudioManager.STREAM_DTMF]          双音多频
 * * [AudioManager.STREAM_ACCESSIBILITY] 无障碍性
 */
fun getStreamMinVolume(
    streamType: Int = AudioManager.STREAM_MUSIC
): Int =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        applicationContext.audioManager?.getStreamMinVolume(streamType) ?: 0
    } else {
        0
    }
