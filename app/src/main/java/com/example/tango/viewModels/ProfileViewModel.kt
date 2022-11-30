package com.example.tango.viewModels

import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tango.glide.Glide
import com.example.tango.model.User
import com.example.tango.services.DataService
import com.example.tango.services.Storage
import com.google.firebase.firestore.FirebaseFirestore

class ProfileViewModel(dataService: DataService? = null, user: User? = null) : ViewModel() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val storage = Storage()
    private val dataService = DataService()
    var currentUser = MutableLiveData<User>(user)

    init {
        Log.i(javaClass.simpleName, "PROFILE VM INIT w user ${user?.id}")
        if (user?.id!=null) {
            currentUser.postValue(user!!)
            getUserInfo(user!!.id!!)
        }
    }

    fun getUserInfo(userID: String) {
        Log.i(javaClass.simpleName, "Call to get user info for ${currentUser.value?.id}")
        dataService?.fetchUserInfo(userID) {

        }
    }

    fun getProfilePic(imageView: ImageView) {
        Log.d(javaClass.simpleName, "Call to fetchProfPic")
        if (currentUser.value != null) {
            val storagePath = "users/${currentUser.value!!.id}/profilePic"
            Glide.fetch(storage.uuid2StorageReference(storagePath), imageView)
        }
    }

}