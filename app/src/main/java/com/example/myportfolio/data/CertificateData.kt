package com.example.myportfolio.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "certificate_table")
data class CertificateData(
    val companyName: String,
    val credentialId: String,
    val credentialUrl: String,
    @PrimaryKey(autoGenerate = true) val id : Int = 0
) : Parcelable
