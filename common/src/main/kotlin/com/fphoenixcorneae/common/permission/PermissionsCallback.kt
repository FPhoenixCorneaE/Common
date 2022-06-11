package com.fphoenixcorneae.common.permission

internal typealias OnGranted = () -> Unit

internal typealias OnDenied = (
    @ParameterName("permissions") List<String>
) -> Unit

internal typealias OnShowRationale = (
    @ParameterName("permissions") List<String>,
    @ParameterName("positive") () -> Unit,
    @ParameterName("negative") () -> Unit
) -> Unit

internal typealias OnNeverAsk = (
    @ParameterName("permissions") List<String>
) -> Unit

/**
 * @desc：PermissionsCallback
 * @date：2022/6/11 18:37
 */
interface PermissionsCallback {
    fun onGranted()

    fun onDenied(permissions: List<String>)

    fun onShowRationale(permissions: List<String>, positive: () -> Unit, negative: () -> Unit)

    fun onNeverAsk(permissions: List<String>)
}

/**
 * @desc：DSL implementation for [PermissionsCallback].
 * @date：2022/6/11 18:37
 */
class PermissionsCallbackDSL : PermissionsCallback {

    private var onGranted: OnGranted = {}
    private var onDenied: OnDenied = {}
    private var onShowRationale: OnShowRationale = { _, _, _ -> }
    private var onNeverAsk: OnNeverAsk = {}

    fun onGranted(func: OnGranted) {
        onGranted = func
    }

    fun onDenied(func: OnDenied) {
        onDenied = func
    }

    fun onShowRationale(func: OnShowRationale) {
        onShowRationale = func
    }

    fun onNeverAsk(func: OnNeverAsk) {
        onNeverAsk = func
    }

    override fun onGranted() {
        onGranted.invoke()
    }

    override fun onDenied(permissions: List<String>) {
        onDenied.invoke(permissions)
    }

    override fun onShowRationale(
        permissions: List<String>,
        positive: () -> Unit,
        negative: () -> Unit
    ) {
        onShowRationale.invoke(permissions, positive, negative)
    }

    override fun onNeverAsk(permissions: List<String>) {
        onNeverAsk.invoke(permissions)
    }
}