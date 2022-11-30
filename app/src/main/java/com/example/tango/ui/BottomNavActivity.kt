package com.example.tango.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commitNow
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.tango.R
import com.example.tango.databinding.ActivityBottomNavBinding
import com.example.tango.databinding.FragmentHomeBinding
import com.example.tango.ui.home.HomeFragmentDirections
import com.example.tango.ui.profile.ProfileFragment
import com.example.tango.viewModels.AuthUserViewModel
import com.example.tango.viewModels.ChatsListViewModel
import com.example.tango.viewModels.ProfilesListViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class BottomNavActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityBottomNavBinding
    private lateinit var bottomNavView: BottomNavigationView
    private lateinit var navController: NavController
    private val authVM: AuthUserViewModel by viewModels()
    private val profilesListViewModel: ProfilesListViewModel by viewModels()
    private val chatsListViewModel: ChatsListViewModel by viewModels()

    fun setBottomNavigationVisibility(visibility: Int) {
        binding.bottomNavView.visibility = visibility
    }

    fun signOut() {
        Log.i(javaClass.simpleName, "Call to sign out")
        authVM.signUserOut(this) {
            finish()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    fun launchProfileFragment() {
        val action = HomeFragmentDirections.actionHomeFragToProfileFrag()
        navController.navigate(action)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBottomNavBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        bottomNavView = binding.bottomNavView
        navController = findNavController(R.id.bottom_nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_chats, R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNavView.setupWithNavController(navController)
        setBottomNavigationVisibility(View.VISIBLE)

        authVM.observeCurrentUser().observe(this) { firebaseUser ->
            if (firebaseUser!=null) {
                authVM.getUserInfo() { }
                chatsListViewModel.loadChats()
                profilesListViewModel.fetchAvailableUsers()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

}