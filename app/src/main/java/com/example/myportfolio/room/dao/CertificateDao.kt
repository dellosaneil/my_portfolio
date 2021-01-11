package com.example.myportfolio.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myportfolio.data.CertificateData

@Dao
interface CertificateDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateCertificateList(certificate : CertificateData)

    @Query("SELECT * FROM certificate_table")
    fun retrieveCertificateList() : LiveData<List<CertificateData>>


}