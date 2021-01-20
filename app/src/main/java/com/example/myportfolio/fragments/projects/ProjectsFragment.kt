package com.example.myportfolio.fragments.projects

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myportfolio.R
import com.example.myportfolio.data.ProjectData
import com.example.myportfolio.databinding.FragmentProjectsBinding
import com.example.myportfolio.utility.Constants.Companion.BUNDLE_PROJECT_DETAILS
import com.example.myportfolio.utility.FragmentLifecycleLog
import com.example.myportfolio.utility.RecyclerViewDecorator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ProjectsFragment : FragmentLifecycleLog(), ProjectsAdapter.ProjectDetailListener {

    private var _binding: FragmentProjectsBinding? = null
    private val binding get() = _binding!!
    private val projectViewModel: ProjectsViewModel by viewModels()
    private lateinit var projectsAdapter: ProjectsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProjectsBinding.inflate(inflater, container, false)
        initializeRecyclerView()
        lifecycleScope.launch(IO) {
            val icon = BitmapFactory.decodeResource(resources, R.drawable.ic_news_tracker)

            projectViewModel.insertProject(
                ProjectData(
                    "Test Project",
                    resources.getString(R.string.lorem_text),
                    icon,
                    "https://github.com/dellosaneil/my_portfolio"
                )
            )
        }
        return binding.root
    }

    private fun initializeRecyclerView() {
        projectsAdapter = ProjectsAdapter(this)
        val decorator = RecyclerViewDecorator(5)
        binding.projectsRecyclerView.apply {
            adapter = projectsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(decorator)
        }
        updateRecyclerViewContents()
    }

    private fun updateRecyclerViewContents() {
        projectViewModel.projectList().observe(viewLifecycleOwner, {
            projectsAdapter.setProjectList(it)
            if (it.isNotEmpty()) {
                binding.projectsRecyclerView.smoothScrollToPosition(it.size - 1)
            }

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun projectClickListener(index: Int) {
        val bundle = bundleOf(
            BUNDLE_PROJECT_DETAILS to (projectViewModel.projectList().value?.get(
                index
            ))
        )
        Navigation.findNavController(binding.root).navigate(
            R.id.projectFragment_projectDetails,
            bundle
        )
    }
}