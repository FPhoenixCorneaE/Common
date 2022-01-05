package com.fphoenixcorneae.common.util

import android.os.Build
import android.os.Environment
import com.fphoenixcorneae.common.ext.appContext
import java.io.File

/**
 * 本地路径工具类
 */
class PathUtil private constructor() {
    companion object {
        /**
         * Return the path of /system.
         *
         * @return the path of /system
         */
        val rootPath: String
            get() = getAbsolutePath(Environment.getRootDirectory())

        /**
         * Return the path of /data.
         *
         * @return the path of /data
         */
        val dataPath: String
            get() = getAbsolutePath(Environment.getDataDirectory())

        /**
         * Return the path of /cache.
         *
         * @return the path of /cache
         */
        val downloadCachePath: String
            get() = getAbsolutePath(Environment.getDownloadCacheDirectory())

        /**
         * Return the path of /data/data/package.
         *
         * @return the path of /data/data/package
         */
        val internalDataPath: String
            get() = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                appContext.applicationInfo.dataDir
            } else {
                getAbsolutePath(appContext.dataDir)
            }

        /**
         * Return the path of /data/data/package/code_cache.
         *
         * @return the path of /data/data/package/code_cache
         */
        val internalCodeCacheDir: String
            get() = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                appContext.applicationInfo.dataDir + "/code_cache"
            } else {
                getAbsolutePath(appContext.codeCacheDir)
            }

        /**
         * Return the path of /data/data/package/cache.
         *
         * @return the path of /data/data/package/cache
         */
        val internalCachePath: String
            get() = getAbsolutePath(appContext.cacheDir)

        /**
         * Return the path of /data/data/package/databases.
         *
         * @return the path of /data/data/package/databases
         */
        val internalDatabasesPath: String
            get() = appContext.applicationInfo.dataDir + "/databases"

        /**
         * Return the path of /data/data/package/databases/name.
         *
         * @param name The name of database.
         * @return the path of /data/data/package/databases/name
         */
        fun getInternalDatabasePath(name: String?): String {
            return getAbsolutePath(appContext.getDatabasePath(name))
        }

        /**
         * Return the path of /data/data/package/files.
         *
         * @return the path of /data/data/package/files
         */
        val internalFilesPath: String
            get() = getAbsolutePath(appContext.filesDir)

        /**
         * Return the path of /data/data/package/shared_prefs.
         *
         * @return the path of /data/data/package/shared_prefs
         */
        val internalSharedPrefsPath: String
            get() = appContext.applicationInfo.dataDir + "/shared_prefs"

        /**
         * Return the path of /data/data/package/no_backup.
         *
         * @return the path of /data/data/package/no_backup
         */
        val internalNoBackupFilesPath: String
            get() = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                appContext.applicationInfo.dataDir + "/no_backup"
            } else {
                getAbsolutePath(appContext.noBackupFilesDir)
            }

        /**
         * Return the path of /storage/emulated/0.
         *
         * @return the path of /storage/emulated/0
         */
        val externalStoragePath: String
            get() = if (isExternalStorageDisable) {
                ""
            } else {
                getAbsolutePath(Environment.getExternalStorageDirectory())
            }

        /**
         * Return the path of /storage/emulated/0/Music.
         *
         * @return the path of /storage/emulated/0/Music
         */
        val externalMusicPath: String
            get() = if (isExternalStorageDisable) {
                ""
            } else {
                getAbsolutePath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC))
            }

        /**
         * Return the path of /storage/emulated/0/Podcasts.
         *
         * @return the path of /storage/emulated/0/Podcasts
         */
        val externalPodcastsPath: String
            get() = if (isExternalStorageDisable) {
                ""
            } else {
                getAbsolutePath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PODCASTS))
            }

        /**
         * Return the path of /storage/emulated/0/Ringtones.
         *
         * @return the path of /storage/emulated/0/Ringtones
         */
        val externalRingtonesPath: String
            get() = if (isExternalStorageDisable) {
                ""
            } else {
                getAbsolutePath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_RINGTONES))
            }

        /**
         * Return the path of /storage/emulated/0/Alarms.
         *
         * @return the path of /storage/emulated/0/Alarms
         */
        val externalAlarmsPath: String
            get() = if (isExternalStorageDisable) {
                ""
            } else {
                getAbsolutePath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS))
            }

        /**
         * Return the path of /storage/emulated/0/Notifications.
         *
         * @return the path of /storage/emulated/0/Notifications
         */
        val externalNotificationsPath: String
            get() = if (isExternalStorageDisable) {
                ""
            } else {
                getAbsolutePath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_NOTIFICATIONS))
            }

        /**
         * Return the path of /storage/emulated/0/Pictures.
         *
         * @return the path of /storage/emulated/0/Pictures
         */
        val externalPicturesPath: String
            get() = if (isExternalStorageDisable) {
                ""
            } else {
                getAbsolutePath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES))
            }

        /**
         * Return the path of /storage/emulated/0/Movies.
         *
         * @return the path of /storage/emulated/0/Movies
         */
        val externalMoviesPath: String
            get() = if (isExternalStorageDisable) {
                ""
            } else {
                getAbsolutePath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES))
            }

        /**
         * Return the path of /storage/emulated/0/Download.
         *
         * @return the path of /storage/emulated/0/Download
         */
        val externalDownloadsPath: String
            get() = if (isExternalStorageDisable) {
                ""
            } else {
                getAbsolutePath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS))
            }

        /**
         * Return the path of /storage/emulated/0/DCIM.
         *
         * @return the path of /storage/emulated/0/DCIM
         */
        val externalDcimPath: String
            get() = if (isExternalStorageDisable) {
                ""
            } else {
                getAbsolutePath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM))
            }

        /**
         * Return the path of /storage/emulated/0/Documents.
         *
         * @return the path of /storage/emulated/0/Documents
         */
        val externalDocumentsPath: String
            get() {
                if (isExternalStorageDisable) {
                    return ""
                }
                return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                    getAbsolutePath(Environment.getExternalStorageDirectory()) + "/Documents"
                } else {
                    getAbsolutePath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS))
                }
            }

        /**
         * Return the path of /storage/emulated/0/Android/data/package.
         *
         * @return the path of /storage/emulated/0/Android/data/package
         */
        val externalDataPath: String
            get() {
                if (isExternalStorageDisable) {
                    return ""
                }
                val externalCacheDir = appContext.externalCacheDir ?: return ""
                return getAbsolutePath(externalCacheDir.parentFile)
            }

        /**
         * Return the path of /storage/emulated/0/Android/data/package/cache.
         *
         * @return the path of /storage/emulated/0/Android/data/package/cache
         */
        val externalCachePath: String
            get() = if (isExternalStorageDisable) {
                ""
            } else {
                getAbsolutePath(appContext.externalCacheDir)
            }

        /**
         * Return the path of /storage/emulated/0/Android/data/package/files.
         *
         * @return the path of /storage/emulated/0/Android/data/package/files
         */
        val externalFilesPath: String
            get() = if (isExternalStorageDisable) {
                ""
            } else {
                getAbsolutePath(appContext.getExternalFilesDir(null))
            }

        /**
         * Return the path of /storage/emulated/0/Android/data/package/files/Music.
         *
         * @return the path of /storage/emulated/0/Android/data/package/files/Music
         */
        val externalAppMusicPath: String
            get() = if (isExternalStorageDisable) {
                ""
            } else {
                getAbsolutePath(appContext.getExternalFilesDir(Environment.DIRECTORY_MUSIC))
            }

        /**
         * Return the path of /storage/emulated/0/Android/data/package/files/Podcasts.
         *
         * @return the path of /storage/emulated/0/Android/data/package/files/Podcasts
         */
        val externalAppPodcastsPath: String
            get() = if (isExternalStorageDisable) {
                ""
            } else {
                getAbsolutePath(appContext.getExternalFilesDir(Environment.DIRECTORY_PODCASTS))
            }

        /**
         * Return the path of /storage/emulated/0/Android/data/package/files/Ringtones.
         *
         * @return the path of /storage/emulated/0/Android/data/package/files/Ringtones
         */
        val externalAppRingtonesPath: String
            get() = if (isExternalStorageDisable) {
                ""
            } else {
                getAbsolutePath(appContext.getExternalFilesDir(Environment.DIRECTORY_RINGTONES))
            }

        /**
         * Return the path of /storage/emulated/0/Android/data/package/files/Alarms.
         *
         * @return the path of /storage/emulated/0/Android/data/package/files/Alarms
         */
        val externalAppAlarmsPath: String
            get() = if (isExternalStorageDisable) {
                ""
            } else {
                getAbsolutePath(appContext.getExternalFilesDir(Environment.DIRECTORY_ALARMS))
            }

        /**
         * Return the path of /storage/emulated/0/Android/data/package/files/Notifications.
         *
         * @return the path of /storage/emulated/0/Android/data/package/files/Notifications
         */
        val externalAppNotificationsPath: String
            get() = if (isExternalStorageDisable) {
                ""
            } else {
                getAbsolutePath(appContext.getExternalFilesDir(Environment.DIRECTORY_NOTIFICATIONS))
            }

        /**
         * Return the path of /storage/emulated/0/Android/data/package/files/Pictures.
         *
         * @return path of /storage/emulated/0/Android/data/package/files/Pictures
         */
        val externalAppPicturesPath: String
            get() = if (isExternalStorageDisable) {
                ""
            } else {
                getAbsolutePath(appContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES))
            }

        /**
         * Return the path of /storage/emulated/0/Android/data/package/files/Movies.
         *
         * @return the path of /storage/emulated/0/Android/data/package/files/Movies
         */
        val externalAppMoviesPath: String
            get() = if (isExternalStorageDisable) {
                ""
            } else {
                getAbsolutePath(appContext.getExternalFilesDir(Environment.DIRECTORY_MOVIES))
            }

        /**
         * Return the path of /storage/emulated/0/Android/data/package/files/Download.
         *
         * @return the path of /storage/emulated/0/Android/data/package/files/Download
         */
        val externalAppDownloadPath: String
            get() = if (isExternalStorageDisable) {
                ""
            } else {
                getAbsolutePath(appContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS))
            }

        /**
         * Return the path of /storage/emulated/0/Android/data/package/files/DCIM.
         *
         * @return the path of /storage/emulated/0/Android/data/package/files/DCIM
         */
        val externalAppDcimPath: String
            get() = if (isExternalStorageDisable) {
                ""
            } else {
                getAbsolutePath(appContext.getExternalFilesDir(Environment.DIRECTORY_DCIM))
            }

        /**
         * Return the path of /storage/emulated/0/Android/data/package/files/Documents.
         *
         * @return the path of /storage/emulated/0/Android/data/package/files/Documents
         */
        val externalAppDocumentsPath: String
            get() {
                if (isExternalStorageDisable) {
                    return ""
                }
                return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                    getAbsolutePath(
                        appContext.getExternalFilesDir(
                            null
                        )
                    ) + "/Documents"
                } else {
                    getAbsolutePath(appContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS))
                }
            }

        /**
         * Return the path of /storage/emulated/0/Android/obb/package.
         *
         * @return the path of /storage/emulated/0/Android/obb/package
         */
        val externalAppObbPath: String
            get() = if (isExternalStorageDisable) {
                ""
            } else {
                getAbsolutePath(appContext.obbDir)
            }

        private val isExternalStorageDisable: Boolean
            get() = Environment.MEDIA_MOUNTED != Environment.getExternalStorageState()

        private fun getAbsolutePath(file: File?): String {
            return if (file == null) {
                ""
            } else {
                file.absolutePath
            }
        }
    }

    init {
        throw UnsupportedOperationException("u can't instantiate me...")
    }
}