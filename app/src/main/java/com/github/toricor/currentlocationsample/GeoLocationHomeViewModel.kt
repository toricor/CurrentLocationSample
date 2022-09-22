package com.github.toricor.currentlocationsample

import android.app.Application
import androidx.lifecycle.*
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch

class GeoLocationHomeViewModel(application: Application): AndroidViewModel(application) {
    private val geoLocationRepository = GeoLocationRepository(LocationServices.getFusedLocationProviderClient(application.applicationContext))

    private val _locationPayload = MutableLiveData<LocationPayload>(getEmptyLocationPayload())
    val locationPayload: LiveData<LocationPayload>
        get() = _locationPayload

    private val _isLocationGranted = MutableLiveData<Boolean>(LocationPermission(application.applicationContext).isPermissionGranted)
    val isLocationGranted: LiveData<Boolean>
        get() = _isLocationGranted

    fun updateGranted(changedTo: Boolean) {
        _isLocationGranted.value = changedTo
    }

    fun updateCurrentLocation() {
        viewModelScope.launch {
            _locationPayload.value = geoLocationRepository.getCurrentLocation()
        }
    }

    /*
    fun updateLastLocation() {
        viewModelScope.launch {
            _locationPayload.value = geoLocationRepository.getLastLocation()
        }
    }
     */

    fun getEmptyLocationPayload() : LocationPayload {
        return GeoLocationUtil.getEmptyLocationPayload()
    }
}