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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import com.ecoloop.domain.model.Offer
import com.ecoloop.ui.components.ChipVariant
import com.ecoloop.ui.components.EcoloopButton
import com.ecoloop.ui.components.EcoloopChip
import com.ecoloop.ui.components.PartnerScaffold
import com.ecoloop.ui.household.formatDate
import com.ecoloop.ui.components.Skeleton

@Composable
fun PartnerHomeScreen(
    navController: NavHostController = rememberNavController()
) {
    val viewModel: PartnerHomeViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    PartnerScaffold(
        navController = navController,
        topBarTitle = "Partner Dashboard"
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            if (uiState.isLoading) {
                KpiSkeletons()
            } else {
                uiState.kpis?.let { kpis ->
                    KPICard(
                        title = "Offers today",
                        value = kpis.offersToday.toString(),
                        subtitle = "Active jobs: ${kpis.activeJobs.toString()}"
                    )
                    KPICard(
                        title = "Monthly completions",
                        value = kpis.monthlyCompletions.toString(),
                        subtitle = "Points balance: ${kpis.pointsBalance.toString()}"
                    )
                    KPICard(
                        title = "Capacity",
                        value = "${kpis.capacityUsed}/${kpis.capacityTotal}",
                        subtitle = kpis.nextOfferAt?.let { "Next offer: ${formatDate(it)}" } ?: "No upcoming offers"
                    )
                } ?: run {
                    Text(
                        text = "No KPI data available",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun KPICard(
    title: String,
    value: String,
    subtitle: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(top = 4.dp)
            )
            Text(
                text = subtitle,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
fun KpiSkeletons() {
    repeat(3) {
        Skeleton(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            height = 80.dp,
            radius = 12.dp
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}
