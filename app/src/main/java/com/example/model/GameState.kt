package com.example.model

enum class ScreenTab {
    HOME,
    BOOSTS,
    COLLECTION,
    SHOP,
    ICON_MAKER,
    BG_EDITOR
}

enum class BackgroundTheme(
    val title: String,
    val primaryBg: Long,
    val secondaryBg: Long,
    val accentColor: Long,
    val description: String
) {
    PURPLE_GALAXY("Purple Galaxy", 0xFF1A0033, 0xFF0D001A, 0xFF9933FF, "Deep cosmic nebula with stars and aurora"),
    RAINBOW_AURORA("Rainbow Aurora", 0xFF10002B, 0xFF240046, 0xFFFF00AA, "Shimmering rainbow aurora sky"),
    DUBAI_GOLD("Dubai 24K Gold", 0xFF1F1604, 0xFF000000, 0xFFFFD700, "Ultra luxury black & molten gold glitter"),
    PASTEL_GLITTER("Pastel Dream", 0xFF2A1B3D, 0xFF44318D, 0xFFE0AAFF, "Soft pastel lavender with diamond dust"),
    CRYSTAL_PRISM("Crystal Prism", 0xFF0A1128, 0xFF001F54, 0xFF00F5D4, "Deep prismatic gemstone reflections")
}

enum class ButtonStyle(val title: String) {
    HOLO_CHROME("Holo Chrome"),
    GOLD_METALLIC("24K Gold"),
    PASTEL_GLITTER("Pastel Sparkle"),
    NEON_PRISM("Neon Prism")
}

data class UpgradeData(
    val id: String,
    val title: String,
    val subtitle: String,
    val iconName: String,
    val baseCost: Long,
    val costMultiplier: Double = 1.35,
    val level: Int = 0,
    val dpsBonus: Long,
    val tapBonus: Long
) {
    val currentCost: Long
        get() = (baseCost * Math.pow(costMultiplier, level.toDouble())).toLong()
}

data class BoostData(
    val id: String,
    val title: String,
    val description: String,
    val durationSec: Int,
    val costDiamonds: Long,
    val multiplier: Double,
    val type: BoostKind,
    var remainingSeconds: Int = 0,
    var isActive: Boolean = false
)

enum class BoostKind {
    TAP_MULTIPLIER,
    IDLE_MULTIPLIER,
    AUTO_CLICK,
    INSTANT_VAULT
}

data class CollectionItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val icon: String,
    val requirementText: String,
    val isUnlocked: Boolean = false
)

data class TapParticle(
    val id: Long,
    val x: Float,
    val y: Float,
    val valueText: String,
    val isGold: Boolean = false
)
