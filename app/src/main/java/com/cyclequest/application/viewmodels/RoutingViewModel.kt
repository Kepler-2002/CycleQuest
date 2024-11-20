package com.cyclequest.application.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amap.api.maps2d.model.LatLng
import com.cyclequest.service.route.RouteService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RoutingViewModel @Inject constructor(
    private val routeService: RouteService
) : ViewModel() {
    
    private val _routePoints = MutableStateFlow<List<LatLng>>(emptyList())
    val routePoints: StateFlow<List<LatLng>> = _routePoints.asStateFlow()

    private val _routeInfo = MutableStateFlow<RouteService.RouteInfo?>(null)
    val routeInfo: StateFlow<RouteService.RouteInfo?> = _routeInfo.asStateFlow()

    private val _isNavigationStarted = MutableStateFlow(false)
    val isNavigationStarted: StateFlow<Boolean> = _isNavigationStarted.asStateFlow()

    private val _isRouteInfoMinimized = MutableStateFlow(false)
    val isRouteInfoMinimized: StateFlow<Boolean> = _isRouteInfoMinimized.asStateFlow()

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

    fun NaviFlag_Set() {
        _isNavigationStarted.value = true
    }

    fun NaviFlag_Reset() {
        _isNavigationStarted.value = false
    }
} 