package com.example.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.BackgroundTheme
import com.example.model.BoostData
import com.example.model.BoostKind
import com.example.model.ButtonStyle
import com.example.model.CollectionItem
import com.example.model.ScreenTab
import com.example.model.TapParticle
import com.example.model.UpgradeData
import com.example.sound.GameAudio
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale
import kotlin.random.Random

data class GameUiState(
    val diamonds: Double = 2_847_392.0,
    val diamondsPerSec: Double = 1_241.0,
    val tapValue: Double = 100.0,
    val offlineEarnings: Long = 482_103L,
    val offlineDurationText: String = "6m 12s",
    val showOfflineDialog: Boolean = false,
    val activeTab: ScreenTab = ScreenTab.HOME,
    val activeTheme: BackgroundTheme = BackgroundTheme.PURPLE_GALAXY,
    val buttonStyle: ButtonStyle = ButtonStyle.HOLO_CHROME,
    val upgrades: List<UpgradeData> = emptyList(),
    val boosts: List<BoostData> = emptyList(),
    val collections: List<CollectionItem> = emptyList(),
    val particles: List<TapParticle> = emptyList(),
    val isSoundEnabled: Boolean = true,
    val isHapticsEnabled: Boolean = true,
    val totalClicks: Long = 0L,
    val activeTapMultiplier: Double = 1.0,
    val activeIdleMultiplier: Double = 1.0,
    val isAutoClicking: Boolean = false,
    val showSettingsDialog: Boolean = false,
    val showInfoDialog: Boolean = false
) {
    val biggerIceCreamLevel: Int
        get() = upgrades.find { it.id == "bigger_ice_cream" }?.level ?: 0

    val moreCatsLevel: Int
        get() = upgrades.find { it.id == "more_cats" }?.level ?: 0

    val holoEffectLevel: Int
        get() = upgrades.find { it.id == "holo_effect" }?.level ?: 0

    val goldDripLevel: Int
        get() = upgrades.find { it.id == "gold_drip" }?.level ?: 0

    val formattedDiamonds: String
        get() = NumberFormat.getNumberInstance(Locale.US).format(diamonds.toLong())

    val formattedDps: String
        get() = NumberFormat.getNumberInstance(Locale.US).format((diamondsPerSec * activeIdleMultiplier).toLong())
}

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs: SharedPreferences = application.getSharedPreferences("cge_drip_drop_save", Context.MODE_PRIVATE)
    val audio = GameAudio(application)

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    init {
        loadSavedGame()
        startIdleLoop()
        startBoostTimerLoop()
    }

    private fun loadSavedGame() {
        val isFirstLaunch = !prefs.contains("diamonds")

        val initialUpgrades = listOf(
            UpgradeData(
                id = "bigger_ice_cream",
                title = "Bigger Ice Cream",
                subtitle = "+10% Size & Tap Power",
                iconName = "ice_cream",
                baseCost = 50_000L,
                costMultiplier = 1.30,
                level = prefs.getInt("upgrade_bigger_ice_cream", 0),
                dpsBonus = 50L,
                tapBonus = 100L
            ),
            UpgradeData(
                id = "more_cats",
                title = "More Cats",
                subtitle = "Orbiting Cheering Kittens",
                iconName = "paw",
                baseCost = 120_000L,
                costMultiplier = 1.35,
                level = prefs.getInt("upgrade_more_cats", 0),
                dpsBonus = 350L,
                tapBonus = 25L
            ),
            UpgradeData(
                id = "holo_effect",
                title = "Holo Effect",
                subtitle = "Iridescent Sparkle Glow",
                iconName = "sparkle",
                baseCost = 300_000L,
                costMultiplier = 1.40,
                level = prefs.getInt("upgrade_holo_effect", 0),
                dpsBonus = 800L,
                tapBonus = 50L
            ),
            UpgradeData(
                id = "gold_drip",
                title = "Gold Drip x2",
                subtitle = "Doubles Gold Drops & Value",
                iconName = "gold_drop",
                baseCost = 750_000L,
                costMultiplier = 1.50,
                level = prefs.getInt("upgrade_gold_drip", 0),
                dpsBonus = 2000L,
                tapBonus = 200L
            )
        )

        val initialBoosts = listOf(
            BoostData(
                id = "golden_rush",
                title = "Golden Drip Frenzy",
                description = "10x Tap Value for 30 seconds!",
                durationSec = 30,
                costDiamonds = 150_000L,
                multiplier = 10.0,
                type = BoostKind.TAP_MULTIPLIER
            ),
            BoostData(
                id = "diamond_storm",
                title = "Diamond Storm",
                description = "5x Idle Earnings for 60 seconds!",
                durationSec = 60,
                costDiamonds = 250_000L,
                multiplier = 5.0,
                type = BoostKind.IDLE_MULTIPLIER
            ),
            BoostData(
                id = "cat_paws",
                title = "Royal Cat Auto-Paws",
                description = "Cats frenzy-tap 12 times/sec for 20s!",
                durationSec = 20,
                costDiamonds = 180_000L,
                multiplier = 1.0,
                type = BoostKind.AUTO_CLICK
            ),
            BoostData(
                id = "instant_vault",
                title = "Dubai 24K Vault Burst",
                description = "Instantly gain +1,000,000 Diamonds!",
                durationSec = 0,
                costDiamonds = 0L, // Free bonus or unlockable
                multiplier = 1_000_000.0,
                type = BoostKind.INSTANT_VAULT
            )
        )

        val initialCollections = listOf(
            CollectionItem("trophy_1", "Silver Crown", "White Cat Mascot", "👑", "Reach 1,000,000 Diamonds", true),
            CollectionItem("trophy_2", "Rainbow Tiara", "Black Cat Mascot", "💎", "Purchase 5 Upgrades", true),
            CollectionItem("trophy_3", "Molten Gold Cone", "Dubai 24K Cone", "🍦", "Reach 5,000,000 Diamonds", true),
            CollectionItem("trophy_4", "Prismatic Star", "Celestial Hologram", "✨", "Activate 3 Boosts", false),
            CollectionItem("trophy_5", "Chalice of Kings", "Grand Prestige Trophy", "🏆", "Reach 10,000,000 Diamonds", false)
        )

        val savedDiamonds = prefs.getFloat("diamonds", 2_847_392f).toDouble()
        val savedClicks = prefs.getLong("totalClicks", 0L)
        val soundOn = prefs.getBoolean("sound_enabled", true)
        val hapticOn = prefs.getBoolean("haptics_enabled", true)
        audio.isSoundEnabled = soundOn
        audio.isHapticsEnabled = hapticOn

        val themeName = prefs.getString("theme", BackgroundTheme.PURPLE_GALAXY.name) ?: BackgroundTheme.PURPLE_GALAXY.name
        val activeTheme = try { BackgroundTheme.valueOf(themeName) } catch (_: Exception) { BackgroundTheme.PURPLE_GALAXY }

        val btnStyleName = prefs.getString("btn_style", ButtonStyle.HOLO_CHROME.name) ?: ButtonStyle.HOLO_CHROME.name
        val buttonStyle = try { ButtonStyle.valueOf(btnStyleName) } catch (_: Exception) { ButtonStyle.HOLO_CHROME }

        // Compute Base DPS and Tap Value from upgrades
        var baseDps = 1241.0
        var baseTap = 100.0
        initialUpgrades.forEach { u ->
            baseDps += u.level * u.dpsBonus
            baseTap += u.level * u.tapBonus
        }

        // Offline earnings calculation
        val lastSaveTime = prefs.getLong("last_save_time", 0L)
        val currentTime = System.currentTimeMillis()
        var offlineEarned = 482_103L
        var offlineSec = 372L // 6m 12s

        if (!isFirstLaunch && lastSaveTime > 0L) {
            val diffSec = ((currentTime - lastSaveTime) / 1000L).coerceIn(5L, 86400L * 3) // up to 3 days
            offlineSec = diffSec
            offlineEarned = (diffSec * baseDps).toLong()
        }

        val minutes = offlineSec / 60
        val seconds = offlineSec % 60
        val durationStr = "${minutes}m ${seconds}s"

        _uiState.value = GameUiState(
            diamonds = if (isFirstLaunch) 2_847_392.0 else savedDiamonds,
            diamondsPerSec = baseDps,
            tapValue = baseTap,
            offlineEarnings = offlineEarned,
            offlineDurationText = durationStr,
            showOfflineDialog = !isFirstLaunch && offlineEarned > 1000L,
            upgrades = initialUpgrades,
            boosts = initialBoosts,
            collections = initialCollections,
            isSoundEnabled = soundOn,
            isHapticsEnabled = hapticOn,
            totalClicks = savedClicks,
            activeTheme = activeTheme,
            buttonStyle = buttonStyle
        )
    }

    private fun startIdleLoop() {
        viewModelScope.launch {
            while (true) {
                delay(100) // 10 ticks per second for ultra smooth progression
                val currentState = _uiState.value
                val increment = (currentState.diamondsPerSec * currentState.activeIdleMultiplier) / 10.0

                _uiState.update { it.copy(diamonds = it.diamonds + increment) }

                // Auto-clicker if active
                if (currentState.isAutoClicking) {
                    val autoTapInc = (currentState.tapValue * currentState.activeTapMultiplier) * 1.2
                    _uiState.update { it.copy(diamonds = it.diamonds + autoTapInc) }
                }
            }
        }
    }

    private fun startBoostTimerLoop() {
        viewModelScope.launch {
            var saveCounter = 0
            while (true) {
                delay(1000)
                saveCounter++
                if (saveCounter >= 5) {
                    saveCounter = 0
                    saveGameToDisk()
                }

                _uiState.update { state ->
                    var newTapMult = 1.0
                    var newIdleMult = 1.0
                    var newAutoClick = false

                    val updatedBoosts = state.boosts.map { boost ->
                        if (boost.isActive && boost.remainingSeconds > 0) {
                            val newRemaining = boost.remainingSeconds - 1
                            val stillActive = newRemaining > 0

                            if (stillActive) {
                                when (boost.type) {
                                    BoostKind.TAP_MULTIPLIER -> newTapMult = boost.multiplier
                                    BoostKind.IDLE_MULTIPLIER -> newIdleMult = boost.multiplier
                                    BoostKind.AUTO_CLICK -> newAutoClick = true
                                    BoostKind.INSTANT_VAULT -> {}
                                }
                            }
                            boost.copy(remainingSeconds = newRemaining, isActive = stillActive)
                        } else {
                            boost
                        }
                    }

                    state.copy(
                        boosts = updatedBoosts,
                        activeTapMultiplier = newTapMult,
                        activeIdleMultiplier = newIdleMult,
                        isAutoClicking = newAutoClick
                    )
                }
            }
        }
    }

    fun onIceCreamTapped(clickX: Float, clickY: Float) {
        val state = _uiState.value
        val tapGain = state.tapValue * state.activeTapMultiplier
        val isCrit = Random.nextFloat() < 0.15f
        val finalGain = if (isCrit) tapGain * 2.0 else tapGain

        audio.playTapDing()
        audio.vibrateTap()

        val particle = TapParticle(
            id = System.currentTimeMillis() + Random.nextLong(1000),
            x = clickX,
            y = clickY,
            valueText = if (isCrit) "CRIT +${finalGain.toLong()} 💎" else "+${finalGain.toLong()}",
            isGold = isCrit || state.activeTapMultiplier > 1.0
        )

        _uiState.update {
            it.copy(
                diamonds = it.diamonds + finalGain,
                totalClicks = it.totalClicks + 1,
                particles = (it.particles + particle).takeLast(12)
            )
        }
    }

    fun removeParticle(id: Long) {
        _uiState.update { it.copy(particles = it.particles.filterNot { p -> p.id == id }) }
    }

    fun buyUpgrade(upgradeId: String) {
        val state = _uiState.value
        val upgrade = state.upgrades.find { it.id == upgradeId } ?: return

        if (state.diamonds >= upgrade.currentCost) {
            audio.playUpgradeSparkle()
            audio.vibratePurchase()

            val newDiamonds = state.diamonds - upgrade.currentCost
            val newLevel = upgrade.level + 1

            val updatedUpgrades = state.upgrades.map {
                if (it.id == upgradeId) it.copy(level = newLevel) else it
            }

            var newDps = 1241.0
            var newTap = 100.0
            updatedUpgrades.forEach {
                newDps += it.level * it.dpsBonus
                newTap += it.level * it.tapBonus
            }

            _uiState.update {
                it.copy(
                    diamonds = newDiamonds,
                    diamondsPerSec = newDps,
                    tapValue = newTap,
                    upgrades = updatedUpgrades
                )
            }

            prefs.edit().putInt("upgrade_$upgradeId", newLevel).apply()
            saveGameToDisk()
        }
    }

    fun activateBoost(boostId: String) {
        val state = _uiState.value
        val boost = state.boosts.find { it.id == boostId } ?: return

        if (boost.costDiamonds > 0 && state.diamonds < boost.costDiamonds) return

        audio.playBoostFanfare()
        audio.vibratePurchase()

        var newDiamonds = state.diamonds
        if (boost.costDiamonds > 0) {
            newDiamonds -= boost.costDiamonds
        }

        if (boost.type == BoostKind.INSTANT_VAULT) {
            newDiamonds += boost.multiplier
            _uiState.update { it.copy(diamonds = newDiamonds) }
            return
        }

        _uiState.update { s ->
            val newBoosts = s.boosts.map {
                if (it.id == boostId) {
                    it.copy(
                        isActive = true,
                        remainingSeconds = it.durationSec
                    )
                } else it
            }
            s.copy(diamonds = newDiamonds, boosts = newBoosts)
        }
    }

    fun claimOfflineEarnings() {
        val state = _uiState.value
        audio.playBoostFanfare()
        audio.vibratePurchase()
        _uiState.update {
            it.copy(
                diamonds = it.diamonds + it.offlineEarnings,
                showOfflineDialog = false
            )
        }
        saveGameToDisk()
    }

    fun dismissOfflineEarnings() {
        _uiState.update { it.copy(showOfflineDialog = false) }
    }

    fun selectTab(tab: ScreenTab) {
        _uiState.update { it.copy(activeTab = tab) }
    }

    fun setBackgroundTheme(theme: BackgroundTheme) {
        _uiState.update { it.copy(activeTheme = theme) }
        prefs.edit().putString("theme", theme.name).apply()
    }

    fun setButtonStyle(style: ButtonStyle) {
        _uiState.update { it.copy(buttonStyle = style) }
        prefs.edit().putString("btn_style", style.name).apply()
    }

    fun toggleSound() {
        val newValue = !_uiState.value.isSoundEnabled
        audio.isSoundEnabled = newValue
        _uiState.update { it.copy(isSoundEnabled = newValue) }
        prefs.edit().putBoolean("sound_enabled", newValue).apply()
    }

    fun toggleHaptics() {
        val newValue = !_uiState.value.isHapticsEnabled
        audio.isHapticsEnabled = newValue
        _uiState.update { it.copy(isHapticsEnabled = newValue) }
        prefs.edit().putBoolean("haptics_enabled", newValue).apply()
    }

    fun toggleSettingsDialog(show: Boolean) {
        _uiState.update { it.copy(showSettingsDialog = show) }
    }

    fun toggleInfoDialog(show: Boolean) {
        _uiState.update { it.copy(showInfoDialog = show) }
    }

    fun resetProgress() {
        prefs.edit().clear().apply()
        loadSavedGame()
    }

    private fun saveGameToDisk() {
        val state = _uiState.value
        prefs.edit()
            .putFloat("diamonds", state.diamonds.toFloat())
            .putLong("totalClicks", state.totalClicks)
            .putLong("last_save_time", System.currentTimeMillis())
            .apply()
    }

    override fun onCleared() {
        super.onCleared()
        saveGameToDisk()
    }
}
