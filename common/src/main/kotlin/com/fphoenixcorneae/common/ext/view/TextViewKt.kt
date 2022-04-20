package com.fphoenixcorneae.common.ext.view

import android.graphics.Paint
import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView

/**
 * 自动缩小文字大小，以全部显示文字
 */
fun TextView.autoScaleTextSize(maxWidth: Int) {
    if (maxWidth <= 0) {
        return
    }
    val textString = text
    var width = paint.measureText(textString, 0, textString.length)
    while (width > maxWidth && paint.textSize > 0) {
        paint.textSize = paint.textSize - 1
        width = paint.measureText(textString, 0, textString.length)
    }
    invalidate()
}

/**
 * 给TextView设置下划线
 */
fun TextView.setUnderLine() {
    paint.flags = Paint.UNDERLINE_TEXT_FLAG
    paint.isAntiAlias = true
}

/**
 * 判断 text 是否为空 并传入相关操作
 */
fun TextView.textAction(
    notEmptyAction: TextView.() -> Unit,
    emptyAction: TextView.() -> Unit
) {
    if (text.toString().isNotEmpty()) notEmptyAction() else emptyAction()
}

fun TextView.textWatcher(watcher: KtxTextWatcher.() -> Unit) =
    addTextChangedListener(KtxTextWatcher().apply(watcher))

class KtxTextWatcher : TextWatcher {

    private var _afterTextChanged: ((Editable?) -> Unit)? = null
    private var _beforeTextChanged: ((CharSequence?, Int, Int, Int) -> Unit)? = null
    private var _onTextChanged: ((CharSequence?, Int, Int, Int) -> Unit)? = null


    fun afterTextChanged(listener: ((Editable?) -> Unit)) {
        _afterTextChanged = listener
    }

    fun beforeTextChanged(listener: (CharSequence?, Int, Int, Int) -> Unit) {
        _beforeTextChanged = listener
    }

    fun onTextChanged(listener: (CharSequence?, Int, Int, Int) -> Unit) {
        _onTextChanged = listener
    }

    override fun afterTextChanged(s: Editable?) {
        _afterTextChanged?.invoke(s)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        _beforeTextChanged?.invoke(s, start, count, after)
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        _onTextChanged?.invoke(s, start, before, count)
    }

}