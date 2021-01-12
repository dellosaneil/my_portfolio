package com.example.myportfolio.data

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "projects_table")
data class ProjectData(
    val projectTitle: String,
    val projectDescription: String,
    val projectImage: Bitmap,
    @PrimaryKey(autoGenerate = true) val projectId: Int = 0
)