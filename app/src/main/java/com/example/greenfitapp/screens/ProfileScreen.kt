package com.example.greenfitapp.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.greenfitapp.R
import com.example.greenfitapp.data.MembershipItem
import com.example.greenfitapp.data.WorkoutSession
import com.example.greenfitapp.data.auth.AuthManager
import com.example.greenfitapp.data.workoutSessionsList
import com.example.greenfitapp.ui.theme.GreenFitAppTheme
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProfileScreen(
        onBuyMembershipClick: () -> Unit,
        onBookClassClick: () -> Unit,
        onConfirmation: () -> Unit
    ){
    var dialogState by remember { mutableStateOf(false) }
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        ProfilePicture()
        ProfileUsername()
        ProfileMemberships(onBuyMembershipClick = onBuyMembershipClick)
        ProfileClasses(onBookClassClick = onBookClassClick)
        SignOutButton(onLogoutClick = { dialogState = !dialogState})

    }
    if (dialogState){
        ConfirmLogoutDialog(
            onDismissRequest = { dialogState = !dialogState },
            onConfirmation = onConfirmation
        )
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
            modifier = Modifier.size(18.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            text = AuthManager.getCurrentUserName() ?: "User",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun ProfileMemberships(onBuyMembershipClick: () -> Unit){
    var userMembership by remember { mutableStateOf<MembershipItem?>(null) }

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        Text(
            text = stringResource(R.string.memberships_label)
        )
        if (userMembership == null){
            EmptyMembershipState(onBuyMembershipClick = onBuyMembershipClick)
        }else{
            MembershipCard(userMembership!!)
        }
    }
}

@Composable
fun EmptyMembershipState(onBuyMembershipClick: () -> Unit){
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
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
fun ProfileClasses(onBookClassClick: () -> Unit){
    val today = LocalDate.now().toString()

    val upcoming = workoutSessionsList.filter { it.date >= today }
    val history = workoutSessionsList.filter { it.date < today }

    var state by remember { mutableStateOf(0) }
    val tabs = listOf(stringResource(R.string.upcoming_tab), stringResource(R.string.history_tab))

    var userClasses by remember { mutableStateOf<List<WorkoutSession>>(emptyList()) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.classes_label)
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

        val filteredSessions = if(state == 0){upcoming}else{history}
        if(userClasses.isEmpty()){
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

@Composable
fun EmptyClassesState(onBookClassClick: () -> Unit){
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

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview(){
    GreenFitAppTheme() {

    }
}