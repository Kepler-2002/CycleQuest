package com.example.amap.application.ui.components.map

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.amap.api.maps.AMap
import com.amap.api.maps.MapView
import com.amap.api.maps.model.CameraPosition
import com.amap.api.maps.model.LatLng

@Composable
fun AMapComposable(
    modifier: Modifier = Modifier,
    cameraPositionState: CameraPositionState = rememberCameraPositionState()
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            MapView(context).apply {
                onCreate(null)
                getMapAsync { aMap ->
                    aMap.moveCamera(com.amap.api.maps.CameraUpdateFactory.newCameraPosition(cameraPositionState.position))
                }
            }
        },
        update = { mapView ->
            mapView.getMapAsync { aMap ->
                aMap.moveCamera(com.amap.api.maps.CameraUpdateFactory.newCameraPosition(cameraPositionState.position))
            }
        }
    )
}

@Composable
fun rememberCameraPositionState(
    key: String? = null,
    initialPosition: CameraPosition = CameraPosition(LatLng(39.90923, 116.397428), 10f, 0f, 0f)
): CameraPositionState = rememberSaveable(key = key, saver = CameraPositionState.Saver) {
    CameraPositionState(initialPosition)
}

class CameraPositionState(initialPosition: CameraPosition) {
    var position by mutableStateOf(initialPosition)
    
    companion object {
        val Saver: Saver<CameraPositionState, *> = listSaver(
            save = { listOf(it.position.target.latitude, it.position.target.longitude, it.position.zoom) },
            restore = { CameraPositionState(CameraPosition(LatLng(it[0], it[1]), it[2], 0f, 0f))) }
        )
    }
}
