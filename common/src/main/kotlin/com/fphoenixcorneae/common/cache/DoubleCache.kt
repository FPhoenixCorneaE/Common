package com.fphoenixcorneae.common.cache

import android.os.Parcelable
import kotlinx.parcelize.Parceler

/**
 * @desc：双重缓存
 * @date：2022/01/05 14:45
 */
class DoubleCache private constructor(
    val mMemoryCache: MemoryCache,
    val mDiskCache: DiskCache
) {

    /**
     * Put value in cache.
     *
     * @param key            The key of cache.
     * @param value          The value of cache.
     * @param saveTime       The save time of cache, in seconds.
     */
    fun put(key: String, value: Any?, saveTime: Int = -1) {
        mMemoryCache.put(key, value, saveTime)
        mDiskCache.put(key, value, saveTime)
    }

    /**
     * Return the value in cache.
     *
     * @param key              The key of cache.
     * @param defaultValue     The default value if the cache doesn't exist.
     * @param <T>              The value type.
     * @return the value if cache exists or defaultValue otherwise
    </T> */
    inline operator fun <reified T> get(
        key: String,
        defaultValue: T? = null,
    ): T? {
        return mMemoryCache[key]
            ?: mDiskCache[key, defaultValue]?.also { mMemoryCache.put(key, it) }
            ?: defaultValue
    }

    /**
     * Return the parcelable in cache.
     *
     * @param key          The key of cache.
     * @param creator      The creator.
     * @param defaultValue The default value if the cache doesn't exist.
     * @param <T>          The value type.
     * @return the parcelable if cache exists or defaultValue otherwise
    </T> */
    fun <T> getParcelable(
        key: String,
        creator: Parcelable.Creator<T>,
        defaultValue: T? = null
    ): T? {
        return mMemoryCache.get<T>(key)
            ?: mDiskCache.getParcelable(key, creator)?.also { mMemoryCache.put(key, it) }
            ?: defaultValue
    }

    /**
     * Return the parcelable in cache.
     *
     * @param key          The key of cache.
     * @param parceler     The parceler.
     * @param defaultValue The default value if the cache doesn't exist.
     * @param <T>          The value type.
     * @return the parcelable if cache exists or defaultValue otherwise
     */
    fun <T> getParcelable(
        key: String,
        parceler: Parceler<T>,
        defaultValue: T? = null
    ): T? {
        return mMemoryCache.get<T>(key)
            ?: mDiskCache.getParcelable(key, parceler)?.also { mMemoryCache.put(key, it) }
            ?: defaultValue
    }

    /**
     * Return the size of cache in disk.
     *
     * @return the size of cache in disk
     */
    val cacheDiskSize: Long
        get() = mDiskCache.cacheSize

    /**
     * Return the count of cache in disk.
     *
     * @return the count of cache in disk
     */
    val cacheDiskCount: Int
        get() = mDiskCache.cacheCount

    /**
     * Return the count of cache in memory.
     *
     * @return the count of cache in memory.
     */
    val cacheMemoryCount: Int
        get() = mMemoryCache.cacheCount

    /**
     * Remove the cache by key.
     *
     * @param key The key of cache.
     */
    fun remove(key: String) {
        mMemoryCache.remove(key)
        mDiskCache.remove(key)
    }

    /**
     * Clear all of the cache.
     */
    fun clear() {
        mMemoryCache.clear()
        mDiskCache.clear()
    }

    companion object {
        private val MAP: MutableMap<String, DoubleCache> = HashMap()

        /**
         * Return the single [DoubleCache] instance.
         *
         * @param memoryCache The instance of [MemoryCache].
         * @param diskCache   The instance of [DiskCache].
         * @return the single [DoubleCache] instance
         */
        fun getInstance(
            memoryCache: MemoryCache = MemoryCache.getInstance(),
            diskCache: DiskCache = DiskCache.getInstance()
        ): DoubleCache {
            val cacheKey = diskCache.toString() + "_" + memoryCache.toString()
            var cache = MAP[cacheKey]
            if (cache == null) {
                synchronized(DoubleCache::class.java) {
                    cache = MAP[cacheKey]
                    if (cache == null) {
                        cache = DoubleCache(memoryCache, diskCache)
                        MAP[cacheKey] = cache!!
                    }
                }
            }
            return cache!!
        }
    }
}