package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "solved_cases")
data class SolvedCaseEntity(
    @PrimaryKey val caseId: String,
    val selectedVerdict: String,
    val isCorrect: Boolean,
    val cluesFoundCount: Int,
    val totalCluesCount: Int,
    val earnedXp: Int,
    val timestamp: Long,
    val notes: String = ""
)

@Entity(tableName = "user_stats")
data class UserStatsEntity(
    @PrimaryKey val id: Int = 1,
    val totalXp: Int = 0,
    val solvedCount: Int = 0,
    val correctCount: Int = 0,
    val currentStreak: Int = 0,
    val detectiveRank: String = "Rookie Sleuth"
)

@Entity(tableName = "unlocked_badges")
data class BadgeEntity(
    @PrimaryKey val badgeId: String,
    val unlockedAt: Long
)
