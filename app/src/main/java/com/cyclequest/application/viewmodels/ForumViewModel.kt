package com.cyclequest.application.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyclequest.data.local.entity.PostEntity
import com.cyclequest.domain.model.Post
import com.cyclequest.domain.repository.PostRepository
import com.cyclequest.data.local.preferences.UserPreferences
import com.cyclequest.domain.model.Achievement
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

import kotlinx.coroutines.flow.flowOf

import com.cyclequest.domain.repository.AchievementRepository
import kotlinx.coroutines.flow.collect

@HiltViewModel
class ForumViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val userPreferences: UserPreferences,
    private val achievementRepository: AchievementRepository
) : ViewModel() {

    private val _selectedTabIndex = MutableStateFlow(0) // 默认选中第一个标签
    val selectedTabIndex: StateFlow<Int> = _selectedTabIndex

    fun selectTab(index: Int) {
        _selectedTabIndex.value = index
    }

    private val _userPosts = MutableStateFlow<List<Post>>(emptyList())
    val userPosts: StateFlow<List<Post>> = _userPosts

    init {
        loadUserPosts()
        loadAchievements()
    }

    private fun loadUserPosts() {
        viewModelScope.launch {
            val userId = userPreferences.getUser()?.id
            userId?.let {
                postRepository.getUserPostsFlow(it).collect { posts ->
                    _userPosts.value = posts
                }
            }
        }
    }

    val userPostsFlow: Flow<List<Post>> = userPreferences.getUser()?.let { user ->
        postRepository.getUserPostsFlow(user.id)
    } ?: flowOf(emptyList())

    private val _achievements = MutableStateFlow<List<Achievement>>(emptyList())
    val achievements: StateFlow<List<Achievement>> = _achievements

    private fun loadAchievements() {
        viewModelScope.launch {
            achievementRepository.getAllAchievements().collect { achievementsList ->
                _achievements.value = achievementsList
            }
        }
    }
}