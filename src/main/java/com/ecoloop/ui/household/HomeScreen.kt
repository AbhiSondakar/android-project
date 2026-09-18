package com.ecoloop.ui.household

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ecoloop.R
import com.ecoloop.ui.components.EcoloopButton
import com.ecoloop.ui.components.EcoloopCard
import com.ecoloop.ui.components.EcoloopChip
import com.ecoloop.ui.components.EcoloopSegmentControl
import com.ecoloop.ui.components.HouseholdScaffold
import com.ecoloop.ui.components.Skeleton
import com.ecoloop.ui.components.TimelineView
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun HomeScreen(
    navController: NavHostController = androidx.navigation.compose.rememberNavController(),
    onDeviceClick: (String) -> Unit,
    onPickupsClick: () -> Unit,
    onSubmitDeviceClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onLogout: () -> Unit
) {
    val viewModel: HouseholdHomeViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val user by viewModel.user.collectAsStateWithLifecycle()

    HouseholdScaffold(
        navController = navController,
        topBarTitle = "ECOLOOP",
        topBarActions = {
            IconButton(onClick = onNotificationsClick) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notifications",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        floatingActionButton = {
            EcoloopButton(
                text = "Submit device",
                onClick = onSubmitDeviceClick,
                modifier = Modifier.padding(16.dp)
            )
        }
    ) { innerPadding ->
        val scrollState = androidx.compose.foundation.rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            Text(
                text = "Hello, ${user?.name ?: "there"}!",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Points balance",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp
                        )
                    )
                    Text(
                        text = uiState.pointsBalance.toString(),
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    TextButton(
                        onClick = { onPickupsClick() },
                        modifier = Modifier.padding(top = 4.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = "View ledger",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            EcoloopButton(
                text = "Submit device",
                onClick = onSubmitDeviceClick,
                fullWidth = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Recent devices",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            if (uiState.isLoading) {
                repeat(3) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Skeleton(
                            modifier = Modifier.size(56.dp),
                            height = 56.dp,
                            radius = 8.dp
                        )
                        Column(
                            modifier = Modifier
                                .padding(start = 12.dp)
                                .weight(1f)
                        ) {
                            Skeleton(
                                modifier = Modifier
                                    .fillMaxWidth(0.8f)
                                    .height(16.dp),
                                height = 16.dp,
                                radius = 4.dp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Skeleton(
                                modifier = Modifier
                                    .fillMaxWidth(0.5f)
                                    .height(14.dp),
                                height = 14.dp,
                                radius = 4.dp
                            )
                        }
                    }
                }
            } else if (uiState.recentDevices.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "No devices submitted yet",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                uiState.recentDevices.forEach { device ->
                    androidx.compose.material3.OutlinedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { onDeviceClick(device.id) },
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp, MaterialTheme.colorScheme.outline
                        ),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Row(modifier = Modifier.padding(12.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "📷", fontSize = 24.sp)
                            }
                            Column(
                                modifier = Modifier
                                    .padding(start = 12.dp)
                                    .weight(1f)
                            ) {
                                Text(
                                    text = device.displayCategory,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Row(
                                    modifier = Modifier.padding(top = 4.dp),
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
                                    EcoloopChip(
                                        label = device.status?.replace("_", " ")
                                            ?.replaceFirstChar { it.uppercase() } ?: "—",
                                        variant = when (device.status) {
                                            "completed" -> com.ecoloop.ui.components.ChipVariant.Success
                                            "accepted" -> com.ecoloop.ui.components.ChipVariant.Warning
                                            "rejected" -> com.ecoloop.ui.components.ChipVariant.Danger
                                            "in_progress" -> com.ecoloop.ui.components.ChipVariant.Info
                                            else -> com.ecoloop.ui.components.ChipVariant.Neutral
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
