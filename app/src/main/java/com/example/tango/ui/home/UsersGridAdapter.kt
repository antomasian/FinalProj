package com.example.tango.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.tango.databinding.ProfileGridItemBinding
import com.example.tango.ui.BottomNavActivity
import com.example.tango.viewModels.ProfileViewModel
import com.example.tango.viewModels.ProfilesListViewModel

class UsersGridAdapter(private val profilesListViewModel: ProfilesListViewModel, private val bottomNavActivity: BottomNavActivity)
    : androidx.recyclerview.widget.ListAdapter<ProfileViewModel, UsersGridAdapter.VH>(ProfileDiff()) {
    class ProfileDiff : DiffUtil.ItemCallback<ProfileViewModel>() {
        override fun areItemsTheSame(oldItem: ProfileViewModel, newItem: ProfileViewModel): Boolean {
            return oldItem.currentUser?.value?.id == newItem.currentUser?.value?.id
        }
        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: ProfileViewModel, newItem: ProfileViewModel): Boolean {
            return oldItem == newItem
        }
    }

    inner class VH(val userGridItemBinding: ProfileGridItemBinding)
        : RecyclerView.ViewHolder(userGridItemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ProfileGridItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val binding = holder.userGridItemBinding
        val item = getItem(position)
        binding.apply {
            profilesListViewModel.observeProfiles().observe(bottomNavActivity) {
                binding.displayNameTV.text = item.currentUser?.value?.displayName
            }
            item.getProfilePic(binding.profPicImgView)
        }

        binding.root.setOnClickListener {
            bottomNavActivity.launchProfileFragment()
        }

    }

}