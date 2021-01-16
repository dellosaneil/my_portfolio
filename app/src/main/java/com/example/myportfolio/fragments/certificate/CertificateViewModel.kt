package com.example.myportfolio.fragments.certificate

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.myportfolio.data.CertificateData
import com.example.myportfolio.repository.CertificateRepository
import com.example.myportfolio.utility.Constants.Companion.CERTIFICATE_COLLECTION
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
        val roomSize = certificateList().value?.size
        val collectionDocuments = certificateReference.get()
        collectionDocuments.addOnSuccessListener { documents ->
            run {
                if (roomSize == documents.size()) {
                    Log.i(TAG, "is Equal")
                }else{
                    Log.i(TAG, "needs Update")
                }
            }
        }
    }

}