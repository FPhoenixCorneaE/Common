package com.fphoenixcorneae.common.ext.gson

/**
 * @desc：Enum 类型的数据序列化与反序列化, 须实现 [GsonEnum] interface
 *        eg:
 *        @Keep
 *        enum class DetectStatus : GsonEnum<DetectStatus> {
 *            Default,
 *            Detecting,
 *            DetectSuccess,
 *            DetectFailure;

 *            override fun serialize(): String? {
 *                return name
 *            }

 *            override fun deserialize(jsonEnum: String): DetectStatus {
 *                return when (jsonEnum) {
 *                    Detecting.name -> Detecting
 *                    DetectSuccess.name -> DetectSuccess
 *                    DetectFailure.name -> DetectFailure
 *                    else -> Default
 *                }
 *            }
 *        }
 * @date：2021/09/06 17:12
 */
interface GsonEnum<E> {
    fun serialize(): String?
    fun deserialize(jsonEnum: String): E
}