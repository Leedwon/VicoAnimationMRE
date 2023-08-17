package com.example.vicoanimation

import android.graphics.Path
import android.graphics.RectF
import android.graphics.Typeface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.patrykandpatrick.vico.compose.style.ChartStyle
import com.patrykandpatrick.vico.core.chart.dimensions.HorizontalDimensions
import com.patrykandpatrick.vico.core.chart.line.LineChart
import com.patrykandpatrick.vico.core.component.shape.ShapeComponent
import com.patrykandpatrick.vico.core.component.shape.Shapes

@Composable
internal fun rememberChartStyle(
    axisColor: Color = MaterialTheme.colorScheme.outlineVariant,
    pointsColor: Color = MaterialTheme.colorScheme.primary,
    elevationOverlayColor: Color = MaterialTheme.colorScheme.primary
) = remember {
    ChartStyle(
        axis = ChartStyle.Axis(
            axisLabelColor = axisColor,
            axisGuidelineColor = axisColor,
            axisLineColor = axisColor,
            axisLabelTypeface = Typeface.SANS_SERIF
        ),
        columnChart = ChartStyle.ColumnChart(columns = emptyList()),
        lineChart = ChartStyle.LineChart(
            lines = listOf(
                LineChart.LineSpec(
                    lineColor = pointsColor.toArgb(),
                    pointConnector = EmptyPointConnector,
                    point = ShapeComponent(
                        shape = Shapes.pillShape,
                        color = pointsColor.toArgb()
                    ),
                    pointSizeDp = 12.0f
                )
            )
        ),
        marker = ChartStyle.Marker(),
        elevationOverlayColor = elevationOverlayColor
    )
}

private object EmptyPointConnector : LineChart.LineSpec.PointConnector {
    override fun connect(
        path: Path,
        prevX: Float,
        prevY: Float,
        x: Float,
        y: Float,
        horizontalDimensions: HorizontalDimensions,
        bounds: RectF
    ) {
        // no-op
    }
}
