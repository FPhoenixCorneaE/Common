package com.fphoenixcorneae.common.ext

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import androidx.annotation.IntDef
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

/**
 * Return whether the notifications enabled.
 *
 * @return `true`: yes<br></br>`false`: no
 */
fun areNotificationsEnabled(): Boolean {
    return NotificationManagerCompat.from(applicationContext)
        .areNotificationsEnabled()
}

/**
 * Post a notification to be shown in the status bar.
 *
 * @param id    An identifier for this notification.
 * @param func1 The function of create the builder of notification.
 */
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
fun notify(
    id: Int,
    func1: Func1<Void, NotificationCompat.Builder>,
) {
    notify(null, id, ChannelConfig.DEFAULT_CHANNEL_CONFIG, func1)
}

/**
 * Post a notification to be shown in the status bar.
 *
 * @param tag   A string identifier for this notification.  May be `null`.
 * @param id    An identifier for this notification.
 * @param func1 The function of create the builder of notification.
 */
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
fun notify(
    tag: String?,
    id: Int,
    func1: Func1<Void, NotificationCompat.Builder>,
) {
    notify(tag, id, ChannelConfig.DEFAULT_CHANNEL_CONFIG, func1)
}

/**
 * Post a notification to be shown in the status bar.
 *
 * @param id            An identifier for this notification.
 * @param channelConfig The notification channel of config.
 * @param func1         The function of create the builder of notification.
 */
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
fun notify(
    id: Int,
    channelConfig: ChannelConfig,
    func1: Func1<Void, NotificationCompat.Builder>,
) {
    notify(null, id, channelConfig, func1)
}

/**
 * Post a notification to be shown in the status bar.
 *
 * @param tag           A string identifier for this notification.  May be `null`.
 * @param id            An identifier for this notification.
 * @param channelConfig The notification channel of config.
 * @param func1         The function of create the builder of notification.
 */
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
fun notify(
    tag: String?,
    id: Int,
    channelConfig: ChannelConfig,
    func1: Func1<Void, NotificationCompat.Builder>,
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val nm =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.createNotificationChannel(channelConfig.notificationChannel!!)
    }
    val nmc = NotificationManagerCompat.from(applicationContext)
    val builder = NotificationCompat.Builder(applicationContext)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        builder.setChannelId(channelConfig.notificationChannel!!.id)
    }
    func1.call(builder)
    nmc.notify(tag, id, builder.build())
}

/**
 * Cancel The notification.
 *
 * @param tag The tag for the notification will be cancelled.
 * @param id  The identifier for the notification will be cancelled.
 */
fun cancelNotification(tag: String?, id: Int) {
    NotificationManagerCompat.from(applicationContext).cancel(tag, id)
}

/**
 * Cancel The notification.
 *
 * @param id The identifier for the notification will be cancelled.
 */
fun cancelNotification(id: Int) {
    NotificationManagerCompat.from(applicationContext).cancel(id)
}

/**
 * Cancel all of the notifications.
 */
fun cancelAllNotifications() {
    NotificationManagerCompat.from(applicationContext).cancelAll()
}

/**
 * Set the notification bar's visibility.
 *
 * Must hold `<uses-permission android:name="android.permission.EXPAND_STATUS_BAR" />`
 *
 * @param isVisible True to set notification bar visible, false otherwise.
 */
@SuppressLint("ObsoleteSdkInt")
@RequiresPermission(Manifest.permission.EXPAND_STATUS_BAR)
fun setNotificationBarVisibility(isVisible: Boolean) {
    (if (isVisible) {
        if (Build.VERSION.SDK_INT <= 16) "expand" else "expandNotificationsPanel"
    } else {
        if (Build.VERSION.SDK_INT <= 16) "collapse" else "collapsePanels"
    }).also {
        invokePanels(it)
    }
}

