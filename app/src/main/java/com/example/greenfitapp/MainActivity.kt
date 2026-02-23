package com.example.greenfitapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CardMembership
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.greenfitapp.data.newsList
import com.example.greenfitapp.ui.theme.GreenFitAppTheme
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import com.example.greenfitapp.data.NewsItem

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GreenFitAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GreenFitMainApp(
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
fun GreenFitMainApp(modifier: Modifier = Modifier) {
    Scaffold(
        topBar = {
            GreenFitHeader()
        },
        bottomBar = {
            GreenFitFooter()
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                GreenFitAppButtons()
                GreenFitAppGreetingText("Гість")

                GreenFitAppNews(modifier = Modifier.weight(1f))
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GreenFitHeader(modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(R.string.header_title),
                color = Color(0xFF1b5e17)
            )
        },
    )

}

@Composable
fun GreenFitFooter(modifier: Modifier = Modifier) {

    var selectedItem by remember { mutableStateOf(0) }

    NavigationBar(
        modifier = modifier
    ) {
        NavigationBarItem(
            selected = selectedItem == 0,
            onClick = { selectedItem = 0 },
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = stringResource(R.string.homeButton)
                )
               },
            label = { Text(stringResource(R.string.homeButton)) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                indicatorColor = MaterialTheme.colorScheme.primary
            )
        )
        NavigationBarItem(
            selected = selectedItem == 1,
            onClick = { selectedItem = 1 },
            icon = {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = stringResource(R.string.profileButton)
                )
            },
            label = { Text(stringResource(R.string.profileButton)) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                indicatorColor = MaterialTheme.colorScheme.primary
            )
        )
    }
}

@Composable
fun GreenFitAppButtons(modifier: Modifier = Modifier) {
    Column(

    ) {
        Button(
            onClick = { /* ? */ },
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
                onClick = { /* ? */ },
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
                onClick = { /* ? */ },
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
        text = "Привіт, $name",
        style = MaterialTheme.typography.headlineMedium,
        modifier = Modifier.padding(vertical = 16.dp)
    )
}

@Composable
fun NewsCard(title: String, newsContent: String, @DrawableRes imageId: Int?) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column() {
            if (imageId != null){
                Image(
                    painter = painterResource(imageId),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(150.dp)
                        .fillMaxWidth()
                        .padding(8.dp)
                )
            }
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(start = 8.dp)
            )
            Text(
                text = newsContent,
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
            text = "НОВИНИ",
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
                NewsCard(
                    title = stringResource(news.title),
                    newsContent = stringResource(news.newsContent),
                    imageId = news.imageId
                )
            }
        }
    }
}

@Preview()
@Composable
fun GreetingPreview() {
    GreenFitAppTheme(darkTheme = false) {
        GreenFitMainApp()
    }
}