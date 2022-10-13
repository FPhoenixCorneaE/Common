package com.fphoenixcorneae.common.ext

private val cnArr = charArrayOf('零', '一', '二', '三', '四', '五', '六', '七', '八', '九')
private val chArr = charArrayOf('零', '十', '百', '千', '万', '亿')
private const val ALL_CHINESE_NUMBERS = "零一二三四五六七八九十百千万亿"
private const val ALL_ARABIC_NUMBERS = "0123456789"
private const val CHINESE_NUMBER_1 = "一二三四五六七八九"
private const val CHINESE_NUMBER_2 = "十百千万亿"
private const val ZERO = "零"

/**
 * 将字符串中的中文数字转换阿拉伯数字，其它非数字汉字不替换
 */
fun String.chineseToArabic(): String {
    val resultStr = StringBuilder()
    var tempresult = 0
    var temp = 1 //存放一个单位的数字如：十万
    var count = 0 //判断是否有单位
    // 重新将 temp, count, tempresult 设置为初始值
    var setInitial = false
    // 以十百千万亿结束的在最后加
    var isAdd = false
    var num1flag = false
    var num2flag = false
    for (i in this.indices) {
        if (setInitial) {
            tempresult = 0
            temp = 1
            count = 0
            setInitial = false
        }
        var b = true //判断是否是chArr
        val c = this[i]
        if (ALL_CHINESE_NUMBERS.indexOf(c) >= 0) {
            if (i < this.length - 1 && CHINESE_NUMBER_1.indexOf(c) >= 0 && CHINESE_NUMBER_1.indexOf(this[i + 1]) >= 0) {
                num1flag = true
            }
            for (j in cnArr.indices) {
                if (c == cnArr[j]) {
                    if (0 != count) { //添加下一个单位之前，先把上一个单位值添加到结果中
                        tempresult += temp
                        temp = 1
                        count = 0
                    }
                    if (!isAdd && (i == this.length - 1
                                || ALL_CHINESE_NUMBERS.indexOf(this[i + 1]) < 0)
                    ) {
                        tempresult += j
                        setInitial = true
                        resultStr.append(tempresult)
                        isAdd = true
                    }
                    // 下标+1，就是对应的值
                    temp = j
                    b = false
                    break
                }
            }
            if (num1flag) {
                resultStr.append(temp)
                num1flag = false
                setInitial = true
                continue
            }
            val test = (i < this.length - 1 && ZERO.indexOf(this[i + 1]) >= 0
                    || i > 0 && ZERO.indexOf(this[i - 1]) >= 0)
            if (i < this.length - 1 && ZERO.indexOf(c) >= 0 && test) {
                num2flag = true
            }
            if (b) { //单位{'十','百','千','万','亿'}
                for (j in chArr.indices) {
                    if (c == chArr[j]) {
                        when (j) {
                            0 -> temp *= 1
                            1 -> temp *= 10
                            2 -> temp *= 100
                            3 -> temp *= 1_000
                            4 -> temp *= 10_000
                            5 -> temp *= 100_000_000
                            else -> {}
                        }
                        count++
                    }
                }
            }
            if (num2flag) {
                resultStr.append(temp)
                num2flag = false
                setInitial = true
                continue
            }
            if (!isAdd && (i == this.length - 1 || ALL_CHINESE_NUMBERS.indexOf(this[i + 1]) < 0)) {
                tempresult += temp
                setInitial = true
                resultStr.append(tempresult)
                isAdd = true
            }
        } else {
            isAdd = false
            resultStr.append(c)
        }
    }
    return resultStr.toString()
}

/**
 * 将数字转换为中文数字， 这里只写到了千万
 */
fun Int.arabicToChinese(): String? {
    val si = this.toString()
    var sd: String? = ""
    when (si.length) {
        1 -> {
            if (this == 0) {
                return sd
            }
            sd += cnArr[this]
            return sd
        }
        2 -> {
            if (si.startsWith("1")) {
                sd += "十"
                if (this % 10 == 0) {
                    return sd
                }
            } else {
                sd += cnArr[this / 10].toString() + "十"
            }
            sd += (this % 10).arabicToChinese()
        }
        3 -> {
            sd += cnArr[this / 100].toString() + "百"
            if ((this % 100).toString().length < 2) {
                if (this % 100 == 0) {
                    return sd
                }
                sd += "零"
            }
            sd += (this % 100).arabicToChinese()
        }
        4 -> {
            sd += cnArr[this / 1_000].toString() + "千"
            if ((this % 1_000).toString().length < 3) {
                if (this % 1_000 == 0) {
                    return sd
                }
                sd += "零"
                if ((this % 1_000).toString().startsWith("1")) {
                    sd += "一"
                }
            }
            sd += (this % 1_000).arabicToChinese()
        }
        5 -> {
            sd += cnArr[this / 10_000].toString() + "万"
            if ((this % 10_000).toString().length < 4) {
                if (this % 10_000 == 0) {
                    return sd
                }
                sd += "零"
                if ((this % 10_000).toString().startsWith("1")) {
                    sd += "一"
                }
            }
            sd += (this % 10_000).arabicToChinese()
        }
        6 -> {
            sd += if (si.substring(0, 1) == "1") {
                "十"
            } else {
                (this / 100_000).arabicToChinese() + "十"
            }
            if ((this % 100_000).toString().length < 5) {
                sd += "万"
                if (this % 100_000 == 0) {
                    return sd
                }
                if ((this % 100_000).toString().length < 4) {
                    sd += "零"
                }
            }
            sd += (this % 100_000).arabicToChinese()
        }
        7 -> {
            sd += cnArr[this / 1_000_000].toString() + "百"
            if ((this % 1_000_000).toString().length < 6) {
                if ((this % 1_000_000).toString().length < 5) {
                    sd += "万"
                }
                if (this % 1_000_000 == 0) {
                    return sd
                }
                if ((this % 1_000_000).toString().length == 5 || (this % 1_000_000).toString().length < 4) {
                    sd += "零"
                }
            } else if ((this % 1_000_000).toString().startsWith("1")) {
                sd += "一"
            }
            sd += (this % 1_000_000).arabicToChinese()
        }
        8 -> {
            sd += cnArr[this / 10_000_000].toString() + "千"
            if ((this % 10_000_000).toString().length < 7) {
                if ((this % 10_000_000).toString().length < 5) {
                    sd += "万"
                }
                if (this % 10_000_000 == 0) {
                    return sd
                }
                sd += "零"
                if ((this % 10_000_000).toString().startsWith("1")) {
                    sd += "一"
                }
            }
            sd += (this % 10_000_000).arabicToChinese()
        }
    }
    return sd
}

/**
 * 判断传入的字符串是否全是汉字数字
 */
fun CharSequence.isChineseOnly(): Boolean = run {
    var isChineseOnly = true
    val ch = toString().toCharArray()
    run breaking@{
        for (c in ch) {
            if (!ALL_CHINESE_NUMBERS.contains(c.toString())) {
                isChineseOnly = false
                return@breaking
            }
        }
    }
    isChineseOnly
}

/**
 * 判断数字字符串是否是整数字符串
 */
fun CharSequence.isDigitOnly(): Boolean = run {
    matches(Regex("[0-9]+"))
}

