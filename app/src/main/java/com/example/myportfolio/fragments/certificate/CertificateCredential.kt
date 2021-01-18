package com.example.myportfolio.fragments.certificate

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.myportfolio.R
import com.example.myportfolio.data.CertificateData
import com.example.myportfolio.databinding.FragmentCertificateCredentialBinding
import com.example.myportfolio.utility.Constants.Companion.BUNDLE_CERTIFICATE_DETAILS

class CertificateCredential : Fragment() {
    private var _binding: FragmentCertificateCredentialBinding? = null
    private val binding get() = _binding!!


    private val TAG = "CertificateCredential"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCertificateCredentialBinding.inflate(inflater, container, false)
        val rawDetails = arguments?.getParcelable<CertificateData>(BUNDLE_CERTIFICATE_DETAILS)
        binding.materialToolbar.title = rawDetails?.certificateTitle
        loadWebView(rawDetails?.credentialUrl)
        return binding.root
    }

    private fun loadWebView(credentialUrl: String?) {
        credentialUrl?.let {
            binding.webView
                .loadUrl(it)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeNavigationListener(view)
    }

    private fun initializeNavigationListener(view: View) {
        binding.materialToolbar.setNavigationOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.certificateCredential_certificateFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.webView.destroy()
        _binding = null
    }
}