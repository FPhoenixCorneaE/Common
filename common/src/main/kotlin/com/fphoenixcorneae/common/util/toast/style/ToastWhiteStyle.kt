package com.fphoenixcorneae.common.util.toast.style

/**
 * 白色样式实现
 */
class ToastWhiteStyle : ToastBlackStyle() {
    override val backgroundColor: Int
        get() = -0x151516

    override val textColor: Int
        get() = -0x45000000
}