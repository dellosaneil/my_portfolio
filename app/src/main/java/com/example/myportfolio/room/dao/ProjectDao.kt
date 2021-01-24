package com.example.myportfolio.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myportfolio.data.ProjectData

@Dao
interface ProjectDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project : ProjectData)

    @Query("SELECT * FROM projects_table")
    fun retrieveProjects() : LiveData<List<ProjectData>>

    @Query("DELETE FROM projects_table")
    suspend fun deleteAllProjects()

    @Query("SELECT projectTitle FROM projects_table")
    fun retrieveProjectTitles() : List<String>

}