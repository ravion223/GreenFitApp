package com.example.greenfitapp.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.greenfitapp.R

data class MembershipItem(
    @DrawableRes val imageId: Int,
    @StringRes val descriptionId: Int,
    val priceMonth: Int,
    val priceYear: Int
)

val membershipList = listOf<MembershipItem>(
    MembershipItem(R.drawable.silvertier, R.string.silverTierDesc, 1500, 14400),
    MembershipItem(R.drawable.goldtier, R.string.goldTierDesc, 2800, 26800),
    MembershipItem(R.drawable.platinumtier, R.string.platinumTierDesc, 4500, 43200),
)