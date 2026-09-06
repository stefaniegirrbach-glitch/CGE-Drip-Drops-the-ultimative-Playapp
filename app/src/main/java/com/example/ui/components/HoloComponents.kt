package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ButtonStyle
import com.example.model.TapParticle
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

// Holographic rainbow palette for chrome borders and text
val HoloRainbowColors = listOf(
    Color(0xFFFF0077), // Neon Magenta
    Color(0xFFFF7700), // Orange
    Color(0xFFFFEA00), // Bright Gold
    Color(0xFF00FF88), // Spring Mint
    Color(0xFF00D4FF), // Prismatic Cyan
    Color(0xFF9D00FF), // Neon Purple
    Color(0xFFFF00C8), // Hot Pink
    Color(0xFFFF0077)  // Loop wrap
)

val GoldMetallicColors = listOf(
    Color(0xFFFFE259),
    Color(0xFFFFA751),
    Color(0xFFFFE259),
    Color(0xFFE5A93C),
    Color(0xFFFFE259)
)

val PastelGlitterColors = listOf(
    Color(0xFFFFC6FF),
    Color(0xFFBDB2FF),
    Color(0xFFA0C4FF),
    Color(0xFF9BF6FF),
    Color(0xFFFDFFB6),
    Color(0xFFFFD6A5),
    Color(0xFFFFADAD)
)

val NeonPrismColors = listOf(
    Color(0xFF00F5D4),
    Color(0xFF7B2CBF),
    Color(0xFFF72585),
    Color(0xFF4CC9F0),
    Color(0xFF00F5D4)
)

@Composable
fun rememberHoloBrush(
    buttonStyle: ButtonStyle = ButtonStyle.HOLO_CHROME,
    durationMs: Int = 3000
): Brush {
    val infiniteTransition = rememberInfiniteTransition(label = "holo_rainbow")
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMs, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase"
    )

    val colors = when (buttonStyle) {
        ButtonStyle.HOLO_CHROME -> HoloRainbowColors
        ButtonStyle.GOLD_METALLIC -> GoldMetallicColors
        ButtonStyle.PASTEL_GLITTER -> PastelGlitterColors
        ButtonStyle.NEON_PRISM -> NeonPrismColors
    }

    val offsetFactor = phase * 600f
    return Brush.linearGradient(
        colors = colors,
        start = Offset(offsetFactor, 0f),
        end = Offset(offsetFactor + 600f, 600f)
    )
}

@Composable
fun HoloRainbowBorderBox(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(20.dp),
    borderWidth: Dp = 2.dp,
    buttonStyle: ButtonStyle = ButtonStyle.HOLO_CHROME,
    content: @Composable () -> Unit
) {
    val brush = rememberHoloBrush(buttonStyle = buttonStyle)

    Box(
        modifier = modifier
            .border(width = borderWidth, brush = brush, shape = shape)
            .clip(shape)
    ) {
        content()
    }
}

@Composable
fun CgeButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    buttonStyle: ButtonStyle = ButtonStyle.HOLO_CHROME,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(16.dp)
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1.0f,
        label = "button_scale"
    )

    HoloRainbowBorderBox(
        modifier = modifier
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = onClick
            ),
        shape = shape,
        buttonStyle = buttonStyle,
        borderWidth = if (enabled) 2.dp else 1.dp
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = if (enabled) 0.15f else 0.05f),
                            Color.White.copy(alpha = if (enabled) 0.05f else 0.02f)
                        )
                    )
                )
                .padding(horizontal = 24.dp, vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = if (enabled) Color.White else Color.Gray,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                style = TextStyle(
                    shadow = Shadow(
                        color = if (enabled) Color.Black.copy(alpha = 0.5f) else Color.Transparent,
                        offset = Offset(2f, 2f),
                        blurRadius = 4f
                    )
                )
            )
        }
    }
}

// 3D-styled Gold Text for the Diamond Counter
@Composable
fun Gold3DText(
    text: String,
    fontSize: TextUnit = 38.sp,
    modifier: Modifier = Modifier
) {
    val goldBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFFFF7C2), // Highlight top
            Color(0xFFFFD700), // Gold body
            Color(0xFFFFB300), // Deep amber
            Color(0xFFCC8800)  // Shadow bottom
        )
    )

    Text(
        text = text,
        fontSize = fontSize,
        fontWeight = FontWeight.Black,
        fontFamily = FontFamily.SansSerif,
        style = TextStyle(
            brush = goldBrush,
            shadow = Shadow(
                color = Color(0x99000000),
                offset = Offset(3f, 4f),
                blurRadius = 6f
            )
        ),
        modifier = modifier
    )
}

