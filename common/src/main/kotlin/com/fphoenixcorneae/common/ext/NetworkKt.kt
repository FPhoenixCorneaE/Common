package com.fphoenixcorneae.common.ext

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.text.TextUtils
import java.io.IOException
import java.net.Inet4Address
import java.net.NetworkInterface
import java.net.SocketException
import java.util.*

private const val ETHERNET = "eth0"
private const val WLAN = "wlan0"
private const val DNS1 = "[net.dns1]"
private const val DNS2 = "[net.dns2]"
private const val ETHERNET_GATEWAY = "[dhcp.eth0.gateway]"
private const val WLAN_GATEWAY = "[dhcp.wlan0.gateway]"
private const val ETHERNET_MASK = "[dhcp.eth0.mask]"
private const val WLAN_MASK = "[dhcp.wlan0.mask]"

/**
 * 判断网络是否连接
 * Judge whether current network is connected
 */
val isNetworkConnected: Boolean
    get() {
        val connectivityManager = applicationContext.connectivityManager
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                // Android 6.0 以上可用方法
                // 当 NetworkCapabilities 的描述中有 INTERNET&&VALIDATED 这个描述时，此网络是真正可用的
                val networkCapabilities = connectivityManager?.getNetworkCapabilities(connectivityManager.activeNetwork)
                networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            }
            else -> {
                val info = connectivityManager?.activeNetworkInfo
                info != null && info.isConnected
            }
        }
    }

/**
 * 判断WIFI网络是否可用
 */
val isWifiConnected: Boolean
    get() {
        val connectivityManager = applicationContext.connectivityManager
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                val networkCapabilities = connectivityManager?.getNetworkCapabilities(connectivityManager.activeNetwork)
                networkCapabilities != null && networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                        && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            }
            else -> {
                val networkInfo = connectivityManager?.activeNetworkInfo
                networkInfo != null && networkInfo.type == ConnectivityManager.TYPE_WIFI && networkInfo.isConnected
            }
        }
    }

/**
 * 判断MOBILE网络是否可用
 */
val isMobileConnected: Boolean
    get() {
        val connectivityManager = applicationContext.connectivityManager
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                val networkCapabilities = connectivityManager?.getNetworkCapabilities(connectivityManager.activeNetwork)
                networkCapabilities != null && networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
            }
            else -> {
                val networkInfo = connectivityManager?.activeNetworkInfo
                networkInfo != null && networkInfo.type == ConnectivityManager.TYPE_MOBILE && networkInfo.isConnected
            }
        }
    }

/**
 * 获取当前网络连接的类型信息
 */
val connectedType: Int
    get() {
        val connectivityManager = applicationContext.connectivityManager
        val networkInfo = connectivityManager?.activeNetworkInfo
        return if (networkInfo != null && networkInfo.isConnected) {
            networkInfo.type
        } else {
            -1
        }
    }

/**
 * Get local ipv4 address
 */
val localIPv4: String
    get() = getLocalIp(true)

/**
 * Get local ipv6 address
 */
val localIPv6: String
    get() = getLocalIp(false)

/**
 * Get local ip address
 */
private fun getLocalIp(useIPv4: Boolean): String {
    try {
        val en = NetworkInterface.getNetworkInterfaces()
        while (en.hasMoreElements()) {
            val nif = en.nextElement()
            val inet = nif.inetAddresses
            while (inet.hasMoreElements()) {
                val addr = inet.nextElement()
                if (!addr.isLoopbackAddress) {
                    val ip = addr.hostAddress.uppercase(Locale.getDefault())
                    val isIPv4 = addr is Inet4Address
                    if (useIPv4) {
                        if (isIPv4) {
                            return ip
                        }
                    } else {
                        if (!isIPv4) {
                            val delim = ip.indexOf('%')
                            return if (delim < 0) ip else ip.substring(0, delim)
                        }
                    }
                }
            }
        }
    } catch (e: SocketException) {
        e.toString().loge()
    }
    return ""
}

/**
 * Get wlan mac address
 */
val wlanMacAddress: String
    get() = getMacAddress(WLAN)

/**
 * Get ethernet mac address
 */
val ethernetMacAddress: String
    get() = getMacAddress(ETHERNET)

/**
 * Get mac address
 */
private fun getMacAddress(interfaceName: String?): String {
    try {
        val interfaces = Collections.list(NetworkInterface.getNetworkInterfaces())
        for (intf in interfaces) {
            if (interfaceName != null) {
                if (!intf.name.equals(interfaceName, ignoreCase = true)) {
                    continue
                }
            }
            val mac: ByteArray = intf.hardwareAddress ?: return ""
            val buf = StringBuilder()
            for (aMac in mac) {
                buf.append(String.format("%02X:", aMac))
            }
            if (buf.isNotEmpty()) {
                buf.deleteCharAt(buf.length - 1)
            }
            return buf.toString()
        }
    } catch (e: SocketException) {
        e.toString().loge()
    }

    return ""
}

/**
 * Get dns1
 */
val dnS1: String
    get() = getPropInfo(DNS1)

/**
 * Get dns2
 */
val dnS2: String
    get() = getPropInfo(DNS2)

/**
 * Get ethernet gateway
 */
val ethernetGateway: String
    get() = getPropInfo(ETHERNET_GATEWAY)

/**
 * Get wlan gateway
 */
val wlanGateway: String
    get() = getPropInfo(WLAN_GATEWAY)

/**
 * Get ethernet mask
 */
val ethernetMask: String
    get() = getPropInfo(ETHERNET_MASK)

/**
 * Get wlan mask
 */
val wlanMask: String
    get() = getPropInfo(WLAN_MASK)

/**
 * Get prop information by different interface name
 */
private fun getPropInfo(interfaceName: String): String {
    var re = ""
    try {
        val process = Runtime.getRuntime().exec("getprop")
        val pr = Properties()
        pr.load(process.inputStream)
        re = pr.getProperty(interfaceName, "")
        if (!TextUtils.isEmpty(re) && re.length > 6) {
            re = re.substring(1, re.length - 1)
            return re
        }
    } catch (e: IOException) {
        e.toString().loge()
    }

    return re
}