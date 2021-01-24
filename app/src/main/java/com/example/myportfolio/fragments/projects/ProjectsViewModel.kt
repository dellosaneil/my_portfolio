package com.example.myportfolio.fragments.projects

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myportfolio.data.ProjectData
import com.example.myportfolio.firebase_data.ProjectFirebase
import com.example.myportfolio.repository.ProjectsRepository
import com.example.myportfolio.utility.Constants.Companion.CHECK_UPDATE_COLLECTION
import com.example.myportfolio.utility.Constants.Companion.PROJECT_COLLECTION
import com.example.myportfolio.utility.Constants.Companion.UPDATE
import com.example.myportfolio.utility.Constants.Companion.UPDATE_PROJECT
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await

private const val TAG = "ProjectsViewModel"

class ProjectsViewModel @ViewModelInject constructor(private val repository: ProjectsRepository) :
    ViewModel() {

    private val MAX_BYTES = 10L * 1024 * 1024;

    private var _projectList: LiveData<List<ProjectData>> = repository.retrieveProjects()
    fun projectList() = _projectList
    private var _currentState = MutableLiveData(false)

    fun currentState() = _currentState

    private val firestoreReference = Firebase.firestore
    private val storage = Firebase.storage.reference
    private var firebaseStatusListener : ListenerRegistration

    init {
        firebaseStatusListener = firestoreReference.collection(CHECK_UPDATE_COLLECTION).document(UPDATE_PROJECT)
            .addSnapshotListener { snapshot, _ ->
                run {
                    val currentState = snapshot?.get(UPDATE).let { it as Boolean }
                    _currentState.value = currentState
                }
            }
    }

    suspend fun updateProjectList() {
        Log.i(TAG, "updateProjectList: ")
        val projectReference = firestoreReference.collection(PROJECT_COLLECTION)
        val documentTitles = repository.retrieveProjectTitles()
        val documentList = mutableListOf<ProjectFirebase>()
        projectReference.get().addOnSuccessListener { documents ->
            run {
                for (document in documents) {
                    val doc = document.toObject(ProjectFirebase::class.java)
                    if (!documentTitles.contains(doc.name)) {
                        documentList.add(doc)
                    }
                }
            }
        }.await()
        processResourceReference(documentList)
    }

    private suspend fun processResourceReference(documentList: MutableList<ProjectFirebase>) {
        for (data in documentList) {
            var bytes: ByteArray? = null
            storage.child(data.firstImage).getBytes(MAX_BYTES)
                .addOnSuccessListener { image -> bytes = image }.await()
            val bitmap = convertByteArrayToBitmap(bytes)
            val tempProjectData = ProjectData(
                data.language,
                data.name,
                data.description,
                bitmap,
                data.secondImage,
                data.thirdImage,
                data.github
            )
            insertProject(tempProjectData)
        }
        changeProjectState()
    }


    private fun changeProjectState() {
        val temp = mapOf(UPDATE to false)
        firestoreReference.collection(CHECK_UPDATE_COLLECTION).document(UPDATE_PROJECT)
            .set(temp)
    }

    private fun convertByteArrayToBitmap(byte: ByteArray?): Bitmap? {
        return byte?.let { BitmapFactory.decodeByteArray(it, 0, it.size) }
    }

    private suspend fun insertProject(project: ProjectData) = repository.insertProject(project)

    fun removeListener(){
        firebaseStatusListener.remove()
    }



}