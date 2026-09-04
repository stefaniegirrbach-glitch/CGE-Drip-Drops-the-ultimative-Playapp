package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.UpgradeData
import com.example.ui.components.FloatingTapParticle
import com.example.ui.components.Gold3DText
import com.example.ui.components.GoldDripCanvas
import com.example.ui.components.HoloRainbowBorderBox
import com.example.ui.components.rememberHoloBrush
import com.example.viewmodel.GameUiState
import com.example.viewmodel.GameViewModel
import kotlinx.coroutines.delay
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun HomeScreen(
    uiState: GameUiState,
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    // Dynamic scale for Bigger Ice Cream upgrade
    val baseScale = 1.0f + (uiState.biggerIceCreamLevel * 0.04f).coerceAtMost(0.28f)
    var tapBounce by remember { mutableStateOf(1.0f) }

    LaunchedEffect(tapBounce) {
        if (tapBounce < 1.0f) {
            delay(90)
            tapBounce = 1.0f
        }
    }

    // Floating idle animation for center tower
    val infiniteTransition = rememberInfiniteTransition(label = "idle_anim")
    val floatOffset by infiniteTransition.animateFloat(
        initialValue = -5f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "floating"
    )

    // Cat bounce animation
    val catBounce by infiniteTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "cat_bounce"
    )

    // Orbit angle for "More Cats" upgrade
    val orbitAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "orbit"
    )

    Box(modifier = modifier.fillMaxSize()) {
        // Gold drip and falling diamonds particle background
        GoldDripCanvas(
            modifier = Modifier.fillMaxSize(),
            goldDripMultiplier = 1.0 + (uiState.goldDripLevel * 0.5),
            holoIntensity = 1.0 + (uiState.holoEffectLevel * 0.4)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 14.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ==================== TOP BAR ====================
            TopBarSection(
                dps = uiState.formattedDps,
                onSettingsClick = { viewModel.toggleSettingsDialog(true) },
                onInfoClick = { viewModel.toggleInfoDialog(true) },
                buttonStyle = uiState.buttonStyle
            )

            Spacer(modifier = Modifier.height(10.dp))

            // ==================== CENTER HERO STAGE ====================
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(290.dp)
                    .clip(RoundedCornerShape(26.dp))
                    .testTag("ice_cream_tap_target")
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        tapBounce = 0.92f
                        viewModel.onIceCreamTapped(clickX = 180f, clickY = 150f)
                    },
                contentAlignment = Alignment.Center
            ) {
                // Main hero visual
                Image(
                    painter = painterResource(id = R.drawable.cge_hero_main_art),
                    contentDescription = "CGE Diamond Ice Cream and Cheering Cats",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .offset(y = floatOffset.dp)
                        .scale(baseScale * tapBounce)
                        .clip(RoundedCornerShape(24.dp))
                )

                // Optional Orbiting Cats when "More Cats" upgrade > 0
                if (uiState.moreCatsLevel > 0) {
                    val count = minOf(uiState.moreCatsLevel, 6)
                    for (i in 0 until count) {
                        val angleRad = Math.toRadians((orbitAngle + (i * (360.0 / count))))
                        val radiusX = 120.0
                        val radiusY = 85.0
                        val offsetX = (radiusX * cos(angleRad)).toFloat()
                        val offsetY = (radiusY * sin(angleRad)).toFloat()

                        Box(
                            modifier = Modifier
                                .offset(x = offsetX.dp, y = offsetY.dp)
                                .size(34.dp)
                                .scale(catBounce)
                                .background(Color(0xCC2A0845), CircleShape)
                                .border(1.5.dp, rememberHoloBrush(), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (i % 2 == 0) "🐾" else "🐱",
                                fontSize = 16.sp
                            )
                        }
                    }
                }

                // Shimmering Tap Prompt Overlay
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 8.dp)
                        .background(Color(0xAA140026), RoundedCornerShape(12.dp))
                        .border(1.dp, Color(0x66FFD700), RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "✦ TAP FOR +${uiState.tapValue.toLong()} 💎 ✦",
                        color = Color(0xFFFFE680),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ==================== MIDDLE COUNTER CARD ====================
            MiddleDiamondCounterCard(
                diamonds = uiState.formattedDiamonds,
                dps = uiState.formattedDps,
                offlineEarnings = uiState.offlineEarnings,
                offlineDuration = uiState.offlineDurationText,
                buttonStyle = uiState.buttonStyle,
                onClaimOffline = { viewModel.claimOfflineEarnings() }
            )

            Spacer(modifier = Modifier.height(14.dp))

            // ==================== UPGRADES SECTION ====================
            Text(
                text = "✦ UPGRADES ✦",
                color = Color(0xFFE0AAFF),
                fontSize = 19.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.SansSerif,
                letterSpacing = 2.sp,
                style = TextStyle(
                    shadow = Shadow(
                        color = Color(0xFFFF00AA),
                        blurRadius = 14f
                    )
                ),
                modifier = Modifier.padding(bottom = 10.dp)
            )

            // 2x2 Grid for the 4 upgrades
            val upgrades = uiState.upgrades
            if (upgrades.size >= 4) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    UpgradeCard(
                        upgrade = upgrades[0],
                        canAfford = uiState.diamonds >= upgrades[0].currentCost,
                        buttonStyle = uiState.buttonStyle,
                        onBuy = { viewModel.buyUpgrade(upgrades[0].id) },
                        modifier = Modifier.weight(1f)
                    )
                    UpgradeCard(
                        upgrade = upgrades[1],
                        canAfford = uiState.diamonds >= upgrades[1].currentCost,
                        buttonStyle = uiState.buttonStyle,
                        onBuy = { viewModel.buyUpgrade(upgrades[1].id) },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    UpgradeCard(
                        upgrade = upgrades[2],
                        canAfford = uiState.diamonds >= upgrades[2].currentCost,
                        buttonStyle = uiState.buttonStyle,
                        onBuy = { viewModel.buyUpgrade(upgrades[2].id) },
                        modifier = Modifier.weight(1f)
                    )
                    UpgradeCard(
                        upgrade = upgrades[3],
                        canAfford = uiState.diamonds >= upgrades[3].currentCost,
                        buttonStyle = uiState.buttonStyle,
                        onBuy = { viewModel.buyUpgrade(upgrades[3].id) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(90.dp)) // Padding for bottom nav
        }

        // Tap particles overlay
        uiState.particles.forEach { particle ->
            FloatingTapParticle(
                particle = particle,
                onFinished = { viewModel.removeParticle(particle.id) }
            )
        }
    }
}

