package com.example.jetpackcomposeloginapp.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.jetpackcomposeloginapp.ui.Incident
import com.example.jetpackcomposeloginapp.ui.IncidentManager
import com.example.jetpackcomposeloginapp.ui.ThreatLevel
import com.example.jetpackcomposeloginapp.ui.LogEntry
import com.example.jetpackcomposeloginapp.ui.LogManager
import com.example.jetpackcomposeloginapp.ui.LogType
import kotlinx.coroutines.delay
import java.util.Date

@Composable
fun LoginScreen(
    navController: NavController,
    incidentManager: IncidentManager,
    logManager: LogManager,
    paddingValues: PaddingValues = PaddingValues(0.dp)
) {
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var remainingAttempts by remember { mutableStateOf(3) } // Состояние для счетчика попыток
    var isAccountLocked by remember { mutableStateOf(false) } // Состояние для блокировки
    var lockMessage by remember { mutableStateOf<String?>(null) }
    var unlockTimeMillis by remember { mutableStateOf(0L) } // Состояние для времени разблокировки


    // Проверка таймера разблокировки
    LaunchedEffect(isAccountLocked) {
        if (isAccountLocked) {
            unlockTimeMillis = System.currentTimeMillis() + 60000L // время разблокировки
            delay(60000L) // Ждем 60 секунд
            isAccountLocked = false // Разблокируем
            remainingAttempts = 3 // Сбрасываем счетчик
            errorMessage = null // Очищаем сообщение
            lockMessage = null // Очищаем сообщение о блокировке
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = login,
                onValueChange = {
                    login = it
                    errorMessage = null
                },
                label = { Text("Логин") }
            )

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    errorMessage = null
                },
                label = { Text("Пароль") },
                visualTransformation = PasswordVisualTransformation()
            )

            errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Button(
                onClick = {
                    if (isAccountLocked) {
                        errorMessage = "Учетная запись временно заблокирована. Обратитесь в поддержку: SDMonitoring@rostel.ru"
                        return@Button
                    }

                    // Сбрасываем предыдущие сообщения об ошибке
                    errorMessage = null
                    lockMessage = null // Также сбрасываем сообщение о блокировке перед попыткой

                    if (login == "admin" && password == "1234") {
                        // Успешный вход
                        logManager.addLog(
                            LogEntry(
                                timestamp = Date(),
                                type = LogType.INFO,
                                message = "Пользователь $login успешно вошел."
                            )
                        )
                        remainingAttempts = 3 // Сбрасываем счетчик при успешном входе
                        isAccountLocked = false // Убеждаемся, что не заблокировано
                        unlockTimeMillis = 0L

                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    } else {
                        // Неверный логин/пароль
                        remainingAttempts-- // Уменьшаем счетчик
                        logManager.addLog(
                            LogEntry(
                                timestamp = Date(),
                                type = LogType.WARNING,
                                message = "Неудачная попытка входа для пользователя $login."
                            )
                        )
                        // Добавляем инцидент только при неверной попытке
                        incidentManager.addIncident(
                            Incident(
                                type = "Неудачная попытка входа",
                                level = ThreatLevel.LOW,
                                source = "Авторизация",
                                timestamp = Date(),
                                description = "Пользователь $login ввел неверные учетные данные."
                            )
                        )


                        if (remainingAttempts > 0) {
                            errorMessage = "Неверный логин или пароль. Осталось $remainingAttempts попыт${if (remainingAttempts == 1) "ка" else "ки"}."
                            // Опционально: Триггер покачивания здесь при неверных данных
                        } else {
                            // Блокировка учетной записи
                            isAccountLocked = true
                            unlockTimeMillis = System.currentTimeMillis() + 60000L // Устанавливаем время блокировки
                            errorMessage = "Учетная запись временно заблокирована. Обратитесь в поддержку: SDMonitoring@rostel.ru"
                            lockMessage = errorMessage // Сохраняем сообщение о блокировке
                            // Опционально: Триггер покачивания здесь при блокировке
                        }
                    }
                },
                enabled = !isAccountLocked // Кнопка неактивна, если учетная запись заблокирована
            ) {
                Text("Войти")
            }
            // Добавьте отображение сообщений о блокировке здесь, если нужно отдельное поле
            lockMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error, // Или другой цвет для блокировки
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

// Обновление Preview для передачи paddingValues и incidentManager
@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    val mockIncidentManager = remember { IncidentManager() } // Создаем моковый IncidentManager для превью
    val mockLogManager = remember { LogManager() }
    val mockNavController = rememberNavController()
    LoginScreen(
        navController = mockNavController,
        incidentManager = mockIncidentManager, // Передаем IncidentManager
        logManager = mockLogManager,
        paddingValues = PaddingValues(0.dp) // Передаем PaddingValues для превью
    )
}