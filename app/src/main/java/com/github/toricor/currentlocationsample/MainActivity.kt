package com.github.toricor.currentlocationsample

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.github.toricor.currentlocationsample.ui.theme.CurrentLocationSampleTheme
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.LocationServices

const val GOOGLE_PLAY_SERVICES_VALIDATION = 1
const val GRANT_LOCATION_PERMISSION = 2

class MainActivity : ComponentActivity() {
    private var currentLatitude: Double = 0.0
    private var currentLongitude: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        validateGooglePlayServices(this)



        setContent {
            CurrentLocationSampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting(currentLatitude, currentLongitude)
                }
            }
        }
    }

    private fun validateGooglePlayServices(activity: Activity) {
        GoogleApiAvailability.getInstance().apply {
            val errorCode: Int = isGooglePlayServicesAvailable(activity)
            showErrorDialogFragment(
                activity,
                errorCode,
                GOOGLE_PLAY_SERVICES_VALIDATION,
            ) {
                activity.finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (ActivityCompat.checkSelfPermission(
                this,
                ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, ACCESS_FINE_LOCATION)) {
                // TODO: show UI
                Log.d("MainActivity@", "shouldShowRequestPermissionRationale")
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION),
                    GRANT_LOCATION_PERMISSION
                )
            }
        } else {
            lifecycleScope.launchWhenResumed {
                val locationProviderClient =
                    LocationServices.getFusedLocationProviderClient(this@MainActivity)
                val locationPayload: LocationPayload =
                    GeoLocation(locationProviderClient).getCurrentLocation()
                currentLatitude = locationPayload.lat
                currentLongitude = locationPayload.lng
            }
        }
    }

    override fun onPause() {
        super.onPause()
    }
}



@Composable
fun Greeting(lat: Double, lng: Double) {
    Text(text = "lat: $lat \nlng: $lng")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CurrentLocationSampleTheme {
        Greeting(35.6809591, 139.7673068)
    }
}