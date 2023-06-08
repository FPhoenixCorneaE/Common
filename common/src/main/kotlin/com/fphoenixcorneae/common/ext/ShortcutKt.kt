package com.fphoenixcorneae.common.ext

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ShortcutInfo
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresPermission
import androidx.fragment.app.FragmentActivity

/**
 * 检测是否存在快捷键
 *
 * @param activity     Activity
 * @param shortcutName 快捷方式的名称
 * @return 是否存在桌面图标
 */
@SuppressLint("ObsoleteSdkInt", "Recycle")
fun hasShortcut(activity: FragmentActivity, shortcutName: String): Boolean {
    var isInstallShortcut = false
    val cr = activity.contentResolver
    val uriStr = StringBuilder()
    uriStr.append("content://")
    var permission = "com.android.launcher.permission.READ_SETTINGS"
    var authority = getAuthorityFromPermission(
        activity,
        permission
    )
    if (authority.isEmpty()) {
        // 需要把这里的权限添加到清单配置文件中,否则查询失败
        permission = getCurrentLauncherPackageName(activity) + ".permission.READ_SETTINGS"
        authority = getAuthorityFromPermission(
            activity,
            permission
        )
    }
    if (authority.isEmpty()) {
        when {
            Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO -> {
                uriStr.append("com.android.launcher.settings")
            }
            Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT -> {
                uriStr.append("com.android.launcher2.settings")
            }
            else -> {
                uriStr.append("com.android.launcher3.settings")
            }
        }
    } else {
        uriStr.append(authority)
    }
    uriStr.append("/favorites?notify=true")
    val contentUri = Uri.parse(uriStr.toString())
    var cursor: Cursor? = null
    try {
        cursor = cr.query(
            contentUri,
            arrayOf("title", "iconResource"),
            "title=?",
            arrayOf(shortcutName.trim { it <= ' ' }),
            null
        )
        if (cursor != null && cursor.count > 0) {
            isInstallShortcut = true
        }
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        cursor.closeQuietly()
    }
    return isInstallShortcut
}

/**
 * get the current Launcher's Package Name
 */
private fun getCurrentLauncherPackageName(context: Context): String {
    val intent = Intent(Intent.ACTION_MAIN)
    intent.addCategory(Intent.CATEGORY_HOME)
    val res = context.packageManager.resolveActivity(intent, 0)
    return when {
        res?.activityInfo == null -> ""
        res.activityInfo.packageName == "android" -> ""
        else -> res.activityInfo.packageName
    }
}

@SuppressLint("QueryPermissionsNeeded")
private fun getAuthorityFromPermission(context: Context, permission: String): String {
    val packageInfoList = context.packageManager.getInstalledPackages(PackageManager.GET_PROVIDERS)
    packageInfoList.forEach { packageInfo ->
        val providerInfoArray = packageInfo.providers
        providerInfoArray?.forEach { providerInfo ->
            when (permission) {
                providerInfo.readPermission -> {
                    return providerInfo.authority
                }
                providerInfo.writePermission -> {
                    return providerInfo.authority
                }
            }
        }
    }
    return ""
}

/**
 * 为程序创建桌面快捷方式
 * 添加权限"com.android.launcher.permission.INSTALL_SHORTCUT"
 *
 * 注意!!!：
 * 经测试部分手机需要手动开启创建桌面快捷方式权限才能创建成功，而且无法在app里面去动态申请这个权限。
 * 需要在权限管理里边将桌面快捷方式权限打开
 *
 * @param activity     Activity
 * @param shortcutName 快捷方式的名称
 */
@SuppressLint("UnspecifiedImmutableFlag")
@RequiresPermission(Manifest.permission.INSTALL_SHORTCUT)
fun createShortcut(
    activity: FragmentActivity,
    shortcutName: String?,
    id: String = "",
    shortcutBitmap: Bitmap? = null,
    bundle: Bundle? = null,
) {
    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
            val shortcutManager = activity.shortcutManager
            if (shortcutManager?.isRequestPinShortcutSupported == true) {
                val shortcutIntent = Intent()
                shortcutIntent.component = ComponentName(activity, activity::class.java)
                // action 必须设置,不然报错
                shortcutIntent.action = Intent.ACTION_VIEW
                shortcutIntent.putExtras(bundle ?: Bundle())
                val pinShortcutInfo = ShortcutInfo.Builder(activity, id)
                    .setShortLabel(shortcutName.toString())
                    .setIcon(Icon.createWithBitmap(shortcutBitmap))
                    .setIntent(shortcutIntent)
                    .build()
                val pinnedShortcutCallbackIntent = shortcutManager.createShortcutResultIntent(pinShortcutInfo)
                // 当添加快捷方式的确认弹框弹出来时,将被回调
                val pendingIntent = PendingIntent.getBroadcast(
                    activity,
                    0,
                    pinnedShortcutCallbackIntent,
                    0
                )
                // 请求创建固定的快捷方式
                shortcutManager.requestPinShortcut(
                    pinShortcutInfo,
                    pendingIntent.intentSender
                )
            }
        }
        else -> {
            val shortcut = Intent("com.android.launcher.action.INSTALL_SHORTCUT")
            // 快捷方式的名称
            shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortcutName)
            // 快捷方式的图标
            shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON, shortcutBitmap)
            // 不允许重复创建
            shortcut.putExtra("duplicate", false)
            val shortcutIntent = Intent(Intent.ACTION_MAIN)
            shortcutIntent.setClassName(activity, activity.javaClass.name)
            // 添加category:CATEGORY_LAUNCHER 应用被卸载时快捷方式也随之删除
            shortcutIntent.addCategory(Intent.CATEGORY_LAUNCHER)
            shortcutIntent.putExtras(bundle ?: Bundle())
            shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent)
            activity.sendBroadcast(shortcut)
        }
    }
}

/**
 * 删除程序的快捷方式
 * 添加权限"com.android.launcher.permission.UNINSTALL_SHORTCUT"
 *
 * @param activity     Activity
 * @param shortcutName 快捷方式的名称
 */
@RequiresPermission(Manifest.permission.UNINSTALL_SHORTCUT)
fun deleteShortcut(activity: FragmentActivity, shortcutName: String?) {
    val shortcut = Intent("com.android.launcher.action.UNINSTALL_SHORTCUT")
    // 快捷方式的名称
    shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortcutName)
    val appClass = activity.packageName + "." + activity.localClassName
    val comp = ComponentName(activity.packageName, appClass)
    shortcut.putExtra(
        Intent.EXTRA_SHORTCUT_INTENT,
        Intent(Intent.ACTION_MAIN).setComponent(comp)
    )
    activity.sendBroadcast(shortcut)
}