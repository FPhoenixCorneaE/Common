package com.fphoenixcorneae.common.ext

import android.app.Application
import android.text.TextUtils
import com.fphoenixcorneae.common.CommonInitializer

val applicationContext: Application by lazy { CommonInitializer.sApplication }

/**
 * 判断是否为空 并传入相关操作
 */
inline fun <reified T> T?.action(
    crossinline notNullAction: (T) -> Unit = {},
    crossinline nullAction: () -> Unit = {}
) {
    if (this != null) {
        notNullAction.invoke(this)
    } else {
        nullAction.invoke()
    }
}

/**
 * 判断任意一个对象是否为null
 */
fun Any?.isNull(): Boolean {
    return this == null
}

/**
 * 判断任意一个对象是否为非null
 */
fun Any?.isNotNull(): Boolean {
    return this != null
}

fun <T> Collection<T>?.isNotNullOrEmpty(): Boolean {
    return this.isNullOrEmpty().not()
}

/**
 * 字符序列比较
 */
fun CharSequence?.equals(charSequence: CharSequence?): Boolean {
    return TextUtils.equals(this, charSequence)
}




