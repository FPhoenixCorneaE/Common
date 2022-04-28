package com.fphoenixcorneae.common.ext

import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import java.util.*

/**
 * Return all of the services are running.
 *
 * @return all of the services are running
 */
val allRunningServices: Set<String>?
    get() {
        val info = applicationContext.activityManager?.getRunningServices(Integer.MAX_VALUE)
        val names = HashSet<String>()
        if (info == null || info.size == 0) {
            return null
        }
        for (aInfo in info) {
            names.add(aInfo.service.className)
        }
        return names
    }

/**
 * @desc：Service
 * @date：2021/11/26 16:33
 */

/**
 * 启动服务
 * @param cls 服务类
 */
fun startService(cls: Class<*>) = run {
    applicationContext.startService(Intent(applicationContext, cls))
}

/**
 * 启动服务
 * @param className 完整包名的服务类名
 */
fun startService(className: String) = runCatching {
    startService(Class.forName(className))
}.onFailure {
    it.printStackTrace()
}

/**
 * 停止服务
 * @param cls 服务类
 * @return true：停止成功  false：停止失败
 */
fun stopService(cls: Class<*>): Boolean = run {
    applicationContext.stopService(Intent(applicationContext, cls))
}

/**
 * 停止服务
 * @param className 完整包名的服务类名
 * @return true：停止成功  false：停止失败
 */
fun stopService(className: String): Boolean = runCatching {
    stopService(Class.forName(className))
}.onFailure {
    it.printStackTrace()
}.getOrDefault(false)

/**
 * 绑定服务
 * @param cls   服务类
 * @param conn  服务连接对象
 * @param flags 绑定选项
 * [Context.BIND_AUTO_CREATE]
 * [Context.BIND_DEBUG_UNBIND]
 * [Context.BIND_NOT_FOREGROUND]
 * [Context.BIND_ABOVE_CLIENT]
 * [Context.BIND_ALLOW_OOM_MANAGEMENT]
 * [Context.BIND_WAIVE_PRIORITY]
 */
fun bindService(cls: Class<*>, conn: ServiceConnection, flags: Int) = run {
    applicationContext.bindService(Intent(applicationContext, cls), conn, flags)
}

/**
 * 绑定服务
 * @param className 完整包名的服务类名
 * @param conn      服务连接对象
 * @param flags     绑定选项
 */
fun bindService(className: String, conn: ServiceConnection, flags: Int) = runCatching {
    bindService(Class.forName(className), conn, flags)
}.onFailure {
    it.printStackTrace()
}

/**
 * 解绑服务
 * @param conn 服务连接对象
 */
fun unBindService(conn: ServiceConnection) = run {
    applicationContext.unbindService(conn)
}

/**
 * 判断服务是否运行
 * @param cls    服务类
 * @return true：是   false：否
 */
@Suppress("DEPRECATION")
fun isServiceRunning(cls: Class<*>): Boolean = run {
    applicationContext.activityManager?.getRunningServices(Integer.MAX_VALUE)
        ?.any { it -> it.service.className == cls.name }
        ?: false
}

/**
 * 判断服务是否运行
 * @param className 完整包名的服务类名
 * @return true：是   false：否
 */
fun isServiceRunning(className: String): Boolean = runCatching {
    isServiceRunning(Class.forName(className))
}.onFailure {
    it.printStackTrace()
}.getOrDefault(false)