package com.example.myportfolio.fragments.projects

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.myportfolio.R
import com.example.myportfolio.data.ProjectData
import com.example.myportfolio.databinding.FragmentProjectDetailsBinding
import com.example.myportfolio.utility.Constants.Companion.BUNDLE_PROJECT_DETAILS
import com.example.myportfolio.utility.Constants.Companion.BUNDLE_TO_WEB_VIEW_DETAILS
import com.example.myportfolio.utility.FragmentLifecycleLog
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class ProjectDetails : FragmentLifecycleLog() {

    private var _binding: FragmentProjectDetailsBinding? = null
    private val binding get() = _binding!!
    private val storage = Firebase.storage.reference
    private val maxBytes = 10L * 1024 * 1024


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
        details?.let { placeScreenshots(it) }
        binding.projectDetailsGithub.setOnClickListener {
            val bundle = bundleOf(BUNDLE_TO_WEB_VIEW_DETAILS to details)
            Navigation.findNavController(view)
                .navigate(R.id.projectDetails_projectWebView, bundle)
        }
        details?.projectTitle.let { binding.projectDetailsProjectName.text = it }
    }

    private fun placeScreenshots(projectData: ProjectData) {
        val viewArray = arrayOf(
            binding.firstScreenshot,
            binding.secondScreenshot,
            binding.thirdScreenshot,
            binding.projectDetailsLanguageUsed
        )
        lifecycleScope.launch(IO) {
            repeat(2) {
                val index = it + 1
                val referenceLink =
                    if (index == 1) projectData.secondImageReference else projectData.thirdImageReference
                storage.child(referenceLink).getBytes(maxBytes).addOnSuccessListener { image ->
                    run {
                        Glide.with(binding.root.context)
                            .asBitmap()
                            .load(image)
                            .into(viewArray[index])
                    }
                }
            }
        }
        val drawableLanguage = checkLanguage(projectData.projectLanguage)
        Glide.with(binding.root.context)
            .load(drawableLanguage)
            .into(viewArray[3])
        Glide.with(binding.root.context)
            .load(projectData.projectImage)
            .into(viewArray[0])
    }

    private fun checkLanguage(language: String): Int {
        return if (language == "Kotlin") R.drawable.ic_kotlin_big_48
        else R.drawable.ic_java_48
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}