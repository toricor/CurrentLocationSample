package com.github.toricor.currentlocationsample

import android.annotation.SuppressLint
import android.location.Location
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.CompletableDeferred

class GeoLocationResource(private val locationProvider: FusedLocationProviderClient) {
    @SuppressLint("MissingPermission")
    suspend fun getLastLocation(): LocationPayload {
        val def = CompletableDeferred<LocationPayload>()
        val locationTask: Task<Location> = locationProvider.lastLocation

        locationTask.addOnSuccessListener { location: Location? ->
            Log.d("GeoLocationResource@getLastLocation: ", location.toString())
            def.complete(
                if (location == null) {
                    GeoLocationUtil.getEmptyLocationPayload()
                } else {
                    LocationPayload(
                        lat = location.latitude,
                        lng = location.longitude,
                    )
                }
            )
        }
        locationTask.addOnFailureListener {
            def.complete(GeoLocationUtil.getEmptyLocationPayload())
        }
        return def.await()
    }

    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(): LocationPayload {
        val def = CompletableDeferred<LocationPayload>()
        val cancellationTokenSource = CancellationTokenSource()
        val locationTask: Task<Location> = locationProvider.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY, cancellationTokenSource.token
        )

        locationTask.addOnSuccessListener { location: Location? ->
            Log.d("GeoLocationResource@getCurrentLocation", location.toString())
            def.complete(
                if (location == null) {
                    GeoLocationUtil.getEmptyLocationPayload()
                } else {
                    LocationPayload(
                        lat = location.latitude,
                        lng = location.longitude,
                    )
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

}