package com.example.greenfitapp.data

import androidx.annotation.DrawableRes
import com.example.greenfitapp.R
import com.google.firebase.firestore.Exclude
import java.util.Calendar

data class UserProfile(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val avatar: String = "",

    val activeMembershipId: Int? = null,
    val membershipExpireDate: Long? = null,
    val bookedClassesIds: List<String> = emptyList()
) {
    @get:Exclude
    @get:DrawableRes
    val avatarRes: Int
        get() = when(avatar){
            "avatar_1" -> R.drawable.avatar_1
            "avatar_2" -> R.drawable.avatar_2
            "avatar_3" -> R.drawable.avatar_3
            "avatar_4" -> R.drawable.avatar_4
            else -> R.drawable.ic_launcher_foreground
        }
}

fun calculateExpireDate(isYearly: Boolean): Long {
    val calendar = Calendar.getInstance()

    if (isYearly) {
        calendar.add(Calendar.YEAR, 1)
    }else{
        calendar.add(Calendar.MONTH, 1)
    }

    return calendar.timeInMillis
}