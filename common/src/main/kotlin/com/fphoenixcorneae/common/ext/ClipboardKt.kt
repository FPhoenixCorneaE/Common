package com.fphoenixcorneae.common.ext

import android.content.ClipData
import android.content.Intent
import android.net.Uri

/**
 * 复制文本到剪贴板
 */
fun CharSequence?.copy2Clipboard() =
    appContext.clipboardManager
        ?.setPrimaryClip(ClipData.newPlainText("text", this))

/**
 * 获取剪贴板的文本, it is called in the post(Runnable).
 */
fun getClipboardText(): CharSequence? =
    appContext.clipboardManager
        ?.primaryClip
        ?.run {
            if (itemCount > 0) {
                getItemAt(0).coerceToText(appContext)
            } else {
                null
            }
        }

/**
 * 复制Uri到剪贴板
 */
fun Uri?.copy2Clipboard() =
    appContext.clipboardManager
        ?.setPrimaryClip(ClipData.newUri(appContext.contentResolver, "uri", this))

/**
 * 获取剪贴板的Uri, it is called in the post(Runnable).
 */
fun getClipboardUri(): Uri? =
    appContext.clipboardManager
        ?.primaryClip
        ?.run {
            if (itemCount > 0) {
                getItemAt(0).uri
            } else {
                null
            }
        }

/**
 * 复制意图到剪贴板
 */
fun Intent.copy2Clipboard() =
    appContext.clipboardManager
        ?.setPrimaryClip(ClipData.newIntent("intent", this))

/**
 * 获取剪贴板的意图, it is called in the post(Runnable).
 */
fun getClipboardIntent(): Intent? =
    appContext.clipboardManager
        ?.primaryClip
        ?.run {
            if (itemCount > 0) {
                getItemAt(0).intent
            } else {
                null
            }
        }
