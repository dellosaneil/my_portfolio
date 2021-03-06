package com.example.myportfolio.di

import android.content.Context
import androidx.room.Room
import com.example.myportfolio.repository.DataStoreRepository
import com.example.myportfolio.room.MyDatabase
import com.example.myportfolio.room.dao.CertificateDao
import com.example.myportfolio.room.dao.ProjectDao
import com.example.myportfolio.utility.Constants.Companion.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

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

    @Singleton
    @Provides
    fun provideDataStoreRepository(@ApplicationContext context : Context) : DataStoreRepository = DataStoreRepository(context)

}