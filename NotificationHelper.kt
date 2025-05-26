package com.example.jetpackcomposeloginapp.ui // Или ваш пакет

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat // Импортируем
import androidx.core.content.ContextCompat // Импортируем
import com.example.jetpackcomposeloginapp.R // Импортируем R для доступа к ресурсам (иконка)
import com.example.jetpackcomposeloginapp.ui.LogEntry // Импортируем LogEntry

// ID для уведомлений (должен быть уникальным в рамках приложения)
private var notificationIdCounter = 0

fun showLogNotification(context: Context, logEntry: LogEntry) {
    // Проверяем разрешение на уведомления (особенно важно для Android 13+)
    if (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        // Разрешение не дано, не показываем уведомление
        return
    }

    val notificationManager = ContextCompat.getSystemService(
        context,
        NotificationManager::class.java
    ) as NotificationManager

    // Создаем уведомление
    val builder = NotificationCompat.Builder(context, "log_channel")
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentTitle("Новая запись в журнал")
        .setContentText(logEntry.message)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setAutoCancel(true)

    // Показываем уведомление
    notificationManager.notify(notificationIdCounter++, builder.build())
}