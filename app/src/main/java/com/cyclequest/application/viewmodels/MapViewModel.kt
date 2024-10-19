package com.cyclequest.application.viewmodels

import androidx.lifecycle.ViewModel
import com.cyclequest.service.location.LocationService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val locationService: LocationService
) : ViewModel() {

    fun startLocationTracking() {
        locationService.startLocation()
    }

    fun stopLocationTracking() {
        locationService.stopLocation()
    }
}
