package com.example.myportfolio.fragments.certificate

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myportfolio.R
import com.example.myportfolio.data.CertificateData
import com.example.myportfolio.databinding.FragmentCertificateBinding
import com.example.myportfolio.utility.Constants
import com.example.myportfolio.utility.FragmentLifecycleLog
import com.example.myportfolio.utility.RecyclerViewDecorator
import com.example.myportfolio.fragments.settings.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CertificateFragment : FragmentLifecycleLog(), CertificateAdapter.CertificateDetailsListener,
    CertificateDialog.DialogEventListenerCredential {

    private var _binding: FragmentCertificateBinding? = null
    private val binding get() = _binding!!


    private val certificateViewModel: CertificateViewModel by viewModels()
    private val settingsViewModel: SettingsViewModel by viewModels()

    private var canRefresh = false

    private lateinit var certificateDialog: CertificateDialog
    private lateinit var certificateAdapter: CertificateAdapter

    private lateinit var autoUpdateObserver: Observer<Boolean>
    private lateinit var needUpdateObserver: Observer<Boolean>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCertificateBinding.inflate(inflater, container, false)
        initializeRecyclerView()
        initializeObservers()
        checkRefreshFinish()
        refreshListener()
        return binding.root
    }

    private fun initializeObservers() {
        initializeAutoUpdateObserver()
        initializeNeedUpdateObserver()
    }

    private fun initializeNeedUpdateObserver() {
        needUpdateObserver = Observer {
            if (it) {
                lifecycleScope.launch(IO) {
                    certificateViewModel.updateCertificationList()
                }
                binding.certificateRefresh.isRefreshing = true
            }
        }
    }

    override fun onStart() {
        super.onStart()
        certificateViewModel.attachCurrentStateListener()
        handleAutoUpdateSetting()
    }

    override fun onStop() {
        super.onStop()
        certificateViewModel.removeListeners()
        certificateViewModel.needUpdate().removeObserver(needUpdateObserver)
        settingsViewModel.isAutoUpdate.removeObserver(autoUpdateObserver)
    }


    private fun initializeAutoUpdateObserver() {
        autoUpdateObserver = Observer {
            if (it) {
                certificateViewModel.needUpdate().observe(viewLifecycleOwner, needUpdateObserver)
            }
        }
    }

    /*Automatically Sync to Network when setting is ON*/
    private fun handleAutoUpdateSetting() {
        settingsViewModel.isAutoUpdate.observe(viewLifecycleOwner, autoUpdateObserver)
    }


    /*When swiped up it will sync it to the latest data.*/
    private fun refreshListener() {
        binding.certificateRefresh.setOnRefreshListener {
            lifecycleScope.launch(IO) {
                if (canRefresh) {
                    certificateViewModel.updateCertificationList()
                } else {
                    binding.certificateRefresh.isRefreshing = false
                }
            }
        }
    }
    private val TAG = "CertificateFragment"

    /*Checks whether a new data has been placed. */
    private fun checkRefreshFinish() {
        certificateViewModel.needUpdate().observe(viewLifecycleOwner, {
            Log.i(TAG, "checkRefreshFinish: $it")
            canRefresh = it
            if (!it) {
                binding.certificateRefresh.isRefreshing = false
                binding.certificateTitle.setTextColor(Color.BLACK)
            } else {
                binding.certificateTitle.setTextColor(Color.GREEN)
            }
        })
    }

    private fun initializeRecyclerView() {
        val recyclerViewDecorator = RecyclerViewDecorator(10)
        certificateAdapter = CertificateAdapter(this)
        binding.certificatesRecyclerView.apply {
            adapter = certificateAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
            addItemDecoration(recyclerViewDecorator)
        }
        updateRecyclerViewContents()
    }

    private fun updateRecyclerViewContents() {
        certificateViewModel.certificateList().observe(viewLifecycleOwner, {
            certificateAdapter.setCertificateList(it)
        })
    }

    override fun certificateDetailIndex(index: Int) {
        val details = certificateViewModel.certificateList().value?.get(index)
        details?.let {
            certificateDialog = CertificateDialog(requireActivity(), it, this)
            certificateDialog.showCertificateDetails()
        }
            ?: Toast.makeText(requireContext(), "Error", Toast.LENGTH_LONG).show()
    }

    override fun showWebView(details: CertificateData) {
        val bundle = bundleOf(Constants.BUNDLE_CERTIFICATE_DETAILS to details)
        Navigation.findNavController(binding.root)
            .navigate(R.id.certificateFragment_certificateCredential, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        certificateViewModel.removeListeners()
        _binding = null
    }
}