private fun invokePanels(methodName: String) {
    try {
        @SuppressLint("WrongConstant")
        val service = applicationContext.getSystemService("statusbar")

        @SuppressLint("PrivateApi")
        val statusBarManager = Class.forName("android.app.StatusBarManager")
        val expand = statusBarManager.getMethod(methodName)
        expand.invoke(service)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@IntDef(
    Importance.IMPORTANCE_UNSPECIFIED,
    Importance.IMPORTANCE_NONE,
    Importance.IMPORTANCE_MIN,
    Importance.IMPORTANCE_LOW,
    Importance.IMPORTANCE_DEFAULT,
    Importance.IMPORTANCE_HIGH
)
@Retention(AnnotationRetention.SOURCE)
annotation class Importance {
    companion object {
        const val IMPORTANCE_UNSPECIFIED = -1000
        const val IMPORTANCE_NONE = 0
        const val IMPORTANCE_MIN = 1
        const val IMPORTANCE_LOW = 2
        const val IMPORTANCE_DEFAULT = 3
        const val IMPORTANCE_HIGH = 4
    }
}

class ChannelConfig(
    id: String?,
    name: CharSequence?,
    @Importance importance: Int,
) {
    var notificationChannel: NotificationChannel? = null

    /**
     * Sets whether or not notifications posted to this channel can interrupt the user in
     * [android.app.NotificationManager.INTERRUPTION_FILTER_PRIORITY] mode.
     *
     *
     * Only modifiable by the system and notification ranker.
     */
    fun setBypassDnd(bypassDnd: Boolean): ChannelConfig {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel!!.setBypassDnd(bypassDnd)
        }
        return this
    }

    /**
     * Sets the user visible description of this channel.
     *
     *
     * The recommended maximum length is 300 characters; the value may be truncated if it is too
     * long.
     */
    fun setDescription(description: String?): ChannelConfig {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel!!.description = description
        }
        return this
    }

    /**
     * Sets what group this channel belongs to.
     *
     *
     * Group information is only used for presentation, not for behavior.
     *
     *
     * Only modifiable before the channel is submitted to
     * [NotificationManager.createNotificationChannel], unless the
     * channel is not currently part of a group.
     *
     * @param groupId the id of a group created by
     * [)][NotificationManager.createNotificationChannelGroup].
     */
    fun setGroup(groupId: String?): ChannelConfig {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel!!.group = groupId
        }
        return this
    }

    /**
     * Sets the level of interruption of this notification channel.
     *
     *
     * Only modifiable before the channel is submitted to
     * [NotificationManager.createNotificationChannel].
     *
     * @param importance the amount the user should be interrupted by
     * notifications from this channel.
     */
    fun setImportance(@Importance importance: Int): ChannelConfig {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel!!.importance = importance
        }
        return this
    }

    /**
     * Sets the notification light color for notifications posted to this channel, if lights are
     * [enabled][NotificationChannel.enableLights] on this channel and the device supports that feature.
     *
     *
     * Only modifiable before the channel is submitted to
     * [NotificationManager.createNotificationChannel].
     */
    fun setLightColor(argb: Int): ChannelConfig {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel!!.lightColor = argb
        }
        return this
    }

    /**
     * Sets whether notifications posted to this channel appear on the lockscreen or not, and if so,
     * whether they appear in a redacted form. See e.g. [Notification.VISIBILITY_SECRET].
     *
     *
     * Only modifiable by the system and notification ranker.
     */
    fun setLockscreenVisibility(lockscreenVisibility: Int): ChannelConfig {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel!!.lockscreenVisibility = lockscreenVisibility
        }
        return this
    }

    /**
     * Sets the user visible name of this channel.
     *
     *
     * The recommended maximum length is 40 characters; the value may be truncated if it is too
     * long.
     */
    fun setName(name: CharSequence?): ChannelConfig {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel!!.name = name
        }
        return this
    }

    /**
     * Sets whether notifications posted to this channel can appear as application icon badges
     * in a Launcher.
     *
     *
     * Only modifiable before the channel is submitted to
     * [NotificationManager.createNotificationChannel].
     *
     * @param showBadge true if badges should be allowed to be shown.
     */
    fun setShowBadge(showBadge: Boolean): ChannelConfig {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel!!.setShowBadge(showBadge)
        }
        return this
    }

    /**
     * Sets the sound that should be played for notifications posted to this channel and its
     * audio attributes. Notification channels with an [importance][NotificationChannel.getImportance] of at
     * least [NotificationManager.IMPORTANCE_DEFAULT] should have a sound.
     *
     *
     * Only modifiable before the channel is submitted to
     * [NotificationManager.createNotificationChannel].
     */
    fun setSound(sound: Uri?, audioAttributes: AudioAttributes?): ChannelConfig {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel!!.setSound(sound, audioAttributes)
        }
        return this
    }

    /**
     * Sets the vibration pattern for notifications posted to this channel. If the provided
     * pattern is valid (non-null, non-empty), will [NotificationChannel.enableVibration] enable
     * vibration} as well. Otherwise, vibration will be disabled.
     *
     *
     * Only modifiable before the channel is submitted to
     * [NotificationManager.createNotificationChannel].
     */
    fun setVibrationPattern(vibrationPattern: LongArray?): ChannelConfig {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel!!.vibrationPattern = vibrationPattern
        }
        return this
    }

    companion object {
        val DEFAULT_CHANNEL_CONFIG = ChannelConfig(
            applicationContext.packageName,
            applicationContext.packageName,
            Importance.IMPORTANCE_DEFAULT
        )
    }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(id, name, importance)
        }
    }
}

interface Func1<Ret, Par> {
    fun call(param: Par): Ret
}