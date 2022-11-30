package com.example.tango.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tango.model.User
import com.example.tango.services.DataService
import com.google.firebase.firestore.FirebaseFirestore

class ProfilesListViewModel(): ViewModel() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val dataService = DataService()
    private val profileVMs = MutableLiveData<List<ProfileViewModel>>(emptyList())

    fun fetchAvailableUsers() {
        db.collection("users")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val profiles = querySnapshot.documents.mapNotNull {
                    ProfileViewModel(user=it.toObject(User::class.java))
                }
                Log.i(javaClass.simpleName, "Found ${profiles.count()} available users")
                profileVMs.postValue(profiles)
            }
    }

    fun getUser(userID: String, profileViewModel: MutableLiveData<ProfileViewModel?>) {
        Log.i(javaClass.simpleName, "Call to get user")
        val profileIDs = profileVMs.value!!.map { it.currentUser?.value?.id }
        if (profileIDs.contains(userID)) {
            // if we already have the profile, return it
            Log.i(javaClass.simpleName, "HERE")
            val index = profileIDs.indexOfFirst { it == userID }
            profileViewModel.postValue(profileVMs.value!![index])
        } else {
            // fetch it
            dataService.fetchUserInfo(userID) { user ->
                val profileVM = ProfileViewModel(user=user)
                // TODO: append
                profileViewModel.postValue(profileVM)
            }
        }

    }

    fun observeProfiles(): LiveData<List<ProfileViewModel>> {
        return profileVMs
    }

}