package com.example.myportfolio.fragments.certificate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myportfolio.data.CertificateData
import com.example.myportfolio.repository.CertificateRepository
import com.example.myportfolio.utility.Constants.Companion.CERTIFICATE_COLLECTION
import com.example.myportfolio.utility.Constants.Companion.CHECK_UPDATE_COLLECTION
import com.example.myportfolio.utility.Constants.Companion.MAIN_COLLECTION
import com.example.myportfolio.utility.Constants.Companion.MAIN_DOCUMENT
import com.example.myportfolio.utility.Constants.Companion.UPDATE
import com.example.myportfolio.utility.Constants.Companion.UPDATE_CERTIFICATION
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class CertificateViewModel @Inject constructor(private val repository: CertificateRepository) :
    ViewModel() {

    private val firestoreReference = Firebase.firestore.collection(MAIN_COLLECTION).document(
        MAIN_DOCUMENT
    )

    private var _needUpdate = MutableLiveData(false)
    private var mCertificateList: LiveData<List<CertificateData>> =
        repository.retrieveCertificates()
    private lateinit var updateCertificateUpdate: ListenerRegistration

    fun needUpdate() = _needUpdate
    fun certificateList() = mCertificateList

    /*
    * Function gets called when FAB gets clicked. It will update the contents of the ROOM database
    * */
    suspend fun updateCertificationList() {
        val documentCertificateData = mutableListOf<CertificateData>()
        val allDocuments = firestoreReference.collection(CERTIFICATE_COLLECTION).get()
        allDocuments.addOnSuccessListener {
            for (document in it.documents) {
                val temp = document.toObject<CertificateData>()
                temp?.let { documentCertificateData.add(temp) }
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
            val changeState = mapOf(UPDATE to false)
            firestoreReference.collection(CHECK_UPDATE_COLLECTION).document(UPDATE_CERTIFICATION)
                .set(changeState)
        }
    }

    private val TAG = "CertificateViewModel"

    /*Checks whether the application is up to date.*/
    fun attachCurrentStateListener() {
        updateCertificateUpdate =
            firestoreReference.collection(CHECK_UPDATE_COLLECTION).document(UPDATE_CERTIFICATION)
                .addSnapshotListener { snapShot, exception ->
                    run {
                        if (exception != null) {
                            _needUpdate.value = false
                        } else {
                            val currentState = snapShot?.get(UPDATE).let { it as Boolean }
                            _needUpdate.value = currentState
                        }
                    }
                }
    }

    fun removeListeners() {
        onCleared()
        updateCertificateUpdate.remove()
    }
}