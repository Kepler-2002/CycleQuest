package com.cyclequest.application.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amap.api.maps2d.model.LatLng
import com.cyclequest.domain.repository.AdministrativeDivisionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DiscoveryViewModel @Inject constructor(
    private val administrativeDivisionRepository: AdministrativeDivisionRepository
) : ViewModel() {

    private val _boundaryPoints = MutableStateFlow<List<LatLng>>(emptyList())
    val boundaryPoints: StateFlow<List<LatLng>> = _boundaryPoints.asStateFlow()

    fun loadBoundary(divisionCode: String) {
        viewModelScope.launch {
            administrativeDivisionRepository.getAdministrativeDivisionBoundary(divisionCode)
                .onSuccess { division ->
                    _boundaryPoints.value = division.boundaryPoints
                }
                .onFailure {
                    _boundaryPoints.value = emptyList()
                }
        }
    }
} 