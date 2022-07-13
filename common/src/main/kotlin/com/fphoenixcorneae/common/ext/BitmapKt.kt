package com.fphoenixcorneae.common.ext

import android.content.res.Resources
import android.graphics.*
import android.media.ExifInterface
import android.os.Build
import android.renderscript.*
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.annotation.RequiresApi
import com.fphoenixcorneae.common.annotation.ImageType
import java.io.*
import java.util.*
import kotlin.math.abs

/**
 * 图片缩放
 * @param scale 缩放比例
 */
fun Bitmap.scale(scale: Float): Bitmap? = scale(scaleX = scale, scaleY = scale)

/**
 * 图片缩放
 * @param scaleX  The scale of width.
 * @param scaleY  The scale of height.
 * @param recycle True to recycle the source of bitmap, false otherwise.
 */
fun Bitmap.scale(scaleX: Float, scaleY: Float, recycle: Boolean = false): Bitmap? = run {
    if (isEmpty()) {
        null
    } else {
        val matrix = Matrix()
        matrix.postScale(scaleX, scaleY)
        val newBitmap = Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
        if (recycle && !isRecycled && newBitmap != this) {
            recycle()
        }
        newBitmap
    }
}

/**
 * 图片缩放
 * @param newWidth  缩放新宽度
 * @param newHeight 缩放新高度
 * @param recycle   是否回收
 */
fun Bitmap.scale(newWidth: Int, newHeight: Int, recycle: Boolean = false): Bitmap? = run {
    if (isEmpty()) {
        null
    } else {
        val newBitmap = Bitmap.createScaledBitmap(this, newWidth, newHeight, true)
        if (recycle && !isRecycled && newBitmap != this) {
            recycle()
        }
        newBitmap
    }
}

/**
 * file to bitmap.
 */
fun File?.toBitmap(maxWidth: Int = 0, maxHeight: Int = 0): Bitmap? = this?.run {
    if (maxWidth > 0 && maxHeight > 0) {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(absolutePath, options)
        options.inSampleSize = calculateBitmapInSampleSize(options, maxWidth, maxHeight)
        options.inJustDecodeBounds = false
        BitmapFactory.decodeFile(absolutePath, options)
    } else {
        BitmapFactory.decodeFile(absolutePath)
    }
}

/**
 * input stream to bitmap
 */
fun InputStream?.toBitmap(maxWidth: Int = 0, maxHeight: Int = 0, offset: Int = 0): Bitmap? = this?.run {
    if (maxWidth > 0 && maxHeight > 0) {
        toBytes()?.toBitmap(maxWidth, maxHeight, offset)
    } else {
        BitmapFactory.decodeStream(this)
    }
}

/**
 * byte array to bitmap
 */
fun ByteArray?.toBitmap(maxWidth: Int, maxHeight: Int, offset: Int = 0): Bitmap? = this?.run {
    if (maxWidth > 0 && maxHeight > 0) {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeByteArray(this, offset, this.size, options)
        options.inSampleSize = calculateBitmapInSampleSize(options, maxWidth, maxHeight)
        options.inJustDecodeBounds = false
        BitmapFactory.decodeByteArray(this, offset, this.size, options)
    } else {
        BitmapFactory.decodeByteArray(this, offset, this.size)
    }
}

/**
 * drawable resource id to bitmap.
 */
