package com.cyclequest.application.viewmodels

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amap.api.location.AMapLocation
import com.amap.api.maps2d.model.CameraPosition
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.route.RouteSearch
import com.amap.api.services.route.WalkRouteResult
import com.cyclequest.domain.repository.AdministrativeDivisionRepository
import com.cyclequest.service.route.RouteService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import kotlinx.coroutines.launch
import android.util.Log
import com.amap.api.maps2d.model.LatLng

@HiltViewModel
class MapViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {   

    sealed class MapMode {
        object Default : MapMode()
        object Routing : MapMode()
        object Discovery : MapMode()
    }

    private val _mapMode = MutableStateFlow<MapMode>(MapMode.Default)
    val mapMode: StateFlow<MapMode> = _mapMode.asStateFlow()

    private val _currentLocation = MutableStateFlow<LatLng?>(null)
    val currentLocation: StateFlow<LatLng?> = _currentLocation.asStateFlow()

    fun setMapMode(mode: String) {
        _mapMode.value = when (mode) {
            "探索" -> MapMode.Discovery
            "路线" -> MapMode.Routing
            else -> MapMode.Default
        }
    }

    // 地理位置信息监听更新回调函数
    fun locationUpdateCallback(location: LatLng){
        Log.d("MapViewModel", "位置已更新到ViewModel: lat=${location.latitude}, lng=${location.longitude}")
        _currentLocation.value = location

    }

    fun updateCurrentLocation(location: LatLng) {
        // 这里可以添加任何你需要基于位置信息执行的逻辑
    }

    private fun checkLocationPermission(): Boolean =
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
}
