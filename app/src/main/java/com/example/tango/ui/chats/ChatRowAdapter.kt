package com.example.finalproj.ui.chats

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.commitNow
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.tango.MainActivity
import com.example.tango.databinding.ChatRowBinding
import com.example.tango.ui.chats.ChatViewModel
import com.example.tango.ui.chats.ChatsListViewModel
import com.example.tango.R
import com.example.tango.ui.chats.ChatFragment
import com.example.tango.ui.chats.ChatsListFragmentDirections

class ChatRowAdapter(private val viewModel: ChatsListViewModel, private val mainActivity: MainActivity)
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

            item.chattingUser?.observe(mainActivity) {
                binding.displayName.text = it?.displayName
            }
            item.messages?.observe(mainActivity) {
                binding.messageText.text = it?.lastOrNull()?.text
            }
            item.fetchProfilePic(imageView = binding.imageView)
        }

        binding.root.setOnClickListener {
            Log.d(javaClass.simpleName, "Item touched $position")
            val action = ChatsListFragmentDirections.actionChatsListFragToChatFrag(position)
            mainActivity.findNavController(R.id.nav_host_fragment_activity_main).navigate(action)

        }
    }

}