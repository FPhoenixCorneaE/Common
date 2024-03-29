package com.fphoenixcorneae.common.ext

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment

/**
 * 加入 QQ 聊天群
 */
fun Fragment.joinQQGroup(key: String, addNewTaskFlag: Boolean = false): Boolean {
    return activity?.joinQQGroup(key, addNewTaskFlag) == true
}

/**
 * 加入 QQ 聊天群
 */
fun Activity.joinQQGroup(key: String, isNewTask: Boolean = false): Boolean {
    val intent = Intent()
    intent.data =
        Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D$key")
    // 此 Flag 可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面
    // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    if (isNewTask) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    return try {
        startActivity(intent)
        true
    } catch (e: Exception) {
        // 未安装手Q或安装的版本不支持
        toast("未安装手机QQ或安装的版本不支持")
        false
    }
}