package com.example.myportfolio.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myportfolio.data.CertificateData
import com.example.myportfolio.room.dao.CertificateDao

@Database(entities = [CertificateData::class], version = 1)
abstract class MyDatabase : RoomDatabase(){
    abstract fun certificateDao() : CertificateDao
}