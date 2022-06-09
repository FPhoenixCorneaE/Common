package com.fphoenixcorneae.common.demo.cache

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize

/**
 * @desc：
 * @date：2022/06/08 16:23
 */
@Keep
@Parcelize
data class UserInfo(
    val id: String?,
    val username: String?,
    val phone: String?
) : Parcelable {
    companion object : Parceler<UserInfo> {
        override fun create(parcel: Parcel): UserInfo {
            return UserInfo(id = parcel.readString(), username = parcel.readString(), phone = parcel.readString())
        }

        override fun UserInfo.write(parcel: Parcel, flags: Int) {
            parcel.writeString(id)
            parcel.writeString(username)
            parcel.writeString(phone)
        }
    }
}
