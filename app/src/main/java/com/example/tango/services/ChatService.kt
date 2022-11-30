package com.example.tango.services

import android.util.Log
import com.example.tango.model.Chat
import com.example.tango.model.Message
import com.example.tango.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*

class ChatService {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    @ExperimentalCoroutinesApi
    fun Query.getQuerySnapshotFlow(): Flow<QuerySnapshot?> {
        return callbackFlow {
            val listenerRegistration =
                addSnapshotListener { querySnapshot, error ->
                    if (error != null) {
                        return@addSnapshotListener
                    }
                    trySend(querySnapshot)
                }
            awaitClose {
                listenerRegistration.remove()
            }
        }
    }

    @ExperimentalCoroutinesApi
    fun <T> Query.getDataFlow(mapper: (QuerySnapshot?) -> T): Flow<T> {
        return getQuerySnapshotFlow()
            .map {
                return@map mapper(it)
            }
    }

    @ExperimentalCoroutinesApi
    fun getChatsFlow(): Flow<List<Chat>> {
        Log.i(javaClass.simpleName, "Call to getChatsListFlow")
        val uid = FirebaseAuth.getInstance().uid as String
        return db.collection("chats")
            .whereArrayContains("chattingUserIDs", uid)
            .getDataFlow { querySnapshot ->
                querySnapshot?.documents?.map {
                    it.toObject(Chat::class.java)!!
                } ?: listOf()
            }
    }

    @ExperimentalCoroutinesApi
    fun getMessagesFlow(chatID: String): Flow<List<Message>> {
        Log.i(javaClass.simpleName, "Call to get messages for chat $chatID")
        return db.collection("chats/$chatID/messages") // collection ref
            .orderBy("createdTime")
            .getDataFlow { querySnapshot ->
                querySnapshot?.documents?.map {
                    it.toObject(Message::class.java)!!
                } ?: listOf()
            }
    }

    fun createMessageDoc(chatID: String?, message: Message) {
        if (chatID != null) {
            db.collection("chats/$chatID/messages")
                .add(message)
                .addOnSuccessListener { docRef ->
                    Log.i(javaClass.simpleName, "Added message doc")
                }
        } else {
            Log.i(javaClass.simpleName, "chatID null")
        }
    }
}