package com.example.jetpackcomposeloginapp.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.util.*

@Composable
fun SettingsScreen(
    incidentManager: IncidentManager,
    logManager: LogManager,
    navController: NavController,
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    paddingValues: PaddingValues
) {
    val context = LocalContext.current
    var checked by remember { mutableStateOf(isDarkTheme) }
    var showAboutDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.Top),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Заголовок
        Text(
            text = "Настройки",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        // Темная тема
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Тёмная тема", modifier = Modifier.weight(1f))
            Switch(
                checked = checked,
                onCheckedChange = {
                    checked = it
                    onThemeChange(it)
                }
            )
        }

        // Кнопка: Очистить логи и инциденты
        Button(
            onClick = {
                logManager.clearLogs()
                incidentManager.clearIncidents()
                Toast.makeText(context, "Данные очищены", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Очистить логи и инциденты")
        }

        // Кнопка: Экспорт логов
        Button(
            onClick = {
                val result = logManager.exportToFile(context)
                Toast.makeText(
                    context,
                    if (result) "Файл экспортирован" else "Ошибка экспорта",
                    Toast.LENGTH_SHORT
                ).show()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Экспортировать логи")
        }

        // Кнопка: Отправить отзыв
        Button(
            onClick = {
                Toast.makeText(context, "Форма отзыва в разработке", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Отправить отзыв")
        }

        // Кнопка: О разработчике
        OutlinedButton(
            onClick = { showAboutDialog = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("О разработчике")
        }

        if (showAboutDialog) {
            AlertDialog(
                onDismissRequest = { showAboutDialog = false },
                title = { Text("О разработчике") },
                text = {
                    Text("Приложение разработал — Щёголев Данила\nИКБО-17-21\nМИРЭА, 2025 год")
                },
                confirmButton = {
                    TextButton(onClick = { showAboutDialog = false }) {
                        Text("Закрыть")
                    }
                }
            )
        }

        // Кнопка: Выйти из аккаунта
        Button(
            onClick = {
                logManager.addLog(
                    LogEntry(
                        timestamp = Date(),
                        type = LogType.INFO,
                        message = "Пользователь вышел из системы."
                    )
                )
                incidentManager.addIncident(
                    Incident(
                        type = "Выход из системы",
                        level = ThreatLevel.INFO,
                        source = "Настройки",
                        timestamp = Date(),
                        description = "Пользователь завершил сессию."
                    )
                )
                navController.navigate("login") {
                    popUpTo("home") { inclusive = true }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Text("Выйти", color = MaterialTheme.colorScheme.onError)
        }

        Spacer(modifier = Modifier.weight(1f))

        // Версия приложения
        Text(
            text = "Версия приложения: 1.0.0",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
