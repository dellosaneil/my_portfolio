package com.example.myportfolio.fragments.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myportfolio.data.ProfileData
import com.example.myportfolio.databinding.ListItemProfileBinding

class ProfileAdapter : RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder>() {

    private var introductionList: List<ProfileData> = listOf()

    fun setIntroductionList(newList: List<ProfileData>) {
        val oldList = introductionList
        val diffResult = DiffUtil.calculateDiff(
            DiffUtilProfileCallback(oldList, newList)
        )
        introductionList = newList
        diffResult.dispatchUpdatesTo(this)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val binding = ListItemProfileBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ProfileViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        val introduction = introductionList[position]
        holder.bind(introduction)
    }

    override fun getItemCount() = introductionList.size

    class DiffUtilProfileCallback(
        private val oldItem: List<ProfileData>,
        private val newItem: List<ProfileData>
    ) :
        DiffUtil.Callback() {
        override fun getOldListSize() = oldItem.size

        override fun getNewListSize() = newItem.size
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldItem[oldItemPosition].title == newItem[newItemPosition].title

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldItem[oldItemPosition] == newItem[newItemPosition]

    }


    inner class ProfileViewHolder(private val binding: ListItemProfileBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(profileData: ProfileData) {
            binding.rvProfileTitle.text = profileData.title
            binding.rvProfileContent.text = profileData.content
        }
    }
}