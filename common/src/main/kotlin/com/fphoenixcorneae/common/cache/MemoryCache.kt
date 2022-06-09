package com.fphoenixcorneae.common.cache

import android.util.LruCache

/**
 * @desc：内存缓存
 * @date：2022/01/04 15:41
 */
class MemoryCache private constructor(
    private val mCacheKey: String,
    private val mMemoryCache: LruCache<String, CacheValue>
) {

    /**
     * Put value in cache.
     *
     * @param key      The key of cache.
     * @param value    The value of cache.
     * @param saveTime The save time of cache, in seconds.
     */
    fun put(key: String, value: Any?, saveTime: Int = -1) {
        if (value == null) return
        val dueTime = if (saveTime < 0) -1 else System.currentTimeMillis() + saveTime * 1000
        mMemoryCache.put(key, CacheValue(dueTime, value))
    }

    /**
     * Return the value in cache.
     *
     * @param key          The key of cache.
     * @param defaultValue The default value if the cache doesn't exist.
     * @param <T>          The value type.
     * @return the value if cache exists or defaultValue otherwise
    </T> */
    operator fun <T> get(key: String, defaultValue: T? = null): T? {
        val `val`: CacheValue = mMemoryCache.get(key) ?: return defaultValue
        if (`val`.dueTime == -1L || `val`.dueTime >= System.currentTimeMillis()) {
            return `val`.value as T
        }
        mMemoryCache.remove(key)
        return defaultValue
    }

    /**
     * Remove the cache by key.
     *
     * @param key The key of cache.
     * @return `true`: success<br></br>`false`: fail
     */
    fun remove(key: String): Any? {
        val remove: CacheValue = mMemoryCache.remove(key) ?: return null
        return remove.value
    }

    /**
     * Return the count of cache.
     */
    val cacheCount: Int
        get() = mMemoryCache.size()

    /**
     * Clear all of the cache.
     */
    fun clear() {
        mMemoryCache.evictAll()
    }

    override fun toString(): String {
        return mCacheKey + "@" + Integer.toHexString(hashCode())
    }

    companion object {
        private const val DEFAULT_MAX_COUNT = 256
        private val CACHE_MAP = hashMapOf<String, MemoryCache>()

        /**
         * single [MemoryCache] instance
         * @param maxCount The max count of cache.
         */
        fun getInstance(maxCount: Int = DEFAULT_MAX_COUNT): MemoryCache {
            return getInstance(maxCount.toString(), maxCount)
        }

        /**
         * single [MemoryCache] instance
         * @param cacheKey The key of cache.
         * @param maxCount The max count of cache.
         */
        fun getInstance(cacheKey: String, maxCount: Int = DEFAULT_MAX_COUNT): MemoryCache {
            var cache = CACHE_MAP[cacheKey]
            if (cache == null) {
                synchronized(MemoryCache::class.java) {
                    cache = CACHE_MAP[cacheKey]
                    if (cache == null) {
                        cache = MemoryCache(cacheKey, LruCache<String, CacheValue>(maxCount))
                        CACHE_MAP[cacheKey] = cache!!
                    }
                }
            }
            return cache!!
        }
    }

    /**
     * @desc：CacheValue
     * @date：2022/01/04 15:51
     */
    private class CacheValue(
        var dueTime: Long,
        var value: Any
    )
}