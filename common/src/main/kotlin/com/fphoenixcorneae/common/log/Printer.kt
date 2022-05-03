package com.fphoenixcorneae.common.log

/**
 * @desc：Printer
 * @date：2022/5/3 11:29
 */
interface Printer {
    fun v(tag: String?, message: String, vararg args: Any?)

    fun d(tag: String?, message: String, vararg args: Any?)

    fun i(tag: String?, message: String, vararg args: Any?)

    fun w(tag: String?, message: String, vararg args: Any?)

    fun e(tag: String?, message: String, vararg args: Any?)

    fun wtf(tag: String?, message: String, vararg args: Any?)

    fun json(tag: String?, message: String?)

    fun xml(tag: String?, message: String?)
}