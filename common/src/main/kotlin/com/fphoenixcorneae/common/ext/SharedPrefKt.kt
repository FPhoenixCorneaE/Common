package com.fphoenixcorneae.common.ext

import android.content.Context
import android.content.SharedPreferences

/**
 * SharedPreferences 偏好设定工具类,轻量级的存储
 * 1、每次添加键值对时,会重新写入整个文件数据,所以不适合大量数据存储;
 * 2、commit()提交到硬盘是同步过程;apply()先提交到内存后异步提交到硬盘,无返回值;主线程使用commit()需考虑ANR问题;
 *    不关心提交结果是否成功的情况下,优先考虑apply()方法;
 * 3、多线程场景下效率较低,get()操作会锁定SharedPreferencesImpl里的对象,互斥其他操作,而当put()、commit()和
 *    apply()操作时会锁住Editor对象;
 * 4、由于每次都会把整个文件加载到内存中,因此,如果SharedPreferences文件过大,或者其中的键值对是大对象的JSON
 *    数据则会占用大量内存,读取较慢是一方面,同时也会引发程序频繁GC,导致界面卡顿。
 *
 * 建议:
 * 1、频繁修改的数据修改后统一提交,而不是修改过后马上提交;
 * 2、在跨进程通讯中不去使用SharedPreferences;
 * 3、获取SharedPreferences对象时会读取SharedPreferences文件,如果文件没有读取完,就执行get()和put()操作,可能
 *    会出现需要等待的情况,因此最好提交获取SharedPreferences对象;
 * 4、每次调用edit()方法都会创建一个新的EditorImpl对象,不要频繁调用edit()方法。
 *
 * @date 2019-12-05 10:14
 */

/**
 * Get SharedPreferences
 */
private val mSharedPreferences: SharedPreferences by lazy {
    applicationContext.getSharedPreferences("sp_default", Context.MODE_PRIVATE)
}

/**
 * Get SharedPreferences by name
 */
fun getSharedPreferences(name: String?): SharedPreferences =
    name?.run {
        applicationContext.getSharedPreferences(name, Context.MODE_PRIVATE)
    } ?: mSharedPreferences

/**
 * Put SharedPreferences, the method may set the string/boolean/int/float/long value in the preferences editor
 */
fun putSP(vararg pairs: Pair<String, Any?>, name: String? = null): Boolean =
    getSharedPreferences(name).edit().run {
        pairs.forEach { pair ->
            val key = pair.first
            when (val value = pair.second) {
                is String? -> {
                    putString(key, value)
                }
                is Boolean -> {
                    putBoolean(key, value)
                }
                is Float -> {
                    putFloat(key, value)
                }
                is Int -> {
                    putInt(key, value)
                }
                is Long -> {
                    putLong(key, value)
                }
                else -> {
                    putString(key, value.serialize())
                }
            }
        }
        commit()
    }

/**
 * Retrieve value from the preferences.
 */
inline fun <reified T> getSP(key: String, defValue: T, name: String? = null): T =
    getSharedPreferences(name).run {
        when (T::class) {
            String::class -> {
                getString(key, defValue as String).toString() as T
            }
            Boolean::class -> {
                getBoolean(key, defValue as Boolean) as T
            }
            Float::class -> {
                getFloat(key, defValue as Float) as T
            }
            Int::class -> {
                getInt(key, defValue as Int) as T
            }
            Long::class -> {
                getLong(key, defValue as Long) as T
            }
            else -> {
                getString(key, defValue.serialize()).toString().deserialize<T>() ?: defValue
            }
        }
    }

/**
 * Retrieve all values from the preferences.
 *
 * @return Returns a map containing a list of pairs key/value representing the preferences.
 */
fun getSPAll(name: String? = null): Map<String, *> {
    return getSharedPreferences(name).all
}

/**
 * Mark in the editor that a preference value should be removed.
 */
fun removeSP(key: String, name: String? = null): Boolean =
    getSharedPreferences(name).edit().run {
        remove(key)
        commit()
    }

/**
 * 查询某个key是否已经存在
 *
 * @param key
 * @return
 */
fun containsSP(key: String): Boolean {
    return mSharedPreferences.contains(key)
}

/**
 * Mark in the editor to remove all values from the preferences.
 */
fun clearSP(name: String? = null): Boolean =
    getSharedPreferences(name).edit().run {
        clear()
        commit()
    }
