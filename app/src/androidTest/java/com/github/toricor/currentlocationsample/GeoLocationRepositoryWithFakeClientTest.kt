package com.github.toricor.currentlocationsample

import android.app.PendingIntent
import android.location.Location
import android.os.Looper
import android.os.SystemClock
import com.google.android.gms.common.api.Api
import com.google.android.gms.common.api.internal.ApiKey
import com.google.android.gms.location.*
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.util.concurrent.Executor

class FakeFusedLocationProviderClient : FusedLocationProviderClient {
    var shouldFail = false

    override fun getApiKey(): ApiKey<Api.ApiOptions.NoOptions> {
        TODO("Not yet implemented")
    }

    override fun flushLocations(): Task<Void> {
        TODO("Not yet implemented")
    }

    override fun getCurrentLocation(p0: Int, p1: CancellationToken?): Task<Location> {
        TODO("Not yet implemented")
    }

    override fun getCurrentLocation(
        p0: CurrentLocationRequest,
        p1: CancellationToken?
    ): Task<Location> {
        val location = Location("gps").apply {
            latitude = 35.6812362
            longitude = 139.7671248
            speed = 42.0F
            accuracy = 0.68f
            time = System.currentTimeMillis()
            elapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos()
        }
        return if (shouldFail) {
            Tasks.forException(Exception())
        } else {
            Tasks.forResult(location)
        }
    }

    override fun getLastLocation(): Task<Location> {
        TODO("Not yet implemented")
    }

    override fun getLastLocation(p0: LastLocationRequest): Task<Location> {
        TODO("Not yet implemented")
    }

    override fun getLocationAvailability(): Task<LocationAvailability> {
        TODO("Not yet implemented")
    }

    override fun removeLocationUpdates(p0: PendingIntent): Task<Void> {
        TODO("Not yet implemented")
    }

    override fun removeLocationUpdates(p0: LocationCallback): Task<Void> {
        TODO("Not yet implemented")
    }

    override fun removeLocationUpdates(p0: LocationListener): Task<Void> {
        TODO("Not yet implemented")
    }

    override fun requestLocationUpdates(p0: LocationRequest, p1: PendingIntent): Task<Void> {
        TODO("Not yet implemented")
    }

    override fun requestLocationUpdates(
        p0: LocationRequest,
        p1: LocationCallback,
        p2: Looper?
    ): Task<Void> {
        TODO("Not yet implemented")
    }

    override fun requestLocationUpdates(
        p0: LocationRequest,
        p1: LocationListener,
        p2: Looper?
    ): Task<Void> {
        TODO("Not yet implemented")
    }

    override fun requestLocationUpdates(
        p0: LocationRequest,
        p1: Executor,
        p2: LocationCallback
    ): Task<Void> {
        TODO("Not yet implemented")
    }

    override fun requestLocationUpdates(
        p0: LocationRequest,
        p1: Executor,
        p2: LocationListener
    ): Task<Void> {
        TODO("Not yet implemented")
    }

    override fun setMockLocation(p0: Location): Task<Void> {
        TODO("Not yet implemented")
    }

    override fun setMockMode(p0: Boolean): Task<Void> {
        TODO("Not yet implemented")
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class GeoLocationRepositoryWithFakeClientTest {
    private lateinit var fakeClient: FakeFusedLocationProviderClient

    @Before
    fun setupClient() {
        fakeClient = FakeFusedLocationProviderClient()
    }

    @Test
    fun latitudeIsCorrect() {
        runTest {
            val acquiredLocation = GeoLocationRepository(fakeClient).getCurrentLocation()
            assertEquals(35.6812362, acquiredLocation.latitude, 0.0001)
        }
    }

    @Test
    fun zeroLatitudeIsAcquiredWhenFail() {
        runTest {
            fakeClient.shouldFail = true
            val acquiredLocation = GeoLocationRepository(fakeClient).getCurrentLocation()
            assertEquals(0.0, acquiredLocation.latitude, 0.0001)
        }
    }
}