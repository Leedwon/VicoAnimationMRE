package com.example.vicoanimation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.vicoanimation.ui.theme.VicoAnimationMRETheme
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.core.chart.values.AxisValuesOverrider
import com.patrykandpatrick.vico.core.entry.ChartEntry
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import kotlin.math.max
import kotlin.math.min

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VicoAnimationMRETheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    VicoMre()
                }
            }
        }
    }
}

@Composable
private fun VicoMre() {
    val chartEntryModelProducer = remember { ChartEntryModelProducer() }

    var clicks by remember { mutableIntStateOf(0) }
    var minY by remember { mutableFloatStateOf(0f) }
    var maxY by remember { mutableFloatStateOf(0f) }

    fun setData(data: List<List<SimpleChartEntry>>) {
        minY = min(0f, data.flatten().minOf { it.y })
        maxY = max(data.flatten().maxOf { it.y }, 0f)
        chartEntryModelProducer.setEntries(data)
    }

    LaunchedEffect(Unit) {
        setData(data[0])
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Chart(
            minY = minY,
            maxY = maxY,
            chartEntryModelProducer = chartEntryModelProducer
        )
        Button(
            onClick = {
                clicks++
                setData(data[clicks % 2])
            }
        ) {
            Text("Change values")
        }
    }
}

@Composable
private fun Chart(
    minY: Float,
    maxY: Float,
    chartEntryModelProducer: ChartEntryModelProducer
) {
    val axisValuesOverrider = remember(minY, maxY) {
        AxisValuesOverrider.fixed(minY = minY, maxY = maxY)
    }

    ProvideChartStyle(rememberChartStyle()) {
        Chart(
            chart = lineChart(axisValuesOverrider = axisValuesOverrider),
            chartModelProducer = chartEntryModelProducer,
            startAxis = rememberStartAxis(),
            bottomAxis = rememberBottomAxis(guideline = null),
            runInitialAnimation = false
        )
    }
}

val data = listOf(
    listOf(
        listOf(
            SimpleChartEntry(x = 0.0f, y = 5.0f),
            SimpleChartEntry(x = 1.0f, y = 15.0f),
            SimpleChartEntry(x = 2.0f, y = 25.0f),
            SimpleChartEntry(x = 3.0f, y = 115.0f)
        )
    ),
    listOf(
        listOf(
            SimpleChartEntry(x = 2.0f, y = -115.0f),
            SimpleChartEntry(x = 3.0f, y = -135.0f),
        )
    )
)

class SimpleChartEntry(
    override val x: Float,
    override val y: Float
) : ChartEntry {
    override fun withY(y: Float): ChartEntry {
        return SimpleChartEntry(x = x, y = y)
    }
}
