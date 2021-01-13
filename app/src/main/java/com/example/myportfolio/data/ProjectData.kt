package com.example.myportfolio.data

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "projects_table")
data class ProjectData(
    val projectTitle: String,
    val projectDescription: String,
//    val projectImage: Bitmap,
    val gitHubRepository : String = "",
){
    @PrimaryKey(autoGenerate = true) var projectId: Int = 0
}