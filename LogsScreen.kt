package com.example.jetpackcomposeloginapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.text.SimpleDateFormat
import java.util.*




@Composable
fun LogsScreen(
    navController: NavController,
    incidentManager: IncidentManager,
    logManager: LogManager,
    paddingValues: PaddingValues
) {
    var selectedLogType by remember { mutableStateOf<LogType?>(null) }

    val filteredLogs = remember(logManager.logs, selectedLogType) {
        if (selectedLogType == null) logManager.logs
        else logManager.logs.filter { it.type == selectedLogType }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Журнал логов", style = MaterialTheme.typography.headlineSmall)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            LogTypeFilterButton("Все", isSelected = selectedLogType == null) {
                selectedLogType = null
            }
            LogType.values().forEach { type ->
                LogTypeFilterButton(type.name, isSelected = selectedLogType == type) {
                    selectedLogType = type
                }
            }
        }

        Divider()

        if (filteredLogs.isEmpty()) {
            Text("Журнал логов пуст.")
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredLogs) { logEntry ->
                    LogEntryRow(logEntry = logEntry)
                    Divider()
                }
            }
        }
    }
}

@Composable
fun LogEntryRow(logEntry: LogEntry) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault()).format(logEntry.timestamp),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.width(120.dp)
        )
        Text(
            text = logEntry.type.name,
            color = logEntry.type.color,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.width(80.dp)
        )
        Text(
            text = logEntry.message,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun LogTypeFilterButton(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
            contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
        ),
    ) {
        Text(label)
    }
}
