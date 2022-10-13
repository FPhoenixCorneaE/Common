package com.fphoenixcorneae.common

import com.fphoenixcorneae.common.ext.arabicToChinese
import com.fphoenixcorneae.common.ext.chineseToArabic
import org.junit.Test

class ChineseArabicUnitTest {

    @Test
    fun chineseArabicConvert() {
        println("一千零一夜一千零一夜".chineseToArabic())
        println("五百七十九".chineseToArabic())
        println("一万零五百七十九".chineseToArabic())
        println("一万零七十九".chineseToArabic())
        println("一万零五百零九".chineseToArabic())
        println("十二传说".chineseToArabic())
        println("十二传说二".chineseToArabic())
        println("一百五十级".chineseToArabic())
        println("十二传说第一百五十级".chineseToArabic())
        println("十月围城".chineseToArabic())
        println("百变星君".chineseToArabic())
        println("零零七".chineseToArabic())
        println("九九八十一".chineseToArabic())
        println("一九三七".chineseToArabic())
        println(1_010.arabicToChinese())
        println(11_010.arabicToChinese())
        println(80_000.arabicToChinese())
        println(100_000.arabicToChinese())
        println(100_520.arabicToChinese())
        println(110_520.arabicToChinese())
        println(237_896.arabicToChinese())
        println(1_100_000.arabicToChinese())
        println(1_001_020.arabicToChinese())
        println(1_231_096.arabicToChinese())
        println(8_031_096.arabicToChinese())
        println(10_000_000.arabicToChinese())
        println(10_101_000.arabicToChinese())
        println(11_000_000.arabicToChinese())
        println(21_101_080.arabicToChinese())
    }
}