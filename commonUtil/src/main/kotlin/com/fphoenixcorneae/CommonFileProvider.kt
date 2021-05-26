package com.fphoenixcorneae

import android.app.Application
import android.content.pm.ApplicationInfo
import androidx.core.content.FileProvider
import com.fphoenixcorneae.ext.loggerD
import com.fphoenixcorneae.util.AppUtil
import com.fphoenixcorneae.util.ContextUtil
import com.fphoenixcorneae.util.CrashUtil
import com.fphoenixcorneae.util.toast.ToastUtil
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy

/**
 * 工具集文件提供者:
 * 1、初始化 ContextUtil;
 * 2、初始化 ToastUtil;
 * 3、初始化 CrashUtil;
 * 4、初始化日志打印配置;
 */
class CommonFileProvider : FileProvider() {

    override fun onCreate(): Boolean {
        // 初始化 ContextUtil
        ContextUtil.init(context!!)
        // 初始化 ToastUtil
        ToastUtil.init(context!!.applicationContext as Application)
        // 初始化 CrashUtil
        initCrashUtil()
        // 初始化日志打印配置
        initLoggerConfig()
        // 返回 true 表示初始化成功,返回 false 则初始化失败.
        return true
    }

    /**
     * 初始化崩溃处理工具
     */
    private fun initCrashUtil() {
        CrashUtil.init(object : CrashUtil.OnCrashListener {
            override fun onCrash(crashInfo: String, e: Throwable?) {
                loggerD(crashInfo)
                // 重启应用
                AppUtil.relaunchApp()
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
                return context!!.applicationInfo != null
                    && (context!!.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
            }
        })
    }
}