package com.example.myportfolio.fragments.certificate

import android.widget.Toast
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myportfolio.data.CertificateData
import com.example.myportfolio.data.CertificationUpdate
import com.example.myportfolio.repository.CertificateRepository
import com.example.myportfolio.utility.Constants.Companion.CERTIFICATE_COLLECTION
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class CertificateViewModel @ViewModelInject constructor(private val repository: CertificateRepository) :
    ViewModel() {

    private val certificateReference = Firebase.firestore.collection(CERTIFICATE_COLLECTION)

    private var _needUpdate = MutableLiveData(false)

    fun needUpdate() = _needUpdate

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
    private val TAG = "CertificateViewModel"

    suspend fun deleteCertificates() = repository.deleteAllCertificates()

    fun checkUpdate(){
        certificateReference.document("update").addSnapshotListener{snapShot , exception ->
            run {
                if (exception != null) {
                    _needUpdate.value = false
                }else{
                    val temp = snapShot?.toObject(CertificationUpdate::class.java)
                    _needUpdate.value = temp?.needUpdate
                }
            }
        }
    }
}