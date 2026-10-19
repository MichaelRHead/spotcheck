package com.example.domain

import com.example.data.model.AssessmentGrade
import com.example.data.model.CaseCategory
import com.example.data.model.CaseVerdict
import com.example.data.model.FactCheckResult
import com.example.data.model.MediaCase
import com.example.data.model.ReliabilityAssessment
import com.example.data.model.UserFeedInteraction

/**
 * Service responsible for processing user interactions with the media feed
 * and evaluating whether an article or video is reliable or misinformation.
 */
interface FactCheckService {
    /**
     * Evaluates a full fact-check submission by a user on a given feed item.
     * Computes the reliability score, detects red flags or credible markers,
     * and provides personalized educational feedback.
     */
    fun evaluateFactCheck(
        mediaCase: MediaCase,
        userVerdict: CaseVerdict,
        discoveredClues: Set<String>,
        userNotes: String = ""
    ): FactCheckResult

    /**
     * Assesses a standalone headline/article for reliability indicators.
     */
    fun assessArticleReliability(
        title: String,
        creatorHandle: String,
        isVerified: Boolean,
        hasCitations: Boolean,
        hasSensationalTerms: Boolean
    ): ReliabilityAssessment
}

class MediaFactCheckService : FactCheckService {

    override fun evaluateFactCheck(
        mediaCase: MediaCase,
        userVerdict: CaseVerdict,
        discoveredClues: Set<String>,
        userNotes: String
    ): FactCheckResult {
        val isArticleReliable = mediaCase.category == CaseCategory.CREDIBLE_FACT
        val isUserCorrect = userVerdict == mediaCase.correctVerdict
        val totalClues = mediaCase.clues.size
        val cluesFoundCount = mediaCase.clues.count { it.id in discoveredClues }

        // Compute 0-100 Reliability Score for the content
        val reliabilityScore = computeReliabilityScore(mediaCase)

        // Identify specific red flags present in the case
        val redFlags = extractRedFlags(mediaCase)

        // Identify credibility indicators present in the case
        val credibilityIndicators = extractCredibilityIndicators(mediaCase)

        // Calculate XP reward
        var xpEarned = if (isUserCorrect) mediaCase.xpReward else 35
        xpEarned += (cluesFoundCount * 25)

        // Determine grade based on accuracy and clue discovery
        val grade = when {
            isUserCorrect && cluesFoundCount == totalClues -> AssessmentGrade.MASTER_SLEUTH
            isUserCorrect -> AssessmentGrade.SHARP_EYE
            cluesFoundCount > 0 -> AssessmentGrade.CLOSE_CALL
            else -> AssessmentGrade.TRICKED
        }

        // Generate personalized educational feedback message
        val feedback = buildFeedbackMessage(
            isUserCorrect = isUserCorrect,
            isContentReliable = isArticleReliable,
            userVerdict = userVerdict,
            correctVerdict = mediaCase.correctVerdict,
            cluesFound = cluesFoundCount,
            totalClues = totalClues,
            category = mediaCase.category
        )

        // Actionable safety tips
        val safetyTips = listOf(
            "S.I.F.T. Rule: ${mediaCase.sourceInvestigation.siftLesson.stop}",
            "Source Check: ${mediaCase.sourceInvestigation.siftLesson.investigate}",
            "Lateral Search: ${mediaCase.sourceInvestigation.siftLesson.findBetter}",
            "Trace Context: ${mediaCase.sourceInvestigation.siftLesson.traceOriginal}"
        )

        return FactCheckResult(
            caseId = mediaCase.id,
            isReliable = isArticleReliable,
            isUserAssessmentCorrect = isUserCorrect,
            userVerdict = userVerdict,
            correctVerdict = mediaCase.correctVerdict,
            reliabilityScore = reliabilityScore,
            feedbackMessage = feedback,
            primaryReason = mediaCase.takeawayRule,
            detectedRedFlags = redFlags,
            credibilityIndicators = credibilityIndicators,
            actionableSafetyTips = safetyTips,
            accuracyGrade = grade,
            cluesFoundCount = cluesFoundCount,
            totalCluesCount = totalClues,
            earnedXp = xpEarned
        )
    }

