package com.example

import com.example.data.local.BadgeEntity
import com.example.data.local.SolvedCaseEntity
import com.example.data.local.UserStatsEntity
import com.example.data.model.AssessmentGrade
import com.example.data.model.CaseCategory
import com.example.data.model.CaseVerdict
import com.example.data.model.FactCheckResult
import com.example.domain.MediaProgressTrackerService
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ProgressTrackerServiceTest {

    private val progressService = MediaProgressTrackerService()

    @Test
    fun `master badges catalog contains valid tiered badges`() {
        val badges = progressService.getMasterBadges()
        assertTrue("Badge catalog should contain multiple badges", badges.size >= 8)
        assertTrue("All badges should have non-empty titles and descriptions", badges.all { it.title.isNotBlank() && it.description.isNotBlank() })
        assertTrue("Target counts must be greater than zero", badges.all { it.targetCount > 0 })
    }

    @Test
    fun `evaluateProgress awards first case badge and AI badge on correct AI identification`() {
        val fakeResult = FactCheckResult(
            caseId = "case_ai_cat",
            isReliable = false,
            isUserAssessmentCorrect = true,
            userVerdict = CaseVerdict.AI_MANIPULATED,
            correctVerdict = CaseVerdict.AI_MANIPULATED,
            reliabilityScore = 12,
            feedbackMessage = "Good job!",
            primaryReason = "AI paws glitch",
            detectedRedFlags = listOf("Warped paw"),
            credibilityIndicators = emptyList(),
            actionableSafetyTips = listOf("Check paws"),
            accuracyGrade = AssessmentGrade.MASTER_SLEUTH,
            cluesFoundCount = 3,
            totalCluesCount = 3,
            earnedXp = 175
        )

        val evaluation = progressService.evaluateProgress(
            latestResult = fakeResult,
            solvedCases = emptyList(),
            unlockedBadgeEntities = emptyList(),
            currentStats = UserStatsEntity()
        )

        val newlyUnlockedIds = evaluation.newlyUnlockedBadges.map { it.id }.toSet()
        assertTrue("Should unlock First Case Closed badge", newlyUnlockedIds.contains("badge_first_case"))
        assertTrue("Should unlock Deepfake Decoder badge", newlyUnlockedIds.contains("badge_deepfake_spotter"))
        assertTrue("Should unlock Clue Master badge for finding all 3 clues", newlyUnlockedIds.contains("badge_clue_master"))
        assertEquals("Streak should increment to 1", 1, evaluation.updatedStats.currentStreak)
    }

    @Test
    fun `streak bonus and streak badge are awarded on 3 consecutive correct answers`() {
        val solvedCases = listOf(
            SolvedCaseEntity("case_1", CaseVerdict.AI_MANIPULATED.name, true, 2, 2, 100, 1000L),
            SolvedCaseEntity("case_2", CaseVerdict.OUT_OF_CONTEXT.name, true, 2, 2, 100, 2000L)
        )

        val thirdCorrectResult = FactCheckResult(
            caseId = "case_3",
            isReliable = false,
            isUserAssessmentCorrect = true,
            userVerdict = CaseVerdict.CLICKBAIT_EXAGGERATED,
            correctVerdict = CaseVerdict.CLICKBAIT_EXAGGERATED,
            reliabilityScore = 20,
            feedbackMessage = "Streak continues!",
            primaryReason = "Sensational words",
            detectedRedFlags = listOf("All caps"),
            credibilityIndicators = emptyList(),
            actionableSafetyTips = emptyList(),
            accuracyGrade = AssessmentGrade.SHARP_EYE,
            cluesFoundCount = 2,
            totalCluesCount = 2,
            earnedXp = 150
        )

        val evaluation = progressService.evaluateProgress(
            latestResult = thirdCorrectResult,
            solvedCases = solvedCases,
            unlockedBadgeEntities = listOf(BadgeEntity("badge_first_case", 1000L)),
            currentStats = UserStatsEntity(currentStreak = 2, totalXp = 200)
        )

        assertEquals("Streak should be 3", 3, evaluation.updatedStats.currentStreak)
        assertTrue("Streak bonus XP should be awarded for streak >= 2", evaluation.streakBonusXp > 0)
        assertTrue(
            "Triple streak badge should be unlocked",
            evaluation.newlyUnlockedBadges.any { it.id == "badge_streak_3" }
        )
    }

    @Test
    fun `incorrect verdict resets streak to zero`() {
        val wrongResult = FactCheckResult(
            caseId = "case_disaster_cut",
            isReliable = false,
            isUserAssessmentCorrect = false,
            userVerdict = CaseVerdict.ACCURATE_CREDIBLE,
            correctVerdict = CaseVerdict.OUT_OF_CONTEXT,
            reliabilityScore = 25,
            feedbackMessage = "Tricked by date mismatch",
            primaryReason = "Billboard was old",
            detectedRedFlags = listOf("2011 poster"),
            credibilityIndicators = emptyList(),
            actionableSafetyTips = emptyList(),
            accuracyGrade = AssessmentGrade.TRICKED,
            cluesFoundCount = 1,
            totalCluesCount = 3,
            earnedXp = 60
        )

        val evaluation = progressService.evaluateProgress(
            latestResult = wrongResult,
            solvedCases = emptyList(),
            unlockedBadgeEntities = emptyList(),
            currentStats = UserStatsEntity(currentStreak = 4, totalXp = 400)
        )

        assertEquals("Streak should reset to 0 upon incorrect verdict", 0, evaluation.updatedStats.currentStreak)
        assertEquals(0, evaluation.streakBonusXp)
    }

    @Test
    fun `calculateRank correctly progresses across detective levels`() {
        assertEquals("Rookie Sleuth", progressService.calculateRank(50).rankTitle)
        assertEquals("Junior Investigator", progressService.calculateRank(150).rankTitle)
        assertEquals("Senior Fact Detective", progressService.calculateRank(350).rankTitle)
        assertEquals("Master Media Sleuth", progressService.calculateRank(600).rankTitle)
        assertEquals("Chief Forensic Fact-Checker", progressService.calculateRank(1000).rankTitle)
    }

    @Test
    fun `computeBadgeProgress calculates accurate progress counts and fractions`() {
        val solvedCases = listOf(
            SolvedCaseEntity("case_ai_cat", "AI_MANIPULATED", true, 3, 3, 150, 1000L)
        )

        val badgesWithProgress = progressService.computeBadgeProgress(
            solvedCases = solvedCases,
            unlockedBadgeEntities = emptyList()
        )

        val aiMasterBadge = badgesWithProgress.first { it.id == "badge_deepfake_master" }
        assertEquals("AI Forensics Prodigy needs 2 cases, currently 1", 1, aiMasterBadge.currentCount)
        assertEquals(2, aiMasterBadge.targetCount)
        assertEquals(0.5f, aiMasterBadge.progressFraction, 0.01f)
        assertEquals(50, aiMasterBadge.percentComplete)
        assertFalse("Badge should not be unlocked yet at 50%", aiMasterBadge.isUnlocked)
    }
}
