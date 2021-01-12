package com.example.myportfolio.fragments.projects

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myportfolio.data.ProjectData
import com.example.myportfolio.databinding.ListItemCertificateBinding
import com.example.myportfolio.databinding.ListItemProjectBinding

class ProjectsAdapter : RecyclerView.Adapter<ProjectsAdapter.ProjectsViewModel>() {

    private val projectList : List<ProjectData> = listOf()



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectsViewModel {
        val binding = ListItemProjectBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ProjectsViewModel(binding)
    }

    override fun onBindViewHolder(holder: ProjectsViewModel, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount() = projectList.size


    class ProjectsViewModel(private val binding : ListItemProjectBinding) : RecyclerView.ViewHolder(binding.root) {

    }



}