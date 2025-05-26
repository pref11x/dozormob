package com.example.jetpackcomposeloginapp.ui

import java.util.Date // Импортируем Date
import java.util.UUID // Импортируем UUID

data class LogEntry(
    val id: String = UUID.randomUUID().toString(), // Уникальный ID
    val timestamp: Date,
    val type: LogType, // Тип лога (INFO, ERROR, и т.д.)
    val message: String // Сообщение лога
)