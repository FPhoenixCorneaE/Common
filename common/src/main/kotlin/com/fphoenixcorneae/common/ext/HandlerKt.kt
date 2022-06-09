package com.fphoenixcorneae.common.ext

import android.os.Handler
import android.os.Looper

val globalHandler by lazy { Handler(Looper.getMainLooper()) }

fun runOnUiThread(runnable: Runnable) {
    if (Looper.myLooper() == Looper.getMainLooper()) {
        runnable.run()
    } else {
        globalHandler.post(runnable)
    }
}

fun runOnUiThreadDelayed(
    delayMillis: Long,
    runnable: Runnable
) {
    globalHandler.postDelayed(runnable, delayMillis)
}