package com.ecoloop.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.ecoloop.ui.AuthViewModel
import com.ecoloop.ui.auth.ForgotPasswordScreen
import com.ecoloop.ui.auth.LoginScreen
import com.ecoloop.ui.auth.RegisterScreen
import com.ecoloop.ui.household.DeviceDetailScreen
import com.ecoloop.ui.household.DevicesScreen
import com.ecoloop.ui.household.HomeScreen
import com.ecoloop.ui.household.NotificationsScreen
import com.ecoloop.ui.household.PickupsScreen
import com.ecoloop.ui.household.PointsScreen
import com.ecoloop.ui.household.ProfileScreen
import com.ecoloop.ui.household.SubmitDeviceScreen
import com.ecoloop.ui.partner.JobDetailScreen
import com.ecoloop.ui.partner.OffersScreen
import com.ecoloop.ui.partner.JobsScreen
import com.ecoloop.ui.partner.PartnerHomeScreen
import com.ecoloop.ui.partner.PartnerProfileScreen

@Composable
fun EcoloopApp(
    navController: NavHostController = rememberNavController(),
    onLogout: (() -> Unit)? = null
) {
    val authViewModel: AuthViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = "auth"
    ) {
        navigation(
            startDestination = "login",
            route = "auth"
        ) {
            composable("login") {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate("main") {
                            popUpTo("auth") { inclusive = true }
                        }
                    },
                    onRegisterClick = { navController.navigate("register") },
                    onForgotPasswordClick = { navController.navigate("forgot_password") }
                )
            }
            composable("register") {
                RegisterScreen(
                    onRegisterSuccess = {
                        navController.navigate("main") {
                            popUpTo("auth") { inclusive = true }
                        }
                    },
                    onLoginClick = {
                        navController.popBackStack("login", false)
                    }
                )
            }
            composable("forgot_password") {
                ForgotPasswordScreen(
                    onBack = { navController.popBackStack() },
                    onResetSent = {
                        navController.popBackStack("login", false)
                    }
                )
            }
        }

        navigation(
            startDestination = "household_home",
            route = "main"
        ) {
            composable("household_home") {
                HomeScreen(
                    navController = navController,
                    onDeviceClick = { deviceId ->
                        navController.navigate("household_device_detail/$deviceId")
                    },
                    onPickupsClick = { navController.navigate("household_pickups") },
                    onSubmitDeviceClick = { navController.navigate("household_submit") },
                    onNotificationsClick = { navController.navigate("household_notifications") },
                    onLogout = {
                        authViewModel.logout()
                        navController.navigate("auth") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
            composable("household_devices") {
                DevicesScreen(
                    navController = navController,
                    onDeviceClick = { deviceId ->
                        navController.navigate("household_device_detail/$deviceId")
                    }
                )
            }
            composable("household_device_detail/{deviceId}") { backStackEntry ->
                val deviceId = backStackEntry.arguments?.getString("deviceId") ?: return@composable
                DeviceDetailScreen(navController = navController, deviceId = deviceId)
            }
            composable("household_pickups") {
                PickupsScreen(navController = navController)
            }
            composable("household_points") {
                PointsScreen(navController = navController)
            }
            composable("household_profile") {
                ProfileScreen(
                    navController = navController,
                    onLogout = {
                        authViewModel.logout()
                        navController.navigate("auth") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
            composable("household_notifications") {
                NotificationsScreen(navController = navController)
            }
            composable("household_submit") {
                SubmitDeviceScreen(
                    navController = navController,
                    onSubmitted = { deviceId ->
                        navController.navigate("household_device_detail/$deviceId") {
                            popUpTo("household_home") { inclusive = false }
                        }
                    },
                    onCancel = { navController.popBackStack() }
                )
            }

            composable("partner_home") {
                PartnerHomeScreen(navController = navController)
            }
            composable("partner_offers") {
                OffersScreen(navController = navController)
            }
            composable("partner_jobs") {
                JobsScreen(
                    navController = navController,
                    onJobClick = { jobId ->
                        navController.navigate("partner_job_detail/$jobId")
                    }
                )
            }
            composable("partner_job_detail/{jobId}") { backStackEntry ->
                val jobId = backStackEntry.arguments?.getString("jobId") ?: return@composable
                JobDetailScreen(navController = navController, jobId = jobId)
            }
            composable("partner_profile") {
                PartnerProfileScreen(
                    navController = navController,
                    onLogout = {
                        authViewModel.logout()
                        navController.navigate("auth") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}
