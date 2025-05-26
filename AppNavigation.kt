package com.example.jetpackcomposeloginapp.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jetpackcomposeloginapp.ui.LoginScreen


@Composable
fun AppNavigation(
    incidentManager: IncidentManager,
    logManager: LogManager,
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    paddingValues: PaddingValues
) {
    val mainNavController = rememberNavController()

    Scaffold(
        topBar = { ReusableTopBar() }
    ) { paddingValuesFromScaffold ->
        NavHost(
            navController = mainNavController,
            startDestination = "login",
            modifier = Modifier.padding(paddingValuesFromScaffold)
        ) {
            composable("login") {
                LoginScreen(
                    navController = mainNavController,
                    incidentManager = incidentManager,
                    logManager = logManager,
                    paddingValues = paddingValuesFromScaffold
                )
            }
            composable("home") {
                HomeScreen(
                    mainNavController = mainNavController,
                    incidentManager = incidentManager,
                    logManager = logManager,
                    isDarkTheme = isDarkTheme,
                    onThemeChange = onThemeChange,
                    paddingValues = paddingValuesFromScaffold // <-- ВОТ ЗДЕСЬ исправлено
                )
            }
        }
    }
}
