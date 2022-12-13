package com.github.toricor.currentlocationsample

import android.Manifest
import android.location.Location
import android.os.SystemClock
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import androidx.test.uiautomator.UiDevice
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GeoLocationRepositoryTest {
    private lateinit var client: FusedLocationProviderClient

    @get:Rule
    val rule: GrantPermissionRule
        get() = GrantPermissionRule.grant(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION,
        )

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        client = LocationServices.getFusedLocationProviderClient(context)
        client.setMockMode(true).addOnFailureListener { throw it }

        // see: https://android.suzu-sd.com/2020/09/android_geolocation_henkou_testrule/
        val uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        val setCmd = "appops set ${context.packageName} android:mock_location"
        uiDevice.executeShellCommand("${setCmd} allow")
    }

    @After
    fun tearDown() {
        client.setMockMode(false).addOnFailureListener { throw it }
    }

    @Test
    fun latitudeIsCorrect() {
        val mockLocation = Location("mock").apply {
            latitude = 35.6812362
            longitude = 139.7671248
            speed = 42.0F
            accuracy = 0.68f
            time = System.currentTimeMillis()
            elapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos()
        }

        client.setMockLocation(mockLocation).addOnFailureListener { throw it }

        runTest {
            val acquiredLocation = GeoLocationRepository(client).getCurrentLocation()
            assertEquals(35.6812, acquiredLocation.latitude, 0.001)
        }
    }
}