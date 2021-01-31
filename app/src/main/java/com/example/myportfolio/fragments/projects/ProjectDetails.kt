package com.example.myportfolio.fragments.projects

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Point
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.ImageButton
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
    private lateinit var projectTitle: String

    private lateinit var photoStorageReference : List<StorageReference>

    private var currentAnimator: Animator? = null
    private var shortAnimationDuration: Int = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProjectDetailsBinding.inflate(inflater, container, false)
        binding.projectDetailsOpenApp.setOnClickListener(this)
        binding.firstScreenshot.setOnClickListener(this)
        binding.secondScreenshot.setOnClickListener(this)
        binding.thirdScreenshot.setOnClickListener(this)
        shortAnimationDuration = resources.getInteger(android.R.integer.config_shortAnimTime)
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
        photoStorageReference = retrieveStorageReference(storageString)
        val drawableLanguage = checkLanguage(projectData.projectLanguage.toLowerCase(Locale.ROOT))
        GlideApp.with(requireContext())
            .load(drawableLanguage)
            .into(viewArray[3])
        loadFromFirebaseStorage(photoStorageReference, viewArray)
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

            R.id.first_screenshot -> {
                zoomImage(binding.firstScreenshot, 0)
            }

            R.id.second_screenshot -> {
                zoomImage(
                    binding.secondScreenshot,
                   1
                )
            }
            R.id.third_screenshot -> {
                zoomImage(binding.thirdScreenshot, 2)
            }
        }
    }

    private fun zoomImage(firstScreenshot: ImageButton, drawable : Int) {
        currentAnimator?.cancel()
        GlideApp.with(requireContext())
            .asBitmap()
            .load(photoStorageReference[drawable])
            .into(binding.projectDetailsEnlargedImage)

        val startBoundsInt = Rect()
        val finalBoundsInt = Rect()
        val globalOffset = Point()

        firstScreenshot.getGlobalVisibleRect(startBoundsInt)
        binding.projectDetailsContainer
            .getGlobalVisibleRect(finalBoundsInt, globalOffset)
        startBoundsInt.offset(-globalOffset.x, -globalOffset.y)
        finalBoundsInt.offset(-globalOffset.x, -globalOffset.y)

        val startBounds = RectF(startBoundsInt)
        val finalBounds = RectF(finalBoundsInt)

        val startScale: Float
        if ((finalBounds.width() / finalBounds.height() > startBounds.width() / startBounds.height())) {
            // Extend start bounds horizontally
            startScale = startBounds.height() / finalBounds.height()
            val startWidth: Float = startScale * finalBounds.width()
            val deltaWidth: Float = (startWidth - startBounds.width()) / 2
            startBounds.left -= deltaWidth.toInt()
            startBounds.right += deltaWidth.toInt()
        } else {
            // Extend start bounds vertically
            startScale = startBounds.width() / finalBounds.width()
            val startHeight: Float = startScale * finalBounds.height()
            val deltaHeight: Float = (startHeight - startBounds.height()) / 2f
            startBounds.top -= deltaHeight.toInt()
            startBounds.bottom += deltaHeight.toInt()
        }

        firstScreenshot.alpha = 0f
        binding.projectDetailsEnlargedImage.visibility = View.VISIBLE

        binding.projectDetailsEnlargedImage.pivotX = 0f
        binding.projectDetailsEnlargedImage.pivotY = 0f

        currentAnimator = AnimatorSet().apply {
            play(
                ObjectAnimator.ofFloat(
                    binding.projectDetailsEnlargedImage,
                    View.X,
                    startBounds.left,
                    finalBounds.left
                )
            ).apply {
                with(
                    ObjectAnimator.ofFloat(
                        binding.projectDetailsEnlargedImage,
                        View.Y,
                        startBounds.top,
                        finalBounds.top
                    )
                )
                with(
                    ObjectAnimator.ofFloat(
                        binding.projectDetailsEnlargedImage,
                        View.SCALE_X,
                        startScale,
                        1f
                    )
                )
                with(
                    ObjectAnimator.ofFloat(
                        binding.projectDetailsEnlargedImage,
                        View.SCALE_Y,
                        startScale,
                        1f
                    )
                )
            }
            duration = shortAnimationDuration.toLong()
            interpolator = DecelerateInterpolator()
            addListener(object : AnimatorListenerAdapter() {

                override fun onAnimationEnd(animation: Animator) {
                    currentAnimator = null
                }

                override fun onAnimationCancel(animation: Animator) {
                    currentAnimator = null
                }
            })
            start()
        }

        binding.projectDetailsEnlargedImage.setOnClickListener {
            currentAnimator?.cancel()

            currentAnimator = AnimatorSet().apply {
                play(
                    ObjectAnimator.ofFloat(
                        binding.projectDetailsEnlargedImage,
                        View.X,
                        startBounds.left
                    )
                ).apply {
                    with(
                        ObjectAnimator.ofFloat(
                            binding.projectDetailsEnlargedImage,
                            View.Y,
                            startBounds.top
                        )
                    )
                    with(
                        ObjectAnimator.ofFloat(
                            binding.projectDetailsEnlargedImage,
                            View.SCALE_X,
                            startScale
                        )
                    )
                    with(
                        ObjectAnimator.ofFloat(
                            binding.projectDetailsEnlargedImage,
                            View.SCALE_Y,
                            startScale
                        )
                    )
                }
                duration = shortAnimationDuration.toLong()
                interpolator = DecelerateInterpolator()
                addListener(object : AnimatorListenerAdapter() {

                    override fun onAnimationEnd(animation: Animator) {
                        firstScreenshot.alpha = 1f
                        binding.projectDetailsEnlargedImage.visibility = View.GONE
                        currentAnimator = null
                    }

                    override fun onAnimationCancel(animation: Animator) {
                        firstScreenshot.alpha = 1f
                        binding.projectDetailsEnlargedImage.visibility = View.GONE
                        currentAnimator = null
                    }
                })
                start()
            }
        }
    }


}























