package com.fphoenixcorneae.common.permission

/**
 * 权限回调
 */
interface PermissionsCallback {
    fun onGranted()

    fun onDenied(permissions: List<String>)

    fun onShowRationale(permissionsRequest: PermissionsRequest)

    fun onNeverAskAgain(permissions: List<String>)
}

/**
 * DSL implementation for [PermissionsCallback].
 */
class PermissionsCallbackDSL : PermissionsCallback {

    private var onGranted: () -> Unit = {}
    private var onDenied: (permissions: List<String>) -> Unit = {}
    private var onShowRationale: (request: PermissionsRequest) -> Unit = {}
    private var onNeverAskAgain: (permissions: List<String>) -> Unit = {}

    fun onGranted(func: () -> Unit) {
        onGranted = func
    }

    fun onDenied(func: (permissions: List<String>) -> Unit) {
        onDenied = func
    }

    fun onShowRationale(func: (permissionsRequest: PermissionsRequest) -> Unit) {
        onShowRationale = func
    }

    fun onNeverAskAgain(func: (permissions: List<String>) -> Unit) {
        onNeverAskAgain = func
    }

    override fun onGranted() {
        onGranted.invoke()
    }

    override fun onDenied(permissions: List<String>) {
        onDenied.invoke(permissions)
    }

    override fun onShowRationale(permissionsRequest: PermissionsRequest) {
        onShowRationale.invoke(permissionsRequest)
    }

    override fun onNeverAskAgain(permissions: List<String>) {
        onNeverAskAgain.invoke(permissions)
    }

}