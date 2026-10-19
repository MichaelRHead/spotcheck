package com.example.domain

import com.example.data.local.BadgeEntity
import com.example.data.local.SolvedCaseEntity
import com.example.data.local.UserStatsEntity
import com.example.data.model.Badge
import com.example.data.model.BadgeCategory
import com.example.data.model.BadgeTier
import com.example.data.model.CaseCategory
import com.example.data.model.CaseVerdict
import com.example.data.model.FactCheckResult
import com.example.data.repository.ScenariosData

data class RankTierInfo(
    val rankTitle: String,
    val minXp: Int,
    val nextRankXp: Int,
    val rankIcon: String,
    val perkDescription: String
)

data class ProgressEvaluationResult(
    val updatedStats: UserStatsEntity,
    val newlyUnlockedBadges: List<Badge>,
    val allBadges: List<Badge>,
    val hasLeveledUp: Boolean,
    val currentRank: RankTierInfo,
    val streakBonusXp: Int
)

interface ProgressTrackerService {
    /**
     * Returns the master catalog of all available badges in the system.
     */
    fun getMasterBadges(): List<Badge>

    /**
     * Evaluates user progress following a FactCheckResult, updating stats,
     * streaks, XP multipliers, and unlocking newly earned badges.
     */
    fun evaluateProgress(
        latestResult: FactCheckResult,
        solvedCases: List<SolvedCaseEntity>,
        unlockedBadgeEntities: List<BadgeEntity>,
        currentStats: UserStatsEntity
    ): ProgressEvaluationResult

    /**
     * Calculates the user's detective rank and progression tier based on total XP.
     */
    fun calculateRank(totalXp: Int): RankTierInfo

    /**
     * Maps a solved case collection to badges enriched with current progress counts.
     */
    fun computeBadgeProgress(
        solvedCases: List<SolvedCaseEntity>,
        unlockedBadgeEntities: List<BadgeEntity>
    ): List<Badge>
}

class MediaProgressTrackerService : ProgressTrackerService {

    private val masterBadges = listOf(
        Badge(
            id = "badge_first_case",
            title = "First Case Closed",
            description = "Solved your very first media investigation!",
            iconEmoji = "🔍",
            category = BadgeCategory.FIRST_STEPS,
            tier = BadgeTier.BRONZE,
            targetCount = 1,
            xpReward = 50
        ),
        Badge(
            id = "badge_deepfake_spotter",
            title = "Deepfake Decoder",
            description = "Uncovered an AI-generated or manipulated video clip.",
            iconEmoji = "🤖",
            category = BadgeCategory.AI_DETECTION,
            tier = BadgeTier.BRONZE,
            targetCount = 1,
            xpReward = 100
        ),
        Badge(
            id = "badge_deepfake_master",
            title = "AI Forensics Prodigy",
            description = "Successfully analyzed and debunked 2 AI-generated scenarios.",
            iconEmoji = "🧠",
            category = BadgeCategory.AI_DETECTION,
            tier = BadgeTier.SILVER,
            targetCount = 2,
            xpReward = 150
        ),
        Badge(
            id = "badge_context_champ",
            title = "Context Champion",
            description = "Discovered missing timeline facts behind an out-of-context video.",
            iconEmoji = "⏱️",
            category = BadgeCategory.CONTEXT_ANALYSIS,
            tier = BadgeTier.BRONZE,
            targetCount = 1,
            xpReward = 100
        ),
        Badge(
            id = "badge_clickbait_buster",
            title = "Clickbait Buster",
            description = "Resisted emotional shock traps and debunked sensational headlines.",
            iconEmoji = "💥",
            category = BadgeCategory.CLICKBAIT_BUSTING,
            tier = BadgeTier.BRONZE,
            targetCount = 1,
            xpReward = 100
        ),
        Badge(
            id = "badge_source_sleuth",
            title = "Source Sleuth",
            description = "Practiced lateral reading and verified a genuine scientific source.",
            iconEmoji = "🛡️",
            category = BadgeCategory.CREDIBLE_FACTS,
            tier = BadgeTier.BRONZE,
            targetCount = 1,
            xpReward = 100
        ),
        Badge(
            id = "badge_clue_master",
            title = "Clue Master",
            description = "Found all detectable clues in a case before issuing a verdict.",
            iconEmoji = "🏆",
            category = BadgeCategory.INVESTIGATION_MASTERY,
            tier = BadgeTier.SILVER,
            targetCount = 1,
            xpReward = 150
        ),
        Badge(
            id = "badge_eagle_eye",
            title = "Eagle Eye Sleuth",
            description = "Discovered a cumulative total of 6 forensic clues across cases.",
            iconEmoji = "🦅",
            category = BadgeCategory.INVESTIGATION_MASTERY,
            tier = BadgeTier.GOLD,
            targetCount = 6,
            xpReward = 200
        ),
        Badge(
            id = "badge_streak_3",
            title = "Fact-Check Streak",
            description = "Correctly identified 3 media investigations in a row!",
            iconEmoji = "🔥",
            category = BadgeCategory.STREAK,
            tier = BadgeTier.GOLD,
            targetCount = 3,
            xpReward = 250
        ),
        Badge(
            id = "badge_grand_sleuth",
            title = "Grand Master Investigator",
            description = "Accurately solved 4 or more media literacy cases.",
            iconEmoji = "👑",
            category = BadgeCategory.INVESTIGATION_MASTERY,
            tier = BadgeTier.DIAMOND,
            targetCount = 4,
            xpReward = 300
        )
    )

