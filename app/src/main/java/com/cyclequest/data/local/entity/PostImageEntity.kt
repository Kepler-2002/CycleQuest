package com.cyclequest.data.local.entity


import androidx.room.*


@Entity(
    tableName = "post_images",
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
data class PostImageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val postId: String,
    val imageUrl: String,
    val orderIndex: Int
) 