fun Int.toBitmap(maxWidth: Int = 0, maxHeight: Int = 0): Bitmap? = runCatching {
    if (maxWidth > 0 && maxHeight > 0) {
        val options = BitmapFactory.Options()
        val resources: Resources = applicationContext.resources
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(resources, this, options)
        options.inSampleSize = calculateBitmapInSampleSize(options, maxWidth, maxHeight)
        options.inJustDecodeBounds = false
        BitmapFactory.decodeResource(resources, this, options)
    } else {
        getDrawable(this)?.run {
            val canvas = Canvas()
            val bitmap = Bitmap.createBitmap(
                intrinsicWidth,
                intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            canvas.setBitmap(bitmap)
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            draw(canvas)
            bitmap
        }
    }
}.onFailure { e ->
    e.printStackTrace()
}.getOrNull()

/**
 * file descriptor to bitmap
 */
fun FileDescriptor?.toBitmap(maxWidth: Int = 0, maxHeight: Int = 0) = this?.run {
    if (maxWidth > 0 && maxHeight > 0) {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFileDescriptor(this, null, options)
        options.inSampleSize = calculateBitmapInSampleSize(options, maxWidth, maxHeight)
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeFileDescriptor(this, null, options)
    } else {
        BitmapFactory.decodeFileDescriptor(this)
    }
}

/**
 * Return the bitmap with the specified color.
 * @param color   The color.
 * @param recycle True to recycle the source of bitmap, false otherwise.
 * @return the bitmap with the specified color
 */
fun Bitmap.drawColor(@ColorInt color: Int, recycle: Boolean = false): Bitmap? = run {
    if (isEmpty()) {
        null
    } else {
        val newBitmap = if (recycle) this else copy(config, true)
        val canvas = Canvas(newBitmap)
        canvas.drawColor(color, PorterDuff.Mode.DARKEN)
        newBitmap
    }
}

/**
 * 图片裁剪
 * @param x       The x coordinate of the first pixel.
 * @param y       The y coordinate of the first pixel.
 * @param width   The width.
 * @param height  The height.
 * @param recycle True to recycle the source of bitmap, false otherwise.
 * @return the clipped bitmap
 */
fun Bitmap.clip(x: Int, y: Int, width: Int, height: Int, recycle: Boolean = false): Bitmap? = run {
    if (isEmpty()) {
        null
    } else {
        val newBitmap = Bitmap.createBitmap(this, x, y, width, height)
        if (recycle && !isRecycled && newBitmap != this) {
            recycle()
        }
        newBitmap
    }
}

/**
 * 图片倾斜
 * @param kx      The skew factor of x.
 * @param ky      The skew factor of y.
 * @param px      The x coordinate of the pivot point.
 * @param py      The y coordinate of the pivot point.
 * @param recycle True to recycle the source of bitmap, false otherwise.
 * @return the skewed bitmap
 */
fun Bitmap.skew(kx: Float, ky: Float, px: Float = 0f, py: Float = 0f, recycle: Boolean = false): Bitmap? = run {
    if (isEmpty()) {
        null
    } else {
        val matrix = Matrix()
        matrix.setSkew(kx, ky, px, py)
        val newBitmap = Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
        if (recycle && !isRecycled && newBitmap != this) {
            recycle()
        }
        newBitmap
    }
}

/**
 * 图片旋转
 * @param degrees The number of degrees.
 * @param px      The x coordinate of the pivot point.
 * @param py      The y coordinate of the pivot point.
 * @param recycle True to recycle the source of bitmap, false otherwise.
 * @return the rotated bitmap
 */
fun Bitmap.rotate(degrees: Float, px: Float, py: Float, recycle: Boolean = false): Bitmap? = run {
    if (isEmpty()) {
        null
    } else if (degrees == 0f) {
        this
    } else {
        val matrix = Matrix()
        matrix.setRotate(degrees, px, py)
        val newBitmap = Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
        if (recycle && !isRecycled && newBitmap != this) {
            recycle()
        }
        newBitmap
    }
}

/**
 * Return the rotated degree.
 * [ExifInterface]
 * 一个在JPEG文件或RAW图像文件中读写Exif标签的类
 * 支持的格式有：JPEG, DNG, CR2, NEF, NRW, ARW, RW2, ORF and RAF。
 * @return the rotated degree
 */
fun File.getRotateDegree(): Int = runCatching {
    when (ExifInterface(this).getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
        ExifInterface.ORIENTATION_ROTATE_90 -> 90
        ExifInterface.ORIENTATION_ROTATE_180 -> 180
        ExifInterface.ORIENTATION_ROTATE_270 -> 270
        else -> 0
    }
}.onFailure { e ->
    e.printStackTrace()
}.getOrDefault(-1)

/**
 * 图片变成圆图
 * @param borderSize  The size of border.
 * @param borderColor The color of border.
 * @return the round bitmap
 */
fun Bitmap.toRound(
    @IntRange(from = 0) borderSize: Int = 0,
    @ColorInt borderColor: Int = 0,
    recycle: Boolean = false,
): Bitmap? = run {
    if (isEmpty()) {
        null
    } else {
        val size = width.coerceAtMost(height)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        val newBitmap = Bitmap.createBitmap(width, height, config)
        val center = size / 2f
        val rectF = RectF(0f, 0f, width.toFloat(), height.toFloat())
        rectF.inset((width - size) / 2f, (height - size) / 2f)
        val matrix = Matrix()
        matrix.setTranslate(rectF.left, rectF.top)
        if (width != height) {
            matrix.preScale(size.toFloat() / width, size.toFloat() / height)
        }
        val shader = BitmapShader(this, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        shader.setLocalMatrix(matrix)
        paint.shader = shader
        val canvas = Canvas(newBitmap)
        canvas.drawRoundRect(rectF, center, center, paint)
        if (borderSize > 0) {
            paint.shader = null
            paint.color = borderColor
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = borderSize.toFloat()
            val radius = center - borderSize / 2f
            canvas.drawCircle(width / 2f, height / 2f, radius, paint)
        }
        if (recycle && !isRecycled && newBitmap != this) {
            recycle()
        }
        newBitmap
    }
}

/**
 * 图片变成圆角图片
 * @param cornerRadius The radius of corner.
 * @param borderSize   The size of border.
 * @param borderColor  The color of border.
 * @return the round corner bitmap
 */
fun Bitmap.toRoundCorner(
    cornerRadius: Float,
    @IntRange(from = 0) borderSize: Int = 0,
    @ColorInt borderColor: Int = 0,
    recycle: Boolean = false,
): Bitmap? = run {
    if (isEmpty()) {
        null
    } else {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        val newBitmap = Bitmap.createBitmap(width, height, config)
        val shader = BitmapShader(this, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        paint.shader = shader
        val canvas = Canvas(newBitmap)
        val rectF = RectF(0f, 0f, width.toFloat(), height.toFloat())
        val halfBorderSize = borderSize / 2f
        rectF.inset(halfBorderSize, halfBorderSize)
        canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, paint)
        if (borderSize > 0) {
            paint.shader = null
            paint.color = borderColor
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = borderSize.toFloat()
            paint.strokeCap = Paint.Cap.ROUND
            canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, paint)
        }
        if (recycle && !isRecycled && newBitmap != this) {
            recycle()
        }
        newBitmap
    }
}

/**
 * 图片添加边框
 * @param borderSize   The size of border.
 * @param borderColor  The color of border.
 * @param isCircle     True to draw circle, false to draw corner.
 * @param cornerRadius The radius of corner.
 * @param recycle      True to recycle the source of bitmap, false otherwise.
 * @return the bitmap with border
 */
fun Bitmap.addBorder(
    @IntRange(from = 1) borderSize: Int,
    @ColorInt borderColor: Int,
    isCircle: Boolean,
    cornerRadius: Float,
    recycle: Boolean = false,
): Bitmap? = run {
    if (isEmpty()) {
        null
    } else {
        val newBitmap = if (recycle) this else copy(config, true)
        val width = newBitmap.width
        val height = newBitmap.height
        val canvas = Canvas(newBitmap)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = borderColor
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = borderSize.toFloat()
        if (isCircle) {
            val radius = width.coerceAtMost(height) / 2f - borderSize / 2f
            canvas.drawCircle(width / 2f, height / 2f, radius, paint)
        } else {
            val halfBorderSize = borderSize shr 1
            val rectF = RectF(
                halfBorderSize.toFloat(), halfBorderSize.toFloat(),
                (width - halfBorderSize).toFloat(), (height - halfBorderSize).toFloat()
            )
            canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, paint)
        }
        newBitmap
    }
}

