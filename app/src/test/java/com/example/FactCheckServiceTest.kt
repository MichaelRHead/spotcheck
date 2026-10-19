package com.example

import com.example.data.model.AssessmentGrade
import com.example.data.model.CaseCategory
import com.example.data.model.CaseVerdict
import com.example.data.repository.ScenariosData
import com.example.domain.MediaFactCheckService
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class FactCheckServiceTest {

    private val service = MediaFactCheckService()

    @Test
    fun `evaluate reliable science article returns positive feedback and high score`() {
        val nasaCase = ScenariosData.sampleCases.first { it.id == "case_authentic_nasa" }
        val allClues = nasaCase.clues.map { it.id }.toSet()

        val result = service.evaluateFactCheck(
            mediaCase = nasaCase,
            userVerdict = CaseVerdict.ACCURATE_CREDIBLE,
            discoveredClues = allClues,
            userNotes = "Verified space agency with peer-reviewed Nature paper."
        )

        assertTrue("Content should be evaluated as reliable", result.isReliable)
        assertTrue("User assessment should be correct", result.isUserAssessmentCorrect)
        assertEquals(CaseVerdict.ACCURATE_CREDIBLE, result.correctVerdict)
        assertTrue("Reliability score should be above 80", result.reliabilityScore >= 80)
        assertEquals(AssessmentGrade.MASTER_SLEUTH, result.accuracyGrade)
        assertTrue("Credibility indicators should not be empty", result.credibilityIndicators.isNotEmpty())
        assertTrue("Feedback message should highlight reliable journalism", result.feedbackMessage.contains("reliable", ignoreCase = true))
    }

    @Test
    fun `evaluate AI misinformation returns low reliability score and flags anomalies`() {
        val aiCatCase = ScenariosData.sampleCases.first { it.id == "case_ai_cat" }
        val allClues = aiCatCase.clues.map { it.id }.toSet()

        val result = service.evaluateFactCheck(
            mediaCase = aiCatCase,
            userVerdict = CaseVerdict.AI_MANIPULATED,
            discoveredClues = allClues,
            userNotes = "Paw had 6 claws and eyes blurred."
        )

        assertFalse("AI Cat should be evaluated as NOT reliable", result.isReliable)
        assertTrue("User assessment should be correct", result.isUserAssessmentCorrect)
        assertTrue("Reliability score should be low for misinformation", result.reliabilityScore < 30)
        assertTrue("Detected red flags should note unverified or AI flaws", result.detectedRedFlags.isNotEmpty())
        assertTrue("Feedback should identify misinformation", result.feedbackMessage.contains("MISINFORMATION", ignoreCase = true))
    }

    @Test
    fun `evaluate user fooled by misinformation returns constructive guidance`() {
        val stormCase = ScenariosData.sampleCases.first { it.id == "case_disaster_cut" }

        val result = service.evaluateFactCheck(
            mediaCase = stormCase,
            userVerdict = CaseVerdict.ACCURATE_CREDIBLE, // User got tricked!
            discoveredClues = emptySet(),
            userNotes = "Looks scary!"
        )

        assertFalse("Storm video is NOT reliable", result.isReliable)
        assertFalse("User got tricked so assessment is incorrect", result.isUserAssessmentCorrect)
        assertEquals(AssessmentGrade.TRICKED, result.accuracyGrade)
        assertTrue("Feedback should explain that user was misled", result.feedbackMessage.contains("misled", ignoreCase = true))
    }

    @Test
    fun `assessArticleReliability correctly flags clickbait and unverified accounts`() {
        val clickbaitAssessment = service.assessArticleReliability(
            title = "SHOCKING: Aliens Found in School Cafeteria! Share before deleted!",
            creatorHandle = "viral_bot_44",
            isVerified = false,
            hasCitations = false,
            hasSensationalTerms = true
        )

        assertFalse("Should be flagged as unreliable", clickbaitAssessment.isLikelyReliable)
        assertTrue("Should have low confidence score", clickbaitAssessment.confidenceScore < 50)
        assertTrue("Should list flagged patterns", clickbaitAssessment.flaggedPatterns.isNotEmpty())

        val credibleAssessment = service.assessArticleReliability(
            title = "James Webb Space Telescope Captures Jupiter Auroral Activity",
            creatorHandle = "NASAWebbOfficial",
            isVerified = true,
            hasCitations = true,
            hasSensationalTerms = false
        )

        assertTrue("Should be evaluated as likely reliable", credibleAssessment.isLikelyReliable)
        assertTrue("Should have high confidence score", credibleAssessment.confidenceScore >= 70)
    }
}
