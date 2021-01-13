package com.example.myportfolio.fragments.certificate

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.myportfolio.data.CertificateData
import com.example.myportfolio.repository.CertificateRepository

class CertificateViewModel @ViewModelInject constructor(private val repository: CertificateRepository) :
    ViewModel() {

    private var mCertificateList: LiveData<List<CertificateData>> =
        repository.retrieveCertificates()

    fun certificateList() = mCertificateList

    suspend fun updateCertificationList(certificate: CertificateData): Boolean {
        val check = repository.checkCertificate(certificate.credentialId)
        return if (check == 0) {
            repository.addCertificate(certificate)
            true
        } else {
            false
        }
    }

    suspend fun deleteCertificates() = repository.deleteAllCertificates()

}