package com.example.myportfolio.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.myportfolio.data.ProjectData

@Dao
interface ProjectDao {

    @Insert
    suspend fun insertProject(project : ProjectData)

    @Query("SELECT * FROM projects_table")
    fun retrieveAllProjects() : LiveData<ProjectData>

    @Query("DELETE FROM projects_table")
    suspend fun deleteAllProjects()


}