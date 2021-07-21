package com.fphoenixcorneae.permission

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.fphoenixcorneae.ext.logd

const val TAG = "KtxPermissionFragment"

/** 定位权限 */
private val LOCATION = arrayOf(
    Manifest.permission.ACCESS_COARSE_LOCATION,
    Manifest.permission.ACCESS_FINE_LOCATION
)

/** 电话权限 */
private val PHONE =
    arrayOf(Manifest.permission.CALL_PHONE)

/** 读写存储权限 */
private val WRITE = arrayOf(
    Manifest.permission.READ_EXTERNAL_STORAGE,
    Manifest.permission.WRITE_EXTERNAL_STORAGE
)

/** 短信权限 */
private val SMS =
    arrayOf(Manifest.permission.SEND_SMS)

/** 相机权限，相机权限包括读写文件权限 */
private val CAMERA = arrayOf(
    Manifest.permission.CAMERA,
    Manifest.permission.WRITE_EXTERNAL_STORAGE,
    Manifest.permission.READ_EXTERNAL_STORAGE
)

/** 申请定位权限 */
fun FragmentActivity.requestLocationPermission(callbacks: (PermissionsCallbackDSL.() -> Unit)? = null) {
    request(permissions = LOCATION, callbacks = callbacks)
}

/** 申请电话权限 */
fun FragmentActivity.requestPhonePermission(callbacks: (PermissionsCallbackDSL.() -> Unit)? = null) {
    request(permissions = PHONE, callbacks = callbacks)
}

/** 申请读写权限 */
fun FragmentActivity.requestWritePermission(callbacks: (PermissionsCallbackDSL.() -> Unit)? = null) {
    request(permissions = WRITE, callbacks = callbacks)
}

/** 申请短信息权限 */
fun FragmentActivity.requestSmsPermission(callbacks: (PermissionsCallbackDSL.() -> Unit)? = null) {
    request(permissions = SMS, callbacks = callbacks)
}

/** 申请相机权限 */
fun FragmentActivity.requestCameraPermission(callbacks: (PermissionsCallbackDSL.() -> Unit)? = null) {
    request(permissions = CAMERA, callbacks = callbacks)
}

/** 申请权限 */
fun FragmentActivity.request(vararg permissions: String) {
    ActivityCompat.requestPermissions(this, permissions, 0XFF)
}

fun FragmentActivity.request(
    vararg permissions: String,
    callbacks: (PermissionsCallbackDSL.() -> Unit)? = null,
) {

    val permissionsCallback = PermissionsCallbackDSL().apply {
        callbacks?.invoke(this)
    }
    val requestCode = PermissionsMap.put(permissionsCallback)

    val needRequestPermissions = permissions.filter {
        "$it isGranted: ${isGranted(it)}  isRevoked: ${isRevoked(it)}".logd("requestPermissions")
        !isGranted(it) || isRevoked(it)
    }

    if (needRequestPermissions.isEmpty()) {
        permissionsCallback.onGranted()
    } else {
        val shouldShowRationalePermissions = mutableListOf<String>()
        val shouldNotShowRationalePermissions = mutableListOf<String>()
        for (permission in needRequestPermissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                shouldShowRationalePermissions.add(permission)
            } else {
                shouldNotShowRationalePermissions.add(permission)
            }
        }

        if (shouldShowRationalePermissions.isNotEmpty()) {
            permissionsCallback.onShowRationale(
                PermissionRequest(
                    getKtxPermissionFragment(this),
                    shouldShowRationalePermissions,
                    requestCode
                )
            )
        }

        if (shouldNotShowRationalePermissions.isNotEmpty()) {
            getKtxPermissionFragment(this).requestPermissionsByFragment(
                shouldNotShowRationalePermissions.toTypedArray(),
                requestCode
            )
        }
    }
}

private fun getKtxPermissionFragment(activity: FragmentActivity): KtxPermissionFragment {
    var fragment = activity.supportFragmentManager.findFragmentByTag(TAG)
    if (fragment == null) {
        fragment = KtxPermissionFragment()
        activity.supportFragmentManager.beginTransaction().add(fragment, TAG).commitNow()
    }
    return fragment as KtxPermissionFragment
}


fun Activity.isGranted(permission: String): Boolean {
    return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
        ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}

fun Activity.isRevoked(permission: String): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
        packageManager.isPermissionRevokedByPolicy(permission, packageName)
}