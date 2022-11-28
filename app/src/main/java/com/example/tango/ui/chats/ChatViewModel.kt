package com.example.tango.ui.chats

import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tango.glide.Glide
import com.example.tango.model.Chat
import com.example.tango.model.Message
import com.example.tango.model.User
import com.example.tango.services.ChatService
import com.example.tango.services.Storage
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ChatViewModel(val chat: Chat,
                    val uid: String,
                    private val chatService: ChatService
) : ViewModel() {

    var messages = MutableLiveData<List<Message>>(emptyList())
    var chattingUser = MutableLiveData<User?>(null)
    private val storage = Storage() // TODO: Pass in

    init {
        Log.i(javaClass.simpleName, "ChatVM w/ ID ${chat.id} initialized")
        fetchChattingUser()
        viewModelScope.launch {
            chatService.getMessagesFlow(chat.id!!).collect() { it ->
                Log.d(javaClass.simpleName, "${it.count()} messages found in chat ${chat.id!!}")
                messages.value = it
            }
        }
    }

    private fun fetchChattingUser() {
        Log.d(javaClass.simpleName, "Call to fetchChattingUser")
        // TODO: fix the actual chat docs to make sense w new uids
        val chatUserID = chat.chattingUserIDs.filter { it != uid }[0]
        chatService.fetchChattingUser(chatUserID) { user ->
            Log.i(javaClass.simpleName, "chattingUser is ${user?.displayName}")
            chattingUser.postValue(user)
        }

    }

    fun fetchProfilePic(uid: String = "iC60h19LByDlsY75g2Bl", imageView: ImageView) {
        Log.d(javaClass.simpleName, "Call to fetchProfPic")
        val storagePath = "users/$uid/profilePic"
        Glide.fetch(storage.uuid2StorageReference(storagePath), imageView)
    }

    fun observeMessages(): LiveData<List<Message>> {
        return messages
    }

    fun sendMessage(text: String) {
        val recipientID = chat.chattingUserIDs.filter { it != uid }[0]
        Log.i(javaClass.simpleName,"recipientID is $recipientID")
        val message = Message(uid, "", recipientID, text)
        chatService.createMessageDoc(chat.id, message)
    }

}