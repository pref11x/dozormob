package com.example.jetpackcomposeloginapp.ui

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    mainNavController: NavController,
    incidentManager: IncidentManager,
    logManager: LogManager,
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    paddingValues: PaddingValues
) {
    val bottomNavController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    val latestLog by rememberUpdatedState(newValue = logManager.latestLog.value)
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(latestLog) {
        latestLog?.let { logEntry ->
            coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    message = "Новая запись в лог: ${logEntry.message}",
                    duration = SnackbarDuration.Short
                )
            }
            showLogNotification(context, logEntry)
        }
    }

    Scaffold(
        bottomBar = { BottomNavigationBar(bottomNavController) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { bottomPaddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            NavHost(
                navController = bottomNavController,
                startDestination = BottomNavItem.Incidents.route
            ) {
                composable(BottomNavItem.Incidents.route) {
                    IncidentsScreen(
                        navController = bottomNavController,
                        incidentManager = incidentManager,
                        paddingValues = bottomPaddingValues
                    )
                }
                composable(BottomNavItem.Metrics.route) {
                    MetricsScreen(paddingValues = bottomPaddingValues)
                }
                composable(BottomNavItem.Logs.route) {
                    LogsScreen(navController = bottomNavController, incidentManager, logManager, bottomPaddingValues)
                }
                composable(BottomNavItem.Settings.route) {
                    SettingsScreen(
                        incidentManager = incidentManager,
                        logManager = logManager,
                        navController = mainNavController,
                        isDarkTheme = isDarkTheme,
                        onThemeChange = onThemeChange,
                        paddingValues = bottomPaddingValues
                    )
                }

                composable(
                    route = "incidentDetail/{incidentId}/{incidentType}/{incidentLevel}/{incidentSource}/{incidentTimestamp}/{incidentDescription}",
                    arguments = listOf(
                        navArgument("incidentId") { type = NavType.StringType },
                        navArgument("incidentType") { type = NavType.StringType },
                        navArgument("incidentLevel") { type = NavType.StringType },
                        navArgument("incidentSource") { type = NavType.StringType },
                        navArgument("incidentTimestamp") { type = NavType.LongType },
                        navArgument("incidentDescription") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    IncidentDetailScreen(
                        navController = bottomNavController,
                        incidentId = backStackEntry.arguments?.getString("incidentId"),
                        incidentType = backStackEntry.arguments?.getString("incidentType"),
                        incidentLevel = backStackEntry.arguments?.getString("incidentLevel"),
                        incidentSource = backStackEntry.arguments?.getString("incidentSource"),
                        incidentTimestamp = backStackEntry.arguments?.getLong("incidentTimestamp"),
                        incidentDescription = backStackEntry.arguments?.getString("incidentDescription"),
                        paddingValues = paddingValues
                    )
                }
            }
        }
    }
}
