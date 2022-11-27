package com.example.tango.services

import android.util.Log
import com.example.tango.model.Chat
import com.example.tango.model.Message
import com.google.firebase.firestore.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*

class ChatService {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    @ExperimentalCoroutinesApi
    fun CollectionReference.getQuerySnapshotFlow(): Flow<QuerySnapshot?> {
        return callbackFlow {
            val listenerRegistration =
                addSnapshotListener { querySnapshot, error ->
                    if (error != null) {
                        Log.e(javaClass.simpleName, "Error fetching collection data at path - $path", error)
                        return@addSnapshotListener
                    }
                    trySend(querySnapshot)
                }
            awaitClose {
                Log.d(javaClass.simpleName, "Cancelling the listener at collection path - $path")
                listenerRegistration.remove()
            }
        }
    }

    @ExperimentalCoroutinesApi
    fun <T> CollectionReference.getDataFlow(mapper: (QuerySnapshot?) -> T): Flow<T> {
        return getQuerySnapshotFlow()
            .map {
                return@map mapper(it)
            }
    }

    @ExperimentalCoroutinesApi
    fun getChatsFlow(): Flow<List<Chat>> {
        Log.i(javaClass.simpleName, "Call to getChatsListFlow")
        return db.collection("chats")
            .getDataFlow { querySnapshot ->
                querySnapshot?.documents?.map {
                    it.toObject(Chat::class.java)!!
                } ?: listOf()
            }
    }

    // return chats from above, store in a set
    // when set gets a new member, create a new key in a dictionary & fetch messages
    // fetch profile of chatting user
    @ExperimentalCoroutinesApi
    fun getMessagesFlow(chatID: String): Flow<List<Message>> {
        Log.i(javaClass.simpleName, "Call to get messages for chat $chatID")
        return db.collection("chats/$chatID/messages")
            .getDataFlow { querySnapshot ->
                querySnapshot?.documents?.map {
                    it.toObject(Message::class.java)!!
                } ?: listOf()
            }
    }

    fun fetchChattingUser() {

    }

}