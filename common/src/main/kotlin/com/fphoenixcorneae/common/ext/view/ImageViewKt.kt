package com.fphoenixcorneae.common.ext.view

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.Px
import androidx.core.graphics.drawable.DrawableCompat
import coil.Coil
import coil.request.ImageRequest
import com.fphoenixcorneae.common.ext.action
import com.fphoenixcorneae.common.ext.appContext
import com.fphoenixcorneae.common.ext.toDrawable
import com.fphoenixcorneae.common.util.ImageUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.FileDescriptor

/**
 * 获取着色位图
 */
fun getTintDrawable(
    @ColorInt tintColor: Int,
    drawable: Drawable?
): Drawable? = drawable?.run {
    DrawableCompat.wrap(constantState?.newDrawable() ?: this)
        .mutate()
}?.apply {
    DrawableCompat.setTint(this, tintColor)
}

/**
 * 图片着色
 * @param tintColor     着色后的颜色
 * @param drawableResId 图片资源 id
 */
fun ImageView.setTintColor(
    tintColor: Int,
    @DrawableRes drawableResId: Int? = null
) {
    drawableResId.action({
        ImageUtil.setTintColor(this, it, tintColor)
    }) {
        ImageUtil.setTintColor(this, tintColor)
    }
}

/**
 * 在协程中，使用 Coil 的同步方法加载图片。
 */
fun ImageView.load(imgUrl: Any?, placeholderDrawable: Drawable? = null) {
    GlobalScope.launch {
        val resultDrawable = Coil.imageLoader(context)
            .execute(
                ImageRequest.Builder(context)
                    .data(imgUrl)
                    .placeholder(placeholderDrawable)
                    .build()
            ).drawable
        withContext(Dispatchers.Main) {
            setImageDrawable(resultDrawable)
        }
    }
}

/**
 * 在协程中，使用 Coil 的同步方法加载图片。
 */
fun ImageView.load(
    imgUrl: Any?,
    placeholderDrawable: Drawable? = null,
    @Px width: Int = 0,
    @Px height: Int = 0,
    onStart: ((placeholder: Drawable?) -> Unit)? = null,
    onSuccess: ((result: Drawable) -> Unit)? = null,
    onError: ((error: Drawable?) -> Unit)? = null
) {
    GlobalScope.launch {
        Coil.imageLoader(context)
            .enqueue(
                ImageRequest.Builder(context)
                    .data(imgUrl)
                    .placeholder(placeholderDrawable)
                    .apply {
                        if (width > 0 && height > 0) {
                            size(width, height)
                        }
                    }
                    .target(
                        onStart = { placeholder ->
                            GlobalScope.launch(Dispatchers.Main) {
                                onStart?.invoke(placeholder)
                            }
                        },
                        onSuccess = { result ->
                            GlobalScope.launch(Dispatchers.Main) {
                                onSuccess?.invoke(result)
                            }
                        },
                        onError = { error ->
                            GlobalScope.launch(Dispatchers.Main) {
                                onError?.invoke(error)
                            }
                        }
                    )
                    .build()
            )
    }
}

/**
 * 通过 uri 加载图片
 */
fun getDrawableFromUri(uri: Uri): Drawable? {
    try {
        val parcelFileDescriptor: ParcelFileDescriptor? =
            appContext.contentResolver.openFileDescriptor(uri, "r")
        val fileDescriptor: FileDescriptor? = parcelFileDescriptor?.fileDescriptor
        val image: Bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor)
        parcelFileDescriptor?.close()
        return image.toDrawable()
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}
