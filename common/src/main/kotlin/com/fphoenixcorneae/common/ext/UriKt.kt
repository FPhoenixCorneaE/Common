package com.fphoenixcorneae.common.ext

import android.annotation.TargetApi
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.loader.content.CursorLoader
import java.io.*

/**
 * The path of resource to uri.
 *
 * [res type]/[res name].toUri() -> [drawable/icon].toUri(), [raw/icon].toUri()
 * [resource id].toUri() -> [R.drawable.icon].toUri()
 */
fun String?.toUri(): Uri? =
    this?.let {
        Uri.parse("android.resource://$appPackageName/$it")
    }

/**
 * File to uri.
 */
fun File?.toUri(): Uri? =
    this?.let {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val authority = "${appPackageName}.FileProvider"
            FileProvider.getUriForFile(applicationContext, authority, this)
        } else {
            Uri.fromFile(this)
        }
    }


/**
 * 复杂版处理  (适配多种API)
 */
fun Uri?.getRealPath(): String? =
    this?.let {
        when {
            sdkVersionCode < Build.VERSION_CODES.HONEYCOMB -> {
                it.getRealPathFromUriBelowApi11()
            }
            sdkVersionCode < Build.VERSION_CODES.KITKAT -> {
                it.getRealPathFromUriApi11To18()
            }
            sdkVersionCode < Build.VERSION_CODES.N -> {
                it.getRealPathFromUriApi19To23()
            }
            else -> {
                it.getRealPathFromUriAboveApi24()
            }
        }
    }

/**
 * 适配api24以上,根据uri获取图片的绝对路径
 */
@TargetApi(Build.VERSION_CODES.N)
private fun Uri?.getRealPathFromUriAboveApi24(): String? =
    this?.let {
        val rootDataDir = applicationContext.filesDir.path
        val fileName = it.getFileName()
        if (fileName.isNullOrEmpty().not()) {
            val copyFile = File(rootDataDir + File.separator + fileName)
            copyFile(applicationContext, it, copyFile)
            copyFile.absolutePath
        } else {
            null
        }
    }

/**
 * 适配api19-api24,根据uri获取图片的绝对路径
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
private fun Uri?.getRealPathFromUriApi19To23(): String? =
    this?.let {
        if (DocumentsContract.isDocumentUri(applicationContext, it)) {
            if (it.isExternalStorageDocument()) {
                val docId = DocumentsContract.getDocumentId(it)
                val split = docId.split(":").toTypedArray()
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                } else {
                    // Below logic is how External Storage provider build URI for documents
                    // http://stackoverflow.com/questions/28605278/android-5-sd-card-label
                    val mStorageManager = applicationContext.storageManager
                    try {
                        val storageVolumeClazz = Class.forName("android.os.storage.StorageVolume")
                        val getVolumeList = mStorageManager?.javaClass?.getMethod("getVolumeList")
                        val getUuid = storageVolumeClazz.getMethod("getUuid")
                        val getState = storageVolumeClazz.getMethod("getState")
                        val getPath = storageVolumeClazz.getMethod("getPath")
                        val isPrimary = storageVolumeClazz.getMethod("isPrimary")
                        val isEmulated = storageVolumeClazz.getMethod("isEmulated")
                        val result = getVolumeList?.invoke(mStorageManager)
                        if (result != null) {
                            var realPath: String? = null
                            val length = java.lang.reflect.Array.getLength(result)
                            for (i in 0 until length) {
                                val storageVolumeElement = java.lang.reflect.Array.get(result, i)
                                //String uuid = (String) getUuid.invoke(storageVolumeElement);
                                val mounted = Environment.MEDIA_MOUNTED == getState.invoke(storageVolumeElement)
                                        || Environment.MEDIA_MOUNTED_READ_ONLY == getState.invoke(storageVolumeElement)

                                //if the media is not mounted, we need not get the volume details
                                if (!mounted) continue

                                //Primary storage is already handled.
                                if (isPrimary.invoke(storageVolumeElement) as Boolean
                                    && isEmulated.invoke(storageVolumeElement) as Boolean
                                ) {
                                    continue
                                }
                                val uuid = getUuid.invoke(storageVolumeElement) as String?
                                realPath = if (uuid != null && uuid == type) {
                                    File(getPath.invoke(storageVolumeElement).toString() + "/" + split[1]).path
                                } else {
                                    null
                                }
                            }
                            realPath
                        } else {
                            null
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        null
                    }
                }
            } else if (it.isDownloadsDocument()) {
                val id = DocumentsContract.getDocumentId(it)
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"),
                    java.lang.Long.valueOf(id)
                )
                getDataColumn(applicationContext, contentUri, null, null)
            } else if (it.isMediaDocument()) {
                val docId = DocumentsContract.getDocumentId(it)
                val split = docId.split(":").toTypedArray()
                val contentUri: Uri = when (split[0]) {
                    "image" -> {
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    }
                    "video" -> {
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    }
                    "audio" -> {
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    }
                    else -> {
                        MediaStore.Files.getContentUri("external")
                    }
                }
                val selection = "_id=?"
                val selectionArgs = arrayOf(split[1])
                getDataColumn(applicationContext, contentUri, selection, selectionArgs)
            } else {
                null
            }
        } else if (ContentResolver.SCHEME_CONTENT.equals(it.scheme, ignoreCase = true)) {
            getDataColumn(applicationContext, it, null, null)
        } else if (ContentResolver.SCHEME_FILE.equals(it.scheme, ignoreCase = true)) {
            it.path
        } else {
            null
        }
    }

/**
 * 适配api11-api18,根据uri获取图片的绝对路径
 */
