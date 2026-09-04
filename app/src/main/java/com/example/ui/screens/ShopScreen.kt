package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ScreenTab
import com.example.ui.components.HoloRainbowBorderBox
import com.example.viewmodel.GameUiState
import com.example.viewmodel.GameViewModel

@Composable
fun ShopScreen(
    uiState: GameUiState,
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "🛍️ DUBAI LUXURY BOUTIQUE 🛍️",
            color = Color(0xFFFFD700),
            fontSize = 20.sp,
            fontWeight = FontWeight.Black
        )
        Text(
            text = "Exclusive themes, diamond vault rewards & creative studios",
            color = Color(0xFFD4BFFF),
            fontSize = 12.sp,
            modifier = Modifier.padding(bottom = 14.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            // Featured studio tools banner
            item {
                HoloRainbowBorderBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.selectTab(ScreenTab.ICON_MAKER) },
                    shape = RoundedCornerShape(20.dp),
                    borderWidth = 2.dp,
                    buttonStyle = uiState.buttonStyle
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(Color(0xFF4A0072), Color(0xFF1B003A))
                                )
                            )
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("✨", fontSize = 32.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Holo Icon Maker & Photo Changer",
                                color = Color(0xFFFFD700),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Upload any photo -> convert into holo rainbow drip & gold glitter diamond icon!",
                                color = Color.White,
                                fontSize = 11.sp
                            )
                        }
                        Text("OPEN ➔", color = Color(0xFF00FFFF), fontSize = 12.sp, fontWeight = FontWeight.Black)
                    }
                }
            }

            item {
                HoloRainbowBorderBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.selectTab(ScreenTab.BG_EDITOR) },
                    shape = RoundedCornerShape(20.dp),
                    borderWidth = 2.dp,
                    buttonStyle = uiState.buttonStyle
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(Color(0xFF2C0B4E), Color(0xFF0F0022))
                                )
                            )
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("🎨", fontSize = 32.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Background & Button Studio",
                                color = Color(0xFF00FF88),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Pastel glitter, 24K gold, rainbow aurora and custom holo buttons!",
                                color = Color.White,
                                fontSize = 11.sp
                            )
                        }
                        Text("EDIT ➔", color = Color(0xFF00FFFF), fontSize = 12.sp, fontWeight = FontWeight.Black)
                    }
                }
            }

            // Shop packages
            item {
                ShopPackageCard(
                    title = "Celestial VIP Ring",
                    desc = "Permanent +20% Tap Multiplier",
                    cost = "1,500,000 💎",
                    icon = "💍",
                    canAfford = uiState.diamonds >= 1_500_000,
                    onBuy = {
                        if (uiState.diamonds >= 1_500_000) {
                            viewModel.onIceCreamTapped(180f, 150f)
                        }
                    }
                )
            }

            item {
                ShopPackageCard(
                    title = "24K Molten Gold Cascade",
                    desc = "Permanent 2x Golden Drip rate",
                    cost = "3,000,000 💎",
                    icon = "👑",
                    canAfford = uiState.diamonds >= 3_000_000,
                    onBuy = {
                        if (uiState.diamonds >= 3_000_000) {
                            viewModel.buyUpgrade("gold_drip")
                        }
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(90.dp))
            }
        }
    }
}

@Composable
fun ShopPackageCard(
    title: String,
    desc: String,
    cost: String,
    icon: String,
    canAfford: Boolean,
    onBuy: () -> Unit
) {
    HoloRainbowBorderBox(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        borderWidth = 1.5.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xDD1F0038))
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .background(Color(0x33FFD700), CircleShape)
                    .border(1.dp, Color(0xFFFFD700), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = icon, fontSize = 22.sp)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text(text = desc, color = Color(0xFFD4BFFF), fontSize = 11.sp)
            }

            Box(
                modifier = Modifier
                    .background(if (canAfford) Color(0x44FFD700) else Color(0x22FFFFFF), RoundedCornerShape(10.dp))
                    .border(1.dp, if (canAfford) Color(0xFFFFD700) else Color(0x33FFFFFF), RoundedCornerShape(10.dp))
                    .clickable(enabled = canAfford) { onBuy() }
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(text = cost, color = if (canAfford) Color(0xFFFFE680) else Color(0xFFAAAAAA), fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
