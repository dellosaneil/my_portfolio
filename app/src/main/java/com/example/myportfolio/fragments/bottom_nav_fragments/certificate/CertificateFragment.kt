package com.example.myportfolio.fragments.bottom_nav_fragments.certificate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myportfolio.R
import com.example.myportfolio.RecyclerViewDecorator
import com.example.myportfolio.data.CertificateData
import com.example.myportfolio.data.ProfileData
import com.example.myportfolio.databinding.FragmentCertificateBinding
import com.example.myportfolio.databinding.FragmentProfileBinding
import com.example.myportfolio.fragments.bottom_nav_fragments.profile.ProfileAdapter

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
        return binding.root
    }

    private fun initializeRecyclerView() {
        val recyclerViewDecorator = RecyclerViewDecorator(5)
        certificateAdapter = CertificateAdapter()
        binding.certificatesRecyclerView.apply {
            adapter = certificateAdapter
            layoutManager = GridLayoutManager(requireContext(), 3)
            addItemDecoration(recyclerViewDecorator)
        }
        populateRecyclerView()
    }

    private fun populateRecyclerView() {
        certificateViewModel.certificateList().observe(viewLifecycleOwner, Observer {
            certificateAdapter.setCertificateList(it)
        })
    }
}