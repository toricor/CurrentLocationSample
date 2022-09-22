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
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.github.toricor.currentlocationsample.ui.theme.CurrentLocationSampleTheme
import com.google.android.gms.common.GoogleApiAvailability


const val GOOGLE_PLAY_SERVICES_VALIDATION = 1

class MainActivity : ComponentActivity() {
    private var fineLocationPermission: Boolean = false
    private var coarseLocationPermission: Boolean = false

    private lateinit var viewModel: GeoLocationHomeViewModel

    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val isGrantedBefore = fineLocationPermission && coarseLocationPermission
            permissions.entries.forEach {
                Log.d("MainActivity:Permissions", "${it.key} = ${it.value}")

                if (it.key == "android.permission.ACCESS_FINE_LOCATION") {
                    fineLocationPermission = it.value == true
                }

                if (it.key == "android.permission.ACCESS_COARSE_LOCATION") {
                    coarseLocationPermission = it.value == true
                }
            }
            val isGranted = fineLocationPermission && coarseLocationPermission
            viewModel.updateGranted(isGranted)

            if (!isGrantedBefore && isGranted) {
                viewModel.updateCurrentLocation()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        validateGooglePlayServices(this)

        viewModel = GeoLocationHomeViewModel(application)
        updatePermissionStatuses()

        setContent {
            CurrentLocationSampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    GeoLocationHome(
                        viewModel = viewModel,
                        requestPermissionsOnClick = {
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

    private fun updatePermissionStatuses() {
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
