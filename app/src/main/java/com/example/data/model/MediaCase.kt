package com.example.data.model

data class MediaCase(
    val id: String,
    val title: String,
    val creatorHandle: String,
    val creatorDisplayName: String,
    val isVerifiedCreator: Boolean,
    val followerCount: String,
    val videoDurationSec: Int,
    val caption: String,
    val tags: List<String>,
    val audioTrackTitle: String,
    val category: CaseCategory,
    val difficulty: DifficultyLevel,
    val visualType: VideoVisualType,
    val primarySceneColor: Long,
    val secondarySceneColor: Long,
    val clues: List<DetectionClue>,
    val comments: List<SimulatedComment>,
    val sourceInvestigation: SourceInvestigation,
    val correctVerdict: CaseVerdict,
    val takeawayRule: String,
    val xpReward: Int = 100
)
