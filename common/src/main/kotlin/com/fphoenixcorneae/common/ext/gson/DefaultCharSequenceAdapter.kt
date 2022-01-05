package com.fphoenixcorneae.common.ext.gson

import com.google.gson.*
import java.lang.reflect.Type

/**
 * @desc：Gson 解析默认 CharSequence 类型适配器
 * @date：2021/09/06 17:12
 */
class DefaultCharSequenceAdapter : JsonSerializer<CharSequence>, JsonDeserializer<CharSequence> {

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): CharSequence? {
        return json?.asString
    }

    override fun serialize(src: CharSequence?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(src?.toString())
    }
}