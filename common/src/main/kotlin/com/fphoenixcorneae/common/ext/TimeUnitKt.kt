package com.fphoenixcorneae.common.ext

import java.util.concurrent.TimeUnit

/**
 * MILLISECONDS的毫秒值
 */
val Int.MILLISECONDS: Long
    get() =
        this.times(other = TimeUnit.MILLISECONDS.toMillis(1))

/**
 * SECONDS的毫秒值
 */
val Int.SECONDS: Long
    get() =
        this.times(other = TimeUnit.SECONDS.toMillis(1))

/**
 * MINUTES的毫秒值
 */
val Int.MINUTES: Long
    get() =
        this.times(other = TimeUnit.MINUTES.toMillis(1))

/**
 * HOURS的毫秒值
 */
val Int.HOURS: Long
    get() =
        this.times(other = TimeUnit.HOURS.toMillis(1))

/**
 * DAYS的毫秒值
 */
val Int.DAYS: Long
    get() =
        this.times(other = TimeUnit.DAYS.toMillis(1))

/**
 * MONTHS的毫秒值
 */
val Int.MONTHS: Long
    get() =
        this.times(other = TimeUnit.DAYS.toMillis(30))

/**
 * YEARS的毫秒值
 */
val Int.YEARS: Long
    get() =
        this.times(other = TimeUnit.DAYS.toMillis(365))