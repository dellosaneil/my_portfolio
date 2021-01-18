package com.example.myportfolio.fragments.settings

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.myportfolio.R
import com.example.myportfolio.data.CertificationUpdate
import com.example.myportfolio.repository.CertificateRepository
import com.example.myportfolio.utility.Constants.Companion.CERTIFICATE_COLLECTION
import com.example.myportfolio.utility.Constants.Companion.CERTIFICATE_PATH_UPDATE
import com.example.myportfolio.utility.Constants.Companion.CLEAR_DATABASE_KEY
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
            withContext(Main) {
                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.settings_cache_cleared),
                    Toast.LENGTH_LONG
                )
                    .show()
            }
            val temp = CertificationUpdate(true)
            Firebase.firestore.collection(CERTIFICATE_COLLECTION).document(CERTIFICATE_PATH_UPDATE)
                .set(temp)
        }
    }
}