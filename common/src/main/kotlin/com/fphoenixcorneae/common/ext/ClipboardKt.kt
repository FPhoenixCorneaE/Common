package com.fphoenixcorneae.common.ext

import android.content.ClipData
import android.content.ClipboardManager.OnPrimaryClipChangedListener
import android.content.Intent
import android.net.Uri

/**
 * 复制文本到剪贴板
 */
fun CharSequence?.copy2Clipboard(
    label: CharSequence? = "text"
) =
    appContext.clipboardManager
        ?.setPrimaryClip(ClipData.newPlainText(label, this))

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
fun Uri?.copy2Clipboard(
    label: CharSequence? = "uri"
) =
    appContext.clipboardManager
        ?.setPrimaryClip(ClipData.newUri(appContext.contentResolver, label, this))

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
fun Intent.copy2Clipboard(
    label: CharSequence? = "intent"
) =
    appContext.clipboardManager
        ?.setPrimaryClip(ClipData.newIntent(label, this))

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

/**
 * Return the label for clipboard.
 */
fun getClipboardLabel(): CharSequence? =
    appContext.clipboardManager?.primaryClipDescription?.label

/**
 * Clear the clipboard.
 */
fun clear() =
    appContext.clipboardManager?.setPrimaryClip(ClipData.newPlainText(null, ""))

/**
 * Add the clipboard changed listener.
 */
fun addChangedListener(listener: OnPrimaryClipChangedListener?) {
    appContext.clipboardManager?.addPrimaryClipChangedListener(listener)
}

/**
 * Remove the clipboard changed listener.
 */
fun removeChangedListener(listener: OnPrimaryClipChangedListener?) {
    appContext.clipboardManager?.removePrimaryClipChangedListener(listener)
}
