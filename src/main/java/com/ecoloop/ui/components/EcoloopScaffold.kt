package com.ecoloop.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.DeviceUnknown
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

data class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)

@Composable
fun HouseholdScaffold(
    navController: NavHostController,
    topBarTitle: String,
    topBarActions: @Composable (RowScope.() -> Unit)? = null,
    floatingActionButton: @Composable (() -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    val items = listOf(
        BottomNavItem("Home", Icons.Default.Home, "household_home"),
        BottomNavItem("Devices", Icons.Default.DeviceUnknown, "household_devices"),
        BottomNavItem("Pickups", Icons.Default.LocalShipping, "household_pickups"),
        BottomNavItem("Points", Icons.Default.Stars, "household_points"),
        BottomNavItem("Profile", Icons.Default.Person, "household_profile")
    )

    Scaffold(
        topBar = {
            EcoloopTopAppBar(title = topBarTitle, actions = topBarActions ?: {})
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 4.dp
            ) {
                val currentRoute = navController.currentBackStackEntry?.destination?.route
                items.forEach { item ->
                    val selected = currentRoute == item.route ||
                        (item.route == "household_home" && currentRoute?.startsWith("household_home") == true)
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            val tint = if (selected) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurfaceVariant.copy(0.6f)
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title,
                                tint = tint,
                                modifier = Modifier.size(20.dp).semantics {
                                    contentDescription = item.title
                                }
                            )
                        },
                        label = {
                            Text(
                                text = item.title,
                                fontSize = 11.sp,
                                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                                color = if (selected) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurfaceVariant.copy(0.6f)
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.6f),
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.6f),
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }
        },
        floatingActionButton = floatingActionButton ?: {}
    ) { innerPadding ->
        content(innerPadding)
    }
}

@Composable
fun PartnerScaffold(
    navController: NavHostController,
    topBarTitle: String,
    topBarActions: @Composable (RowScope.() -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    val items = listOf(
        BottomNavItem("Home", Icons.Default.Home, "partner_home"),
        BottomNavItem("Offers", Icons.Default.LocalOffer, "partner_offers"),
        BottomNavItem("Jobs", Icons.Default.Work, "partner_jobs"),
        BottomNavItem("Profile", Icons.Default.Person, "partner_profile")
    )

    Scaffold(
        topBar = {
            EcoloopTopAppBar(title = topBarTitle, actions = topBarActions ?: {})
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 4.dp
            ) {
                val currentRoute = navController.currentBackStackEntry?.destination?.route
                items.forEach { item ->
                    val selected = currentRoute == item.route || currentRoute?.startsWith(item.route) == true
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            val tint = if (selected) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurfaceVariant.copy(0.6f)
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title,
                                tint = tint,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        label = {
                            Text(
                                text = item.title,
                                fontSize = 11.sp,
                                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                                color = if (selected) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurfaceVariant.copy(0.6f)
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.6f),
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.6f),
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        content(innerPadding)
    }
}
