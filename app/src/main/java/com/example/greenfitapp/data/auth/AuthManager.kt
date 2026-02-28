package com.example.greenfitapp.data.auth

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


class AuthManager {
    private val auth: FirebaseAuth = Firebase.auth

    fun isUserLoggedIn(): Boolean = auth.currentUser != null

    fun signUp(email: String, password: String, onComplete: (Boolean) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                onComplete(task.isSuccessful)
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
}