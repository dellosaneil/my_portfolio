package com.example.myportfolio.di

import android.content.Context
import androidx.room.Room
import com.example.myportfolio.utility.Constants.Companion.DATABASE_NAME
import com.example.myportfolio.room.MyDatabase
import com.example.myportfolio.room.dao.CertificateDao
import com.example.myportfolio.room.dao.ProjectDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): MyDatabase =
        Room.databaseBuilder(context, MyDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun provideCertificateDao(database: MyDatabase): CertificateDao = database.certificateDao()

    @Singleton
    @Provides
    fun provideProjectDao(database: MyDatabase) : ProjectDao = database.projectDao()


}