/**
 * 图片添加倒影
 * @param reflectionHeight The height of reflection.
 * @param reflectionGap    The gap between bitmap and reflection.
 * @param recycle          True to recycle the source of bitmap, false otherwise.
 * @return the bitmap with reflection
 */
fun Bitmap.addReflection(reflectionHeight: Int, reflectionGap: Float = 0f, recycle: Boolean = false): Bitmap? = run {
    if (isEmpty()) {
        null
    } else {
        val srcWidth = width
        val srcHeight = height
        val matrix = Matrix()
        matrix.preScale(1f, -1f)
        val reflectionBitmap = Bitmap.createBitmap(
            this, 0, srcHeight - reflectionHeight,
            srcWidth, reflectionHeight, matrix, false
        )
        val newBitmap = Bitmap.createBitmap(srcWidth, srcHeight + reflectionHeight, config)
        val canvas = Canvas(newBitmap)
        canvas.drawBitmap(this, 0f, 0f, null)
        canvas.drawBitmap(reflectionBitmap, 0f, srcHeight + reflectionGap, null)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        val shader = LinearGradient(
            0f, srcHeight.toFloat(),
            0f, newBitmap.height + reflectionGap,
            0x70FFFFFF,
            0x00FFFFFF,
            Shader.TileMode.MIRROR
        )
        paint.shader = shader
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
        canvas.drawRect(
            0f,
            srcHeight + reflectionGap,
            srcWidth.toFloat(),
            newBitmap.height.toFloat(),
            paint
        )
        if (!reflectionBitmap.isRecycled) {
            reflectionBitmap.recycle()
        }
        if (recycle && !isRecycled && newBitmap != this) {
            recycle()
        }
        newBitmap
    }
}

/**
 * 图片添加文字水印
 * @param content   The content of text.
 * @param textSize  The size of text.
 * @param textColor The color of text.
 * @param x         The x coordinate of the first pixel.
 * @param y         The y coordinate of the first pixel.
 * @param recycle   True to recycle the source of bitmap, false otherwise.
 * @return the bitmap with text watermarking
 */
fun Bitmap.addTextWatermark(
    content: String?,
    textSize: Float,
    @ColorInt textColor: Int,
    x: Float,
    y: Float,
    recycle: Boolean = false,
): Bitmap? = run {
    if (isEmpty() || content == null) {
        null
    } else {
        val newBitmap = copy(config, true)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        val canvas = Canvas(newBitmap)
        paint.color = textColor
        paint.textSize = textSize
        val bounds = Rect()
        paint.getTextBounds(content, 0, content.length, bounds)
        canvas.drawText(content, x, y + textSize, paint)
        if (recycle && !isRecycled && newBitmap != this) {
            recycle()
        }
        newBitmap
    }
}

