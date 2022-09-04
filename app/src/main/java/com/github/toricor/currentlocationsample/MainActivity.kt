package com.github.toricor.currentlocationsample

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.github.toricor.currentlocationsample.ui.theme.CurrentLocationSampleTheme
import com.google.android.gms.common.GoogleApiAvailability


const val GOOGLE_PLAY_SERVICES_VALIDATION = 1
const val GRANT_LOCATION_PERMISSION = 2

class MainActivity : ComponentActivity() {
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
                    GeoLocationHome(viewModel = GeoLocationHomeViewModel(application))
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
}
