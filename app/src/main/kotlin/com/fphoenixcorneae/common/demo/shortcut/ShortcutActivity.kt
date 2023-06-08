package com.fphoenixcorneae.common.demo.shortcut

import android.graphics.BitmapFactory
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.fphoenixcorneae.common.demo.R
import com.fphoenixcorneae.common.ext.createShortcut
import com.fphoenixcorneae.common.ext.deleteShortcut
import com.fphoenixcorneae.common.ext.hasShortcut
import com.fphoenixcorneae.common.ext.toastQQStyle

class ShortcutActivity : AppCompatActivity(R.layout.activity_shortcut) {

    fun hasShortcut(view: View) {
        toastQQStyle("存在目标快捷方式与否：${hasShortcut(this, "测试快捷方式")}")
    }

    fun createShortcut(view: View) {
        createShortcut(
            activity = this,
            shortcutName = "测试快捷方式",
            id = "test shortcut",
            shortcutBitmap = BitmapFactory.decodeResource(resources, R.mipmap.ic_shortcut)
        )
    }

    fun deleteShortcut(view: View) {
        deleteShortcut(this, "测试快捷方式")
    }
}