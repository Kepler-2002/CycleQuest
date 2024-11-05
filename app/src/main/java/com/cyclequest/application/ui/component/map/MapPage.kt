package com.cyclequest.application.ui.components.map

import android.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.amap.api.maps2d.AMap
import com.amap.api.maps2d.CameraUpdateFactory
import com.amap.api.maps2d.MapView
import com.amap.api.maps2d.model.CameraPosition
import com.amap.api.maps2d.model.LatLng
import com.amap.api.maps2d.model.MyLocationStyle
import com.amap.api.maps2d.model.PolylineOptions

@Composable
fun MapPage(
    modifier: Modifier = Modifier,
    cameraPositionState: CameraPositionState = rememberCameraPositionState(),
    onMapReady: (AMap) -> Unit
) {
    val mapView = rememberMapViewWithLifecycle()

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
fun rememberMapViewWithLifecycle(): MapView {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val mapView = remember { MapView(context) }

//    Map Control Object
    var mAMap: AMap = mapView.map

    var myLocationStyle: MyLocationStyle
    myLocationStyle =
        MyLocationStyle() //初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
    myLocationStyle.interval(1000) //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
    myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW)
    mAMap.setMyLocationStyle(myLocationStyle) //设置定位蓝点的Style
    //aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
    mAMap.setMyLocationEnabled(true) // 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

//    Test: polyline
    val latLngs: MutableList<LatLng> = ArrayList()
    latLngs.add(LatLng(39.999391, 116.135972))
    latLngs.add(LatLng(39.898323, 116.057694))
    latLngs.add(LatLng(39.900430, 116.265061))
    latLngs.add(LatLng(39.955192, 116.140092))
    mAMap.addPolyline(
        PolylineOptions().addAll
            (latLngs).width(10f).color(Color.argb(255, 1, 1, 1))
    )

//    TODO: 引入DiscoveryLayer与RoutingLayer，需要传入mAMap变量，控制自定义图层绘制

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> mapView.onCreate(null)
                Lifecycle.Event.ON_START -> mapView.onResume()
                Lifecycle.Event.ON_STOP -> mapView.onPause()
                Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    return mapView
}

@Composable
fun rememberCameraPositionState(
    key: String? = null,
    // 可以设置为读取上一次退出时保存的最后位置
    // 当前默认香港九零城区中心
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
