package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CollectionItem
import com.example.ui.components.HoloRainbowBorderBox
import com.example.viewmodel.GameUiState
import com.example.viewmodel.GameViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CollectionScreen(
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
            text = "🏆 CROWN JEWELS & STATS 🏆",
            color = Color(0xFFFFD700),
            fontSize = 21.sp,
            fontWeight = FontWeight.Black
        )
        Text(
            text = "Trophies and achievements of the Dubai Ice Cream Palace",
            color = Color(0xFFD4BFFF),
            fontSize = 12.sp,
            modifier = Modifier.padding(bottom = 14.dp)
        )

        // Stats summary card
        HoloRainbowBorderBox(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            borderWidth = 1.5.dp,
            buttonStyle = uiState.buttonStyle
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xDD1F0038))
                    .padding(14.dp)
            ) {
                Text(
                    text = "Player Statistics",
                    color = Color(0xFF00FFFF),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total Manual Clicks:", color = Color.White, fontSize = 13.sp)
                    Text("${uiState.totalClicks}", color = Color(0xFFFFD700), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Tap Power:", color = Color.White, fontSize = 13.sp)
                    Text("+${uiState.tapValue.toLong()} 💎", color = Color(0xFF00FF88), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Idle Production:", color = Color.White, fontSize = 13.sp)
                    Text("+${uiState.formattedDps} / sec", color = Color(0xFF00FFFF), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(uiState.collections) { item ->
                CollectionCard(item = item, buttonStyle = uiState.buttonStyle)
            }
            item {
                Spacer(modifier = Modifier.height(90.dp))
            }
        }
    }
}

@Composable
fun CollectionCard(
    item: CollectionItem,
    buttonStyle: com.example.model.ButtonStyle
) {
    HoloRainbowBorderBox(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        borderWidth = if (item.isUnlocked) 1.5.dp else 1.dp,
        buttonStyle = buttonStyle
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = if (item.isUnlocked) {
                            listOf(Color(0xDD2A0845), Color(0xDD17002B))
                        } else {
                            listOf(Color(0x8812001F), Color(0x88090012))
                        }
                    )
                )
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(if (item.isUnlocked) Color(0x44FFD700) else Color(0x22FFFFFF), CircleShape)
                    .border(1.dp, if (item.isUnlocked) Color(0xFFFFD700) else Color(0x33FFFFFF), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = if (item.isUnlocked) item.icon else "🔒", fontSize = 22.sp)
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    color = if (item.isUnlocked) Color.White else Color(0xFFAAAAAA),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = item.subtitle,
                    color = Color(0xFFC77DFF),
                    fontSize = 12.sp
                )
                Text(
                    text = item.requirementText,
                    color = Color(0xFF888888),
                    fontSize = 11.sp
                )
            }

            Text(
                text = if (item.isUnlocked) "UNLOCKED ✨" else "LOCKED",
                color = if (item.isUnlocked) Color(0xFFFFD700) else Color(0xFF777777),
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}
