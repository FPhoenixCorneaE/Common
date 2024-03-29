package com.fphoenixcorneae.common.util.xtoast.listener

import com.fphoenixcorneae.common.util.xtoast.XToast

/**
 * Toast 显示销毁监听
 */
interface OnToastListener {
    /**
     * 显示监听
     */
    fun onShow(toast: XToast?)

    /**
     * 消失监听
     */
    fun onDismiss(toast: XToast?)
}