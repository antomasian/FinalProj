package com.example.tango.ui.onboarding

import android.os.Bundle
import android.os.PersistableBundle
import androidx.fragment.app.FragmentActivity
import com.example.tango.databinding.FragmentOnboardingBinding


class OnboardingFragment: FragmentActivity() {
    private lateinit var binding: FragmentOnboardingBinding

    companion object {
        fun newInstance() = OnboardingFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        binding = FragmentOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }



}