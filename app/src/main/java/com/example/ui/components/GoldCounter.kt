package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.ButtonStyle

@Composable
fun GoldCounter(
    diamonds: String,
    buttonStyle: ButtonStyle = ButtonStyle.HOLO_CHROME,
    modifier: Modifier = Modifier
) {
    HoloRainbowBorderBox(
        modifier = modifier,
        buttonStyle = buttonStyle,
        shape = RoundedCornerShape(bottomStart = 24.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.15f),
                            Color.White.copy(alpha = 0.05f)
                        )
                    )
                )
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Gold Icon
                Icon(
                    painter = painterResource(id = android.R.drawable.star_big_on),
                    contentDescription = "Gold",
                    tint = Color(0xFFFFD700),
                    modifier = Modifier.size(20.dp)
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                // Neon Glowing Text
                Text(
                    text = diamonds,
                    color = Color(0xFFFFD700),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(
                        shadow = Shadow(
                            color = Color(0xFFFFD700).copy(alpha = 0.8f),
                            blurRadius = 12f
                        )
                    )
                )
                
                Spacer(modifier = Modifier.width(4.dp))
                
                Text(
                    text = "💎",
                    fontSize = 16.sp
                )
            }
        }
    }
}
