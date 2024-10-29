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
import com.cyclequest.service.location.LocationService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import kotlinx.coroutines.launch
import timber.log.Timber

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
            Timber.d("MapViewModel: 开始获取当前位置")
            locationService.getCurrentLocation { location ->
                Timber.d("MapViewModel: 收到位置更新 - $location")
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
            Timber.d("MapViewModel: 缺少定位权限")
        }
        _forceUpdateCamera.value++
    }

    fun loadAdministrativeBoundary(divisionCode: String) {
        viewModelScope.launch {
            administrativeDivisionRepository.getAdministrativeDivisionBoundary(divisionCode)
                .onSuccess { division ->
                    _boundaryPoints.value = division.boundaryPoints
                }
                .onFailure {
                    Timber.e(it, "加载行政区划边界失败")
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        locationService.destroy()
    }
}
