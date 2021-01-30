package com.example.myportfolio.repository

import com.example.myportfolio.data.CertificateData
import com.example.myportfolio.room.dao.CertificateDao
import javax.inject.Inject

class CertificateRepository @Inject constructor(private val dao: CertificateDao) {

    suspend fun addCertificate(certificate: CertificateData) = dao.insertCertificate(certificate)
    fun retrieveCertificates() = dao.retrieveCertificateList()
    suspend fun deleteAllCertificates() = dao.deleteAllCertificates()

}