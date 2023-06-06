package com.fphoenixcorneae.common.ext

import android.content.pm.PackageManager
import android.graphics.SurfaceTexture
import android.hardware.Camera
import android.util.Log
import java.io.IOException

private var mCamera: Camera? = null
private var mSurfaceTexture: SurfaceTexture? = null
/**
 * Return whether the device supports flashlight.
 *
 * @return `true`: yes<br></br>`false`: no
 */
val isFlashlightEnable: Boolean
    get() = applicationContext.packageManager
        .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)

/**
 * Return whether the flashlight is working.
 *
 * @return `true`: yes<br></br>`false`: no
 */
val isFlashlightOn: Boolean
    get() {
        if (!init()) return false
        val parameters = mCamera?.parameters
        return Camera.Parameters.FLASH_MODE_TORCH == parameters?.flashMode
    }

/**
 * Turn on or turn off the flashlight.
 *
 * @param isOn True to turn on the flashlight, false otherwise.
 */
fun setFlashlightStatus(isOn: Boolean) {
    if (!init()) return
    val parameters = mCamera?.parameters
    if (isOn) {
        if (Camera.Parameters.FLASH_MODE_TORCH != parameters?.flashMode) {
            try {
                mCamera?.setPreviewTexture(mSurfaceTexture)
                mCamera?.startPreview()
                parameters?.flashMode = Camera.Parameters.FLASH_MODE_TORCH
                mCamera?.parameters = parameters
            } catch (e: IOException) {
                Log.e("Flashlight", "setFlashlightStatusOn: ", e)
            }
        }
    } else {
        if (Camera.Parameters.FLASH_MODE_OFF != parameters?.flashMode) {
            parameters?.flashMode = Camera.Parameters.FLASH_MODE_OFF
            mCamera?.parameters = parameters
        }
    }
}

/**
 * release the flashlight.
 */
fun releaseCamera() {
    mCamera?.release()
    mSurfaceTexture = null
    mCamera = null
}

private fun init(): Boolean {
    if (mCamera == null) {
        try {
            mCamera = Camera.open(0)
            mSurfaceTexture = SurfaceTexture(0)
        } catch (t: Throwable) {
            Log.e("Flashlight", "init failed: ", t)
            return false
        }
    }
    if (mCamera == null) {
        Log.e("Flashlight", "init failed.")
        return false
    }
    return true
}