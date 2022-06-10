package com.fphoenixcorneae.common.permission

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch

class RequestPermissionsFragment private constructor(
    private val mPermissions: Array<String>,
    private val mPermissionsCallback: PermissionsCallback?,
) : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestPermissions()
    }

    fun requestPermissions() {
//        requestPermissions(permissions, requestCode)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED){
                registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                    val neverAskPermissions = mutableListOf<String>()
                    val deniedPermissions = mutableListOf<String>()
                    val grantedPermissions = mutableListOf<String>()
                    it.entries.forEach {
                        if (it.value) {
                            grantedPermissions.add(it.key)
                        } else {
                            if (shouldShowRequestPermissionRationale(it.key)) {
                                deniedPermissions.add(it.key)
                            } else {
                                neverAskPermissions.add(it.key)
                            }
                        }
                    }
                    if (deniedPermissions.isNotEmpty()) {
                        // denied
                        mPermissionsCallback?.onDenied(deniedPermissions)
                    }

                    if (neverAskPermissions.isNotEmpty()) {
                        // never ask
                        mPermissionsCallback?.onNeverAskAgain(neverAskPermissions)
                    }

                    if (deniedPermissions.isEmpty() && neverAskPermissions.isEmpty()) {
                        // granted
                        mPermissionsCallback?.onGranted()
                        removeFragment()
                    }
                }.launch(mPermissions)
            }
        }
    }

    private fun removeFragment() {
        parentFragmentManager.beginTransaction().remove(this).commitAllowingStateLoss()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val neverAskPermissions = mutableListOf<String>()
        val deniedPermissions = mutableListOf<String>()
        val grantedPermissions = mutableListOf<String>()
        permissions.forEachIndexed { index, permission ->
            if (grantResults[index] == PackageManager.PERMISSION_DENIED) {
                if (shouldShowRequestPermissionRationale(permission)) {
                    deniedPermissions.add(permission)
                } else {
                    neverAskPermissions.add(permission)
                }
            } else {
                grantedPermissions.add(permission)
            }
        }

        val permissionsCallback = PermissionsMap.get(requestCode)
        if (deniedPermissions.isNotEmpty()) {
            // denied
            permissionsCallback?.onDenied(deniedPermissions)
        }

        if (neverAskPermissions.isNotEmpty()) {
            // never ask
            permissionsCallback?.onNeverAskAgain(neverAskPermissions)
        }

        if (deniedPermissions.isEmpty() && neverAskPermissions.isEmpty()) {
            // granted
            permissionsCallback?.onGranted()
        }
    }

    companion object {
        fun getInstance(
            permissions: Array<String>,
            permissionsCallback: PermissionsCallback?
        ): RequestPermissionsFragment {
            return RequestPermissionsFragment(
                mPermissions = permissions,
                mPermissionsCallback = permissionsCallback
            )
        }
    }
}