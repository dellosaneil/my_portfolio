package com.example.myportfolio.fragments.bottom_nav_fragments.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myportfolio.data.ProfileData
import com.example.myportfolio.databinding.ListItemProfileBinding

class ProfileAdapter : RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder>() {

    private var introductionList: List<ProfileData> = listOf()

    fun setIntroductionList(profileData: List<ProfileData>) {
        introductionList = profileData
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

    inner class ProfileViewHolder(private val binding: ListItemProfileBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(profileData: ProfileData) {
            binding.rvProfileTitle.text = profileData.title
            binding.rvProfileContent.text = profileData.content
        }
    }
}