package com.example.tango.ui.chats

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tango.model.Chat
import com.example.tango.model.Message
import com.example.tango.services.ChatService
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ChatViewModel(val chat: Chat,
                    val uid: String,
                    private val chatService: ChatService
) : ViewModel() {

    var messages = MutableLiveData<List<Message>>(emptyList())

    init {
        Log.i(javaClass.simpleName, "ChatVM w/ ID ${chat.id} initialized")
        viewModelScope.launch {
            chatService.getMessagesFlow(chat.id!!).collect() {
                Log.d(javaClass.simpleName, "${it.count()} messages found in chat ${chat.id!!}")
                messages.value = it
                println("first message is ${messages.value!![0].text}")
                println("messages count is ${messages.value!!.count()}")
            }
        }
    }

    fun observeMessages(): LiveData<List<Message>> {
        return messages
    }

}