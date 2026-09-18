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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ecoloop.ui.components.ButtonVariant
import com.ecoloop.ui.components.ChipVariant
import com.ecoloop.ui.components.EcoloopButton
import com.ecoloop.ui.components.EcoloopCard
import com.ecoloop.ui.components.EcoloopChip
import com.ecoloop.ui.components.HouseholdScaffold
import com.ecoloop.ui.components.Skeleton
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun PointsScreen(
    navController: NavHostController = androidx.navigation.compose.rememberNavController()
) {
    val viewModel: PointsViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var showRedeem by rememberSaveable { mutableStateOf(false) }

    HouseholdScaffold(
        navController = navController,
        topBarTitle = "Points"
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Available balance",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.sp
                        )
                    )
                    if (uiState.isLoading) {
                        Skeleton(
                            modifier = Modifier
                                .fillMaxWidth(0.4f)
                                .height(32.dp),
                            height = 32.dp,
                            radius = 4.dp
                        )
                    } else {
                        Text(
                            text = uiState.balance.toString(),
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                    EcoloopButton(
                        text = "Redeem rewards",
                        onClick = { showRedeem = true },
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Ledger history",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (uiState.isLoading) {
                repeat(5) {
                    Skeleton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        height = 64.dp,
                        radius = 12.dp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            } else if (uiState.ledger.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "No points history yet",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                uiState.ledger.forEach { entry ->
                    LedgerRow(entry)
                }
            }
        }
    }

    if (showRedeem) {
        RedeemDialog(
            balance = uiState.balance,
            catalog = uiState.catalog,
            onDismiss = { showRedeem = false },
            onRedeemed = {
                showRedeem = false
                viewModel.refresh()
            }
        )
    }
}

@Composable
fun LedgerRow(entry: com.ecoloop.domain.model.PointsEntry) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = entry.description ?: "",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = formatDate(entry.createdAt ?: ""),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            EcoloopChip(
                label = if (entry.points >= 0) "+${entry.points}" else entry.points.toString(),
                variant = if (entry.points >= 0) ChipVariant.Success else ChipVariant.Warning
            )
        }
    }
}

@Composable
fun RedeemDialog(
    balance: Int,
    catalog: List<com.ecoloop.domain.model.RewardCatalogItem>,
    onDismiss: () -> Unit,
    onRedeemed: () -> Unit
) {
    var selected by rememberSaveable { mutableStateOf<String?>(null) }
    val viewModel: PointsViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text(
                    text = "Redeem points",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "$balance points available",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        },
        text = {
            if (catalog.isEmpty()) {
                Text("No rewards available right now.")
            } else {
                Column {
                    catalog.forEach { reward ->
                        val affordable = balance >= reward.pointsCost
                        val isSelected = selected == reward.id
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .let {
                                    if (affordable) it.clickable {
                                        selected = if (isSelected) null else reward.id
                                    } else it
                                },
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer
                                    else MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Row(modifier = Modifier.padding(12.dp)) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = reward.name,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "${reward.pointsCost} points",
                                        fontSize = 13.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                if (affordable && isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.align(Alignment.CenterVertically)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            val redeemable = catalog.find { it.id == selected }
            EcoloopButton(
                text = if (state.isRedeeming) "Redeeming..." else "Redeem",
                onClick = {
                    redeemable?.let { reward ->
                        viewModel.redeem(reward)
                    }
                },
                enabled = redeemable != null && !state.isRedeeming,
                isLoading = state.isRedeeming,
                fullWidth = true
            )
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Cancel",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    )

    if (state.redeemSuccess) {
        onRedeemed()
        LaunchedEffect(Unit) {
            viewModel.clearRedeemStatus()
        }
    }
}