    override fun getMasterBadges(): List<Badge> = masterBadges

    override fun evaluateProgress(
        latestResult: FactCheckResult,
        solvedCases: List<SolvedCaseEntity>,
        unlockedBadgeEntities: List<BadgeEntity>,
        currentStats: UserStatsEntity
    ): ProgressEvaluationResult {
        val unlockedIds = unlockedBadgeEntities.map { it.badgeId }.toSet()
        val allSolved = (solvedCases.filter { it.caseId != latestResult.caseId } +
            SolvedCaseEntity(
                caseId = latestResult.caseId,
                selectedVerdict = latestResult.userVerdict.name,
                isCorrect = latestResult.isUserAssessmentCorrect,
                cluesFoundCount = latestResult.cluesFoundCount,
                totalCluesCount = latestResult.totalCluesCount,
                earnedXp = latestResult.earnedXp,
                timestamp = latestResult.evaluatedAtTimestamp
            ))

        // Calculate Streak
        val newStreak = if (latestResult.isUserAssessmentCorrect) {
            currentStats.currentStreak + 1
        } else {
            0
        }

        // Streak Bonus XP
        val streakBonusXp = if (newStreak >= 2) (newStreak * 15) else 0

        // Calculate Totals
        val totalSolved = allSolved.size
        val totalCorrect = allSolved.count { it.isCorrect }
        val priorTotalXp = currentStats.totalXp
        val newTotalXp = allSolved.sumOf { it.earnedXp } + streakBonusXp

        val priorRank = calculateRank(priorTotalXp)
        val newRank = calculateRank(newTotalXp)
        val hasLeveledUp = newRank.rankTitle != priorRank.rankTitle

        val updatedStats = currentStats.copy(
            totalXp = newTotalXp,
            solvedCount = totalSolved,
            correctCount = totalCorrect,
            currentStreak = newStreak,
            detectiveRank = newRank.rankTitle
        )

        // Evaluate badge requirements
        val badgesWithProgress = computeBadgeProgress(allSolved, unlockedBadgeEntities)
        val newlyUnlocked = mutableListOf<Badge>()

        badgesWithProgress.forEach { badge ->
            if (!unlockedIds.contains(badge.id) && badge.currentCount >= badge.targetCount) {
                newlyUnlocked.add(
                    badge.copy(
                        isUnlocked = true,
                        unlockedAtTimestamp = System.currentTimeMillis()
                    )
                )
            }
        }

        return ProgressEvaluationResult(
            updatedStats = updatedStats,
            newlyUnlockedBadges = newlyUnlocked,
            allBadges = badgesWithProgress,
            hasLeveledUp = hasLeveledUp,
            currentRank = newRank,
            streakBonusXp = streakBonusXp
        )
    }

