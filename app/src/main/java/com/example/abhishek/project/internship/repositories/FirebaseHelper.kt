package com.example.abhishek.project.internship.repositories

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.FirebaseStorage

class FirebaseHelper private constructor() {

    private val storageRef = FirebaseStorage.getInstance().reference
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    companion object {
        private var instance: FirebaseHelper? = null
        fun getInstance(): FirebaseHelper {
            if (instance == null) instance = FirebaseHelper()
            return instance!!
        }
    }

    fun login(email: String, password: String): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                result.value = task.isSuccessful
            }
        return result
    }

    fun getCurrentUser(): LiveData<FirebaseUser?> {
        val currentUser = MutableLiveData<FirebaseUser?>()
        currentUser.value = auth.currentUser
        return currentUser
    }

    fun register(fullName: String, email: String, password: String): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val profileUpdates = com.google.firebase.auth.UserProfileChangeRequest.Builder()
                        .setDisplayName(fullName)
                        .build()
                    user?.updateProfile(profileUpdates)
                    result.value = true
                } else {
                    result.value = false
                }
            }
        return result
    }

    fun loginWithGoogle(idToken: String): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                result.value = task.isSuccessful
            }
        return result
    }

    fun getCurrentUserName(): String {
        return auth.currentUser?.displayName ?: "User"
    }

    fun getCurrentUserEmail(): String {
        return auth.currentUser?.email ?: ""
    }


    fun uploadProfileImage(uri: Uri, callback: (Boolean, String?) -> Unit) {
        if (auth.currentUser == null) {
            callback(false, null)
            return
        }

        val fileRef = storageRef.child("profile_images/${auth.currentUser?.uid}.jpg")
        val uploadTask = fileRef.putFile(uri)

        uploadTask.addOnSuccessListener {
            fileRef.downloadUrl.addOnSuccessListener { downloadUri ->
                callback(true, downloadUri.toString())
            }
        }.addOnFailureListener {
            callback(false, null)
        }
    }

//    fun updateUserProfile(name: String, email: String, onResult: (Boolean) -> Unit) {
//        val userId = auth.currentUser?.uid ?: return onResult(false)
//        val updates = mapOf("name" to name, "email" to email)
//        db.collection("users").document(userId)
//            .update(updates)
//            .addOnSuccessListener { onResult(true) }
//            .addOnFailureListener { onResult(false) }
//    }

    fun getProfileImageUrl(callback: (String?) -> Unit) {
        if (auth.currentUser == null) {
            callback(null)
            return
        }

        val fileRef = storageRef.child("profile_images/${auth.currentUser?.uid}.jpg")
        fileRef.downloadUrl
            .addOnSuccessListener { uri -> callback(uri.toString()) }
            .addOnFailureListener { callback(null) }
    }

    fun logoutAuth() {
        auth.signOut()
    }
}
