package com.example.tango.ui.chats

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalproj.ui.chats.ChatRowAdapter
import com.example.tango.ui.BottomNavActivity
import com.example.tango.databinding.FragmentChatsListBinding
import com.example.tango.viewModels.ChatsListViewModel

class ChatsListFragment : Fragment() {
    private var _binding: FragmentChatsListBinding? = null
    private val binding get() = _binding!!
    private val chatsListVM: ChatsListViewModel by activityViewModels()
//    private val authViewModel: AuthViewModel by activityViewModels()
//    private val chatsListVM = authViewModel.chatsListViewModel

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
        val mainActivity = requireActivity() as BottomNavActivity
        val adapter = ChatRowAdapter(chatsListVM, mainActivity)
        binding.recyclerView.adapter = adapter

        chatsListVM.observeChatsListVMs().observe(viewLifecycleOwner) { chatVMsList ->
            adapter.submitList(chatVMsList)
        }

    }

}