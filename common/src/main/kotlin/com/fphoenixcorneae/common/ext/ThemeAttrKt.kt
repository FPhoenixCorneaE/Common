package com.fphoenixcorneae.common.ext

import android.content.Context
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.fragment.app.Fragment

/**
 * 获取主题属性颜色
 */
@ColorInt
fun Context.getThemeAttrColor(
    @AttrRes themeAttrId: Int,
    typedValue: TypedValue = TypedValue(),
    resolveRefs: Boolean = true,
) = run {
    theme.resolveAttribute(themeAttrId, typedValue, resolveRefs)
    typedValue.data
}

/**
 * 获取主题属性颜色
 */
@ColorInt
fun Fragment.getThemeAttrColor(
    @AttrRes themeAttrId: Int,
    typedValue: TypedValue = TypedValue(),
    resolveRefs: Boolean = true,
) = run {
    requireContext().theme.resolveAttribute(themeAttrId, typedValue, resolveRefs)
    typedValue.data
}