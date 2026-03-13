package com.example.greenfitapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.greenfitapp.ui.theme.GreenFitAppTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.example.greenfitapp.screens.HomeScreen
import com.example.greenfitapp.components.GreenFitFooter
import com.example.greenfitapp.components.GreenFitHeader
import com.example.greenfitapp.data.ThemeManager
import com.example.greenfitapp.data.auth.AuthManager
import com.example.greenfitapp.screens.GymLocationsScreen
import com.example.greenfitapp.screens.MembershipsScreen
import com.example.greenfitapp.screens.ProfileScreen
import com.example.greenfitapp.screens.SettingsScreen
import com.example.greenfitapp.screens.WorkoutSessionsScreen
import com.example.greenfitapp.screens.auth.AuthScreen
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val themeManager = ThemeManager(this)

        setContent {

            val isDarkMode by themeManager.getTheme.collectAsState(initial = false)
            val coroutineScope = rememberCoroutineScope()

            GreenFitAppTheme(darkTheme = isDarkMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GreenFitMainApp(
                        modifier = Modifier.fillMaxSize(),
                        isDarkMode = isDarkMode,
                        onThemeChange = { newThemeValue ->
                            coroutineScope.launch {
                                themeManager.saveTheme(newThemeValue)
                            }
                        }
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GreenFitMainApp(
    modifier: Modifier = Modifier,
    isDarkMode: Boolean,
    onThemeChange: (Boolean) -> Unit
) {

    var selectedItem by remember { mutableStateOf(0) }
    var showLogin by remember { mutableStateOf(AuthManager.getCurrentUser() == null) }
    val coroutineScope = rememberCoroutineScope() // async writing in DataStore

    if (showLogin) {
        AuthScreen(onAuthSuccess = { showLogin = false })
    } else {
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
                when (selectedItem) {
                    0 -> HomeScreen(
                        onMembershipButtonClick = { selectedItem = 1 },
                        onLocationsButtonClick = { selectedItem = 2 },
                        onWorkoutSessionsButtonClick = { selectedItem = 3 }
                    )

                    1 -> MembershipsScreen()
                    2 -> GymLocationsScreen()
                    3 -> WorkoutSessionsScreen()
                    4 -> ProfileScreen(
                        onBuyMembershipClick = { selectedItem = 1 },
                        onBookClassClick = { selectedItem = 3 },
                        onConfirmation = {
                            showLogin = true
                            selectedItem = 0
                        }
                    )
                    5 -> SettingsScreen(
                        isDarkMode = isDarkMode,
                        onThemeChange = onThemeChange
                    )
                }
            }
        }
    }
}