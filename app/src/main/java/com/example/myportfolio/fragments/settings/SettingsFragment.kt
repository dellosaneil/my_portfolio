package com.example.myportfolio.fragments.settings

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.myportfolio.R
import com.example.myportfolio.repository.CertificateRepository
import com.example.myportfolio.room.dao.CertificateDao
import com.example.myportfolio.utility.Constants
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {

    @Inject
    lateinit var certificateRepository: CertificateRepository

    override fun onPreferenceTreeClick(preference: Preference?): Boolean {
        if (preference?.key == "clear_data") {
            alertDialog()
            return true
        }
        return false
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_main, rootKey)
    }

    private fun alertDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setCancelable(false)
            .setTitle("Clear all data")
            .setMessage("This will delete all the saved contents in your device.")
            .setPositiveButton("Clear Data") { _, _ ->
                run {
                    lifecycleScope.launch(IO) {
                        certificateRepository.deleteAllCertificates()
                        withContext(Main) {
                            Toast.makeText(requireContext(), "Cleared Cache", Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }


}