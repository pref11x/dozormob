package com.example.jetpackcomposeloginapp.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.jetpackcomposeloginapp.ui.ThreatLevel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun IncidentsScreen(
    navController: NavController,
    incidentManager: IncidentManager,
    paddingValues: PaddingValues // <-- Добавляем параметр paddingValues (от BottomBar)
) {
    // Получаем список инцидентов из IncidentManager
    val incidents = incidentManager.incidents

    // Состояние для выбранного фильтра (начальное значение null - показывать все)
    var selectedFilter by remember { mutableStateOf<ThreatLevel?>(null) }

    // Фильтруем инциденты на основе выбранного фильтра
    val filteredIncidents = remember(incidents, selectedFilter) {
        if (selectedFilter == null) {
            incidents
        } else {
            incidents.filter { it.level == selectedFilter }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues) // <-- Применяем paddingValues (от BottomBar)
            .padding(16.dp), // Дополнительный внутренний отступ
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Инциденты", style = MaterialTheme.typography.headlineSmall)

        // Кнопки фильтрации
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { selectedFilter = null },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedFilter == null) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = if (selectedFilter == null) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Text("Все")
            }
            ThreatLevel.values().forEach { level ->
                Button(
                    onClick = { selectedFilter = level },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedFilter == level) level.color else MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = if (selectedFilter == level) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    Text(level.name)
                }
            }
        }

        Divider() // Разделитель

        // Список инцидентов
        if (filteredIncidents.isEmpty()) {
            Text("Нет инцидентов для отображения.")
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredIncidents) { incident ->
                    IncidentItem(incident = incident, onClick = {
                        // Навигация на экран деталей инцидента
                        navController.navigate(
                            "incidentDetail/${incident.id}/${incident.type}/${incident.level.name}/${incident.source}/${incident.timestamp.time}/${incident.description}"
                        )
                    })
                    Divider() // Разделитель между элементами списка
                }
            }
        }
    }
}

@Composable
fun IncidentItem(incident: Incident, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick) // Делаем элемент списка кликабельным
            .padding(vertical = 8.dp), // Отступ внутри элемента
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
            Text("Тип: ${incident.type}", fontWeight = FontWeight.Bold)
            Text("Источник: ${incident.source}")
            Text("Время: ${SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(incident.timestamp)}")
        }
        Text(
            text = incident.level.name,
            color = incident.level.color, // Применяем цвет в зависимости от уровня
            fontWeight = FontWeight.Bold
        )
    }
}


// Preview для IncidentsScreen (потребуется моковый IncidentManager)
@Preview(showBackground = true)
@Composable
fun PreviewIncidentsScreen() {
    // Создаем моковый IncidentManager для превью
    val mockIncidentManager = remember {
        IncidentManager().apply {
            addIncident(Incident(type = "Вход", level = ThreatLevel.INFO, source = "Авторизация", timestamp = Date(), description = "Успешный вход.")) // <-- Используем ThreatLevel.INFO
            addIncident(Incident(type = "Ошибка", level = ThreatLevel.ERROR, source = "Система", timestamp = Date(), description = "Неизвестная ошибка.")) // <-- Используем ThreatLevel.ERROR
        }
    }
    val mockNavController = rememberNavController()

    IncidentsScreen(
        navController = mockNavController,
        incidentManager = mockIncidentManager,
        paddingValues = PaddingValues(0.dp) // Передаем пустые отступы для превью
    )
}