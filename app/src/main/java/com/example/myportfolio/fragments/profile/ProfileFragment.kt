package com.example.myportfolio.fragments.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.myportfolio.R
import com.example.myportfolio.data.ProfileData
import com.example.myportfolio.databinding.FragmentProfileBinding
import com.example.myportfolio.utility.FragmentLifecycleLog
import com.example.myportfolio.utility.RecyclerViewDecorator

class ProfileFragment : FragmentLifecycleLog() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var profileAdapter: ProfileAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        initializeRecyclerView()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        placeImage(view)
    }

    private fun placeImage(view: View) {
        Glide.with(view)
            .load(R.drawable.photo)
            .placeholder(R.drawable.ic_certificates_24)
            .into(binding.dialogCertificateFace)
    }


    private fun initializeRecyclerView() {
        val recyclerViewDecorator = RecyclerViewDecorator(5)
        profileAdapter = ProfileAdapter()
        binding.profileRecyclerView.apply {
            adapter = profileAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(recyclerViewDecorator)
        }
        populateRecyclerView()
    }

    private fun populateRecyclerView() {
        val tempData = listOf(
            ProfileData(
                "Android Journey",
                "I started my Android Development journey at the beginning of the pandemic. I had the basic knowledge in Java which made it easier to transition to Android Development. At the beginning it was stressful because there are not many resources that you can get with Android Development compared to Web Development. It was a steep road doing everything on my own reading, watching, and testing but it was worth it because I became independent."),
            ProfileData(
                "Learning Style",
                "I would watch some YouTube video regarding the new concept that I want to learn. After finishing the video, I would find articles from medium.com to supplement that videos that I watch, then I would read the official documentation of the Android team from Google. And the most important one is I would make projects that would test my knowledge regarding the new concept."),
            ProfileData("Why Android Development?",
                "I chose Android Development as my career path because I can bring the projects that I created with me. I can use it in my daily life, and I can create applications for the people I know. Even though it was hard to start with Android Development it was still fun for me because I love what I was doing. "),
            ProfileData(
                "About me",
                "I am a self-taught Android Developer which mainly use Kotlin in my Android Applications. Some of my hobbies are solving Mathematical equations, Muay Thai, programming and learning. I dedicated 2 years just to learn programming and I am confident in my skills as a programmer. I learn by doing projects and I make sure that it is scalable. I like writing clean codes with functions/method that would be understood even without documentation. I like using MVVM in my projects because it makes the code cleaner.")
        )
        profileAdapter.setIntroductionList(tempData)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}