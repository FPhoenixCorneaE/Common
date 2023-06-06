package com.fphoenixcorneae.common.ext

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import java.io.File
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException
import java.util.*

/**
 * Return whether device is rooted.
 * @return `true`: yes<br></br>`false`: no
 */
val isDeviceRooted: Boolean
    get() {
        val su = "su"
        val locations = arrayOf(
            "/system/bin/", "/system/xbin/", "/sbin/", "/system/sd/xbin/",
            "/system/bin/failsafe/", "/data/local/xbin/", "/data/local/bin/", "/data/local/",
            "/system/sbin/", "/usr/bin/", "/vendor/bin/"
        )
        for (location in locations) {
            if (File(location + su).exists()) {
                return true
            }
        }
        return false
    }

/**
 * Return whether ADB is enabled.
 * @return `true`: yes<br></br>`false`: no
 */
@get:RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
val isAdbEnabled: Boolean
    get() =
        Settings.Secure.getInt(applicationContext.contentResolver, Settings.Global.ADB_ENABLED, 0) > 0

/**
 * Return the version name of device's system.
 */
val sdkVersionName: String
    get() =
        Build.VERSION.RELEASE

/**
 * Return version code of device's system.
 */
val sdkVersionCode: Int
    get() =
        Build.VERSION.SDK_INT

/**
 * The Android ID
 * 通常被认为不可信，因为它有时为null。开发文档中说明了：这个ID会改变如果进行了出厂设置。并且，如果某个
 * Android手机被Root过的话，这个ID也可以被任意改变。无需任何许可。
 *
 * @return AndroidID
 */
@get:SuppressLint("HardwareIds")
val androidID: String
    get() {
        val id = Settings.Secure.getString(applicationContext.contentResolver, Settings.Secure.ANDROID_ID)
        return if ("9774d56d682e549c" == id) "" else id ?: ""
    }

/**
 * Return the MAC address.
 *
 * Must hold
 * `<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />`,
 * `<uses-permission android:name="android.permission.INTERNET" />`,
 * `<uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />`
 */
@get:RequiresPermission(
    allOf = [
        Manifest.permission.ACCESS_WIFI_STATE,
        Manifest.permission.INTERNET,
        Manifest.permission.CHANGE_WIFI_STATE
    ]
)
val macAddress: String
    get() {
        val macAddress = getMacAddress()
        if (!TextUtils.isEmpty(macAddress) || wifiEnabled) {
            return macAddress
        }
        wifiEnabled = true
        wifiEnabled = false
        return getMacAddress()
    }

/**
 * Enable or disable wifi.
 *
 * Must hold `<uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />`
 */
@set:RequiresPermission(Manifest.permission.CHANGE_WIFI_STATE)
var wifiEnabled: Boolean
    get() =
        applicationContext.wifiManager?.isWifiEnabled ?: false
    set(enabled) {
        if (enabled != applicationContext.wifiManager?.isWifiEnabled) {
            applicationContext.wifiManager?.isWifiEnabled = enabled
        }
    }

/**
 * Return the MAC address.
 *
 * Must hold `<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />`,
 * `<uses-permission android:name="android.permission.INTERNET" />`
 *
 * @return the MAC address
 */
@RequiresPermission(allOf = [Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.INTERNET])
fun getMacAddress(vararg excepts: String?): String {
    var macAddress = macAddressByNetworkInterface
    if (isAddressNotInExcepts(macAddress, *excepts)) {
        return macAddress
    }
    macAddress = macAddressByInetAddress
    if (isAddressNotInExcepts(macAddress, *excepts)) {
        return macAddress
    }
    macAddress = macAddressByWifiInfo
    if (isAddressNotInExcepts(macAddress, *excepts)) {
        return macAddress
    }
    macAddress = macAddressByFile
    return if (isAddressNotInExcepts(macAddress, *excepts)) {
        macAddress
    } else {
        ""
    }
}

private fun isAddressNotInExcepts(address: String, vararg excepts: String?): Boolean {
    if (TextUtils.isEmpty(address)) {
        return false
    }
    if ("02:00:00:00:00:00" == address) {
        return false
    }
    if (excepts.isEmpty()) {
        return true
    }
    for (filter in excepts) {
        if (filter != null && filter == address) {
            return false
        }
    }
    return true
}

