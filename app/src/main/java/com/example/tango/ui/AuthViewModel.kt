package com.example.tango.ui

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tango.model.User
import com.example.tango.services.UserService
import com.example.tango.ui.chats.ChatsListViewModel
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class AuthViewModel(): ViewModel() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val userService = UserService()
    val usersList = MutableLiveData<List<User>>(emptyList())
    var currentUser = MutableLiveData<User?>(null)
    var currentFirebaseUser = MutableLiveData<FirebaseUser?>(null)

    init {
//        userService.fetchAvailableUsers(usersList)
        FirebaseAuth.getInstance().addAuthStateListener {
            currentFirebaseUser.postValue(FirebaseAuth.getInstance().currentUser)
        }
    }

    fun observeCurrentUser(): LiveData<FirebaseUser?> {
        return currentFirebaseUser
    }

    fun signUserIn(signInLauncher: ActivityResultLauncher<Intent>) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            Log.d(javaClass.simpleName, "No current user")
            val providers = arrayListOf(
                AuthUI.IdpConfig.EmailBuilder().build(),
                AuthUI.IdpConfig.GoogleBuilder().build()
            )
            val signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setIsSmartLockEnabled(false)
                .build()
            signInLauncher.launch(signInIntent)
        }
    }

    fun getUserInfo(callback: (Boolean) -> Unit) {
        val user = FirebaseAuth.getInstance().currentUser
        Log.d(javaClass.simpleName, "Logged in as ${user!!.displayName}, email ${user!!.email}")
        userService.fetchUser(user!!.uid) { user, isNewUser ->
            if (isNewUser) {
                callback(true)
            } else {
                callback(false)
            }
            if (user != null) {
                currentUser.postValue(user)
            }
        }
    }

    fun signUserOut(context: Context, callback: (Boolean)->Unit) {
        FirebaseAuth.getInstance().signOut()
        AuthUI.getInstance().signOut(context)
            .addOnSuccessListener {
                callback(true)
            }
            .addOnFailureListener {
                Log.i(javaClass.simpleName, "FAILED TO LOG OUT")
            }
    }

    fun getProfilePic(imageView: ImageView) {
        userService.fetchProfilePic(imageView)
    }
}