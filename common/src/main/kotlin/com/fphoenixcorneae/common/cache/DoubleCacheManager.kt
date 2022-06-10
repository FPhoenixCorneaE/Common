package com.fphoenixcorneae.common.cache

import android.os.Parcelable
import kotlinx.parcelize.Parceler

/**
 * @desc：DoubleCacheManager
 * @date：2022/06/07 11:42
 */
class DoubleCacheManager private constructor() {
    private var customDoubleCache: DoubleCache? = null

    /**
     * Get the default instance of [DoubleCache].
     */
    val defaultDoubleCache: DoubleCache
        get() = customDoubleCache ?: DoubleCache.getInstance()

    /**
     * Set the default instance of [DoubleCache].
     */
    fun setDefaultDoubleCache(doubleCache: DoubleCache) {
        customDoubleCache = doubleCache
    }

    /**
     * Put value in cache.
     *
     * @param key            The key of cache.
     * @param value          The value of cache.
     * @param saveTime       The save time of cache, in seconds.
     * @param doubleCache    The instance of [DoubleCache].
     */
    fun put(
        key: String,
        value: Any?,
        saveTime: Int = -1,
        doubleCache: DoubleCache = defaultDoubleCache
    ) {
        doubleCache.put(key, value, saveTime)
    }

    /**
     * Return the value in cache.
     *
     * @param key              The key of cache.
     * @param defaultValue     The default value if the cache doesn't exist.
     * @param doubleCache      The instance of [DoubleCache].
     * @param <T>              The value type.
     * @return the value if cache exists or defaultValue otherwise
    </T> */
    inline operator fun <reified T> get(
        key: String,
        defaultValue: T? = null,
        doubleCache: DoubleCache = defaultDoubleCache
    ): T? {
        return doubleCache[key, defaultValue]
    }

    /**
     * Return the value in cache.
     *
     * @param key              The key of cache.
     * @param defaultValue     The default value if the cache doesn't exist.
     * @param doubleCache      The instance of [DoubleCache].
     * @param <T>              The value type.
     * @return the value if cache exists or defaultValue otherwise
    </T> */
    fun <T> getParcelable(
        key: String,
        creator: Parcelable.Creator<T>,
        defaultValue: T? = null,
        doubleCache: DoubleCache = defaultDoubleCache
    ): T? {
        return doubleCache.getParcelable(key, creator, defaultValue)
    }

    /**
     * Return the value in cache.
     *
     * @param key              The key of cache.
     * @param defaultValue     The default value if the cache doesn't exist.
     * @param doubleCache      The instance of [DoubleCache].
     * @param <T>              The value type.
     * @return the value if cache exists or defaultValue otherwise
    </T> */
    fun <T> getParcelable(
        key: String,
        parceler: Parceler<T>,
        defaultValue: T? = null,
        doubleCache: DoubleCache = defaultDoubleCache
    ): T? {
        return doubleCache.getParcelable(key, parceler, defaultValue)
    }

    /**
     * Return the size of cache in disk.
     *
     * @param doubleCache The instance of [DoubleCache].
     * @return the size of cache in disk
     */
    fun getCacheDiskSize(doubleCache: DoubleCache = defaultDoubleCache): Long {
        return doubleCache.cacheDiskSize
    }

    /**
     * Return the count of cache in disk.
     *
     * @param doubleCache The instance of [DoubleCache].
     * @return the count of cache in disk
     */
    fun getCacheDiskCount(doubleCache: DoubleCache = defaultDoubleCache): Int {
        return doubleCache.cacheDiskCount
    }

    /**
     * Return the count of cache in memory.
     *
     * @param doubleCache The instance of [DoubleCache].
     * @return the count of cache in memory.
     */
    fun getCacheMemoryCount(doubleCache: DoubleCache = defaultDoubleCache): Int {
        return doubleCache.cacheMemoryCount
    }

    /**
     * Remove the cache by key.
     *
     * @param key              The key of cache.
     * @param doubleCache The instance of [DoubleCache].
     */
    fun remove(key: String, doubleCache: DoubleCache = defaultDoubleCache) {
        doubleCache.remove(key)
    }

    /**
     * Clear all of the cache.
     *
     * @param doubleCache The instance of [DoubleCache].
     */
    fun clear(doubleCache: DoubleCache = defaultDoubleCache) {
        doubleCache.clear()
    }

    companion object {
        @Volatile
        private var sDoubleCacheManager: DoubleCacheManager? = null

        @Synchronized
        fun getInstance(): DoubleCacheManager {
            return sDoubleCacheManager ?: synchronized(DoubleCacheManager::class.java) {
                sDoubleCacheManager ?: DoubleCacheManager().also { sDoubleCacheManager = it }
            }
        }
    }
}