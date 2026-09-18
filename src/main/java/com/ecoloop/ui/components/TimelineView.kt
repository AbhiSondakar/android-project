package com.ecoloop.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

data class TimelineStep(
    val key: String,
    val label: String,
    val done: Boolean,
    val timestamp: String? = null
)

@Composable
fun TimelineView(
    steps: List<TimelineStep>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        steps.forEachIndexed { index, step ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Column(
                    modifier = Modifier.width(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val dotColor = if (step.done) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.outline
                    val dotBorderColor = dotColor
                    Canvas(modifier = Modifier.size(12.dp)) {
                        val radius = 5.dp.toPx()
                        val strokeWidthPx = 2.dp.toPx()
                        drawCircle(
                            color = dotBorderColor,
                            radius = radius,
                            style = Stroke(width = strokeWidthPx),
                            center = center
                        )
                        if (step.done) {
                            drawCircle(
                                color = MaterialTheme.colorScheme.primary,
                                radius = 4.dp.toPx(),
                                center = center
                            )
                        }
                    }

                    if (index < steps.size - 1) {
                        val lineColor = if (step.done) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.outline
                        Canvas(modifier = Modifier
                            .width(2.dp)
                            .height(28.dp)
                            .padding(top = 2.dp)
                        ) {
                            val strokeWidthPx = 2.dp.toPx()
                            drawLine(
                                color = lineColor,
                                start = center.copy(x = size.width / 2, y = 0f),
                                end = center.copy(x = size.width / 2, y = size.height),
                                strokeWidth = strokeWidthPx
                            )
                        }
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = step.label,
                        style = if (step.done) MaterialTheme.typography.bodyMedium
                            .copy(fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold)
                        else MaterialTheme.typography.bodySmall
                        .copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                        color = if (step.done) MaterialTheme.colorScheme.onSurface
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    step.timestamp?.let { timestamp ->
                        Text(
                            text = timestamp,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.7f),
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }
            }
        }
    }
}
