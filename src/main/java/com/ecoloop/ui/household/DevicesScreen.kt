package com.ecoloop.ui.household

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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

@Composable
fun DevicesScreen(
    navController: NavHostController,
    onDeviceClick: (String) -> Unit
) {
    val viewModel: DevicesViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val filteredDevices by viewModel.filteredDevices.collectAsState()

    HouseholdScaffold(
        navController = navController,
        topBarTitle = "Devices"
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            EcoloopSegmentControl(
                items = listOf("All", "Pending", "In progress", "Done"),
                selected = uiState.activeFilter,
                onSelect = { filter ->
                    val f = when (filter) {
                        "All" -> "all"
                        "Pending" -> "pending"
                        "In progress" -> "in_progress"
                        "Done" -> "completed"
                        else -> "all"
                    }
                    viewModel.setFilter(f)
                    viewModel.refresh()
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (uiState.isLoading) {
                repeat(4) {
                    Skeleton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(72.dp),
                        height = 72.dp,
                        radius = 12.dp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            } else if (filteredDevices.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "No devices match this filter",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                filteredDevices.forEach { device ->
                    DeviceRow(device, onDeviceClick)
                }
            }
        }
    }
}

@Composable
fun DeviceRow(device: com.ecoloop.domain.model.Device, onClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick(device.id) },
        shape = RoundedCornerShape(12.dp),
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = device.displayCategory,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    EcoloopChip(
                        label = device.status?.replace("_", " ")
                            ?.replaceFirstChar { it.uppercase() } ?: "—",
                        variant = when (device.status) {
                            "completed" -> ChipVariant.Success
                            "accepted" -> ChipVariant.Warning
                            "rejected" -> ChipVariant.Danger
                            "in_progress" -> ChipVariant.Info
                            else -> ChipVariant.Neutral
                        }
                    )
                }
                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    EcoloopChip(
                        label = device.condition?.replace("_", " ")
                            ?.replaceFirstChar { it.uppercase() } ?: "—",
                        variant = when (device.condition) {
                            "working" -> ChipVariant.Success
                            "slightly_damaged" -> ChipVariant.Warning
                            "fully_damaged" -> ChipVariant.Danger
                            else -> ChipVariant.Neutral
                        }
                    )
                    Text(
                        text = device.createdAt?.let {
                            java.text.SimpleDateFormat("MMM d, yyyy").format(
                                java.util.Date.parse(it)
                            )
                        } ?: "",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
