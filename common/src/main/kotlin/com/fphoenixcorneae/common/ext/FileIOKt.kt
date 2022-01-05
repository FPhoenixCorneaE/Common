package com.fphoenixcorneae.common.ext

import java.io.*
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.util.*

typealias OnFileProgressUpdateListener = (progress: Double) -> Unit

/**
 * Default size equals 512KB.
 */
var sBufferSize = 524288

/**
 * Write file from input stream.
 *
 * @param filePath The path of file.
 * @param is       The input stream.
 * @param append   True to append, false otherwise.
 * @param listener The progress update listener.
 * @return `true`: success<br></br>`false`: fail
 */
fun writeFileFromIS(
    filePath: String?,
    `is`: InputStream?,
    append: Boolean = false,
    listener: OnFileProgressUpdateListener? = null
): Boolean {
    return writeFileFromIS(getFileByPath(filePath), `is`, append, listener)
}

/**
 * Write file from input stream.
 *
 * @param file     The file.
 * @param is       The input stream.
 * @param append   True to append, false otherwise.
 * @param listener The progress update listener.
 * @return `true`: success<br></br>`false`: fail
 */
fun writeFileFromIS(
    file: File?,
    `is`: InputStream?,
    append: Boolean = false,
    listener: OnFileProgressUpdateListener? = null
): Boolean {
    if (`is` == null || !createOrExistsFile(file)) {
        "create file <$file> failed.".loge()
        return false
    }
    var os: OutputStream? = null
    return try {
        os = BufferedOutputStream(FileOutputStream(file, append), sBufferSize)
        if (listener == null) {
            val data = ByteArray(sBufferSize)
            var len: Int
            while (`is`.read(data).also { len = it } != -1) {
                os.write(data, 0, len)
            }
        } else {
            val totalSize = `is`.available().toDouble()
            var curSize = 0
            listener(0.0)
            val data = ByteArray(sBufferSize)
            var len: Int
            while (`is`.read(data).also { len = it } != -1) {
                os.write(data, 0, len)
                curSize += len
                listener(curSize / totalSize)
            }
        }
        true
    } catch (e: IOException) {
        e.printStackTrace()
        false
    } finally {
        closeIO(`is`, os)
    }
}

/**
 * Write file from bytes by stream.
 *
 * @param filePath The path of file.
 * @param bytes    The bytes.
 * @param append   True to append, false otherwise.
 * @param listener The progress update listener.
 * @return `true`: success<br></br>`false`: fail
 */
fun writeFileFromBytesByStream(
    filePath: String?,
    bytes: ByteArray?,
    append: Boolean = false,
    listener: OnFileProgressUpdateListener? = null
): Boolean {
    return writeFileFromBytesByStream(getFileByPath(filePath), bytes, append, listener)
}

/**
 * Write file from bytes by stream.
 *
 * @param file     The file.
 * @param bytes    The bytes.
 * @param append   True to append, false otherwise.
 * @param listener The progress update listener.
 * @return `true`: success<br></br>`false`: fail
 */
fun writeFileFromBytesByStream(
    file: File?,
    bytes: ByteArray?,
    append: Boolean = false,
    listener: OnFileProgressUpdateListener? = null
): Boolean {
    return if (bytes == null) {
        false
    } else {
        writeFileFromIS(file, ByteArrayInputStream(bytes), append, listener)
    }
}

/**
 * Write file from bytes by channel.
 *
 * @param filePath The path of file.
 * @param bytes    The bytes.
 * @param append   True to append, false otherwise.
 * @param isForce  True to force write file, false otherwise.
 * @return `true`: success<br></br>`false`: fail
 */
fun writeFileFromBytesByChannel(
    filePath: String?,
    bytes: ByteArray?,
    append: Boolean = false,
    isForce: Boolean = true
): Boolean {
    return writeFileFromBytesByChannel(getFileByPath(filePath), bytes, append, isForce)
}

/**
 * Write file from bytes by channel.
 *
 * @param file    The file.
 * @param bytes   The bytes.
 * @param append  True to append, false otherwise.
 * @param isForce True to force write file, false otherwise.
 * @return `true`: success<br></br>`false`: fail
 */
