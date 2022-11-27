package com.example.tango.ui.chats

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tango.R
import com.example.tango.databinding.MessageRowBinding
import com.example.tango.model.Message

class MessageRowAdapter(private val viewModel: ChatViewModel)
    : ListAdapter<Message, MessageRowAdapter.VH>(MessageDiff()) {
    class MessageDiff : DiffUtil.ItemCallback<Message>() {
        override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem.id == newItem.id
        }
        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem == newItem
        }
    }

    inner class VH(val messageRowBinding: MessageRowBinding)
        : RecyclerView.ViewHolder(messageRowBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = MessageRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val binding = holder.messageRowBinding
        val item = getItem(position)
        println("Applying: ${item.text}")


        println("${item.senderID}, ${viewModel.uid}")
        val isMyMessage = (item.senderID == viewModel.uid)

        binding.apply {
            binding.messageTV.text = item.text
            if (!isMyMessage) {
                binding.spacer.visibility = View.GONE
                binding.messageTV.setTextColor(Color.BLACK)
                binding.messageTV.setBackgroundResource(R.drawable.rounded_rectangle1)
            }

        }
    }

}