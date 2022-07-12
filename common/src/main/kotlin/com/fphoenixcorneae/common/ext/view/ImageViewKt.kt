package com.fphoenixcorneae.common.ext.view

import android.content.res.ColorStateList
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.Px
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import coil.Coil
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import coil.transform.Transformation
import com.fphoenixcorneae.common.ext.action
import com.fphoenixcorneae.common.ext.applicationContext
import com.fphoenixcorneae.common.ext.toDrawable
import kotlinx.coroutines.*

/**
 * 获取可以进行tint的Drawable
 *
 * 对原drawable进行重新实例化  newDrawable()
 * 包装  warp()
 * 可变操作 mutate()
 *
 * @param drawable  原始drawable
 * @param tint      使用着色
 * @return 可着色的drawable
 */
fun getCanTintDrawable(
    drawable: Drawable?,
    tint: ColorStateList,
): Drawable? = drawable?.run {
    // constantState - 获取此drawable的共享状态实例
    // 对drawable 进行重新实例化、包装、可变操作
    DrawableCompat.wrap(constantState?.newDrawable() ?: this)
        .mutate()
}?.apply {
    DrawableCompat.setTintList(this, tint)
}

/**
 * 图片着色
 * @param tint          着色后的颜色
 * @param drawableResId 图片资源 id
 */
fun ImageView.setTintColor(
    @ColorInt tint: Int,
    @DrawableRes drawableResId: Int? = null,
) = setTintColorStateList(ColorStateList.valueOf(tint), drawableResId)

/**
 * 图片着色
 * @param tint          着色后的颜色
 * @param drawableResId 图片资源 id
 */
fun ImageView.setTintColorStateList(
    tint: ColorStateList,
    @DrawableRes drawableResId: Int? = null,
) = drawableResId.action({
    ContextCompat.getDrawable(applicationContext, it)?.let {
        setImageDrawable(getCanTintDrawable(it, tint))
    }
}) {
    setImageDrawable(getCanTintDrawable(drawable, tint))
}

/**
 * 在协程中，使用 Coil 的同步方法加载图片。
 */
fun ImageView.load(
    imgUrl: Any?,
    placeholderDrawable: Drawable? = null,
    isCircle: Boolean = false,
) {
    CoroutineScope(Dispatchers.Default).launch {
        val resultDrawable = Coil.imageLoader(context)
            .execute(
                ImageRequest.Builder(context)
                    .data(imgUrl)
                    .placeholder(placeholderDrawable)
                    .transformations(arrayListOf<Transformation>().apply {
                        if (isCircle) {
                            add(CircleCropTransformation())
                        }
                    })
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
    onError: ((error: Drawable?) -> Unit)? = null,
) {
    CoroutineScope(Dispatchers.Default).launch {
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
                            MainScope().launch {
                                onStart?.invoke(placeholder)
                            }
                        },
                        onSuccess = { result ->
                            MainScope().launch {
                                onSuccess?.invoke(result)
                            }
                        },
                        onError = { error ->
                            MainScope().launch {
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
fun getDrawableFromUri(uri: Uri): Drawable? = runCatching {
    val parcelFileDescriptor = applicationContext.contentResolver.openFileDescriptor(uri, "r")
    val fileDescriptor = parcelFileDescriptor?.fileDescriptor
    val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
    parcelFileDescriptor?.close()
    image.toDrawable()
}.onFailure { e ->
    e.printStackTrace()
}.getOrNull()
