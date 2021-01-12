package com.example.myportfolio.fragments.bottom_nav_fragments.certificate

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import com.example.myportfolio.data.CertificateData
import com.example.myportfolio.repository.CertificateRepository

class CertificateViewModel @ViewModelInject constructor(private val repository : CertificateRepository) {

    private var mCertificateList : LiveData<List<CertificateData>> = repository.retrieveCertificates()
    fun certificateList() = mCertificateList

    suspend fun updateCertificationList(certificate: CertificateData) : Boolean{
        val check = repository.checkCertificate(certificate.credentialId)
        return if(check == 0){
            repository.addCertificate(certificate)
            true
        }else{
            false
        }
    }
}