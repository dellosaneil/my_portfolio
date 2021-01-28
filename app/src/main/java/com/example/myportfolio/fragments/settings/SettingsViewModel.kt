package com.example.myportfolio.fragments.settings

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.myportfolio.repository.DataStoreRepository

class SettingsViewModel @ViewModelInject constructor(repository: DataStoreRepository) : ViewModel() {
    val isAutoUpdate = repository.checkAutoUpdate.asLiveData()
}