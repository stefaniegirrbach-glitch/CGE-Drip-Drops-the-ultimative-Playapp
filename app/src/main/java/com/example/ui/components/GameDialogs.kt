package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.viewmodel.GameUiState
import com.example.viewmodel.GameViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun SettingsDialog(
    uiState: GameUiState,
    viewModel: GameViewModel
) {
    if (!uiState.showSettingsDialog) return

    Dialog(onDismissRequest = { viewModel.toggleSettingsDialog(false) }) {
        HoloRainbowBorderBox(
            shape = RoundedCornerShape(24.dp),
            borderWidth = 2.5.dp,
            buttonStyle = uiState.buttonStyle
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color(0xFF260045), Color(0xFF140024))
                        )
                    )
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "⚙️ SETTINGS ⚙️",
                    color = Color(0xFFFFD700),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Sound toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Sound Effects & Chimes", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    Switch(
                        checked = uiState.isSoundEnabled,
                        onCheckedChange = { viewModel.toggleSound() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color(0xFFFFD700),
                            checkedTrackColor = Color(0xFF7B2CBF)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Haptic feedback toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Haptic Vibration", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    Switch(
                        checked = uiState.isHapticsEnabled,
                        onCheckedChange = { viewModel.toggleHaptics() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color(0xFFFFD700),
                            checkedTrackColor = Color(0xFF7B2CBF)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Reset Progress
                Button(
                    onClick = {
                        viewModel.resetProgress()
                        viewModel.toggleSettingsDialog(false)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0x33FF0055)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("RESET GAME PROGRESS", color = Color(0xFFFF4D6D), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Close Button
                Button(
                    onClick = { viewModel.toggleSettingsDialog(false) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("DONE", color = Color.Black, fontSize = 13.sp, fontWeight = FontWeight.Black)
                }
            }
        }
    }
}

@Composable
fun InfoDialog(
    uiState: GameUiState,
    viewModel: GameViewModel
) {
    if (!uiState.showInfoDialog) return

    Dialog(onDismissRequest = { viewModel.toggleInfoDialog(false) }) {
        HoloRainbowBorderBox(
            shape = RoundedCornerShape(24.dp),
            borderWidth = 2.5.dp,
            buttonStyle = uiState.buttonStyle
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color(0xFF260045), Color(0xFF140024))
                        )
                    )
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "💎 CGE DRIP DROP 💎",
                    color = Color(0xFFFFD700),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "IDLE CLICKER • v1.2.4",
                    color = Color(0xFFD4BFFF),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Step into the ultra-luxury world of Dubai's Grand Prismatic Diamond Ice Cream Tower! Tap the giant crystal scoops to harvest diamonds, collect dripping molten 24K gold, and team up with your crowned mascot kittens.",
                    color = Color.White,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0x33000000), RoundedCornerShape(12.dp))
                        .padding(10.dp)
                ) {
                    Text("• Tap Power: +${uiState.tapValue.toLong()} 💎 per tap", color = Color(0xFF00FF88), fontSize = 12.sp)
                    Text("• Idle Earnings: +${uiState.formattedDps} 💎 per second", color = Color(0xFF00FFFF), fontSize = 12.sp)
                    Text("• Mascots: White Cat (Silver Crown) & Black Cat (Rainbow Tiara)", color = Color(0xFFFFD700), fontSize = 12.sp)
                    Text("• Built-in: Holo Icon Maker & Background Studio", color = Color(0xFFE0AAFF), fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { viewModel.toggleInfoDialog(false) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("CLOSE", color = Color.Black, fontSize = 13.sp, fontWeight = FontWeight.Black)
                }
            }
        }
    }
}

@Composable
fun OfflineEarningsDialog(
    uiState: GameUiState,
    viewModel: GameViewModel
) {
    if (!uiState.showOfflineDialog) return

    Dialog(onDismissRequest = { viewModel.dismissOfflineEarnings() }) {
        HoloRainbowBorderBox(
            shape = RoundedCornerShape(26.dp),
            borderWidth = 3.dp,
            buttonStyle = uiState.buttonStyle
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color(0xFF33005A), Color(0xFF140026))
                        )
                    )
                    .padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "✨ WELCOME BACK! ✨",
                    color = Color(0xFFFFD700),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "While you were away (${uiState.offlineDurationText}):",
                    color = Color(0xFFD4BFFF),
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .background(Color(0x33FFD700), CircleShape)
                        .border(2.dp, Color(0xFFFFD700), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("💰", fontSize = 34.sp)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "+${NumberFormat.getNumberInstance(Locale.US).format(uiState.offlineEarnings)}",
                    color = Color(0xFFFFD700),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "DIAMONDS COLLECTED",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = { viewModel.claimOfflineEarnings() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text("COLLECT DIAMONDS 💎", color = Color.Black, fontSize = 14.sp, fontWeight = FontWeight.Black)
                }
            }
        }
    }
}
