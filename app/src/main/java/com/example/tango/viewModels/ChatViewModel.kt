package com.example.tango.viewModels

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
import kotlinx.coroutines.launch

class ChatViewModel(val chat: Chat,
                    val uid: String,
                    private val chatService: ChatService,
                    private val profilesListVM: ProfilesListViewModel
) : ViewModel() {
    var messages = MutableLiveData<List<Message>>(emptyList())
    var chattingProfileVM = MutableLiveData<ProfileViewModel?>(null)
    private var chatUserID = MutableLiveData<String?>(null)
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
        chatUserID.postValue(chat.chattingUserIDs.filter { it != uid }[0])
        val chatuserid = chat.chattingUserIDs.filter { it != uid }[0]
//        if (chatUserID.value != null) {
            profilesListVM.getUser(chatuserid, chattingProfileVM)
//        }
    }

    fun getProfilePic(imageView: ImageView) {
        Log.d(javaClass.simpleName, "Call to fetchProfPic")
        val storagePath = "users/${chatUserID.value}/profilePic"
        Glide.fetch(storage.uuid2StorageReference(storagePath), imageView)
    }

    fun observeMessages(): LiveData<List<Message>> {
        return messages
    }

    fun observeChattingProfileVM(): LiveData<ProfileViewModel?> {
        return chattingProfileVM
    }

    fun sendMessage(text: String) {
        val recipientID = chat.chattingUserIDs.filter { it != uid }[0]
        Log.i(javaClass.simpleName,"recipientID is $recipientID")
        val message = Message(uid, "", recipientID, text)
        chatService.createMessageDoc(chat.id, message)
    }

}