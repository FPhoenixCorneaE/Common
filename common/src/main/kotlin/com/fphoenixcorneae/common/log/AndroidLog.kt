package com.fphoenixcorneae.common.log

/**
 * @desc：AndroidLog
 * @date：2022/5/2 22:26
 */
object AndroidLog {

    private var printer: Printer = DefaultPrinter()

    fun setPrinter(printer: Printer) {
        this.printer = printer
    }

    fun getPrinter() = printer
}