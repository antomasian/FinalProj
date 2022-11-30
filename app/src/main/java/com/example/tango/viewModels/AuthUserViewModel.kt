package com.example.tango.viewModels

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
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class AuthUserViewModel(): ViewModel() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val userService = UserService()
    var currentUser = MutableLiveData<User?>(null)
    var currentFirebaseUser = MutableLiveData<FirebaseUser?>(null)
    private val profileViewModel = ProfileViewModel()

    init {
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

    fun createNewUser(user: User, callback: (Boolean) -> Unit) {
        userService.createUserDoc(user, currentFirebaseUser.value!!.uid) { success ->
            callback(true)
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

    fun getProfilePic(imageView: ImageView) {
        // TODO: change
        profileViewModel.currentUser.postValue(currentUser.value)
        profileViewModel.getProfilePic(imageView)
    }

    fun signUserOut(context: Context, callback: (Boolean)->Unit) {
        Log.i(javaClass.simpleName, "Call to sign user out")
        AuthUI.getInstance().signOut(context)
            .addOnSuccessListener {
                Log.i(javaClass.simpleName, "SUCCESS SIGN OUT")
                callback(true)
            }
            .addOnFailureListener {
                Log.i(javaClass.simpleName, "FAILED SIGN OUT")
                callback(false)
            }
    }
}