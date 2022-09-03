package com.github.toricor.currentlocationsample

data class LocationPayload(
    val latitude: Double,
    val longitude: Double,
    val accuracy: Float,
    val time: Long,
    val speed: Float,
    val mocked: Boolean,
)
