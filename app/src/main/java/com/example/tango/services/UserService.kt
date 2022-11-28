package com.example.tango.services

import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import com.example.tango.glide.Glide
import com.example.tango.model.User
import com.google.firebase.firestore.FirebaseFirestore

class UserService {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val storage = Storage()
    private var userID: String? = null

    fun fetchUser(uid: String, callback: (User?, Boolean)->Unit) { // return user, isNewUser
        userID = uid
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

    fun fetchAvailableUsers(usersList: MutableLiveData<List<User>>) {
        db.collection("users")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val users = querySnapshot.documents.mapNotNull {
                    it.toObject(User::class.java)
                }
                Log.i(javaClass.simpleName, "Found ${users.count()} available users")
                usersList.postValue(users)
            }
    }

    fun fetchProfilePic(imageView: ImageView) {
        Log.d(javaClass.simpleName, "Call to fetchProfPic")
        val storagePath = "users/$userID/profilePic"
        Glide.fetch(storage.uuid2StorageReference(storagePath), imageView)
    }

}