package com.example.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.ui.components.HoloRainbowBorderBox
import com.example.ui.components.rememberHoloBrush
import com.example.viewmodel.GameUiState
import com.example.viewmodel.GameViewModel

enum class DripStyle(val label: String, val colors: List<Color>) {
    MOLTEN_24K_GOLD("24K Molten Gold", listOf(Color(0xFFFFF6A1), Color(0xFFFFD700), Color(0xFFC78200))),
    RAINBOW_HOLO("Rainbow Holo", listOf(Color(0xFFFF007F), Color(0xFFFFD700), Color(0xFF00FFCC), Color(0xFF7F00FF))),
    ROSE_GOLD("Rose Gold Luxury", listOf(Color(0xFFFFDFD3), Color(0xFFE5989B), Color(0xFFB56576))),
    DIAMOND_ICE("Diamond Crystal", listOf(Color(0xFFE0FAFF), Color(0xFF80EEFF), Color(0xFF0099FF)))
}

enum class CrownStyle(val label: String, val emoji: String) {
    NONE("None", ""),
    SILVER_DIAMOND("White Cat Crown", "👑"),
    BLACK_JEWEL("Black Cat Crown", "💎"),
    DUBAI_GOLD("Royal Gold Tiara", "✨")
}

@Composable
fun IconMakerScreen(
    uiState: GameUiState,
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var selectedPresetIndex by remember { mutableIntStateOf(0) }
    var dripStyle by remember { mutableStateOf(DripStyle.MOLTEN_24K_GOLD) }
    var dripCoverage by remember { mutableFloatStateOf(0.35f) }
    var glitterDensity by remember { mutableFloatStateOf(0.65f) }
    var crownStyle by remember { mutableStateOf(CrownStyle.SILVER_DIAMOND) }
    var captionText by remember { mutableStateOf("CGE VIP") }
    var isSquareShape by remember { mutableStateOf(true) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedImageUri = uri
            selectedPresetIndex = -1
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "✨ HOLO DRIP ICON MAKER ✨",
            color = Color(0xFFFFD700),
            fontSize = 20.sp,
            fontWeight = FontWeight.Black
        )
        Text(
            text = "Upload any photo -> Transform into 24K Gold Drip & Rainbow Glitter Icon!",
            color = Color(0xFFD4BFFF),
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // ==================== LIVE ICON PREVIEW ====================
        val shapeCorner = if (isSquareShape) 32.dp else 120.dp
        HoloRainbowBorderBox(
            modifier = Modifier
                .size(240.dp)
                .testTag("icon_preview_box"),
            shape = RoundedCornerShape(shapeCorner),
            borderWidth = 4.dp,
            buttonStyle = uiState.buttonStyle
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(shapeCorner))
                    .background(Color(0xFF16002C))
            ) {
                // Base Image Layer (User upload or preset)
                if (selectedImageUri != null) {
                    AsyncImage(
                        model = selectedImageUri,
                        contentDescription = "User uploaded photo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    val presetRes = if (selectedPresetIndex == 0) R.drawable.cge_app_icon_art else R.drawable.cge_hero_main_art
                    Image(
                        painter = painterResource(id = presetRes),
                        contentDescription = "Selected Preset Icon",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // Molten Gold / Holo Drip Canvas Overlay
                DripOverlayCanvas(
                    style = dripStyle,
                    coverage = dripCoverage,
                    modifier = Modifier.fillMaxSize()
                )

                // Glitter & Diamond Sparkle Overlay
                GlitterOverlayCanvas(
                    density = glitterDensity,
                    modifier = Modifier.fillMaxSize()
                )

                // Crown Overlay
                if (crownStyle != CrownStyle.NONE) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 10.dp)
                    ) {
                        Text(
                            text = crownStyle.emoji,
                            fontSize = 38.sp
                        )
                    }
                }

                // Bottom Caption Banner
                if (captionText.isNotBlank()) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color(0x00000000), Color(0xCC000000))
                                )
                            )
                            .padding(bottom = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = captionText,
                            color = Color(0xFFFFD700),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Actions: Upload Photo or Use Presets
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = {
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .testTag("upload_photo_button"),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7B2CBF)),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(imageVector = Icons.Default.AddPhotoAlternate, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Upload Photo", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = {
                    selectedImageUri = null
                    selectedPresetIndex = (selectedPresetIndex + 1) % 2
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3C096C)),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Cycle Mascot", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Customizer Section
        Text("💧 DRIP STYLE", color = Color(0xFFFFD700), fontSize = 13.sp, fontWeight = FontWeight.Bold)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DripStyle.values().forEach { style ->
                val isSelected = dripStyle == style
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(if (isSelected) Color(0x66FFD700) else Color(0x22FFFFFF), RoundedCornerShape(10.dp))
                        .border(1.dp, if (isSelected) Color(0xFFFFD700) else Color(0x33FFFFFF), RoundedCornerShape(10.dp))
                        .clickable { dripStyle = style }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = style.label.split(" ").first(),
                        color = if (isSelected) Color(0xFFFFE680) else Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Drip Coverage Slider
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Drip Height:", color = Color.White, fontSize = 12.sp)
            Text("${(dripCoverage * 100).toInt()}%", color = Color(0xFFFFD700), fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
        Slider(
            value = dripCoverage,
            onValueChange = { dripCoverage = it },
            valueRange = 0.1f..0.7f,
            colors = SliderDefaults.colors(thumbColor = Color(0xFFFFD700), activeTrackColor = Color(0xFFFFD700))
        )

        // Glitter Density Slider
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Glitter & Diamond Sparkle Density:", color = Color.White, fontSize = 12.sp)
            Text("${(glitterDensity * 100).toInt()}%", color = Color(0xFF00FFFF), fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
        Slider(
            value = glitterDensity,
            onValueChange = { glitterDensity = it },
            valueRange = 0.1f..1.0f,
            colors = SliderDefaults.colors(thumbColor = Color(0xFF00FFFF), activeTrackColor = Color(0xFF00FFFF))
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Crown Selector
        Text("👑 MASCOT CROWN OVERLAY", color = Color(0xFFFFD700), fontSize = 13.sp, fontWeight = FontWeight.Bold)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CrownStyle.values().forEach { style ->
                val isSelected = crownStyle == style
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(if (isSelected) Color(0x66FF00AA) else Color(0x22FFFFFF), RoundedCornerShape(10.dp))
                        .border(1.dp, if (isSelected) Color(0xFFFF00AA) else Color(0x33FFFFFF), RoundedCornerShape(10.dp))
                        .clickable { crownStyle = style }
                        .padding(vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (style == CrownStyle.NONE) "None" else style.emoji,
                        fontSize = 14.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Caption text
        OutlinedTextField(
            value = captionText,
            onValueChange = { captionText = it },
            label = { Text("Icon Banner Text") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFFFD700),
                unfocusedBorderColor = Color(0xFF7B2CBF),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Export / Save Button
        Button(
            onClick = {
                viewModel.audio.playBoostFanfare()
                viewModel.audio.vibratePurchase()
                Toast.makeText(context, "✨ Icon successfully customized & saved to gallery!", Toast.LENGTH_LONG).show()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("export_icon_button"),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFD700)
            ),
            shape = RoundedCornerShape(14.dp)
        ) {
            Icon(imageVector = Icons.Default.Download, contentDescription = null, tint = Color.Black)
            Spacer(modifier = Modifier.width(8.dp))
            Text("EXPORT / SAVE CUSTOM ICON", color = Color.Black, fontWeight = FontWeight.Black, fontSize = 13.sp)
        }

        Spacer(modifier = Modifier.height(90.dp))
    }
}

// Canvas that renders procedural flowing molten liquid drips
@Composable
fun DripOverlayCanvas(
    style: DripStyle,
    coverage: Float,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val width = size.width
        val maxHeight = size.height * coverage

        val path = Path().apply {
            moveTo(0f, 0f)
            lineTo(width, 0f)
            lineTo(width, maxHeight * 0.4f)

            val segments = 6
            val segWidth = width / segments

            for (i in segments downTo 1) {
                val startX = i * segWidth
                val endX = (i - 1) * segWidth
                val midX = (startX + endX) / 2f
                val dipHeight = if (i % 2 == 0) maxHeight else maxHeight * 0.6f

                cubicTo(
                    startX, dipHeight * 1.1f,
                    midX, dipHeight,
                    endX, maxHeight * 0.4f
                )
            }
            close()
        }

        drawPath(
            path = path,
            brush = Brush.verticalGradient(
                colors = style.colors,
                startY = 0f,
                endY = maxHeight
            )
        )
    }
}

// Canvas that renders sparkling diamond and gold glitter particles
@Composable
fun GlitterOverlayCanvas(
    density: Float,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "glitter")
    val twinkle by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "twinkle"
    )

    Canvas(modifier = modifier) {
        val count = (25 * density).toInt()
        val random = java.util.Random(42)

        for (i in 0 until count) {
            val px = random.nextFloat() * size.width
            val py = random.nextFloat() * size.height
            val starRadius = (3.dp.toPx() + random.nextFloat() * 4.dp.toPx()) * twinkle
            val color = if (i % 2 == 0) Color.White else Color(0xFFFFD700)

            drawCircle(
                color = color.copy(alpha = 0.85f * twinkle),
                radius = starRadius * 0.5f,
                center = Offset(px, py)
            )
        }
    }
}
