package com.example.myportfolio.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize


@Entity(tableName = "projects_table")
@Parcelize
data class ProjectData(
    val projectTitle: String,
    val projectDescription: String,
//    val projectImage: Bitmap,
    val gitHubRepository: String = "",
) : Parcelable {
    @IgnoredOnParcel
    @PrimaryKey(autoGenerate = true)
    var projectId: Int = 0
}