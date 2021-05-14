package com.fphoenixcorneae.util.demo

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.fphoenixcorneae.dsl.layout.TextView
import com.fphoenixcorneae.ext.action
import com.fphoenixcorneae.ext.algorithm.*
import com.fphoenixcorneae.ext.logd
import com.fphoenixcorneae.ext.toastAliPayStyle
import com.fphoenixcorneae.ext.toastQQStyle
import com.fphoenixcorneae.ext.view.queryTextListener
import com.fphoenixcorneae.ext.view.textAction
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
    }
}