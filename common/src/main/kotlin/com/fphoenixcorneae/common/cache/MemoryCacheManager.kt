package com.fphoenixcorneae.common.cache

/**
 * @desc：MemoryCacheManager
 * @date：2022/06/07 11:42
 */
class MemoryCacheManager private constructor(){
    private var customMemoryCache: MemoryCache? = null

    /**
     * Get the default instance of [MemoryCache].
     */
    private val defaultMemoryCache: MemoryCache
        get() = customMemoryCache ?: MemoryCache.getInstance()

    /**
     * Set the default instance of [MemoryCache].
     */
    fun setDefaultMemoryCache(memoryCache: MemoryCache) {
        customMemoryCache = memoryCache
    }

    /**
     * Put value in cache.
     *
     * @param key              The key of cache.
     * @param value            The value of cache.
     * @param saveTime         The save time of cache, in seconds.
     * @param memoryCache The instance of [MemoryCache].
     */
    fun put(
        key: String,
        value: Any?,
        saveTime: Int = -1,
        memoryCache: MemoryCache = defaultMemoryCache
    ) {
        memoryCache.put(key, value, saveTime)
    }

    /**
     * Return the value in cache.
     *
     * @param key              The key of cache.
     * @param defaultValue     The default value if the cache doesn't exist.
     * @param memoryCache The instance of [MemoryCache].
     * @param <T>              The value type.
     * @return the value if cache exists or defaultValue otherwise
    </T> */
    operator fun <T> get(
        key: String,
        defaultValue: T? = null,
        memoryCache: MemoryCache = defaultMemoryCache
    ): T? {
        return memoryCache[key, defaultValue]
    }

    /**
     * Return the count of cache.
     *
     * @param memoryCache The instance of [MemoryCache].
     * @return the count of cache
     */
    fun getCacheCount(memoryCache: MemoryCache = defaultMemoryCache): Int {
        return memoryCache.cacheCount
    }

    /**
     * Remove the cache by key.
     *
     * @param key              The key of cache.
     * @param memoryCache The instance of [MemoryCache].
     * @return `true`: success<br></br>`false`: fail
     */
    fun remove(key: String, memoryCache: MemoryCache = defaultMemoryCache): Any? {
        return memoryCache.remove(key)
    }

    /**
     * Clear all of the cache.
     *
     * @param memoryCache The instance of [MemoryCache].
     */
    fun clear(memoryCache: MemoryCache = defaultMemoryCache) {
        memoryCache.clear()
    }

    companion object {
        @Volatile
        private var sMemoryCacheManager: MemoryCacheManager? = null

        @Synchronized
        fun getInstance(): MemoryCacheManager {
            return sMemoryCacheManager ?: synchronized(MemoryCacheManager::class.java) {
                sMemoryCacheManager ?: MemoryCacheManager().also { sMemoryCacheManager = it }
            }
        }
    }
}