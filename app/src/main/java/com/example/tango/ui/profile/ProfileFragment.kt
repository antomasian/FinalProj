package com.example.tango.ui.profile

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.view.MenuHost
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.tango.MainActivity
import com.example.tango.R
import com.example.tango.databinding.FragmentChatsListBinding
import com.example.tango.databinding.FragmentProfileBinding
import com.example.tango.glide.Glide
import com.example.tango.ui.AuthViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog

class ProfileFragment : Fragment() {
    companion object {
        fun newInstance() = ProfileFragment()
    }

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: AuthViewModel by activityViewModels()

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

        authViewModel.getProfilePic(binding.profPicImgView) // TODO: make live data?
        authViewModel.currentUser.observe(viewLifecycleOwner) {
            binding.displayNameTV.text = it?.displayName
            binding.emailTV.text = it?.email

        }

        val mainActivity = requireActivity() as MainActivity

        binding.centerButton.setOnClickListener {
            authViewModel.signUserOut(it.context) {
                mainActivity.reloadActivity()
            }
        }

        binding.addPicButton.setOnClickListener {
            Log.i(javaClass.simpleName, "Change pic pressed")

            val dialog = BottomSheetDialog(mainActivity, R.style.BottomSheetDialog)
            val dialogView = layoutInflater.inflate(R.layout.change_pic_sheet, null)
            dialogView.setBackgroundResource(R.drawable.rounded_dialog)
            dialog.setContentView(dialogView)
            dialog.show()

//            val choosePhotoIntent = Intent(Intent.ACTION_PICK)
//            choosePhotoIntent.type = "image/*"
//            imageGalleryLauncher.launch(choosePhotoIntent)

//            val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//            val photoFile = mainActivity.getPhotoFile("photo.jpeg")
//            val providerFile = FileProvider.getUriForFile(mainActivity, "com.example.tango", photoFile)
//            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, providerFile)
//            cameraLauncher.launch(takePhotoIntent)
        }
    }

}