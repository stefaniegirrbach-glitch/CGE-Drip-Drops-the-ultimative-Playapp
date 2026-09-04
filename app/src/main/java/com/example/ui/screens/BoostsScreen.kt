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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BoostData
import com.example.model.BoostKind
import com.example.ui.components.HoloRainbowBorderBox
import com.example.viewmodel.GameUiState
import com.example.viewmodel.GameViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun BoostsScreen(
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
            text = "🚀 ROYAL BOOSTERS 🚀",
            color = Color(0xFFFFD700),
            fontSize = 22.sp,
            fontWeight = FontWeight.Black
        )
        Text(
            text = "Supercharge your diamond drips and auto-taps!",
            color = Color(0xFFD4BFFF),
            fontSize = 13.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(uiState.boosts) { boost ->
                BoostCardItem(
                    boost = boost,
                    canAfford = boost.costDiamonds == 0L || uiState.diamonds >= boost.costDiamonds,
                    buttonStyle = uiState.buttonStyle,
                    onActivate = { viewModel.activateBoost(boost.id) }
                )
            }
            item {
                Spacer(modifier = Modifier.height(90.dp))
            }
        }
    }
}

@Composable
fun BoostCardItem(
    boost: BoostData,
    canAfford: Boolean,
    buttonStyle: com.example.model.ButtonStyle,
    onActivate: () -> Unit
) {
    val emoji = when (boost.type) {
        BoostKind.TAP_MULTIPLIER -> "⚡"
        BoostKind.IDLE_MULTIPLIER -> "🌌"
        BoostKind.AUTO_CLICK -> "🐾"
        BoostKind.INSTANT_VAULT -> "💰"
    }

    HoloRainbowBorderBox(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("boost_${boost.id}"),
        shape = RoundedCornerShape(20.dp),
        borderWidth = if (boost.isActive) 2.5.dp else 1.5.dp,
        buttonStyle = buttonStyle
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = if (boost.isActive) {
                            listOf(Color(0xEE4A0E4E), Color(0xEE1E0836))
                        } else {
                            listOf(Color(0xCC240046), Color(0xCC10002B))
                        }
                    )
                )
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .background(Color(0x443C096C), CircleShape)
                    .border(1.5.dp, Color(0xFFFF00AA), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = emoji, fontSize = 26.sp)
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = boost.title,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = boost.description,
                    color = Color(0xFFD4BFFF),
                    fontSize = 12.sp
                )

                if (boost.isActive) {
                    Text(
                        text = "ACTIVE! ${boost.remainingSeconds}s left",
                        color = Color(0xFF00FF88),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Action Button
            Box(
                modifier = Modifier
                    .background(
                        if (boost.isActive) Color(0x3300FF88)
                        else if (canAfford) Color(0x44FFD700)
                        else Color(0x22FFFFFF),
                        RoundedCornerShape(12.dp)
                    )
                    .border(
                        1.5.dp,
                        if (boost.isActive) Color(0xFF00FF88)
                        else if (canAfford) Color(0xFFFFD700)
                        else Color(0x44FFFFFF),
                        RoundedCornerShape(12.dp)
                    )
                    .clickable(enabled = !boost.isActive && canAfford) { onActivate() }
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (boost.isActive) "ACTIVE"
                    else if (boost.costDiamonds == 0L) "FREE CLAIM"
                    else "${NumberFormat.getNumberInstance(Locale.US).format(boost.costDiamonds)} 💎",
                    color = if (boost.isActive) Color(0xFF00FF88)
                    else if (canAfford) Color(0xFFFFE680)
                    else Color(0xFFAAAAAA),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
