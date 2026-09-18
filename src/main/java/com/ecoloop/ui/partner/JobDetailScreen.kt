package com.ecoloop.ui.partner

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import coil.compose.rememberImagePainter
import com.ecoloop.mobile.R
import com.ecoloop.ui.components.ChipVariant
import com.ecoloop.ui.components.EcoloopButton
import com.ecoloop.ui.components.EcoloopChip
import com.ecoloop.ui.components.PartnerScaffold
import com.ecoloop.ui.household.formatDate
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun JobDetailScreen(
    navController: NavHostController = rememberNavController(),
    jobId: String
) {
    val viewModel: JobDetailViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    if (jobId.isNotBlank()) {
        viewModel.load(jobId)
    }

    LaunchedEffect(uiState.verifySuccess) {
        if (uiState.verifySuccess) {
            viewModel.clearVerifySuccess()
        }
    }

    PartnerScaffold(
        navController = navController,
        topBarTitle = "Job detail"
    ) { innerPadding ->
        val job = uiState.job

        if (uiState.isLoading || job == null) {
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
                Text(
                    text = job.address ?: "No address",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    EcoloopChip(
                        label = job.status?.replace("_", " ")?.replaceFirstChar { it.uppercase() } ?: "Pending",
                        variant = when (job.status) {
                            "completed" -> ChipVariant.Success
                            "in_progress" -> ChipVariant.Info
                            "accepted" -> ChipVariant.Warning
                            else -> ChipVariant.Neutral
                        }
                    )
                    job.scheduledAt?.let { scheduled ->
                        EcoloopChip(
                            label = "Scheduled ${formatDate(scheduled)}",
                            variant = ChipVariant.Neutral
                        )
                    }
                }

                job.completedAt?.let { completed ->
                    Text(
                        text = "Completed: ${formatDate(completed)}",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                job.aiCategory?.let { aiCat ->
                    Column(modifier = Modifier.padding(top = 12.dp)) {
                        Text(
                            text = "AI classification",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = aiCat,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                if (job.status != "completed") {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Verification",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    VerificationForm(
                        uiState = uiState,
                        onConditionToggle = { viewModel.setConditionOk(it) },
                        onEvidencePick = { uri -> viewModel.setEvidence(uri) },
                        onNotesChange = { viewModel.setNotes(it) },
                        onVerify = { viewModel.verifyAndComplete() },
                        evidenceUri = uiState.evidenceUri
                    )
                }

                uiState.error?.let { error ->
                    Text(
                        text = error,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun VerificationForm(
    uiState: JobDetailUiState,
    onConditionToggle: (Boolean) -> Unit,
    onEvidencePick: (Uri) -> Unit,
    onNotesChange: (String) -> Unit,
    onVerify: () -> Unit,
    evidenceUri: Uri?
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { onEvidencePick(it) }
    }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Device condition matches",
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            var conditionOk by rememberSaveable { mutableStateOf(uiState.conditionOk) }
            androidx.compose.material3.Switch(
                checked = conditionOk,
                onCheckedChange = {
                    conditionOk = it
                    onConditionToggle(it)
                }
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .padding(top = 12.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                .clickable { launcher.launch("image/*") }
        ) {
            if (evidenceUri != null) {
                Image(
                    painter = rememberImagePainter(evidenceUri),
                    contentDescription = "Evidence photo",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter = painterResource(R.drawable.ic_baseline_camera),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.size(32.dp)
                        )
                        Text(
                            text = "Tap to add evidence photo",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        }

        androidx.compose.material3.OutlinedTextField(
            value = uiState.notes,
            onValueChange = onNotesChange,
            label = {
                Text(
                    text = "Notes",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            placeholder = { Text("Any additional notes") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            maxLines = 4,
            shape = RoundedCornerShape(12.dp),
            colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            )
        )

        EcoloopButton(
            text = if (uiState.isVerifying) "Verifying..." else "Verify & complete",
            onClick = onVerify,
            enabled = !uiState.isVerifying,
            isLoading = uiState.isVerifying,
            fullWidth = true,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}
