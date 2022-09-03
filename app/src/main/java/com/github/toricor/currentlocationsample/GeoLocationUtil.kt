package com.github.toricor.currentlocationsample

object GeoLocationUtil {
    fun getEmptyLocationPayload(): LocationPayload {
        return LocationPayload(0.0, 0.0)
    }
}