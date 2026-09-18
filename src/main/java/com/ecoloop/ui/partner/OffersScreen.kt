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
import androidx.compose.material3.TextButton
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
import com.ecoloop.ui.components.Skeleton
import com.ecoloop.ui.household.formatDate

@Composable
fun OffersScreen(
    navController: NavHostController = rememberNavController()
) {
    val viewModel: OffersViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    PartnerScaffold(
        navController = navController,
        topBarTitle = "Offers"
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            if (uiState.isLoading) {
                repeat(6) {
                    Skeleton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp),
                        height = 80.dp,
                        radius = 12.dp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            } else if (uiState.offers.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "No offers available",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                uiState.offers.forEach { offer ->
                    OfferRow(
                        offer = offer,
                        onAccept = { viewModel.acceptOffer(offer.id) },
                        onReject = { reason -> viewModel.rejectOffer(offer.id, reason) }
                    )
                }
            }
        }
    }
}

@Composable
fun OfferRow(
    offer: Offer,
    onAccept: () -> Unit,
    onReject: (String) -> Unit
) {
    var showRejectDialog by rememberSaveable { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Offer #${offer.id.take(8)}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                EcoloopChip(
                    label = when (offer.status) {
                        "accepted" -> "Accepted"
                        "rejected" -> "Rejected"
                        "expired" -> "Expired"
                        else -> "Offered"
                    },
                    variant = when (offer.status) {
                        "accepted" -> ChipVariant.Success
                        "rejected" -> ChipVariant.Danger
                        "expired" -> ChipVariant.Neutral
                        else -> ChipVariant.Warning
                    }
                )
            }

            offer.expiresAt?.let {
                Text(
                    text = "Expires: ${formatDate(it)}",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            val canAct = offer.status == "offered"
            if (canAct) {
                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    EcoloopButton(
                        text = "Accept",
                        onClick = onAccept,
                        modifier = Modifier.weight(1f)
                    )
                    EcoloopButton(
                        text = "Reject",
                        onClick = { showRejectDialog = true },
                        variant = com.ecoloop.ui.components.ButtonVariant.Danger,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }

    if (showRejectDialog) {
        var reason by rememberSaveable { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showRejectDialog = false },
            title = { Text("Reject offer") },
            text = {
                Text(
                    text = "Please provide a reason for rejecting this offer:",
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (reason.isNotBlank()) {
                            onReject(reason)
                        }
                        showRejectDialog = false
                    }
                ) {
                    Text(
                        text = "Reject",
                        color = MaterialTheme.colorScheme.statusDanger,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showRejectDialog = false }) {
                    Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        )
    }
}
