package com.example.myportfolio.fragments.projects

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import com.example.myportfolio.R
import com.example.myportfolio.data.ProjectData
import com.example.myportfolio.databinding.FragmentProjectDetailsBinding
import com.example.myportfolio.utility.Constants.Companion.BUCKET_LINK
import com.example.myportfolio.utility.Constants.Companion.BUNDLE_PROJECT_DETAILS
import com.example.myportfolio.utility.Constants.Companion.BUNDLE_TO_WEB_VIEW_DETAILS
import com.example.myportfolio.utility.FragmentLifecycleLog
import com.example.myportfolio.utility.GlideApp
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.util.*

class ProjectDetails : FragmentLifecycleLog() {

    private var _binding: FragmentProjectDetailsBinding? = null
    private val binding get() = _binding!!
    private val storage = Firebase.storage

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

        binding.projectDetailsGithub.setOnClickListener {
            val bundle = bundleOf(BUNDLE_TO_WEB_VIEW_DETAILS to details)
            Navigation.findNavController(view)
                .navigate(R.id.projectDetails_projectWebView, bundle)
        }
        details?.let {
            displayText(it)
            placeScreenshots(it)
        }
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
        val firstScreenShotReference =
            storage.getReferenceFromUrl("$BUCKET_LINK${projectData.firstImageReference}")
        val secondScreenshotReference =
            storage.getReferenceFromUrl("$BUCKET_LINK${projectData.secondImageReference}")
        val thirdScreenshotReference =
            storage.getReferenceFromUrl("$BUCKET_LINK${projectData.thirdImageReference}")
        val photoReference = arrayOf(firstScreenShotReference, secondScreenshotReference, thirdScreenshotReference)
        val drawableLanguage = checkLanguage(projectData.projectLanguage.toLowerCase(Locale.ROOT))
        GlideApp.with(requireContext())
            .load(drawableLanguage)
            .into(viewArray[3])
        loadFromFirebaseStorage(photoReference, viewArray)
    }

    private fun loadFromFirebaseStorage(
        photoReference: Array<StorageReference>, viewArray: Array<ImageView>
    ) {
        repeat(3) {
            GlideApp.with(requireContext())
                .load(photoReference[it])
                .placeholder(R.drawable.ic_news_tracker)
                .into(viewArray[it])
        }
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