package com.fphoenixcorneae.common.ext

import android.text.Spanned
import androidx.core.text.HtmlCompat

fun String.toHtml(flag: Int = HtmlCompat.FROM_HTML_MODE_LEGACY): Spanned {
    return HtmlCompat.fromHtml(this, flag)
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