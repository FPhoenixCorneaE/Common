package com.fphoenixcorneae.common

import android.app.Application
import android.content.Context
import androidx.startup.Initializer
import com.fphoenixcorneae.common.ext.isDebuggable
import com.fphoenixcorneae.common.ext.logd
import com.fphoenixcorneae.common.ext.relaunchApp
import com.fphoenixcorneae.common.util.ContextUtil
import com.fphoenixcorneae.common.util.CrashUtil
import com.fphoenixcorneae.common.util.toast.ToastUtil
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
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
        // 初始化日志打印配置
        initLoggerConfig()
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

    /**
     * 初始化日志打印配置
     */
    private fun initLoggerConfig() {
        val formatStrategy = PrettyFormatStrategy.newBuilder()
            // 隐藏线程信息 默认：显示
            .showThreadInfo(false)
            // 决定打印多少行（每一行代表一个方法）默认：2
            .methodCount(2)
            // (Optional) Hides internal method calls up to offset. Default 5
            .methodOffset(7)
            // (Optional) Global tag for every log. Default PRETTY_LOGGER
            .tag("PhoenixKtx")
            .build()
        Logger.addLogAdapter(object : AndroidLogAdapter(formatStrategy) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                // Module 默认会提供 Release 版给其他 Module 或工程使用，BuildConfig.DEBUG 会始终为 false
                return isDebuggable
            }
        })
    }
}