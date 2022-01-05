package com.fphoenixcorneae.common.cache

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Parcelable
import org.json.JSONArray
import org.json.JSONObject
import java.io.Serializable
import java.util.*

/**
 * @desc：双重缓存
 * @date：2022/01/05 14:45
 */
class DoubleCache private constructor(
    private val mMemoryCache: MemoryCache,
    private val mDiskCache: DiskCache
) {
    /**
     * Put bytes in cache.
     *
     * @param key      The key of cache.
     * @param value    The value of cache.
     * @param saveTime The save time of cache, in seconds.
     */
    fun put(key: String, value: ByteArray?, saveTime: Int = -1) {
        mMemoryCache.put(key, value, saveTime)
        mDiskCache.put(key, value, saveTime)
    }

    /**
     * Return the bytes in cache.
     *
     * @param key          The key of cache.
     * @param defaultValue The default value if the cache doesn't exist.
     * @return the bytes if cache exists or defaultValue otherwise
     */
    fun getBytes(key: String, defaultValue: ByteArray? = null): ByteArray? {
        return mMemoryCache.get<ByteArray>(key)
            ?: mDiskCache.getBytes(key)?.also { mMemoryCache.put(key, it) }
            ?: defaultValue
    }

    /**
     * Put string value in cache.
     *
     * @param key      The key of cache.
     * @param value    The value of cache.
     * @param saveTime The save time of cache, in seconds.
     */
    fun put(key: String, value: String?, saveTime: Int = -1) {
        mMemoryCache.put(key, value, saveTime)
        mDiskCache.put(key, value, saveTime)
    }

    /**
     * Return the string value in cache.
     *
     * @param key          The key of cache.
     * @param defaultValue The default value if the cache doesn't exist.
     * @return the string value if cache exists or defaultValue otherwise
     */
    fun getString(key: String, defaultValue: String? = null): String? {
        return mMemoryCache.get<String>(key)
            ?: mDiskCache.getString(key)?.also { mMemoryCache.put(key, it) }
            ?: defaultValue
    }

    /**
     * Put JSONObject in cache.
     *
     * @param key      The key of cache.
     * @param value    The value of cache.
     * @param saveTime The save time of cache, in seconds.
     */
    fun put(key: String, value: JSONObject?, saveTime: Int = -1) {
        mMemoryCache.put(key, value, saveTime)
        mDiskCache.put(key, value, saveTime)
    }

    /**
     * Return the JSONObject in cache.
     *
     * @param key          The key of cache.
     * @param defaultValue The default value if the cache doesn't exist.
     * @return the JSONObject if cache exists or defaultValue otherwise
     */
    fun getJSONObject(key: String, defaultValue: JSONObject? = null): JSONObject? {
        return mMemoryCache.get<JSONObject>(key)
            ?: mDiskCache.getJSONObject(key)?.also { mMemoryCache.put(key, it) }
            ?: defaultValue
    }

    /**
     * Put JSONArray in cache.
     *
     * @param key      The key of cache.
     * @param value    The value of cache.
     * @param saveTime The save time of cache, in seconds.
     */
    fun put(key: String, value: JSONArray?, saveTime: Int = -1) {
        mMemoryCache.put(key, value, saveTime)
        mDiskCache.put(key, value, saveTime)
    }

    /**
     * Return the JSONArray in cache.
     *
     * @param key          The key of cache.
     * @param defaultValue The default value if the cache doesn't exist.
     * @return the JSONArray if cache exists or defaultValue otherwise
     */
    fun getJSONArray(key: String, defaultValue: JSONArray? = null): JSONArray? {
        return mMemoryCache.get<JSONArray>(key)
            ?: mDiskCache.getJSONArray(key)?.also { mMemoryCache.put(key, it) }
            ?: defaultValue
    }

    /**
     * Put bitmap in cache.
     *
     * @param key      The key of cache.
     * @param value    The value of cache.
     * @param saveTime The save time of cache, in seconds.
     */
    fun put(key: String, value: Bitmap?, saveTime: Int = -1) {
        mMemoryCache.put(key, value, saveTime)
        mDiskCache.put(key, value, saveTime)
    }

    /**
     * Return the bitmap in cache.
     *
     * @param key          The key of cache.
     * @param defaultValue The default value if the cache doesn't exist.
     * @return the bitmap if cache exists or defaultValue otherwise
     */
    fun getBitmap(key: String, defaultValue: Bitmap? = null): Bitmap? {
        return mMemoryCache.get<Bitmap>(key)
            ?: mDiskCache.getBitmap(key)?.also { mMemoryCache.put(key, it) }
            ?: defaultValue
    }

    /**
     * Put drawable in cache.
     *
     * @param key      The key of cache.
     * @param value    The value of cache.
     * @param saveTime The save time of cache, in seconds.
     */
    fun put(key: String, value: Drawable?, saveTime: Int = -1) {
        mMemoryCache.put(key, value, saveTime)
        mDiskCache.put(key, value, saveTime)
    }

    /**
     * Return the drawable in cache.
     *
     * @param key          The key of cache.
     * @param defaultValue The default value if the cache doesn't exist.
     * @return the drawable if cache exists or defaultValue otherwise
     */
    fun getDrawable(key: String, defaultValue: Drawable? = null): Drawable? {
        return mMemoryCache.get<Drawable>(key)
            ?: mDiskCache.getDrawable(key)?.also { mMemoryCache.put(key, it) }
            ?: defaultValue
    }

    /**
     * Put parcelable in cache.
     *
     * @param key      The key of cache.
     * @param value    The value of cache.
     * @param saveTime The save time of cache, in seconds.
     */
    fun put(key: String, value: Parcelable?, saveTime: Int = -1) {
        mMemoryCache.put(key, value, saveTime)
        mDiskCache.put(key, value, saveTime)
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
     * Put serializable in cache.
     *
     * @param key      The key of cache.
     * @param value    The value of cache.
     * @param saveTime The save time of cache, in seconds.
     */
    fun put(key: String, value: Serializable?, saveTime: Int = -1) {
        mMemoryCache.put(key, value, saveTime)
        mDiskCache.put(key, value, saveTime)
    }

    /**
     * Return the serializable in cache.
     *
     * @param key          The key of cache.
     * @param defaultValue The default value if the cache doesn't exist.
     * @return the bitmap if cache exists or defaultValue otherwise
     */
    fun getSerializable(key: String, defaultValue: Any? = null): Any? {
        return mMemoryCache.get<Any>(key)
            ?: mDiskCache.getSerializable(key)?.also { mMemoryCache.put(key, it) }
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