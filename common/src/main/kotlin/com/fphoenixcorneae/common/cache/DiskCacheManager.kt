package com.fphoenixcorneae.common.cache

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Parcelable
import kotlinx.parcelize.Parceler
import org.json.JSONArray
import org.json.JSONObject
import java.io.Serializable

/**
 * @desc：DiskCacheManager
 * @date：2022/06/07 11:42
 */
class DiskCacheManager private constructor() {
    private var customDiskCache: DiskCache? = null

    /**
     * Get the default instance of [DiskCache].
     */
    val defaultDiskCache: DiskCache
        get() = customDiskCache ?: DiskCache.getInstance()

    /**
     * Set the default instance of [DiskCache].
     */
    fun setDefaultDiskCache(diskCache: DiskCache) {
        customDiskCache = diskCache
    }

    /**
     * Put value in cache.
     *
     * @param key            The key of cache.
     * @param value          The value of cache.
     * @param saveTime       The save time of cache, in seconds.
     * @param diskCache      The instance of [DiskCache].
     */
    fun put(
        key: String,
        value: Any?,
        saveTime: Int = -1,
        diskCache: DiskCache = defaultDiskCache
    ) {
        when (value) {
            is ByteArray? -> diskCache.put(key, value, saveTime)
            is String? -> diskCache.put(key, value, saveTime)
            is JSONObject? -> diskCache.put(key, value, saveTime)
            is JSONArray? -> diskCache.put(key, value, saveTime)
            is Bitmap? -> diskCache.put(key, value, saveTime)
            is Drawable? -> diskCache.put(key, value, saveTime)
            is Parcelable? -> diskCache.put(key, value, saveTime)
            is Serializable? -> diskCache.put(key, value, saveTime)
            else -> {}
        }
    }

    /**
     * Return the value in cache.
     *
     * @param key              The key of cache.
     * @param defaultValue     The default value if the cache doesn't exist.
     * @param diskCache        The instance of [DiskCache].
     * @param <T>              The value type.
     * @return the value if cache exists or defaultValue otherwise
    </T> */
    inline operator fun <reified T> get(
        key: String,
        defaultValue: T? = null,
        diskCache: DiskCache = defaultDiskCache
    ): T? {
        return diskCache[key, defaultValue]
    }

    /**
     * Return the value in cache.
     *
     * @param key              The key of cache.
     * @param creator          The creator.
     * @param defaultValue     The default value if the cache doesn't exist.
     * @param diskCache        The instance of [DiskCache].
     * @param <T>              The value type.
     * @return the value if cache exists or defaultValue otherwise
    </T> */
    fun <T> getParcelable(
        key: String,
        creator: Parcelable.Creator<T>,
        defaultValue: T? = null,
        diskCache: DiskCache = defaultDiskCache
    ): T? {
        return diskCache.getParcelable(key, creator, defaultValue)
    }

    /**
     * Return the value in cache.
     *
     * @param key              The key of cache.
     * @param parceler         The parceler.
     * @param defaultValue     The default value if the cache doesn't exist.
     * @param diskCache        The instance of [DiskCache].
     * @param <T>              The value type.
     * @return the value if cache exists or defaultValue otherwise
    </T> */
    fun <T> getParcelable(
        key: String,
        parceler: Parceler<T>,
        defaultValue: T? = null,
        diskCache: DiskCache = defaultDiskCache
    ): T? {
        return diskCache.getParcelable(key, parceler, defaultValue)
    }

    /**
     * Return the size of cache, in bytes.
     *
     * @param diskCache The instance of [DiskCache].
     * @return the size of cache, in bytes
     */
    fun getCacheSize(diskCache: DiskCache = defaultDiskCache): Long {
        return diskCache.cacheSize
    }

    /**
     * Return the count of cache.
     *
     * @param diskCache The instance of [DiskCache].
     * @return the count of cache
     */
    fun getCacheCount(diskCache: DiskCache = defaultDiskCache): Int {
        return diskCache.cacheCount
    }

    /**
     * Remove the cache by key.
     *
     * @param key            The key of cache.
     * @param diskCache The instance of [DiskCache].
     * @return `true`: success<br></br>`false`: fail
     */
    fun remove(key: String, diskCache: DiskCache = defaultDiskCache): Boolean {
        return diskCache.remove(key)
    }

    /**
     * Clear all of the cache.
     *
     * @param diskCache The instance of [DiskCache].
     * @return `true`: success<br></br>`false`: fail
     */
    fun clear(diskCache: DiskCache = defaultDiskCache): Boolean {
        return diskCache.clear()
    }

    companion object {
        @Volatile
        private var sDiskCacheManager: DiskCacheManager? = null

        @Synchronized
        fun getInstance(): DiskCacheManager {
            return sDiskCacheManager ?: synchronized(DiskCacheManager::class.java) {
                sDiskCacheManager ?: DiskCacheManager().also { sDiskCacheManager = it }
            }
        }
    }
}