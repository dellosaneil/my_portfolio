package com.example.myportfolio.fragments.certificate

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myportfolio.R
import com.example.myportfolio.utility.RecyclerViewDecorator
import com.example.myportfolio.databinding.FragmentCertificateBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CertificateFragment : Fragment(), CertificateAdapter.CertificateDetailsListener {

    private var _binding: FragmentCertificateBinding? = null
    private val binding get() = _binding!!
    private lateinit var certificateAdapter: CertificateAdapter
    private val certificateViewModel: CertificateViewModel by viewModels()
    private lateinit var certificateDialog: CertificateDialog


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCertificateBinding.inflate(inflater, container, false)
        initializeRecyclerView()
        observeUpdate()
        updateClickListener()
        return binding.root
    }

    private fun updateClickListener() {
        binding.certificateUpdate.setOnClickListener {
            lifecycleScope.launch(IO) {
                certificateViewModel.updateCertificationList()
            }
        }
    }

    private fun observeUpdate() {
        certificateViewModel.checkUpdate()
        certificateViewModel.needUpdate().observe(viewLifecycleOwner, {
            if (it) {
                binding.certificateUpdate.visibility = View.VISIBLE
            } else {
                binding.certificateUpdate.visibility = View.GONE
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

    override fun onDestroyView() {
        super.onDestroyView()
        certificateViewModel.removeListeners()
        _binding = null
    }

    override fun certificateDetailIndex(index: Int) {
        val details = certificateViewModel.certificateList().value?.get(index)
        details?.let { certificateDialog = CertificateDialog(requireActivity(), it) }
            ?: Toast.makeText(requireContext(), "Error", Toast.LENGTH_LONG).show()
        certificateDialog.showCertificateDetails()
    }


}