package com.fphoenixcorneae.common.ext.view

import android.widget.SeekBar

inline fun SeekBar.setOnSeekBarChangeListener(
    crossinline onProgressChanged: (
        seekBar: SeekBar?, progress: Int, fromUser: Boolean
    ) -> Unit = { _, _, _ -> },
    crossinline onStartTrackingTouch: (seekBar: SeekBar?) -> Unit = {},
    crossinline onStopTrackingTouch: (seekBar: SeekBar?) -> Unit = {}
): SeekBar.OnSeekBarChangeListener {
    val onSeekBarChangeListener = object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            onProgressChanged.invoke(seekBar, progress, fromUser)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {
            onStartTrackingTouch.invoke(seekBar)
        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {
            onStopTrackingTouch.invoke(seekBar)
        }
    }
    setOnSeekBarChangeListener(onSeekBarChangeListener)
    return onSeekBarChangeListener
}