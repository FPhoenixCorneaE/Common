package com.fphoenixcorneae.common.ext.gson

import com.google.gson.*
import java.lang.reflect.Type

/**
 * @desc：Gson 解析默认 Enum 类型适配器
 * @date：2021/09/06 17:11
 */
class GsonEnumAdapter<E>(
    private val gsonEnum: GsonEnum<E>
) : JsonSerializer<E>, JsonDeserializer<E> {

    /**
     * 对象转为 Json 时调用,实现 JsonSerializer<T> 接口
     */
    override fun serialize(src: E, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement? {
        if (src is GsonEnum<*>) {
            return JsonPrimitive((src as? GsonEnum<*>)?.serialize())
        }
        return null
    }

    /**
     * json 转为对象时调用,实现 JsonDeserializer<T> 接口
     */
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): E? {
        return json?.run {
            gsonEnum.deserialize(asString)
        }
    }
}