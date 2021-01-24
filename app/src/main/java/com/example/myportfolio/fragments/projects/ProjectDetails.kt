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
import java.util.*

class ProjectDetails : FragmentLifecycleLog() {

    private var _binding: FragmentProjectDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var projectDetailsViewModel: ProjectDetailsViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProjectDetailsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val details = arguments?.getParcelable<ProjectData>(BUNDLE_PROJECT_DETAILS)
        projectDetailsViewModel = details?.let { ProjectDetailsViewModel(it) }!!

        binding.projectDetailsGithub.setOnClickListener {
            val bundle = bundleOf(BUNDLE_TO_WEB_VIEW_DETAILS to details)
            Navigation.findNavController(view)
                .navigate(R.id.projectDetails_projectWebView, bundle)
        }
        displayText(details)
        projectDetailsViewModel.getFromStorage()
        placeScreenshots(details)
    }

    private fun displayText(projectData: ProjectData) {
        binding.projectDetailsDescription.text = projectData.projectDescription
        projectData.projectTitle.let { binding.projectDetailsProjectName.text = it }
    }


    private fun placeScreenshots(projectData: ProjectData) {
        val viewArray = arrayOf(
            binding.firstScreenshot,
            binding.secondScreenshot,
            binding.thirdScreenshot,
            binding.projectDetailsLanguageUsed
        )

        projectDetailsViewModel.byteImages().observe(viewLifecycleOwner) {
            if (it.size >= 1) {
                val index = it.size
                Glide.with(binding.root.context)
                    .asBitmap()
                    .load(it[it.size - 1])
                    .into(viewArray[index])
            }
        }
        val drawableLanguage = checkLanguage(projectData.projectLanguage.toLowerCase(Locale.ROOT))
        Glide.with(binding.root.context)
            .load(drawableLanguage)
            .into(viewArray[3])
        Glide.with(binding.root.context)
            .load(projectData.projectImage)
            .into(viewArray[0])
    }

    private fun checkLanguage(language: String): Int {
        return if (language == "kotlin") R.drawable.ic_kotlin_big_48
        else R.drawable.ic_java_48
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}