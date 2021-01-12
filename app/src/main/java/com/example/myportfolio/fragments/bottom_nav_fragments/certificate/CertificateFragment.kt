package com.example.myportfolio.fragments.bottom_nav_fragments.certificate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myportfolio.RecyclerViewDecorator
import com.example.myportfolio.data.CertificateData
import com.example.myportfolio.databinding.FragmentCertificateBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CertificateFragment : Fragment() {

    private var _binding: FragmentCertificateBinding? = null
    private val binding get() = _binding!!
    private lateinit var certificateAdapter: CertificateAdapter
    private val certificateViewModel: CertificateViewModel by viewModels()
    private var number  = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCertificateBinding.inflate(inflater, container, false)
        initializeRecyclerView()



//        DELETE ALL THIS....
        binding.certificateAdd.setOnClickListener {
            lifecycleScope.launch(IO) { certificateViewModel.updateCertificationList(CertificateData("Test", "Udemy", number.toString(), "aksdnaskld")) }
            number++
        }
        binding.certificateDelete.setOnClickListener {
            lifecycleScope.launch(IO) {
                certificateViewModel.deleteCertificates()
            }
        }
//        UNTIL HEREEEEE

        return binding.root
    }

    private fun initializeRecyclerView() {
        val recyclerViewDecorator = RecyclerViewDecorator(5)
        certificateAdapter = CertificateAdapter()
        binding.certificatesRecyclerView.apply {
            adapter = certificateAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
            addItemDecoration(recyclerViewDecorator)
        }
        populateRecyclerView()
    }

    private fun populateRecyclerView() {
        certificateViewModel.certificateList().observe(viewLifecycleOwner, {
            certificateAdapter.setCertificateList(it)
        })
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}