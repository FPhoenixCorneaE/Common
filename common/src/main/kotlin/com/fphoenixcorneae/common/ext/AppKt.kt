package com.fphoenixcorneae.common.ext

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Process
import java.io.ByteArrayInputStream
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import javax.security.auth.x500.X500Principal
import kotlin.system.exitProcess

private val DEBUG_DN = X500Principal("CN=Android Debug, O=Android, C=US")

/**
 * Get package name
 */
val appPackageName: String
    get() =
        applicationContext.packageName

/**
 * Get version name
 */
val appVersionName: String
    get() =
        runCatching {
            applicationContext.packageManager.getPackageInfo(appPackageName, 0).versionName
        }.onFailure {
            it.printStackTrace()
        }.getOrDefault("")

/**
 * Get version code
 */
val appVersionCode: Long
    get() =
        runCatching {
            applicationContext.packageManager.getPackageInfo(appPackageName, 0).run {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    longVersionCode
                } else {
                    versionCode.toLong()
                }
            }
        }.onFailure {
            it.printStackTrace()
        }.getOrDefault(0)

/**
 * Get icon
 */
val appIcon: Drawable?
    get() =
        runCatching {
            applicationContext.packageManager.run {
                getApplicationInfo(appPackageName, 0).loadIcon(this)
            }
        }.onFailure {
            it.printStackTrace()
        }.getOrNull()

/**
 * Get app name
 */
val appName: String
    get() =
        runCatching {
            applicationContext.packageManager.run {
                getApplicationInfo(appPackageName, 0).loadLabel(this).toString()
            }
        }.onFailure {
            it.printStackTrace()
        }.getOrDefault("")

/**
 * Get app permission
 */
val appPermissions: Array<String>?
    get() =
        runCatching {
            applicationContext.packageManager
                .getPackageInfo(appPackageName, PackageManager.GET_PERMISSIONS)
                .requestedPermissions
        }.onFailure {
            it.printStackTrace()
        }.getOrNull()

/**
 * Get app signature
 */
val appSignature: String
    @SuppressLint("PackageManagerGetSignatures")
    get() =
        runCatching {
            applicationContext.packageManager
                .getPackageInfo(appPackageName, PackageManager.GET_SIGNATURES)
                .signatures[0]
                .toString()
        }.onFailure {
            it.printStackTrace()
        }.getOrDefault("")


/**
 * Return the application's signature for SHA1 value.
 */
val appSignatureSHA1: String
    get() =
        getAppSignatureHash(appPackageName, "SHA1")

/**
 * Return the application's signature for SHA256 value.
 */
val appSignatureSHA256: String
    get() =
        getAppSignatureHash(appPackageName, "SHA256")

/**
 * Return the application's signature for MD5 value.
 */
val appSignatureMD5: String
    get() =
        getAppSignatureHash(appPackageName, "MD5")

/**
 * Return the application's user-ID.
 * @return the application's signature for MD5 value
 */
val appUid: Int
    get() =
        runCatching {
            applicationContext.packageManager.getApplicationInfo(appPackageName, 0).uid
        }.onFailure {
            it.printStackTrace()
        }.getOrDefault(-1)

/**
 * Return the application's path.
 */
val appPath: String
    get() =
        runCatching {
            applicationContext.packageManager.getPackageInfo(appPackageName, 0).applicationInfo.sourceDir
        }.onFailure {
            it.printStackTrace()
        }.getOrDefault("")

/**
 * Judge whether an app is debugDN
 */
