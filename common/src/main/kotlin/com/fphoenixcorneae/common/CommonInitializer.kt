package com.fphoenixcorneae.common

import android.app.Application
import android.content.Context
import androidx.startup.Initializer
import com.fphoenixcorneae.common.ext.logd
import com.fphoenixcorneae.common.ext.relaunchApp
import com.fphoenixcorneae.common.ext.setupGlobalExceptionHandler
import com.fphoenixcorneae.common.lifecycle.ActivityLifecycleCallbacksImpl
import com.fphoenixcorneae.common.util.toast.ToastUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope

/**
 * @desc：Startup 初始化Toast、Crash
 * @date：2022/04/28 11:20
 */
class CommonInitializer : Initializer<Unit>, CoroutineScope by MainScope() {

    override fun create(context: Context) {
        sApplication = (context.applicationContext as Application).also {
            // 注册应用生命周期回调
            it.registerActivityLifecycleCallbacks(ActivityLifecycleCallbacksImpl())
            // 初始化 ToastUtil
            ToastUtil.init(it)
        }
        // 全局异常处理
        setupGlobalExceptionHandler { _, _ ->
            // 重启应用
            relaunchApp()
        }
        "CommonInitializer 初始化".logd("startup")
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        // No dependencies on other libraries.
        return mutableListOf()
    }

    companion object {
        lateinit var sApplication: Application
    }
}
