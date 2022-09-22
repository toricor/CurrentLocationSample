package com.github.toricor.currentlocationsample

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

class LocationPermission(private val context: Context?) {
    val isPermissionGranted: Boolean
        get() = isAccessFineLocationGranted() && isAccessCoarseLocationGranted()

    private fun isAccessFineLocationGranted(): Boolean {
        if (context == null) return false
        return ContextCompat.checkSelfPermission(
            context,
            ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun isAccessCoarseLocationGranted(): Boolean {
        if (context == null) return false
        return ContextCompat.checkSelfPermission(
            context,
            ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
}