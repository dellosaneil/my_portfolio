package com.example.myportfolio.fragments.projects

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myportfolio.R
import com.example.myportfolio.data.ProjectData
import com.example.myportfolio.databinding.ListItemProjectBinding


class ProjectsAdapter(private val listener : ProjectDetailListener) : RecyclerView.Adapter<ProjectsAdapter.ProjectsViewHolder>() {

    private var projectList: List<ProjectData> = listOf()

    fun setProjectList(newList: List<ProjectData>) {
        val oldList = projectList
        val diffResult = DiffUtil.calculateDiff(
            DiffUtilCallbackProject(oldList, newList)
        )
        projectList = newList
        diffResult.dispatchUpdatesTo(this)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectsViewHolder {
        val binding = ListItemProjectBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ProjectsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProjectsViewHolder, position: Int) {
        val data = projectList[position]
        holder.bind(data)

    }

    override fun getItemCount() = projectList.size

    private class DiffUtilCallbackProject(
        private val oldItem: List<ProjectData>,
        private val newItem: List<ProjectData>
    ) : DiffUtil.Callback() {
        override fun getOldListSize() = oldItem.size
        override fun getNewListSize() = newItem.size
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldItem[oldItemPosition].projectId == newItem[newItemPosition].projectId

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldItem[oldItemPosition] == newItem[newItemPosition]
    }


    inner class ProjectsViewHolder(private val binding: ListItemProjectBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init{
            binding.projectsImage.setOnClickListener(this)
        }

        fun bind(data: ProjectData) {
            binding.projectsName.text = data.projectTitle
            binding.projectsDescription.text = data.projectDescription

            Glide.with(binding.root.context)
                .load(R.drawable.ic_news_tracker)
                .into(binding.projectsImage)
        }

        override fun onClick(v: View?) {
            when(v?.id){
                R.id.projects_image -> listener.projectClickListener(adapterPosition)
            }
        }
    }

    interface ProjectDetailListener{
        fun projectClickListener(index : Int)
    }



}