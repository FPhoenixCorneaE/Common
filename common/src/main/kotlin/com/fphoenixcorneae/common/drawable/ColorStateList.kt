package com.fphoenixcorneae.common.drawable

import android.util.SparseArray
import androidx.annotation.ColorInt
import java.lang.ref.WeakReference

/**
 * @desc：状态颜色列表
 * @date：2022/03/31 11:12
 */
class ColorStateList private constructor(
    private val states: Array<out IntArray>,
    private val colors: IntArray
) : android.content.res.ColorStateList(states, colors) {

    companion object {
        private val EMPTY = arrayOf(IntArray(0))

        /** Thread-safe cache of single-color ColorStateLists.  */
        private val sCache = SparseArray<WeakReference<ColorStateList>>()

        /**
         * @return A ColorStateList containing a single color.
         */
        fun valueOf(@ColorInt color: Int): ColorStateList {
            synchronized(sCache) {
                val index: Int = sCache.indexOfKey(color)
                if (index >= 0) {
                    val cached: ColorStateList? = sCache.valueAt(index).get()
                    if (cached != null) {
                        return cached
                    }

                    // Prune missing entry.
                    sCache.removeAt(index)
                }

                // Prune the cache before adding new ColorItems.
                val cacheSize: Int = sCache.size()
                for (i in cacheSize - 1 downTo 0) {
                    if (sCache.valueAt(i).get() == null) {
                        sCache.removeAt(i)
                    }
                }
                val csl = ColorStateList(EMPTY, intArrayOf(color))
                sCache.put(color, WeakReference(csl))
                return csl
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as ColorStateList
        return states.contentDeepEquals(other.states) && colors.contentEquals(other.colors)
    }

    override fun hashCode(): Int {
        return 31 * states.contentDeepHashCode() + colors.contentHashCode()
    }

    override fun toString(): String {
        return "ColorStateList(states=${states.contentToString()}, colors=${colors.contentToString()})"
    }

    class Builder {
        private var colorItems: MutableList<SelectorColorItem> = arrayListOf()
        private var maxAttributeNum = 0

        fun addSelectorColorItem(colorItemBuilder: SelectorColorItem.Builder) = apply {
            val colorItem = colorItemBuilder.build()
            colorItems.add(colorItem)
            maxAttributeNum = maxAttributeNum.coerceAtLeast(colorItem.states.size)
        }

        fun build(): ColorStateList {
            if (colorItems.isEmpty()) throw IllegalArgumentException("colorItems is empty")
            val colors = IntArray(colorItems.size)

            // 初始化数组
            val states = Array(colorItems.size) {
                IntArray(maxAttributeNum) {
                    0
                }
            }
            colorItems.forEachIndexed { index, colorItem ->
                colors[index] = colorItem.color
                colorItem.states.forEachIndexed { index2, state ->
                    states[index][index2] = state
                }
            }
            return ColorStateList(states, colors)
        }
    }
}

class SelectorColorItem private constructor(val color: Int, val states: MutableList<Int>) {
    class Builder {
        private var color: Int = -1
        private var states: MutableList<Int> = arrayListOf()

        fun color(color: Int): Builder = apply {
            this.color = color
        }

        /**
         * 动态添加正向状态 android:state_pressed="true"
         */
        fun addState(state: State): Builder = apply {
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

        internal fun build(): SelectorColorItem {
            if (color == -1) throw IllegalArgumentException("must call color method")

            /**
             * 两个SelectorColorItem添加state的内容相同，添加顺序不同，会被认为是相同
             * @see ColorStateList.equals(other: Any?)
             */
            states.sort()
            return SelectorColorItem(color, states)
        }
    }
}