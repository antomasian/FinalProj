package com.example.tango

import android.app.Activity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.tango.databinding.ActivityMainBinding
import com.example.tango.ui.AuthViewModel
import com.example.tango.ui.chats.ChatsListViewModel
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var bottomNavView: BottomNavigationView
    private lateinit var binding: ActivityMainBinding
    private val authVM: AuthViewModel by viewModels()
    private val chatsListViewModel: ChatsListViewModel by viewModels()

    private val signInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            authVM.getUserInfo { isNewUser ->
                if (isNewUser) {
                    Log.i(javaClass.simpleName, "new user")
                } else {
                    Log.i(javaClass.simpleName, "existing user")
                    chatsListViewModel.loadChats()
                }
            }
        } else {
            // sign in failed
        }
    }

    fun setBottomNavigationVisibility(visibility: Int) {
        binding.navView.visibility = visibility
    }

    fun getPhotoFile(filename: String): File {
        val directoryStorage = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(filename, ".jpg", directoryStorage)
    }

    fun reloadActivity() {
        recreate()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottomNavView = binding.navView
        navController = findNavController(R.id.nav_host_fragment_activity_main)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_chats, R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNavView.setupWithNavController(navController)

        authVM.signUserIn(signInLauncher)

        authVM.observeCurrentUser().observe(this) {
            if (it != null) {
                authVM.getUserInfo { isNewUser ->
                    if (!isNewUser) {
                        chatsListViewModel.loadChats()
                    }
                }
            } else {

            }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}