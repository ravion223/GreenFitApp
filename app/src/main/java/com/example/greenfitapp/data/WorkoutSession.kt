package com.example.greenfitapp.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.greenfitapp.R

data class WorkoutSession(
    val id: String = "",
    val categoryType: String = "",
    val trainerId: String = "",
    val locationId: Int = 0,
    val startTime: String = "",
    val date: String = "",
    val durationMinutes: Int = 0,
    val maxParticipants: Int = 0,
    val currentParticipants: Int = 0,
) {
    @get:StringRes
    val titleRes: Int
        get() = when (categoryType) {
            "yoga" -> R.string.workout_yoga_title
            "crossfit" -> R.string.workout_crossfit_title
            "swimming" -> R.string.workout_swimming_title
            "pilates" -> R.string.workout_pilates_title
            "boxing" -> R.string.workout_boxing_title
            else -> R.string.unknown
        }

    @get:DrawableRes
    val imageId: Int
        get() = when (categoryType){
            "yoga" -> R.drawable.greenfit_yoga
            "crossfit" -> R.drawable.greenfit_crossfit
            "swimming" -> R.drawable.greenfit_swimming
            "pilates" -> R.drawable.greenfit_pilates
            "boxing" -> R.drawable.greenfit_boxing
            else -> R.drawable.ic_launcher_background
        }

    @get:StringRes
    val trainerRes: Int
        get() = when (trainerId) {
            "anna" -> R.string.trainer_anna
            "mark" -> R.string.trainer_mark
            "elena" -> R.string.trainer_olena
            "david" -> R.string.trainer_david
            else -> R.string.unknown
        }
}