/**
 * 图片添加图象水印
 * @param watermark The image watermarking.
 * @param x         The x coordinate of the first pixel.
 * @param y         The y coordinate of the first pixel.
 * @param alpha     The alpha of watermark.
 * @return the bitmap with image watermarking
 */
fun Bitmap.addImageWatermark(
    watermark: Bitmap?,
    x: Float,
    y: Float,
    alpha: Int,
    recycle: Boolean = false,
): Bitmap? = run {
    if (isEmpty()) {
        null
    } else {
        val newBitmap = copy(config, true)
        if (!watermark.isEmpty()) {
            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            val canvas = Canvas(newBitmap)
            paint.alpha = alpha
            canvas.drawBitmap(watermark!!, x, y, paint)
        }
        if (recycle && !isRecycled && newBitmap != this) {
            recycle()
        }
        newBitmap
    }
}

/**
 * 图片变成透明图片
 * @param recycle True to recycle the source of bitmap, false otherwise.
 * @return the alpha bitmap
 */
fun Bitmap.toAlpha(
    recycle: Boolean = false,
): Bitmap? = run {
    if (isEmpty()) {
        null
    } else {
        val newBitmap = extractAlpha()
        if (recycle && !isRecycled && newBitmap != this) {
            recycle()
        }
        newBitmap
    }
}

/**
 * 图片变成灰色图片
 * @param recycle True to recycle the source of bitmap, false otherwise.
 * @return the gray bitmap
 */
fun Bitmap.toGray(
    recycle: Boolean = false,
): Bitmap? = run {
    if (isEmpty()) {
        null
    } else {
        val newBitmap = Bitmap.createBitmap(width, height, config)
        val canvas = Canvas(newBitmap)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        val colorMatrix = ColorMatrix()
        colorMatrix.setSaturation(0f)
        val colorMatrixColorFilter = ColorMatrixColorFilter(colorMatrix)
        paint.colorFilter = colorMatrixColorFilter
        canvas.drawBitmap(this, 0f, 0f, paint)
        if (recycle && !isRecycled && newBitmap != this) {
            recycle()
        }
        newBitmap
    }
}

/**
 * 图片高斯模糊
 * zoom out, blur, zoom in
 * @param scale         The scale(0...1).
 * @param radius        The radius(0...25).
 * @param recycle       True to recycle the source of bitmap, false otherwise.
 * @param isReturnScale True to return the scale blur bitmap, false otherwise.
 * @return the blur bitmap
 */
fun Bitmap.fastBlur(
    @FloatRange(from = 0.0, to = 1.0, fromInclusive = false) scale: Float = 1f,
    @FloatRange(from = 0.0, to = 25.0, fromInclusive = false) radius: Float = 20f,
    recycle: Boolean = false,
    isReturnScale: Boolean = false,
): Bitmap? = run {
    if (isEmpty()) {
        null
    } else {
        val matrix = Matrix()
        matrix.setScale(scale, scale)
        var scaleBitmap = Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
        val paint = Paint(Paint.FILTER_BITMAP_FLAG or Paint.ANTI_ALIAS_FLAG)
        val canvas = Canvas()
        val filter = PorterDuffColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_ATOP)
        paint.colorFilter = filter
        canvas.scale(scale, scale)
        canvas.drawBitmap(scaleBitmap, 0f, 0f, paint)
        scaleBitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            scaleBitmap.renderScriptBlur(radius, recycle)
        } else {
            scaleBitmap.stackBlur(radius.toInt(), recycle)
        }
        if (scale == 1f || isReturnScale) {
            if (recycle && !isRecycled && scaleBitmap != this) {
                recycle()
            }
            scaleBitmap
        } else {
            val newBitmap = Bitmap.createScaledBitmap(scaleBitmap, width, height, true)
            if (!scaleBitmap.isRecycled) {
                scaleBitmap.recycle()
            }
            if (recycle && !isRecycled && newBitmap != this) {
                recycle()
            }
            newBitmap
        }
    }
}

/**
 * 使用渲染脚本使图片高斯模糊
 * @param radius  The radius(0...25).
 * @param recycle True to recycle the source of bitmap, false otherwise.
 * @return the blur bitmap
 */
