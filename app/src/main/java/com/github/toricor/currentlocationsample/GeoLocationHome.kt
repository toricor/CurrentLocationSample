package com.github.toricor.currentlocationsample

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.github.toricor.currentlocationsample.ui.theme.CurrentLocationSampleTheme


@Composable
fun TopBar(
    title: String,
) {
    TopAppBar(
        title = { Text(text = title) },
        backgroundColor = MaterialTheme.colors.primary
    )
}

@Composable
fun BottomBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    BottomNavigation(elevation = 10.dp) {
        BottomNavigationItem(
            selected = currentDestination?.hierarchy?.any { it.route == "Home" } == true,
            onClick = { navController.navigate("Home") },
            icon = { Icon(imageVector = Icons.Default.Home, contentDescription = "Home") },
            label = { Text(text = "Home") }
        )
        BottomNavigationItem(
            selected = currentDestination?.hierarchy?.any { it.route == "Favorite" } == true,
            onClick = { navController.navigate("Favorite") },
            icon = { Icon(imageVector = Icons.Default.AccountBox, contentDescription = "Favorite") },
            label = { Text(text = "Favorite") }
        )
    }
}

@Composable
fun Favorite() {
    Text("favorite")
}

@Composable
fun GeoLocationHome(
    viewModel: GeoLocationHomeViewModel,
    requestPermissionsOnClick: () -> Unit,
) {
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Open))
    val navController = rememberNavController()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { TopBar(title = "CurrentLocation") },
        bottomBar = { BottomBar(navController) },
    ) { paddingValues ->
        NavHost(navController = navController, startDestination = "Home") {
            composable("Home") {
                GeoLocationMainContent(
                    viewModel = viewModel,
                    requestPermissionsOnClick = requestPermissionsOnClick,
                    modifier = Modifier.padding(paddingValues),
                )
            }
            composable("Favorite") {
                Favorite()
            }
        }
    }
}

@Composable
fun GeoLocationMainContent(
    viewModel: GeoLocationHomeViewModel,
    requestPermissionsOnClick: () -> Unit,
    modifier: Modifier,
) {
    val isLocationGranted by viewModel.isLocationGranted.observeAsState(false)
    val isUpdating by viewModel.isUpdating.observeAsState(false)
    val rawLocationPayload by viewModel.locationPayload.observeAsState(viewModel.getEmptyLocationPayload())
    val geoLocationHomeState = rememberGeoLocationHomeState(rawLocationPayload)

    StatelessGeoLocationWithLoading(
        locationPayload = geoLocationHomeState.location,
        onClick = {
            if (isLocationGranted) {
                viewModel.updateCurrentLocation()
            } else {
                requestPermissionsOnClick()
            }
        },
        isUpdating = isUpdating,
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
            enabled = !isUpdating,
        )
        if (isUpdating) {
            Spacer(Modifier.height(8.dp))
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
    enabled: Boolean,
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
            enabled = enabled,
            modifier = Modifier.padding(top = 8.dp)
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
        Surface {
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
}

@Preview(
    showBackground = true,
    group = "GeoLocationHome",
)
@Composable
fun GeoLocationWithLoadingNotUpdatingPreview() {
    CurrentLocationSampleTheme {
        Surface {
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
                isUpdating = false,
            )
        }
    }
}

@Preview(
    showBackground = true,
    group = "GeoLocationHome",
)
@Composable
fun GeoLocationWithLoadingDarkPreview() {
    CurrentLocationSampleTheme(darkTheme = true) {
        Surface {
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
}

@Preview(
    showBackground = true,
    group = "GeoLocationHome",
)
@Composable
fun GeoLocationWithLoadingNotUpdatingDarkPreview() {
    CurrentLocationSampleTheme(darkTheme = true) {
        Surface {
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
                isUpdating = false,
            )
        }
    }
}
