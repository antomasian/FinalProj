package com.example.tango.ui.home

import android.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.tango.databinding.UserGridItemBinding
import com.example.tango.ui.AuthViewModel

class UsersGridAdapter(private val usersList: List<com.example.tango.model.User>)
    : RecyclerView.Adapter<UsersGridAdapter.VH>() {

    inner class VH(val userRowBinding: UserGridItemBinding)
        : RecyclerView.ViewHolder(userRowBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = UserGridItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val binding = holder.userRowBinding
        binding.displayNameTV.text = usersList.get(position).displayName
    }

    override fun getItemCount(): Int {
        return usersList.count()
    }
}