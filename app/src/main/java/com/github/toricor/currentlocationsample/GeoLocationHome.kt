package com.github.toricor.currentlocationsample

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
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
    val locationPayload: LocationPayload
    val onClick: () -> Unit

    val isLocationGranted by viewModel.isLocationGranted.observeAsState(false)
    if (isLocationGranted) {
        val rawLocationPayload by viewModel.locationPayload.observeAsState()
        locationPayload = rawLocationPayload ?: viewModel.getEmptyLocationPayload()
        onClick = { viewModel.updateCurrentLocation() }
    } else {
        locationPayload = viewModel.getEmptyLocationPayload()
        onClick = { requestPermissionsOnClick() }
    }
    val isUpdating by viewModel.isUpdating.observeAsState(false)

    StatefulGeoLocation(
        locationPayload = locationPayload,
        onClick = { onClick() },
        isUpdating = isUpdating,
    )
}

@Composable
fun StatefulGeoLocation(
    locationPayload: LocationPayload,
    onClick: () -> Unit,
    isUpdating: Boolean,
    modifier: Modifier = Modifier,
) {
    val geoLocationHomeState = rememberGeoLocationHomeState(locationPayload)

    StatelessGeoLocationWithLoading(
        locationPayload = geoLocationHomeState.location,
        onClick = { onClick() },
        isUpdating = isUpdating,
        modifier = modifier,
    )
}

@Composable
fun StatelessGeoLocationWithLoading(
    locationPayload: LocationPayload,
    onClick: () -> Unit,
    isUpdating: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(16.dp)) {
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
        Spacer(Modifier.height(8.dp))
        if (isUpdating) {
            CircularProgressIndicator()
        }
    }
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
    Column(modifier = modifier) {
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
fun GeoLocationWithLoadingPreview() {
    CurrentLocationSampleTheme {
        StatelessGeoLocationWithLoading(
            locationPayload = LocationPayload(
                latitude = 35.6809591,
                longitude = 139.7673068,
                accuracy = 12.589F,
                time = 1662194662614,
                speed = 50.03244F,
                mocked = false,
            ),
            onClick = {},
            isUpdating = true,
        )
    }
}

@Preview(
    showBackground = true,
    group = "GeoLocationHome",
)
@Composable
fun GeoLocationWithLoadingDarkPreview() {
    CurrentLocationSampleTheme(darkTheme = true) {
        StatelessGeoLocationWithLoading(
            locationPayload = LocationPayload(
                latitude = 35.6809591,
                longitude = 139.7673068,
                accuracy = 12.589F,
                time = 1662194662614,
                speed = 50.03244F,
                mocked = false,
            ),
            onClick = {},
            isUpdating = true,
        )
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
