package com.example.presentation.ads

import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.SeedData
import com.example.domain.model.AdvertisementPackage
import com.example.presentation.components.LankaJobsTopBar
import com.example.ui.theme.LankaJobsTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CreateAdScreen(
    viewModel: AdsViewModel,
    onSuccess: (String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val tokens = LankaJobsTheme.tokens
    val context = LocalContext.current

    var title by remember { mutableStateOf("") }
    var orgName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Education & Training") }
    var location by remember { mutableStateOf("Colombo") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var websiteUrl by remember { mutableStateOf("") }

    val packages = uiState.packages
    var selectedPackage by remember(packages) {
        mutableStateOf(packages.firstOrNull { it.isPopular } ?: packages.firstOrNull())
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            LankaJobsTopBar(
                title = "Create Advertisement",
                onBackClick = onBackClick
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(tokens.background)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Admin Workflow Notice Card
            Card(
                colors = CardDefaults.cardColors(containerColor = tokens.primaryContainer),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp).padding(top = 2.dp),
                        tint = tokens.primary
                    )
                    Column {
                        Text(
                            text = "Admin Review & Approval Policy",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = tokens.primary
                        )
                        Text(
                            text = "Your submission will enter 'Pending Approval' status. Once our team verifies your details on the LankaJobs platform, your listing will be published.",
                            style = MaterialTheme.typography.bodySmall,
                            color = tokens.textPrimary,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            // Step 1: Advertisement Content
            Card(
                colors = CardDefaults.cardColors(containerColor = tokens.surface),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, tokens.border),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "1. Announcement Information",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = tokens.textPrimary
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Advertisement Title *",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = tokens.textPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        placeholder = { Text("e.g. Diploma in AI & Cloud Engineering") },
                        modifier = Modifier.fillMaxWidth().testTag("ad_title_input"),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Organization / Business Name *",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = tokens.textPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = orgName,
                        onValueChange = { orgName = it },
                        placeholder = { Text("e.g. Lanka Institute of Technology") },
                        modifier = Modifier.fillMaxWidth().testTag("ad_org_input"),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Description & Offer Details *",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = tokens.textPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        placeholder = { Text("Describe the program, eligibility, schedule, and key highlights...") },
                        modifier = Modifier.fillMaxWidth().height(110.dp).testTag("ad_desc_input"),
                        shape = RoundedCornerShape(8.dp),
                        maxLines = 5
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Category",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = tokens.textPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    val adCategories = listOf("Education & Training", "Recruitment & Events", "Accounting & Finance", "IT & Technology", "Services")
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        adCategories.forEach { cat ->
                            FilterChip(
                                selected = category == cat,
                                onClick = { category = cat },
                                label = { Text(cat, style = MaterialTheme.typography.labelSmall) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = tokens.primaryContainer,
                                    selectedLabelColor = tokens.primary
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Location / District",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = tokens.textPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SeedData.locations.forEach { loc ->
                            FilterChip(
                                selected = location == loc,
                                onClick = { location = loc },
                                label = { Text(loc, style = MaterialTheme.typography.labelSmall) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = tokens.primaryContainer,
                                    selectedLabelColor = tokens.primary
                                )
                            )
                        }
                    }
                }
            }

            // Step 2: Contact Details & Optional Website
            Card(
                colors = CardDefaults.cardColors(containerColor = tokens.surface),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, tokens.border),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "2. Contact & Links",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = tokens.textPrimary
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Inquiry Phone Number",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = tokens.textPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        placeholder = { Text("e.g. +94 11 234 5678") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        leadingIcon = { Icon(imageVector = Icons.Default.Phone, contentDescription = null, tint = tokens.textMuted) },
                        modifier = Modifier.fillMaxWidth().testTag("ad_phone_input"),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Official Website URL (Optional)",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = tokens.textPrimary
                    )
                    Text(
                        text = "If provided, a 'Visit Official Website' button will appear on your listing.",
                        style = MaterialTheme.typography.bodySmall,
                        color = tokens.textSecondary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = websiteUrl,
                        onValueChange = { websiteUrl = it },
                        placeholder = { Text("https://your-academy.lk") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
                        leadingIcon = { Icon(imageVector = Icons.Default.Link, contentDescription = null, tint = tokens.textMuted) },
                        modifier = Modifier.fillMaxWidth().testTag("ad_website_input"),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true
                    )
                }
            }

            // Step 3: Advertising Package Selection
            Card(
                colors = CardDefaults.cardColors(containerColor = tokens.surface),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, tokens.border),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "3. Select Placement Package",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = tokens.textPrimary
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    packages.forEach { pkg ->
                        val isSelected = selectedPackage?.id == pkg.id
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) tokens.primaryContainer else tokens.surfaceElevated
                            ),
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                if (isSelected) 2.dp else 1.dp,
                                if (isSelected) tokens.primary else tokens.border
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable { selectedPackage = pkg }
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(
                                            text = pkg.name,
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = tokens.textPrimary
                                        )
                                        if (pkg.isPopular) {
                                            Surface(
                                                color = tokens.warning,
                                                shape = RoundedCornerShape(4.dp)
                                            ) {
                                                Text(
                                                    text = "POPULAR",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = Color.White,
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                    }

                                    Text(
                                        text = pkg.formattedPrice,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = tokens.primary
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Valid for ${pkg.durationDays} days",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = tokens.textSecondary
                                )

                                Spacer(modifier = Modifier.height(8.dp))
                                pkg.features.forEach { feat ->
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                                        modifier = Modifier.padding(vertical = 2.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            modifier = Modifier.size(13.dp),
                                            tint = tokens.primary
                                        )
                                        Text(
                                            text = feat,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = tokens.textSecondary,
                                            fontSize = 12.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Submit Button
            Button(
                onClick = {
                    if (title.isBlank()) {
                        Toast.makeText(context, "Please enter an advertisement title", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    if (orgName.isBlank()) {
                        Toast.makeText(context, "Please enter your organization name", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    if (description.isBlank()) {
                        Toast.makeText(context, "Please enter the description", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    val pkg = selectedPackage ?: packages.first()

                    viewModel.submitAdvertisement(
                        title = title,
                        organizationName = orgName,
                        description = description,
                        category = category,
                        location = location,
                        phone = phone,
                        email = email,
                        websiteUrl = websiteUrl,
                        selectedPackage = pkg,
                        onSuccess = { id ->
                            Toast.makeText(context, "Advertisement submitted for Admin Approval!", Toast.LENGTH_LONG).show()
                            onSuccess(id)
                        }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("submit_ad_button"),
                colors = ButtonDefaults.buttonColors(containerColor = tokens.primary),
                shape = RoundedCornerShape(10.dp),
                enabled = !uiState.isSubmitting
            ) {
                if (uiState.isSubmitting) {
                    CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp, modifier = Modifier.size(20.dp))
                } else {
                    Text(
                        text = "Submit for Admin Review",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
