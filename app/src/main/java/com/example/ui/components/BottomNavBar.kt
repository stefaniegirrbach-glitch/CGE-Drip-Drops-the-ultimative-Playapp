package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ButtonStyle
import com.example.model.ScreenTab

@Composable
fun CgeBottomNavBar(
    activeTab: ScreenTab,
    buttonStyle: ButtonStyle,
    onTabSelected: (ScreenTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val holoBrush = rememberHoloBrush(buttonStyle = buttonStyle)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 10.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Quick Studio Switcher Pills (Icon Maker & Background Editor)
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(bottom = 6.dp)
        ) {
            QuickStudioPill(
                title = "✨ Icon Maker",
                isSelected = activeTab == ScreenTab.ICON_MAKER,
                onClick = { onTabSelected(ScreenTab.ICON_MAKER) },
                testTag = "nav_icon_maker_pill"
            )
            QuickStudioPill(
                title = "🎨 Studio Themes",
                isSelected = activeTab == ScreenTab.BG_EDITOR,
                onClick = { onTabSelected(ScreenTab.BG_EDITOR) },
                testTag = "nav_bg_editor_pill"
            )
        }

        // Main 4-button Navigation Bar
        HoloRainbowBorderBox(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(26.dp),
            borderWidth = 2.dp,
            buttonStyle = buttonStyle
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xEE16002A))
                    .padding(horizontal = 6.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                NavItem(
                    label = "HOME",
                    icon = Icons.Default.Home,
                    isSelected = activeTab == ScreenTab.HOME,
                    holoBrush = holoBrush,
                    onClick = { onTabSelected(ScreenTab.HOME) },
                    testTag = "nav_home"
                )
                NavItem(
                    label = "BOOSTS",
                    icon = Icons.Default.RocketLaunch,
                    isSelected = activeTab == ScreenTab.BOOSTS,
                    holoBrush = holoBrush,
                    onClick = { onTabSelected(ScreenTab.BOOSTS) },
                    testTag = "nav_boosts"
                )
                NavItem(
                    label = "COLLECTION",
                    icon = Icons.Default.EmojiEvents,
                    isSelected = activeTab == ScreenTab.COLLECTION,
                    holoBrush = holoBrush,
                    onClick = { onTabSelected(ScreenTab.COLLECTION) },
                    testTag = "nav_collection"
                )
                NavItem(
                    label = "SHOP",
                    icon = Icons.Default.ShoppingCart,
                    isSelected = activeTab == ScreenTab.SHOP,
                    holoBrush = holoBrush,
                    onClick = { onTabSelected(ScreenTab.SHOP) },
                    testTag = "nav_shop"
                )
            }
        }
    }
}

@Composable
fun QuickStudioPill(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (isSelected) Brush.horizontalGradient(HoloRainbowColors)
                else Brush.horizontalGradient(listOf(Color(0x992B0845), Color(0x9918002B)))
            )
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 4.dp)
            .testTag(testTag)
    ) {
        Text(
            text = title,
            color = if (isSelected) Color.Black else Color(0xFFFFD700),
            fontSize = 11.sp,
            fontWeight = FontWeight.Black
        )
    }
}

@Composable
fun NavItem(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    holoBrush: Brush,
    onClick: () -> Unit,
    testTag: String
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(18.dp))
            .background(if (isSelected) holoBrush else Brush.linearGradient(listOf(Color.Transparent, Color.Transparent)))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) Color.Black else Color(0xFFC77DFF),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                color = if (isSelected) Color.Black else Color(0xFFD4BFFF),
                fontSize = 10.sp,
                fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold
            )
        }
    }
}
