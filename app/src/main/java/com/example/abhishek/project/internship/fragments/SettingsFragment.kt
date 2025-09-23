package com.example.abhishek.project.internship.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
//import com.bumptech.glide.R
import com.example.abhishek.project.internship.databinding.FragmentSettingsBinding
import com.example.abhishek.project.internship.registeration.LoginActivity
//import com.example.abhishek.project.internship.ui.EditProfileActivity
import com.example.abhishek.project.internship.viewmodels.SettingsViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SettingsViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.userName.text = viewModel.getUserName()
        binding.emailRow.text = viewModel.getUserEmail()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.darkMode.collectLatest { isDarkMode ->
                binding.darkModeSwitch.isChecked = isDarkMode
                AppCompatDelegate.setDefaultNightMode(
                    if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES
                    else AppCompatDelegate.MODE_NIGHT_NO
                )
            }
        }


        binding.darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setDarkMode(isChecked)
            Toast.makeText(
                requireContext(),
                "Dark mode ${if (isChecked) "enabled" else "disabled"}",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.logoutButton.setOnClickListener {
            lifecycleScope.launch {
                viewModel.logout()
                Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(requireContext(), LoginActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
        }


        // Change Password
        binding.changePasswordRow.setOnClickListener {
            val action = viewModel.onChangePasswordClicked()
        }


        binding.editProfileRow.setOnClickListener {
            val action = viewModel.onEditProfileClicked()
        }

        viewModel.profileImageUrl.observe(viewLifecycleOwner) { url ->
            if (!url.isNullOrEmpty()) {
                Glide.with(this).load(url).into(binding.profileImage)
            } else {
//                binding.profileImage.setImageDrawable(R.id.)
            }
        }

        binding.profileImage.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        viewModel.action.observe(viewLifecycleOwner) { action ->
            Toast.makeText(requireContext(), "$action clicked", Toast.LENGTH_SHORT).show()
            when (action) {
                "Edit Profile" -> {
//                    startActivity(Intent(requireContext(), EditProfileActivity::class.java))
                }
                "Change Password" -> { /* navigate */ }

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.uploadProfileImage(it) }
    }


}