private fun Uri?.getRealPathFromUriApi11To18(): String? =
    this?.let {
        var filePath: String? = null
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val loader = CursorLoader(applicationContext, it, projection, null, null, null)
        val cursor = loader.loadInBackground()
        if (cursor != null) {
            cursor.moveToFirst()
            val columnIndex = cursor.getColumnIndex(projection[0])
            if (columnIndex > 0) {
                filePath = cursor.getString(columnIndex)
            }
            closeIOQuietly(cursor)
        }
        filePath
    }

/**
 * 适配api11以下(不包括api11),根据uri获取图片的绝对路径
 */
private fun Uri?.getRealPathFromUriBelowApi11(): String? =
    this?.let {
        var filePath: String? = null
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = applicationContext.contentResolver.query(it, projection, null, null, null)
        if (cursor != null) {
            cursor.moveToFirst()
            val columnIndex = cursor.getColumnIndex(projection[0])
            if (columnIndex > 0) {
                filePath = cursor.getString(columnIndex)
            }
            closeIOQuietly(cursor)
        }
        filePath
    }

/**
 * 获取文件名
 */
fun Uri?.getFileName(): String? {
    if (this == null) {
        return null
    }
    var fileName: String? = null
    val path = this.path
    if (path != null) {
        val cut = path.lastIndexOf('/')
        if (cut != -1) {
            fileName = path.substring(cut + 1)
        }
    }
    return fileName
}

/**
 * 复制文件
 */
private fun copyFile(
    context: Context,
    srcUri: Uri,
    dstFile: File
) {
    var inputStream: InputStream? = null
    var outputStream: OutputStream? = null
    try {
        inputStream = context.contentResolver.openInputStream(srcUri)
        if (inputStream == null) {
            return
        }
        outputStream = FileOutputStream(dstFile)
        copyStream(inputStream, outputStream)
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
    } finally {
        closeIOQuietly(inputStream, outputStream)
    }
}

/**
 * 复制文件流
 */
private fun copyStream(input: InputStream, output: OutputStream): Int {
    val bufferSize = 1024 * 2
    val buffer = ByteArray(bufferSize)
    val `in` = BufferedInputStream(input, bufferSize)
    val out = BufferedOutputStream(output, bufferSize)
    var count = 0
    var n: Int
    try {
        while (`in`.read(buffer, 0, bufferSize).also { n = it } != -1) {
            out.write(buffer, 0, n)
            count += n
        }
        out.flush()
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        closeIOQuietly(out, `in`)
    }
    return count
}

/**
 * Get the value of the data column for this Uri. This is useful for
 * MediaStore Uris, and other file-based ContentProviders.
 *
 * @param context       The context.
 * @param uri           The Uri to query.
 * @param selection     (Optional) Filter used in the query.
 * @param selectionArgs (Optional) Selection arguments used in the query.
 * @return The value of the _data column, which is typically a file path.
 */
private fun getDataColumn(
    context: Context,
    uri: Uri,
    selection: String?,
    selectionArgs: Array<String>?
): String? {
    if ("com.google.android.apps.photos.content" == uri.authority) {
        val lastPathSegment = uri.lastPathSegment
        if (!lastPathSegment.isNullOrEmpty()) {
            return File(lastPathSegment).path
        }
    } else if ("com.tencent.mtt.fileprovider" == uri.authority) {
        val path = uri.path
        if (path.isNullOrEmpty().not()) {
            val fileDir = Environment.getExternalStorageDirectory()
            return File(fileDir, path!!.substring("/QQBrowser".length, path.length)).path
        }
    } else if ("com.huawei.hidisk.fileprovider" == uri.authority) {
        val path = uri.path
        if (path.isNullOrEmpty().not()) {
            return File(path!!.replace("/root", "")).path
        }
    }
    var cursor: Cursor? = null
    val column = MediaStore.MediaColumns.DATA
    val projection = arrayOf(column)
    try {
        cursor = context.contentResolver.query(uri, projection, selection, selectionArgs, null)
        if (cursor != null && cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndexOrThrow(column)
            return cursor.getString(columnIndex)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        closeIOQuietly(cursor)
    }
    return null
}

/**
 * @return Whether the Uri authority is ExternalStorageProvider.
 */
fun Uri.isExternalStorageDocument(): Boolean {
    return "com.android.externalstorage.documents" == authority
}

/**
 * @return Whether the Uri authority is DownloadsProvider.
 */
fun Uri.isDownloadsDocument(): Boolean {
    return "com.android.providers.downloads.documents" == authority
}

/**
 * @return Whether the Uri authority is MediaProvider.
 */
fun Uri.isMediaDocument(): Boolean {
    return "com.android.providers.media.documents" == authority
}