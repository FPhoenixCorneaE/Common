package com.fphoenixcorneae.common.permission

/**
 * Permission
 */
data class PermissionsRequest(
    val permissionFragment: RequestPermissionsFragment,
    val permissions: List<String>,
    val requestCode: Int
)