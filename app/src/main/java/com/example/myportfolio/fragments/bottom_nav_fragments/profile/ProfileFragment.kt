package com.example.myportfolio.fragments.bottom_nav_fragments.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myportfolio.RecyclerViewDecorator
import com.example.myportfolio.data.ProfileData
import com.example.myportfolio.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

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

    private fun initializeRecyclerView() {
        val recyclerViewDecorator = RecyclerViewDecorator(5)
        profileAdapter = ProfileAdapter()
        binding.recyclerViewProfile.apply {
            adapter = profileAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(recyclerViewDecorator)
        }
        populateRecyclerView()
    }

    private fun populateRecyclerView() {
        val tempData = listOf(
            ProfileData(
                "Hello there",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
            ),
            ProfileData(
                "Cabbiler",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Quis hendrerit dolor magna eget. Pellentesque habitant morbi tristique senectus et netus et. Ipsum consequat nisl vel pretium lectus. Scelerisque viverra mauris in aliquam. Tincidunt lobortis feugiat vivamus at augue eget arcu. Enim praesent elementum facilisis leo vel. Elementum facilisis leo vel fringilla est ullamcorper eget. Eu augue ut lectus arcu bibendum at varius vel pharetra. At auctor urna nunc id cursus metus aliquam. Turpis egestas maecenas pharetra convallis posuere morbi leo. Enim lobortis scelerisque fermentum dui faucibus in ornare. Cursus vitae congue mauris rhoncus aenean vel elit scelerisque mauris. Curabitur gravida arcu ac tortor dignissim convallis aenean et. Magna eget est lorem ipsum dolor sit amet. Proin sed libero enim sed faucibus turpis. Massa id neque aliquam vestibulum morbi blandit cursus risus at."
            ),
            ProfileData(
                "Last",
                "Sit amet aliquam id diam maecenas ultricies. Lectus magna fringilla urna porttitor rhoncus. Massa tincidunt nunc pulvinar sapien. Justo eget magna fermentum iaculis eu non. At urna condimentum mattis pellentesque id nibh. Vitae sapien pellentesque habitant morbi tristique senectus et. Mattis nunc sed blandit libero. Sed egestas egestas fringilla phasellus. Suspendisse in est ante in nibh mauris cursus mattis. Vitae tempus quam pellentesque nec nam. Vulputate odio ut enim blandit volutpat maecenas. Fermentum posuere urna nec tincidunt praesent semper feugiat. Tincidunt eget nullam non nisi est sit. Consectetur adipiscing elit duis tristique. Malesuada pellentesque elit eget gravida cum sociis. Faucibus a pellentesque sit amet porttitor eget. Lacus vel facilisis volutpat est velit egestas dui id ornare. Ullamcorper eget nulla facilisi etiam dignissim diam quis."
            )
        )
        profileAdapter.setIntroductionList(tempData)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}