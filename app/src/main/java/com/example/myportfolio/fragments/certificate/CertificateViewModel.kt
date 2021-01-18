package com.example.myportfolio.fragments.certificate

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myportfolio.data.CertificateData
import com.example.myportfolio.data.CertificationUpdate
import com.example.myportfolio.repository.CertificateRepository
import com.example.myportfolio.utility.Constants.Companion.CERTIFICATE_COLLECTION
import com.example.myportfolio.utility.Constants.Companion.CERTIFICATE_PATH_UPDATE
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class CertificateViewModel @ViewModelInject constructor(private val repository: CertificateRepository) :
    ViewModel() {

    private val certificateReference = Firebase.firestore.collection(CERTIFICATE_COLLECTION)

    private var _needUpdate = MutableLiveData(false)
    private var mCertificateList: LiveData<List<CertificateData>> =
        repository.retrieveCertificates()
    private lateinit var updateCertificateUpdate : ListenerRegistration

    fun needUpdate() = _needUpdate
    fun certificateList() = mCertificateList

    /*
    * Function gets called when FAB gets clicked. It will update the contents of the ROOM database
    * */
    suspend fun updateCertificationList() {
        val documentCertificateData = mutableListOf<CertificateData>()
        val allDocuments = certificateReference.get()
        allDocuments.addOnSuccessListener {
            for (document in it.documents) {
                if(document.id != "update"){
                    val temp = document.toObject<CertificateData>()
                    temp?.let { documentCertificateData.add(temp) }
                }
            }
        }.await()
        updateRoomDatabase(documentCertificateData)
    }

    private suspend fun updateRoomDatabase(allDocuments: MutableList<CertificateData>) {
        val temp = certificateList().value
        for (document in allDocuments) {
            temp?.let {
                if (!it.contains(document)) {
                    repository.addCertificate(document)
                }
            }
            val update = CertificationUpdate(false)
            certificateReference.document(CERTIFICATE_PATH_UPDATE).set(update)
        }
    }

    /*Checks whether the application is up to date.*/
    fun checkUpdate() {
        updateCertificateUpdate = certificateReference.document(CERTIFICATE_PATH_UPDATE).addSnapshotListener { snapShot, exception ->
            run {
                if (exception != null) {
                    _needUpdate.value = false
                } else {
                    val temp = snapShot?.toObject(CertificationUpdate::class.java)
                    _needUpdate.value = temp?.needUpdate
                }
            }
        }
    }

    fun removeListeners() {
        onCleared()
        updateCertificateUpdate.remove()
    }
}