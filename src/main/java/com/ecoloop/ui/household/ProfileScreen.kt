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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ecoloop.domain.model.Profile
import com.ecoloop.ui.components.HouseholdScaffold
import com.ecoloop.ui.components.Skeleton
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun ProfileScreen(
    navController: NavHostController = rememberNavController(),
    onLogout: () -> Unit = {}
) {
    val viewModel: ProfileViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HouseholdScaffold(
        navController = navController,
        topBarTitle = "Profile"
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            if (uiState.isLoading) {
                ProfileSkeleton()
            } else {
                uiState.profile?.let { profile ->
                    ProfileContent(
                        profile = profile,
                        onLogout = onLogout
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileContent(
    profile: Profile,
    onLogout: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(16.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.secondary
                            )
                        )
                    )
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x66000000))
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = -48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(104.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier.size(48.dp)
                )
            }

            Text(
                text = profile.name ?: profile.email ?: "User",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = profile.email ?: "",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 2.dp)
            )

            if (profile.pointsBalance != null) {
                Text(
                    text = "${profile.pointsBalance} points",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ProfileItem(
                    icon = Icons.Default.Person,
                    label = "Personal information",
                    onClick = { }
                )
                ProfileItem(
                    icon = Icons.Default.Lock,
                    label = "Change password",
                    onClick = { }
                )
                ProfileItem(
                    icon = Icons.Default.Notifications,
                    label = "Notifications settings",
                    onClick = { }
                )
                ProfileItem(
                    icon = Icons.Default.Info,
                    label = "About EcoLoop",
                    onClick = { }
                )
                ProfileItem(
                    icon = Icons.Default.Logout,
                    label = "Logout",
                    onClick = onLogout,
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun ProfileItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    tint: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = label,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = tint,
                modifier = Modifier.padding(start = 12.dp)
            )
        }
    }
}

@Composable
fun ProfileSkeleton() {
    Column(Modifier.fillMaxWidth()) {
        Skeleton(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            height = 180.dp,
            radius = 16.dp
        )
        Box(
            modifier = Modifier
                .size(104.dp)
                .clip(CircleShape)
                .align(Alignment.CenterHorizontally),
            contentAlignment = Alignment.Center
        ) {
            Skeleton(
                modifier = Modifier.size(104.dp),
                height = 104.dp,
                radius = 52.dp
            )
        }
        Skeleton(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .height(20.dp),
            height = 20.dp,
            radius = 4.dp
        )
        Skeleton(
            modifier = Modifier
                .fillMaxWidth(0.3f)
                .height(16.dp)
                .padding(top = 8.dp),
            height = 16.dp,
            radius = 4.dp
        )
        repeat(6) {
            Skeleton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                height = 56.dp,
                radius = 12.dp
            )
        }
    }
}
