package com.example.data.model

enum class BadgeTier(val displayName: String, val colorHex: Long) {
    BRONZE("Bronze", 0xFFCD7F32),
    SILVER("Silver", 0xFFC0C0C0),
    GOLD("Gold", 0xFFFFD700),
    DIAMOND("Diamond", 0xFF38BDF8)
}

enum class BadgeCategory(val displayName: String) {
    FIRST_STEPS("Getting Started"),
    AI_DETECTION("AI & Deepfakes"),
    CONTEXT_ANALYSIS("Context & Timing"),
    CLICKBAIT_BUSTING("Clickbait & Bias"),
    CREDIBLE_FACTS("Source Literacy"),
    INVESTIGATION_MASTERY("Forensic Mastery"),
    STREAK("Consistency Streak")
}

/**
 * Data model for an awardable Badge gamifying the media literacy experience.
 * Tracks progress requirements, tier level, and unlocked timestamp.
 */
data class Badge(
    val id: String,
    val title: String,
    val description: String,
    val iconEmoji: String,
    val category: BadgeCategory = BadgeCategory.FIRST_STEPS,
    val tier: BadgeTier = BadgeTier.BRONZE,
    val targetCount: Int = 1,
    val currentCount: Int = 0,
    val isUnlocked: Boolean = false,
    val unlockedAtTimestamp: Long? = null,
    val xpReward: Int = 100
) {
    val progressFraction: Float
        get() = if (targetCount <= 0) 1f else (currentCount.toFloat() / targetCount.toFloat()).coerceIn(0f, 1f)

    val percentComplete: Int
        get() = (progressFraction * 100).toInt()
}

// Backward compatibility alias for existing code
typealias DetectiveBadge = Badge
