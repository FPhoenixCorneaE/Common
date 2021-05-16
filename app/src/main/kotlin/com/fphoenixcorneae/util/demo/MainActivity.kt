package com.fphoenixcorneae.util.demo

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.fphoenixcorneae.dsl.layout.TextView
import com.fphoenixcorneae.ext.*
import com.fphoenixcorneae.ext.algorithm.*
import com.fphoenixcorneae.ext.view.queryTextListener
import com.fphoenixcorneae.ext.view.setOnSeekBarChangeListener
import com.fphoenixcorneae.ext.view.textAction
import com.fphoenixcorneae.permission.request
import com.fphoenixcorneae.permission.requestCameraPermission
import com.fphoenixcorneae.util.BrightnessUtil
import com.fphoenixcorneae.util.IntentUtil
import com.fphoenixcorneae.util.demo.databinding.ActivityMainBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var mViewBinding: ActivityMainBinding

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mViewBinding.root)

        mViewBinding.btnTest.setOnClickListener {
            //            toast("测试测试测试！！！")
            toastAliPayStyle("测试测试测试！！！")
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
            requestCameraPermission {
                onGranted {
                    "onGranted".logd("Requesting permission ")
                    IntentUtil.startActivity(this@MainActivity, MediaStore.ACTION_IMAGE_CAPTURE)
                }
                onDenied {
                    "onDenied".logd("Requesting permission ")
                    request(*it.toTypedArray())
                }
                onShowRationale {
                    "onShowRationale".logd("Requesting permission ")
                    request(*it.permissions.toTypedArray())
                }
                onNeverAskAgain {
                    "onNeverAskAgain".logd("Requesting permission ")
                    IntentUtil.openApplicationDetailsSettings()
                }
            }
        }

        // 设置亮度
        mViewBinding.sbBrightness.setOnSeekBarChangeListener(
            onProgressChanged = { _, progress, _ ->
                BrightnessUtil.setBrightness(progress)
            }
        )
    }
}

data class Fruit(
    val type: Int,
    val watermalon: Watermalon
) : Cloneable

data class Watermalon(
    val weight: Float,
    val size: Float
)