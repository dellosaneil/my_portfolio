package com.example.myportfolio.fragments.projects

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myportfolio.data.ProjectData
import com.example.myportfolio.repository.ProjectsRepository
import com.example.myportfolio.utility.Constants.Companion.CHECK_UPDATE_COLLECTION
import com.example.myportfolio.utility.Constants.Companion.PROJECT_COLLECTION
import com.example.myportfolio.utility.Constants.Companion.UPDATE
import com.example.myportfolio.utility.Constants.Companion.UPDATE_PROJECT
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await


class ProjectsViewModel @ViewModelInject constructor(private val repository: ProjectsRepository) :
    ViewModel() {


    private var _projectList: LiveData<List<ProjectData>> = repository.retrieveProjects()
    fun projectList() = _projectList
    private var _currentState = MutableLiveData(false)

    fun currentState() = _currentState

    private val firestoreReference = Firebase.firestore
    private var firebaseStatusListener: ListenerRegistration? = null

    private val TAG = "ProjectsViewModel"
    /*Checks current status of update field*/
    fun attachCurrentStatusListener() {
        firebaseStatusListener =
            firestoreReference.collection(CHECK_UPDATE_COLLECTION).document(UPDATE_PROJECT)
                .addSnapshotListener { snapshot, _ ->
                    run {
                        Log.i(TAG, "attachCurrentStatusListener: ")
                        val currentState = snapshot?.get(UPDATE).let { it as Boolean }
                        _currentState.value = currentState
                    }

                }
    }
    
    suspend fun updateProjectList() {
        val projectReference = firestoreReference.collection(PROJECT_COLLECTION)
        val documentTitles = repository.retrieveProjectTitles()
        val documentList = mutableListOf<ProjectData>()
        projectReference.get().addOnSuccessListener { documents ->
            run {
                for (document in documents) {
                    val doc = document.toObject(ProjectData::class.java)
                    if (!documentTitles.contains(doc.projectTitle)) {
                        documentList.add(doc)
                    }
                }
            }
        }.await()
        processResourceReference(documentList)
    }

    private suspend fun processResourceReference(documentList: MutableList<ProjectData>) {
        for (data in documentList) {
            insertProject(data)
        }
        changeProjectState()
    }

    /*when updating is finished, check the status of update*/
    private fun changeProjectState() {
        val temp = mapOf(UPDATE to false)
        firestoreReference.collection(CHECK_UPDATE_COLLECTION).document(UPDATE_PROJECT)
            .set(temp)
    }

    private suspend fun insertProject(project: ProjectData) = repository.insertProject(project)

    fun removeListener() {
        firebaseStatusListener!!.remove()
        firebaseStatusListener = null
    }
}