val isDebugDN: Boolean
    @SuppressLint("PackageManagerGetSignatures")
    get() {
        var debuggable = false
        try {
            val packageInfo = applicationContext.packageManager.getPackageInfo(
                appPackageName,
                PackageManager.GET_SIGNATURES
            )
            val signatures = packageInfo.signatures
            for (signature in signatures) {
                val cf = CertificateFactory.getInstance("X.509")
                val stream = ByteArrayInputStream(signature.toByteArray())
                val cert = cf.generateCertificate(stream) as X509Certificate
                debuggable = cert.subjectX500Principal == DEBUG_DN
                if (debuggable) {
                    break
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return debuggable
    }

/**
 * Judge whether an app is debuggable
 */
val isDebuggable: Boolean
    get() =
        applicationContext.applicationInfo != null
                && (applicationContext.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0

/**
 * Return whether it is a system application.
 *
 * @param pkgName The name of the package.
 * @return `true`: yes<br></br>`false`: no
 */
fun isSystemApp(pkgName: String? = appPackageName): Boolean {
    return if (pkgName.isNullOrBlank()) {
        false
    } else {
        try {
            val ai = applicationContext.packageManager.getApplicationInfo(pkgName, 0)
            ai.flags and ApplicationInfo.FLAG_SYSTEM != 0
        } catch (e: NameNotFoundException) {
            e.printStackTrace()
            false
        }
    }
}

/**
 * Judge whether an app is in background
 */
val isAppInBackground: Boolean
    @TargetApi(Build.VERSION_CODES.Q)
    get() =
        applicationContext.activityManager
            ?.getRunningTasks(1)
            ?.getOrNull(0)
            ?.topActivity
            ?.packageName != appPackageName

/**
 * Judge whether an app is in foreground
 */
val isAppInForeground: Boolean
    get() {
        val info = applicationContext.activityManager?.runningAppProcesses
        if (info == null || info.size == 0) {
            return false
        }
        for (aInfo in info) {
            if (aInfo.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                if (aInfo.processName == appPackageName) {
                    return true
                }
            }
        }
        return false
    }

/**
 * Return whether application is foreground.
 *
 * Target APIs greater than 21 must hold
 * `<uses-permission android:name="android.permission.PACKAGE_USAGE_STATS" />`
 *
 * @param packageName The name of the package.
 * @return `true`: yes<br></br>`false`: no
 */
fun isAppInForeground(packageName: String): Boolean {
    return packageName.isNotBlank() && packageName == foregroundProcessName
}

/**
 * Return whether application is running.
 *
 * @param pkgName The name of the package.
 * @return `true`: yes<br></br>`false`: no
 */
@SuppressLint("NewApi")
fun isAppRunning(pkgName: String): Boolean {
    val context = applicationContext
    val packageManager: PackageManager = context.packageManager
    val uid = try {
        val ai = packageManager.getApplicationInfo(pkgName, 0)
        ai.uid
    } catch (e: NameNotFoundException) {
        e.printStackTrace()
        return false
    }
    val am = applicationContext.activityManager
    val taskInfo = am?.getRunningTasks(Int.MAX_VALUE)
    if (taskInfo != null && taskInfo.size > 0) {
        for (aInfo in taskInfo) {
            if (pkgName == aInfo.baseActivity!!.packageName) {
                return true
            }
        }
    }
    val serviceInfo = am?.getRunningServices(Int.MAX_VALUE)
    if (serviceInfo != null && serviceInfo.size > 0) {
        for (aInfo in serviceInfo) {
            if (uid == aInfo.uid) {
                return true
            }
        }
    }
    return false
}

/**
 * Launch the application.
 *
 * @param pkgName The name of the package.
 */
fun launchApp(pkgName: String = appPackageName, requestCode: Int) {
    if (pkgName.isSpace()) {
        return
    }
    val launchAppIntent: Intent? = getLaunchAppIntent(pkgName)
    if (launchAppIntent.isNull()) {
        "Launcher activity isn't exist.".loge()
        return
    }
    applicationContext.startActivity(launchAppIntent)
}

/**
 * Launch the application.
 *
 * @param pkgName The name of the package.
 * @param requestCode If &gt;= 0, this code will be returned in
 * onActivityResult() when the activity exits.
 */
fun Activity.launchApp(
    pkgName: String,
    requestCode: Int
) {
    if (pkgName.isSpace()) {
        return
    }
    val launchAppIntent: Intent? = getLaunchAppIntent(pkgName)
    if (launchAppIntent.isNull()) {
        "Launcher activity isn't exist.".loge()
        return
    }
    startActivityForResult(launchAppIntent, requestCode)
}

/**
 * Relaunch the application.
 *
 * @param isKillProcess True to kill the process, false otherwise.
 */
fun relaunchApp(isKillProcess: Boolean = true) {
    val intent = getLaunchAppIntent(appPackageName)
    if (intent.isNull()) {
        "Launcher activity isn't exist.".loge()
        return
    }
    intent!!.addFlags(
        Intent.FLAG_ACTIVITY_NEW_TASK
                or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
    )
    applicationContext.startActivity(intent)
    if (isKillProcess) {
        Process.killProcess(Process.myPid())
        exitProcess(0)
    }
}

/**
 * Exit the application.
 */
fun exitApp() {
    for (i in activityList.indices.reversed()) {
        // remove from top
        val activity = activityList[i]
        // sActivityList remove the index activity at onActivityDestroyed
        activity.finish()
    }
    exitProcess(0)
}

/**
 * 获取应用运行的最大内存
 *
 * @return 最大内存
 */
val maxMemory: Long
    get() = Runtime.getRuntime().maxMemory() / 1024

/**
 * 获取设备的可用内存大小
 *
 * @return 当前内存大小
 */
fun getDeviceUsableMemory(): Int {
    val memoryInfo = ActivityManager.MemoryInfo()
    applicationContext.activityManager?.getMemoryInfo(memoryInfo)
    // 返回当前系统的可用内存
    return (memoryInfo.availMem / (1024 * 1024)).toInt()
}

/**
 * 获取应用签名
 * @return 返回应用的签名
 */
@SuppressLint("PackageManagerGetSignatures")
fun getAppSignatureHexString(): String {
    return hexDigest(appSignature.toByteArray())
}

/**
 * 将签名字符串转换成需要的32位签名
 *
 * @param paramArrayOfByte 签名byte数组
 * @return 32位签名字符串
 */
private fun hexDigest(paramArrayOfByte: ByteArray): String {
    val hexDigits = charArrayOf(
        48.toChar(),
        49.toChar(),
        50.toChar(),
        51.toChar(),
        52.toChar(),
        53.toChar(),
        54.toChar(),
        55.toChar(),
        56.toChar(),
        57.toChar(),
        97.toChar(),
        98.toChar(),
        99.toChar(),
        100.toChar(),
        101.toChar(),
        102.toChar()
    )
    try {
        val localMessageDigest = MessageDigest.getInstance("MD5")
        localMessageDigest.update(paramArrayOfByte)
        val arrayOfByte = localMessageDigest.digest()
        val arrayOfChar = CharArray(32)
        var i = 0
        var j = 0
        while (true) {
            if (i >= 16) {
                return String(arrayOfChar)
            }
            val k = arrayOfByte[i].toInt()
            arrayOfChar[j] = hexDigits[0xF and k.ushr(4)]
            arrayOfChar[++j] = hexDigits[k and 0xF]
            i++
            j++
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return ""
}

private fun getAppSignatureHash(
    packageName: String,
    algorithm: String
): String {
    if (packageName.isSpace()) {
        return ""
    }
    val signature = appSignature
    return if (signature.isEmpty()) {
        ""
    } else {
        hashTemplate(signature.toByteArray(), algorithm).toHexString()
            .replace("(?<=[0-9A-F]{2})[0-9A-F]{2}".toRegex(), ":$0")
    }
}

private fun hashTemplate(
    data: ByteArray?,
    algorithm: String
): ByteArray? {
    return when {
        data.isNull() || data!!.isEmpty() -> null
        else -> try {
            val md =
                MessageDigest.getInstance(algorithm)
            md.update(data)
            md.digest()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            null
        }
    }
}

