package com.example.myportfolio.fragments.settings

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.myportfolio.R
import com.example.myportfolio.repository.CertificateRepository
import com.example.myportfolio.repository.ProjectsRepository
import com.example.myportfolio.utility.Constants.Companion.UPDATE
import com.example.myportfolio.utility.Constants.Companion.CHECK_UPDATE_COLLECTION
import com.example.myportfolio.utility.Constants.Companion.CLEAR_DATABASE_KEY
import com.example.myportfolio.utility.Constants.Companion.UPDATE_CERTIFICATION
import com.example.myportfolio.utility.Constants.Companion.UPDATE_PROJECT
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
    @Inject
    lateinit var projectRepository : ProjectsRepository

    override fun onPreferenceTreeClick(preference: Preference?): Boolean {
        if (preference?.key == CLEAR_DATABASE_KEY) {
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
            .setTitle(resources.getString(R.string.settings_cache_clear_title))
            .setMessage(resources.getString(R.string.settings_cache_clear_message))
            .setPositiveButton(resources.getString(R.string.settings_cache_positive_button)) { _, _ ->
                run {
                    deleteCertificates()
                }
            }
            .setNegativeButton(resources.getString(R.string.settings_cache_negative_button), null)
            .show()
    }

    private fun deleteCertificates() {
        lifecycleScope.launch(IO) {
            certificateRepository.deleteAllCertificates()
            projectRepository.deleteAllProjects()
            withContext(Main) {
                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.settings_cache_cleared),
                    Toast.LENGTH_LONG
                )
                    .show()
            }
            val changeState = mapOf(UPDATE to true)
            Firebase.firestore.collection(CHECK_UPDATE_COLLECTION).document(UPDATE_CERTIFICATION)
                .set(changeState)
            Firebase.firestore.collection(CHECK_UPDATE_COLLECTION).document(UPDATE_PROJECT).set(changeState)
        }
    }
}