    override fun assessArticleReliability(
        title: String,
        creatorHandle: String,
        isVerified: Boolean,
        hasCitations: Boolean,
        hasSensationalTerms: Boolean
    ): ReliabilityAssessment {
        var score = 50
        val flagged = mutableListOf<String>()

        if (hasSensationalTerms || title.contains("!", ignoreCase = true) && title.contains("SHOCKING", ignoreCase = true)) {
            score -= 30
            flagged.add("Sensational shock language in headline")
        }
        if (!isVerified) {
            score -= 15
            flagged.add("Unverified publisher profile")
        }
        if (!hasCitations) {
            score -= 20
            flagged.add("No primary sources or research citations")
        } else {
            score += 25
        }
        if (isVerified) {
            score += 20
        }

        val clampedScore = score.coerceIn(5, 95)
        val isReliable = clampedScore >= 60

        val summary = if (isReliable) {
            "Content demonstrates high credibility with verified sources and objective framing."
        } else {
            "Content displays hallmark misinformation patterns: emotional triggers and lack of source verification."
        }

        return ReliabilityAssessment(
            headline = title,
            isLikelyReliable = isReliable,
            confidenceScore = clampedScore,
            riskSummary = summary,
            flaggedPatterns = flagged
        )
    }

    private fun computeReliabilityScore(mediaCase: MediaCase): Int {
        return when (mediaCase.category) {
            CaseCategory.CREDIBLE_FACT -> 92
            CaseCategory.CLICKBAIT -> 28
            CaseCategory.OUT_OF_CONTEXT -> 22
            CaseCategory.AI_MANIPULATED -> 12
        }
    }

    private fun extractRedFlags(mediaCase: MediaCase): List<String> {
        val flags = mutableListOf<String>()
        if (mediaCase.category != CaseCategory.CREDIBLE_FACT) {
            if (!mediaCase.isVerifiedCreator) {
                flags.add("Unverified publisher profile (@${mediaCase.creatorHandle})")
            }
            when (mediaCase.category) {
                CaseCategory.CLICKBAIT -> {
                    flags.add("Sensational emotional triggers and curiosity gap")
                    flags.add("Miracle promise with zero effort or commercial sales pitch")
                    flags.add("Author lacks verified clinical or professional credentials")
                }
                CaseCategory.OUT_OF_CONTEXT -> {
                    flags.add("Recycled historical footage with mismatched date/location")
                    flags.add("Selective edit cutting away before natural resolution")
                    flags.add("Manipulated dramatic audio track added in post-production")
                }
                CaseCategory.AI_MANIPULATED -> {
                    flags.add("Anatomical or physics glitches in hands/paws/shadows")
                    flags.add("Synthetic robotic cadence and lip-sync mismatch")
                    flags.add("Zero corroborating records in recognized registries")
                }
                else -> {}
            }
        }
        return flags
    }

    private fun extractCredibilityIndicators(mediaCase: MediaCase): List<String> {
        val indicators = mutableListOf<String>()
        if (mediaCase.isVerifiedCreator) {
            indicators.add("Verified institutional credentials (${mediaCase.creatorDisplayName})")
        }
        if (mediaCase.category == CaseCategory.CREDIBLE_FACT) {
            indicators.add("Direct citation of peer-reviewed scientific publications")
            indicators.add("Transparent disclosure of methodology and imaging filters")
            indicators.add("Accountable organization with public institutional oversight")
        }
        return indicators
    }

    private fun buildFeedbackMessage(
        isUserCorrect: Boolean,
        isContentReliable: Boolean,
        userVerdict: CaseVerdict,
        correctVerdict: CaseVerdict,
        cluesFound: Int,
        totalClues: Int,
        category: CaseCategory
    ): String {
        return when {
            isUserCorrect && isContentReliable -> {
                "Spot-on! You recognized that this article is reliable. You noticed the verified credentials and transparent citations, avoiding false skepticism!"
            }
            isUserCorrect -> {
                "Outstanding detection! You identified that this content is MISINFORMATION ($category). By uncovering $cluesFound of $totalClues clues, you saw past the emotional bait."
            }
            !isUserCorrect && isContentReliable -> {
                "Close call! You thought this was ${userVerdict.label}, but it is actually RELIABLE journalism. Remember: real science can sound amazing, but verified sources provide data to back it up."
            }
            else -> {
                "You were misled this time! You classified this as ${userVerdict.label}, but it was actually ${correctVerdict.label}. Don't worry—the creator used subtle tricks to deceive viewers. Review the clues to sharpen your skills!"
            }
        }
    }
}
