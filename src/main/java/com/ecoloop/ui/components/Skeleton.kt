package com.ecoloop.ui.components

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Composable
fun Skeleton(
    modifier: Modifier = Modifier,
    height: androidx.compose.ui.unit.Dp = 16.dp,
    radius: androidx.compose.ui.unit.Dp = 4.dp
) {
    val animation = rememberInfiniteTransition(label = "skeleton")
    val animatedAlpha by animation.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800)
        ),
        label = "alpha"
    )

    Box(
        modifier = modifier
            .height(height)
            .fillMaxWidth()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.onSurface.copy(animatedAlpha * 0.4f),
                        MaterialTheme.colorScheme.onSurface.copy(animatedAlpha * 0.2f),
                        MaterialTheme.colorScheme.onSurface.copy(animatedAlpha * 0.4f)
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(100f, 100f)
                ),
                shape = RoundedCornerShape(radius)
            )
    )
}

@Composable
fun SkeletonDeviceList(count: Int = 3) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        repeat(count) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Skeleton(
                    modifier = Modifier.size(56.dp),
                    height = 56.dp,
                    radius = 8.dp
                )
                Column(
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .weight(1f)
                ) {
                    Skeleton(height = 16.dp, modifier = Modifier.fillMaxWidth(0.8f))
                    Spacer(modifier = Modifier.height(4.dp))
                    Skeleton(height = 14.dp, modifier = Modifier.fillMaxWidth(0.5f))
                }
            }
        }
    }
}
