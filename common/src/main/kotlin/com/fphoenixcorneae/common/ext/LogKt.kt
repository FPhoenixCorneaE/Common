package com.fphoenixcorneae.common.ext

import com.orhanobut.logger.Logger

fun loggerD(any: Any?) {
    Logger.d(any)
}

fun loggerD(message: String, tag: String? = null) {
    Logger.t(tag).d(message)
}

fun loggerE(message: String, tag: String? = null) {
    Logger.t(tag).e(message)
}

fun loggerI(message: String, tag: String? = null) {
    Logger.t(tag).i(message)
}

fun loggerW(message: String, tag: String? = null) {
    Logger.t(tag).w(message)
}

fun loggerJson(json: String?, tag: String? = null) {
    Logger.t(tag).json(json)
}

fun loggerXml(xml: String?, tag: String? = null) {
    Logger.t(tag).xml(xml)
}

fun Any?.logd() {
    loggerD(this)
}

fun String.logd(tag: String? = null) {
    loggerD(this, tag)
}

fun String.loge(tag: String? = null) {
    loggerE(this, tag)
}

fun String.logi(tag: String? = null) {
    loggerI(this, tag)
}

fun String.logw(tag: String? = null) {
    loggerW(this, tag)
}

fun String.logJson(tag: String? = null) {
    loggerJson(this, tag)
}

fun String.logXml(tag: String? = null) {
    loggerXml(this, tag)
}