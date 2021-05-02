package com.fphoenixcorneae.ext

import android.media.AudioManager

/**
 * 获取最大音量
 */
fun AudioManager.getMaxVolume(
    streamType: Int = AudioManager.STREAM_MUSIC
) = getStreamMaxVolume(streamType)

/**
 * 获取当前媒体音量
 *
 * AudioManager.STREAM_SYSTEM //系统声音
 * AudioManager.STREAM_VOICE_CALL //通话声音
 * AudioManager.STREAM_RING //铃声声音
 * AudioManager.STREAM_MUSIC //媒体声音
 * AudioManager.STREAM_ALARM //提示声音
 */
fun AudioManager.getStreamVolume(
    streamType: Int = AudioManager.STREAM_MUSIC
) = getStreamVolume(streamType)

/**
 * 获取当前媒体音量
 *
 * AudioManager.STREAM_SYSTEM //系统声音
 * AudioManager.STREAM_VOICE_CALL //通话声音
 * AudioManager.STREAM_RING //铃声声音
 * AudioManager.STREAM_MUSIC //媒体声音
 * AudioManager.STREAM_ALARM //提示声音
 */
fun AudioManager.getStreamMaxVolume(
    streamType: Int = AudioManager.STREAM_MUSIC
) = getStreamMaxVolume(streamType)

/**
 * 设置音量
 */
fun AudioManager.setVolume(
    volumeValue: Int,
    streamType: Int = AudioManager.STREAM_MUSIC
) {
    setStreamVolume(streamType, volumeValue, 0)
}