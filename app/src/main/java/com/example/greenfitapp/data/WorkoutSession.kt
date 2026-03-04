package com.example.greenfitapp.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.greenfitapp.R

data class WorkoutSession(
    val id: Int,
    @StringRes val titleRes: Int,
    @StringRes val trainerRes: Int,
    val locationId: Int,
    val startTime: String,
    val date: String,
    val durationMinutes: Int,
    val maxParticipants: Int,
    val currentParticipants: Int,
    @DrawableRes val imageId: Int
)

val workoutSessionsList = listOf(
    WorkoutSession(
        id = 1,
        titleRes = R.string.workout_yoga_title,
        trainerRes = R.string.trainer_anna,
        locationId = 2,
        startTime = "08:30 AM",
        date = "2026-03-05",
        durationMinutes = 60,
        maxParticipants = 12,
        currentParticipants = 10,
        imageId = R.drawable.greenfit_yoga
    ),
    WorkoutSession(
        id = 2,
        titleRes = R.string.workout_crossfit_title,
        trainerRes = R.string.trainer_mark,
        locationId = 1,
        startTime = "06:00 PM",
        date = "2026-02-24",
        durationMinutes = 90,
        maxParticipants = 20,
        currentParticipants = 20,
        imageId = R.drawable.greenfit_crossfit
    ),
    WorkoutSession(
        id = 3,
        titleRes = R.string.workout_swimming_title,
        trainerRes = R.string.trainer_olena,
        locationId = 3,
        startTime = "10:00 AM",
        date = "2026-02-28",
        durationMinutes = 45,
        maxParticipants = 15,
        currentParticipants = 6,
        imageId = R.drawable.greenfit_swimming
    ),
    WorkoutSession(
        id = 4,
        titleRes = R.string.workout_pilates_title,
        trainerRes = R.string.trainer_anna,
        locationId = 2,
        startTime = "11:30 AM",
        date = "2026-03-08",
        durationMinutes = 50,
        maxParticipants = 10,
        currentParticipants = 4,
        imageId = R.drawable.greenfit_pilates
    ),
    WorkoutSession(
        id = 5,
        titleRes = R.string.workout_boxing_title,
        trainerRes = R.string.trainer_david,
        locationId = 1,
        startTime = "07:30 PM",
        date = "2026-03-10",
        durationMinutes = 75,
        maxParticipants = 16,
        currentParticipants = 12,
        imageId = R.drawable.greenfit_boxing
    )
)