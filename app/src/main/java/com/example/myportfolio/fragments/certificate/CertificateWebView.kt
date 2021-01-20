package com.example.myportfolio.fragments.certificate

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.navigation.Navigation
import com.example.myportfolio.data.CertificateData
import com.example.myportfolio.databinding.FragmentCertificateWebviewBinding
import com.example.myportfolio.utility.Constants.Companion.BUNDLE_CERTIFICATE_DETAILS
import com.example.myportfolio.utility.FragmentLifecycleLog

class CertificateWebView : FragmentLifecycleLog() {
    private var _binding: FragmentCertificateWebviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCertificateWebviewBinding.inflate(inflater, container, false)
        val rawDetails = arguments?.getParcelable<CertificateData>(BUNDLE_CERTIFICATE_DETAILS)
        binding.certificateWebViewToolbar.title = rawDetails?.certificateTitle
        loadWebView(rawDetails?.credentialUrl)
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadWebView(credentialUrl: String?) {
        binding.certificateWebViewWebView.webViewClient = WebViewClient()
        credentialUrl?.let {
            binding.certificateWebViewWebView.apply {
                loadUrl(it)
                settings.javaScriptEnabled = true
                settings.displayZoomControls = true
                settings.loadsImagesAutomatically = true
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    settings.safeBrowsingEnabled = true
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeNavigationListener(view)
    }

    private fun initializeNavigationListener(view: View) {
        binding.certificateWebViewToolbar.setNavigationOnClickListener {
            Navigation.findNavController(view).navigateUp()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding.certificateWebViewWebView.destroy()
        _binding = null
    }


    inner class WebViewClient : android.webkit.WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return false
        }

        override fun onPageCommitVisible(view: WebView?, url: String?) {
            super.onPageCommitVisible(view, url)
            binding.certificateWebViewProgressBar.visibility = View.GONE
            binding.certificateWebViewWebView.visibility = View.VISIBLE
        }
    }



}