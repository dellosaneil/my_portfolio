package com.example.myportfolio.fragments.projects

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.myportfolio.R
import com.example.myportfolio.data.ProjectData
import com.example.myportfolio.databinding.FragmentProjectWebViewBinding
import com.example.myportfolio.utility.Constants.Companion.BUNDLE_TO_WEB_VIEW_DETAILS
import com.example.myportfolio.utility.FragmentLifecycleLog

class ProjectWebView : FragmentLifecycleLog() {

    private var _binding: FragmentProjectWebViewBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProjectWebViewBinding.inflate(inflater, container, false)
        val details = arguments?.getParcelable<ProjectData>(BUNDLE_TO_WEB_VIEW_DETAILS)
        initializeWebView(details?.gitHubRepository)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.projectWebViewToolBar.setNavigationOnClickListener {
            Navigation.findNavController(view).navigateUp()
        }
    }


    @SuppressLint("SetJavaScriptEnabled")
    fun initializeWebView(url: String?) {
        binding.projectWebViewWebView.webViewClient = WebViewClient()
        binding.projectWebViewWebView.apply {
            url?.let {
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

    override fun onDestroyView() {
        super.onDestroyView()
        binding.projectWebViewWebView.destroy()
        _binding = null
    }



    inner class WebViewClient : android.webkit.WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return false
        }

        override fun onPageCommitVisible(view: WebView?, url: String?) {
            super.onPageCommitVisible(view, url)
            binding.projectWebViewProgressBar.visibility = View.GONE
            binding.projectWebViewWebView.visibility = View.VISIBLE
        }
    }

}