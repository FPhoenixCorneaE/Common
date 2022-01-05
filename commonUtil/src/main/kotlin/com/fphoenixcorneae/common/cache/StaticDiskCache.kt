package com.fphoenixcorneae.common.cache

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Parcelable
import org.json.JSONArray
import org.json.JSONObject
import java.io.Serializable

/**
 * @desc：StaticDiskCache
 * @date：2022/01/05 15:00
 */
object StaticDiskCache {
    private var sDefaultDiskCache: DiskCache? = null

    /**
     * Get the default instance of [DiskCache].
     */
    private val defaultDiskCache: DiskCache
        get() = sDefaultDiskCache ?: DiskCache.getInstance()

    /**
     * Set the default instance of [DiskCache].
     */
    fun setDefaultDiskCache(diskCache: DiskCache) {
        sDefaultDiskCache = diskCache
    }

    /**
     * Put bytes in cache.
     *
     * @param key            The key of cache.
     * @param value          The value of cache.
     * @param saveTime       The save time of cache, in seconds.
     * @param diskCache The instance of [DiskCache].
     */
    fun put(
        key: String,
        value: ByteArray?,
        saveTime: Int = -1,
        diskCache: DiskCache = defaultDiskCache
    ) {
        diskCache.put(key, value, saveTime)
    }

    /**
     * Return the bytes in cache.
     *
     * @param key            The key of cache.
     * @param defaultValue   The default value if the cache doesn't exist.
     * @param diskCache The instance of [DiskCache].
     * @return the bytes if cache exists or defaultValue otherwise
     */
    fun getBytes(
        key: String,
        defaultValue: ByteArray? = null,
        diskCache: DiskCache = defaultDiskCache
    ): ByteArray? {
        return diskCache.getBytes(key, defaultValue)
    }

    /**
     * Put string value in cache.
     *
     * @param key            The key of cache.
     * @param value          The value of cache.
     * @param saveTime       The save time of cache, in seconds.
     * @param diskCache The instance of [DiskCache].
     */
    fun put(
        key: String,
        value: String?,
        saveTime: Int = -1,
        diskCache: DiskCache = defaultDiskCache
    ) {
        diskCache.put(key, value, saveTime)
    }

    /**
     * Return the string value in cache.
     *
     * @param key            The key of cache.
     * @param defaultValue   The default value if the cache doesn't exist.
     * @param diskCache The instance of [DiskCache].
     * @return the string value if cache exists or defaultValue otherwise
     */
    fun getString(
        key: String,
        defaultValue: String? = null,
        diskCache: DiskCache = defaultDiskCache
    ): String? {
        return diskCache.getString(key, defaultValue)
    }

    /**
     * Put JSONObject in cache.
     *
     * @param key            The key of cache.
     * @param value          The value of cache.
     * @param saveTime       The save time of cache, in seconds.
     * @param diskCache The instance of [DiskCache].
     */
    fun put(
        key: String,
        value: JSONObject?,
        saveTime: Int = -1,
        diskCache: DiskCache = defaultDiskCache
    ) {
        diskCache.put(key, value, saveTime)
    }

    /**
     * Return the JSONObject in cache.
     *
     * @param key            The key of cache.
     * @param defaultValue   The default value if the cache doesn't exist.
     * @param diskCache The instance of [DiskCache].
     * @return the JSONObject if cache exists or defaultValue otherwise
     */
    fun getJSONObject(
        key: String,
        defaultValue: JSONObject? = null,
        diskCache: DiskCache = defaultDiskCache
    ): JSONObject? {
        return diskCache.getJSONObject(key, defaultValue)
    }

    /**
     * Put JSONArray in cache.
     *
     * @param key            The key of cache.
     * @param value          The value of cache.
     * @param saveTime       The save time of cache, in seconds.
     * @param diskCache The instance of [DiskCache].
     */
    fun put(
        key: String,
        value: JSONArray?,
        saveTime: Int = -1,
        diskCache: DiskCache = defaultDiskCache
    ) {
        diskCache.put(key, value, saveTime)
    }

