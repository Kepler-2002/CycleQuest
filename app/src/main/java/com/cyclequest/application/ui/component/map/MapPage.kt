package com.cyclequest.application.ui.components.map

import android.graphics.Color
import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver

import com.amap.api.maps2d.AMap
import com.amap.api.maps2d.MapView
import com.amap.api.maps2d.CameraUpdateFactory
import com.amap.api.maps2d.model.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import com.cyclequest.R
import com.cyclequest.service.location.LocationService

enum class SimulationMode {
    NONE,           // 不模拟，使用真实定位
    DISCOVERY,      // 探索模式的模拟定位
    NAVIGATION      // 导航模式的模拟定位
}
@Composable
fun MapPage(
    modifier: Modifier = Modifier,
    cameraPositionState: CameraPositionState = rememberCameraPositionState(),
    onMapReady: (AMap) -> Unit,
    onLocationChanged: (LatLng) -> Unit,
    simulationMode: SimulationMode,
    locationService: LocationService,
    navigationRoute: List<LatLng> = emptyList()
) {
    val mapView = rememberMapViewWithLifecycle(
        onLocationChanged,
        simulationMode,
        locationService,
        navigationRoute
    )
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> mapView.onCreate(null)
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    DisposableEffect(mapView) {
        onMapReady(mapView.map)
        onDispose { }
    }

    AndroidView(
        modifier = modifier,
        factory = { mapView },
        update = { view ->
            view.map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPositionState.position))
        }
    )
}

@Composable
fun rememberMapViewWithLifecycle(
    onLocationChanged: (LatLng) -> Unit,
    simulationMode: SimulationMode,
    locationService: LocationService,
    navigationRoute: List<LatLng>
): MapView {
    val context = LocalContext.current
    val mapView = remember { MapView(context) }
    val mAMap: AMap = mapView.map
    val scope = rememberCoroutineScope()
    
    var lastLocation by remember { mutableStateOf<LatLng?>(null) }
    var simulationJob by remember { mutableStateOf<Job?>(null) }
    var locationMarker by remember { mutableStateOf<Marker?>(null) }
    var currentZoom by remember { mutableStateOf(17f) }

    // 添加地图缩放监听
    LaunchedEffect(Unit) {
        mAMap.setOnCameraChangeListener(object : AMap.OnCameraChangeListener {
            override fun onCameraChange(position: CameraPosition?) {}
            override fun onCameraChangeFinish(position: CameraPosition?) {
                position?.let { currentZoom = it.zoom }
            }
        })
    }

    // 初始化定位和标记
    LaunchedEffect(Unit) {
        locationService.getCurrentLocation { location ->
            location?.let {
                val initialLatLng = LatLng(it.latitude, it.longitude)
                // 确保先移除旧的marker
                locationMarker?.remove()
                locationMarker = mAMap.addMarker(MarkerOptions().apply {
                    position(initialLatLng)
                    icon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker))
                    anchor(0.5f, 0.5f)
                })
                mAMap.animateCamera(CameraUpdateFactory.newLatLngZoom(initialLatLng, 17f))
                lastLocation = initialLatLng
            }
        }
    }

    // 处理定位模式切换
    DisposableEffect(simulationMode) {
        simulationJob?.cancel()

        // 确保不会创建新的marker，而是更新现有的marker位置
        when (simulationMode) {
            SimulationMode.NONE -> {
                // 真实定位模式
                locationService.setLocationInterval(2000)
                locationService.startLocationUpdates()
                
                scope.launch {
                    locationService.currentLocation.collect { location ->
                        location?.let {
                            val currentLatLng = LatLng(it.latitude, it.longitude)
                            locationMarker?.position = currentLatLng
                            lastLocation = currentLatLng
                            onLocationChanged(currentLatLng)
                        }
                    }
                }
            }
            
            SimulationMode.DISCOVERY -> {
                // 探索模式的模拟定位
                locationService.stopLocationUpdates()
                simulationJob = scope.launch {
                    val startLocation = lastLocation ?: LatLng(22.31251, 114.1928467)
                    var currentLat = startLocation.latitude
                    var currentLng = startLocation.longitude

                    while (isActive) {
                        currentLat += 0.0005
                        currentLng += 0.0005
                        val simulatedLocation = LatLng(currentLat, currentLng)
                        locationMarker?.position = simulatedLocation
                        mAMap.animateCamera(CameraUpdateFactory.newLatLngZoom(simulatedLocation, currentZoom))
                        onLocationChanged(simulatedLocation)
                        delay(1000)
                    }
                }
            }
            
            SimulationMode.NAVIGATION -> {
                // 导航模式的模拟定位
                locationService.stopLocationUpdates()
                if (navigationRoute.isNotEmpty()) {
                    simulationJob = scope.launch {
                        // TODO: 实现导航路线的模拟移
                    }
                }
            }
        }

        onDispose {
            simulationJob?.cancel()
            simulationJob = null
            locationService.stopLocationUpdates()
        }
    }

    // 清理资源
    DisposableEffect(Unit) {
        onDispose {
            locationMarker?.remove()
            locationMarker = null
        }
    }

    return mapView
}

@Composable
fun rememberCameraPositionState(
    key: String? = null,
    initialPosition: CameraPosition = CameraPosition(LatLng(22.31251, 114.1928467), 10f, 0f, 0f)
): CameraPositionState = rememberSaveable(key = key, saver = CameraPositionState.Saver) {
    CameraPositionState(initialPosition)
}

class CameraPositionState(initialPosition: CameraPosition) {
    var position by mutableStateOf(initialPosition)

    companion object {
        val Saver: Saver<CameraPositionState, *> = listSaver(
            save = { listOf(it.position.target.latitude, it.position.target.longitude, it.position.zoom, it.position.tilt, it.position.bearing) },
            restore = { CameraPositionState(CameraPosition(LatLng(it[0] as Double, it[1] as Double), it[2] as Float, it[3] as Float, it[4] as Float)) }
        )
    }
}