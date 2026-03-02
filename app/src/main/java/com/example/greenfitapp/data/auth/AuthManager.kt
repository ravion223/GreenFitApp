package com.example.greenfitapp.data.auth

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.auth
import com.google.firebase.auth.userProfileChangeRequest


object AuthManager {
    private val auth: FirebaseAuth = Firebase.auth

    fun isUserLoggedIn(): Boolean = auth.currentUser != null

    fun getCurrentUser() = auth.currentUser

    fun signUp(email: String, password: String, userName: String, onComplete: (Boolean) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    val profileUpdates = userProfileChangeRequest {
                        displayName = userName
                    }

                    auth.currentUser?.updateProfile(profileUpdates)
                        ?.addOnCompleteListener { profileTask ->
                            onComplete(profileTask.isSuccessful)
                        }
                } else {
                    onComplete(false)
                }
            }
    }

    fun signIn(email: String, password: String, onComplete: (Boolean) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                onComplete(task.isSuccessful)
            }
    }

    fun logout(){
        auth.signOut()
    }

    fun getCurrentUserName(): String? {
        return auth.currentUser?.displayName
    }
}