package com.example.demotest.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "imagesList")
data class Images(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val author: String,
    val width: Int,
    val height: Int,
    val url: String,
    val download_url: String
)