// Canvas particle system for falling gold liquid droplets and sparkling diamonds
@Composable
fun GoldDripCanvas(
    modifier: Modifier = Modifier,
    goldDripMultiplier: Double = 1.0,
    holoIntensity: Double = 1.0
) {
    val infiniteTransition = rememberInfiniteTransition(label = "gold_drip")
    val animTime by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween((3000 / goldDripMultiplier).toInt().coerceAtLeast(600), easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "time"
    )

    val randomSeeds = remember {
        List(24) {
            Triple(
                Random.nextFloat(), // x position ratio
                Random.nextFloat(), // speed offset
                Random.nextFloat()  // size factor
            )
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        // Falling molten gold drops
        randomSeeds.forEachIndexed { index, seed ->
            val xRatio = seed.first
            val speedOffset = seed.second
            val sizeFactor = seed.third

            // Progress looped
            val progress = (animTime + speedOffset) % 1f
            val px = width * xRatio
            val py = height * progress

            if (index % 2 == 0) {
                // Gold drop (teardrop shape)
                val dropRadius = (4.dp.toPx() + sizeFactor * 4.dp.toPx())
                val path = Path().apply {
                    moveTo(px, py - dropRadius * 1.5f)
                    cubicTo(
                        px + dropRadius, py,
                        px + dropRadius, py + dropRadius,
                        px, py + dropRadius
                    )
                    cubicTo(
                        px - dropRadius, py + dropRadius,
                        px - dropRadius, py,
                        px, py - dropRadius * 1.5f
                    )
                    close()
                }

                drawPath(
                    path = path,
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0xFFFFF9B0), Color(0xFFFFD700), Color(0xFFCC8400)),
                        center = Offset(px - dropRadius * 0.3f, py),
                        radius = dropRadius * 1.2f
                    )
                )
            } else {
                // Sparkling Diamond
                val diamondSize = (3.dp.toPx() + sizeFactor * 4.dp.toPx()) * holoIntensity.toFloat()
                val diamondAlpha = (sin(progress * PI.toFloat()) * 0.9f).coerceIn(0.1f, 1f)

                drawSparkleStar(
                    center = Offset(px, py),
                    radius = diamondSize,
                    color = Color.White.copy(alpha = diamondAlpha),
                    glowColor = Color(0xFF00E5FF).copy(alpha = diamondAlpha * 0.7f)
                )
            }
        }
    }
}

// Draw 4-pointed radiant sparkle star
private fun DrawScope.drawSparkleStar(
    center: Offset,
    radius: Float,
    color: Color,
    glowColor: Color
) {
    // Subtle glow
    drawCircle(
        color = glowColor,
        radius = radius * 1.8f,
        center = center
    )

    // Vertical beam
    drawLine(
        color = color,
        start = Offset(center.x, center.y - radius * 2.2f),
        end = Offset(center.x, center.y + radius * 2.2f),
        strokeWidth = radius * 0.4f,
        cap = StrokeCap.Round
    )

    // Horizontal beam
    drawLine(
        color = color,
        start = Offset(center.x - radius * 2.2f, center.y),
        end = Offset(center.x + radius * 2.2f, center.y),
        strokeWidth = radius * 0.4f,
        cap = StrokeCap.Round
    )

    // Core
    drawCircle(
        color = Color.White,
        radius = radius * 0.5f,
        center = center
    )
}

// Floating tap particle text that drifts up and fades
@Composable
fun FloatingTapParticle(
    particle: TapParticle,
    onFinished: () -> Unit
) {
    val progress = remember { androidx.compose.animation.core.Animatable(0f) }
    androidx.compose.runtime.LaunchedEffect(particle.id) {
        progress.animateTo(1f, animationSpec = tween(650, easing = FastOutSlowInEasing))
        onFinished()
    }

    val yOffset = -progress.value * 90f
    val alpha = (1f - progress.value).coerceIn(0f, 1f)

    Box(
        modifier = Modifier
            .offset(x = particle.x.dp - 30.dp, y = particle.y.dp + yOffset.dp)
            .alpha(alpha)
    ) {
        Text(
            text = particle.valueText,
            color = if (particle.isGold) Color(0xFFFFD700) else Color(0xFF00FFFF),
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            style = TextStyle(
                shadow = Shadow(
                    color = Color.Black,
                    offset = Offset(2f, 2f),
                    blurRadius = 4f
                )
            )
        )
    }
}
