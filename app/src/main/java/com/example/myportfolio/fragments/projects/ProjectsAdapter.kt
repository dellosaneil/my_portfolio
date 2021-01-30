package com.example.myportfolio.fragments.projects

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myportfolio.R
import com.example.myportfolio.data.ProjectData
import com.example.myportfolio.databinding.ListItemProjectBinding
import com.example.myportfolio.utility.Constants
import com.example.myportfolio.utility.GlideApp
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.*


class ProjectsAdapter(private val listener: ProjectDetailListener) :
    RecyclerView.Adapter<ProjectsAdapter.ProjectsViewHolder>() {

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

        init {
            binding.projectsListItemImage.setOnClickListener(this)
        }

        fun bind(data: ProjectData) {
            binding.projectsListItemName.text = data.projectTitle
            binding.projectsListItemDescription.text = data.projectDescription
            val firstScreenShotReference =
                Firebase.storage.getReferenceFromUrl("${Constants.BUCKET_LINK}${data.firstImageReference}")
            GlideApp.with(binding.root.context)
                .asBitmap()
                .fitCenter()
                .load(firstScreenShotReference)
                .placeholder(R.drawable.ic_android_place_holder)
                .into(binding.projectsListItemImage)
            GlideApp.with(binding.root.context)
                .asDrawable()
                .load(indicateLanguageUsed(data.projectLanguage.toLowerCase(Locale.ROOT)))
                .into(binding.projectsLanguage)
        }

        private fun indicateLanguageUsed(language: String): Int {
            return if (language == "java") R.drawable.ic_java_24 else R.drawable.ic_kotlin_24
        }


        override fun onClick(v: View?) {
            when (v?.id) {
                R.id.projectsListItem_image -> listener.projectClickListener(adapterPosition)
            }
        }
    }

    interface ProjectDetailListener {
        fun projectClickListener(index: Int)
    }

}