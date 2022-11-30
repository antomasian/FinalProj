package com.example.finalproj.ui.chats

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.tango.ui.BottomNavActivity
import com.example.tango.databinding.ChatRowBinding
import com.example.tango.viewModels.ChatViewModel
import com.example.tango.viewModels.ChatsListViewModel
import com.example.tango.R
import com.example.tango.ui.chats.ChatsListFragmentDirections
import com.example.tango.viewModels.ProfileViewModel
import com.firebase.ui.auth.data.model.User
import java.util.*

class ChatRowAdapter(private val viewModel: ChatsListViewModel, private val bottomNavActivity: BottomNavActivity)
    : ListAdapter<ChatViewModel, ChatRowAdapter.VH>(ChatDiff()) {
    class ChatDiff : DiffUtil.ItemCallback<ChatViewModel>() {
        override fun areItemsTheSame(oldItem: ChatViewModel, newItem: ChatViewModel): Boolean {
            return oldItem.chat.id == newItem.chat.id
        }
        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: ChatViewModel, newItem: ChatViewModel): Boolean {
            return oldItem.chat == newItem.chat
        }
    }

    inner class VH(val chatRowBinding: ChatRowBinding)
        : RecyclerView.ViewHolder(chatRowBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ChatRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val binding = holder.chatRowBinding
        val item = getItem(position)
        binding.apply {
            item.observeChattingProfileVM().observe(bottomNavActivity) {
                binding.displayName.text = it?.currentUser?.value?.displayName
                item.getProfilePic(imageView = binding.imageView)
            }

            item.messages?.observe(bottomNavActivity) {
                binding.messageText.text = it?.lastOrNull()?.text
            }

        }

        binding.root.setOnClickListener {
            Log.d(javaClass.simpleName, "Item touched $position")
            val action = ChatsListFragmentDirections.actionChatsListFragToChatFrag(position)
            bottomNavActivity.findNavController(R.id.bottom_nav_host_fragment).navigate(action)

        }
    }

}