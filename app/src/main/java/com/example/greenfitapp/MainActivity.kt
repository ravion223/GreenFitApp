package com.example.greenfitapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.greenfitapp.ui.theme.GreenFitAppTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.greenfitapp.screens.HomeScreen
import com.example.greenfitapp.components.GreenFitFooter
import com.example.greenfitapp.components.GreenFitHeader
import com.example.greenfitapp.screens.GymLocationsScreen
import com.example.greenfitapp.screens.MembershipsScreen
import com.example.greenfitapp.screens.WorkoutSessionsScreen

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

    var selectedItem by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            GreenFitHeader()
        },
        bottomBar = {
            GreenFitFooter(
                selectedIndex = selectedItem,
                onTabSelected = { index -> selectedItem = index }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            when(selectedItem) {
                0 -> HomeScreen(
                    onMembershipButtonClick = { selectedItem = 1 },
                    onLocationsButtonClick = { selectedItem = 2 },
                    onWorkoutSessionsButtonClick = { selectedItem = 3 }
                )
                1 -> MembershipsScreen()
                2 -> GymLocationsScreen()
                3 -> WorkoutSessionsScreen()
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