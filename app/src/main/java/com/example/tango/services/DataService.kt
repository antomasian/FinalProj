package com.example.tango.services

import android.util.Log
import com.example.tango.model.User
import com.google.firebase.firestore.FirebaseFirestore

class DataService {
    private val db = FirebaseFirestore.getInstance()

    fun fetchUserInfo(userID: String, callback: (User?)->Unit) {
        db.collection("users").document(userID)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val user = documentSnapshot.toObject(User::class.java)
                Log.i(javaClass.simpleName, "fetched user $user")
                Log.i(javaClass.simpleName, "user display name is ${user?.displayName}")
                callback(user)
            }
    }


}