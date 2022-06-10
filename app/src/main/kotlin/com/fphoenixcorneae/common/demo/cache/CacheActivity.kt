package com.fphoenixcorneae.common.demo.cache

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.fphoenixcorneae.common.demo.databinding.ActivityCacheBinding
import com.fphoenixcorneae.common.ext.logd
import com.fphoenixcorneae.common.ext.toast
import com.fphoenixcorneae.common.permission.requestWritePermission
import com.fphoenixcorneae.common.util.IntentUtil

/**
 * @desc：
 * @date：2022/06/09 16:23
 */
class CacheActivity : AppCompatActivity() {

    private lateinit var mViewBinding: ActivityCacheBinding
    private val mViewModel by viewModels<CacheViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewBinding = ActivityCacheBinding.inflate(layoutInflater).also {
            setContentView(it.root)
            it.lifecycleOwner = this
        }

        mViewBinding.apply {
            viewModel = mViewModel

            rgCacheDataType.setOnCheckedChangeListener { radioGroup, i ->
                "radioGroup: index: ${i - (i - 1) / 8 * 8}".logd()
                mViewModel.setCacheTypePosition(i - (i - 1) / 8 * 8 - 1)
            }
        }

        requestPermissions()
    }

    private fun requestPermissions() {
        requestWritePermission {
            onGranted {
                "onGranted".logd("requestPermissions")
                toast("读写权限申请成功！")
            }
            onDenied {
                "onDenied".logd("requestPermissions")
            }
            onShowRationale {
                "onShowRationale".logd("requestPermissions")
                AlertDialog.Builder(this@CacheActivity)
                    .setTitle("权限申请")
                    .setMessage("需要申请读写权限")
                    .setNegativeButton("取消") { dialog, which ->
                        dialog.dismiss()
                    }
                    .setPositiveButton("确定") { dialog, which ->
                        dialog.dismiss()
                        requestPermissions()
                    }
                    .show()
            }
            onNeverAskAgain {
                "onNeverAskAgain".logd("requestPermissions")
                IntentUtil.openApplicationDetailsSettings()
            }
        }
    }
}