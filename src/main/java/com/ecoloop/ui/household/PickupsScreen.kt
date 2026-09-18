package com.ecoloop.ui.household

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ecoloop.ui.components.ChipVariant
import com.ecoloop.ui.components.EcoloopChip
import com.ecoloop.ui.components.EcoloopSegmentControl
import com.ecoloop.ui.components.HouseholdScaffold
import com.ecoloop.ui.components.Skeleton
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun PickupsScreen(
    navController: NavHostController = androidx.navigation.compose.rememberNavController()
) {
    val viewModel: PickupsViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HouseholdScaffold(
        navController = navController,
        topBarTitle = "Pickups"
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            EcoloopSegmentControl(
                items = listOf("Active", "History"),
                selected = uiState.activeSegment,
                onSelect = { segment ->
                    val s = if (segment == "Active") "active" else "history"
                    viewModel.setSegment(s)
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (uiState.isLoading) {
                repeat(3) {
                    Skeleton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(96.dp),
                        height = 96.dp,
                        radius = 12.dp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            } else if (uiState.pickups.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = if (uiState.activeSegment == "active") "No active pickups" else "No pickup history",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                uiState.pickups.forEach { pickup ->
                    PickupRow(pickup)
                }
            }
        }
    }
}

@Composable
fun PickupRow(pickup: com.ecoloop.domain.model.Pickup) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Pickup #${pickup.id.take(8)}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                EcoloopChip(
                    label = when (pickup.status) {
                        "pending", "submitted" -> "Pending"
                        "accepted" -> "Accepted"
                        "completed" -> "Done"
                        "cancelled" -> "Cancelled"
                        "rejected" -> "Rejected"
                        else -> pickup.status ?: "—"
                    },
                    variant = when (pickup.status) {
                        "completed" -> ChipVariant.Success
                        "accepted" -> ChipVariant.Warning
                        "cancelled", "rejected" -> ChipVariant.Danger
                        "in_progress" -> ChipVariant.Info
                        else -> ChipVariant.Neutral
                    }
                )
            }

            pickup.address?.takeIf { it.isNotBlank() }?.let { addr ->
                Text(
                    text = "Address: $addr",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            pickup.scheduledAt?.let { scheduled ->
                Text(
                    text = "Scheduled: ${formatDate(scheduled)}",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}
