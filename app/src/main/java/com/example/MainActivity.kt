package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.model.BackgroundTheme
import com.example.model.ScreenTab
import com.example.ui.components.CgeBottomNavBar
import com.example.ui.components.CosmicStarfieldBackground
import com.example.ui.components.InfoDialog
import com.example.ui.components.OfflineEarningsDialog
import com.example.ui.components.SettingsDialog
import com.example.ui.screens.BackgroundEditorScreen
import com.example.ui.screens.BoostsScreen
import com.example.ui.screens.CollectionScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.IconMakerScreen
import com.example.ui.screens.ShopScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.GameViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val uiState by viewModel.uiState.collectAsState()

            MyApplicationTheme {
                MainAppScreen(
                    viewModel = viewModel,
                    uiState = uiState
                )
            }
        }
    }
}

@Composable
fun MainAppScreen(
    viewModel: GameViewModel,
    uiState: com.example.viewmodel.GameUiState
) {
    // Dynamic theme background gradient
    val backgroundBrush = when (uiState.activeTheme) {
        BackgroundTheme.PURPLE_GALAXY -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFF1A0033),
                Color(0xFF2C004F),
                Color(0xFF120024),
                Color(0xFF090014)
            )
        )
        BackgroundTheme.RAINBOW_AURORA -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFF003049),
                Color(0xFF1B4965),
                Color(0xFF7209B7),
                Color(0xFF10002B)
            )
        )
        BackgroundTheme.DUBAI_GOLD -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFF332200),
                Color(0xFF573E06),
                Color(0xFF2B1D00),
                Color(0xFF140D00)
            )
        )
        BackgroundTheme.PASTEL_GLITTER -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFF3F2B56),
                Color(0xFF5A3E7B),
                Color(0xFF2F1D45),
                Color(0xFF190C28)
            )
        )
        BackgroundTheme.CRYSTAL_PRISM -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFF002B36),
                Color(0xFF073642),
                Color(0xFF1E1035),
                Color(0xFF0A0514)
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush)
    ) {
        // Twinkling stars in deep space
        CosmicStarfieldBackground()

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            bottomBar = {
                CgeBottomNavBar(
                    activeTab = uiState.activeTab,
                    buttonStyle = uiState.buttonStyle,
                    onTabSelected = { viewModel.selectTab(it) }
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .statusBarsPadding()
            ) {
                when (uiState.activeTab) {
                    ScreenTab.HOME -> HomeScreen(uiState = uiState, viewModel = viewModel)
                    ScreenTab.BOOSTS -> BoostsScreen(uiState = uiState, viewModel = viewModel)
                    ScreenTab.COLLECTION -> CollectionScreen(uiState = uiState, viewModel = viewModel)
                    ScreenTab.SHOP -> ShopScreen(uiState = uiState, viewModel = viewModel)
                    ScreenTab.ICON_MAKER -> IconMakerScreen(uiState = uiState, viewModel = viewModel)
                    ScreenTab.BG_EDITOR -> BackgroundEditorScreen(uiState = uiState, viewModel = viewModel)
                }
            }
        }

        // Global Dialogs
        SettingsDialog(uiState = uiState, viewModel = viewModel)
        InfoDialog(uiState = uiState, viewModel = viewModel)
        OfflineEarningsDialog(uiState = uiState, viewModel = viewModel)
    }
}
