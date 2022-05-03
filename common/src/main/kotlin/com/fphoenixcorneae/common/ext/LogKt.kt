package com.fphoenixcorneae.common.ext

import com.fphoenixcorneae.common.log.AndroidLog

fun Any?.logd() {
    AndroidLog.getPrinter().d(null, this.toString())
}

fun String.logd(tag: String? = null) {
    AndroidLog.getPrinter().d(tag, this)
}

fun String.loge(tag: String? = null) {
    AndroidLog.getPrinter().e(tag, this)
}

fun String.logi(tag: String? = null) {
    AndroidLog.getPrinter().i(tag, this)
}

fun String.logw(tag: String? = null) {
    AndroidLog.getPrinter().w(tag, this)
}

fun String.logv(tag: String? = null) {
    AndroidLog.getPrinter().v(tag, this)
}

fun String.logWtf(tag: String? = null) {
    AndroidLog.getPrinter().wtf(tag, this)
}

fun String?.logJson(tag: String? = null) {
    AndroidLog.getPrinter().json(tag, this)
}

fun String?.logXml(tag: String? = null) {
    AndroidLog.getPrinter().xml(tag, this)
}