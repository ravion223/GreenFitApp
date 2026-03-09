package com.example.greenfitapp.screens

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.greenfitapp.R
import com.example.greenfitapp.components.GreenFitLoadingScreen
import com.example.greenfitapp.data.UserProfile
import com.example.greenfitapp.data.WorkoutManager
import com.example.greenfitapp.data.WorkoutSession
import com.example.greenfitapp.data.auth.AuthManager
import com.example.greenfitapp.data.locationsList
import com.example.greenfitapp.ui.theme.GreenFitAppTheme

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WorkoutSessionsScreen(modifier: Modifier = Modifier){
    var selectedId by remember { mutableStateOf<Int?>(null) }
    var workoutSessionsList by remember { mutableStateOf<List<WorkoutSession>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val today = java.time.LocalDate.now().toString()

    Column(
        modifier = Modifier
            .padding(8.dp)
    ) {
        LazyRow() {
            item {
                FilterChip(
                    selected = selectedId == null,
                    onClick = {
                        selectedId = null
                    },
                    label = { Text("All") },

                    )
            }

            items(locationsList) { location ->
                FilterChip(
                    selected = selectedId == location.id,
                    onClick = {
                        selectedId = location.id
                    },
                    label = { Text(stringResource(location.nameRes)) }
                )
            }
        }

        LaunchedEffect(Unit){
            AuthManager.getUserProfile { profile ->
                if (profile != null){
                    WorkoutManager.getWorkouts(profile.bookedClassesIds) { workoutSessions ->
                        workoutSessionsList = workoutSessions
                        isLoading = false
                    }
                }else{
                    WorkoutManager.getWorkouts(emptyList()) { workoutSessions ->
                        workoutSessionsList = workoutSessions
                        isLoading = false
                    }
                }
            }
        }

        if(isLoading){
            GreenFitLoadingScreen(modifier = Modifier.fillMaxSize())
        }else {
            if (workoutSessionsList.isEmpty()){
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = stringResource(R.string.workouts_not_found),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }else{
                LazyColumn() {
                    val filteredList = workoutSessionsList.filter { workoutSession ->
                        val isUpcoming = workoutSession.date >= today

                        val matchesLocation = selectedId == null || workoutSession.locationId == selectedId

                        isUpcoming && matchesLocation
                    }
                    items(filteredList) { workout ->
                        WorkoutSessionCard(
                            workout,
                            onApplySuccess = {
                                workoutSessionsList = workoutSessionsList.filter{it.id != workout.id}
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WorkoutSessionCard(
    workoutSession: WorkoutSession,
    onApplySuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    val location = locationsList.find { it.id == workoutSession.locationId }
    val locationName = if (location != null) {stringResource(location.nameRes)} else "Unknown"
    var isBooking by remember { mutableStateOf(false) }
    val context = LocalContext.current

    ElevatedCard(
        modifier = Modifier.padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Image(
                painter = painterResource(workoutSession.imageId),
                contentDescription = stringResource(workoutSession.titleRes),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .aspectRatio(16f / 9f)
                    .fillMaxWidth()
            )

            IconText(Icons.Default.Star, "${stringResource(workoutSession.titleRes)} with ${stringResource(workoutSession.trainerRes)}")
            IconText(Icons.Default.LocationOn, locationName)
            IconText(Icons.Default.Alarm, "${stringResource(R.string.label_start,workoutSession.date, workoutSession.startTime)} - ${stringResource(R.string.label_duration, workoutSession.durationMinutes)}")
            IconText(Icons.Default.People, "${stringResource(R.string.label_slots_available , workoutSession.currentParticipants, workoutSession.maxParticipants)} ")

            if (workoutSession.maxParticipants != workoutSession.currentParticipants) {
                Button(
                    enabled = !isBooking,
                    onClick = {
                        isBooking = true

                        AuthManager.bookClasses(
                            workoutSession.id,
                        ) { success ->
                            isBooking = false
                            if (success) {
                                Toast.makeText(
                                    context,
                                    context.getText(R.string.success_apply_toast),
                                    Toast.LENGTH_SHORT
                                ).show()

                                onApplySuccess()
                            }
                        }
                    }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = modifier
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircleOutline,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if(isBooking) "Booking..." else stringResource(R.string.label_apply),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            } else {
                Text(
                    text = stringResource(R.string.full_slots),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun IconText(
    imageVector: ImageVector,
    text: String,
    modifier: Modifier = Modifier,
    tint: Color = MaterialTheme.colorScheme.primary
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = tint
        )
        Spacer(Modifier.width(4.dp))
        Text(text = text, style = MaterialTheme.typography.bodyMedium)
    }
}

@Preview (showBackground = true)
@Composable
fun WorkoutSessionCardPreview() {
    GreenFitAppTheme() {

    }
}