package com.fphoenixcorneae.common.ext

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.SearchManager
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.provider.Settings
import android.telephony.SmsManager
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
import com.fphoenixcorneae.permissions.requestCameraPermission
import com.fphoenixcorneae.permissions.requestPermissionsOnLifecycle
import com.fphoenixcorneae.permissions.requestPhonePermission
import java.io.File
import java.util.*

/**
 * Return whether the intent is available.
 * @return `true`: yes<br></br>`false`: no
 */
@SuppressLint("QueryPermissionsNeeded")
fun Intent?.isAvailable(): Boolean =
    this?.run {
        applicationContext.packageManager
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
fun getInstallAppIntent(file: File, isNewTask: Boolean = true): Intent? {
    if (!file.exists()) {
        return null
    }
    val uri: Uri = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
        Uri.fromFile(file)
    } else {
        val authority = "$appPackageName.FileProvider"
        FileProvider.getUriForFile(applicationContext, authority, file)
    }
    return getInstallAppIntent(uri, isNewTask)
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
fun getInstallAppIntent(uri: Uri?, isNewTask: Boolean = true): Intent? {
    if (uri == null) {
        return null
    }
    val intent = Intent(Intent.ACTION_VIEW)
    val type = "application/vnd.android.package-archive"
    applicationContext.grantUriPermission(appPackageName, uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
    intent.setDataAndType(uri, type)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
    }
    return addIntentFlags(intent, isNewTask)
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
fun getLaunchAppIntent(pkgName: String?, isNewTask: Boolean = true): Intent? {
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
    return addIntentFlags(intent, isNewTask)
}

/**
 * Return the intent of launch app details settings.
 *
 * @param pkgName The name of the package.
 * @return the intent of launch app details settings
 */
fun getLaunchAppDetailsSettingsIntent(pkgName: String): Intent {
    return getLaunchAppDetailsSettingsIntent(pkgName, true)
}

/**
 * Return the intent of launch app details settings.
 *
 * @param pkgName The name of the package.
 * @return the intent of launch app details settings
 */
fun getLaunchAppDetailsSettingsIntent(pkgName: String, isNewTask: Boolean = true): Intent {
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
    isNewTask: Boolean = false,
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
fun goToAppInfoPage(packageName: String = appPackageName) {
    applicationContext.startActivity(getAppInfoIntent(packageName))
}

/**
 * 跳转到日期和时间页面
 */
fun goToDateAndTimePage() {
    applicationContext.startActivity(getDateAndTimeIntent())
}

/**
 * 跳转到语言设置页面
 */
fun goToLanguagePage() {
    applicationContext.startActivity(getLanguageIntent())
}

/**
 * 跳转到无障碍服务设置页面
 */
fun goToAccessibilitySetting() =
    Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).run {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        applicationContext.startActivity(this)
    }

/**
 * Search a word in a browser
 */
fun search(string: String?) {
    val intent = Intent(Intent.ACTION_WEB_SEARCH)
    intent.putExtra(SearchManager.QUERY, string)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    applicationContext.startActivity(intent)
}

/**
 * 浏览器打开指定网页
 */
fun openBrowser(url: String) {
    Intent(Intent.ACTION_VIEW, Uri.parse(url)).run {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        applicationContext.startActivity(this)
    }
}

/**
 * Open map in a map app
 * @param mapAppPkgName 地图软件包名
 */
fun openMap(mapAppPkgName: String?, parh: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(parh))
    intent.setPackage(mapAppPkgName)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    applicationContext.startActivity(intent)
}

/**
 * Open dial
 */
fun openDial() {
    val intent = Intent(Intent.ACTION_CALL_BUTTON)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    applicationContext.startActivity(intent)
}

/**
 * Open dial with a number
 */
fun openDial(number: String) {
    val uri = Uri.parse("tel:$number")
    val intent = Intent(Intent.ACTION_DIAL, uri)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    applicationContext.startActivity(intent)
}

/**
 * Call up, requires Permission "android.permission.CALL_PHONE"
 * 必须装有sim卡才可以跳转拨打电话界面
 */
@RequiresPermission(Manifest.permission.CALL_PHONE)
fun FragmentActivity.callPhone(
    telephoneNumber: String,
) {
    requestPhonePermission {
        onGranted {
            val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$telephoneNumber"))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
        onDenied {
        }
        onShowRationale { permissions, positive, negative ->
            AlertDialog.Builder(this@callPhone)
                .setTitle("权限申请")
                .setMessage("需要申请电话权限")
                .setCancelable(false)
                .setNegativeButton("取消") { dialog, _ ->
                    negative.invoke()
                    dialog.dismiss()
                }
                .setPositiveButton("确定") { dialog, _ ->
                    positive.invoke()
                    dialog.dismiss()
                }
                .show()
        }
        onNeverAsk {
            openApplicationDetailsSettings()
        }
    }
}

/**
 * Send message
 */
fun sendMessage(
    sendNo: String,
    sendContent: String?,
) {
    val uri = Uri.parse("smsto:$sendNo")
    val intent = Intent(Intent.ACTION_SENDTO, uri)
    intent.putExtra("sms_body", sendContent)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    applicationContext.startActivity(intent)
}

/**
 * Send Text Message
 * @param destinationAddress 接收方手机号码
 * @param textMessage 信息内容
 */
fun FragmentActivity.sendTextMessage(
    destinationAddress: String,
    textMessage: String,
) {
    requestPermissionsOnLifecycle(
        listOf(
            Manifest.permission.SEND_SMS,
            Manifest.permission.READ_PHONE_STATE
        )
    ) {
        onGranted {
            if (destinationAddress.isNotBlank()) {
                // 移动运营商允许每次发送的字节数据有限，我们可以使用Android给我们提供的短信工具
                val sms = SmsManager.getDefault()
                // 如果短信没有超过限制长度，则返回一个长度的List
                val texts = sms.divideMessage(textMessage)
                texts.forEach {
                    sms.sendTextMessage(destinationAddress, null, it, null, null)
                }
            }
        }
        onDenied {
        }
        onShowRationale { permissions, positive, negative ->
            AlertDialog.Builder(this@sendTextMessage)
                .setTitle("权限申请")
                .setMessage("需要申请短信与手机状态权限")
                .setCancelable(false)
                .setNegativeButton("取消") { dialog, _ ->
                    negative.invoke()
                    dialog.dismiss()
                }
                .setPositiveButton("确定") { dialog, _ ->
                    positive.invoke()
                    dialog.dismiss()
                }
                .show()
        }
        onNeverAsk {
            openApplicationDetailsSettings()
        }
    }
}

/**
 * Open contact person
 */
fun openContacts() {
    val intent = Intent(Intent.ACTION_VIEW, ContactsContract.Contacts.CONTENT_URI)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    applicationContext.startActivity(intent)
}

/**
 * Open system settings
 *
 * @param action The action contains global system-level device preferences.
 */
fun openSettings(action: String? = Settings.ACTION_SETTINGS) {
    val intent = Intent(action)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    applicationContext.startActivity(intent)
}

/**
 * 打开悬浮窗设置页面
 */
@SuppressLint("InlinedApi")
fun openSettingsCanDrawOverlays() {
    val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
    intent.data = Uri.parse("package:${appPackageName}")
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    applicationContext.startActivity(intent)
}

/**
 * 打开应用修改系统设置页面
 */
@SuppressLint("InlinedApi")
fun openApplicationManageWriteSettings() {
    val intent = Intent(
        Settings.ACTION_MANAGE_WRITE_SETTINGS,
        Uri.parse("package:${appPackageName}")
    )
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    applicationContext.startActivity(intent)
}

/**
 * Open App Detail page
 */
fun openApplicationDetailsSettings() {
    getLaunchAppDetailsSettingsIntent(appPackageName).also {
        applicationContext.startActivity(it)
    }
}

/**
 * Open camera
 */
fun FragmentActivity.openCamera() {
    requestCameraPermission {
        onGranted {
            startActivityByAction(MediaStore.ACTION_IMAGE_CAPTURE)
        }
        onDenied {

        }
        onShowRationale { permissions, positive, negative ->
            AlertDialog.Builder(this@openCamera)
                .setTitle("权限申请")
                .setMessage("需要申请相机权限")
                .setCancelable(false)
                .setNegativeButton("取消") { dialog, which ->
                    negative.invoke()
                    dialog.dismiss()
                }
                .setPositiveButton("确定") { dialog, which ->
                    positive.invoke()
                    dialog.dismiss()
                }
                .show()
        }
        onNeverAsk {
            openApplicationDetailsSettings()
        }
    }
}

/**
 * Take camera, this photo data will be returned in onActivityResult()
 */
fun Activity.takeCamera(requestCode: Int) {
    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    startActivityForResult(intent, requestCode)
}

/**
 * Choose photo, this photo data will be returned in onActivityResult()
 */
fun Activity.choosePhoto(requestCode: Int) {
    val intent = Intent(Intent.ACTION_GET_CONTENT)
    intent.type = "image/*"
    startActivityForResult(intent, requestCode)
}

/**
 * 在应用商店中打开应用
 */
fun openInAppStore(packageName: String = appPackageName) {
    val intent = Intent(Intent.ACTION_VIEW)
    try {
        intent.data = Uri.parse("market://details?id=$packageName")
    } catch (ifPlayStoreNotInstalled: ActivityNotFoundException) {
        intent.data = Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
    }
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    applicationContext.startActivity(intent)
}

/**
 * 根据包名启动 app
 */
fun openApp(packageName: String) =
    applicationContext.packageManager.getLaunchIntentForPackage(packageName)?.run {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        applicationContext.startActivity(this)
    }

/**
 * 安装 Apk
 * need android.permission.REQUEST_INSTALL_PACKAGES after N
 */
fun installApk(apkFile: File) {
    getInstallAppIntent(apkFile)?.run {
        applicationContext.startActivity(this)
    }
}

/**
 * 根据包名卸载 app
 * need android.permission.REQUEST_DELETE_PACKAGES after N
 */
fun uninstallApp(packageName: String) {
    getUninstallAppIntent(packageName).run {
        applicationContext.startActivity(this)
    }
}


