package com.example.myportfolio.fragments.projects

import android.graphics.Color
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
    private var canRefresh = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProjectsBinding.inflate(inflater, container, false)
        initializeRecyclerView()
        refreshListenerProgress()
        refreshListener()
        return binding.root
    }


    /*Whenever RefreshLayout is triggered update ProjectLists.*/
    private fun refreshListener() {
        binding.projectsRefresh.setOnRefreshListener {
            if (canRefresh) {
                lifecycleScope.launch(IO) {
                    projectViewModel.updateProjectList()
                }
            } else {
                binding.projectsRefresh.isRefreshing = false
            }
        }
    }


    /*Checks whether updating is finished and change value of refresh availability.*/
    private fun refreshListenerProgress() {
        projectViewModel.currentState().observe(viewLifecycleOwner) {
            canRefresh = it
            if (!it) {
                binding.projectsRefresh.isRefreshing = false
                binding.projectsTitle.setTextColor(Color.BLACK)
            }else{
                binding.projectsTitle.setTextColor(Color.GREEN)
            }
        }
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


    /*Places the values in the ROOM database inside the Recycler view*/
    private fun updateRecyclerViewContents() {
        projectViewModel.projectList().observe(viewLifecycleOwner, {
            projectsAdapter.setProjectList(it)
            if (it.isNotEmpty()) {
                binding.projectsRecyclerView.smoothScrollToPosition(it.size - 1)
            }

        })
    }
    /*Redirect to different Fragment*/
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

    override fun onDestroyView() {
        super.onDestroyView()
        projectViewModel.removeListener()
        _binding = null
    }


}