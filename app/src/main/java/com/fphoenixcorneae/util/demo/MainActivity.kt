package com.fphoenixcorneae.util.demo

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.fphoenixcorneae.ext.action
import com.fphoenixcorneae.ext.logd
import com.fphoenixcorneae.ext.toastAliPayStyle
import com.fphoenixcorneae.ext.toastQQStyle
import com.fphoenixcorneae.ext.view.queryTextListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        btnTest.setOnClickListener {
//            toast("测试测试测试！！！")
            toastAliPayStyle("测试测试测试！！！")
        }

        val job: Job = lifecycleScope.launch { }
        job.action({
            "notNull".logd("action====")
        }) {
            "null".logd("action====")
        }

        svSearch.queryTextListener {
            onQueryTextChange {
                toastQQStyle(it)
            }
            onQueryTextSubmit {
                toastQQStyle(it)
            }
        }
    }
}