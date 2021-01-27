package com.example.myportfolio.utility

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.createDataStore
import androidx.lifecycle.asLiveData
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.myportfolio.R
import com.example.myportfolio.databinding.ActivityMainBinding
import com.example.myportfolio.utility.Constants.Companion.DARK_THEME
import com.example.myportfolio.utility.Constants.Companion.SETTINGS_PREFERENCE
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    private val TAG = "MainActivity"

    val testKey = booleanPreferencesKey(DARK_THEME)
    val datastore: DataStore<Preferences> = this.createDataStore(SETTINGS_PREFERENCE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navController = findNavController(R.id.navHostFragment)
        setUpBottomNavigation()
        destinationListener()
        testDataStore()

        val testCollect = testDataStore().asLiveData()
        testCollect.observe(this){observe -> Log.i(TAG, "onCreate: $observe")}

    }

    private fun testDataStore(): Flow<Boolean> =
        datastore.data
            .map { preferences ->
                val borkTheme = preferences[testKey] ?: false
                borkTheme
            }


    private fun destinationListener() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            run {
                when (destination.id) {
                    R.id.projectDetails, R.id.certificateWebView, R.id.projectWebView -> binding.bottomNavigationView.visibility =
                        View.GONE
                    else -> binding.bottomNavigationView.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setUpBottomNavigation() {
        binding.bottomNavigationView.setupWithNavController(navController)
    }


}