package com.example.greenfitapp.data

import java.util.Calendar

data class UserProfile(
    val uid: String = "",
    val name: String = "",
    val email: String = "",

    val activeMembershipId: Int? = null,
    val membershipExpireDate: Long? = null,
    val bookedClassesIds: List<Int> = emptyList()
)

fun calculateExpireDate(isYearly: Boolean): Long {
    val calendar = Calendar.getInstance()

    if (isYearly) {
        calendar.add(Calendar.YEAR, 1)
    }else{
        calendar.add(Calendar.MONTH, 1)
    }

    return calendar.timeInMillis
}