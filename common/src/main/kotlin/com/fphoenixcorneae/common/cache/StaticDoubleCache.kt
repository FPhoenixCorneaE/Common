package com.fphoenixcorneae.common.cache

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Parcelable
import org.json.JSONArray
import org.json.JSONObject
import java.io.Serializable

/**
 * @desc：StaticDoubleCache
 * @date：2022/01/05 15:56
 */
object StaticDoubleCache {
    private var sDefaultDoubleCache: DoubleCache? = null

    /**
     * Get the default instance of [DoubleCache].
     */
    private val defaultDoubleCache: DoubleCache
        get() = sDefaultDoubleCache ?: DoubleCache.getInstance()

    /**
     * Set the default instance of [DoubleCache].
     */
    fun setDefaultDoubleCache(doubleCache: DoubleCache) {
        sDefaultDoubleCache = doubleCache
    }

    /**
     * Put bytes in cache.
     *
     * @param key              The key of cache.
     * @param value            The value of cache.
     * @param saveTime         The save time of cache, in seconds.
     * @param doubleCache      The instance of [DoubleCache].
     */
    fun put(
        key: String,
        value: ByteArray?,
        saveTime: Int = -1,
        doubleCache: DoubleCache = defaultDoubleCache
    ) {
        doubleCache.put(key, value, saveTime)
    }

    /**
     * Return the bytes in cache.
     *
     * @param key              The key of cache.
     * @param defaultValue     The default value if the cache doesn't exist.
     * @param doubleCache      The instance of [DoubleCache].
     * @return the bytes if cache exists or defaultValue otherwise
     */
    fun getBytes(
        key: String,
        defaultValue: ByteArray? = null,
        doubleCache: DoubleCache = defaultDoubleCache
    ): ByteArray? {
        return doubleCache.getBytes(key, defaultValue)
    }

    /**
     * Put string value in cache.
     *
     * @param key              The key of cache.
     * @param value            The value of cache.
     * @param saveTime         The save time of cache, in seconds.
     * @param doubleCache      The instance of [DoubleCache].
     */
    fun put(
        key: String,
        value: String?,
        saveTime: Int = -1,
        doubleCache: DoubleCache = defaultDoubleCache
    ) {
        doubleCache.put(key, value, saveTime)
    }

    /**
     * Return the string value in cache.
     *
     * @param key              The key of cache.
     * @param defaultValue     The default value if the cache doesn't exist.
     * @param doubleCache The instance of [DoubleCache].
     * @return the string value if cache exists or defaultValue otherwise
     */
    fun getString(
        key: String,
        defaultValue: String? = null,
        doubleCache: DoubleCache = defaultDoubleCache
    ): String? {
        return doubleCache.getString(key, defaultValue)
    }

    /**
     * Put JSONObject in cache.
     *
     * @param key              The key of cache.
     * @param value            The value of cache.
     * @param saveTime         The save time of cache, in seconds.
     * @param doubleCache      The instance of [DoubleCache].
     */
    fun put(
        key: String,
        value: JSONObject?,
        saveTime: Int = -1,
        doubleCache: DoubleCache = defaultDoubleCache
    ) {
        doubleCache.put(key, value, saveTime)
    }

    /**
     * Return the JSONObject in cache.
     *
     * @param key              The key of cache.
     * @param defaultValue     The default value if the cache doesn't exist.
     * @param doubleCache The instance of [DoubleCache].
     * @return the JSONObject if cache exists or defaultValue otherwise
     */
    fun getJSONObject(
        key: String,
        defaultValue: JSONObject? = null,
        doubleCache: DoubleCache = defaultDoubleCache
    ): JSONObject? {
        return doubleCache.getJSONObject(key, defaultValue)
    }

    /**
     * Put JSONArray in cache.
     *
     * @param key              The key of cache.
     * @param value            The value of cache.
     * @param saveTime         The save time of cache, in seconds.
     * @param doubleCache      The instance of [DoubleCache].
     */
    fun put(
        key: String,
        value: JSONArray?,
        saveTime: Int = -1,
        doubleCache: DoubleCache = defaultDoubleCache
    ) {
        doubleCache.put(key, value, saveTime)
    }


