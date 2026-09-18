package com.ecoloop.ui.partner

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ecoloop.domain.model.Job
import com.ecoloop.ui.components.ChipVariant
import com.ecoloop.ui.components.EcoloopChip
import com.ecoloop.ui.components.EcoloopSegmentControl
import com.ecoloop.ui.components.PartnerScaffold
import com.ecoloop.ui.components.Skeleton
import com.ecoloop.ui.household.formatDate

@Composable
fun JobsScreen(
    navController: NavHostController = rememberNavController(),
    onJobClick: (String) -> Unit = {}
) {
    val viewModel: JobsViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    var activeFilter by rememberSaveable { mutableStateOf("all") }

    val filteredJobs = if (activeFilter == "all") {
        uiState.jobs
    } else {
        uiState.jobs.filter { it.status == activeFilter }
    }

    PartnerScaffold(
        navController = navController,
        topBarTitle = "Jobs"
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            EcoloopSegmentControl(
                items = listOf("All", "Pending", "In Progress", "Completed"),
                selected = when (activeFilter) {
                    "all" -> "All"
                    "pending" -> "Pending"
                    "in_progress" -> "In Progress"
                    "completed" -> "Completed"
                    else -> "All"
                },
                onSelect = { selected ->
                    activeFilter = when (selected) {
                        "All" -> "all"
                        "Pending" -> "pending"
                        "In Progress" -> "in_progress"
                        "Completed" -> "completed"
                        else -> "all"
                    }
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
            } else if (filteredJobs.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "No jobs match this filter",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                filteredJobs.forEach { job ->
                    JobRow(
                        job = job,
                        onClick = { onJobClick(job.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun JobRow(
    job: Job,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(modifier = Modifier.padding(12.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = job.address ?: "No address",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                EcoloopChip(
                    label = job.status?.replace("_", " ")?.replaceFirstChar { it.uppercase() } ?: "—",
                    variant = when (job.status) {
                        "completed" -> ChipVariant.Success
                        "in_progress" -> ChipVariant.Info
                        "accepted" -> ChipVariant.Warning
                        "cancelled", "rejected" -> ChipVariant.Danger
                        else -> ChipVariant.Neutral
                    }
                )
                job.scheduledAt?.let {
                    Text(
                        text = "Scheduled: ${formatDate(it)}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}
