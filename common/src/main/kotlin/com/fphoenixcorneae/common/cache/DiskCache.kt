package com.fphoenixcorneae.common.cache

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Parcelable
import com.fphoenixcorneae.common.ext.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.Serializable
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong

/**
 * @desc：磁盘缓存
 * @date：2022/01/05 13:56
 */
class DiskCache private constructor(
    private val mCacheKey: String,
    private val mCacheDir: File,
    private val mMaxSize: Long,
    private val mMaxCount: Int
) {
    private var mDiskCacheManager: DiskCacheManager? = null
        get() {
            if (mCacheDir.exists()) {
                if (field == null) {
                    mDiskCacheManager = DiskCacheManager(mCacheDir, mMaxSize, mMaxCount)
                }
            } else {
                if (mCacheDir.mkdirs()) {
                    mDiskCacheManager = DiskCacheManager(mCacheDir, mMaxSize, mMaxCount)
                } else {
                    "can't make dirs in " + mCacheDir.absolutePath.loge("DiskCache")
                }
            }
            return field
        }

    override fun toString(): String {
        return mCacheKey + "@" + Integer.toHexString(hashCode())
    }

    /**
     * Put bytes in cache.
     *
     * @param key   The key of cache.
     * @param value The value of cache.
     * @param saveTime The save time of cache, in seconds.
     */
    fun put(key: String, value: ByteArray?, saveTime: Int = -1) {
        realPutBytes(TYPE_BYTE + key, value, saveTime)
    }

    private fun realPutBytes(key: String, value: ByteArray?, saveTime: Int) {
        var value = value ?: return
        val diskCacheManager = mDiskCacheManager ?: return
        if (saveTime >= 0) {
            value = DiskCacheHelper.newByteArrayWithTime(saveTime, value)
        }
        val file = diskCacheManager.getFileBeforePut(key)
        writeFileFromBytesByChannel(file, value)
        diskCacheManager.updateModify(file)
        diskCacheManager.put(file)
    }

    /**
     * Return the bytes in cache.
     *
     * @param key          The key of cache.
     * @param defaultValue The default value if the cache doesn't exist.
     * @return the bytes if cache exists or defaultValue otherwise
     */
    fun getBytes(key: String, defaultValue: ByteArray? = null): ByteArray? {
        return realGetBytes(TYPE_BYTE + key, defaultValue)
    }

    private fun realGetBytes(key: String, defaultValue: ByteArray? = null): ByteArray? {
        val diskCacheManager = mDiskCacheManager ?: return defaultValue
        val file = diskCacheManager.getFileIfExists(key) ?: return defaultValue
        val data: ByteArray = readFile2Bytes(file) ?: return null
        if (DiskCacheHelper.isDue(data)) {
            diskCacheManager.removeByKey(key)
            return defaultValue
        }
        diskCacheManager.updateModify(file)
        return DiskCacheHelper.getDataWithoutDueTime(data)
    }

    /**
     * Put string value in cache.
     *
     * @param key      The key of cache.
     * @param value    The value of cache.
     * @param saveTime The save time of cache, in seconds.
     */
    fun put(key: String, value: String?, saveTime: Int = -1) {
        realPutBytes(TYPE_STRING + key, value?.toBytes(), saveTime)
    }

    /**
     * Return the string value in cache.
     *
     * @param key          The key of cache.
     * @param defaultValue The default value if the cache doesn't exist.
     * @return the string value if cache exists or defaultValue otherwise
     */
    fun getString(key: String, defaultValue: String? = null): String? {
        val bytes = realGetBytes(TYPE_STRING + key) ?: return defaultValue
        return bytes.toString()
    }

    /**
     * Put JSONObject in cache.
     *
     * @param key      The key of cache.
     * @param value    The value of cache.
     * @param saveTime The save time of cache, in seconds.
     */
    fun put(key: String, value: JSONObject?, saveTime: Int = -1) {
        realPutBytes(TYPE_JSON_OBJECT + key, value?.toString()?.toBytes(), saveTime)
    }

    /**
     * Return the JSONObject in cache.
     *
     * @param key          The key of cache.
     * @param defaultValue The default value if the cache doesn't exist.
     * @return the JSONObject if cache exists or defaultValue otherwise
     */
    fun getJSONObject(key: String, defaultValue: JSONObject? = null): JSONObject? {
        val bytes = realGetBytes(TYPE_JSON_OBJECT + key) ?: return defaultValue
        return bytes.toJSONObject()
    }

    /**
     * Put JSONArray in cache.
     *
     * @param key      The key of cache.
     * @param value    The value of cache.
     * @param saveTime The save time of cache, in seconds.
     */
    fun put(key: String, value: JSONArray?, saveTime: Int = -1) {
        realPutBytes(TYPE_JSON_ARRAY + key, value?.toString()?.toBytes(), saveTime)
    }

    /**
     * Return the JSONArray in cache.
     *
     * @param key          The key of cache.
     * @param defaultValue The default value if the cache doesn't exist.
     * @return the JSONArray if cache exists or defaultValue otherwise
     */
    fun getJSONArray(key: String, defaultValue: JSONArray? = null): JSONArray? {
        val bytes = realGetBytes(TYPE_JSON_ARRAY + key) ?: return defaultValue
        return bytes.toJSONArray()
    }

    /**
     * Put bitmap in cache.
     *
     * @param key      The key of cache.
     * @param value    The value of cache.
     * @param saveTime The save time of cache, in seconds.
     */
    fun put(key: String, value: Bitmap?, saveTime: Int = -1) {
        realPutBytes(TYPE_BITMAP + key, value.toBytes(), saveTime)
    }

    /**
     * Return the bitmap in cache.
     *
     * @param key          The key of cache.
     * @param defaultValue The default value if the cache doesn't exist.
     * @return the bitmap if cache exists or defaultValue otherwise
     */
    fun getBitmap(key: String, defaultValue: Bitmap? = null): Bitmap? {
        val bytes = realGetBytes(TYPE_BITMAP + key) ?: return defaultValue
        return bytes.toBitmap()
    }

    /**
     * Put drawable in cache.
     *
     * @param key      The key of cache.
     * @param value    The value of cache.
     * @param saveTime The save time of cache, in seconds.
     */
    fun put(key: String, value: Drawable?, saveTime: Int = -1) {
        realPutBytes(TYPE_DRAWABLE + key, value.toBytes(), saveTime)
    }

    /**
     * Return the drawable in cache.
     *
     * @param key          The key of cache.
     * @param defaultValue The default value if the cache doesn't exist.
     * @return the drawable if cache exists or defaultValue otherwise
     */
    fun getDrawable(key: String, defaultValue: Drawable? = null): Drawable? {
        val bytes = realGetBytes(TYPE_DRAWABLE + key) ?: return defaultValue
        return bytes.toDrawable()
    }

    /**
     * Put parcelable in cache.
     *
     * @param key      The key of cache.
     * @param value    The value of cache.
     * @param saveTime The save time of cache, in seconds.
     */
    fun put(key: String, value: Parcelable?, saveTime: Int = -1) {
        realPutBytes(TYPE_PARCELABLE + key, value.toBytes(), saveTime)
    }

    /**
     * Return the parcelable in cache.
     *
     * @param key          The key of cache.
     * @param creator      The creator.
     * @param defaultValue The default value if the cache doesn't exist.
     * @param <T>          The value type.
     * @return the parcelable if cache exists or defaultValue otherwise
     */
    fun <T> getParcelable(
        key: String,
        creator: Parcelable.Creator<T>,
        defaultValue: T? = null
    ): T? {
        val bytes = realGetBytes(TYPE_PARCELABLE + key) ?: return defaultValue
        return bytes.toParcelable(creator)
    }

    /**
     * Put serializable in cache.
     *
     * @param key      The key of cache.
     * @param value    The value of cache.
     * @param saveTime The save time of cache, in seconds.
     */
    fun put(key: String, value: Serializable?, saveTime: Int = -1) {
        realPutBytes(TYPE_SERIALIZABLE + key, value.toBytes(), saveTime)
    }

    /**
     * Return the serializable in cache.
     *
     * @param key          The key of cache.
     * @param defaultValue The default value if the cache doesn't exist.
     * @return the bitmap if cache exists or defaultValue otherwise
     */
    fun getSerializable(key: String, defaultValue: Any? = null): Any? {
        val bytes = realGetBytes(TYPE_SERIALIZABLE + key) ?: return defaultValue
        return bytes.toObject()
    }

    /**
     * Return the size of cache, in bytes.
     *
     * @return the size of cache, in bytes
     */
    val cacheSize: Long
        get() = mDiskCacheManager?.getCacheSize() ?: 0

    /**
     * Return the count of cache.
     *
     * @return the count of cache
     */
    val cacheCount: Int
        get() = mDiskCacheManager?.getCacheCount() ?: 0

    /**
     * Remove the cache by key.
     *
     * @param key The key of cache.
     * @return `true`: success<br></br>`false`: fail
     */
    fun remove(key: String): Boolean {
        val diskCacheManager = mDiskCacheManager ?: return true
        return (diskCacheManager.removeByKey(TYPE_BYTE + key)
                && diskCacheManager.removeByKey(TYPE_STRING + key)
                && diskCacheManager.removeByKey(TYPE_JSON_OBJECT + key)
                && diskCacheManager.removeByKey(TYPE_JSON_ARRAY + key)
                && diskCacheManager.removeByKey(TYPE_BITMAP + key)
                && diskCacheManager.removeByKey(TYPE_DRAWABLE + key)
                && diskCacheManager.removeByKey(TYPE_PARCELABLE + key)
                && diskCacheManager.removeByKey(TYPE_SERIALIZABLE + key))
    }

    /**
     * Clear all of the cache.
     *
     * @return `true`: success<br></br>`false`: fail
     */
    fun clear(): Boolean = mDiskCacheManager?.clear() ?: true

    private class DiskCacheManager(
        private val cacheDir: File,
        private val sizeLimit: Long,
        private val countLimit: Int
    ) {
        private val cacheSize: AtomicLong
        private val cacheCount: AtomicInteger
        private val lastUsageDates = Collections.synchronizedMap(HashMap<File, Long>())
        private val mThread: Thread

        fun getCacheSize(): Long {
            wait2InitOk()
            return cacheSize.get()
        }

        fun getCacheCount(): Int {
            wait2InitOk()
            return cacheCount.get()
        }

        fun getFileBeforePut(key: String): File {
            wait2InitOk()
            val file = File(cacheDir, getCacheNameByKey(key))
            if (file.exists()) {
                cacheCount.addAndGet(-1)
                cacheSize.addAndGet(-file.length())
            }
            return file
        }

        private fun wait2InitOk() {
            try {
                mThread.join()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }

        fun getFileIfExists(key: String): File? {
            val file = File(cacheDir, getCacheNameByKey(key))
            return if (!file.exists()) null else file
        }

        private fun getCacheNameByKey(key: String): String {
            return CACHE_PREFIX + key.substring(0, 3) + key.substring(3).hashCode()
        }

        fun put(file: File) {
            cacheCount.addAndGet(1)
            cacheSize.addAndGet(file.length())
            while (cacheCount.get() > countLimit || cacheSize.get() > sizeLimit) {
                cacheSize.addAndGet(-removeOldest())
                cacheCount.addAndGet(-1)
            }
        }

        fun updateModify(file: File) {
            val millis = System.currentTimeMillis()
            file.setLastModified(millis)
            lastUsageDates[file] = millis
        }

        fun removeByKey(key: String): Boolean {
            val file = getFileIfExists(key) ?: return true
            if (!file.delete()) {
                return false
            }
            cacheSize.addAndGet(-file.length())
            cacheCount.addAndGet(-1)
            lastUsageDates.remove(file)
            return true
        }

        fun clear(): Boolean {
            val files = cacheDir.listFiles { dir, name -> name.startsWith(CACHE_PREFIX) }
            if (files == null || files.isEmpty()) {
                return true
            }
            var flag = true
            for (file in files) {
                if (!file.delete()) {
                    flag = false
                    continue
                }
                cacheSize.addAndGet(-file.length())
                cacheCount.addAndGet(-1)
                lastUsageDates.remove(file)
            }
            if (flag) {
                lastUsageDates.clear()
                cacheSize.set(0)
                cacheCount.set(0)
            }
            return flag
        }

        /**
         * Remove the oldest files.
         *
         * @return the size of oldest files, in bytes
         */
        private fun removeOldest(): Long {
            if (lastUsageDates.isEmpty()) {
                return 0
            }
            var oldestUsage = Long.MAX_VALUE
            var oldestFile: File? = null
            val entries: Set<Map.Entry<File, Long>> = lastUsageDates.entries
            synchronized(lastUsageDates) {
                for ((key, lastValueUsage) in entries) {
                    if (lastValueUsage < oldestUsage) {
                        oldestUsage = lastValueUsage
                        oldestFile = key
                    }
                }
            }
            if (oldestFile == null) {
                return 0
            }
            val fileSize = oldestFile!!.length()
            if (oldestFile!!.delete()) {
                lastUsageDates.remove(oldestFile)
                return fileSize
            }
            return 0
        }

        init {
            cacheSize = AtomicLong()
            cacheCount = AtomicInteger()
            mThread = Thread {
                var size = 0
                var count = 0
                val cachedFiles = cacheDir.listFiles { dir, name -> name.startsWith(CACHE_PREFIX) }
                if (cachedFiles != null) {
                    for (cachedFile in cachedFiles) {
                        size += cachedFile.length().toInt()
                        count += 1
                        lastUsageDates[cachedFile] = cachedFile.lastModified()
                    }
                    cacheSize.getAndAdd(size.toLong())
                    cacheCount.getAndAdd(count)
                }
            }
            mThread.start()
        }
    }

    private object DiskCacheHelper {
        const val TIME_INFO_LEN = 14

        fun newByteArrayWithTime(second: Int, data: ByteArray): ByteArray {
            val time = createDueTime(second).toByteArray()
            val content = ByteArray(time.size + data.size)
            System.arraycopy(time, 0, content, 0, time.size)
            System.arraycopy(data, 0, content, time.size, data.size)
            return content
        }

        /**
         * Return the string of due time.
         *
         * @param seconds The seconds.
         * @return the string of due time
         */
        private fun createDueTime(seconds: Int): String {
            return String.format(
                Locale.getDefault(),
                "_$%010d\$_",
                System.currentTimeMillis() / 1000 + seconds
            )
        }

        fun isDue(data: ByteArray): Boolean {
            val millis = getDueTime(data)
            return millis != -1L && System.currentTimeMillis() > millis
        }

        private fun getDueTime(data: ByteArray): Long {
            if (hasTimeInfo(data)) {
                val millis = String(copyOfRange(data, 2, 12))
                return try {
                    millis.toLong() * 1000
                } catch (e: NumberFormatException) {
                    -1
                }
            }
            return -1
        }

        fun getDataWithoutDueTime(data: ByteArray): ByteArray {
            return if (hasTimeInfo(data)) {
                copyOfRange(data, TIME_INFO_LEN, data.size)
            } else data
        }

        private fun copyOfRange(original: ByteArray, from: Int, to: Int): ByteArray {
            val newLength = to - from
            require(newLength >= 0) { "$from > $to" }
            val copy = ByteArray(newLength)
            System.arraycopy(original, from, copy, 0, Math.min(original.size - from, newLength))
            return copy
        }

        private fun hasTimeInfo(data: ByteArray?): Boolean {
            return data != null && data.size >= TIME_INFO_LEN
                    && data[0] == '_'.code.toByte()
                    && data[1] == '$'.code.toByte()
                    && data[12] == '$'.code.toByte()
                    && data[13] == '_'.code.toByte()
        }
    }

    companion object {
        private const val DEFAULT_MAX_SIZE = Long.MAX_VALUE
        private const val DEFAULT_MAX_COUNT = Int.MAX_VALUE
        private const val CACHE_PREFIX = "cdu_"
        private const val TYPE_BYTE = "by_"
        private const val TYPE_STRING = "st_"
        private const val TYPE_JSON_OBJECT = "jo_"
        private const val TYPE_JSON_ARRAY = "ja_"
        private const val TYPE_BITMAP = "bi_"
        private const val TYPE_DRAWABLE = "dr_"
        private const val TYPE_PARCELABLE = "pa_"
        private const val TYPE_SERIALIZABLE = "se_"
        private val MAP: MutableMap<String, DiskCache> = HashMap()

        /**
         * Return the single [DiskCache] instance.
         *
         * cache directory: /data/data/package/cache/cacheName
         *
         * @param cacheName The name of cache.
         * @param maxSize   The max size of cache, in bytes.
         * @param maxCount  The max count of cache.
         * @return the single [DiskCache] instance
         */
        fun getInstance(
            cacheName: String = "",
            maxSize: Long = DEFAULT_MAX_SIZE,
            maxCount: Int = DEFAULT_MAX_COUNT
        ): DiskCache {
            var cacheName = cacheName
            if (cacheName.isSpace()) {
                cacheName = "diskCache"
            }
            val file = File(appContext.cacheDir, cacheName)
            return getInstance(file, maxSize, maxCount)
        }

        /**
         * Return the single [DiskCache] instance.
         *
         * @param cacheDir The directory of cache.
         * @param maxSize  The max size of cache, in bytes.
         * @param maxCount The max count of cache.
         * @return the single [DiskCache] instance
         */
        fun getInstance(
            cacheDir: File,
            maxSize: Long = DEFAULT_MAX_SIZE,
            maxCount: Int = DEFAULT_MAX_COUNT
        ): DiskCache {
            val cacheKey = cacheDir.absoluteFile.toString() + "_" + maxSize + "_" + maxCount
            var cache = MAP[cacheKey]
            if (cache == null) {
                synchronized(DiskCache::class.java) {
                    cache = MAP[cacheKey]
                    if (cache == null) {
                        cache = DiskCache(cacheKey, cacheDir, maxSize, maxCount)
                        MAP[cacheKey] = cache!!
                    }
                }
            }
            return cache!!
        }
    }
}