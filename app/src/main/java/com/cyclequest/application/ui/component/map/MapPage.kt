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

@Composable
fun MapPage(
    modifier: Modifier = Modifier,
    cameraPositionState: CameraPositionState = rememberCameraPositionState(),
    onMapReady: (AMap) -> Unit,
    onLocationChanged: (LatLng) -> Unit,
    isSimulationMode: Boolean,
    locationService: LocationService
) {
    val mapView = rememberMapViewWithLifecycle(onLocationChanged, isSimulationMode, locationService)
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
    isSimulationMode: Boolean,
    locationService: LocationService
): MapView {
    val context = LocalContext.current
    val mapView = remember { MapView(context) }
    val mAMap: AMap = mapView.map
    val scope = rememberCoroutineScope()
    
    var lastLocation by remember { mutableStateOf<LatLng?>(null) }
    var simulationJob by remember { mutableStateOf<Job?>(null) }
    var locationMarker by remember { mutableStateOf<Marker?>(null) }

    // 初始化定位和标记
    LaunchedEffect(Unit) {
        locationService.getCurrentLocation { location ->
            location?.let {
                val initialLatLng = LatLng(it.latitude, it.longitude)
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
    DisposableEffect(isSimulationMode) {
        simulationJob?.cancel()

        if (!isSimulationMode) {
            // 真实定位模式
            locationService.setLocationInterval(2000) // 2秒更新一次
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
        } else {
            // 模拟定位模式
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
                    mAMap.animateCamera(CameraUpdateFactory.newLatLng(simulatedLocation))
                    onLocationChanged(simulatedLocation)
                    delay(1000)
                }
            }
        }

        onDispose {
            simulationJob?.cancel()
            simulationJob = null
            locationService.stopLocationUpdates()
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