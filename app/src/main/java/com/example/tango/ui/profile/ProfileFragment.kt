package com.example.tango.ui.profile

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.example.tango.ui.BottomNavActivity
import com.example.tango.R
import com.example.tango.databinding.FragmentProfileBinding
import com.example.tango.viewModels.AuthUserViewModel
import com.example.tango.viewModels.ProfileViewModel
import com.example.tango.viewModels.ProfilesListViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog

class ProfileFragment() : Fragment() {
    companion object {
        fun newInstance() = ProfileFragment()
    }

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val profilesListViewModel: ProfilesListViewModel by activityViewModels()
    private val authViewModel: AuthUserViewModel by activityViewModels()

    private val args: ProfileFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, )
        return binding.root
    }

    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
//            viewModel.pictureSuccess()
        } else {
//            viewModel.pictureFailure()
        }
    }

    private val imageGalleryLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val bottomNavActivity = requireActivity() as BottomNavActivity

        if (args.isMyProfile) { // is my profile
            authViewModel.getProfilePic(binding.profPicImgView) // TODO: make live data?
            authViewModel.currentUser.observe(viewLifecycleOwner) {
                println(it?.displayName)
                print(it?.email)
                binding.displayNameTV.text = it?.displayName
                binding.emailTV.text = it?.email
            }
            binding.centerButton.setOnClickListener {
                bottomNavActivity.signOut()
            }
            binding.addPicButton.setOnClickListener {
                val dialog = BottomSheetDialog(bottomNavActivity, R.style.BottomSheetDialog)
                val dialogView = layoutInflater.inflate(R.layout.change_pic_sheet, null)
                dialogView.setBackgroundResource(R.drawable.rounded_dialog)
                dialog.setContentView(dialogView)
                dialog.show()
            }
        } else {

        ////            profileViewModel.getProfilePic(binding.profPicImgView)
//            binding.displayNameTV.text = profileViewModel?.currentUser?.value?.displayName
//            binding.emailTV.text = profileViewModel?.currentUser?.value?.email

        }
    }
}

//            val choosePhotoIntent = Intent(Intent.ACTION_PICK)
//            choosePhotoIntent.type = "image/*"
//            imageGalleryLauncher.launch(choosePhotoIntent)

//            val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//            val photoFile = bottomNavActivity.getPhotoFile("photo.jpeg")
//            val providerFile = FileProvider.getUriForFile(bottomNavActivity, "com.example.tango", photoFile)
//            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, providerFile)
//            cameraLauncher.launch(takePhotoIntent)