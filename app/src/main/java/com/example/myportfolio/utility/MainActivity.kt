package com.example.myportfolio.utility

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.myportfolio.R
import com.example.myportfolio.databinding.ActivityMainBinding
import com.example.myportfolio.utility.Constants.Companion.SETTINGS_PREFERENCE
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navController = findNavController(R.id.navHostFragment)
        setUpBottomNavigation()
        destinationListener()
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