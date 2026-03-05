package com.example.greenfitapp.data

data class UserProfile(
    val uid: String = "",
    val name: String = "",
    val email: String = "",

    val activeMembershipId: Int? = null,

    val bookedClassesIds: List<Int> = emptyList()
)
