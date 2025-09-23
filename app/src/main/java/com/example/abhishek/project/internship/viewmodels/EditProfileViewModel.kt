//package com.example.abhishek.project.internship.viewmodels
//
//class EditProfileViewModel(private val repository: UserRepository) : ViewModel() {
//
//    private val _profileImageUrl = MutableLiveData<String?>()
//    val profileImageUrl: LiveData<String?> get() = _profileImageUrl
//
//    private val _updateStatus = MutableLiveData<Boolean>()
//    val updateStatus: LiveData<Boolean> get() = _updateStatus
//
//    fun uploadProfileImage(uri: Uri) {
//        repository.uploadProfileImage(uri) { success, url ->
//            if (success && url != null) _profileImageUrl.value = url
//        }
//    }
//
//    fun updateProfile(name: String, email: String) {
//        repository.updateProfile(name, email) { success ->
//            _updateStatus.value = success
//        }
//    }
//}
