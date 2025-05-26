package com.example.jetpackcomposeloginapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment // <-- Добавьте эту строку
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun IncidentDetailScreen(
    navController: NavController,
    incidentId: String?,
    incidentType: String?,
    incidentLevel: String?,
    incidentSource: String?,
    incidentTimestamp: Long?,
    incidentDescription: String?,
    paddingValues: PaddingValues
) {
    // Форматирование времени
    val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault())
    val formattedTimestamp = incidentTimestamp?.let { dateFormat.format(Date(it)) } ?: "N/A"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues) // <-- Применяем paddingValues
            .padding(16.dp), // Сохраняем внутренний отступ
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Детали инцидента",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Divider()

        Text("ID: ${incidentId ?: "N/A"}", fontWeight = FontWeight.Bold)
        Text("Тип: ${incidentType ?: "N/A"}", fontWeight = FontWeight.Bold)
        Text("Уровень: ${incidentLevel ?: "N/A"}", fontWeight = FontWeight.Bold)
        Text("Источник: ${incidentSource ?: "N/A"}", fontWeight = FontWeight.Bold)
        Text("Время: $formattedTimestamp", fontWeight = FontWeight.Bold)

        Text(
            text = "Описание:",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = incidentDescription ?: "Описание отсутствует.",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.weight(1f)) // Занимает оставшееся пространство

        Button(onClick = { navController.popBackStack() }) {
            Text("Назад")
        }
    }
}
