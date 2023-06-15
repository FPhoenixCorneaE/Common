package com.fphoenixcorneae.common.log

import android.util.Log
import com.fphoenixcorneae.common.ext.isDebuggable

/**
 * @desc：DefaultPrinter
 * @date：2022/5/3 11:33
 */
class DefaultPrinter : Printer {

    override fun d(tag: String?, message: String, vararg args: Any?) {
        if (isDebuggable) {
            Log.d(tag, if (args.isEmpty()) message else String.format(message, args))
        }
    }

    override fun v(tag: String?, message: String, vararg args: Any?) {
        if (isDebuggable) {
            Log.v(tag, if (args.isEmpty()) message else String.format(message, args))
        }
    }

    override fun i(tag: String?, message: String, vararg args: Any?) {
        if (isDebuggable) {
            Log.i(tag, if (args.isEmpty()) message else String.format(message, args))
        }
    }

    override fun w(tag: String?, message: String, vararg args: Any?) {
        if (isDebuggable) {
            Log.w(tag, if (args.isEmpty()) message else String.format(message, args))
        }
    }

    override fun e(tag: String?, message: String, vararg args: Any?) {
        if (isDebuggable) {
            Log.e(tag, if (args.isEmpty()) message else String.format(message, args))
        }
    }

    override fun wtf(tag: String?, message: String, vararg args: Any?) {
        if (isDebuggable) {
            Log.wtf(tag, if (args.isEmpty()) message else String.format(message, args))
        }
    }

    override fun json(tag: String?, message: String?) {
        if (isDebuggable) {
            Log.d(tag, "$message")
        }
    }

    override fun xml(tag: String?, message: String?) {
        if (isDebuggable) {
            Log.d(tag, "$message")
        }
    }
}