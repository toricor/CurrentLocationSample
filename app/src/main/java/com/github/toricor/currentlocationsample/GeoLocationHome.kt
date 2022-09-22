package com.github.toricor.currentlocationsample

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.toricor.currentlocationsample.ui.theme.CurrentLocationSampleTheme

@Composable
fun GeoLocationHome(
    viewModel: GeoLocationHomeViewModel,
    requestPermissionsOnClick: () -> Unit,
) {
    val isLocationGranted by viewModel.isLocationGranted.observeAsState()
    if (isLocationGranted == true) {
        GeoLocationHomePermissionsGranted(
            viewModel = viewModel,
        )
    } else {
        GeoLocationEmptyHome(
            locationPayload = viewModel.getEmptyLocationPayload(),
            onClick = { requestPermissionsOnClick() },
        )
    }
}

@Composable
fun GeoLocationHomePermissionsGranted(
    viewModel: GeoLocationHomeViewModel,
    modifier: Modifier = Modifier,
) {
    val rawLocationPayload by viewModel.locationPayload.observeAsState()
    val locationPayload = rawLocationPayload ?: viewModel.getEmptyLocationPayload()
    val geoLocationHomeState = rememberGeoLocationHomeState(locationPayload)
    StatefulGeoLocation(modifier, geoLocationHomeState) { viewModel.updateCurrentLocation() }
}

@Composable
fun GeoLocationEmptyHome(
    locationPayload: LocationPayload,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val geoLocationHomeState = rememberGeoLocationHomeState(locationPayload)
    StatefulGeoLocation(modifier, geoLocationHomeState) { onClick() }
    Log.d("GeoLocationHome", "GeoLocationEmptyHome")
}

@Composable
fun StatefulGeoLocation(
    modifier: Modifier = Modifier,
    state: GeoLocationHomeState,
    onClick: () -> Unit,
) {
    val locationPayload = state.location

    StatelessGeoLocation(
        latitude = locationPayload.latitude,
        longitude = locationPayload.longitude,
        accuracy = locationPayload.accuracy,
        time = locationPayload.time,
        speed = locationPayload.speed,
        mocked = locationPayload.mocked,
        onClick = onClick,
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

@Preview(
    showBackground = true,
    group = "GeoLocationHome",
)
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

@Preview(
    showBackground = true,
    group = "GeoLocationHome",
)
@Composable
fun GeoLocationDarkPreview() {
    CurrentLocationSampleTheme(darkTheme = true) {
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
