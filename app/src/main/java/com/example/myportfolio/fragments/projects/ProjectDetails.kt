package com.example.myportfolio.fragments.projects

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.myportfolio.R
import com.example.myportfolio.data.ProjectData
import com.example.myportfolio.databinding.FragmentProjectDetailsBinding
import com.example.myportfolio.utility.Constants.Companion.BUNDLE_PROJECT_DETAILS
import com.example.myportfolio.utility.Constants.Companion.BUNDLE_TO_WEB_VIEW_DETAILS
import com.example.myportfolio.utility.FragmentLifecycleLog

class ProjectDetails : FragmentLifecycleLog() {

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
        val details = arguments?.getParcelable<ProjectData>(BUNDLE_PROJECT_DETAILS)
        binding.projectDetailsGithub.setOnClickListener {
            val bundle = bundleOf(BUNDLE_TO_WEB_VIEW_DETAILS to details)
            Navigation.findNavController(view)
                .navigate(R.id.projectDetails_projectWebView, bundle)
        }

        details?.projectTitle.let { binding.projectDetailsProjectName.text = it }
    }

    private fun placeScreenshots() {
        val drawableArray = arrayOf(
            R.drawable.ic_news_tracker,
            R.drawable.ic_news_tracker,
            R.drawable.ic_news_tracker,
            R.drawable.ic_kotlin_big_48
        )
        val viewArray = arrayOf(
            binding.firstScreenshot,
            binding.secondScreenshot,
            binding.thirdScreenshot,
            binding.projectDetailsLanguageUsed
        )

        repeat(4) { index ->
            run {
                Glide.with(binding.root.context)
                    .load(drawableArray[index])
                    .into(viewArray[index])
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}