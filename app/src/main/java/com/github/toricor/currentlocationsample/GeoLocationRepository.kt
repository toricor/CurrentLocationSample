package com.github.toricor.currentlocationsample

import android.annotation.SuppressLint
import android.location.Location
import android.os.Build
import android.util.Log
import androidx.core.location.LocationCompat
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.CompletableDeferred

class GeoLocationRepository(private val locationProvider: FusedLocationProviderClient) {

    private val currentLocationRequest = CurrentLocationRequest.Builder().apply {
        setPriority(Priority.PRIORITY_HIGH_ACCURACY)
        setDurationMillis(1000L)
        setMaxUpdateAgeMillis(30000L)
    }.build()

    /*
    @SuppressLint("MissingPermission")
    suspend fun getLastLocation(): LocationPayload {
        val def = CompletableDeferred<LocationPayload>()
        val locationTask: Task<Location> = locationProvider.lastLocation

        locationTask.addOnSuccessListener { location: Location? ->
            Log.d("GeoLocationRepository@getLastLocation: ", location.toString())
            def.complete(
                if (location == null) {
                    getEmptyLocationPayload()
                } else {
                    buildLocationPayload(location)
                }
            )
        }
        locationTask.addOnFailureListener {
            def.complete(getEmptyLocationPayload())
        }
        return def.await()
    }
     */

    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(): LocationPayload {
        val def = CompletableDeferred<LocationPayload>()
        val cancellationTokenSource = CancellationTokenSource()
        val locationTask: Task<Location> = locationProvider.getCurrentLocation(
            currentLocationRequest, cancellationTokenSource.token
        )

        locationTask.addOnSuccessListener { location: Location? ->
            Log.d("GeoLocationRepository@getCurrentLocation", location.toString())
            def.complete(
                if (location == null) {
                    getEmptyLocationPayload()
                } else {
                    buildLocationPayload(location)
                }
            )
        }
        locationTask.addOnFailureListener {
            def.completeExceptionally(it)
        }

        return try {
            def.await()
        } finally {
            cancellationTokenSource.cancel()
        }
    }

    private fun buildLocationPayload(location: Location): LocationPayload {
        return LocationPayload(
            latitude = location.latitude,
            longitude = location.longitude,
            accuracy = location.accuracy,
            time = location.time,
            speed = if (!location.speed.isNaN()) location.speed else 0.0F,
            mocked = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                location.isMock
            } else {
                // Use Location.isMock() on Android S and above, otherwise use LocationCompat.isMock() from the compat libraries instead.
                // https://developers.google.com/android/reference/com/google/android/gms/location/FusedLocationProviderClient#constant-summary
                LocationCompat.isMock(location)
            },
        )
    }

    private fun getEmptyLocationPayload(): LocationPayload {
        return GeoLocationUtil.getEmptyLocationPayload()
    }
}