    /**
     * Return the JSONArray in cache.
     *
     * @param key              The key of cache.
     * @param defaultValue     The default value if the cache doesn't exist.
     * @param doubleCache The instance of [DoubleCache].
     * @return the JSONArray if cache exists or defaultValue otherwise
     */
    fun getJSONArray(
        key: String,
        defaultValue: JSONArray? = null,
        doubleCache: DoubleCache = defaultDoubleCache
    ): JSONArray? {
        return doubleCache.getJSONArray(key, defaultValue)
    }

    /**
     * Put bitmap in cache.
     *
     * @param key              The key of cache.
     * @param value            The value of cache.
     * @param saveTime         The save time of cache, in seconds.
     * @param doubleCache      The instance of [DoubleCache].
     */
    fun put(
        key: String,
        value: Bitmap?,
        saveTime: Int = -1,
        doubleCache: DoubleCache = defaultDoubleCache
    ) {
        doubleCache.put(key, value, saveTime)
    }

    /**
     * Return the bitmap in cache.
     *
     * @param key              The key of cache.
     * @param defaultValue     The default value if the cache doesn't exist.
     * @param doubleCache      The instance of [DoubleCache].
     * @return the bitmap if cache exists or defaultValue otherwise
     */
    fun getBitmap(
        key: String,
        defaultValue: Bitmap? = null,
        doubleCache: DoubleCache = defaultDoubleCache
    ): Bitmap? {
        return doubleCache.getBitmap(key, defaultValue)
    }

    /**
     * Put drawable in cache.
     *
     * @param key              The key of cache.
     * @param value            The value of cache.
     * @param saveTime         The save time of cache, in seconds.
     * @param doubleCache      The instance of [DoubleCache].
     */
    fun put(
        key: String,
        value: Drawable?,
        saveTime: Int = -1,
        doubleCache: DoubleCache = defaultDoubleCache
    ) {
        doubleCache.put(key, value, saveTime)
    }

    /**
     * Return the drawable in cache.
     *
     * @param key              The key of cache.
     * @param defaultValue     The default value if the cache doesn't exist.
     * @param doubleCache The instance of [DoubleCache].
     * @return the drawable if cache exists or defaultValue otherwise
     */
    fun getDrawable(
        key: String,
        defaultValue: Drawable? = null,
        doubleCache: DoubleCache = defaultDoubleCache
    ): Drawable? {
        return doubleCache.getDrawable(key, defaultValue)
    }

    /**
     * Put parcelable in cache.
     *
     * @param key              The key of cache.
     * @param value            The value of cache.
     * @param saveTime         The save time of cache, in seconds.
     * @param doubleCache      The instance of [DoubleCache].
     */
    fun put(
        key: String,
        value: Parcelable?,
        saveTime: Int = -1,
        doubleCache: DoubleCache = defaultDoubleCache
    ) {
        doubleCache.put(key, value, saveTime)
    }

    /**
     * Return the parcelable in cache.
     *
     * @param key              The key of cache.
     * @param creator          The creator.
     * @param defaultValue     The default value if the cache doesn't exist.
     * @param doubleCache      The instance of [DoubleCache].
     * @param <T>              The value type.
     * @return the parcelable if cache exists or defaultValue otherwise
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
     * Put serializable in cache.
     *
     * @param key              The key of cache.
     * @param value            The value of cache.
     * @param saveTime         The save time of cache, in seconds.
     * @param doubleCache      The instance of [DoubleCache].
     */
    fun put(
        key: String,
        value: Serializable?,
        saveTime: Int = -1,
        doubleCache: DoubleCache = defaultDoubleCache
    ) {
        doubleCache.put(key, value, saveTime)
    }

    /**
     * Return the serializable in cache.
     *
     * @param key              The key of cache.
     * @param defaultValue     The default value if the cache doesn't exist.
     * @param doubleCache The instance of [DoubleCache].
     * @return the bitmap if cache exists or defaultValue otherwise
     */
    fun getSerializable(
        key: String,
        defaultValue: Any? = null,
        doubleCache: DoubleCache = defaultDoubleCache
    ): Any? {
        return doubleCache.getSerializable(key, defaultValue)
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
}