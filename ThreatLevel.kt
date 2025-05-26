package com.example.jetpackcomposeloginapp.ui

import androidx.compose.ui.graphics.Color

enum class ThreatLevel(val color: Color) {
    LOW(Color.Green),
    MEDIUM(Color.Yellow),
    HIGH(Color.Red),
    INFO(Color.Blue),
    ERROR(Color(0xFFFF5722)),
    UNKNOWN(Color.Gray) // Для случаев, когда уровень неизвестен
}