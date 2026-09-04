package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import java.util.Random

@Composable
fun CosmicStarfieldBackground(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "stars")
    val twinkle by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "twinkle"
    )

    val stars = remember {
        val rand = Random(12345)
        List(40) {
            Triple(
                rand.nextFloat(), // x
                rand.nextFloat(), // y
                rand.nextFloat()  // size
            )
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        stars.forEachIndexed { i, (x, y, s) ->
            val starAlpha = if (i % 2 == 0) twinkle else (1.1f - twinkle)
            val radius = 1.2f + s * 2.2f
            drawCircle(
                color = Color.White.copy(alpha = (starAlpha * 0.75f).coerceIn(0.1f, 0.9f)),
                radius = radius,
                center = Offset(x * size.width, y * size.height)
            )
        }
    }
}
