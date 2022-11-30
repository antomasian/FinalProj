package com.example.tango.ui.chats

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tango.ui.BottomNavActivity
import com.example.tango.databinding.FragmentChatBinding
import com.example.tango.viewModels.ChatsListViewModel

class ChatFragment : Fragment() {
    private var _binding: FragmentChatBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val chatsListVM: ChatsListViewModel by activityViewModels()
    private val args: ChatFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mainActivity = requireActivity() as BottomNavActivity
        mainActivity.setBottomNavigationVisibility(View.GONE)
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val chatVM = chatsListVM.chatViewModels.value!![args.index]

        val layoutManager = LinearLayoutManager(binding.root.context)
        binding.messagesRV.layoutManager = layoutManager
        val adapter = MessageRowAdapter(chatVM)
        binding.messagesRV.adapter = adapter


        chatVM.observeMessages().observe(viewLifecycleOwner) { messages ->
            Log.d(javaClass.simpleName, "Submitting ${messages.count()} messages")
            adapter.submitList(messages)
            val count = adapter.itemCount
        }

        binding.sendButton.setOnClickListener {
            if (binding.messageET.text.isNotEmpty()) {
                chatVM.sendMessage(binding.messageET.text.toString())
            }
        }

        binding.messagesRV.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            binding.messagesRV.scrollToPosition(adapter.itemCount-1)
        }

    }

override fun onDestroyView() {
        super.onDestroyView()
    val mainActivity = requireActivity() as BottomNavActivity
    mainActivity.setBottomNavigationVisibility(View.VISIBLE)
    _binding = null
    }
}