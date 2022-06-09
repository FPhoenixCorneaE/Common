package com.fphoenixcorneae.common.demo.cache

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.lifecycle.ViewModel
import com.fphoenixcorneae.common.cache.diskCacheManager
import com.fphoenixcorneae.common.cache.doubleCacheManager
import com.fphoenixcorneae.common.cache.memoryCacheManager
import com.fphoenixcorneae.common.ext.gson.toJson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject
import java.io.Serializable

/**
 * @desc：
 * @date：2022/06/09 15:20
 */
class CacheViewModel : ViewModel() {

    private val _cacheDataTypePosition = MutableStateFlow(0)
    val cacheDataTypePosition = _cacheDataTypePosition.asStateFlow()

    private val _cacheData = MutableStateFlow<String?>(null)
    val cacheData = _cacheData.asStateFlow()

    private val _imageCache = MutableStateFlow<Any?>(null)
    val imageCache = _imageCache.asStateFlow()

    private val _textViewVisible = MutableStateFlow(true)
    val textViewVisible = _textViewVisible.asStateFlow()

    private val _imageViewVisible = MutableStateFlow(false)
    val imageViewVisible = _imageViewVisible.asStateFlow()

    fun setCacheTypePosition(position: Int) {
        _cacheDataTypePosition.value = position
    }

    fun putCache(cacheType: Int) {
        val position = cacheDataTypePosition.value
        when (cacheType) {
            0 -> {
                memoryCacheManager.put("cache$position", CacheData.DATA[position])
            }
            1 -> {
                diskCacheManager.put("cache$position", CacheData.DATA[position])
            }
            2 -> {
                doubleCacheManager.put("cache$position", CacheData.DATA[position])
            }
            else -> {}
        }
    }

    fun getCache(cacheType: Int) {
        val position = cacheDataTypePosition.value
        when (position) {
            0, 1, 2, 3, 7 -> {
                _textViewVisible.value = true
                _imageViewVisible.value = false
                _cacheData.value = when (cacheType) {
                    0 -> {
                        when (position) {
                            0 -> {
                                memoryCacheManager.get<ByteArray>("cache$position")?.let {
                                    String(it)
                                }
                            }
                            1 -> {
                                memoryCacheManager.get<String>("cache$position")
                            }
                            2 -> {
                                memoryCacheManager.get<JSONObject>("cache$position").toString()
                            }
                            3 -> {
                                memoryCacheManager.get<JSONArray>("cache$position").toString()
                            }
                            else -> {
                                memoryCacheManager.get<Serializable>("cache$position").toString()
                            }
                        }
                    }
                    1 -> {
                        when (position) {
                            0 -> {
                                diskCacheManager.get<ByteArray>("cache$position")?.let {
                                    String(it)
                                }
                            }
                            1 -> {
                                diskCacheManager.get<String>("cache$position")
                            }
                            2 -> {
                                diskCacheManager.get<JSONObject>("cache$position").toString()
                            }
                            3 -> {
                                diskCacheManager.get<JSONArray>("cache$position").toString()
                            }
                            else -> {
                                diskCacheManager.get<Serializable>("cache$position").toString()
                            }
                        }
                    }
                    2 -> {
                        when (position) {
                            0 -> {
                                doubleCacheManager.get<ByteArray>("cache$position")?.let {
                                    String(it)
                                }
                            }
                            1 -> {
                                doubleCacheManager.get<String>("cache$position")
                            }
                            2 -> {
                                doubleCacheManager.get<JSONObject>("cache$position").toString()
                            }
                            3 -> {
                                doubleCacheManager.get<JSONArray>("cache$position").toString()
                            }
                            else -> {
                                doubleCacheManager.get<Serializable>("cache$position").toString()
                            }
                        }
                    }
                    else -> null
                }
            }
            6 -> {
                _textViewVisible.value = true
                _imageViewVisible.value = false
                _cacheData.value = when (cacheType) {
                    0 -> {
                        memoryCacheManager["cache$position"]
                    }
                    1 -> {
//                        diskCacheManager.getParcelable("cache$position", creator= TestUser.CREATOR)
                        diskCacheManager.getParcelable("cache$position", parceler = UserInfo.Companion)
                    }
                    2 -> {
//                        doubleCacheManager.getParcelable("cache$position", creator = TestUser.CREATOR)
                        doubleCacheManager.getParcelable("cache$position", parceler = UserInfo.Companion)
                    }
                    else -> null
                }.toJson()
            }
            else -> {
                _textViewVisible.value = false
                _imageViewVisible.value = true
                _imageCache.value = when (cacheType) {
                    0 -> {
                        when (position) {
                            4 -> memoryCacheManager.get<Bitmap>("cache$position")
                            5 -> memoryCacheManager.get<Drawable>("cache$position")
                            else -> {}
                        }
                    }
                    1 -> {
                        when (position) {
                            4 -> diskCacheManager.get<Bitmap>("cache$position")
                            5 -> diskCacheManager.get<Drawable>("cache$position")
                            else -> {}
                        }
                    }
                    2 -> {
                        when (position) {
                            4 -> doubleCacheManager.get<Bitmap>("cache$position")
                            5 -> doubleCacheManager.get<Drawable>("cache$position")
                            else -> {}
                        }
                    }
                    else -> null
                }
            }
        }
    }

    fun removeCache(cacheType: Int) {
        val position = cacheDataTypePosition.value
        when (cacheType) {
            0 -> memoryCacheManager.remove("cache$position")
            1 -> diskCacheManager.remove("cache$position")
            2 -> doubleCacheManager.remove("cache$position")
            else -> {}
        }
        getCache(cacheType)
    }
}