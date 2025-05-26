package com.example.jetpackcomposeloginapp

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.core.content.ContextCompat
import com.example.jetpackcomposeloginapp.ui.AppNavigation
import com.example.jetpackcomposeloginapp.ui.LogManager
import com.example.jetpackcomposeloginapp.ui.IncidentManager
import com.example.jetpackcomposeloginapp.ui.theme.JetpackComposeLoginAppTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.dp


class MainActivity : ComponentActivity() {

    // Регистрация ActivityResultLauncher для запроса разрешений
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // Разрешение получено, можно отправлять уведомления
            } else {
                // Разрешение отклонено, уведомления не будут показаны
            }
        }

    // Создаем синглтоны менеджеров здесь
    private val incidentManager = IncidentManager()
    private val logManager = LogManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Создание канала уведомлений при запуске
        createNotificationChannel()

        // Запрос разрешения на уведомления (для Android 13+)
        requestNotificationPermission()

        setContent {
            // Состояние для темной темы, сохраняемое при поворотах/уничтожении активити
            var isDarkTheme by rememberSaveable { mutableStateOf(false) } // Здесь храним состояние темы

            JetpackComposeLoginAppTheme(darkTheme = isDarkTheme) { // Передаем состояние темы в тему приложения
                AppNavigation(
                    incidentManager = incidentManager,
                    logManager = logManager,
                    isDarkTheme = isDarkTheme, // Передаем состояние темы
                    onThemeChange = { isDark -> isDarkTheme = isDark },
                    paddingValues = PaddingValues(0.dp)
                )
            }
        }
    }

    // Функция для создания канала уведомлений
    private fun createNotificationChannel() {
        // Создаем канал только на API 26+ (Oreo)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Лог-события"
            val descriptionText = "Уведомления о новых записях в журнале логов"
            val importance = NotificationManager.IMPORTANCE_HIGH // Высокая важность
            val channel = NotificationChannel("log_channel", name, importance).apply {
                description = descriptionText
            }
            // Регистрируем канал в системе
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    // Функция для запроса разрешения на уведомления (для Android 13+)
    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Проверяем версию Android (API 33+)
            when {
                // Проверяем, дано ли разрешение
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // Разрешение уже есть, ничего не делаем
                }
                // Можно добавить логику для показа объяснения перед запросом
                // shouldShowRequestPermissionRationale(...) -> { ... }
                else -> {
                    // Запрашиваем разрешение
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }
}