package com.example.presentation.ads

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.AdStatus
import com.example.domain.model.Advertisement
import com.example.domain.repository.AdvertisementRepository
import com.example.presentation.components.EmptyState
import com.example.presentation.components.LankaJobsTopBar
import com.example.ui.theme.LankaJobsTheme

@Composable
fun MyAdsScreen(
    userId: String,
    advertisementRepository: AdvertisementRepository,
    onAdClick: (String) -> Unit,
    onCreateAdClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val userAds by advertisementRepository.getUserAdvertisements(userId).collectAsState(initial = emptyList())
    val tokens = LankaJobsTheme.tokens

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            LankaJobsTopBar(
                title = "My Advertisements",
                onBackClick = onBackClick
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(tokens.background)
        ) {
            if (userAds.isEmpty()) {
                EmptyState(
                    title = "No Advertisements Submitted",
                    subtitle = "Promote your courses, career fairs, or corporate recruitment programs on LankaJobs.",
                    actionLabel = "Create Advertisement",
                    onActionClick = onCreateAdClick,
                    icon = Icons.Default.Campaign,
                    modifier = Modifier.weight(1f)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("my_ads_list"),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(userAds, key = { it.id }) { ad ->
                        MyAdItemCard(ad = ad, onClick = { onAdClick(ad.id) })
                    }
                }
            }
        }
    }
}

@Composable
private fun MyAdItemCard(
    ad: Advertisement,
    onClick: () -> Unit
) {
    val tokens = LankaJobsTheme.tokens
    val statusColor = when (ad.status) {
        AdStatus.APPROVED -> tokens.success
        AdStatus.PENDING_APPROVAL -> tokens.warning
        AdStatus.REJECTED -> tokens.error
        else -> tokens.secondary
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = tokens.surface),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, tokens.border),
        modifier = Modifier.fillMaxWidth().testTag("my_ad_card_${ad.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = statusColor.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = ad.status.label.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = statusColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                Text(
                    text = ad.packageName,
                    style = MaterialTheme.typography.bodySmall,
                    color = tokens.textMuted
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = ad.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = tokens.textPrimary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${ad.organizationName} • ${ad.location}",
                style = MaterialTheme.typography.bodySmall,
                color = tokens.textSecondary
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (ad.status == AdStatus.PENDING_APPROVAL) {
                Text(
                    text = "Awaiting review from LankaJobs platform administration team.",
                    style = MaterialTheme.typography.labelSmall,
                    color = tokens.warning,
                    fontSize = 11.sp
                )
            }
        }
    }
}
