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
    suspend fun insertCertificate(certificate : CertificateData)

    @Query("SELECT * FROM certificate_table ORDER BY timeUploaded DESC")
    fun retrieveCertificateList() : LiveData<List<CertificateData>>

    @Query("SELECT COUNT(*) FROM certificate_table WHERE credentialId = :id")
    fun checkCertificate(id : String) : Int

    @Query("DELETE FROM certificate_table")
    suspend fun deleteAllCertificates()


}