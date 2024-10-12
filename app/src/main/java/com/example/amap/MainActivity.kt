package com.example.amap

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.amap.api.maps2d.AMap
import com.amap.api.maps2d.MapView
import com.amap.api.maps2d.model.LatLng
import com.amap.api.maps2d.model.MarkerOptions
import com.amap.api.maps2d.model.PolylineOptions

class MainActivity : AppCompatActivity(), AMap.OnMapClickListener {

    private lateinit var mapView: MapView
    private lateinit var aMap: AMap
    private var startPoint: LatLng? = null
    private var endPoint: LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 更新隐私合规状态
//        MapsInitializer.updatePrivacyShow(this, true, true)
//        MapsInitializer.updatePrivacyAgree(this, true)

        setContentView(R.layout.activity_main)

        mapView = findViewById(R.id.map)
        mapView.onCreate(savedInstanceState)
        
        init()
    }

    private fun init() {
        if (!::aMap.isInitialized) {
            aMap = mapView.map
        }
        
        aMap.setOnMapClickListener(this)

        findViewById<Button>(R.id.btn_plan_route).setOnClickListener {
            planRoute()
        }

        findViewById<Button>(R.id.basicmap).setOnClickListener {
            aMap.mapType = AMap.MAP_TYPE_NORMAL
        }

        findViewById<Button>(R.id.rsmap).setOnClickListener {
            aMap.mapType = AMap.MAP_TYPE_SATELLITE
        }

        findViewById<RadioGroup>(R.id.check_language).setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radio_en -> aMap.setMapLanguage(AMap.ENGLISH)
                else -> aMap.setMapLanguage(AMap.CHINESE)
            }
        }

        checkPermissions()
    }

    private fun checkPermissions() {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val notGrantedPermissions = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }
        if (notGrantedPermissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, notGrantedPermissions.toTypedArray(), 1)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                // 所有权限都已授予
            } else {
                Toast.makeText(this, "需要所有权限才能正常使用应用", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onMapClick(latLng: LatLng) {
        if (startPoint == null) {
            startPoint = latLng
            addMarker(startPoint!!, "起点")
        } else if (endPoint == null) {
            endPoint = latLng
            addMarker(endPoint!!, "终点")
        }
    }

    private fun addMarker(latLng: LatLng, title: String) {
        aMap.addMarker(MarkerOptions().position(latLng).title(title))
    }

    private fun planRoute() {
        val start = startPoint
        val end = endPoint
        if (start == null || end == null) {
            Toast.makeText(this, "请先选择起点和终点", Toast.LENGTH_SHORT).show()
            return
        }

        // 简单地绘制一条直线来代替路径规划
        drawRouteLine(start, end)
    }

    private fun drawRouteLine(start: LatLng, end: LatLng) {
        val polylineOptions = PolylineOptions()
            .add(start, end)
            .width(10f)
            .color(ContextCompat.getColor(this, R.color.route_color))

        aMap.addPolyline(polylineOptions)
        
        // 调整地图视野以显示整个路线
        val boundsBuilder = com.amap.api.maps2d.model.LatLngBounds.Builder()
        boundsBuilder.include(start)
        boundsBuilder.include(end)
        val bounds = boundsBuilder.build()
        aMap.animateCamera(com.amap.api.maps2d.CameraUpdateFactory.newLatLngBounds(bounds, 100))
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }
}