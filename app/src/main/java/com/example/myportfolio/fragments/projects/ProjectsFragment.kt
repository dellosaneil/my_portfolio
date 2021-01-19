package com.example.myportfolio.fragments.projects

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myportfolio.R
import com.example.myportfolio.data.ProjectData
import com.example.myportfolio.utility.RecyclerViewDecorator
import com.example.myportfolio.databinding.FragmentProjectsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ProjectsFragment : Fragment(), ProjectsAdapter.ProjectDetailListener {

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
        lifecycleScope.launch(IO){
            projectViewModel.insertProject(ProjectData("Test Project", resources.getString(R.string.lorem_text), "asmkldnl"))
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
            if(it.isNotEmpty()){
                binding.projectsRecyclerView.smoothScrollToPosition(it.size - 1)
            }

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun projectClickListener(index: Int) {
        Navigation.findNavController(binding.root).navigate(R.id.projectFragment_projectDetails)
    }
}