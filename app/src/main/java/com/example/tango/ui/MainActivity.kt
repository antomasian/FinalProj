package com.example.tango.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.tango.R
import com.example.tango.databinding.ActivityMainBinding
import com.example.tango.viewModels.AuthUserViewModel
import com.example.tango.ui.onboarding.LoadingFragmentDirections
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val authVM: AuthUserViewModel by viewModels()

    private val signInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            authVM.getUserInfo { isNewUser ->
                if (isNewUser) {
                    Log.i(javaClass.simpleName, "new user")
                    onboardUser()
                } else {
                    Log.i(javaClass.simpleName, "existing user")
                }
            }
        } else {
            Log.e(javaClass.simpleName, "Error signing in")
        }
    }

    fun getPhotoFile(filename: String): File {
        val directoryStorage = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(filename, ".jpg", directoryStorage)
    }

    fun onboardUser() {
        val action = LoadingFragmentDirections.actionLoadingFragmentToOnboardingFragment()
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        navController.navigate(action)
    }

    fun reloadActivity() {
        recreate()
    }

    fun createNewUser(user: com.example.tango.model.User) {
        authVM.createNewUser(user) { success ->
            if (success) {
                Log.i(javaClass.simpleName, "success creating user")
                finish()
                val intent = Intent(this, BottomNavActivity::class.java)
                startActivity(intent)
            } else {
                // TODO: show alert
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val navController: NavController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        authVM.signUserIn(signInLauncher)

        authVM.observeCurrentUser().observe(this) {
            if (it != null) {
                authVM.getUserInfo { isNewUser ->
                    if (!isNewUser) {
                        val intent = Intent(this, BottomNavActivity::class.java)
                        startActivity(intent)
                    } else {
                        onboardUser()
                    }
                }
            } else {

            }
        }

    }
}