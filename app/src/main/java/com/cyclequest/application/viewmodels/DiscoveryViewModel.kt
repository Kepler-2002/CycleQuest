package com.cyclequest.application.viewmodels

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amap.api.maps2d.model.LatLng
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.geocoder.GeocodeResult
import com.amap.api.services.geocoder.GeocodeSearch
import com.amap.api.services.geocoder.RegeocodeQuery
import com.amap.api.services.geocoder.RegeocodeResult
import com.cyclequest.domain.repository.AdministrativeDivisionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class ProvinceState {
    DEFAULT, EXPLORED
}

@HiltViewModel
class DiscoveryViewModel @Inject constructor(
    private val administrativeDivisionRepository: AdministrativeDivisionRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _provinceStates = mutableStateMapOf<String, ProvinceState>()
    val provinceStates: Map<String, ProvinceState> get() = _provinceStates

    private val _provinceBoundaries = mutableStateMapOf<String, List<LatLng>>()
    val provinceBoundaries: Map<String, List<LatLng>> get() = _provinceBoundaries

    private val geocodeSearch by lazy {
        GeocodeSearch(context)
    }

    fun loadBoundary(divisionCode: String) {
        viewModelScope.launch {
            administrativeDivisionRepository.getAdministrativeDivisionBoundary(divisionCode)
                .onSuccess { division ->
                    _provinceBoundaries[divisionCode] = division.boundaryPoints
                    _provinceStates.putIfAbsent(divisionCode, ProvinceState.DEFAULT)
                }
                .onFailure {
                    _provinceBoundaries[divisionCode] = emptyList()
                }
        }
    }

    fun setProvinceState(provinceCode: String, state: ProvinceState) {
        _provinceStates[provinceCode] = state
    }

    // 地理位置信息监听更新回调函数
    fun locationUpdateCallback(location: LatLng) {
        Log.d("DiscoveryViewModel", "位置已更新: lat=${location.latitude}, lng=${location.longitude}")
        
        // 获取行政区划代码
        val latLonPoint = LatLonPoint(location.latitude, location.longitude)
        val regeocodeQuery = RegeocodeQuery(latLonPoint, 200f, GeocodeSearch.AMAP)
        
        geocodeSearch.getFromLocationAsyn(regeocodeQuery)
        geocodeSearch.setOnGeocodeSearchListener(object : GeocodeSearch.OnGeocodeSearchListener {
            override fun onRegeocodeSearched(result: RegeocodeResult?, rCode: Int) {
                if (rCode == 1000) {
                    result?.regeocodeAddress?.let { address ->
                        val adCode = address.adCode
                        Log.d("DiscoveryViewModel", "获取到行政区划代码: $adCode")
                        setProvinceState(adCode, ProvinceState.EXPLORED)
                    }
                } else {
                    Log.e("DiscoveryViewModel", "获取行政区划失败: $rCode")
                }
            }

            override fun onGeocodeSearched(p0: GeocodeResult?, p1: Int) {
                // 正向地理编码回调,这里不需要实现
            }
        })
    }
}