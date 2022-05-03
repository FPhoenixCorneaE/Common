package com.fphoenixcorneae.common.demo

import com.fphoenixcorneae.common.log.Printer
import com.orhanobut.logger.Logger

/**
 * @desc：CustomPrinter
 * @date：2022/5/3 11:41
 */
class CustomPrinter : Printer {
    override fun v(tag: String?, message: String, vararg args: Any?) {
        Logger.t(tag).v(message, args)
    }

    override fun d(tag: String?, message: String, vararg args: Any?) {
        Logger.t(tag).d(message, args)
    }

    override fun i(tag: String?, message: String, vararg args: Any?) {
        Logger.t(tag).i(message, args)
    }

    override fun w(tag: String?, message: String, vararg args: Any?) {
        Logger.t(tag).w(message, args)
    }

    override fun e(tag: String?, message: String, vararg args: Any?) {
        Logger.t(tag).e(message, args)
    }

    override fun wtf(tag: String?, message: String, vararg args: Any?) {
        Logger.t(tag).wtf(message, args)
    }

    override fun json(tag: String?, message: String?) {
        Logger.t(tag).json(message)
    }

    override fun xml(tag: String?, message: String?) {
        Logger.t(tag).xml(message)
    }
}