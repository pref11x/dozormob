package com.example.jetpackcomposeloginapp.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

@Composable
fun MetricsScreen(paddingValues: PaddingValues) {
    var trafficSpeed by remember { mutableStateOf(Random.nextInt(1000, 5000)) }
    var activeConnections by remember { mutableStateOf(Random.nextInt(50, 200)) }
    var systemLoad by remember { mutableStateOf(Random.nextInt(30, 90)) }
    var activityData by remember { mutableStateOf(List(24) { Random.nextInt(0, 100) }) }
    var threatGeo by remember { mutableStateOf("Россия") }

    fun updateMetrics() {
        trafficSpeed = Random.nextInt(1000, 5000)
        activeConnections = Random.nextInt(50, 200)
        systemLoad = Random.nextInt(30, 90)
        activityData = List(24) { Random.nextInt(0, 100) }
        threatGeo = listOf("Россия", "Казахстан", "Беларусь").random()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text("Метрики", style = MaterialTheme.typography.headlineSmall)
        }

        item {
            MetricBox("Скорость входящего трафика", "$trafficSpeed MB/s")
        }

        item {
            MetricBox("Число активных подключений", "$activeConnections")
        }

        item {
            MetricBox("Нагрузка на систему", "$systemLoad%", progress = systemLoad / 100f)
        }

        item {
            Text("График активности по часам", style = MaterialTheme.typography.titleMedium)
            ActivityChart(data = activityData)
        }

        item {
            MetricBox("География угроз", threatGeo)
        }

        item {
            Button(onClick = { updateMetrics() }) {
                Text("Обновить метрики")
            }
        }
    }
}

@Composable
fun MetricBox(label: String, value: String, progress: Float? = null) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(16.dp)
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        if (progress != null) {
            Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            LinearProgressIndicator(progress = progress, modifier = Modifier.fillMaxWidth())
        } else {
            Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
fun ActivityChart(data: List<Int>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            data.forEach { value ->
                Box(
                    modifier = Modifier
                        .width(10.dp)
                        .fillMaxHeight((value / 100f).coerceIn(0f, 1f))
                        .background(Color(0xFF6200EE))
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            (0 until data.size).forEach { hour ->
                Text(text = "${hour}", fontSize = 10.sp)
            }
        }
    }
}
