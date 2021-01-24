package com.example.myportfolio.firebase_data
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProjectFirebase(val name : String ="",
                           val description: String = "",
                           val language: String = "",
                           val github: String = "",
                           val firstImage: String = "",
                           val secondImage: String = "",
                           val thirdImage: String = "") : Parcelable
