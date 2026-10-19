package com.example.data.model

/**
 * Detailed outcome of an evaluated media literacy check on a feed article/video.
 * Provides clear feedback on whether the content is reliable or misinformation,
 * along with educational breakdown of red flags, credibility markers, and safety advice.
 */
data class FactCheckResult(
    val caseId: String,
    val isReliable: Boolean,
    val isUserAssessmentCorrect: Boolean,
    val userVerdict: CaseVerdict,
    val correctVerdict: CaseVerdict,
    val reliabilityScore: Int, // 0 (Definite Misinformation) to 100 (Fully Verified & Credible)
    val feedbackMessage: String,
    val primaryReason: String,
    val detectedRedFlags: List<String>,
    val credibilityIndicators: List<String>,
    val actionableSafetyTips: List<String>,
    val accuracyGrade: AssessmentGrade,
    val cluesFoundCount: Int,
    val totalCluesCount: Int,
    val earnedXp: Int,
    val evaluatedAtTimestamp: Long = System.currentTimeMillis()
)

enum class AssessmentGrade(val label: String, val badgeColor: Long) {
    MASTER_SLEUTH("Master Sleuth", 0xFF10B981),
    SHARP_EYE("Sharp Eye", 0xFF06B6D4),
    CLOSE_CALL("Close Call", 0xFFF59E0B),
    TRICKED("Fooled by the Trap", 0xFFEF4444)
}

/**
 * Types of live feed interactions processed by the evaluation service.
 */
sealed class UserFeedInteraction {
    data class ClueInspected(val clueId: String, val timestampSec: Float) : UserFeedInteraction()
    data class SourceChecked(val creatorHandle: String) : UserFeedInteraction()
    data class CommentsExamined(val skepticalCommentsViewed: Int) : UserFeedInteraction()
    data class VerdictSubmitted(val selectedVerdict: CaseVerdict, val userNotes: String) : UserFeedInteraction()
}

/**
 * Quick assessment for standalone articles or headlines.
 */
data class ReliabilityAssessment(
    val headline: String,
    val isLikelyReliable: Boolean,
    val confidenceScore: Int,
    val riskSummary: String,
    val flaggedPatterns: List<String>
)
