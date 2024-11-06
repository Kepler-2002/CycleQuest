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
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.route.RouteSearch
import com.amap.api.services.route.WalkRouteResult
import com.cyclequest.domain.repository.AdministrativeDivisionRepository
import com.cyclequest.service.location.LocationService
import com.cyclequest.service.route.RouteService
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
    private val routeService: RouteService,
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

    private val _routePoints = MutableStateFlow<List<LatLng>>(emptyList())
    val routePoints: StateFlow<List<LatLng>> = _routePoints.asStateFlow()

    private val _routeInfo = MutableStateFlow<RouteService.RouteInfo?>(null)
    val routeInfo: StateFlow<RouteService.RouteInfo?> = _routeInfo.asStateFlow()

    private val _isRouteInfoMinimized = MutableStateFlow(false)
    val isRouteInfoMinimized: StateFlow<Boolean> = _isRouteInfoMinimized.asStateFlow()

    init {
        viewModelScope.launch {
            // 监听路线点数据
            routeService.rideRouteResult.collect { result ->
                result?.paths?.firstOrNull()?.steps?.flatMap { step ->
                    step.polyline?.map { point ->
                        LatLng(point.latitude, point.longitude)
                    } ?: emptyList()
                }?.let { points ->
                    _routePoints.value = points
                }
            }
        }

        viewModelScope.launch {
            // 监听路线信息数据
            routeService.routeInfo.collect { info ->
                _routeInfo.value = info
            }
        }
    }

    fun setMapMode(mode: String) {
        viewModelScope.launch {
            when (mode) {
                "探索" -> loadDiscoveryMode()
                "路线" -> loadRoutingMode()
                else -> _mapMode.value = MapMode.Default
            }
        }
    }

    // Routing Mode
    private suspend fun loadRoutingMode() {
        _mapMode.value = MapMode.Routing

        // 假定起点和终点（这里以北京天安门和王府井为例）
        val startPoint = LatLng(39.909187, 116.397451)  // 天安门
        val endPoint = LatLng(39.914759, 116.408333)    // 王府井

        routeService.searchRideRoute(startPoint, endPoint)
    }

    // Discovery Mode
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

    fun clearRouteInfo() {
        _routeInfo.value = null
        _routePoints.value = emptyList()
    }

    fun toggleRouteInfoMinimized() {
        _isRouteInfoMinimized.value = !_isRouteInfoMinimized.value
    }
}
