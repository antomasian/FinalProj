package com.example.tango.ui.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tango.ui.MainActivity
import com.example.tango.databinding.FragmentOnboardingBinding
import com.example.tango.model.User
import java.util.*

class OnboardingFragment : Fragment() {
    private var _binding: FragmentOnboardingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOnboardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainActivity = requireActivity() as MainActivity
        mainActivity.supportActionBar?.setDisplayHomeAsUpEnabled(false)

        binding.creatAcctButton.setOnClickListener {
            val fullName = binding.fullNameET.text.toString()
            val displayName = binding.displayNameET.text.toString()
            val email = binding.emailET.text.toString()
            var birthday = Date()
            birthday.year = binding.datePicker.year
            birthday.month = binding.datePicker.month
            birthday.date = binding.datePicker.dayOfMonth
            var user = User(fullName, displayName, birthday, email)
            println(birthday)
            mainActivity.createNewUser(user)
        }
    }

}