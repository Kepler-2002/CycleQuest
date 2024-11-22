package com.cyclequest.application.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyclequest.domain.model.Post
import com.cyclequest.data.local.entity.PostEntity
import com.cyclequest.domain.repository.PostRepository
import com.cyclequest.data.local.preferences.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val postRepository: PostRepository,
    internal val userPreferences: UserPreferences
) : ViewModel() {
    var postText by mutableStateOf("") // Define postText as a mutable state

    fun updatePostText(newText: String) {
        postText = newText // Update the postText state
    }

    fun savePost(post: Post) {
        viewModelScope.launch {
            val user = userPreferences.getUser()
            user?.let {
                val newPostId = postRepository.getNextPostId(it.id)
                val postEntity = PostEntity(postId = newPostId.toString(), content = post.content, userId = it.id)
                postRepository.savePost(postEntity)
            } ?: run {
                // 处理 user 为 null 的情况，例如记录错误或显示消息
            }
        }
    }

    fun getCurrentUser() = userPreferences.getUser()
}