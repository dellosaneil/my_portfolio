package com.example.myportfolio.fragments.projects

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
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
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ProjectDetails : FragmentLifecycleLog() {

    private var _binding: FragmentProjectDetailsBinding? = null
    private val binding get() = _binding!!
    private val storage = Firebase.storage.reference
    private val MAX_BYTES = 10L * 1024 * 1024


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

        lifecycleScope.launch(IO){
            val drawableArray = convertByteArray(projectData)
            val viewArray = arrayOf(
                binding.firstScreenshot,
                binding.secondScreenshot,
                binding.thirdScreenshot,
                binding.projectDetailsLanguageUsed
            )
            withContext(Main) {
                repeat(4) { index ->
                    run {
                        Glide.with(binding.root.context)
                            .load(drawableArray[index])
                            .into(viewArray[index])
                    }
                }
            }
        }


    }

    private suspend fun convertByteArray(projectData: ProjectData): MutableList<Bitmap?> {
        val bitmapList = mutableListOf<Bitmap?>()
        bitmapList.add(projectData.projectImage)
        val job = lifecycleScope.async(IO) {
            repeat(2) {
                    storage.child(projectData.secondImageReference).getBytes(MAX_BYTES)
                        .addOnSuccessListener { imageByteArray ->
                            run {
                                bitmapList.add(BitmapFactory.decodeByteArray(imageByteArray, 0 , imageByteArray.size))
                            }
                        }.await()
            }
        }
        job.await()
        bitmapList.add(resources.getDrawable(R.drawable.ic_coursera_48).toBitmap())
        return bitmapList
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}