@Composable
fun TopBarSection(
    dps: String,
    onSettingsClick: () -> Unit,
    onInfoClick: () -> Unit,
    buttonStyle: com.example.model.ButtonStyle
) {
    HoloRainbowBorderBox(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        borderWidth = 2.dp,
        buttonStyle = buttonStyle
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xCC2A004E), Color(0xEE120024))
                    )
                )
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Gear button (Settings)
                IconButton(
                    onClick = onSettingsClick,
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color(0x553C096C), CircleShape)
                        .testTag("settings_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = Color(0xFFC77DFF),
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Center Title
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "💎 ",
                        fontSize = 20.sp
                    )
                    Text(
                        text = "CGE ",
                        color = Color(0xFF00FFFF),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.SansSerif,
                        style = TextStyle(
                            shadow = Shadow(
                                color = Color(0xFFFF007F),
                                blurRadius = 12f
                            )
                        )
                    )
                    Text(
                        text = "DRIP DROP",
                        color = Color(0xFFFFD700),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.SansSerif,
                        style = TextStyle(
                            shadow = Shadow(
                                color = Color(0xFF00FFFF),
                                blurRadius = 12f
                            )
                        )
                    )
                    Text(
                        text = " ✨",
                        fontSize = 18.sp
                    )
                }

                // Info button
                IconButton(
                    onClick = onInfoClick,
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color(0x553C096C), CircleShape)
                        .testTag("info_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Game Info",
                        tint = Color(0xFFC77DFF),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(2.dp))

            // Subtitle
            Text(
                text = "IDLE CLICKER • v1.2.4 • IDLE +${dps}/s",
                color = Color(0xFFD4BFFF),
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.5.sp
            )
        }
    }
}

