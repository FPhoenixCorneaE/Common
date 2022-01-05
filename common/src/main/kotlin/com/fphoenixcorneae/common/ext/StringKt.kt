package com.fphoenixcorneae.common.ext

import android.annotation.SuppressLint
import android.os.Build
import android.text.Html
import android.text.Spanned

@SuppressLint("InlinedApi")
fun String.toHtml(flag: Int = Html.FROM_HTML_MODE_LEGACY): Spanned {
    return when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {
            Html.fromHtml(this, flag)
        }
        else -> {
            Html.fromHtml(this)
        }
    }
}

/**
 * 判断任意一个字符串是否为空
 */
fun String?.isSpace(): Boolean {
    if (this.isNull()) {
        return true
    }
    var i = 0
    val len = this!!.length
    while (i < len) {
        if (!Character.isWhitespace(this[i])) {
            return false
        }
        ++i
    }
    return true
}