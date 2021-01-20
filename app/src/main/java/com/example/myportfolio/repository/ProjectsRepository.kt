package com.example.myportfolio.repository

import com.example.myportfolio.data.ProjectData
import com.example.myportfolio.room.dao.ProjectDao
import javax.inject.Inject

class ProjectsRepository @Inject constructor(private val dao: ProjectDao) {

    suspend fun insertProject(project: ProjectData) = dao.insertProject(project)
    fun retrieveProjects() = dao.retrieveProjects()
    suspend fun deleteAllProjects() = dao.deleteAllProjects()

}