package com.fphoenixcorneae.common.demo.cache

import android.graphics.BitmapFactory
import androidx.appcompat.content.res.AppCompatResources
import com.fphoenixcorneae.common.demo.R
import com.fphoenixcorneae.common.ext.applicationContext
import org.json.JSONArray
import org.json.JSONObject

/**
 * @desc：
 * @date：2022/06/09 15:20
 */
object CacheData {

    val DATA = listOf(
        "1122".toByteArray(),
        "我是一个String！",
        JSONObject("{\"l1_2\":{\"l1_2_1\":121}}"),
        JSONArray("[\"l1_1_1\",\"l1_1_2\"]"),
        BitmapFactory.decodeResource(applicationContext.resources, R.mipmap.ic_launcher),
        AppCompatResources.getDrawable(applicationContext, R.drawable.shape_color_drawable),
        UserInfo("9527", "唐伯虎", "12306"),
        arrayListOf(111, 222)
    )
}