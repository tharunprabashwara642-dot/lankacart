package com.example.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.domain.model.UserProfile
import com.example.ui.theme.LankaJobsTheme

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onNavigateToSignIn: () -> Unit,
    onNavigateToEditProfile: () -> Unit,
    onNavigateToMyAds: () -> Unit,
    onNavigateToSavedJobs: () -> Unit,
    onNavigateToNotifications: () -> Unit,
    onSignedOut: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val tokens = LankaJobsTheme.tokens

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(tokens.background)
            .verticalScroll(rememberScrollState())
    ) {
        // Top Header
        Surface(
            color = tokens.surface,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                Text(
                    text = "My Profile",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = tokens.textPrimary
                )
            }
        }

        if (uiState.isSignedIn && uiState.userProfile != null) {
            SignedInContent(
                profile = uiState.userProfile!!,
                onEditProfile = onNavigateToEditProfile,
                onMyAds = onNavigateToMyAds,
                onSavedJobs = onNavigateToSavedJobs,
                onNotifications = onNavigateToNotifications,
                onSignOut = { viewModel.signOut { onSignedOut() } }
            )
        } else {
            SignedOutContent(
                isLoading = uiState.isLoading,
                onSignInClick = onNavigateToSignIn
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SignedInContent(
    profile: UserProfile,
    onEditProfile: () -> Unit,
    onMyAds: () -> Unit,
    onSavedJobs: () -> Unit,
    onNotifications: () -> Unit,
    onSignOut: () -> Unit
) {
    val tokens = LankaJobsTheme.tokens

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // User Info Card
        Card(
            colors = CardDefaults.cardColors(containerColor = tokens.surface),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, tokens.border),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .background(tokens.primary, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = profile.fullName.take(2).uppercase(),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = profile.fullName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = tokens.textPrimary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = profile.email,
                            style = MaterialTheme.typography.bodySmall,
                            color = tokens.textSecondary
                        )
                    }

                    OutlinedButton(
                        onClick = onEditProfile,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("edit_profile_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Edit", style = MaterialTheme.typography.labelSmall)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Metadata details
                if (profile.phoneNumber.isNotBlank()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.padding(vertical = 2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = tokens.textMuted
                        )
                        Text(
                            text = profile.phoneNumber,
                            style = MaterialTheme.typography.bodySmall,
                            color = tokens.textSecondary
                        )
                    }
                }

                if (profile.preferredLocation.isNotBlank()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.padding(vertical = 2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = tokens.textMuted
                        )
                        Text(
                            text = "Preferred: ${profile.preferredLocation}",
                            style = MaterialTheme.typography.bodySmall,
                            color = tokens.textSecondary
                        )
                    }
                }

                if (profile.preferredCategories.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        profile.preferredCategories.forEach { cat ->
                            Surface(
                                color = tokens.primaryContainer,
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = cat,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = tokens.primary,
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Action Menu Items
        Card(
            colors = CardDefaults.cardColors(containerColor = tokens.surface),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, tokens.border),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                ProfileMenuItem(
                    icon = Icons.Default.Campaign,
                    title = "My Advertisements",
                    subtitle = "Track approval status of your sponsored listings",
                    onClick = onMyAds,
                    testTag = "menu_my_advertisements"
                )
                ProfileMenuDivider()
                ProfileMenuItem(
                    icon = Icons.Default.Bookmark,
                    title = "Saved Jobs",
                    subtitle = "View offline and bookmarked career opportunities",
                    onClick = onSavedJobs,
                    testTag = "menu_saved_jobs"
                )
                ProfileMenuDivider()
                ProfileMenuItem(
                    icon = Icons.Default.Notifications,
                    title = "Notification Settings",
                    subtitle = "Manage alerts for new jobs and announcements",
                    onClick = onNotifications,
                    testTag = "menu_notifications"
                )
            }
        }

        // Security / Transparency Notice
        Card(
            colors = CardDefaults.cardColors(containerColor = tokens.surfaceElevated),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, tokens.border),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = tokens.primary
                )
                Text(
                    text = "LankaJobs protects your personal information. Your contact number is only shared when you submit verified employer inquiries.",
                    style = MaterialTheme.typography.bodySmall,
                    color = tokens.textSecondary,
                    lineHeight = 18.sp
                )
            }
        }

        // Sign Out Button
        OutlinedButton(
            onClick = onSignOut,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("sign_out_button"),
            shape = RoundedCornerShape(10.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, tokens.error)
        ) {
            Icon(
                imageVector = Icons.Default.Logout,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = tokens.error
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Sign Out",
                style = MaterialTheme.typography.labelLarge,
                color = tokens.error,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
private fun SignedOutContent(
    isLoading: Boolean,
    onSignInClick: () -> Unit
) {
    val tokens = LankaJobsTheme.tokens

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .size(72.dp)
                .background(tokens.primaryContainer, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.size(36.dp),
                tint = tokens.primary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Welcome to LankaJobs",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = tokens.textPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Sign in to unlock verified advertising, bookmark synchronization, and tailored job recommendations across Sri Lanka.",
            style = MaterialTheme.typography.bodyMedium,
            color = tokens.textSecondary,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp,
            modifier = Modifier.padding(horizontal = 12.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = tokens.surface),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, tokens.border),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                BenefitItem(text = "Submit paid promotional ads and training programs")
                BenefitItem(text = "Sync your saved jobs seamlessly across all devices")
                BenefitItem(text = "Set custom alerts for roles matching your location")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onSignInClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("profile_continue_with_google_button"),
            colors = ButtonDefaults.buttonColors(containerColor = tokens.primary),
            shape = RoundedCornerShape(10.dp),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Sign In with Google",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun BenefitItem(text: String) {
    val tokens = LankaJobsTheme.tokens
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = tokens.success
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = tokens.textPrimary
        )
    }
}

@Composable
private fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    testTag: String
) {
    val tokens = LankaJobsTheme.tokens
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp)
            .testTag(testTag),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(tokens.primaryContainer, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = tokens.primary
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = tokens.textPrimary
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = tokens.textSecondary
            )
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = tokens.textMuted
        )
    }
}

@Composable
private fun ProfileMenuDivider() {
    val tokens = LankaJobsTheme.tokens
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(tokens.border)
    )
}