fun writeFileFromBytesByChannel(
    file: File?,
    bytes: ByteArray?,
    append: Boolean = false,
    isForce: Boolean = true
): Boolean {
    if (bytes == null) {
        return false
    }
    if (!createOrExistsFile(file)) {
        "create file <$file> failed.".loge()
        return false
    }
    var fc: FileChannel? = null
    return try {
        fc = FileOutputStream(file, append).channel
        if (fc == null) {
            "fc is null.".loge()
            return false
        }
        fc.position(fc.size())
        fc.write(ByteBuffer.wrap(bytes))
        if (isForce) {
            fc.force(true)
        }
        true
    } catch (e: IOException) {
        e.printStackTrace()
        false
    } finally {
        fc.closeSafely()
    }
}

/**
 * Write file from bytes by map.
 *
 * @param filePath The path of file.
 * @param bytes    The bytes.
 * @param append   True to append, false otherwise.
 * @param isForce  True to force write file, false otherwise.
 * @return `true`: success<br></br>`false`: fail
 */
fun writeFileFromBytesByMap(
    filePath: String?,
    bytes: ByteArray?,
    append: Boolean = false,
    isForce: Boolean = true
): Boolean {
    return writeFileFromBytesByMap(getFileByPath(filePath), bytes, append, isForce)
}

/**
 * Write file from bytes by map.
 *
 * @param file    The file.
 * @param bytes   The bytes.
 * @param append  True to append, false otherwise.
 * @param isForce True to force write file, false otherwise.
 * @return `true`: success<br></br>`false`: fail
 */
