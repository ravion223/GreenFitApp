package com.example.greenfitapp.data.auth

import android.annotation.SuppressLint
import androidx.compose.runtime.disableHotReloadMode
import com.example.greenfitapp.data.UserProfile
import com.example.greenfitapp.data.calculateExpireDate
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.auth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore


object AuthManager {
    private val auth: FirebaseAuth = Firebase.auth

    private val db: FirebaseFirestore by lazy { Firebase.firestore }

    fun isUserLoggedIn(): Boolean = auth.currentUser != null
    fun getCurrentUser() = auth.currentUser

    fun getUserProfile(onComplete: (UserProfile?) -> Unit){
        val uid = auth.currentUser?.uid
        if (uid == null){
            onComplete(null)
        }else{
            db.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val userProfile = document.toObject(UserProfile::class.java)
                        onComplete(userProfile)
                    }
                    else{
                        onComplete(null)
                    }
                }
                .addOnFailureListener {
                    onComplete(null)
                }
        }
    }

    fun signUp(email: String, password: String, userName: String, onComplete: (Boolean) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    val firebaseUser = getCurrentUser()

                    // This part remains because it loads data for profile much faster
                    val profileUpdates = userProfileChangeRequest {
                        displayName = userName
                    }

                    auth.currentUser?.updateProfile(profileUpdates)
                        ?.addOnCompleteListener { profileTask ->
                            if (profileTask.isSuccessful && firebaseUser != null) {
                                val newUserProfile = UserProfile(
                                    uid = firebaseUser.uid,
                                    name = userName.trim(),
                                    email = email
                                )

                                db.collection("users")
                                    .document(newUserProfile.uid)
                                    .set(newUserProfile)
                                    .addOnCompleteListener {
                                        onComplete(true)
                                    }
                                    .addOnFailureListener {
                                        onComplete(false)
                                    }
                            }
                            else{
                                onComplete(false)
                            }
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

    fun purchaseMembership(
        membershipId: Int,
        isYearly: Boolean,
        onComplete: (Boolean) -> Unit
    ) {
        val uid = auth.currentUser?.uid
        if (uid == null){
            onComplete(false)
            return
        }

        val expireDate = calculateExpireDate(isYearly)

        val updates = mapOf(
            "activeMembershipId" to membershipId,
            "membershipExpireDate" to expireDate
        )

        db.collection("users")
            .document(uid)
            .update(updates)
            .addOnCompleteListener {
                onComplete(true)
            }
            .addOnFailureListener {
                onComplete(false)
            }
    }

    fun bookClasses(
        classId: String,
        onComplete: (Boolean) -> Unit
    ) {
        val uid = auth.currentUser?.uid
        if (uid == null){
            onComplete(false)
            return
        }

        val userRef = db.collection("users").document(uid)
        val classRef = db.collection("classes").document(classId)

        db.runBatch { batch ->
            batch.update(userRef, "bookedClassesIds", FieldValue.arrayUnion(classId))
            batch.update(classRef, "currentParticipants", FieldValue.increment(1))
        }.addOnSuccessListener {
            onComplete(true)
        }.addOnFailureListener {
            onComplete(false)
        }
    }

    fun cancelClass(
        classId: String,
        onComplete: (Boolean) -> Unit
    ){
        val uid = auth.currentUser?.uid
        if (uid == null){
            onComplete(false)
            return
        }

        val userRef = db.collection("users").document(uid)
        val classRef = db.collection("classes").document(classId)

        db.runBatch { batch ->
            batch.update(userRef, "bookedClassesIds", FieldValue.arrayRemove(classId))
            batch.update(classRef, "currentParticipants", FieldValue.increment(-1))
        }.addOnSuccessListener {
            onComplete(true)
        }.addOnFailureListener {
            onComplete(false)
        }
    }

    fun UpdateUsername(
        newName: String,
        onComplete: (Boolean) -> Unit
    ) {
        val uid = auth.currentUser?.uid
        if (uid == null){
            onComplete(false)
            return
        }

        db.collection("users")
            .document(uid)
            .update("name", newName)
            .addOnSuccessListener {
                onComplete(true)
            }
            .addOnFailureListener {
                onComplete(false)
            }
    }
}