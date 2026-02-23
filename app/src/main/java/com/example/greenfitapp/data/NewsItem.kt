package com.example.greenfitapp.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.greenfitapp.R

data class NewsItem(
    @StringRes val title: Int,
    @StringRes val newsContent: Int,
    @DrawableRes val imageId: Int? = null
)

val newsList = listOf(
    NewsItem(R.string.title1, R.string.newsContent1, R.drawable.sale),
    NewsItem(R.string.title2, R.string.newsContent2),
    NewsItem(R.string.title3, R.string.newsContent3, R.drawable.qualified_personal_trainer_1024x682),
)
