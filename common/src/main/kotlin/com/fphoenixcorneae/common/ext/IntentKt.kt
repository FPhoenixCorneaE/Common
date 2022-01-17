package com.fphoenixcorneae.common.ext

import android.Manifest
import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import androidx.annotation.RequiresPermission
import androidx.core.content.FileProvider
import java.io.File
import java.util.*

/**
 * Return whether the intent is available.
 * @return `true`: yes<br></br>`false`: no
 */
@SuppressLint("QueryPermissionsNeeded")
fun Intent?.isAvailable(): Boolean =
    this?.run {
        appContext.packageManager
            ?.queryIntentActivities(this, PackageManager.MATCH_DEFAULT_ONLY)
            ?.isNotEmpty()
    } ?: false

/**
 * Return the intent of install app.
 *
 * Target APIs greater than 25 must hold
 * `<uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />`
 *
 * @param file The file.
 * @return the intent of install app
 */
@SuppressLint("InlinedApi")
@RequiresPermission(Manifest.permission.REQUEST_INSTALL_PACKAGES)
fun getInstallAppIntent(file: File): Intent? {
    if (!file.exists()) {
        return null
    }
    val uri: Uri = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
        Uri.fromFile(file)
    } else {
        val authority = "$appPackageName.FileProvider"
        FileProvider.getUriForFile(appContext, authority, file)
    }
    return getInstallAppIntent(uri)
}

/**
 * Return the intent of install app.
 *
 * Target APIs greater than 25 must hold
 * `<uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />`
 *
 * @param uri The uri.
 * @return the intent of install app
 */
@SuppressLint("InlinedApi")
@RequiresPermission(Manifest.permission.REQUEST_INSTALL_PACKAGES)
fun getInstallAppIntent(uri: Uri?): Intent? {
    if (uri == null) {
        return null
    }
    val intent = Intent(Intent.ACTION_VIEW)
    val type = "application/vnd.android.package-archive"
    intent.setDataAndType(uri, type)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
    }
    return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
}

/**
 * Return the intent of uninstall app.
 *
 * Target APIs greater than 25 must hold
 * Must hold `<uses-permission android:name="android.permission.REQUEST_DELETE_PACKAGES" />`
 *
 * @param pkgName The name of the package.
 * @return the intent of uninstall app
 */
@SuppressLint("InlinedApi")
@RequiresPermission(Manifest.permission.REQUEST_DELETE_PACKAGES)
fun getUninstallAppIntent(pkgName: String): Intent {
    val intent = Intent(Intent.ACTION_DELETE)
    intent.data = Uri.parse("package:$pkgName")
    return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
}

/**
 * Return the intent of launch app.
 *
 * @param pkgName The name of the package.
 * @return the intent of launch app
 */
fun getLaunchAppIntent(pkgName: String?): Intent? {
    if (pkgName == null) {
        return null
    }
    val launcherActivity: String = getLauncherActivity(pkgName)
    if (launcherActivity.isSpace()) {
        return null
    }
    val intent = Intent(Intent.ACTION_MAIN)
    intent.addCategory(Intent.CATEGORY_LAUNCHER)
    intent.setClassName(pkgName, launcherActivity)
    return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
}

/**
 * Return the intent of launch app details settings.
 *
 * @param pkgName The name of the package.
 * @return the intent of launch app details settings
 */
fun getLaunchAppDetailsSettingsIntent(pkgName: String): Intent {
    return getLaunchAppDetailsSettingsIntent(pkgName, false)
}

/**
 * Return the intent of launch app details settings.
 *
 * @param pkgName The name of the package.
 * @return the intent of launch app details settings
 */
fun getLaunchAppDetailsSettingsIntent(pkgName: String, isNewTask: Boolean): Intent {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    intent.data = Uri.parse("package:$pkgName")
    return addIntentFlags(intent, isNewTask)
}

