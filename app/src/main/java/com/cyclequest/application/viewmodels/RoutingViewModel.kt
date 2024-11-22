package com.cyclequest.application.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amap.api.maps2d.model.LatLng
import com.cyclequest.core.network.ApiError
import com.cyclequest.core.network.ApiResult
import com.cyclequest.domain.model.PlannedPath
import com.cyclequest.service.route.RouteService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

import com.cyclequest.domain.repository.PlannedRouteRepository

@HiltViewModel
class RoutingViewModel @Inject constructor(
    private val routeService: RouteService,

    private val plannedRouteRepository: PlannedRouteRepository
) : ViewModel() {
    
    private val _routePoints = MutableStateFlow<List<LatLng>>(emptyList())
    val routePoints: StateFlow<List<LatLng>> = _routePoints.asStateFlow()

    private val _routeInfo = MutableStateFlow<RouteService.RouteInfo?>(null)
    val routeInfo: StateFlow<RouteService.RouteInfo?> = _routeInfo.asStateFlow()

    private val _isNavigationStarted = MutableStateFlow(false)
    val isNavigationStarted: StateFlow<Boolean> = _isNavigationStarted.asStateFlow()

    private val _isRouteInfoMinimized = MutableStateFlow(false)
    val isRouteInfoMinimized: StateFlow<Boolean> = _isRouteInfoMinimized.asStateFlow()

    private val _isSimulateNaviOn = MutableStateFlow(false)
    val isSimulateNaviOn: StateFlow<Boolean> = _isSimulateNaviOn.asStateFlow()

    init {
        viewModelScope.launch {
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
            routeService.routeInfo.collect { info ->
                _routeInfo.value = info
            }
        }
    }

    fun searchRoute(startPoint: LatLng, endPoint: LatLng) {
        viewModelScope.launch {
            routeService.searchRideRoute(startPoint, endPoint)
        }
    }

    fun clearRouteInfo() {
        _routeInfo.value = null
        _routePoints.value = emptyList()
    }

    fun toggleRouteInfoMinimized() {
        _isRouteInfoMinimized.value = !_isRouteInfoMinimized.value
    }

    fun NaviFlagRead():Boolean{
        return _isNavigationStarted.value
    }

    fun NaviFlag_Set() {
        _isNavigationStarted.value = true
    }

    fun NaviFlag_Reset() {
        _isNavigationStarted.value = false
    }

    fun simNavi_Set() {
        _isSimulateNaviOn.value = true
    }

    fun simNavi_Reset() {
        _isSimulateNaviOn.value = false
    }

    fun LatLng2doubleList(points: List<LatLng>): List<List<Double>> {
        return points.map { listOf(it.latitude, it.longitude) }
    }

    fun saveRoute(userId: String) {
        viewModelScope.launch {
            Log.i("RVM", "userId: $userId")
            plannedRouteRepository.saveRoute2DB(
                PlannedPath(
                    userId = userId, // "7df81256-fbbb-422c-8b6d-881f066320a5",
                    distance = _routeInfo.value?.totalDistance ?: 0f,
                    duration = _routeInfo.value?.totalDuration ?: 0,
                    routeData = LatLng2doubleList(routePoints.value),
//                    listOf( // Sample route data
//                        listOf(37.7749, 122.4194), // Example coordinates (latitude, longitude)
//                        listOf(34.0522, 118.2437)),
                    createdAt = System.currentTimeMillis(),
                )
            )
        }
    }

} 