@Composable
fun MiddleDiamondCounterCard(
    diamonds: String,
    dps: String,
    offlineEarnings: Long,
    offlineDuration: String,
    buttonStyle: com.example.model.ButtonStyle,
    onClaimOffline: () -> Unit
) {
    HoloRainbowBorderBox(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        borderWidth = 2.dp,
        buttonStyle = buttonStyle
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xDD28004B),
                            Color(0xEE16002C)
                        )
                    )
                )
                .padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Large 3D diamond jewel & gold stack
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(Color(0x6600FFFF), Color(0x11000000))
                            ),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "💎",
                        fontSize = 44.sp,
                        style = TextStyle(
                            shadow = Shadow(
                                color = Color(0xFF00FFFF),
                                blurRadius = 16f
                            )
                        )
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Numbers Column
                Column {
                    // Gold 3D Counter
                    Gold3DText(
                        text = diamonds,
                        fontSize = 32.sp
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Diamonds",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "+$dps / sec ▲",
                            color = Color(0xFF00F5D4),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Offline earnings indicator
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0x33000000), RoundedCornerShape(10.dp))
                    .clickable { onClaimOffline() }
                    .padding(horizontal = 10.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Offline Earnings: +${NumberFormat.getNumberInstance(Locale.US).format(offlineEarnings)} ($offlineDuration)",
                    color = Color(0xFFD4BFFF),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "Claim 💰",
                    color = Color(0xFFFFD700),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun UpgradeCard(
    upgrade: UpgradeData,
    canAfford: Boolean,
    buttonStyle: com.example.model.ButtonStyle,
    onBuy: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(targetValue = if (isPressed) 0.94f else 1.0f, label = "upgrade_press")

    val iconEmoji = when (upgrade.id) {
        "bigger_ice_cream" -> "🍦+"
        "more_cats" -> "🐾+"
        "holo_effect" -> "✨"
        "gold_drip" -> "💧x2"
        else -> "💎"
    }

    HoloRainbowBorderBox(
        modifier = modifier
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onBuy
            )
            .testTag("upgrade_${upgrade.id}"),
        shape = RoundedCornerShape(18.dp),
        borderWidth = if (canAfford) 1.8.dp else 1.dp,
        buttonStyle = buttonStyle
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(if (canAfford) 0xCC2A084D else 0x9919022E),
                            Color(if (canAfford) 0xDD18002E else 0xAA0F001C)
                        )
                    )
                )
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Icon + Plus
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(Color(0x333C096C), CircleShape)
                    .border(1.dp, Color(0x66FF00C8), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = iconEmoji,
                    fontSize = 20.sp
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = upgrade.title,
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Lvl ${upgrade.level}",
                color = Color(0xFFC77DFF),
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Cost pill
            Box(
                modifier = Modifier
                    .background(
                        if (canAfford) Color(0x44FFD700) else Color(0x22FFFFFF),
                        RoundedCornerShape(8.dp)
                    )
                    .border(
                        1.dp,
                        if (canAfford) Color(0xFFFFD700) else Color(0x44FFFFFF),
                        RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = "Cost: ${NumberFormat.getNumberInstance(Locale.US).format(upgrade.currentCost)} 💎",
                    color = if (canAfford) Color(0xFFFFE680) else Color(0xFFAAAAAA),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
    }
}