fun writeFileFromBytesByMap(
    file: File?,
    bytes: ByteArray?,
    append: Boolean = false,
    isForce: Boolean = true
): Boolean {
    if (bytes == null || !createOrExistsFile(file)) {
        "create file <$file> failed.".loge()
        return false
    }
    var fc: FileChannel? = null
    return try {
        fc = FileOutputStream(file, append).channel
        if (fc == null) {
            "fc is null.".loge()
            return false
        }
        val mbb = fc.map(FileChannel.MapMode.READ_WRITE, fc.size(), bytes.size.toLong())
        mbb.put(bytes)
        if (isForce) mbb.force()
        true
    } catch (e: IOException) {
        e.printStackTrace()
        false
    } finally {
        try {
            fc?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}

/**
 * Write file from string.
 *
 * @param filePath The path of file.
 * @param content  The string of content.
 * @param append   True to append, false otherwise.
 * @return `true`: success<br></br>`false`: fail
 */
fun writeFileFromString(
    filePath: String?,
    content: String?,
    append: Boolean = false
): Boolean {
    return writeFileFromString(getFileByPath(filePath), content, append)
}

/**
 * Write file from string.
 *
 * @param file    The file.
 * @param content The string of content.
 * @param append  True to append, false otherwise.
 * @return `true`: success<br></br>`false`: fail
 */
fun writeFileFromString(
    file: File?,
    content: String?,
    append: Boolean = false
): Boolean {
    if (file == null || content == null) {
        return false
    }
    if (!createOrExistsFile(file)) {
        "create file <$file> failed.".loge()
        return false
    }
    var bw: BufferedWriter? = null
    return try {
        bw = BufferedWriter(FileWriter(file, append))
        bw.write(content)
        true
    } catch (e: IOException) {
        e.printStackTrace()
        false
    } finally {
        bw.closeSafely()
    }
}

/**
 * Return the lines in file.
 *
 * @param filePath    The path of file.
 * @param st          The line's index of start.
 * @param end         The line's index of end.
 * @param charsetName The name of charset.
 * @return the lines in file
 */
fun readFile2List(
    filePath: String?,
    st: Int = 0,
    end: Int = 0x7FFFFFFF,
    charsetName: String? = null
): List<String>? {
    return readFile2List(getFileByPath(filePath), st, end, charsetName)
}

/**
 * Return the lines in file.
 *
 * @param file        The file.
 * @param st          The line's index of start.
 * @param end         The line's index of end.
 * @param charsetName The name of charset.
 * @return the lines in file
 */
fun readFile2List(
    file: File?,
    st: Int = 0,
    end: Int = 0x7FFFFFFF,
    charsetName: String? = null
): List<String>? {
    if (!isFileExists(file)) return null
    if (st > end) return null
    var reader: BufferedReader? = null
    return try {
        var line: String
        var curLine = 1
        val list: MutableList<String> = ArrayList()
        reader = if (charsetName.isSpace()) {
            BufferedReader(InputStreamReader(FileInputStream(file)))
        } else {
            BufferedReader(
                InputStreamReader(FileInputStream(file), charsetName)
            )
        }
        while (reader.readLine().also { line = it } != null) {
            if (curLine > end) {
                break
            }
            if (curLine in st..end) {
                list.add(line)
            }
            ++curLine
        }
        list
    } catch (e: IOException) {
        e.printStackTrace()
        null
    } finally {
        reader.closeSafely()
    }
}


/**
 * Return the bytes in file by stream.
 *
 * @param filePath The path of file.
 * @param listener The progress update listener.
 * @return the bytes in file
 */
fun readFile2BytesByStream(
    filePath: String?,
    listener: OnFileProgressUpdateListener? = null
): ByteArray? {
    return readFile2BytesByStream(getFileByPath(filePath), listener)
}

/**
 * Return the bytes in file by stream.
 *
 * @param file     The file.
 * @param listener The progress update listener.
 * @return the bytes in file
 */
fun readFile2BytesByStream(
    file: File?,
    listener: OnFileProgressUpdateListener? = null
): ByteArray? {
    return if (!isFileExists(file)) {
        null
    } else {
        try {
            var os: ByteArrayOutputStream? = null
            val `is`: InputStream = BufferedInputStream(FileInputStream(file), sBufferSize)
            try {
                os = ByteArrayOutputStream()
                val b = ByteArray(sBufferSize)
                var len: Int
                if (listener == null) {
                    while (`is`.read(b, 0, sBufferSize).also { len = it } != -1) {
                        os.write(b, 0, len)
                    }
                } else {
                    val totalSize = `is`.available().toDouble()
                    var curSize = 0
                    listener(0.0)
                    while (`is`.read(b, 0, sBufferSize).also { len = it } != -1) {
                        os.write(b, 0, len)
                        curSize += len
                        listener(curSize / totalSize)
                    }
                }
                os.toByteArray()
            } catch (e: IOException) {
                e.printStackTrace()
                null
            } finally {
                closeIO(`is`, os)
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            null
        }
    }
}

/**
 * Return the bytes in file by channel.
 *
 * @param filePath The path of file.
 * @return the bytes in file
 */
fun readFile2BytesByChannel(filePath: String?): ByteArray? {
    return readFile2BytesByChannel(getFileByPath(filePath))
}

/**
 * Return the bytes in file by channel.
 *
 * @param file The file.
 * @return the bytes in file
 */
fun readFile2BytesByChannel(file: File?): ByteArray? {
    if (!isFileExists(file)) return null
    var fc: FileChannel? = null
    return try {
        fc = RandomAccessFile(file, "r").channel
        if (fc == null) {
            "fc is null.".loge()
            return ByteArray(0)
        }
        val byteBuffer = ByteBuffer.allocate(fc.size().toInt())
        while (true) {
            if (fc.read(byteBuffer) <= 0) break
        }
        byteBuffer.array()
    } catch (e: IOException) {
        e.printStackTrace()
        null
    } finally {
        fc.closeSafely()
    }
}

/**
 * Return the bytes in file by map.
 *
 * @param filePath The path of file.
 * @return the bytes in file
 */
fun readFile2BytesByMap(filePath: String?): ByteArray? {
    return readFile2BytesByMap(getFileByPath(filePath))
}

/**
 * Return the bytes in file by map.
 *
 * @param file The file.
 * @return the bytes in file
 */
fun readFile2BytesByMap(file: File?): ByteArray? {
    if (!isFileExists(file)) return null
    var fc: FileChannel? = null
    return try {
        fc = RandomAccessFile(file, "r").channel
        if (fc == null) {
            "fc is null.".loge()
            return ByteArray(0)
        }
        val size = fc.size().toInt()
        val mbb = fc.map(FileChannel.MapMode.READ_ONLY, 0, size.toLong()).load()
        val result = ByteArray(size)
        mbb[result, 0, size]
        result
    } catch (e: IOException) {
        e.printStackTrace()
        null
    } finally {
        fc.closeSafely()
    }
}