package com.github.toricor.currentlocationsample

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.github.toricor.currentlocationsample.ui.theme.CurrentLocationSampleTheme
import com.google.android.gms.common.GoogleApiAvailability


const val GOOGLE_PLAY_SERVICES_VALIDATION = 1
const val GRANT_LOCATION_PERMISSION = 2

class MainActivity : ComponentActivity() {
    private var fineLocationPermission: Boolean = false
    private var coarseLocationPermission: Boolean = false

    private lateinit var viewModel: GeoLocationHomeViewModel

    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                Log.d("MainActivity: ", "${it.key} = ${it.value}")

                if (it.key == "android.permission.ACCESS_FINE_LOCATION") {
                    fineLocationPermission = it.value == true
                }

                if (it.key == "android.permission.ACCESS_COARSE_LOCATION") {
                    coarseLocationPermission = it.value == true
                }
            }
            val isGranted = fineLocationPermission && coarseLocationPermission
            viewModel.updateGranted(isGranted)
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        validateGooglePlayServices(this)

        viewModel = GeoLocationHomeViewModel(application)

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fineLocationPermission = true
        }

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            coarseLocationPermission = true
        }


        setContent {
            CurrentLocationSampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val isLocationGranted by viewModel.isLocationGranted.observeAsState()
                    if (isLocationGranted == true) {
                        GeoLocationHome(
                            viewModel = viewModel,
                        )
                    } else {
                        GeoLocationEmptyHome(
                            locationPayload = viewModel.getEmptyLocationPayload(),
                            onClick = {
                                requestMultiplePermissions.launch(
                                    arrayOf(
                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.ACCESS_COARSE_LOCATION
                                    )
                                )
                            }
                        )
                    }
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
