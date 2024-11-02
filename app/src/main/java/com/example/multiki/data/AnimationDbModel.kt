package com.example.multiki.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "anims")
data class AnimationDbModel(
    @PrimaryKey
    val createAt: Long,
    val fileName: String
)