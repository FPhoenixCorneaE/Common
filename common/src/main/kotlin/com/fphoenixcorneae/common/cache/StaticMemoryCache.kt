package com.fphoenixcorneae.common.cache

/**
 * @desc：StaticMemoryCache
 * @date：2022/01/05 14:36
 */
object StaticMemoryCache {
    private var sDefaultMemoryCache: MemoryCache? = null

    /**
     * Get the default instance of [MemoryCache].
     */
    private val defaultMemoryCache: MemoryCache
        get() = sDefaultMemoryCache ?: MemoryCache.getInstance()

    /**
     * Set the default instance of [MemoryCache].
     */
    fun setDefaultMemoryCache(memoryCache: MemoryCache) {
        sDefaultMemoryCache = memoryCache
    }

    /**
     * Put bytes in cache.
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
}