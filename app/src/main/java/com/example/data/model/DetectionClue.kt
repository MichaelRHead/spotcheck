package com.example.data.model

enum class ClueType(val displayName: String) {
    VISUAL_ARTIFACT("Visual Artifact"),
    EMOTIONAL_HEADLINE("Shock Headline"),
    SOURCE_ANONYMITY("Unverified Source"),
    DATE_TIMELINE("Mismatched Date"),
    AUDIO_SYNTHETIC("Synthetic Voice / Sound"),
    MISSING_CONTEXT("Missing Crucial Fact")
}

data class DetectionClue(
    val id: String,
    val title: String,
    val type: ClueType,
    val hint: String,
    val explanation: String,
    // Relative coordinates on the video viewport (0.0f to 1.0f)
    val normX: Float,
    val normY: Float,
    val timeSecondTrigger: Int = 2
)

data class SimulatedComment(
    val id: String,
    val author: String,
    val handle: String,
    val text: String,
    val likesCount: String,
    val isSkeptical: Boolean = false,
    val isBotLike: Boolean = false,
    val badge: String? = null
)

data class SiftLesson(
    val stop: String,
    val investigate: String,
    val findBetter: String,
    val traceOriginal: String
)

data class SourceInvestigation(
    val originalCreator: String,
    val accountType: String,
    val creationDate: String,
    val originalUploadDate: String,
    val verificationNotes: String,
    val realContext: String,
    val siftLesson: SiftLesson
)
