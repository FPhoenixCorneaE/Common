package com.fphoenixcorneae.common

import android.app.Application
import android.content.Context
import androidx.startup.Initializer
import com.fphoenixcorneae.common.ext.logd
import com.fphoenixcorneae.common.ext.relaunchApp
import com.fphoenixcorneae.common.util.ContextUtil
import com.fphoenixcorneae.common.util.CrashUtil
import com.fphoenixcorneae.common.util.toast.ToastUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope

/**
 * @desc：Startup 初始化Context、Log、Toast、Crash
 * @date：2022/04/28 11:20
 */
class CommonInitializer : Initializer<Unit>, CoroutineScope by MainScope() {
    override fun create(context: Context) {
        // 初始化 ContextUtil
        ContextUtil.init(context)
        // 初始化 ToastUtil
        ToastUtil.init(context.applicationContext as Application)
        // 初始化 CrashUtil
        initCrashUtil()
        "CommonInitializer 初始化".logd("startup")
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        // No dependencies on other libraries.
        return mutableListOf()
    }

    /**
     * 初始化崩溃处理工具
     */
    private fun initCrashUtil() {
        CrashUtil.init(object : CrashUtil.OnCrashListener {
            override fun onCrash(crashInfo: String, e: Throwable?) {
                crashInfo.logd()
                // 重启应用
                relaunchApp()
            }
        })
    }
}