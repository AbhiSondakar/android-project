package com.ecoloop.ui.partner

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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ecoloop.ui.components.PartnerScaffold
import com.ecoloop.ui.components.Skeleton

@Composable
fun PartnerProfileScreen(
    navController: NavHostController = rememberNavController(),
    onLogout: () -> Unit = {}
) {
    val viewModel: PartnerProfileViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    PartnerScaffold(
        navController = navController,
        topBarTitle = "Partner Profile"
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            if (uiState.isLoading) {
                repeat(6) {
                    Skeleton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        height = 56.dp,
                        radius = 12.dp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            } else {
                uiState.partner?.let { partner ->
                    Text(
                        text = partner.orgName ?: "Organization",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    ProfileInfoRow("Type", partner.type ?: "—")
                    ProfileInfoRow("Status", partner.status ?: "—")
                    partner.licenseNo?.let { ProfileInfoRow("License", it) }
                    partner.serviceAreas?.let { ProfileInfoRow("Service areas", it) }
                    partner.capabilities?.let { ProfileInfoRow("Capabilities", it) }
                    partner.capacity?.let { ProfileInfoRow("Capacity", it.toString()) }
                    partner.rating?.let { ProfileInfoRow("Rating", String.format("%.1f", it)) }
                    partner.activeJobCount?.let { ProfileInfoRow("Active jobs", it.toString()) }

                    Spacer(modifier = Modifier.height(24.dp))

                    TextButton(
                        onClick = onLogout,
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(vertical = 12.dp)
                    ) {
                        Text(
                            text = "Logout",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileInfoRow(
    label: String,
    value: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
