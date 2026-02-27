package com.example.greenfitapp.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.greenfitapp.R

data class GymLocation(
    val id: Int,
    @StringRes val nameRes: Int,
    @StringRes val gymAddressRes: Int,
    @StringRes val gymDescriptionRes: Int,
    val rating: Double,
    val reviewsCount: Int,
    @DrawableRes val imageId: Int,
)

val locationsList = listOf(
    GymLocation(
        id = 1,
        nameRes = R.string.location_arena_title,
        gymAddressRes = R.string.location_arena_address,
        gymDescriptionRes = R.string.location_arena_desc,
        rating = 4.8,
        reviewsCount = 156,
        imageId = R.drawable.greenfit_arena
    ),
    GymLocation(
        id = 2,
        nameRes = R.string.location_studio_title,
        gymAddressRes = R.string.location_studio_address,
        gymDescriptionRes = R.string.location_studio_desc,
        rating = 5.0,
        reviewsCount = 42,
        imageId = R.drawable.greenfit_studio
    ),
    GymLocation(
        id = 3,
        nameRes = R.string.location_central_title,
        gymAddressRes = R.string.location_central_address,
        gymDescriptionRes = R.string.location_central_desc,
        rating = 4.6,
        reviewsCount = 289,
        imageId = R.drawable.greenfit_central
    )
)