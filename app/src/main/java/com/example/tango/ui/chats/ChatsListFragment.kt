package com.example.tango.ui.chats

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commitNow
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalproj.ui.chats.ChatRowAdapter
import com.example.tango.MainActivity
import com.example.tango.R
import com.example.tango.databinding.FragmentChatsListBinding

class ChatsListFragment : Fragment() {
    private var _binding: FragmentChatsListBinding? = null
    private val binding get() = _binding!!
    private val chatsListVM: ChatsListViewModel by activityViewModels()

    companion object {
        fun newInstance() = ChatsListFragment()
    }

    private lateinit var viewModel: ChatsListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChatsListBinding.inflate(inflater, )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // init adapter
        val layoutManager = LinearLayoutManager(binding.root.context)
        binding.recyclerView.layoutManager = layoutManager
        val mainActivity = requireActivity() as MainActivity
        val adapter = ChatRowAdapter(chatsListVM, mainActivity)
        binding.recyclerView.adapter = adapter

        chatsListVM.observeChatsListVMs().observe(viewLifecycleOwner) { chatVMsList ->
            adapter.submitList(chatVMsList)
        }

    }

}