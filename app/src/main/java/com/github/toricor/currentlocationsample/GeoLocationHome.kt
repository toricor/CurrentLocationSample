package com.github.toricor.currentlocationsample

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
        val rawLocationPayload by viewModel.locationPayload.observeAsState()
        val locationPayload = rawLocationPayload ?: viewModel.getEmptyLocationPayload()
        StatefulGeoLocation(
            locationPayload = locationPayload,
            onClick = { viewModel.updateCurrentLocation() },
        )
    } else {
        StatefulGeoLocation(
            locationPayload = viewModel.getEmptyLocationPayload(),
            onClick = { requestPermissionsOnClick() },
        )
    }
}

@Composable
fun StatefulGeoLocation(
    locationPayload: LocationPayload,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val geoLocationHomeState = rememberGeoLocationHomeState(locationPayload)
    val location = geoLocationHomeState.location
    StatelessGeoLocation(
        latitude = location.latitude,
        longitude = location.longitude,
        accuracy = location.accuracy,
        time = location.time,
        speed = location.speed,
        mocked = location.mocked,
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
