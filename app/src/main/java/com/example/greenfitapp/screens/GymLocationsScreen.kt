package com.example.greenfitapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.greenfitapp.R
import com.example.greenfitapp.data.GymLocation
import com.example.greenfitapp.data.locationsList
import com.example.greenfitapp.ui.theme.GreenFitAppTheme

@Composable
fun GymLocationsScreen(modifier: Modifier = Modifier) {
    LazyColumn() {
        items(locationsList){ gymLocation ->
            GymLocationCard(gymLocation = gymLocation)
        }
    }
}

@Composable
fun GymLocationCard(
    gymLocation: GymLocation,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Image(
                painter = painterResource(gymLocation.imageId),
                contentDescription = stringResource(gymLocation.gymDescriptionRes),
                contentScale = ContentScale.Crop,
                modifier = Modifier

                    .aspectRatio(16f / 9f)
                    .fillMaxWidth()
            )
            Text(
                text = "${stringResource(gymLocation.nameRes)} • ★ ${gymLocation.rating} • (${gymLocation.reviewsCount})" ,
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = "${stringResource(gymLocation.gymAddressRes)} ",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = "${stringResource(gymLocation.gymDescriptionRes)} ",
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = 20.sp,
                textAlign = TextAlign.Justify,
            )

        }
    }
}

@Preview(showBackground = true)
@Composable
fun GymLocationCardPreview(){
    GreenFitAppTheme() {
        GymLocationCard(
            GymLocation(
                id = 3,
                nameRes = R.string.location_central_title,
                gymAddressRes = R.string.location_central_address,
                gymDescriptionRes = R.string.location_central_desc,
                rating = 4.6,
                reviewsCount = 289,
                imageId = R.drawable.greenfit_central
            )
        )
    }
}