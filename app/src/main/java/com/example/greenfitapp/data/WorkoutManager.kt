package com.example.greenfitapp.data

import androidx.compose.ui.graphics.rememberGraphicsLayer
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

object WorkoutManager {
    private val db: FirebaseFirestore by lazy { Firebase.firestore }

    fun getWorkouts(
            bookedIds: List<String>,
            onComplete: (List<WorkoutSession>
        ) -> Unit){
        db.collection("classes")
            .get()
            .addOnSuccessListener { result ->
                val workoutsList = mutableListOf<WorkoutSession>()
                for (document in result) {
                    if (document.id !in bookedIds){
                        val workout = document.toObject(WorkoutSession::class.java)
                        val workoutWithId = workout.copy(id=document.id)
                        workoutsList.add(workoutWithId)
                    }
                }
                onComplete(workoutsList)
            }
            .addOnFailureListener {
                onComplete(emptyList())
            }
    }

    fun getUserWorkouts(classesIds: List<String>, onComplete: (List<WorkoutSession>) -> Unit){
        if (classesIds.isEmpty()){
            onComplete(emptyList())
            return
        }

        db.collection("classes")
            .whereIn(FieldPath.documentId(), classesIds)
            .get()
            .addOnSuccessListener { result ->
                var bookedClasses = mutableListOf<WorkoutSession>()
                for (document in result) {
                    val workout = document.toObject(WorkoutSession::class.java)
                    bookedClasses.add(workout.copy(id=document.id))
                }
                onComplete(bookedClasses)
            }.addOnFailureListener {
                onComplete(emptyList())
            }
    }
}
