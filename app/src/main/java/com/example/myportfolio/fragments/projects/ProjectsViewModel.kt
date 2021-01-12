package com.example.myportfolio.fragments.projects

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.myportfolio.data.ProjectData
import com.example.myportfolio.repository.ProjectsRepository

class ProjectsViewModel @ViewModelInject constructor(private val repository: ProjectsRepository) :
    ViewModel() {

    private var mProjectList: LiveData<ProjectData> = repository.retrieveAllProjects()

    fun projectList() = mProjectList

    suspend fun clearProjectTable() = repository.deleteAllProjects()

    suspend fun insertProject(project: ProjectData) = repository.insertProject(project)

}