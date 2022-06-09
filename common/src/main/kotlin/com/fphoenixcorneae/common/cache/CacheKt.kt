package com.fphoenixcorneae.common.cache

/**
 * 磁盘缓存
 */
val diskCacheManager by lazy { DiskCacheManager.getInstance() }

/**
 * 内存缓存
 */
val memoryCacheManager by lazy { MemoryCacheManager.getInstance() }

/**
 * 双重缓存
 */
val doubleCacheManager by lazy { DoubleCacheManager.getInstance() }