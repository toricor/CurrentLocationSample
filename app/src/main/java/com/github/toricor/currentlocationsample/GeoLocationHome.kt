package com.github.toricor.currentlocationsample

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.github.toricor.currentlocationsample.ui.theme.CurrentLocationSampleTheme


@Composable
fun GeoLocationHome(
    viewModel: GeoLocationHomeViewModel,
    modifier: Modifier = Modifier,
) {
    val activity = LocalContext.current as Activity

    if (ActivityCompat.checkSelfPermission(
            activity,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            // TODO: show UI
            Log.d("MainActivity@", "shouldShowRequestPermissionRationale")
        } else {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                GRANT_LOCATION_PERMISSION
            )
        }
    }
    val rawLocationPayload by viewModel.locationPayload.observeAsState()
    val locationPayload = rawLocationPayload ?: viewModel.getEmptyLocationPayload()
    val geoLocationHomeState = rememberGeoLocationHomeState(locationPayload)
    StatefulGeoLocation(modifier, viewModel, geoLocationHomeState)
}

@Composable
fun StatefulGeoLocation(
    modifier: Modifier = Modifier,
    viewModel: GeoLocationHomeViewModel,
    state: GeoLocationHomeState = rememberGeoLocationHomeState(viewModel.getEmptyLocationPayload()),
) {
    val locationPayload = state.location

    StatelessGeoLocation(
        latitude = locationPayload.latitude,
        longitude = locationPayload.longitude,
        accuracy = locationPayload.accuracy,
        time = locationPayload.time,
        speed = locationPayload.speed,
        mocked = locationPayload.mocked,
        onClick = {
            viewModel.updateCurrentLocation()
        },
        modifier = modifier,
    )
}

@Composable
fun StatelessGeoLocation(
    latitude: Double,
    longitude: Double,
    accuracy: Float,
    time: Long,
    speed: Float,
    mocked: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(16.dp)) {
        Text(text = "latitude: $latitude")
        Text(text = "longitude: $longitude")
        Text(text = "accuracy: $accuracy")
        Text(text = "time: $time")
        Text(text = "speed: $speed")
        Text(text = "mocked: $mocked")
        Button(
            onClick = onClick,
            Modifier.padding(top = 8.dp)
        ) {
            Text("Update Current Location")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GeoLocationPreview() {
    CurrentLocationSampleTheme {
        StatelessGeoLocation(
            latitude = 35.6809591,
            longitude = 139.7673068,
            accuracy = 12.589F,
            time = 1662194662614,
            speed = 50.03244F,
            mocked = false,
            onClick = {},
        )
    }
}