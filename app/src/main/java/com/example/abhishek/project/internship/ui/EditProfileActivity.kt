//package com.example.abhishek.project.internship.ui
//
//import android.net.Uri
//import android.os.Bundle
//import android.widget.Toast
//import androidx.activity.enableEdgeToEdge
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.view.ViewCompat
//import androidx.core.view.WindowInsetsCompat
//import androidx.lifecycle.ViewModelProvider
//import com.example.abhishek.project.internship.R
//import com.example.abhishek.project.internship.databinding.ActivityEditProfileBinding
//import com.example.abhishek.project.internship.repositories.FirebaseHelper
//import com.example.abhishek.project.internship.repositories.UserRepository
//import com.example.abhishek.project.internship.viewmodels.EditProfileViewModel
//import kotlin.jvm.java
//
//class EditProfileActivity : AppCompatActivity() {
//
//    private lateinit var binding: ActivityEditProfileBinding
//    private lateinit var viewModel: EditProfileViewModel
//
//    private val pickImage = registerForActivityResult(
//        ActivityResultContracts.GetContent()
//    ) { uri: Uri? ->
//        uri?.let {
//            binding.profileImage.setImageURI(it)
////            viewModel.uploadProfileImage(it)
//        }
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityEditProfileBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
////        val repository = UserRepository(FirebaseHelper())
////        viewModel = ViewModelProvider(this, EditProfileVMFactory(repository))
////            .get(EditProfileViewModel::class.java)
//
//        binding.profileImage.setOnClickListener { pickImage.launch("image/*") }
//        binding.changeImageText.setOnClickListener { pickImage.launch("image/*") }
//
//        binding.saveButton.setOnClickListener {
//            val name = binding.nameInput.text.toString()
//            val email = binding.emailInput.text.toString()
//            if (name.isNotEmpty() && email.isNotEmpty()) {
//                viewModel.updateProfile(name, email)
//            } else {
//                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show()
//            }
//        }
//
////        viewModel.profileImageUrl.observe(this) { url ->
////            if (!url.isNullOrEmpty()) {
////                Glide.with(this).load(url).into(binding.profileImage)
////            }
////        }
////
////        viewModel.updateStatus.observe(this) { success ->
////            if (success) {
////                Toast.makeText(this, "Profile updated!", Toast.LENGTH_SHORT).show()
////                finish()
////            } else {
////                Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show()
////            }
////        }
//    }
//}
