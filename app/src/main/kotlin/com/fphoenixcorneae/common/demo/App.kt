package com.fphoenixcorneae.common.demo

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.fphoenixcorneae.common.ext.isDebuggable
import com.fphoenixcorneae.common.log.AndroidLog
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy

class App : Application(), ViewModelStoreOwner {

    override val viewModelStore: ViewModelStore
        get() = ViewModelStore()

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        // 初始化日志打印配置
        initLoggerConfig()
        AndroidLog.setPrinter(CustomPrinter())
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