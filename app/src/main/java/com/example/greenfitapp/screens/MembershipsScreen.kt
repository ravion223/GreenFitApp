package com.example.greenfitapp.screens

import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardMembership
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.greenfitapp.R
import com.example.greenfitapp.data.MembershipItem
import com.example.greenfitapp.data.membershipList
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.greenfitapp.components.GreenFitLoadingScreen
import com.example.greenfitapp.data.UserProfile
import com.example.greenfitapp.data.auth.AuthManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MembershipsScreen(modifier: Modifier = Modifier) {
    var selectedMembership by remember { mutableStateOf<MembershipItem?>(null) }
    var pendingMembership by remember { mutableStateOf<MembershipItem?>(null) }
    var userProfile by remember { mutableStateOf<UserProfile?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    val context = LocalContext.current
    var dialogState by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        AuthManager.getUserProfile { profile ->
            if (profile != null) {
                userProfile = profile
                isLoading = false
            }
        }
    }

    if(isLoading){
        GreenFitLoadingScreen(modifier = Modifier.fillMaxSize())
    }else {
        LazyColumn() {
            items(membershipList) { membership ->
                MembershipCard(
                    membership,
                    userProfile = userProfile,
                    onBuyClick = {
                        if (userProfile?.activeMembershipId != null){
                            pendingMembership = membership
                            dialogState = true
                        } else {
                            selectedMembership = membership
                        }
                    })
            }
        }
        if(dialogState){
            AlreadyActiveMembership(
                onDismissRequest = {
                    dialogState = false
                    pendingMembership = null
               },
                onConfirmation = {
                    dialogState = false
                    selectedMembership = pendingMembership
                    pendingMembership = null
                }
            )
        }
        selectedMembership?.let { membership ->
            SubscriptionButtonSheet(
                onDismiss = { selectedMembership = null },
                onPlanSelected = { isYearly ->
                    AuthManager.purchaseMembership(
                        isYearly = isYearly,
                        membershipId = membership.id,
                    ) { success ->
                        if (success) {
                            selectedMembership = null
                            Toast.makeText(
                                context,
                                context.getText(R.string.purchase_success),
                                Toast.LENGTH_SHORT
                            ).show()

                            userProfile = userProfile?.copy(
                                activeMembershipId = membership.id
                            )
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun MembershipCard(
    membership: MembershipItem,
    onBuyClick: () -> Unit,
    userProfile: UserProfile?,
    modifier: Modifier = Modifier
) {
    val isOwned = membership.id == userProfile?.activeMembershipId

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        Column(modifier) {
            Row(
                verticalAlignment = Alignment.Top
            ) {
                Image(
                    painter = painterResource(membership.imageId),
                    contentDescription = null,
                )
                Text(
                    text = stringResource(membership.descriptionId),
                    style = MaterialTheme.typography.bodyMedium,
                    lineHeight = 20.sp,
                    textAlign = TextAlign.Justify,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(8.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    enabled = !isOwned,
                    onClick = { onBuyClick() }) {
                    Icon(
                        imageVector = if (isOwned) Icons.Default.CardMembership else Icons.Default.ShoppingCart,
                        contentDescription = stringResource(R.string.buyButton)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isOwned)stringResource(R.string.membership_owned) else stringResource(R.string.buyButton)
                    )
                }
                Row() {
                    PriceBlock(label = stringResource(R.string.monthly), membership.priceMonth)
                    Spacer(modifier.width(12.dp))
                    PriceBlock(stringResource(R.string.yearly), membership.priceYear)
                }

            }
        }
    }
}

@Composable
fun PriceBlock(label: String, price: Int) {
    Column(
        horizontalAlignment = Alignment.End
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = Color.Gray
        )
        Text(
            text = "$price ₴",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionButtonSheet(
    onDismiss: () -> Unit,
    onPlanSelected: (isYearly: Boolean) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = {
            onDismiss()
        },
        sheetState = sheetState
    ) {
        Text(text = stringResource(R.string.subscribe_button_sheet))
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
        ) {
            Button(onClick = { onPlanSelected(false) }) {
                Text(text = stringResource(R.string.one_month_sub))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = { onPlanSelected(true) }) {
                Text(text = stringResource(R.string.one_year_sub))
            }
        }
    }
}

@Composable
fun AlreadyActiveMembership(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
) {
    AlertDialog(
        icon = { Icon(Icons.Default.WarningAmber, contentDescription = null) },
        title = { Text(stringResource(R.string.membership_alert_title)) },
        text = { Text(stringResource(R.string.membership_alert_text)) },
        onDismissRequest = { onDismissRequest() },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text(stringResource(R.string.membership_alert_continue))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text(stringResource(R.string.membership_alert_back))
            }
        }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MembershipsScreenPreview() {

    com.example.greenfitapp.ui.theme.GreenFitAppTheme {
        MembershipsScreen()
    }
}
