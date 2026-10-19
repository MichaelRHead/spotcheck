package com.example.data.model

enum class CaseCategory(val label: String, val iconDesc: String) {
    CLICKBAIT("Clickbait & Exaggeration", "Sensational emotion manipulation"),
    OUT_OF_CONTEXT("Out of Context", "Selective cuts or misleading timelines"),
    AI_MANIPULATED("AI & Deepfake", "Digitally altered or synthetic media"),
    CREDIBLE_FACT("Credible Journalism", "Verified sources and factual reporting")
}

enum class CaseVerdict(val label: String, val description: String) {
    ACCURATE_CREDIBLE("Verified Authentic", "Information is accurate, verifiable, and from a credible source."),
    OUT_OF_CONTEXT("Misleading / Out of Context", "Real clip used with false claims, trimmed quotes, or wrong timeline."),
    AI_MANIPULATED("AI Generated / Manipulated", "Video or voice is artificially made or heavily doctored with AI."),
    CLICKBAIT_EXAGGERATED("Clickbait Exaggeration", "Headline or narration grossly exaggerates reality to provoke shock.")
}

enum class DifficultyLevel(val label: String) {
    ROOKIE("Rookie Sleuth"),
    DETECTIVE("Senior Detective"),
    MASTER_FORENSIC("Master Investigator")
}

enum class VideoVisualType {
    AI_MUTANT_ANIMAL,
    DISASTER_OLD_FOOTAGE,
    MIRACLE_CURE_SNACK,
    SPACE_ALIEN_HOAX,
    AUTHENTIC_SCIENCE_SOLAR,
    STAGED_HERO_RESCUE
}
