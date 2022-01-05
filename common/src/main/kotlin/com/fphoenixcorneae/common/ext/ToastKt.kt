package com.fphoenixcorneae.common.ext

import android.view.Gravity
import androidx.annotation.StringRes
import com.fphoenixcorneae.common.util.toast.ToastUtil
import com.fphoenixcorneae.common.util.toast.style.ToastAliPayStyle
import com.fphoenixcorneae.common.util.toast.style.ToastBlackStyle
import com.fphoenixcorneae.common.util.toast.style.ToastQQStyle
import com.fphoenixcorneae.common.util.toast.style.ToastWhiteStyle

/**
 * 吐司显示字符串
 */
fun toast(content: CharSequence?) {
    ToastUtil.show(content)
}

/**
 * 吐司显示字符串
 */
fun toast(@StringRes id: Int) {
    ToastUtil.show(id)
}

/**
 * 支付宝样式显示字符串
 */
fun toastAliPayStyle(content: CharSequence?) {
    ToastUtil.initStyle(ToastAliPayStyle())
    ToastUtil.show(content)
}

/**
 * 支付宝样式显示字符串
 */
fun toastAliPayStyle(@StringRes id: Int) {
    ToastUtil.initStyle(ToastAliPayStyle())
    ToastUtil.show(id)
}

/**
 * 黑色样式显示字符串
 */
fun toastBlackStyle(content: CharSequence?) {
    ToastUtil.initStyle(ToastBlackStyle())
    ToastUtil.show(content)
}

/**
 * 黑色样式显示字符串
 */
fun toastBlackStyle(@StringRes id: Int) {
    ToastUtil.initStyle(ToastBlackStyle())
    ToastUtil.show(id)
}

/**
 * 白色样式显示字符串
 */
fun toastWhiteStyle(content: CharSequence?) {
    ToastUtil.initStyle(ToastWhiteStyle())
    ToastUtil.show(content)
}

/**
 * 白色样式显示字符串
 */
fun toastWhiteStyle(@StringRes id: Int) {
    ToastUtil.initStyle(ToastWhiteStyle())
    ToastUtil.show(id)
}

/**
 * QQ 样式显示字符串
 */
fun toastQQStyle(content: CharSequence?) {
    ToastUtil.initStyle(ToastQQStyle())
    ToastUtil.show(content)
}

/**
 * QQ 样式显示字符串
 */
fun toastQQStyle(@StringRes id: Int) {
    ToastUtil.initStyle(ToastQQStyle())
    ToastUtil.show(id)
}

/**
 * 吐司显示字符串在屏幕中间
 */
fun toastInCenter(content: CharSequence?) {
    ToastUtil.setGravity(Gravity.CENTER, 0, 0)
    ToastUtil.show(content)
}

/**
 * 吐司显示字符串在屏幕中间
 */
fun toastInCenter(@StringRes id: Int) {
    ToastUtil.setGravity(Gravity.CENTER, 0, 0)
    ToastUtil.show(id)
}


