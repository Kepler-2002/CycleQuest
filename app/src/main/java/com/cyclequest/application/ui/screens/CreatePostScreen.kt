package com.cyclequest.application.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextOverflow  // Add this import
import com.cyclequest.application.ui.component.forum.AwardItem
import com.cyclequest.application.ui.component.forum.AwardEventCard
import com.cyclequest.application.ui.component.forum.PostItem
import com.cyclequest.application.viewmodels.CreatePostViewModel
import com.cyclequest.application.viewmodels.ForumViewModel

import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.ui.unit.sp

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.MaterialTheme


import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Public
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostScreen(
    onNavigateBack: () -> Unit,
    viewModel: CreatePostViewModel = viewModel()
) {
    val darkColorScheme = darkColorScheme()

    Surface(
        color = Color.Black,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                TextButton(
                    onClick = { /* TODO: Implement post action */ },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color.White.copy(alpha = 0.6f)
                    )
                ) {
                    Text("发表", fontSize = 16.sp)
                }
            }

            TextField(
                value = TextFieldValue(viewModel.postText.value),
                onValueChange = { newText: TextFieldValue -> viewModel.updatePostText(newText.text) },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                label = { Text("这一刻的想法...") },
                textStyle = LocalTextStyle.current.copy(color = Color.White),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.White,
                    focusedLabelColor = Color.Gray,
                    unfocusedLabelColor = Color.Gray
                ),
                singleLine = false,
                shape = MaterialTheme.shapes.small
            )

            // Image Selection Box
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.DarkGray.copy(alpha = 0.3f))
                    .clickable { /* TODO: Implement image selection */ },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add Image",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }

            // Bottom Options
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                ListItem(
                    leadingContent = {
                        Icon(
                            Icons.Default.LocationOn,
                            contentDescription = "Location",
                            tint = Color.White
                        )
                    },
                    headlineContent = { Text("所在位置", color = Color.White) },
                    trailingContent = {
                        Icon(
                            Icons.Default.ChevronRight,
                            contentDescription = "Select",
                            tint = Color.Gray
                        )
                    },
                    modifier = Modifier.clickable { /* TODO: Implement location selection */ }
                )

                ListItem(
                    leadingContent = {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "Mention",
                            tint = Color.White
                        )
                    },
                    headlineContent = { Text("提醒谁看", color = Color.White) },
                    trailingContent = {
                        Icon(
                            Icons.Default.ChevronRight,
                            contentDescription = "Select",
                            tint = Color.Gray
                        )
                    },
                    modifier = Modifier.clickable { /* TODO: Implement mention */ }
                )

                ListItem(
                    leadingContent = {
                        Icon(
                            Icons.Default.Public,
                            contentDescription = "Visibility",
                            tint = Color.White
                        )
                    },
                    headlineContent = { Text("谁可以看", color = Color.White) },
                    trailingContent = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("公开", color = Color.Gray)
                            Icon(
                                Icons.Default.ChevronRight,
                                contentDescription = "Select",
                                tint = Color.Gray
                            )
                        }
                    },
                    modifier = Modifier.clickable { /* TODO: Implement visibility settings */ }
                )
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun CreatePostScreenPreview() {
    CreatePostScreen(onNavigateBack = {})
}