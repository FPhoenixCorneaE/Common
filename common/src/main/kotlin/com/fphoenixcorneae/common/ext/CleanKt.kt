package com.fphoenixcorneae.common.ext

import android.os.Build
import android.os.Environment
import androidx.annotation.RequiresApi
import java.io.File

/**
 * Clean the internal cache.
 *
 * directory: /data/data/package/cache
 *
 * @return `true`: success<br></br>`false`: fail
 */
fun cleanInternalCache(): Boolean =
    deleteAllInDir(appContext.cacheDir)

/**
 * Clean the internal files.
 *
 * directory: /data/data/package/files
 *
 * @return `true`: success<br></br>`false`: fail
 */
fun cleanInternalFiles(): Boolean =
    deleteAllInDir(appContext.filesDir)

/**
 * Clean the internal databases.
 *
 * directory: /data/data/package/databases
 *
 * @return `true`: success<br></br>`false`: fail
 */
fun cleanInternalDbs(): Boolean =
    deleteAllInDir(File(appContext.filesDir.parent, "databases"))

/**
 * Clean the internal database by name.
 *
 * directory: /data/data/package/databases/dbName
 *
 * @param dbName The name of database.
 * @return `true`: success<br></br>`false`: fail
 */
fun cleanInternalDbByName(dbName: String?): Boolean =
    appContext.deleteDatabase(dbName)

/**
 * Clean the internal shared preferences.
 *
 * directory: /data/data/package/shared_prefs
 *
 * @return `true`: success<br></br>`false`: fail
 */
fun cleanInternalSp(): Boolean =
    deleteAllInDir(File(appContext.filesDir.parent, "shared_prefs"))

/**
 * Clean the external cache.
 *
 * directory: /storage/emulated/0/android/data/package/cache
 *
 * @return `true`: success<br></br>`false`: fail
 */
fun cleanExternalCache(): Boolean =
    Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()
            && deleteAllInDir(appContext.externalCacheDir)

/**
 * Clean the custom directory.
 *
 * @param dirPath The path of directory.
 * @return `true`: success<br></br>`false`: fail
 */
fun cleanCustomDir(dirPath: String?): Boolean =
    deleteAllInDir(getFileByPath(dirPath))

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
fun cleanAppUserData() =
    appContext.activityManager?.clearApplicationUserData()