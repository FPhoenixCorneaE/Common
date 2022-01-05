package com.fphoenixcorneae.common.demo

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.fphoenixcorneae.common.dsl.layout.TextView
import com.fphoenixcorneae.common.ext.*
import com.fphoenixcorneae.common.ext.algorithm.*
import com.fphoenixcorneae.common.ext.view.textAction
import com.fphoenixcorneae.common.permission.requestPhonePermission
import com.fphoenixcorneae.common.util.IntentUtil
import com.fphoenixcorneae.common.demo.databinding.ActivityMainBinding
import com.fphoenixcorneae.common.ext.view.queryTextListener
import com.fphoenixcorneae.common.ext.view.setOnSeekBarChangeListener
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var mViewBinding: ActivityMainBinding
    private val appViewModel by androidViewModel<AppViewModel>()

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mViewBinding.root)
        initObserver()

        mViewBinding.btnTest.setOnClickListener {
            //            toast("测试测试测试！！！")
            toastAliPayStyle("测试测试测试！！！")
            appViewModel.setGlobalString("测试 Global.")
        }

        val job: Job = lifecycleScope.launch { }
        job.action({
            "notNull".logd("action====")
        }) {
            "null".logd("action====")
        }

        TextView {}.textAction({

        }, {

        })

        mViewBinding.svSearch.queryTextListener {
            onQueryTextChange {
                toastQQStyle(it)
            }
            onQueryTextSubmit {
                toastQQStyle(it)
            }
        }

        val origin2Md5 = "a123456"
        ("md5: ${origin2Md5.md5()}\n\n" +
            "sha1: ${origin2Md5.sha1()}\n\n" +
            "sha224: ${origin2Md5.sha224()}\n\n" +
            "sha256: ${origin2Md5.sha256()}\n\n" +
            "sha384: ${origin2Md5.sha384()}\n\n" +
            "sha512: ${origin2Md5.sha512()}").also {
            mViewBinding.tvMd5.text = it
        }

        val fruit = Fruit(1, Watermalon(2f, 30f))
        val clone = fruit.copy()
        val deepClone = fruit.deepClone(Fruit::class.java)
        // 判断 Watermalon 对象是否相同，预期值：true
        (fruit.watermalon === clone.watermalon).toString().logd("clone")
        // 判断 Watermalon 对象是否相同，预期值：false
        (fruit.watermalon === deepClone?.watermalon).toString().logd("deepClone")


        // 申请权限
        mViewBinding.btnRequestPermissions.setOnClickListener {
            requestPermissions()
        }

        // 设置亮度
        mViewBinding.sbBrightness.setOnSeekBarChangeListener(
            onProgressChanged = { _, progress, _ ->
                setScreenBrightness(progress)
            }
        )
    }

    private fun initObserver() {
        appViewModel.global.observe(this) {
            "MainActivity: $it".logd("GlobalAndroid")
        }
    }

    private fun requestPermissions() {
        requestPhonePermission {
            onGranted {
                "onGranted".logd("requestPermissions")
                // TODO
            }
            onDenied {
                "onDenied".logd("requestPermissions")
                requestPermissions()
            }
            onShowRationale {
                "onShowRationale".logd("requestPermissions")
                it.retry()
            }
            onNeverAskAgain {
                "onNeverAskAgain".logd("requestPermissions")
                IntentUtil.openApplicationDetailsSettings()
            }
        }
    }
}

data class Fruit(
    val type: Int,
    val watermalon: Watermalon,
) : Cloneable

data class Watermalon(
    val weight: Float,
    val size: Float,
)