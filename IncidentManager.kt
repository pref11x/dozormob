package com.example.jetpackcomposeloginapp.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import java.util.Date
import kotlin.random.Random
import com.example.jetpackcomposeloginapp.ui.IncidentManager



class IncidentManager {
    private val _incidents = mutableStateListOf<Incident>()
    val incidents: SnapshotStateList<Incident> = _incidents

    init {
        addIncident(Incident(type = "Попытка входа", level = ThreatLevel.LOW, source = "Авторизация", timestamp = Date(System.currentTimeMillis() - 60000), description = "Пользователь попытался войти с неверными учетными данными."))
        addIncident(Incident(type = "Сетевая активность", level = ThreatLevel.MEDIUM, source = "Сетевой сенсор", timestamp = Date(System.currentTimeMillis() - 120000), description = "Обнаружен подозрительный трафик на порту 22."))
        addIncident(Incident(type = "Обнаружена вредоносная активность", level = ThreatLevel.HIGH, source = "IDS", timestamp = Date(System.currentTimeMillis() - 300000), description = "Система обнаружения вторжений зафиксировала попытку эксплуатации уязвимости."))
        addIncident(Incident(type = "Попытка входа", level = ThreatLevel.LOW, source = "Авторизация", timestamp = Date(System.currentTimeMillis()), description = "Успешный вход пользователя admin."))
        addIncident(Incident(type = "Неизвестное событие", level = ThreatLevel.UNKNOWN, source = "Система", timestamp = Date(System.currentTimeMillis() - 10000), description = "Зафиксировано неопознанное системное событие."))
    }

    fun addIncident(incident: Incident) {
        _incidents.add(0, incident)
    }

    fun clearIncidents() {
        _incidents.clear()
    }

    fun getFilteredIncidents(filter: ThreatLevel?): SnapshotStateList<Incident> {
        return if (filter == null) {
            incidents
        } else {
            mutableStateListOf<Incident>().apply {
                addAll(incidents.filter { it.level == filter })
            }
        }
    }

    fun generateRandomIncident(): Incident {
        val types = listOf("Сетевая активность", "Попытка входа", "Обнаружение файла", "Системная ошибка")
        val levels = ThreatLevel.values().filter { it != ThreatLevel.UNKNOWN }
        val sources = listOf("Firewall", "Авторизация", "Антивирус", "Система")
        val descriptions = listOf(
            "Событие без подробного описания.",
            "Автоматически сгенерированный инцидент.",
            "Обнаружена потенциальная угроза.",
            "Зафиксировано необычное поведение."
        )

        return Incident(
            type = types.random(),
            level = levels.random(),
            source = sources.random(),
            timestamp = Date(),
            description = descriptions.random()
        )
    }
}
