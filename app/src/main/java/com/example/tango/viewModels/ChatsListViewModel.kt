package com.example.tango.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tango.services.ChatService
import com.google.firebase.auth.FirebaseAuth

class ChatsListViewModel : ViewModel() {
    private val chatService = ChatService()
    var chatViewModels = MutableLiveData<List<ChatViewModel>>(emptyList())

    //TODO: profilesListVM must be init
    private val profilesListVM = ProfilesListViewModel()

    fun loadChats() {
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        viewModelScope.launch {
            chatService.getChatsFlow().collect { chats ->
                Log.d(javaClass.simpleName, "Received ${chats.count()} chats")
                for (chat in chats) {
                    val existingChatIDs = chatViewModels.value!!.map { it.chat.id }
                    if (!existingChatIDs.contains(chat.id)) {
                        // new chat
                        var updatedChatsVMs = chatViewModels.value!!.toMutableList()
                        val newChatVM = ChatViewModel(chat, uid, chatService, profilesListVM)
                        updatedChatsVMs.add(newChatVM)
                        chatViewModels.value = updatedChatsVMs
                    }
                }
            }
        }
    }

    fun observeChatsListVMs(): LiveData<List<ChatViewModel>> {
        return chatViewModels
    }

}

