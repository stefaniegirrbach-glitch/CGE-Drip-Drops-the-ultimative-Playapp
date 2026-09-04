package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BackgroundTheme
import com.example.model.ButtonStyle
import com.example.ui.components.HoloRainbowBorderBox
import com.example.ui.components.rememberHoloBrush
import com.example.viewmodel.GameUiState
import com.example.viewmodel.GameViewModel

@Composable
fun BackgroundEditorScreen(
    uiState: GameUiState,
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "🎨 BACKGROUND & BUTTON STUDIO 🎨",
            color = Color(0xFFFFD700),
            fontSize = 20.sp,
            fontWeight = FontWeight.Black
        )
        Text(
            text = "Customize luxury galaxy backgrounds, pastel glitter, & chrome buttons",
            color = Color(0xFFD4BFFF),
            fontSize = 12.sp,
            modifier = Modifier.padding(bottom = 14.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            // Background Themes
            item {
                Text(
                    text = "SELECT BACKGROUND THEME",
                    color = Color(0xFF00FFFF),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            items(BackgroundTheme.values()) { theme ->
                val isSelected = uiState.activeTheme == theme
                HoloRainbowBorderBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.setBackgroundTheme(theme) }
                        .testTag("theme_${theme.name}"),
                    shape = RoundedCornerShape(18.dp),
                    borderWidth = if (isSelected) 2.5.dp else 1.dp,
                    buttonStyle = uiState.buttonStyle
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(theme.primaryBg),
                                        Color(theme.secondaryBg)
                                    )
                                )
                            )
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .background(Color(theme.accentColor), CircleShape)
                                .border(1.5.dp, Color.White, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isSelected) {
                                Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color.White)
                            }
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = theme.title,
                                color = Color.White,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = theme.description,
                                color = Color(0xFFD4BFFF),
                                fontSize = 11.sp
                            )
                        }

                        if (isSelected) {
                            Text(
                                text = "ACTIVE",
                                color = Color(0xFF00FF88),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }
                }
            }

            // Button Styles
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "SELECT BUTTON & BORDER STYLE",
                    color = Color(0xFFFFD700),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            items(ButtonStyle.values()) { style ->
                val isSelected = uiState.buttonStyle == style
                HoloRainbowBorderBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.setButtonStyle(style) }
                        .testTag("btn_style_${style.name}"),
                    shape = RoundedCornerShape(16.dp),
                    borderWidth = if (isSelected) 2.5.dp else 1.dp,
                    buttonStyle = style
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xDD220038))
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = style.title,
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )

                        // Sample Preview Button
                        Box(
                            modifier = Modifier
                                .background(Color(0x55FF00AA), RoundedCornerShape(10.dp))
                                .border(1.5.dp, rememberHoloBrush(buttonStyle = style), RoundedCornerShape(10.dp))
                                .padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text("Sample Tap ✨", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = {
                        viewModel.audio.playBoostFanfare()
                        viewModel.audio.vibratePurchase()
                        Toast.makeText(context, "✨ Background & Button Theme applied to Game!", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("apply_theme_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("APPLY CURRENT THEME TO APP", color = Color.Black, fontWeight = FontWeight.Black, fontSize = 13.sp)
                }
            }

            item {
                Spacer(modifier = Modifier.height(90.dp))
            }
        }
    }
}
