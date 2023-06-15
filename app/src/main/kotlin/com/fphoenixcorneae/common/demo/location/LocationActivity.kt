package com.fphoenixcorneae.common.demo.location

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.fphoenixcorneae.common.demo.R
import com.fphoenixcorneae.common.ext.*

class LocationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)
    }

    override fun onResume() {
        super.onResume()
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            openApplicationDetailsSettings()
            return
        }
        registerLocationListener(0, 0, object : OnLocationChangeListener {
            override fun getLastKnownLocation(location: Location?) {
            }

            override fun onLocationChanged(location: Location?) {
                getAddress(location?.latitude ?: 0.0, location?.longitude ?: 0.0).let {
                    findViewById<TextView>(R.id.tvAddress).text = "$it"
                }
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            }
        })
    }

    override fun onPause() {
        super.onPause()
        unregisterLocationListener()
    }

    fun gpsEnabled(view: View) {
        (view as TextView).text = "Gps是否可用: $isGpsEnabled"
    }

    fun locationEnabled(view: View) {
        (view as TextView).text = "定位是否可用: $isLocationEnabled"
    }

    fun openGpsSetting(view: View) {
        openGpsSettings()
    }
}