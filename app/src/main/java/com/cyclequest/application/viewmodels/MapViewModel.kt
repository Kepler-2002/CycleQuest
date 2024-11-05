package com.cyclequest.application.viewmodels

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amap.api.location.AMapLocation
import com.amap.api.maps2d.model.CameraPosition
import com.amap.api.maps2d.model.LatLng
import com.cyclequest.domain.repository.AdministrativeDivisionRepository
import com.cyclequest.service.location.LocationService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import kotlinx.coroutines.launch
import android.util.Log

@HiltViewModel
class MapViewModel @Inject constructor(
    private val locationService: LocationService,
    @ApplicationContext private val context: Context,
    private val administrativeDivisionRepository: AdministrativeDivisionRepository
) : ViewModel() {

    sealed class MapMode {
        object Default : MapMode()
        object Routing : MapMode()
        data class Discovery(val boundaryPoints: List<LatLng>) : MapMode()
    }

    private val _mapMode = MutableStateFlow<MapMode>(MapMode.Default)
    val mapMode: StateFlow<MapMode> = _mapMode.asStateFlow()

    private val _currentLocation = MutableStateFlow<LatLng?>(null)
    val currentLocation: StateFlow<LatLng?> = _currentLocation.asStateFlow()

    fun setMapMode(mode: String) {
        viewModelScope.launch {
            when (mode) {
                "探索" -> loadDiscoveryMode()
                "路线" -> _mapMode.value = MapMode.Routing
                else -> _mapMode.value = MapMode.Default
            }
        }
    }

    private suspend fun loadDiscoveryMode() {
        administrativeDivisionRepository.getAdministrativeDivisionBoundary("150000")
            .onSuccess { division ->
                _mapMode.value = MapMode.Discovery(division.boundaryPoints)
            }
            .onFailure { error ->
                // 错误处理
                _mapMode.value = MapMode.Default
            }
    }

    fun updateCurrentLocation() {
        if (checkLocationPermission()) {
            locationService.getCurrentLocation { location ->
                location?.let {
                    _currentLocation.value = LatLng(it.latitude, it.longitude)
                }
            }
        }
    }

    private fun checkLocationPermission(): Boolean =
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
}
