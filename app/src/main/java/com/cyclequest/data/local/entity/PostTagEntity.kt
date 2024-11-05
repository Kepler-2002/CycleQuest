package com.cyclequest.data.local.entity

import androidx.room.*


@Entity(
    tableName = "post_tags",
    foreignKeys = [
        ForeignKey(
            entity = PostEntity::class,
            parentColumns = ["postId"],
            childColumns = ["postId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("postId")]
)
data class PostTagEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val postId: String,
    val tag: String
) 