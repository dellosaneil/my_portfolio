package com.example.myportfolio.fragments.certificate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myportfolio.utility.RecyclerViewDecorator
import com.example.myportfolio.databinding.FragmentCertificateBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CertificateFragment : Fragment() {

    private var _binding: FragmentCertificateBinding? = null
    private val binding get() = _binding!!
    private lateinit var certificateAdapter: CertificateAdapter
    private val certificateViewModel: CertificateViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCertificateBinding.inflate(inflater, container, false)
        initializeRecyclerView()
        observeUpdate()
        return binding.root
    }

    private fun observeUpdate() {
        certificateViewModel.checkUpdate()
        certificateViewModel.needUpdate().observe(viewLifecycleOwner, {
            if(it){
                binding.certificateUpdate.visibility = View.VISIBLE
            }else{
                binding.certificateUpdate.visibility = View.GONE
            }
        })
    }

    private fun initializeRecyclerView() {
        val recyclerViewDecorator = RecyclerViewDecorator(10)
        certificateAdapter = CertificateAdapter()
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
        _binding = null
    }
}