package com.fphoenixcorneae.common.permission

/**
 * @desc：PermissionsResult
 * @date：2022/6/11 12:04
 */
internal sealed class PermissionsResult {
    object Granted : PermissionsResult()

    data class Denied(val permissions: List<String>) : PermissionsResult()

    data class ShowRationale(
        val permissions: List<String>,
        val positive: () -> Unit,
        val negative: () -> Unit
    ) : PermissionsResult()

    data class NeverAsk(val permissions: List<String>) : PermissionsResult()
}
