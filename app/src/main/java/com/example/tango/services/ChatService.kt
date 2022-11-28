package com.example.tango.services

import android.util.Log
import com.example.tango.model.Chat
import com.example.tango.model.Message
import com.example.tango.model.User
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
//                        Log.e(javaClass.simpleName, "Error fetching collection data at path - $path", error)
                        return@addSnapshotListener
                    }
                    trySend(querySnapshot)
                }
            awaitClose {
//                Log.d(javaClass.simpleName, "Cancelling the listener at collection path - $path")
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
        return db.collection("chats/$chatID/messages") // collection ref
            .orderBy("createdTime")
            .getDataFlow { querySnapshot ->
                querySnapshot?.documents?.map {
                    it.toObject(Message::class.java)!!
                } ?: listOf()
            }
    }

    fun fetchChattingUser(uid: String, callback: (User?)->Unit) {
        Log.i(javaClass.simpleName, "Call to fetch user doc $uid")
        db.collection("users").document(uid)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val user = documentSnapshot.toObject(User::class.java)
                Log.i(javaClass.simpleName, "fetched user $user")
                callback(user)
            }
            .addOnFailureListener {
                Log.i(javaClass.simpleName, "chatting user $uid not found")
                callback(null)
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