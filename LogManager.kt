package com.example.jetpackcomposeloginapp.ui

import android.content.Context
import android.os.Environment
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class LogManager {
    private val _logs = mutableStateListOf<LogEntry>()
    val logs: SnapshotStateList<LogEntry> = _logs

    private val _latestLog = mutableStateOf<LogEntry?>(null)
    val latestLog: State<LogEntry?> = _latestLog

    init {
        addLogInternal(
            LogEntry(
                timestamp = Date(System.currentTimeMillis() - 5000),
                type = LogType.INFO,
                message = "Система запущена."
            )
        )
        addLogInternal(
            LogEntry(
                timestamp = Date(System.currentTimeMillis() - 4000),
                type = LogType.DEBUG,
                message = "Проверка подключения к базе данных."
            )
        )
        addLogInternal(
            LogEntry(
                timestamp = Date(System.currentTimeMillis() - 3000),
                type = LogType.WARNING,
                message = "Низкий уровень свободного места на диске."
            )
        )
        addLogInternal(
            LogEntry(
                timestamp = Date(System.currentTimeMillis() - 2000),
                type = LogType.ERROR,
                message = "Не удалось подключиться к серверу."
            )
        )
        addLogInternal(
            LogEntry(
                timestamp = Date(System.currentTimeMillis() - 1000),
                type = LogType.INFO,
                message = "Пользователь admin успешно вошел."
            )
        )
        addLogInternal(
            LogEntry(
                timestamp = Date(),
                type = LogType.DEBUG,
                message = "Рендеринг пользовательского интерфейса."
            )
        )

    }


    fun addLog(log: LogEntry) {
        addLogInternal(log)
        _latestLog.value = log
    }

    private fun addLogInternal(log: LogEntry) {
        _logs.add(0, log)
    }

    fun getFilteredLogs(filter: LogType?): SnapshotStateList<LogEntry> {
        return if (filter == null) {
            logs
        } else {
            mutableStateListOf<LogEntry>().apply {
                addAll(logs.filter { it.type == filter })
            }
        }
    }

    fun clearLogs() {
        _logs.clear()
        _latestLog.value = null
    }

    fun exportToFile(context: Context): Boolean {
        return try {
            val fileName = "logs_${System.currentTimeMillis()}.txt"
            val fileDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
            val file = File(fileDir, fileName)
            val writer = FileWriter(file)

            val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault())
            _logs.forEach {
                writer.write("${formatter.format(it.timestamp)} [${it.type}] ${it.message}\n")
            }

            writer.flush()
            writer.close()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
