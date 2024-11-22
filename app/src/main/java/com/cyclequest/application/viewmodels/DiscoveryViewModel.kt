package com.cyclequest.application.viewmodels

import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amap.api.maps2d.model.LatLng
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.geocoder.GeocodeResult
import com.amap.api.services.geocoder.GeocodeSearch
import com.amap.api.services.geocoder.RegeocodeQuery
import com.amap.api.services.geocoder.RegeocodeResult
import com.cyclequest.data.local.entity.UserExploredRegion
import com.cyclequest.domain.model.Achievement
import com.cyclequest.domain.repository.AdministrativeDivisionRepository
import com.cyclequest.domain.repository.UserExploredRegionRepository
import com.cyclequest.domain.usecase.RegionExplorerAchievementDetector
import com.cyclequest.data.local.preferences.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

enum class ProvinceState {
    DEFAULT, EXPLORED
}

@HiltViewModel
class DiscoveryViewModel @Inject constructor(
    private val administrativeDivisionRepository: AdministrativeDivisionRepository,
    private val userExploredRegionRepository: UserExploredRegionRepository,
    private val regionExplorerAchievementDetector: RegionExplorerAchievementDetector,
    private val userPreferences: UserPreferences,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _provinceStates = mutableStateMapOf<String, ProvinceState>()
    val provinceStates = _provinceStates as Map<String, ProvinceState>

    private val _provinceBoundaries = mutableStateMapOf<String, List<LatLng>>()
    val provinceBoundaries: Map<String, List<LatLng>> get() = _provinceBoundaries

    private val geocodeSearch by lazy {
        GeocodeSearch(context)
    }


    // 定时调用Detector

    private val _isSimulationMode = mutableStateOf(false)
    val isSimulationMode: State<Boolean> = _isSimulationMode

    private val _showAchievementDialog = mutableStateOf<Achievement?>(null)
    val showAchievementDialog: State<Achievement?> = _showAchievementDialog

    private var achievementCheckJob: Job? = null
    private val currentUserId: String
        get() = userPreferences.getUser()?.id ?: throw IllegalStateException("用户未登录")

    private val _navigationEvent = MutableSharedFlow<String>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    init {
        startAchievementCheck()
        // 确保用户已登录
        viewModelScope.launch {
            if (userPreferences.getUser() == null) {
                Log.e("DiscoveryViewModel", "用户未登录")
                // 这里可以添加未登录状态的处理逻辑
            }
        }
    }

    private fun startAchievementCheck() {
        achievementCheckJob?.cancel()
        achievementCheckJob = viewModelScope.launch {
            while (true) {
                delay(1000) // 每秒检查一次
                checkAchievements()
            }
        }
    }

    private suspend fun checkAchievements() {
        try {
            val newAchievements = regionExplorerAchievementDetector.checkAchievements(currentUserId)
            newAchievements.firstOrNull()?.let { achievement ->
                _showAchievementDialog.value = achievement
            }
        } catch (e: Exception) {
            Log.e("DiscoveryViewModel", "检查成就时出错", e)
        }
    }

    fun dismissAchievementDialog() {
        _showAchievementDialog.value = null
    }

    fun shareAchievement() {
        viewModelScope.launch {
            _showAchievementDialog.value?.let { achievement ->
                _navigationEvent.emit("CreatePostScreen")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        achievementCheckJob?.cancel()
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
                        
                        // 记录探索数据
                        viewModelScope.launch {
                            try {
                                val existingRegion = userExploredRegionRepository.getUserExploredRegions(currentUserId)
                                    .find { it.regionCode == adCode }
                                
                                val currentTime = System.currentTimeMillis()
                                
                                if (existingRegion == null) {
                                    Log.d("DiscoveryViewModel", "首次探索新区域: $adCode")
                                    val newRegion = UserExploredRegion(
                                        userId = currentUserId,
                                        regionCode = adCode,
                                        firstExploredTime = currentTime,
                                        lastExploredTime = currentTime,
                                        exploredCount = 1
                                    )
                                    userExploredRegionRepository.addUserExploredRegion(newRegion)
                                } else {
//                                    Log.d("DiscoveryViewModel", "再次探索区域: $adCode, 当前次数=${existingRegion.exploredCount}")
//                                    userExploredRegionRepository.updateUserExploredRegion(
//                                        existingRegion.copy(
//                                            lastExploredTime = currentTime,
//                                            exploredCount = existingRegion.exploredCount + 1
//                                        )
//                                    )
                                }
                            } catch (e: Exception) {
                                Log.e("DiscoveryViewModel", "处理区域探索时出错", e)
                            }
                        }
                    }
                } else {
                    Log.e("DiscoveryViewModel", "获取行政区划失败: $rCode")
                }
            }

            override fun onGeocodeSearched(p0: GeocodeResult?, p1: Int) {}
        })
    }

    fun toggleSimulation() {
        _isSimulationMode.value = !_isSimulationMode.value
    }
}