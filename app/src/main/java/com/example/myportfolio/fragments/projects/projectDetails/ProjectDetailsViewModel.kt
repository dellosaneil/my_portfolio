package com.example.myportfolio.fragments.projects.projectDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myportfolio.data.ProjectData
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProjectDetailsViewModel(private val projectData: ProjectData) : ViewModel() {

    private val storage = Firebase.storage.reference
    private val maxBytes = 10L * 1024 * 1024

    private val _byteImages = MutableLiveData<MutableList<ByteArray>>(mutableListOf())

    fun byteImages(): LiveData<MutableList<ByteArray>> {
        return _byteImages
    }

/*Retrieve image resource from Firebase Storage. As a ByteArray*/
    fun getFromStorage() {
        viewModelScope.launch(Dispatchers.IO) {
            repeat(2) {
                val referenceLink =
                    if (it == 0) projectData.secondImageReference else projectData.thirdImageReference
                storage.child(referenceLink).getBytes(maxBytes).addOnSuccessListener { image ->
                    val currentList = byteImages().value
                    currentList?.add(image)
                    _byteImages.value = currentList
                }
            }
        }
    }
}