package com.example.jetpackcomposeloginapp.ui

import androidx.compose.ui.graphics.Color

enum class LogType(val color: Color) {
    INFO(Color.Gray),
    WARNING(Color.Yellow),
    ERROR(Color.Red),
    DEBUG(Color.Cyan) // Можно выбрать другой цвет, например Color.LightGray
}