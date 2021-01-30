package com.example.myportfolio.fragments.projects

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
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


class ProjectDetails : FragmentLifecycleLog(), View.OnClickListener {

    private var _binding: FragmentProjectDetailsBinding? = null
    private val binding get() = _binding!!
    private val storage = Firebase.storage
    private lateinit var projectTitle : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProjectDetailsBinding.inflate(inflater, container, false)
        binding.projectDetailsOpenApp.setOnClickListener(this)
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
            projectTitle = it.projectTitle
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
        val storageString = arrayOf(
            projectData.firstImageReference,
            projectData.secondImageReference,
            projectData.thirdImageReference
        )
        val photoReference = retrieveStorageReference(storageString)
        val drawableLanguage = checkLanguage(projectData.projectLanguage.toLowerCase(Locale.ROOT))
        GlideApp.with(requireContext())
            .load(drawableLanguage)
            .into(viewArray[3])
        loadFromFirebaseStorage(photoReference, viewArray)
    }

    private fun retrieveStorageReference(storageString: Array<String>): List<StorageReference> {
        val references = mutableListOf<StorageReference>()
        for (storageReference in storageString) {
            references.add(storage.getReferenceFromUrl("${BUCKET_LINK}$storageReference"))
        }
        return references
    }


    private fun loadFromFirebaseStorage(
        photoReference: List<StorageReference>, viewArray: Array<ImageView>
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

    private fun getPackageProject(projectTitle: String): String? {
        return when (projectTitle) {
            "Jose Rizal Reviewer" -> "com.lazybattley.thelazybattley"
            "Portfolio Admin" -> "com.example.portfolioadmin"
            "News Tracker" -> "com.example.newstracker"
            else -> null
        }
    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.projectDetails_openApp -> {
                val packageName = getPackageProject(projectTitle)
                packageName?.let {
                    val launchIntent: Intent? =
                        requireActivity().packageManager.getLaunchIntentForPackage(it)
                    startActivity(launchIntent)
                } ?: Toast.makeText(requireContext(), "Application not found.", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }


}























