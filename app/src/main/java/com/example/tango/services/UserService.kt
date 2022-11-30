package com.example.tango.services

import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import com.example.tango.glide.Glide
import com.example.tango.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserService {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val storage = Storage()

    fun fetchUser(uid: String, callback: (User?, Boolean)->Unit) { // return user, isNewUser
        Log.i(javaClass.simpleName, "Call to fetch user doc $uid")
        db.collection("users").document(uid)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (!documentSnapshot.exists()) {
                    callback(null, true)
                } else {
                    val user = documentSnapshot.toObject(User::class.java)
                    Log.i(javaClass.simpleName, "fetched user $user")
                    Log.i(javaClass.simpleName, "user display name is ${user?.displayName}")
                    callback(user, false)
                }
            }
    }

    fun createUserDoc(user: User, uid: String, callback: (Boolean) -> Unit) {
        Log.i(javaClass.simpleName, "Call to create user doc w id $uid")
        db.collection("users")
            .document(uid)
            .set(user)
            .addOnSuccessListener {
                Log.d(javaClass.simpleName, "created user doc")
                callback(true)
            }
    }

}