package com.fphoenixcorneae.common.drawable

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.os.Build

/**
 * @desc：StateListDrawable
 * @date：2022/03/31 11:14
 * todo 增加缓存机制
 */
class CodeStateListDrawable private constructor() : StateListDrawable() {
    class Builder {
        private var drawableItems: MutableList<SelectorDrawableItem> = arrayListOf()
        private var isConstantSize: Boolean = false
        private var variablePadding: Boolean = false

        private var enterFadeDuration: Int = 0
        private var exitFadeDuration: Int = 0
        private var dither: Boolean = false
        private var autoMirrored: Boolean = false

        fun addSelectorDrawableItem(drawableItem: SelectorDrawableItem) = apply {
            drawableItems.add(drawableItem)
        }

        fun constantSize(isConstantSize: Boolean) = apply {
            this.isConstantSize = isConstantSize
        }

        fun variablePadding(variablePadding: Boolean) = apply {
            this.variablePadding = variablePadding
        }

        /**
         * Added in API level 11
         * 只能在Android API level 11上生效
         */
        fun enterFadeDuration(enterFadeDuration: Int) = apply {
            this.enterFadeDuration = enterFadeDuration
        }

        /**
         * Added in API level 11
         * 只能在Android API level 11上生效
         */
        fun exitFadeDuration(exitFadeDuration: Int) = apply {
            this.exitFadeDuration = exitFadeDuration
        }

        fun dither(dither: Boolean) = apply {
            this.dither = dither
        }

        /**
         * Added in API level 19
         * 只能在Android API level 11上生效
         */
        fun autoMirrored(autoMirrored: Boolean) = apply {
            this.autoMirrored = autoMirrored
        }

        @SuppressLint("ObsoleteSdkInt")
        fun build(): CodeStateListDrawable {
            val stateListDrawable = CodeStateListDrawable()
            for (item in drawableItems) {
                stateListDrawable.addState(item.states.toIntArray(), item.drawable)
            }
            val constantState = stateListDrawable.constantState as DrawableContainerState
            constantState.isConstantSize = this.isConstantSize
            constantState.setVariablePadding(this.variablePadding)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                constantState.enterFadeDuration = this.enterFadeDuration
                constantState.exitFadeDuration = this.exitFadeDuration
            }
            stateListDrawable.setDither(dither)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                stateListDrawable.isAutoMirrored = autoMirrored
            }
            return stateListDrawable
        }
    }
}

class SelectorDrawableItem private constructor(
    val drawable: Drawable,
    val states: MutableList<Int>
) {
    class Builder {
        private lateinit var drawable: Drawable
        private var states: MutableList<Int> = arrayListOf()

        fun drawable(drawable: Drawable): Builder = apply {
            this.drawable = drawable
        }

        /**
         * 动态添加正向状态 android:state_pressed="true"
         */
        fun state(state: State): Builder = apply {
            if (!states.contains(state.value)) {
                states.add(state.value)
            }
        }

        /**
         * 动态添加逆向状态 android:state_pressed="false"
         */
        fun minusState(state: State): Builder = apply {
            if (!states.contains(-state.value)) {
                states.add(-state.value)
            }
        }

        internal fun build(): SelectorDrawableItem {
            return SelectorDrawableItem(drawable, states)
        }
    }
}