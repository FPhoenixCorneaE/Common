package com.fphoenixcorneae.common.ext

import com.orhanobut.logger.Logger

fun Any?.logd() {
    Logger.d(this)
}

fun String.logd(tag: String? = null) {
    Logger.t(tag).d(this)
}

fun String.loge(tag: String? = null) {
    Logger.t(tag).e(this)
}

fun String.logi(tag: String? = null) {
    Logger.t(tag).i(this)
}

fun String.logw(tag: String? = null) {
    Logger.t(tag).w(this)
}

fun String.logv(tag: String? = null) {
    Logger.t(tag).v(this)
}

fun String.logWtf(tag: String? = null) {
    Logger.t(tag).wtf(this)
}

fun String?.logJson(tag: String? = null) {
    Logger.t(tag).json(this)
}

fun String?.logXml(tag: String? = null) {
    Logger.t(tag).xml(this)
}