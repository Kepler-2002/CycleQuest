package com.cyclequest.application.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

import androidx.compose.runtime.State

class CreatePostViewModel : ViewModel() {
    private val _postText = mutableStateOf("")
    val postText: State<String> = _postText

    fun updatePostText(newText: String) {
        _postText.value = newText
    }
}