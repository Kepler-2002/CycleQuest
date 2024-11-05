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

    private val _currentLocation = MutableStateFlow<AMapLocation?>(null)
    val currentLocation: StateFlow<AMapLocation?> = _currentLocation

    private val _permissionGranted = MutableStateFlow(false)
    val permissionGranted: StateFlow<Boolean> = _permissionGranted

    private val _cameraPosition = MutableStateFlow<CameraPosition?>(null)
    val cameraPosition: StateFlow<CameraPosition?> = _cameraPosition

    private val _forceUpdateCamera = MutableStateFlow(0)
    val forceUpdateCamera: StateFlow<Int> = _forceUpdateCamera

    private val _boundaryPoints = MutableStateFlow<List<LatLng>>(emptyList())
    val boundaryPoints: StateFlow<List<LatLng>> = _boundaryPoints.asStateFlow()

    private val _mapMode = MutableStateFlow("地图")
    val mapMode: StateFlow<String> = _mapMode.asStateFlow()

    init {
        checkLocationPermission()
    }

    private fun checkLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun getCurrentLocation() {
        if (checkLocationPermission()) {
            Log.d("MapViewModel", "开始获取当前位置")
            locationService.getCurrentLocation { location ->
                Log.d("MapViewModel", "收到位置更新 - $location")
                location?.let {
                    _currentLocation.value = it
                    _cameraPosition.value = CameraPosition(
                        LatLng(it.latitude, it.longitude),
                        18f,  // 使用更大的缩放级别
                        0f,
                        0f
                    )
                }
            }
        } else {
            Log.d("MapViewModel", "缺少定位权限")
        }
        _forceUpdateCamera.value++
    }

    fun loadAdministrativeBoundary(divisionCode: String) {
        viewModelScope.launch {
            Log.d("MapViewModel", "开始加载行政区划边界: $divisionCode")
            administrativeDivisionRepository.getAdministrativeDivisionBoundary(divisionCode)
                .onSuccess { division ->
                    Log.d("MapViewModel", "成功获取边界数据，点数量: ${division.boundaryPoints.size}")
                    _boundaryPoints.value = division.boundaryPoints
                }
                .onFailure { error ->
                    Log.e("MapViewModel", "加载行政区划边界失败", error)
                }
        }
    }

    fun setMapMode(mode: String) {
        _mapMode.value = mode
        if (mode == "探索") {
            loadAdministrativeBoundary("150000")  // 切换到探索模式时加载边界
        }
    }

    override fun onCleared() {
        super.onCleared()
        locationService.destroy()
    }
}
