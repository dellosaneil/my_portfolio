package com.example.myportfolio.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize


@Entity(tableName = "projects_table")
@Parcelize
data class ProjectData(
    val projectLanguage: String = "",
    val projectTitle: String = "",
    val projectDescription: String = "",
    val firstImageReference : String = "",
    val secondImageReference: String = "",
    val thirdImageReference: String = "",
    val gitHubRepository: String = "",
    val timeUploaded: Long = 0L

) : Parcelable {
    @IgnoredOnParcel
    @PrimaryKey(autoGenerate = true)
    var projectId: Int = 0
}
