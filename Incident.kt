package com.example.jetpackcomposeloginapp.ui

import java.util.Date
import java.util.UUID // Импортируем UUID

data class Incident(
    val id: String = UUID.randomUUID().toString(), // Добавляем уникальный ID
    val type: String,
    val level: ThreatLevel,
    val source: String,
    val timestamp: Date,
    val description: String // Добавляем описание
)