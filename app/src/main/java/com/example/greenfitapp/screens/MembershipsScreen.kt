package com.example.greenfitapp.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.greenfitapp.R
import com.example.greenfitapp.data.MembershipItem
import com.example.greenfitapp.data.membershipList
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun MembershipsScreen(modifier: Modifier = Modifier) {
    LazyColumn() {
        items(membershipList) { membership ->
            MembershipCard(membership)
        }
    }
}

@Composable
fun MembershipCard(
    membership: MembershipItem,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
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
                Button(onClick = {/* Nothing */ }) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = stringResource(R.string.buyButton)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = stringResource(R.string.buyButton)
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MembershipsScreenPreview() {

    com.example.greenfitapp.ui.theme.GreenFitAppTheme {
        MembershipsScreen()
    }
}