    override fun computeBadgeProgress(
        solvedCases: List<SolvedCaseEntity>,
        unlockedBadgeEntities: List<BadgeEntity>
    ): List<Badge> {
        val unlockedMap = unlockedBadgeEntities.associateBy({ it.badgeId }, { it.unlockedAt })
        val caseLookup = ScenariosData.sampleCases.associateBy { it.id }

        // Counts
        val totalSolvedCount = solvedCases.size
        val totalCluesFound = solvedCases.sumOf { it.cluesFoundCount }

        var aiCorrectCount = 0
        var contextCorrectCount = 0
        var clickbaitCorrectCount = 0
        var credibleCorrectCount = 0
        var maxCluesInSingleCase = 0

        solvedCases.forEach { solved ->
            val mediaCase = caseLookup[solved.caseId]
            if (mediaCase != null && solved.isCorrect) {
                when (mediaCase.category) {
                    CaseCategory.AI_MANIPULATED -> aiCorrectCount++
                    CaseCategory.OUT_OF_CONTEXT -> contextCorrectCount++
                    CaseCategory.CLICKBAIT -> clickbaitCorrectCount++
                    CaseCategory.CREDIBLE_FACT -> credibleCorrectCount++
                }
            }
            if (solved.cluesFoundCount >= solved.totalCluesCount && solved.totalCluesCount > 0) {
                maxCluesInSingleCase = 1
            }
        }

        // Streak check
        var currentStreakCounter = 0
        var maxStreakAchieved = 0
        solvedCases.sortedBy { it.timestamp }.forEach { s ->
            if (s.isCorrect) {
                currentStreakCounter++
                if (currentStreakCounter > maxStreakAchieved) maxStreakAchieved = currentStreakCounter
            } else {
                currentStreakCounter = 0
            }
        }

        return masterBadges.map { template ->
            val progressCount = when (template.id) {
                "badge_first_case" -> totalSolvedCount
                "badge_deepfake_spotter" -> aiCorrectCount
                "badge_deepfake_master" -> aiCorrectCount
                "badge_context_champ" -> contextCorrectCount
                "badge_clickbait_buster" -> clickbaitCorrectCount
                "badge_source_sleuth" -> credibleCorrectCount
                "badge_clue_master" -> maxCluesInSingleCase
                "badge_eagle_eye" -> totalCluesFound
                "badge_streak_3" -> maxStreakAchieved
                "badge_grand_sleuth" -> solvedCases.count { it.isCorrect }
                else -> 0
            }

            val unlockedAt = unlockedMap[template.id]
            val isUnlocked = unlockedAt != null || progressCount >= template.targetCount

            template.copy(
                currentCount = progressCount.coerceAtMost(template.targetCount),
                isUnlocked = isUnlocked,
                unlockedAtTimestamp = unlockedAt ?: if (isUnlocked) System.currentTimeMillis() else null
            )
        }
    }

    override fun calculateRank(totalXp: Int): RankTierInfo {
        return when {
            totalXp >= 900 -> RankTierInfo(
                rankTitle = "Chief Forensic Fact-Checker",
                minXp = 900,
                nextRankXp = 1500,
                rankIcon = "🎖️",
                perkDescription = "Master of digital forensics and media verification."
            )
            totalXp >= 550 -> RankTierInfo(
                rankTitle = "Master Media Sleuth",
                minXp = 550,
                nextRankXp = 900,
                rankIcon = "⭐",
                perkDescription = "Unlocks advanced radar scanner and lateral reading tools."
            )
            totalXp >= 300 -> RankTierInfo(
                rankTitle = "Senior Fact Detective",
                minXp = 300,
                nextRankXp = 550,
                rankIcon = "🔎",
                perkDescription = "Earns 1.25x streak bonus XP for consecutive correct verdicts."
            )
            totalXp >= 120 -> RankTierInfo(
                rankTitle = "Junior Investigator",
                minXp = 120,
                nextRankXp = 300,
                rankIcon = "🕵️",
                perkDescription = "Unlocks clue inspection hints and comments analysis."
            )
            else -> RankTierInfo(
                rankTitle = "Rookie Sleuth",
                minXp = 0,
                nextRankXp = 120,
                rankIcon = "🌱",
                perkDescription = "Starting rank for aspiring fact detectives."
            )
        }
    }
}