    /**
     * Return the JSONArray in cache.
     *
     * @param key            The key of cache.
     * @param defaultValue   The default value if the cache doesn't exist.
     * @param diskCache The instance of [DiskCache].
     * @return the JSONArray if cache exists or defaultValue otherwise
     */
    fun getJSONArray(
        key: String,
        defaultValue: JSONArray? = null,
        diskCache: DiskCache = defaultDiskCache
    ): JSONArray? {
        return diskCache.getJSONArray(key, defaultValue)
    }

    /**
     * Put bitmap in cache.
     *
     * @param key            The key of cache.
     * @param value          The value of cache.
     * @param saveTime       The save time of cache, in seconds.
     * @param diskCache The instance of [DiskCache].
     */
    fun put(
        key: String,
        value: Bitmap?,
        saveTime: Int = -1,
        diskCache: DiskCache = defaultDiskCache
    ) {
        diskCache.put(key, value, saveTime)
    }

    /**
     * Return the bitmap in cache.
     *
     * @param key            The key of cache.
     * @param defaultValue   The default value if the cache doesn't exist.
     * @param diskCache The instance of [DiskCache].
     * @return the bitmap if cache exists or defaultValue otherwise
     */
    fun getBitmap(
        key: String,
        defaultValue: Bitmap? = null,
        diskCache: DiskCache = defaultDiskCache
    ): Bitmap? {
        return diskCache.getBitmap(key, defaultValue)
    }

    /**
     * Put drawable in cache.
     *
     * @param key            The key of cache.
     * @param value          The value of cache.
     * @param saveTime       The save time of cache, in seconds.
     * @param diskCache The instance of [DiskCache].
     */
    fun put(
        key: String,
        value: Drawable?,
        saveTime: Int = -1,
        diskCache: DiskCache = defaultDiskCache
    ) {
        diskCache.put(key, value, saveTime)
    }


    /**
     * Return the drawable in cache.
     *
     * @param key            The key of cache.
     * @param defaultValue   The default value if the cache doesn't exist.
     * @param diskCache The instance of [DiskCache].
     * @return the drawable if cache exists or defaultValue otherwise
     */
    fun getDrawable(
        key: String,
        defaultValue: Drawable? = null,
        diskCache: DiskCache = defaultDiskCache
    ): Drawable? {
        return diskCache.getDrawable(key, defaultValue)
    }

    /**
     * Put parcelable in cache.
     *
     * @param key            The key of cache.
     * @param value          The value of cache.
     * @param saveTime       The save time of cache, in seconds.
     * @param diskCache The instance of [DiskCache].
     */
    fun put(
        key: String,
        value: Parcelable?,
        saveTime: Int = -1,
        diskCache: DiskCache = defaultDiskCache
    ) {
        diskCache.put(key, value, saveTime)
    }

    /**
     * Return the parcelable in cache.
     *
     * @param key            The key of cache.
     * @param creator        The creator.
     * @param defaultValue   The default value if the cache doesn't exist.
     * @param diskCache The instance of [DiskCache].
     * @param <T>            The value type.
     * @return the parcelable if cache exists or defaultValue otherwise
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
     * Put serializable in cache.
     *
     * @param key            The key of cache.
     * @param value          The value of cache.
     * @param saveTime       The save time of cache, in seconds.
     * @param diskCache The instance of [DiskCache].
     */

    fun put(
        key: String,
        value: Serializable?,
        saveTime: Int = -1,
        diskCache: DiskCache = defaultDiskCache
    ) {
        diskCache.put(key, value, saveTime)
    }

    /**
     * Return the serializable in cache.
     *
     * @param key            The key of cache.
     * @param defaultValue   The default value if the cache doesn't exist.
     * @param diskCache The instance of [DiskCache].
     * @return the bitmap if cache exists or defaultValue otherwise
     */
    fun getSerializable(
        key: String,
        defaultValue: Any? = null,
        diskCache: DiskCache = defaultDiskCache
    ): Any? {
        return diskCache.getSerializable(key, defaultValue)
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
}