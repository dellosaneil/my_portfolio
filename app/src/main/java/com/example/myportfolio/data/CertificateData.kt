package com.example.myportfolio.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "certificate_table")
data class CertificateData(
    val certificateTitle : String = "",
    val companyName: String = "",
    val credentialId: String = "",
    val credentialUrl: String = "",
    val timeUploaded: Long = 0L
) : Parcelable {
    @IgnoredOnParcel
    @PrimaryKey(autoGenerate = true) var id : Int = 0
}