@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
fun Bitmap.renderScriptBlur(
    @FloatRange(from = 0.0, to = 25.0, fromInclusive = false) radius: Float = 20f,
    recycle: Boolean = false,
): Bitmap = run {
    var rs: RenderScript? = null
    val newBitmap = if (recycle) this else copy(config, true)
    try {
        rs = RenderScript.create(applicationContext)
        rs.messageHandler = RenderScript.RSMessageHandler()
        val input =
            Allocation.createFromBitmap(rs, newBitmap, Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT)
        val output = Allocation.createTyped(rs, input.type)
        val blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
        blurScript.setInput(input)
        blurScript.setRadius(radius)
        blurScript.forEach(output)
        output.copyTo(newBitmap)
    } catch (e: RSIllegalArgumentException) {
        e.printStackTrace()
    } finally {
        rs?.destroy()
    }
    newBitmap
}

/**
 * 使用堆栈使图片高斯模糊
 * @param radius  The radius(0...25).
 * @param recycle True to recycle the source of bitmap, false otherwise.
 * @return the blur bitmap
 */
fun Bitmap.stackBlur(
    @FloatRange(from = 1.0, to = 25.0, fromInclusive = true) radius: Int = 20,
    recycle: Boolean = false,
): Bitmap = run {
    val newBitmap = if (recycle) this else copy(config, true)
    val w = newBitmap.width
    val h = newBitmap.height
    val pix = IntArray(w * h)
    newBitmap.getPixels(pix, 0, w, 0, 0, w, h)
    val wm = w - 1
    val hm = h - 1
    val wh = w * h
    val div = radius + radius + 1
    val r = IntArray(wh)
    val g = IntArray(wh)
    val b = IntArray(wh)
    var rsum: Int
    var gsum: Int
    var bsum: Int
    var x: Int
    var y: Int
    var i: Int
    var p: Int
    var yp: Int
    var yi: Int
    val vmin = IntArray(w.coerceAtLeast(h))
    var divsum = div + 1 shr 1
    divsum *= divsum
    val dv = IntArray(256 * divsum)
    i = 0
    while (i < 256 * divsum) {
        dv[i] = i / divsum
        i++
    }
    yi = 0
    var yw: Int = yi
    val stack = Array(div) { IntArray(3) }
    var stackpointer: Int
    var stackstart: Int
    var sir: IntArray
    var rbs: Int
    val r1 = radius + 1
    var routsum: Int
    var goutsum: Int
    var boutsum: Int
    var rinsum: Int
    var ginsum: Int
    var binsum: Int
    y = 0
    while (y < h) {
        bsum = 0
        gsum = bsum
        rsum = gsum
        boutsum = rsum
        goutsum = boutsum
        routsum = goutsum
        binsum = routsum
        ginsum = binsum
        rinsum = ginsum
        i = -radius
        while (i <= radius) {
            p = pix[yi + wm.coerceAtMost(i.coerceAtLeast(0))]
            sir = stack[i + radius]
            sir[0] = p and 0xff0000 shr 16
            sir[1] = p and 0x00ff00 shr 8
            sir[2] = p and 0x0000ff
            rbs = r1 - abs(i)
            rsum += sir[0] * rbs
            gsum += sir[1] * rbs
            bsum += sir[2] * rbs
            if (i > 0) {
                rinsum += sir[0]
                ginsum += sir[1]
                binsum += sir[2]
            } else {
                routsum += sir[0]
                goutsum += sir[1]
                boutsum += sir[2]
            }
            i++
        }
        stackpointer = radius
        x = 0
        while (x < w) {
            r[yi] = dv[rsum]
            g[yi] = dv[gsum]
            b[yi] = dv[bsum]
            rsum -= routsum
            gsum -= goutsum
            bsum -= boutsum
            stackstart = stackpointer - radius + div
            sir = stack[stackstart % div]
            routsum -= sir[0]
            goutsum -= sir[1]
            boutsum -= sir[2]
            if (y == 0) {
                vmin[x] = (x + radius + 1).coerceAtMost(wm)
            }
            p = pix[yw + vmin[x]]
            sir[0] = p and 0xff0000 shr 16
            sir[1] = p and 0x00ff00 shr 8
            sir[2] = p and 0x0000ff
            rinsum += sir[0]
            ginsum += sir[1]
            binsum += sir[2]
            rsum += rinsum
            gsum += ginsum
            bsum += binsum
            stackpointer = (stackpointer + 1) % div
            sir = stack[stackpointer % div]
            routsum += sir[0]
            goutsum += sir[1]
            boutsum += sir[2]
            rinsum -= sir[0]
            ginsum -= sir[1]
            binsum -= sir[2]
            yi++
            x++
        }
        yw += w
        y++
    }
    x = 0
    while (x < w) {
        bsum = 0
        gsum = bsum
        rsum = gsum
        boutsum = rsum
        goutsum = boutsum
        routsum = goutsum
        binsum = routsum
        ginsum = binsum
        rinsum = ginsum
        yp = -radius * w
        i = -radius
        while (i <= radius) {
            yi = 0.coerceAtLeast(yp) + x
            sir = stack[i + radius]
            sir[0] = r[yi]
            sir[1] = g[yi]
            sir[2] = b[yi]
            rbs = r1 - abs(i)
            rsum += r[yi] * rbs
            gsum += g[yi] * rbs
            bsum += b[yi] * rbs
            if (i > 0) {
                rinsum += sir[0]
                ginsum += sir[1]
                binsum += sir[2]
            } else {
                routsum += sir[0]
                goutsum += sir[1]
                boutsum += sir[2]
            }
            if (i < hm) {
                yp += w
            }
            i++
        }
        yi = x
        stackpointer = radius
        y = 0
        while (y < h) {
            // Preserve alpha channel: ( 0xff000000 & pix[yi] )
            pix[yi] =
                -0x1000000 and pix[yi] or (dv[rsum] shl 16) or (dv[gsum] shl 8) or dv[bsum]
            rsum -= routsum
            gsum -= goutsum
            bsum -= boutsum
            stackstart = stackpointer - radius + div
            sir = stack[stackstart % div]
            routsum -= sir[0]
            goutsum -= sir[1]
            boutsum -= sir[2]
            if (x == 0) {
                vmin[y] = (y + r1).coerceAtMost(hm) * w
            }
            p = x + vmin[y]
            sir[0] = r[p]
            sir[1] = g[p]
            sir[2] = b[p]
            rinsum += sir[0]
            ginsum += sir[1]
            binsum += sir[2]
            rsum += rinsum
            gsum += ginsum
            bsum += binsum
            stackpointer = (stackpointer + 1) % div
            sir = stack[stackpointer]
            routsum += sir[0]
            goutsum += sir[1]
            boutsum += sir[2]
            rinsum -= sir[0]
            ginsum -= sir[1]
            binsum -= sir[2]
            yi += w
            y++
        }
        x++
    }
    newBitmap.setPixels(pix, 0, w, 0, 0, w, h)
    newBitmap
}

