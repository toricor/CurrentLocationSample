package com.github.toricor.currentlocationsample

import android.Manifest
import android.content.Context
import android.location.Location
import android.os.SystemClock
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class GeoLocationRepositoryTest {
    private lateinit var context: Context
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    @get:Rule
    val rule: GrantPermissionRule
        get() = GrantPermissionRule.grant(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION,
        )

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        fusedLocationProviderClient.setMockMode(true).addOnFailureListener { throw it }
    }

    @After
    fun tearDown() {
        fusedLocationProviderClient.setMockMode(false).addOnFailureListener { throw it }
    }

    @Test
    fun locationPayloadIsValid() {
        val location = Location("mock").apply {
            latitude = 35.6812362
            longitude = 139.7671248
            speed = 42.0F
            accuracy = 0.68f
            time = System.currentTimeMillis()
            elapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos()
        }

        // To enable the mock location, enable the android.permission.ACCESS_MOCK_LOCATION permission in the AndroidManifest.xml file,
        // and set the application to the mock location app in the device setting.
        fusedLocationProviderClient.setMockLocation(location).addOnFailureListener { throw it }

        runBlocking {
            val locationPayload =
                GeoLocationRepository(fusedLocationProviderClient).getCurrentLocation()

            // TODO: make assertion method
            assertEquals(35.6812, locationPayload.latitude, 0.001)
            assertEquals(139.76712, locationPayload.longitude, 0.001)
            assertEquals(42.0F, locationPayload.speed, 0.001F)
            assertEquals(0.68F, locationPayload.accuracy, 0.001F)
            assertEquals(true, locationPayload.mocked)
        }
    }
}