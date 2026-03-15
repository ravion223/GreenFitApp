package com.example.greenfitapp.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardMembership
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.greenfitapp.data.newsList
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ElevatedCard
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import com.example.greenfitapp.data.NewsItem
import com.example.greenfitapp.R
import com.example.greenfitapp.data.auth.AuthManager

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onMembershipButtonClick: () -> Unit,
    onLocationsButtonClick: () -> Unit,
    onWorkoutSessionsButtonClick: () -> Unit,
    ) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        GreenFitAppButtons(
            onMembershipButtonClick = onMembershipButtonClick,
            onLocationsButtonClick = onLocationsButtonClick,
            onWorkoutSessionsButtonClick = onWorkoutSessionsButtonClick
        )
        GreenFitAppGreetingText("${AuthManager.getCurrentUserName() ?: "Athlete"} ")
        GreenFitAppNews(modifier = Modifier.weight(1f))
    }
}

@Composable
fun GreenFitAppButtons(
    modifier: Modifier = Modifier,
    onMembershipButtonClick: () -> Unit,
    onLocationsButtonClick: () -> Unit,
    onWorkoutSessionsButtonClick: () -> Unit,
) {
    Column(

    ) {
        Button(
            onClick = { onMembershipButtonClick() },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.CardMembership,
                contentDescription = stringResource(R.string.memberships)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.memberships),
                modifier = Modifier.padding(top=16.dp, bottom = 16.dp),
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row() {
            Button(
                onClick = { onWorkoutSessionsButtonClick() },
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = stringResource(R.string.classes)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.classes),
                    modifier = Modifier.padding(top=16.dp, bottom = 16.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = { onLocationsButtonClick() },
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = stringResource(R.string.locations)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.locations),
                    modifier = Modifier.padding(top=16.dp, bottom = 16.dp)
                )
            }
        }
    }
}

@Composable
fun GreenFitAppGreetingText(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "${stringResource(R.string.greeting)}, $name",
        style = MaterialTheme.typography.headlineMedium,
        modifier = Modifier.padding(vertical = 16.dp)
    )
}

@Composable
fun NewsCard(newsItem: NewsItem) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column() {
            if (newsItem.imageId != null){
                Image(
                    painter = painterResource(newsItem.imageId),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.TopCenter,
                    modifier = Modifier
                        .aspectRatio(16f / 9f)
                        .fillMaxWidth()
                        .padding(8.dp)
                )
            }
            Text(
                text = stringResource(newsItem.title),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 8.dp)
            )
            Text(
                text = stringResource(newsItem.newsContent),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start= 8.dp, end = 8.dp, bottom = 8.dp)
            )
        }
    }
}

@Composable
fun GreenFitAppNews(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.news),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 8.dp),
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            contentPadding = PaddingValues(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(newsList){ news ->
                NewsCard(news)
            }
        }
    }
}