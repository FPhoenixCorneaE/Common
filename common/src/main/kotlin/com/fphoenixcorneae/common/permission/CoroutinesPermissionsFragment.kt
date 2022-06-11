package com.fphoenixcorneae.common.permission

import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * @desc：CoroutinesPermissionsFragment
 * @date：2022/6/11 12:09
 */
internal class CoroutinesPermissionsFragment(
    private val permissions: List<String>,
    private val shouldShowRationale: Boolean
) : Fragment() {

    private val permissionsResultFlow = MutableStateFlow<PermissionsResult?>(null)
    private lateinit var requestMultiplePermissionsLauncher: ActivityResultLauncher<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true

        requestMultiplePermissionsLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                if (it.all { requireContext().run { isGranted(it.key) && !isRevoked(it.key) } }) {
                    // granted
                    permissionsResultFlow.value = PermissionsResult.Granted
                } else {
                    val deniedPermissions = it.filterNot { it.value }.flatMap { listOf(it.key) }
                    // denied
                    if (deniedPermissions.isNotEmpty()) {
                        permissionsResultFlow.value = PermissionsResult.Denied(deniedPermissions)
                    }
                    val neverAskPermissions = deniedPermissions.filterNot {
                        shouldShowRequestPermissionRationale(it)
                    }
                    // never ask again
                    if (neverAskPermissions.isNotEmpty()) {
                        permissionsResultFlow.value =
                            PermissionsResult.NeverAsk(neverAskPermissions)
                    }
                }
            }

        checkPermission()
    }

    private fun requestPermissions(permissions: List<String>) {
        requestMultiplePermissionsLauncher.launch(permissions.toTypedArray())
    }

    private fun checkPermission() {
        val deniedPermissions = permissions.filterNot {
            requireContext().run { isGranted(it) && !isRevoked(it) }
        }
        if (deniedPermissions.isEmpty()) {
            // granted
            permissionsResultFlow.value = PermissionsResult.Granted
        } else {
            if (deniedPermissions.any { shouldShowRequestPermissionRationale(it) && shouldShowRationale }) {
                // show rationale
                permissionsResultFlow.value = PermissionsResult.ShowRationale(
                    permissions = deniedPermissions,
                    positive = {
                        requestPermissions(deniedPermissions)
                    },
                    negative = {
                        // denied
                        permissionsResultFlow.value = PermissionsResult.Denied(deniedPermissions)
                    }
                )
            } else {
                requestPermissions(permissions = deniedPermissions)
            }
        }
    }

    fun getPermissionsResultFlow() = permissionsResultFlow.asStateFlow()
}