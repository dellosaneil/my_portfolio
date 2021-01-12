package com.example.myportfolio.di

import android.content.Context
import androidx.room.Room
import com.example.myportfolio.Constants.Companion.DATABASE_NAME
import com.example.myportfolio.room.MyDatabase
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
    fun provideDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, MyDatabase::class.java, DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideCertificateDao(database : MyDatabase) = database.certificateDao()


}