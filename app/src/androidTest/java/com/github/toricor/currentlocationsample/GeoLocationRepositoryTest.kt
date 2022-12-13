package com.github.toricor.currentlocationsample

import android.location.Location
import android.os.SystemClock
import androidx.test.platform.app.InstrumentationRegistry
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GeoLocationRepositoryTest {
    private lateinit var client: FusedLocationProviderClient

    private val location = Location("mock").apply {
        latitude = 35.6812362
        longitude = 139.7671248
        speed = 42.0F
        accuracy = 0.68f
        time = System.currentTimeMillis()
        elapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos()
    }

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        client = LocationServices.getFusedLocationProviderClient(context)
        client.setMockMode(true).addOnFailureListener { throw it }
    }

    @After
    fun tearDown() {
        client.setMockMode(false).addOnFailureListener { throw it }
    }

    @Test
    fun latitudeIsCorrect() {
        client.setMockLocation(location).addOnFailureListener { throw it }

        runTest {
            val acquiredLocation = GeoLocationRepository(client).getCurrentLocation()
            assertEquals(35.6812, acquiredLocation.latitude, 0.001)
        }
    }
}