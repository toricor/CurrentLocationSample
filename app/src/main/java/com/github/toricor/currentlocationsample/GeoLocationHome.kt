package com.github.toricor.currentlocationsample

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
    } else {
        StatefulGeoLocation(activity, viewModel, modifier)
    }
}

@Composable
fun StatefulGeoLocation(
    context: Context,
    viewModel: GeoLocationHomeViewModel,
    modifier: Modifier = Modifier
) {
    val locationPayload by viewModel.locationPayload.observeAsState()

    StatelessGeoLocation(
        lat = locationPayload?.lat ?: 0.0,
        lng = locationPayload?.lng ?: 0.0,
        onClick = { viewModel.updateCurrentLocation(context) },
        modifier = modifier,
    )
}

@Composable
fun StatelessGeoLocation(
    lat: Double,
    lng: Double,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(16.dp)) {
        Text(text = "lat: $lat")
        Text(text = "lng: $lng")
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
        StatelessGeoLocation(35.6809591, 139.7673068, {})
    }
}