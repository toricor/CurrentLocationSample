package com.github.toricor.currentlocationsample

object GeoLocationUtil {
    fun getEmptyLocationPayload(): LocationPayload {
        return LocationPayload(
            latitude = 0.0,
            longitude = 0.0,
            accuracy = 0.0F,
            time = 0,
            speed = 0.0F,
            mocked = false,
        )
    }
}