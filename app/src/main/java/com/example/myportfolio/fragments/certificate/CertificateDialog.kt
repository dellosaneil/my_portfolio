package com.example.myportfolio.fragments.certificate

import android.app.Activity
import android.view.LayoutInflater
import com.bumptech.glide.Glide
import com.example.myportfolio.R
import com.example.myportfolio.data.CertificateData
import com.example.myportfolio.databinding.DialogCertificateDetailBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class CertificateDialog(
    private val activity: Activity,
    private val details: CertificateData,
    private val listener: DialogEventListenerCredential
) {

    private val binding = DialogCertificateDetailBinding.inflate(LayoutInflater.from(activity))

    private fun initializeValues() {
        binding.dialogCertificateTitle.text = details.certificateTitle
        Glide.with(binding.root)
            .load(imageResource())
            .into(binding.dialogCertificateCompany)
    }

    private fun imageResource(): Int {
        return when (details.companyName) {
            "Coursera" -> R.drawable.ic_coursera_48
            "Udemy" -> R.drawable.ic_udemy_48
            else -> R.drawable.cat
        }
    }


    fun showCertificateDetails() {
        initializeValues()
        MaterialAlertDialogBuilder(activity)
            .setView(binding.root)
            .setCancelable(false)
            .setPositiveButton(R.string.certificate_dialog_positive) { dialog, _ ->
                run {
                    listener.showWebView(details)
                    dialog.dismiss()
                }
            }
            .setNegativeButton(R.string.certificate_dialog_negative) { dialog, _ ->
                run {
                    dialog.dismiss()
                }
            }
            .show()
    }


    interface DialogEventListenerCredential {
        fun showWebView(details: CertificateData)
    }


}