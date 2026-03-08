package com.example.greenfitapp.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.greenfitapp.R
import com.example.greenfitapp.data.MembershipItem
import com.example.greenfitapp.data.UserProfile
import com.example.greenfitapp.data.WorkoutManager
import com.example.greenfitapp.data.WorkoutSession
import com.example.greenfitapp.data.auth.AuthManager
import com.example.greenfitapp.data.membershipList
import com.example.greenfitapp.ui.theme.GreenFitAppTheme
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProfileScreen(
        onBuyMembershipClick: () -> Unit,
        onBookClassClick: () -> Unit,
        onConfirmation: () -> Unit
    ){
    var userProfile by remember { mutableStateOf<UserProfile?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var dialogState by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        AuthManager.getUserProfile { profile ->
            userProfile = profile
            isLoading = false
        }
    }

    if(isLoading){
        Text(stringResource(R.string.loading))
    }else{
        val profile = userProfile
        if (profile == null){
            Text(stringResource(R.string.unknown))
        }else{
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                ProfilePicture()
                ProfileUsername()
                ProfileMemberships(
                    userProfile = profile,
                    onBuyMembershipClick = onBuyMembershipClick
                )
                ProfileClasses(
                    userProfile = profile,
                    onBookClassClick = onBookClassClick
                )
                SignOutButton(onLogoutClick = { dialogState = !dialogState})

            }
            if (dialogState){
                ConfirmLogoutDialog(
                    onDismissRequest = { dialogState = !dialogState },
                    onConfirmation = onConfirmation
                )
            }
        }
    }
}

@Composable
fun ProfilePicture() {
    Box(
        modifier = Modifier
            .size(100.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = AuthManager.getCurrentUserName()?.take(1)?.uppercase() ?: "User",
            style = MaterialTheme.typography.displayMedium
        )
    }
}

@Composable
fun ProfileUsername(){
    Row() {
        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = stringResource(R.string.edit_username),
            modifier = Modifier.size(22.dp),
            tint = MaterialTheme.colorScheme.primary,
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = AuthManager.getCurrentUserName() ?: "User",
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Composable
fun ProfileMemberships(
    userProfile: UserProfile,
    onBuyMembershipClick: () -> Unit
){
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        val activeMembership = membershipList.find { it.id == userProfile.activeMembershipId }
        if (activeMembership == null){
            EmptyMembershipState(onBuyMembershipClick = onBuyMembershipClick)
        }else{
            ProfileMembershipCard(activeMembership, userProfile)
        }
    }
}

@Composable
fun EmptyMembershipState(onBuyMembershipClick: () -> Unit){
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(24.dp),
        ) {
            Text(
                text = stringResource(R.string.membership_empty),
                style = MaterialTheme.typography.bodyLarge
            )
            TextButton(onClick = { onBuyMembershipClick() }) {
                Text(
                    text = stringResource(R.string.membership_redirect)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProfileClasses(
    userProfile: UserProfile,
    onBookClassClick: () -> Unit)
{
    val today = LocalDate.now().toString()
    var workoutSessionsList by remember { mutableStateOf<List<WorkoutSession>>((emptyList())) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        WorkoutManager.getUserWorkouts(userProfile.bookedClassesIds) { workoutSessions ->
            workoutSessionsList = workoutSessions
            isLoading = false
        }
    }

    var state by remember { mutableStateOf(0) }
    val tabs = listOf(stringResource(R.string.upcoming_tab), stringResource(R.string.history_tab))

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.classes_label),
            style = MaterialTheme.typography.titleMedium
        )
        PrimaryTabRow(selectedTabIndex = state) {
            tabs.forEachIndexed { index, tab ->
                Tab(
                    selected = state == index,
                    onClick = { state = index },
                    text = { Text(text = tab, overflow = TextOverflow.Ellipsis) }
                )
            }
        }

        if (isLoading){
            Text(stringResource(R.string.loading))
        }else{
            val upcoming = workoutSessionsList.filter { it.date >= today }
            val history = workoutSessionsList.filter { it.date < today }
            val filteredSessions = if(state == 0){upcoming}else{history}
            if(workoutSessionsList.isEmpty()){
                EmptyClassesState(onBookClassClick)
            }else{
                filteredSessions.forEach { session ->
                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Text(
                                text = stringResource(session.titleRes)
                            )
                            Text(
                                text = session.date
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyClassesState(onBookClassClick: () -> Unit){
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = stringResource(R.string.no_classes_yet),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            TextButton(onClick = { onBookClassClick() }) {
                Text(text = stringResource(R.string.book_first_class))
            }
        }
    }

}

@Composable
fun SignOutButton(onLogoutClick: () -> Unit){
    OutlinedButton(
        onClick = onLogoutClick,
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = stringResource(R.string.signout_button),
            color = Color.White
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmLogoutDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
){
    AlertDialog(
        icon = {
            Icon(
                imageVector = Icons.Default.Logout,
                contentDescription = null
            )
        },
        title = {
            Text(stringResource(R.string.dialog_logout_title))
        },
        text = {
            Text(stringResource(R.string.dialog_logout_text))
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                    onConfirmation()
                    AuthManager.logout()
                }
            ) {
                Text(stringResource(R.string.confirm_logout_button))
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onDismissRequest() }
            ) {
                Text(stringResource(R.string.dismiss_logout_button))
            }
        },
    )
}

@Composable
fun ProfileMembershipCard(membership: MembershipItem, profile: UserProfile) {
    val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    val dateString = profile.membershipExpireDate?.let { formatter.format(Date(it))} ?: ""

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.memberships_label),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
            Image(
                painter = painterResource(membership.imageId),
                contentDescription = null,
                modifier = Modifier.size(256.dp)
            )
            Text(
                text = "${stringResource(R.string.due_to)} ${dateString}",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview(){
    GreenFitAppTheme() {

    }
}