/**
 * Return the intent of share text.
 *
 * @param content The content.
 * @return the intent of share text
 */
fun getShareTextIntent(content: String?): Intent {
    var intent = Intent(Intent.ACTION_SEND)
    intent.type = "text/plain"
    intent.putExtra(Intent.EXTRA_TEXT, content)
    intent = Intent.createChooser(intent, "")
    return addIntentFlags(intent, true)
}

/**
 * Return the intent of share image.
 *
 * @param content   The content.
 * @param imageFile The file of image.
 * @return the intent of share image
 */
fun getShareTextImageIntent(imageFile: File?, content: String? = ""): Intent {
    return getShareTextImageIntent(imageFile.toUri(), content)
}

/**
 * Return the intent of share image.
 *
 * @param content  The content.
 * @param imageUri The uri of image.
 * @return the intent of share image
 */
fun getShareTextImageIntent(imageUri: Uri?, content: String? = ""): Intent {
    var intent = Intent(Intent.ACTION_SEND)
    intent.putExtra(Intent.EXTRA_TEXT, content)
    intent.putExtra(Intent.EXTRA_STREAM, imageUri)
    intent.type = "image/*"
    intent = Intent.createChooser(intent, "")
    return addIntentFlags(intent, true)
}

/**
 * Return the intent of share images.
 *
 * @param content The content.
 * @param images  The files of images.
 * @return the intent of share images
 */
fun getShareTextImageIntent(images: List<File?>?, content: String? = ""): Intent {
    val uris = ArrayList<Uri>()
    if (images != null) {
        for (image in images) {
            val uri = image.toUri()
            if (uri != null) {
                uris.add(uri)
            }
        }
    }
    return getShareTextImageIntent(uris, content)
}

/**
 * Return the intent of share images.
 *
 * @param content The content.
 * @param uris    The uris of image.
 * @return the intent of share image
 */
fun getShareTextImageIntent(uris: ArrayList<Uri>?, content: String? = ""): Intent {
    var intent = Intent(Intent.ACTION_SEND_MULTIPLE)
    intent.putExtra(Intent.EXTRA_TEXT, content)
    intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris)
    intent.type = "image/*"
    intent = Intent.createChooser(intent, "")
    return addIntentFlags(intent, true)
}

/**
 * Return the intent of component.
 *
 * @param pkgName   The name of the package.
 * @param className The name of class.
 * @param bundle    The Bundle of extras to add to this intent.
 * @param isNewTask True to add flag of new task, false otherwise.
 * @return the intent of component
 */
fun getComponentIntent(
    pkgName: String,
    className: String,
    bundle: Bundle? = null,
    isNewTask: Boolean = false
): Intent {
    val intent = Intent()
    if (bundle != null) intent.putExtras(bundle)
    val cn = ComponentName(pkgName, className)
    intent.component = cn
    return addIntentFlags(intent, isNewTask)
}

/**
 * Return the intent of shutdown.
 *
 * Requires root permission
 * or hold `android:sharedUserId="android.uid.system"`,
 * `<uses-permission android:name="android.permission.SHUTDOWN" />`
 * in manifest.
 *
 * @return the intent of shutdown
 */
fun getShutdownIntent(): Intent {
    val intent: Intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        Intent("com.android.internal.intent.action.REQUEST_SHUTDOWN")
    } else {
        Intent("android.intent.action.ACTION_REQUEST_SHUTDOWN")
    }
    intent.putExtra("android.intent.extra.KEY_CONFIRM", false)
    return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
}

/**
 * Return the intent of dial.
 *
 * @param phoneNumber The phone number.
 * @return the intent of dial
 */
fun getDialIntent(phoneNumber: String): Intent {
    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Uri.encode(phoneNumber)))
    return addIntentFlags(intent, true)
}

/**
 * Return the intent of call.
 *
 * Must hold `<uses-permission android:name="android.permission.CALL_PHONE" />`
 *
 * @param phoneNumber The phone number.
 * @return the intent of call
 */
