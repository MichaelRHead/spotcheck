package com.example.data.repository

import com.example.data.local.BadgeEntity
import com.example.data.local.SolvedCaseEntity
import com.example.data.local.SpotCheckDao
import com.example.data.local.UserStatsEntity
import com.example.data.model.Badge
import com.example.data.model.CaseVerdict
import com.example.data.model.FactCheckResult
import com.example.data.model.MediaCase
import com.example.domain.FactCheckService
import com.example.domain.MediaFactCheckService
import com.example.domain.MediaProgressTrackerService
import com.example.domain.ProgressTrackerService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

data class VerdictSubmissionPayload(
    val factCheckResult: FactCheckResult,
    val newlyUnlockedBadges: List<Badge>,
    val hasLeveledUp: Boolean,
    val currentRankTitle: String,
    val streakBonusXp: Int
)

class MediaDetectiveRepository(
    private val dao: SpotCheckDao,
    val factCheckService: FactCheckService = MediaFactCheckService(),
    val progressTrackerService: ProgressTrackerService = MediaProgressTrackerService()
) {
    fun getAllCases(): List<MediaCase> = ScenariosData.sampleCases

    fun getCaseById(id: String): MediaCase? = ScenariosData.sampleCases.find { it.id == id }

    fun getSolvedCases(): Flow<List<SolvedCaseEntity>> = dao.getAllSolvedCases()

    fun getUserStats(): Flow<UserStatsEntity> = dao.getUserStats().map { entity ->
        entity ?: UserStatsEntity(
            id = 1,
            totalXp = 0,
            solvedCount = 0,
            correctCount = 0,
            currentStreak = 0,
            detectiveRank = "Rookie Sleuth"
        )
    }

    /**
     * Emits the complete list of badges with live progress calculations.
     */
    fun getBadges(): Flow<List<Badge>> {
        return combine(dao.getAllSolvedCases(), dao.getUnlockedBadges()) { solved, unlocked ->
            progressTrackerService.computeBadgeProgress(solved, unlocked)
        }
    }

    suspend fun submitVerdict(
        mediaCase: MediaCase,
        selectedVerdict: CaseVerdict,
        discoveredClues: Set<String>,
        userNotes: String = ""
    ): VerdictSubmissionPayload {
        // 1. Evaluate Fact Check using FactCheckService
        val factResult = factCheckService.evaluateFactCheck(
            mediaCase = mediaCase,
            userVerdict = selectedVerdict,
            discoveredClues = discoveredClues,
            userNotes = userNotes
        )

        // 2. Fetch existing state snapshot for progress evaluation
        val existingSolved = dao.getSolvedCasesSnapshot()
        val existingBadges = dao.getUnlockedBadgesSnapshot()
        val currentStats = dao.getUserStatsSnapshot() ?: UserStatsEntity()

        // 3. Evaluate Badge Awards, Streaks, and Level Progression using ProgressTrackerService
        val progressEvaluation = progressTrackerService.evaluateProgress(
            latestResult = factResult,
            solvedCases = existingSolved,
            unlockedBadgeEntities = existingBadges,
            currentStats = currentStats
        )

        // 4. Save solved case entity to Room
        val solvedEntity = SolvedCaseEntity(
            caseId = mediaCase.id,
            selectedVerdict = selectedVerdict.name,
            isCorrect = factResult.isUserAssessmentCorrect,
            cluesFoundCount = factResult.cluesFoundCount,
            totalCluesCount = factResult.totalCluesCount,
            earnedXp = factResult.earnedXp + progressEvaluation.streakBonusXp,
            timestamp = factResult.evaluatedAtTimestamp,
            notes = userNotes
        )
        dao.insertSolvedCase(solvedEntity)

        // 5. Persist newly unlocked badges
        progressEvaluation.newlyUnlockedBadges.forEach { badge ->
            dao.unlockBadge(BadgeEntity(badge.id, System.currentTimeMillis()))
        }

        // 6. Update user stats in Room
        dao.saveUserStats(progressEvaluation.updatedStats)

        return VerdictSubmissionPayload(
            factCheckResult = factResult,
            newlyUnlockedBadges = progressEvaluation.newlyUnlockedBadges,
            hasLeveledUp = progressEvaluation.hasLeveledUp,
            currentRankTitle = progressEvaluation.currentRank.rankTitle,
            streakBonusXp = progressEvaluation.streakBonusXp
        )
    }

    suspend fun resetAllProgress() {
        dao.clearHistory()
    }
}
