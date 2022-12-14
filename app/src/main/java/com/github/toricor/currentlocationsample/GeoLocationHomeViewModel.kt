package com.github.toricor.currentlocationsample

import android.app.Application
import androidx.compose.runtime.Stable
import androidx.lifecycle.*
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Stable
class GeoLocationHomeViewModel(application: Application) : AndroidViewModel(application) {
    private val geoLocationRepository =
        GeoLocationRepository(LocationServices.getFusedLocationProviderClient(application.applicationContext))

    private val _locationPayload = MutableLiveData<LocationPayload>(getEmptyLocationPayload())
    val locationPayload: LiveData<LocationPayload>
        get() = _locationPayload

    private val _isLocationGranted =
        MutableLiveData<Boolean>(LocationPermission(application.applicationContext).isPermissionGranted)
    val isLocationGranted: LiveData<Boolean>
        get() = _isLocationGranted

    private val _isUpdating = MutableLiveData<Boolean>(false)
    val isUpdating: LiveData<Boolean>
        get() = _isUpdating

    fun updateGranted(changedTo: Boolean) {
        _isLocationGranted.value = changedTo
    }

    fun updateCurrentLocation() {
        _isUpdating.value = true
        viewModelScope.launch(Dispatchers.Default) {
            _locationPayload.postValue(geoLocationRepository.getCurrentLocation())
            _isUpdating.postValue(false)
        }
    }

    /*
    fun updateLastLocation() {
        viewModelScope.launch {
            _locationPayload.value = geoLocationRepository.getLastLocation()
        }
    }
     */

    fun getEmptyLocationPayload(): LocationPayload {
        return GeoLocationUtil.getEmptyLocationPayload()
    }
}