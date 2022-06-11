package com.fphoenixcorneae.common.permission

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commitNow
import androidx.lifecycle.lifecycleScope
import com.fphoenixcorneae.common.ext.logd
import kotlinx.coroutines.launch

private const val TAG = "CoroutinesPermissionsFragment"

/** 定位权限 */
private val LOCATION = listOf(
    Manifest.permission.ACCESS_COARSE_LOCATION,
    Manifest.permission.ACCESS_FINE_LOCATION
)

/** 电话权限 */
private val PHONE = listOf(
    Manifest.permission.CALL_PHONE
)

/** 读写存储权限 */
private val WRITE = listOf(
    Manifest.permission.READ_EXTERNAL_STORAGE,
    Manifest.permission.WRITE_EXTERNAL_STORAGE
)

/** 短信权限 */
private val SMS = listOf(
    Manifest.permission.SEND_SMS
)

/** 相机权限，相机权限包括读写文件权限 */
private val CAMERA = listOf(
    Manifest.permission.CAMERA,
    Manifest.permission.WRITE_EXTERNAL_STORAGE,
    Manifest.permission.READ_EXTERNAL_STORAGE
)

/** 申请定位权限 */
fun FragmentActivity.requestLocationPermission(
    shouldShowRationale: Boolean = true,
    callbacks: (PermissionsCallbackDSL.() -> Unit)? = null
) {
    requestPermissionsOnLifecycle(
        permissions = LOCATION,
        shouldShowRationale = shouldShowRationale,
        callbacks = callbacks
    )
}

/** 申请电话权限 */
fun FragmentActivity.requestPhonePermission(
    shouldShowRationale: Boolean = true,
    callbacks: (PermissionsCallbackDSL.() -> Unit)? = null
) {
    requestPermissionsOnLifecycle(
        permissions = PHONE,
        shouldShowRationale = shouldShowRationale,
        callbacks = callbacks
    )
}

/** 申请读写权限 */
fun FragmentActivity.requestWritePermission(
    shouldShowRationale: Boolean = true,
    callbacks: (PermissionsCallbackDSL.() -> Unit)? = null
) {
    requestPermissionsOnLifecycle(
        permissions = WRITE,
        shouldShowRationale = shouldShowRationale,
        callbacks = callbacks
    )
}

/** 申请短信息权限 */
fun FragmentActivity.requestSmsPermission(
    shouldShowRationale: Boolean = true,
    callbacks: (PermissionsCallbackDSL.() -> Unit)? = null
) {
    requestPermissionsOnLifecycle(
        permissions = SMS,
        shouldShowRationale = shouldShowRationale,
        callbacks = callbacks
    )
}

/** 申请相机权限 */
fun FragmentActivity.requestCameraPermission(
    shouldShowRationale: Boolean = true,
    callbacks: (PermissionsCallbackDSL.() -> Unit)? = null
) {
    requestPermissionsOnLifecycle(
        permissions = CAMERA,
        shouldShowRationale = shouldShowRationale,
        callbacks = callbacks
    )
}

fun Fragment.requestPermissionsOnLifecycle(
    permissions: List<String>,
    shouldShowRationale: Boolean = true,
    callbacks: (PermissionsCallbackDSL.() -> Unit)? = null,
) {
    requireActivity().requestPermissionsOnLifecycle(permissions, shouldShowRationale, callbacks)
}

fun FragmentActivity.requestPermissionsOnLifecycle(
    permissions: List<String>,
    shouldShowRationale: Boolean = true,
    callbacks: (PermissionsCallbackDSL.() -> Unit)? = null,
) =
    lifecycleScope.launch {
        supportFragmentManager.run {
            findFragmentByTag(TAG)?.let {
                commitNow(allowStateLoss = true) { remove(it) }
            }
            val requestPermissionsFragment = CoroutinesPermissionsFragment(
                permissions = permissions,
                shouldShowRationale = shouldShowRationale
            ).also {
                commitNow(allowStateLoss = true) { add(it, TAG) }
            }
            val permissionsCallback: PermissionsCallback = PermissionsCallbackDSL().apply {
                callbacks?.invoke(this)
            }
            requestPermissionsFragment.getPermissionsResultFlow().collect { result ->
                when (result) {
                    PermissionsResult.Granted -> {
                        "requestPermissionsOnLifecycle: onGranted".logd()
                        permissionsCallback.onGranted()
                    }
                    is PermissionsResult.Denied -> {
                        "requestPermissionsOnLifecycle: onDenied: ${result.permissions}".logd()
                        permissionsCallback.onDenied(result.permissions)
                    }
                    is PermissionsResult.ShowRationale -> {
                        "requestPermissionsOnLifecycle: onShowRationale: ${result.permissions}".logd()
                        permissionsCallback.onShowRationale(
                            result.permissions,
                            result.positive,
                            result.negative
                        )
                    }
                    is PermissionsResult.NeverAsk -> {
                        "requestPermissionsOnLifecycle: onNeverAsk: ${result.permissions}".logd()
                        permissionsCallback.onNeverAsk(result.permissions)
                    }
                    else -> {}
                }
            }
        }
    }

fun Context.isGranted(permission: String): Boolean {
    return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
            ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}

fun Context.isRevoked(permission: String): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
            packageManager.isPermissionRevokedByPolicy(permission, packageName)
}