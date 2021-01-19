package com.example.myportfolio.fragments.projects

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.myportfolio.R
import com.example.myportfolio.databinding.FragmentProjectDetailsBinding
import com.example.myportfolio.databinding.FragmentProjectsBinding

class ProjectDetails : Fragment() {

    private var _binding: FragmentProjectDetailsBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProjectDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        placeScreenshots()
    }

    private fun placeScreenshots() {
        Glide.with(binding.root.context)
            .load(R.drawable.ic_news_tracker)
            .into(binding.firstScreenshot)

        Glide.with(binding.root.context)
            .load(R.drawable.ic_news_tracker)
            .into(binding.secondScreenshot)

        Glide.with(binding.root.context)
            .load(R.drawable.ic_news_tracker)
            .into(binding.thirdScreenshot)
    }


}