package com.example.greenfitapp.screens

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.DriveFileRenameOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.greenfitapp.R
import com.example.greenfitapp.components.GreenFitLoadingScreen
import com.example.greenfitapp.data.MembershipItem
import com.example.greenfitapp.data.UserProfile
import com.example.greenfitapp.data.WorkoutManager
import com.example.greenfitapp.data.WorkoutSession
import com.example.greenfitapp.data.auth.AuthManager
import com.example.greenfitapp.data.locationsList
import com.example.greenfitapp.data.membershipList
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
    var showUpdateUsernameDialog by remember { mutableStateOf(false) }
    var showAvatarDialog by remember { mutableStateOf(false) }
    val availableAvatars = listOf(
        "avatar_1", "avatar_2", "avatar_3",
        "avatar_4"
    )


    LaunchedEffect(Unit) {
        AuthManager.getUserProfile { profile ->
            userProfile = profile
            isLoading = false
        }
    }

    if(isLoading){
        GreenFitLoadingScreen(modifier = Modifier.fillMaxSize())
    }else{
        val profile = userProfile
        if (profile == null){
            Text(stringResource(R.string.unknown))
        }else{
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.verticalScroll(rememberScrollState()).padding(top=8.dp)
            ) {
                ProfilePicture(
                    userProfile = profile,
                    onAvatarClick = { showAvatarDialog = true }
                )
                ProfileUsername(
                    profileName = userProfile?.name,
                    onUsernameUpdateClick = { showUpdateUsernameDialog = true }
                )
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
            if (showUpdateUsernameDialog){
                UsernameUpdateDialog(
                    onDismissRequest = { showUpdateUsernameDialog = !showUpdateUsernameDialog },
                    onConfirmation = { newName ->
                        showUpdateUsernameDialog = false
                        userProfile = userProfile?.copy(name = newName)
                    }
                )
            }
        }
    }
    if (showAvatarDialog){
        AvatarDialog(
            onDismissRequest = { showAvatarDialog = false },
            onAvatarUpdate = { avatarName ->
                AuthManager.updateAvatar(newAvatar = avatarName) { success ->
                    if (success){
                        showAvatarDialog = false
                        userProfile = userProfile?.copy(avatar = avatarName)
                    }
                }
            },
            availableAvatars = availableAvatars
        )
    }
}

@Composable
fun ProfilePicture(
    userProfile: UserProfile,
    onAvatarClick: () -> Unit
) {
    if(userProfile.avatar.isEmpty()){
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer)
                .clickable { onAvatarClick() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = userProfile.name.take(1).uppercase(),
                style = MaterialTheme.typography.displayMedium
            )
        }
    } else{
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .clickable { onAvatarClick() },
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(userProfile.avatarRes),
                contentDescription = "User Avatar",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
fun ProfileUsername(
    profileName: String?,
    onUsernameUpdateClick: () -> Unit
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = { onUsernameUpdateClick() }
        ){
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = stringResource(R.string.edit_username),
                modifier = Modifier.size(22.dp),
                tint = MaterialTheme.colorScheme.primary,
            )
        }
        Text(
            text = profileName ?: "User",
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
    onBookClassClick: () -> Unit,
)
{
    var workoutSessionsList by remember { mutableStateOf<List<WorkoutSession>>((emptyList())) }
    val today = LocalDate.now().toString()
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(true) }
    var isCancelling by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        WorkoutManager.getUserWorkouts(userProfile.bookedClassesIds) { workoutSessions ->
            workoutSessionsList = workoutSessions
            isLoading = false
        }
    }

    var state by remember { mutableStateOf(0) }
    val tabs = listOf(stringResource(R.string.upcoming_tab), stringResource(R.string.history_tab))

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
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
            GreenFitLoadingScreen(modifier = Modifier.fillMaxSize())
        }else{
            val upcoming = workoutSessionsList.filter { it.date >= today }
            val history = workoutSessionsList.filter { it.date < today }
            val filteredSessions = if(state == 0){upcoming}else{history}
            if(filteredSessions.isEmpty()){
                EmptyClassesState(onBookClassClick)
            }else{
                filteredSessions.forEach { session ->
                    val location = locationsList.find { it.id == session.locationId }
                    val locationName = if (location != null) { stringResource(location.nameRes)} else "Unknown"
                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ){
                            Column(
                                horizontalAlignment = Alignment.Start,
                                modifier = Modifier
                                    .padding(8.dp)
                                    .weight(1f)
                            ) {
                                IconText(
                                    imageVector = Icons.Default.Star,
                                    text = "${stringResource(session.titleRes)} with ${stringResource(session.trainerRes)}"
                                )
                                IconText(
                                    imageVector = Icons.Default.Alarm,
                                    text = "${session.date}, ${session.startTime}"
                                )
                                IconText(
                                    imageVector = Icons.Default.LocationOn,
                                    text = "Location: $locationName"
                                )
                            }
                            IconButton(
                                enabled = !isCancelling,
                                onClick = {
                                    isCancelling = true
                                    AuthManager.cancelClass(session.id)
                                    { success ->
                                        isCancelling = false
                                        if(success){
                                            Toast.makeText(
                                                context,
                                                context.getText(R.string.cancel_class_toast),
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                        workoutSessionsList = workoutSessionsList.filter { it.id != session.id }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Cancel,
                                    contentDescription = stringResource(R.string.cancel_class),
                                    tint = if(!isCancelling)MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                                )
                            }
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsernameUpdateDialog(
    onConfirmation: (String) -> Unit,
    onDismissRequest: () -> Unit
){
    var newUsernameInput by remember { mutableStateOf("") }
    val context = LocalContext.current
    AlertDialog(
        icon = {
            Icon(
                imageVector = Icons.Default.DriveFileRenameOutline,
                contentDescription = null
            )
        },
        title = {
            Text(stringResource(R.string.new_name))
        },
        text = {
            OutlinedTextField(
                value = newUsernameInput,
                onValueChange = { newUsernameInput = it },
            )
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(onClick = {
                AuthManager.updateUsername(
                    newUsernameInput
                ){ success ->
                    if(success){
                        Toast.makeText(context, context.getText(R.string.toast_updateUsername), Toast.LENGTH_SHORT).show()
                        onConfirmation(newUsernameInput)
                    }
                }
            }) {
                Text(stringResource(R.string.confirm_updateUsername_button))
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismissRequest() }) {
                Text(stringResource(R.string.dismiss_updateUsername_button))
            }
        }
    )
}

@Composable
fun AvatarDialog(
    onDismissRequest: () -> Unit,
    onAvatarUpdate: (String) -> Unit,
    availableAvatars: List<String>
){
    val context = LocalContext.current
    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        title = { Text(text = stringResource(R.string.avatar_title)) },
        text = {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(availableAvatars) { avatarName ->
                    val resId = context.resources.getIdentifier(avatarName, "drawable", context.packageName)

                    Image(
                        painter = painterResource(resId),
                        contentDescription = avatarName,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                onAvatarUpdate(avatarName)
                            }
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onDismissRequest() }) {
                Text(text = stringResource(R.string.cancel_avatar_selection))
            }
        }
    )
}