/**
 * 保存图片
 */
fun Bitmap.save(
    filePath: String,
    format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
    quality: Int = 100,
    recycle: Boolean = false,
): Boolean = save(getFileByPath(filePath), format, quality, recycle)

/**
 * 保存图片
 * @param file    The file.
 * @param format  The format of the image.
 * @param quality The quality of the image.
 * @param recycle True to recycle the source of bitmap, false otherwise.
 * @return `true`: success<br></br>`false`: fail
 */
fun Bitmap.save(
    file: File?,
    format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
    quality: Int = 100,
    recycle: Boolean = false,
): Boolean = run {
    if (isEmpty() || !createFileByDeleteOldFile(file)) {
        false
    } else {
        var os: OutputStream? = null
        var success = false
        try {
            os = BufferedOutputStream(FileOutputStream(file))
            success = compress(format, quality, os)
            if (recycle && !isRecycled) {
                recycle()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            os.closeSafely()
        }
        success
    }
}

/**
 * 文件是否为图像
 * @return `true`: yes<br></br>`false`: no
 */
fun File?.isImage(): Boolean = run {
    if (this == null || !exists()) {
        false
    } else {
        runCatching {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(path, options)
            options.outWidth != -1 && options.outHeight != -1
        }.onFailure { e ->
            e.printStackTrace()
        }.getOrDefault(false)
    }
}

/**
 * 获取图片类型
 */
@ImageType
fun File?.getImageType(): String = this?.run {
    var `is`: InputStream? = null
    var type = ImageType.UNKNOWN
    try {
        `is` = FileInputStream(this)
        type = `is`.getImageType()
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        `is`.closeSafely()
    }
    type
} ?: ImageType.UNKNOWN

/**
 * 获取图片类型
 */
@ImageType
fun InputStream?.getImageType(): String = this?.runCatching {
    val bytes = ByteArray(12)
    if (read(bytes) != -1) {
        bytes.getImageType()
    } else {
        ImageType.UNKNOWN
    }
}?.onFailure { e ->
    e.printStackTrace()
}?.getOrDefault(ImageType.UNKNOWN) ?: ImageType.UNKNOWN

/**
 * 获取图片类型
 */
@ImageType
fun ByteArray.getImageType(): String = run {
    val type = toHexString().uppercase(Locale.getDefault())
    when {
        type.contains("FFD8FF") -> {
            ImageType.JPG
        }
        type.contains("89504E47") -> {
            ImageType.PNG
        }
        type.contains("47494638") -> {
            ImageType.GIF
        }
        type.contains("49492A00") || type.contains("4D4D002A") -> {
            ImageType.TIFF
        }
        type.contains("424D") -> {
            ImageType.BMP
        }
        type.startsWith("52494646") && type.endsWith("57454250") -> {
            //524946461c57000057454250-12个字节
            ImageType.WEBP
        }
        type.contains("00000100") || type.contains("00000200") -> {
            ImageType.ICO
        }
        else -> {
            ImageType.UNKNOWN
        }
    }
}

/**
 * 先判断是否为图像，再通过文件头表示判断图片格式
 */
fun File.isJpeg(): Boolean = toBytes()?.run { isJPEG(this) } ?: false
fun File.isPng(): Boolean = toBytes()?.run { isPNG(this) } ?: false
fun File.isGif(): Boolean = toBytes()?.run { isGIF(this) } ?: false
fun File.isTiff(): Boolean = toBytes()?.run { isTIFF(this) } ?: false
fun File.isBmp(): Boolean = toBytes()?.run { isBMP(this) } ?: false
fun File.isWebp(): Boolean = toBytes()?.run { isWEBP(this) } ?: false
fun File.isIco(): Boolean = toBytes()?.run { isICO(this) } ?: false

/**
 * JPEG/JPG - 文件头标识 (2 bytes): $ff, $d8 (SOI) (JPEG 文件标识) - 文件结束标识 (2 bytes): $ff, $d9 (EOI)
 */
private fun isJPEG(b: ByteArray): Boolean {
    return b.size >= 2 && b[0] == 0xFF.toByte() && b[1] == 0xD8.toByte()
}

/**
 * PNG - 文件头标识 (8 bytes)   89 50 4E 47 0D 0A 1A 0A
 */
private fun isPNG(b: ByteArray): Boolean {
    return b.size >= 8
            && b[0] == 0x89.toByte()
            && b[1] == 0x50.toByte()
            && b[2] == 0x4E.toByte()
            && b[3] == 0x47.toByte()
            && b[4] == 0x0D.toByte()
            && b[5] == 0x0A.toByte()
            && b[6] == 0x1A.toByte()
            && b[7] == 0x0A.toByte()
}

/**
 * GIF - 文件头标识 (6 bytes)   47 49 46 38 39(37) 61        G  I  F  8  9(7)  a
 */
private fun isGIF(b: ByteArray): Boolean {
    return b.size >= 6
            && b[0] == 'G'.code.toByte()
            && b[1] == 'I'.code.toByte()
            && b[2] == 'F'.code.toByte()
            && b[3] == '8'.code.toByte()
            && (b[4] == '7'.code.toByte() || b[4] == '9'.code.toByte())
            && b[5] == 'a'.code.toByte()
}

/**
 * TIFF - 文件头标识 (2 bytes)  4D 4D 或 49 49 或 49 20
 */
private fun isTIFF(b: ByteArray): Boolean {
    return b.size >= 2
            && ((b[0] == 0x4D.toByte() && b[1] == 0x4D.toByte())
            || (b[0] == 0x49.toByte() && b[1] == 0x49.toByte())
            || (b[0] == 0x49.toByte() && b[1] == 0x20.toByte()))
}

/**
 * BMP - 文件头标识 (2 bytes)   42 4D                         B  M
 */
private fun isBMP(b: ByteArray): Boolean {
    return b.size >= 2
            && b[0] == 0x42.toByte()
            && b[1] == 0x4D.toByte()
}

/**
 * WEBP - 文件头标识 (2 bytes)   52 49 46 46
 */
private fun isWEBP(b: ByteArray): Boolean {
    return b.size >= 2
            && b[0] == 0x52.toByte()
            && b[1] == 0x49.toByte()
}

/**
 * ICO - 文件头标识 (8 bytes)   00 00 01 00 01 00 20 20
 */
private fun isICO(b: ByteArray): Boolean {
    return b.size >= 8
            && b[0] == 0x00.toByte()
            && b[1] == 0x00.toByte()
            && b[2] == 0x01.toByte()
            && b[3] == 0x00.toByte()
            && b[4] == 0x01.toByte()
            && b[5] == 0x00.toByte()
            && b[6] == 0x20.toByte()
            && b[7] == 0x20.toByte()
}
// ======================================= about compress ======================================================
/**
 * 通过缩放压缩图片
 * @param newWidth  The new width.
 * @param newHeight The new height.
 * @param recycle   True to recycle the source of bitmap, false otherwise.
 * @return the compressed bitmap
 */
fun Bitmap.compressByScale(
    newWidth: Int,
    newHeight: Int,
    recycle: Boolean = false,
): Bitmap? = scale(newWidth, newHeight, recycle)

/**
 * 通过缩放压缩图片
 * @param scaleX      The scale of width.
 * @param scaleY      The scale of height.
 * @param recycle     True to recycle the source of bitmap, false otherwise.
 * @return the compressed bitmap
 */
fun Bitmap.compressByScale(
    scaleX: Float,
    scaleY: Float,
    recycle: Boolean = false,
): Bitmap? = scale(scaleX, scaleY, recycle)

/**
 * 通过质量压缩图片
 * @param quality The quality.
 * @param recycle True to recycle the source of bitmap, false otherwise.
 * @return the compressed data using quality
 */
fun Bitmap.compressByQuality(
    @IntRange(from = 0, to = 100) quality: Int,
    compressFormat: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
    recycle: Boolean = false,
): ByteArray? = run {
    if (isEmpty()) {
        null
    } else {
        val baos = ByteArrayOutputStream()
        compress(compressFormat, quality, baos)
        val bytes = baos.toByteArray()
        if (recycle && !isRecycled) {
            recycle()
        }
        bytes
    }
}

/**
 * 通过质量压缩图片
 * @param maxByteSize The maximum size of byte.
 * @param recycle     True to recycle the source of bitmap, false otherwise.
 * @return the compressed data using quality
 */
fun Bitmap.compressByQuality(
    maxByteSize: Long,
    compressFormat: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
    recycle: Boolean = false,
): ByteArray = run {
    if (isEmpty() || maxByteSize <= 0) {
        ByteArray(0)
    } else {
        val baos = ByteArrayOutputStream()
        compress(compressFormat, 100, baos)
        val bytes: ByteArray
        if (baos.size() <= maxByteSize) {
            bytes = baos.toByteArray()
        } else {
            baos.reset()
            compress(compressFormat, 0, baos)
            if (baos.size() >= maxByteSize) {
                bytes = baos.toByteArray()
            } else {
                // find the best quality using binary search
                var st = 0
                var end = 100
                var mid = 0
                while (st < end) {
                    mid = (st + end) / 2
                    baos.reset()
                    compress(compressFormat, mid, baos)
                    val len = baos.size()
                    if (len.toLong() == maxByteSize) {
                        break
                    } else if (len > maxByteSize) {
                        end = mid - 1
                    } else {
                        st = mid + 1
                    }
                }
                if (end == mid - 1) {
                    baos.reset()
                    compress(compressFormat, st, baos)
                }
                bytes = baos.toByteArray()
            }
        }
        if (recycle && !isRecycled) {
            recycle()
        }
        bytes
    }
}

/**
 * 通过样本大小压缩图片
 * @param sampleSize The sample size.
 * @param recycle    True to recycle the source of bitmap, false otherwise.
 * @return the compressed bitmap
 */
fun Bitmap.compressBySampleSize(
    sampleSize: Int,
    compressFormat: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
    recycle: Boolean = false,
): Bitmap? = run {
    if (isEmpty()) {
        null
    } else {
        val options = BitmapFactory.Options()
        options.inSampleSize = sampleSize
        val baos = ByteArrayOutputStream()
        compress(compressFormat, 100, baos)
        val bytes = baos.toByteArray()
        if (recycle && !isRecycled) {
            recycle()
        }
        BitmapFactory.decodeByteArray(bytes, 0, bytes.size, options)
    }
}

/**
 * 通过样本大小压缩图片
 * @param maxWidth  The maximum width.
 * @param maxHeight The maximum height.
 * @param recycle   True to recycle the source of bitmap, false otherwise.
 * @return the compressed bitmap
 */
fun Bitmap.compressBySampleSize(
    maxWidth: Int,
    maxHeight: Int,
    compressFormat: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
    recycle: Boolean = false,
): Bitmap? = run {
    if (isEmpty()) {
        null
    } else {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        val baos = ByteArrayOutputStream()
        compress(compressFormat, 100, baos)
        val bytes = baos.toByteArray()
        BitmapFactory.decodeByteArray(bytes, 0, bytes.size, options)
        options.inSampleSize = calculateBitmapInSampleSize(options, maxWidth, maxHeight)
        options.inJustDecodeBounds = false
        if (recycle && !isRecycled) {
            recycle()
        }
        BitmapFactory.decodeByteArray(bytes, 0, bytes.size, options)
    }
}

// ======================================= about compress ======================================================

/**
 * 获取图片的宽高大小
 * @return the size of bitmap
 */
fun File?.getSize(): IntArray = this?.run {
    val opts = BitmapFactory.Options()
    opts.inJustDecodeBounds = true
    BitmapFactory.decodeFile(absolutePath, opts)
    intArrayOf(opts.outWidth, opts.outHeight)
} ?: intArrayOf(0, 0)

/**
 * Return the sample size.
 *
 * @param options   The options.
 * @param maxWidth  The maximum width.
 * @param maxHeight The maximum height.
 * @return the sample size
 */
fun calculateBitmapInSampleSize(
    options: BitmapFactory.Options,
    maxWidth: Int,
    maxHeight: Int,
): Int {
    var height = options.outHeight
    var width = options.outWidth
    var inSampleSize = 1
    while (height > maxHeight || width > maxWidth) {
        height = height shr 1
        width = width shr 1
        inSampleSize = inSampleSize shl 1
    }
    return inSampleSize
}

fun Bitmap?.isEmpty(): Boolean = this == null || width == 0 || height == 0