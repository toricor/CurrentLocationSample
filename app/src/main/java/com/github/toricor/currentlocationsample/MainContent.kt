package com.github.toricor.currentlocationsample

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
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
            selected = currentDestination?.hierarchy?.any { it.route == "Places" } == true,
            onClick = { navController.navigate("Places") },
            icon = { Icon(imageVector = Icons.Default.Place, contentDescription = "Places") },
            label = { Text(text = "Places") }
        )
    }
}

// TODO: Add Contents
@Composable
fun Places() {
    Text("show places")
}

@Composable
fun MainContent(
    locationPayload: LocationPayload,
    isUpdating: Boolean,
    onClick: () -> Unit,
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
                GeoLocationHome(
                    locationPayload = locationPayload,
                    onClick = onClick,
                    isUpdating = isUpdating,
                    modifier = Modifier.padding(paddingValues),
                )
            }
            composable("Places") {
                Places()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CurrentLocationSampleTheme {
        MainContent(
            isUpdating = true,
            onClick = {},
            locationPayload = LocationPayload(
                latitude = 35.6809591,
                longitude = 139.7673068,
                accuracy = 12.589F,
                time = 1662194662614,
                speed = 50.03244F,
                mocked = false,
            ),
        )
    }
}