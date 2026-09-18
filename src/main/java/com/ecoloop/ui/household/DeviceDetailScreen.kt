package com.ecoloop.ui.household

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.ecoloop.mobile.R
import com.ecoloop.ui.components.ButtonVariant
import com.ecoloop.ui.components.ChipVariant
import com.ecoloop.ui.components.EcoloopButton
import com.ecoloop.ui.components.EcoloopChip
import com.ecoloop.ui.components.HouseholdScaffold
import com.ecoloop.ui.components.TimelineStep
import com.ecoloop.ui.components.TimelineView
import androidx.navigation.NavHostController

@Composable
fun DeviceDetailScreen(
    navController: NavHostController = androidx.navigation.compose.rememberNavController(),
    deviceId: String
) {
    val viewModel: DeviceDetailViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    if (deviceId.isNotBlank()) {
        viewModel.load(deviceId)
    }

    HouseholdScaffold(
        navController = navController,
        topBarTitle = "Device detail"
    ) { innerPadding ->
        val device = uiState.device
        val pickup = uiState.pickup

        if (uiState.isLoading || device == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.material3.CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    if (device.imageUrl != null) {
                        AsyncImage(
                            model = device.imageUrl,
                            contentDescription = "Device image",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                            placeholder = painterResource(R.drawable.ic_baseline_device)
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.surfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "📷", fontSize = 48.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = device.displayCategory,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Row(
                            modifier = Modifier.padding(top = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            EcoloopChip(
                                label = device.condition?.replace("_", " ")
                                    ?.replaceFirstChar { it.uppercase() } ?: "—",
                                variant = when (device.condition) {
                                    "working" -> com.ecoloop.ui.components.ChipVariant.Success
                                    "slightly_damaged" -> com.ecoloop.ui.components.ChipVariant.Warning
                                    "fully_damaged" -> com.ecoloop.ui.components.ChipVariant.Danger
                                    else -> com.ecoloop.ui.components.ChipVariant.Neutral
                                }
                            )
                            device.aiStatus?.let { aiStatus ->
                                EcoloopChip(
                                    label = when (aiStatus) {
                                        "pending" -> "Analyzing"
                                        "processing" -> "Processing"
                                        "completed" -> "Done"
                                        "failed" -> "Failed"
                                        else -> aiStatus
                                    },
                                    variant = when (aiStatus) {
                                        "completed" -> com.ecoloop.ui.components.ChipVariant.Success
                                        "pending", "processing" -> com.ecoloop.ui.components.ChipVariant.Warning
                                        "failed" -> com.ecoloop.ui.components.ChipVariant.Danger
                                        else -> com.ecoloop.ui.components.ChipVariant.Neutral
                                    }
                                )
                            }
                        }

                        device.aiCategory?.let { aiCat ->
                            Column(modifier = Modifier.padding(top = 12.dp)) {
                                Text(
                                    text = "AI classification",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textTransform = androidx.compose.ui.text.uppercase(
                                        androidx.compose.ui.text.intl.Locale.current
                                    )
                                )
                                Row(modifier = Modifier.padding(top = 4.dp)) {
                                    Text(
                                        text = "Category: ",
                                        fontSize = 13.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = aiCat,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                device.aiConfidence?.let { conf ->
                                    Row(modifier = Modifier.padding(top = 2.dp)) {
                                        Text(
                                            text = "Confidence: ",
                                            fontSize = 13.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            text = "${(conf * 100).roundToInt()}%",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        }

                        device.createdAt?.let { createdAt ->
                            Row(
                                modifier = Modifier
                                    .padding(top = 12.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Submitted",
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = formatDate(createdAt),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Pickup timeline",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        val steps = buildTimeline(device, pickup)
                        TimelineView(steps = steps)
                    }
                }

                val status = pickup?.status ?: device.status
                val canCancel = status == "pending" || status == "in_progress" ||
                    status == "accepted" || status == "submitted"

                if (canCancel) {
                    Spacer(modifier = Modifier.height(16.dp))
                    var showDialog by rememberSaveable { mutableStateOf(false) }
                    EcoloopButton(
                        text = "Cancel pickup",
                        onClick = { showDialog = true },
                        variant = com.ecoloop.ui.components.ButtonVariant.Danger,
                        fullWidth = true
                    )
                    if (showDialog) {
                        AlertDialog(
                            onDismissRequest = { showDialog = false },
                            title = { Text("Cancel pickup") },
                            text = { Text("Are you sure you want to cancel this device submission?") },
                            confirmButton = {
                                Text(
                                    text = "Cancel pickup",
                                    color = MaterialTheme.colorScheme.statusDanger,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.clickable {
                                        viewModel.cancelPickup(device.id)
                                        showDialog = false
                                    }
                                )
                            },
                            dismissButton = {
                                Text(
                                    text = "Keep it",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.clickable { showDialog = false }
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

fun buildTimeline(
    device: com.ecoloop.domain.model.Device,
    pickup: com.ecoloop.domain.model.Pickup?
): List<TimelineStep> {
    return listOf(
        TimelineStep(
            key = "submitted",
            label = "Submitted",
            done = true,
            timestamp = device.createdAt
        ),
        TimelineStep(
            key = "assigned",
            label = if (pickup?.partnerId != null) "Assigned to partner" else "Assigned to partner",
            done = pickup != null && (pickup.status == "accepted" || pickup.status == "picked_up" ||
                pickup.status == "completed"),
            timestamp = pickup?.scheduledAt
        ),
        TimelineStep(
            key = "picked_up",
            label = "Picked up",
            done = pickup != null && (pickup.status == "picked_up" || pickup.status == "completed"),
            timestamp = null
        ),
        TimelineStep(
            key = "completed",
            label = "Completed",
            done = pickup?.status == "completed" || device.status == "completed",
            timestamp = pickup?.completedAt
        )
    )
}

fun formatDate(isoString: String): String {
    return try {
        val instant = java.time.Instant.parse(isoString)
        val formatter = java.time.format.DateTimeFormatter
            .ofPattern("MMM d, yyyy 'at' h:mm a")
            .withZone(java.time.ZoneId.systemDefault())
        formatter.format(instant)
    } catch (e: Exception) {
        isoString
    }
}

fun Double.roundToInt(): Int = kotlin.math.roundToInt(this)
