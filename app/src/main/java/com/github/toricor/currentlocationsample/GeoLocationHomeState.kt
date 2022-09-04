package com.github.toricor.currentlocationsample

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue


class GeoLocationHomeState(private val locationPayload: LocationPayload) {
    var location by mutableStateOf(locationPayload)

    companion object {
        val Saver = mapSaver(
            save = {
                mapOf(
                    "latitude" to it.locationPayload.latitude,
                    "longitude" to it.locationPayload.longitude,
                    "accuracy" to it.locationPayload.accuracy,
                    "speed" to it.locationPayload.speed,
                    "time" to it.locationPayload.time,
                    "mocked" to it.locationPayload.mocked,
                )
            }
        ) {
            GeoLocationHomeState(
                locationPayload = LocationPayload(
                    latitude = it["latitude"] as Double,
                    longitude = it["longitude"] as Double,
                    accuracy = it["accuracy"] as Float,
                    speed = it["speed"] as Float,
                    time = it["time"] as Long,
                    mocked = it["mocked"] as Boolean,
                )
            )
        }
    }
}

@Composable
fun rememberGeoLocationHomeState(locationPayload: LocationPayload): GeoLocationHomeState =
    rememberSaveable(locationPayload, saver = GeoLocationHomeState.Saver) {
        GeoLocationHomeState(locationPayload)
    }