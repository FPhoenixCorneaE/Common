package com.fphoenixcorneae.common.ext

import android.os.Environment
import java.nio.charset.Charset
import java.util.*
import java.util.concurrent.Callable
import java.util.concurrent.ExecutionException
import java.util.concurrent.Executors

typealias OnCrashListener = (
    @ParameterName("crashInfo") String,
    @ParameterName("e") Throwable?,
) -> Unit

/**
 * 全局异常处理
 */
fun setupGlobalExceptionHandler(
    crashDirPath: String = "",
    onCrashListener: OnCrashListener? = null,
) {
    Thread.setDefaultUncaughtExceptionHandler { t, e ->
        val time = yyyyMMddHHmmssFormat.format(Date(System.currentTimeMillis()))
        val sb = StringBuilder()
        val head = "************* Log Head ****************" +
                "\nTime Of Crash      : " + time +
                "\nDevice Manufacturer: " + deviceManufacturer +
                "\nDevice Model       : " + deviceModel +
                "\nAndroid Version    : " + sdkVersionName +
                "\nAndroid SDK        : " + sdkVersionCode +
                "\nApp VersionName    : " + appVersionName +
                "\nApp VersionCode    : " + appVersionCode +
                "\n************* Log Head ****************\n\n"
        sb.append(head)
            .append(e.getFullStackTrace())
        val crashInfo = sb.toString()
        crashInfo.logd("CrashLog")
        val fullPath = "${getCrashDir(crashDirPath) ?: getDefaultCrashDir()}$time.txt"
        if (createOrExistsFile(fullPath)) {
            input2File(crashInfo, fullPath)
        } else {
            "create crash log file $fullPath failed!".logd("CrashLog")
        }
        onCrashListener?.invoke(crashInfo, e)
        Thread.getDefaultUncaughtExceptionHandler()?.uncaughtException(t, e)
    }
}

private fun input2File(input: String, filePath: String) {
    val submit = Executors.newSingleThreadExecutor()
        .submit(Callable {
            writeFileFromString(filePath = filePath, content = input, append = true, charset = Charset.defaultCharset())
        })
    try {
        if (submit.get()) {
            "write crash info to $filePath success!".logd("CrashLog")
            return
        }
    } catch (e: InterruptedException) {
        e.printStackTrace()
    } catch (e: ExecutionException) {
        e.printStackTrace()
    }
    "write crash info to $filePath failed!".logd("CrashLog")
}

private fun getCrashDir(crashDirPath: String) = if (crashDirPath.isSpace()) {
    null
} else {
    if (crashDirPath.endsWith(FILE_SEPARATOR!!)) crashDirPath else crashDirPath + FILE_SEPARATOR
}

private fun getDefaultCrashDir() = if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()
    && applicationContext.externalCacheDir != null
) {
    applicationContext.externalCacheDir.toString() + FILE_SEPARATOR + "crash" + FILE_SEPARATOR
} else {
    applicationContext.cacheDir.toString() + FILE_SEPARATOR + "crash" + FILE_SEPARATOR
}