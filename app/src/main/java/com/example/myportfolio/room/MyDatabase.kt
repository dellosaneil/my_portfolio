package com.example.myportfolio.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.myportfolio.data.CertificateData
import com.example.myportfolio.data.ProjectData
import com.example.myportfolio.room.dao.CertificateDao
import com.example.myportfolio.room.dao.ProjectDao


@Database(entities = [CertificateData::class, ProjectData::class], version = 4, exportSchema = false)
@TypeConverters(Converters::class)
abstract class MyDatabase : RoomDatabase() {
    abstract fun certificateDao(): CertificateDao
    abstract fun projectDao(): ProjectDao
}