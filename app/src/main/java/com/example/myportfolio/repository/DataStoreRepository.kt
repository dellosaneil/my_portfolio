package com.example.myportfolio.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.createDataStore
import com.example.myportfolio.utility.Constants.Companion.AUTO_UPDATE
import com.example.myportfolio.utility.Constants.Companion.SETTINGS_PREFERENCE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException



class DataStoreRepository(context: Context) {

    private val dataStore: DataStore<Preferences> = context.createDataStore(SETTINGS_PREFERENCE)

    val checkAutoUpdate : Flow<Boolean> = dataStore.data
        .catch { exception ->
            if(exception is IOException){
                emit(emptyPreferences())
            }else{
                throw exception
            }
        }.map{ preferences ->
            val autoUpdatePreferenceKey = booleanPreferencesKey(AUTO_UPDATE)
            val autoUpdate  = preferences[autoUpdatePreferenceKey] ?: false
            autoUpdate
        }

    suspend fun saveToDataStore(newValue: Boolean, key: String) {
        val preferenceKey = booleanPreferencesKey(key)
        dataStore.edit {
            it[preferenceKey] = newValue
        }
    }
}