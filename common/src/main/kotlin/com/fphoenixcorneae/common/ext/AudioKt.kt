package com.fphoenixcorneae.common.ext

import android.media.AudioManager

/**
 * 获取当前媒体音量
 * * [AudioManager.STREAM_SYSTEM]       系统声音
 * * [AudioManager.STREAM_VOICE_CALL]   通话声音
 * * [AudioManager.STREAM_RING]         铃声声音
 * * [AudioManager.STREAM_MUSIC]        媒体声音
 * * [AudioManager.STREAM_ALARM]        提示声音
 * * [AudioManager.STREAM_NOTIFICATION] 通知声音
 */
fun getStreamVolume(
    streamType: Int = AudioManager.STREAM_MUSIC
): Int =
    appContext.audioManager?.getStreamVolume(streamType) ?: 0

/**
 * 获取最大音量
 * * [AudioManager.STREAM_SYSTEM]       系统声音
 * * [AudioManager.STREAM_VOICE_CALL]   通话声音
 * * [AudioManager.STREAM_RING]         铃声声音
 * * [AudioManager.STREAM_MUSIC]        媒体声音
 * * [AudioManager.STREAM_ALARM]        提示声音
 * * [AudioManager.STREAM_NOTIFICATION] 通知声音
 */
fun getStreamMaxVolume(
    streamType: Int = AudioManager.STREAM_MUSIC
): Int =
    appContext.audioManager?.getStreamMaxVolume(streamType) ?: 0

/**
 * 设置音量
 */
fun setStreamVolume(
    volumeValue: Int,
    streamType: Int = AudioManager.STREAM_MUSIC
) =
    appContext.audioManager?.setStreamVolume(streamType, volumeValue, 0)