@RequiresPermission(Manifest.permission.CALL_PHONE)
fun getCallIntent(phoneNumber: String): Intent {
    val intent = Intent("android.intent.action.CALL", Uri.parse("tel:" + Uri.encode(phoneNumber)))
    return addIntentFlags(intent, true)
}

/**
 * Return the intent of send SMS.
 *
 * @param phoneNumber The phone number.
 * @param content     The content of SMS.
 * @return the intent of send SMS
 */
fun getSendSmsIntent(phoneNumber: String, content: String?): Intent {
    val uri = Uri.parse("smsto:" + Uri.encode(phoneNumber))
    val intent = Intent(Intent.ACTION_SENDTO, uri)
    intent.putExtra("sms_body", content)
    return addIntentFlags(intent, true)
}

/**
 * Return the intent of capture.
 *
 * @param outUri    The uri of output.
 * @param isNewTask True to add flag of new task, false otherwise.
 * @return the intent of capture
 */
fun getCaptureIntent(outUri: Uri?, isNewTask: Boolean = false): Intent {
    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    intent.putExtra(MediaStore.EXTRA_OUTPUT, outUri)
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    return addIntentFlags(intent, isNewTask)
}

/**
 * Return the intent of language.
 */
fun getLanguageIntent(): Intent =
    Intent(Settings.ACTION_LOCALE_SETTINGS).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
        putExtra("packageName", appPackageName)
    }

/**
 * Return the intent of app info.
 */
fun getAppInfoIntent(packageName: String = appPackageName): Intent =
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
    }

/**
 * Return the intent of date and time.
 */
fun getDateAndTimeIntent(): Intent =
    Intent(Settings.ACTION_DATE_SETTINGS).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
        putExtra("packageName", appPackageName)
    }

private fun addIntentFlags(intent: Intent, isNewTask: Boolean): Intent {
    return if (isNewTask) intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) else intent
}

/**
 * 跳转到应用信息页面
 */
fun Context.goToAppInfoPage(packageName: String = this.packageName) {
    startActivity(getAppInfoIntent(packageName))
}

/**
 * 跳转到日期和时间页面
 */
fun Context.goToDateAndTimePage() {
    startActivity(getDateAndTimeIntent())
}

/**
 * 跳转到语言设置页面
 */
fun Context.goToLanguagePage() {
    startActivity(getLanguageIntent())
}

/**
 * 安装 Apk
 * need android.permission.REQUEST_INSTALL_PACKAGES after N
 */
fun Context.installApk(apkFile: File) {
    getInstallAppIntent(apkFile)?.run {
        startActivity(this)
    }
}

/**
 * 跳转到无障碍服务设置页面
 */
fun Context.goToAccessibilitySetting() =
    Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).run {
    startActivity(this)
}

/**
 * 浏览器打开指定网页
 */
fun Context.openBrowser(url: String) {
    Intent(Intent.ACTION_VIEW, Uri.parse(url)).run {
        startActivity(this)
    }
}

/**
 * 在应用商店中打开应用
 */
fun Context.openInAppStore(packageName: String = this.packageName) {
    val intent = Intent(Intent.ACTION_VIEW)
    try {
        intent.data = Uri.parse("market://details?id=$packageName")
        startActivity(intent)
    } catch (ifPlayStoreNotInstalled: ActivityNotFoundException) {
        intent.data =
            Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
        startActivity(intent)
    }
}

/**
 * 根据包名启动 app
 */
fun Context.openApp(packageName: String) =
    packageManager.getLaunchIntentForPackage(packageName)?.run {
        startActivity(this)
    }

/**
 * 根据包名卸载 app
 */
fun Context.uninstallApp(packageName: String) {
    getUninstallAppIntent(packageName).run {
        data = Uri.parse("package:$packageName")
        startActivity(this)
    }
}


