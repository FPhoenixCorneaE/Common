package com.fphoenixcorneae.common.drawable

import android.content.Context

/**
 * constructs a gradient drawable
 */
fun gradientDrawable(
    context: Context,
    block: CodeGradientDrawable.Builder.() -> Unit
): CodeGradientDrawable =
    CodeGradientDrawable.Builder(context = context).apply { block() }.run { build() }

fun CodeGradientDrawable.Builder.solidColor(block: CodeColorStateList.Builder.() -> Unit) =
    solidColor(CodeColorStateList.Builder().apply { block() }.run { build() })

fun CodeGradientDrawable.Builder.gradient(block: Gradient.Builder.() -> Unit) =
    gradient(Gradient.Builder(context).apply { block() }.run { build() })

fun CodeGradientDrawable.Builder.corner(block: Corner.Builder.() -> Unit) =
    corner(Corner.Builder(context).apply { block() }.run { build() })

fun CodeGradientDrawable.Builder.stroke(block: Stroke.Builder.() -> Unit) =
    stroke(Stroke.Builder(context).apply { block() }.run { build() })

fun CodeGradientDrawable.Builder.padding(block: Padding.Builder.() -> Unit) =
    padding(Padding.Builder(context).apply { block() }.run { build() })

fun Stroke.Builder.color(block: CodeColorStateList.Builder.() -> Unit) =
    color(CodeColorStateList.Builder().apply { block() }.run { build() })

fun CodeColorStateList.Builder.item(block: SelectorColorItem.Builder.() -> Unit) =
    addSelectorColorItem(SelectorColorItem.Builder().apply { block() }.run { build() })

/**
 * constructs a state list drawable
 */
fun stateListDrawable(block: CodeStateListDrawable.Builder.() -> Unit) =
    CodeStateListDrawable.Builder().apply { block() }.run { build() }

fun CodeStateListDrawable.Builder.item(block: SelectorDrawableItem.Builder.() -> Unit) =
    addSelectorDrawableItem(SelectorDrawableItem.Builder().apply { block() }.run { build() })
