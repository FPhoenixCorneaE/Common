package com.fphoenixcorneae.ext

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.net.Uri

/**
 * 复制文本到剪贴板
 *
 * @param text 文本
 */
fun Context.copyText(text: CharSequence?) {
    clipboardManager?.setPrimaryClip(ClipData.newPlainText("text", text))
}

/**
 * 获取剪贴板的文本
 *
 * @return 剪贴板的文本
 */
val clipboardText: CharSequence?
    get() {
        val clipboardManager = appContext.clipboardManager
        val clip = clipboardManager?.primaryClip
        return when {
            clip != null && clip.itemCount > 0 -> {
                clip.getItemAt(0).coerceToText(appContext)
            }
            else -> {
                null
            }
        }
    }

/**
 * 复制 uri 到剪贴板
 *
 * @param uri uri
 */
fun Context.copyUri(uri: Uri?) {
    clipboardManager?.setPrimaryClip(
        ClipData.newUri(
            appContext.contentResolver,
            "uri",
            uri
        )
    )
}

/**
 * 获取剪贴板的uri
 *
 * @return 剪贴板的uri
 */
val clipboardUri: Uri?
    get() {
        val clipboardManager = appContext.clipboardManager
        val clip = clipboardManager?.primaryClip
        return when {
            clip != null && clip.itemCount > 0 -> {
                clip.getItemAt(0).uri
            }
            else -> {
                null
            }
        }
    }

/**
 * 复制意图到剪贴板
 *
 * @param intent 意图
 */
fun Context.copyIntent(intent: Intent?) {
    clipboardManager?.setPrimaryClip(ClipData.newIntent("intent", intent))
}

/**
 * 获取剪贴板的意图
 *
 * @return 剪贴板的意图
 */
val clipboardIntent: Intent?
    get() {
        val clipboardManager = appContext.clipboardManager
        val clip = clipboardManager?.primaryClip
        return when {
            clip != null && clip.itemCount > 0 -> {
                clip.getItemAt(0).intent
            }
            else -> {
                null
            }
        }
    }