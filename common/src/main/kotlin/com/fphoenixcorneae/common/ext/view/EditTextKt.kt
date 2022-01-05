package com.fphoenixcorneae.common.ext.view

import android.text.Editable
import android.text.InputFilter
import android.widget.EditText

/**
 * 删除多少位
 * @param limit 删除的位数
 */
fun Editable.limit(limit: Int = 1) {
    this.delete(this.length - limit, this.length)
}

/**
 * 保留小数点后面多少位数
 * @param limit 默认是保存小数点后 2 位数
 */
fun Editable.pointLimit(limit: Int = 2): Boolean {
    if (this.toString().contains(".")) {
        if (this.toString().length - 1 - this.toString().indexOf(".") > 2) {
            this.toString().subSequence(0, this.toString().indexOf(".") + 3).toString()
            return true
        }
    }
    return false
}

/**
 * 获取/设置[EditText]输入限制最大长度
 */
var EditText.maxLength: Int
    get() {
        val lengthFilter = filters.filterIsInstance<InputFilter.LengthFilter>().firstOrNull()
        return lengthFilter?.max ?: -1
    }
    set(value) {
        if (value > 0) {
            val filters = filters.toMutableList()

            val iterator = filters.iterator()
            while (iterator.hasNext()) {
                val it = iterator.next()

                if (it is InputFilter.LengthFilter) {
                    iterator.remove()
                    break
                }
            }

            filters.add(InputFilter.LengthFilter(value))
            this.filters = filters.toTypedArray()
        }
    }

/**
 * 获取/设置[EditText]text
 */
var EditText.textString: CharSequence
    get() = text.toString()
    set(value) {
        text = Editable.Factory.getInstance().newEditable(value)
    }