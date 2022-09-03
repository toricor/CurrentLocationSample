package com.github.toricor.currentlocationsample

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch

class GeoLocationHomeViewModel: ViewModel() {
    private val _locationPayload = MutableLiveData<LocationPayload>(GeoLocationUtil.getEmptyLocationPayload())
    val locationPayload: LiveData<LocationPayload>
        get() = _locationPayload

    fun updateCurrentLocation(context: Context) {
        viewModelScope.launch {
            _locationPayload.value = GeoLocationResource(getFusedLocationProviderClient(context)).getCurrentLocation()
        }
    }

    fun updateLastLocation(context: Context) {
        viewModelScope.launch {
            _locationPayload.value = GeoLocationResource(getFusedLocationProviderClient(context)).getLastLocation()
        }
    }

    private fun getFusedLocationProviderClient(context: Context) : FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }

}