package com.ecoloop.ui.household

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.ecoloop.R
import com.ecoloop.domain.model.DeviceCondition
import com.ecoloop.ui.components.EcoloopButton
import com.ecoloop.ui.components.EcoloopTextField
import com.ecoloop.ui.components.HouseholdScaffold
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun SubmitDeviceScreen(
    navController: NavHostController = rememberNavController(),
    onSubmitted: (String) -> Unit = {},
    onCancel: () -> Unit = {}
) {
    val viewModel: SubmitDeviceViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.submittedDeviceId) {
        uiState.submittedDeviceId?.let { deviceId ->
            onSubmitted(deviceId)
            viewModel.clearSubmittedDevice()
        }
    }

    HouseholdScaffold(
        navController = navController,
        topBarTitle = "Submit device",
        topBarActions = {
            TextButton(onClick = onCancel) {
                Text(
                    text = "Cancel",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            DeviceForm(
                uiState = uiState,
                onImageSet = { uri, size, mime ->
                    viewModel.setImage(uri, size, mime)
                },
                onConditionChange = { viewModel.setCondition(it) },
                onAddressChange = { viewModel.setAddress(it) },
                onSubmit = { viewModel.submit() },
                context = LocalContext.current
            )

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

@Composable
fun DeviceForm(
    uiState: SubmitUiState,
    onImageSet: (Uri?, Long, String?) -> Unit,
    onConditionChange: (DeviceCondition) -> Unit,
    onAddressChange: (String) -> Unit,
    onSubmit: () -> Unit,
    context: android.content.Context
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val size = context.getFileSize(uri)
            val mime = context.getMimeType(uri)
            onImageSet(uri, size, mime)
        }
    }

    var expandedCondition by rememberSaveable { mutableStateOf(false) }

    Column {
        if (uiState.imageUri != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                Image(
                    painter = rememberImagePainter(uiState.imageUri),
                    contentDescription = "Device photo",
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                IconButton(
                    onClick = { onImageSet(null, 0, null) },
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.TopEnd)
                        .offset(x = 8.dp, y = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Remove photo",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        } else {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clickable { launcher.launch("image/*") },
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
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
                            text = "Tap to add photo",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
                .clickable { expandedCondition = true },
            shape = RoundedCornerShape(8.dp)
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = uiState.condition?.let { conditionLabel(it) } ?: "Select condition",
                    fontSize = 15.sp,
                    color = if (uiState.condition == null) MaterialTheme.colorScheme.onSurfaceVariant
                        else MaterialTheme.colorScheme.onSurface
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        EcoloopTextField(
            value = uiState.address,
            onValueChange = onAddressChange,
            label = "Pickup address",
            placeholder = "Street, city, postal code",
            singleLine = true,
            maxLines = 3,
            modifier = Modifier.padding(top = 12.dp)
        )

        EcoloopButton(
            text = if (uiState.isSubmitting) "Submitting..." else "Submit device",
            onClick = onSubmit,
            enabled = !uiState.isSubmitting &&
                uiState.imageUri != null &&
                uiState.condition != null,
            isLoading = uiState.isSubmitting,
            fullWidth = true,
            modifier = Modifier.padding(top = 16.dp)
        )
    }

    if (expandedCondition) {
        SelectionDialog(
            title = "Condition",
            options = DeviceCondition.All.map { conditionLabel(it) },
            selected = uiState.condition?.let { conditionLabel(it) } ?: "",
            onSelect = { selectedLabel ->
                DeviceCondition.All.find { conditionLabel(it) == selectedLabel }?.let {
                    onConditionChange(it)
                }
                expandedCondition = false
            },
            onDismiss = { expandedCondition = false }
        )
    }
}

fun conditionLabel(condition: DeviceCondition): String = when (condition) {
    DeviceCondition.Working -> "Like new"
    DeviceCondition.SlightlyDamaged -> "Good"
    DeviceCondition.FullyDamaged -> "Poor"
    else -> condition.value.replace("_", " ").replaceFirstChar { it.uppercase() }
}

@Composable
fun SelectionDialog(
    title: String,
    options: List<String>,
    selected: String,
    onSelect: (String) -> Unit,
    onDismiss: () -> Unit
) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column {
                options.forEach { option ->
                    Text(
                        text = option,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onSelect(option)
                            }
                            .padding(vertical = 12.dp),
                        fontWeight = if (option == selected) FontWeight.SemiBold else FontWeight.Normal,
                        color = if (option == selected) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    )
}

fun android.content.Context.getFileSize(uri: Uri): Long {
    return try {
        val fd = contentResolver.openFileDescriptor(uri, "r")
        val size = fd?.statSize ?: 0L
        fd?.close()
        size
    } catch (e: Exception) {
        0L
    }
}

fun android.content.Context.getMimeType(uri: Uri): String? {
    return contentResolver.getType(uri)
}
