package com.example.tango.ui

import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.ViewModel
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AuthViewModel(): ViewModel() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

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
        } else {
            Log.d(javaClass.simpleName, "Logged in as ${user!!.displayName}, email ${user!!.email}")
        }
    }

    fun getProfilePic(uid: String) {

    }
}