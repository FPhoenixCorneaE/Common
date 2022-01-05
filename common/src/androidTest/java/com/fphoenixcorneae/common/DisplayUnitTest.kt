package com.fphoenixcorneae.common

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.fphoenixcorneae.common.ext.dp
import com.fphoenixcorneae.common.ext.sp
import com.fphoenixcorneae.common.ext.toDp
import com.fphoenixcorneae.common.ext.toSp
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class DisplayUnitTest {

    @Test
    fun dp2px_px2dp() {
        print("//==============================dp2px_px2dp=======================================//")
        println()
        print("Int.dp2px: ${10.dp}")
        println()
        print("Float.dp2px: ${10f.dp}")
        println()
        print("Double.dp2px: ${10.0.dp}")
        println()
        print("Int.px2dp: ${26.toDp()}")
        println()
        print("Float.px2dp: ${26f.toDp()}")
        println()
        print("Double.px2dp: ${26.0.toDp()}")
        println()
        print("//==============================dp2px_px2dp=======================================//")
        println()
    }

    @Test
    fun sp2px_px2sp() {
        print("//==============================sp2px_px2sp=======================================//")
        println()
        print("Int.sp2px: ${10.sp}")
        println()
        print("Float.sp2px: ${10f.sp}")
        println()
        print("Double.sp2px: ${10.0.sp}")
        println()
        print("Int.px2sp: ${26.toSp()}")
        println()
        print("Float.px2sp: ${26f.toSp()}")
        println()
        print("Double.px2sp: ${26.0.toSp()}")
        println()
        print("//==============================sp2px_px2sp=======================================//")
        println()
    }
}