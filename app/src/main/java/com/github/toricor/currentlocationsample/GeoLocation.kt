package com.github.toricor.currentlocationsample

import android.annotation.SuppressLint
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.CompletableDeferred

class GeoLocation(private val locationProvider: FusedLocationProviderClient) {
    @SuppressLint("MissingPermission")
    suspend fun getLastLocation(): LocationPayload {
        val def = CompletableDeferred<LocationPayload>()
        val locationTask: Task<Location> = locationProvider.lastLocation

        locationTask.addOnSuccessListener {
            def.complete(
                if (it == null) {
                    getEmptyLocationPayload()
                } else {
                    LocationPayload(
                        lat = it.latitude,
                        lng = it.longitude,
                    )
                }
            )
        }
        locationTask.addOnFailureListener {
            def.complete(getEmptyLocationPayload())
        }
        return def.await()
    }

    private fun getEmptyLocationPayload(): LocationPayload {
        return LocationPayload(0.0, 0.0)
    }
}