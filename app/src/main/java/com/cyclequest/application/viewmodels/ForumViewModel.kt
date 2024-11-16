package com.cyclequest.application.viewmodels
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ForumViewModel : ViewModel() {
    private val _selectedTabIndex = MutableStateFlow(0)
    val selectedTabIndex: StateFlow<Int> = _selectedTabIndex

    fun selectTab(index: Int) {
        _selectedTabIndex.value = index
    }
}