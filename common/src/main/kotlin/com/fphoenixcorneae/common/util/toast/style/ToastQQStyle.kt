package com.fphoenixcorneae.common.util.toast.style

/**
 * QQ 样式实现
 */
class ToastQQStyle : BaseToastStyle() {
    override val z: Int
        get() = 0

    override val cornerRadius: Int
        get() = dp2px(4f)

    override val backgroundColor: Int
        get() = -0xcccccd

    override val textColor: Int
        get() = -0x1c1c1d

    override val textSize: Float
        get() = sp2px(12f).toFloat()

    override val paddingStart: Int
        get() = dp2px(16f)

    override val paddingTop: Int
        get() = dp2px(14f)
}