@get:RequiresPermission(allOf = [Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.ACCESS_FINE_LOCATION])
private val macAddressByWifiInfo: String
    @SuppressLint("HardwareIds", "MissingPermission")
    get() =
        applicationContext.wifiManager?.connectionInfo?.macAddress ?: "02:00:00:00:00:00"

private val macAddressByNetworkInterface: String
    get() {
        try {
            val nis = NetworkInterface.getNetworkInterfaces()
            while (nis.hasMoreElements()) {
                val ni = nis.nextElement()
                if (ni == null || !ni.name.equals("wlan0", ignoreCase = true)) {
                    continue
                }
                val macBytes = ni.hardwareAddress
                if (macBytes != null && macBytes.isNotEmpty()) {
                    val sb = StringBuilder()
                    for (b in macBytes) {
                        sb.append(String.format("%02x:", b))
                    }
                    return sb.substring(0, sb.length - 1)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return "02:00:00:00:00:00"
    }
private val macAddressByInetAddress: String
    get() {
        try {
            val inetAddress = inetAddress
            if (inetAddress != null) {
                val ni = NetworkInterface.getByInetAddress(inetAddress)
                if (ni != null) {
                    val macBytes = ni.hardwareAddress
                    if (macBytes != null && macBytes.isNotEmpty()) {
                        val sb = StringBuilder()
                        for (b in macBytes) {
                            sb.append(String.format("%02x:", b))
                        }
                        return sb.substring(0, sb.length - 1)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return "02:00:00:00:00:00"
    }

// To prevent phone of xiaomi return "10.0.2.15"
private val inetAddress: InetAddress?
    get() {
        try {
            val nis = NetworkInterface.getNetworkInterfaces()
            while (nis.hasMoreElements()) {
                val ni = nis.nextElement()
                // To prevent phone of xiaomi return "10.0.2.15"
                if (!ni.isUp) {
                    continue
                }
                val addresses = ni.inetAddresses
                while (addresses.hasMoreElements()) {
                    val inetAddress = addresses.nextElement()
                    if (!inetAddress.isLoopbackAddress) {
                        val hostAddress = inetAddress.hostAddress
                        if (hostAddress.indexOf(':') < 0) {
                            return inetAddress
                        }
                    }
                }
            }
        } catch (e: SocketException) {
            e.printStackTrace()
        }
        return null
    }

private val macAddressByFile: String
    get() {
        var result = executeCmd("getprop wifi.interface", false)
        if (result.result == 0) {
            val name: String? = result.successMsg
            if (name != null) {
                result = executeCmd("cat /sys/class/net/$name/address", false)
                if (result.result == 0) {
                    val address: String? = result.successMsg
                    if (address != null && address.isNotEmpty()) {
                        return address
                    }
                }
            }
        }
        return "02:00:00:00:00:00"
    }

/**
 * Return the manufacturer of the product/hardware.
 * e.g. Xiaomi
 */
val deviceManufacturer: String
    get() = Build.MANUFACTURER

/**
 * Return the model of device.
 *
 * e.g. MI2SC
 *
 * @return the model of device
 */
val deviceModel: String
    get() {
        var model = Build.MODEL
        model = model?.trim { it <= ' ' }?.replace("\\s*".toRegex(), "") ?: ""
        return model
    }

/**
 * Return an ordered list of ABIs supported by this device. The most preferred ABI is the first
 * element in the list.
 *
 * @return an ordered list of ABIs supported by this device
 */
val aBIs: Array<String>
    @SuppressLint("ObsoleteSdkInt")
    get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        Build.SUPPORTED_ABIS
    } else {
        if (!TextUtils.isEmpty(Build.CPU_ABI2)) {
            arrayOf(Build.CPU_ABI, Build.CPU_ABI2)
        } else {
            arrayOf(Build.CPU_ABI)
        }
    }

/**
 * Return whether device is tablet.
 *
 * @return `true`: yes<br></br>`false`: no
 */
val isTablet: Boolean
    get() = (Resources.getSystem().configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE

/**
 * Return whether device is emulator.
 *
 * @return `true`: yes<br></br>`false`: no
 */
val isEmulator: Boolean
    @SuppressLint("QueryPermissionsNeeded")
    get() {
        val checkProperty = (Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.lowercase(Locale.getDefault()).contains("vbox")
                || Build.FINGERPRINT.lowercase(Locale.getDefault()).contains("test-keys")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk" == Build.PRODUCT)
        if (checkProperty) {
            return true
        }
        var operatorName = ""
        val tm = applicationContext.telephonyManager
        if (tm != null) {
            val name: String? = tm.networkOperatorName
            if (name != null) {
                operatorName = name
            }
        }
        val checkOperatorName = operatorName.lowercase(Locale.getDefault()) == "android"
        if (checkOperatorName) {
            return true
        }
        val url = "tel:" + "123456"
        val intent = Intent()
        intent.data = Uri.parse(url)
        intent.action = Intent.ACTION_DIAL
        return intent.resolveActivity(applicationContext.packageManager) == null
    }

/**
 * Whether user has enabled development settings.
 */
@get:RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
val isDevelopmentSettingsEnabled: Boolean
    get() =
        Settings.Global.getInt(applicationContext.contentResolver, Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 0) > 0

private const val KEY_UDID = "KEY_UDID"

@Volatile
private var udid: String? = null

/**
 * Return the unique device id.
 * <pre>{1}{UUID(macAddress)}</pre>
 * <pre>{2}{UUID(androidId )}</pre>
 * <pre>{9}{UUID(random    )}</pre>
 *
 * @return the unique device id
 */
val uniqueDeviceId: String
    get() =
        getUniqueDeviceId("", true)

/**
 * Return the unique device id.
 * <pre>android 10 deprecated {prefix}{1}{UUID(macAddress)}</pre>
 * <pre>{prefix}{2}{UUID(androidId )}</pre>
 * <pre>{prefix}{9}{UUID(random    )}</pre>
 *
 * @param prefix   The prefix of the unique device id.
 * @param useCache True to use cache, false otherwise.
 * @return the unique device id
 */
@Synchronized
fun getUniqueDeviceId(prefix: String = "", useCache: Boolean = true): String {
    if (!useCache) {
        return getUniqueDeviceIdReal(prefix)
    }
    if (udid == null) {
        udid = getSP(KEY_UDID, "")
    }
    return udid!!
}

private fun getUniqueDeviceIdReal(prefix: String): String {
    try {
        val androidId = androidID
        if (!TextUtils.isEmpty(androidId)) {
            return saveUdid(prefix + 2, androidId)
        }
    } catch (ignore: Exception) { /**/
    }
    return saveUdid(prefix + 9, "")
}

@RequiresPermission(
    allOf = [
        Manifest.permission.ACCESS_WIFI_STATE,
        Manifest.permission.INTERNET,
        Manifest.permission.CHANGE_WIFI_STATE
    ]
)
fun isSameDevice(uniqueDeviceId: String): Boolean {
    // {prefix}{type}{32id}
    if (TextUtils.isEmpty(uniqueDeviceId) && uniqueDeviceId.length < 33) {
        return false
    }
    if (uniqueDeviceId == udid) {
        return true
    }
    val cachedId: String = getSP(KEY_UDID, "")
    if (uniqueDeviceId == cachedId) {
        return true
    }
    val st = uniqueDeviceId.length - 33
    val type = uniqueDeviceId.substring(st, st + 1)
    if (type.startsWith("1")) {
        val macAddress = macAddress
        return if (macAddress == "") {
            false
        } else {
            uniqueDeviceId.substring(st + 1) == getUdid("", macAddress)
        }
    } else if (type.startsWith("2")) {
        val androidId = androidID
        return if (TextUtils.isEmpty(androidId)) {
            false
        } else {
            uniqueDeviceId.substring(st + 1) == getUdid("", androidId)
        }
    }
    return false
}

private fun saveUdid(prefix: String, id: String): String {
    udid = getUdid(prefix, id)
    putSP(KEY_UDID to udid)
    return udid!!
}

private fun getUdid(prefix: String, id: String): String {
    return if (id == "") {
        prefix + UUID.randomUUID().toString().replace("-", "")
    } else {
        prefix + UUID.nameUUIDFromBytes(id.toByteArray()).toString().replace("-", "")
    }
}