package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Dangerous
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.CaseVerdict
import com.example.data.model.FactCheckResult
import com.example.data.model.MediaCase

@Composable
fun VerdictModal(
    mediaCase: MediaCase,
    submissionResult: FactCheckResult?,
    newlyUnlockedBadges: List<com.example.data.model.Badge> = emptyList(),
    onDismiss: () -> Unit,
    onSubmitVerdict: (CaseVerdict, String) -> Unit,
    onNextCase: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedVerdict by remember { mutableStateOf<CaseVerdict?>(null) }
    var detectiveNotes by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .testTag("verdict_modal_card"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
            elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // If not yet submitted, show the verdict choices
                if (submissionResult == null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFF7C3AED),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "OFFICIAL FACT VERDICT",
                                color = Color(0xFFA78BFA),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "Is This Article Reliable?",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.testTag("close_verdict_dialog")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close dialog",
                                tint = Color(0xFF94A3B8)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Based on your investigation, classify this content to receive a full reliability assessment:",
                        color = Color(0xFFCBD5E1),
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Verdict Choice Cards
                    VerdictOptionCard(
                        title = "Verified Authentic (Reliable)",
                        subtitle = "Credible journalism, factual evidence, and verified institutions.",
                        icon = Icons.Default.CheckCircle,
                        accentColor = Color(0xFF10B981),
                        isSelected = selectedVerdict == CaseVerdict.ACCURATE_CREDIBLE,
                        onClick = { selectedVerdict = CaseVerdict.ACCURATE_CREDIBLE },
                        testTag = "verdict_authentic"
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    VerdictOptionCard(
                        title = "Misleading / Out of Context (Misinformation)",
                        subtitle = "Real footage repurposed with false dates, altered timeline, or cut quotes.",
                        icon = Icons.Default.Timer,
                        accentColor = Color(0xFFF59E0B),
                        isSelected = selectedVerdict == CaseVerdict.OUT_OF_CONTEXT,
                        onClick = { selectedVerdict = CaseVerdict.OUT_OF_CONTEXT },
                        testTag = "verdict_out_of_context"
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    VerdictOptionCard(
                        title = "AI Generated / Manipulated (Misinformation)",
                        subtitle = "Deepfake video, synthetic voice clone, or visual generative glitch.",
                        icon = Icons.Default.SmartToy,
                        accentColor = Color(0xFF8B5CF6),
                        isSelected = selectedVerdict == CaseVerdict.AI_MANIPULATED,
                        onClick = { selectedVerdict = CaseVerdict.AI_MANIPULATED },
                        testTag = "verdict_ai"
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    VerdictOptionCard(
                        title = "Clickbait Exaggeration (Misinformation)",
                        subtitle = "Gross emotional exaggeration or hidden sales pitch to provoke clicks.",
                        icon = Icons.Default.Warning,
                        accentColor = Color(0xFFEF4444),
                        isSelected = selectedVerdict == CaseVerdict.CLICKBAIT_EXAGGERATED,
                        onClick = { selectedVerdict = CaseVerdict.CLICKBAIT_EXAGGERATED },
                        testTag = "verdict_clickbait"
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Optional Notes Field
                    OutlinedTextField(
                        value = detectiveNotes,
                        onValueChange = { detectiveNotes = it },
                        label = { Text("What gave it away? (Optional Note)") },
                        placeholder = { Text("e.g. The paws glitched, or the movie poster was from 2011...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("detective_notes_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF06B6D4),
                            unfocusedBorderColor = Color(0x44FFFFFF),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            selectedVerdict?.let { onSubmitVerdict(it, detectiveNotes) }
                        },
                        enabled = selectedVerdict != null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("submit_verdict_button"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF7C3AED),
                            disabledContainerColor = Color(0xFF334155)
                        )
                    ) {
                        Text(
                            text = "Submit Official Verdict",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color.White
                        )
                    }
                } else {
                    // Post-Verdict Celebration & FactCheckResult Debrief
                    FactCheckFeedbackDebrief(
                        result = submissionResult,
                        mediaCase = mediaCase,
                        newlyUnlockedBadges = newlyUnlockedBadges,
                        onNextCase = onNextCase,
                        onClose = onDismiss
                    )
                }
            }
        }
    }
}

@Composable
private fun VerdictOptionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = if (isSelected) accentColor.copy(alpha = 0.15f) else Color(0xFF0F172A),
        border = androidx.compose.foundation.BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) accentColor else Color(0x22FFFFFF)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .tvFocusableClick(
                focusBorderColor = accentColor,
                shape = RoundedCornerShape(14.dp),
                onClick = onClick
            )
            .testTag(testTag)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = accentColor.copy(alpha = 0.2f),
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = if (isSelected) accentColor else Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Text(
                    text = subtitle,
                    color = Color(0xFF94A3B8),
                    fontSize = 11.sp,
                    lineHeight = 15.sp
                )
            }
        }
    }
}

@Composable
private fun FactCheckFeedbackDebrief(
    result: FactCheckResult,
    mediaCase: MediaCase,
    newlyUnlockedBadges: List<com.example.data.model.Badge> = emptyList(),
    onNextCase: () -> Unit,
    onClose: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Status Badge Header
        Surface(
            shape = CircleShape,
            color = if (result.isUserAssessmentCorrect) Color(0xFF065F46) else Color(0xFF78350F),
            modifier = Modifier.size(64.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = if (result.isUserAssessmentCorrect) Icons.Default.CheckCircle else Icons.Default.HelpOutline,
                    contentDescription = null,
                    tint = if (result.isUserAssessmentCorrect) Color(0xFF34D399) else Color(0xFFFBBF24),
                    modifier = Modifier.size(36.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Newly Awarded Badges Celebration
        if (newlyUnlockedBadges.isNotEmpty()) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFF065F46),
                border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFF34D399)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "🎉 NEW BADGE EARNED!",
                        color = Color(0xFF34D399),
                        fontWeight = FontWeight.Black,
                        fontSize = 13.sp,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    newlyUnlockedBadges.forEach { badge ->
                        Text(
                            text = "${badge.iconEmoji} ${badge.title} (+${badge.xpReward} XP)",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Text(
                            text = badge.description,
                            color = Color(0xFFA7F3D0),
                            fontSize = 11.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
        }

        Text(
            text = if (result.isUserAssessmentCorrect) "VERDICT CONFIRMED!" else "CRITICAL LEARNING MOMENT",
            color = if (result.isUserAssessmentCorrect) Color(0xFF34D399) else Color(0xFFFBBF24),
            fontSize = 18.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.sp
        )

        // Grade Pill
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color(result.accuracyGrade.badgeColor).copy(alpha = 0.2f),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(result.accuracyGrade.badgeColor)),
            modifier = Modifier.padding(top = 4.dp)
        ) {
            Text(
                text = "GRADE: ${result.accuracyGrade.label.uppercase()}",
                color = Color(result.accuracyGrade.badgeColor),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Reliability Meter Card
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (result.isReliable) Icons.Default.Shield else Icons.Default.Warning,
                        contentDescription = null,
                        tint = if (result.isReliable) Color(0xFF10B981) else Color(0xFFEF4444),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (result.isReliable) "RELIABLE JOURNALISM" else "MISINFORMATION ALERT",
                        color = if (result.isReliable) Color(0xFF10B981) else Color(0xFFEF4444),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "${result.reliabilityScore}/100 Credibility",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                LinearProgressIndicator(
                    progress = { result.reliabilityScore / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp),
                    color = if (result.isReliable) Color(0xFF10B981) else Color(0xFFEF4444),
                    trackColor = Color(0xFF334155),
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Feedback Message from Service
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color(0x2238BDF8),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x4438BDF8)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = result.feedbackMessage,
                color = Color(0xFFE0F2FE),
                fontSize = 13.sp,
                lineHeight = 18.sp,
                modifier = Modifier.padding(12.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Red Flags / Credibility Factors
        if (result.detectedRedFlags.isNotEmpty()) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "🚨 DETECTED RED FLAGS",
                    color = Color(0xFFEF4444),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                result.detectedRedFlags.forEach { flag ->
                    Text(
                        text = "• $flag",
                        color = Color(0xFFFCA5A5),
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        if (result.credibilityIndicators.isNotEmpty()) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "🛡️ CREDIBILITY INDICATORS",
                    color = Color(0xFF10B981),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                result.credibilityIndicators.forEach { indicator ->
                    Text(
                        text = "• $indicator",
                        color = Color(0xFFA7F3D0),
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        // Actionable Safety Tips
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFF0F172A),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Lightbulb,
                        contentDescription = null,
                        tint = Color(0xFFFBBF24),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "KEY TAKEAWAY",
                        color = Color(0xFFFBBF24),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = result.primaryReason,
                    color = Color(0xFFE2E8F0),
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // XP Breakdown Card
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFF0F172A),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Color(0xFFFBBF24),
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "+${result.earnedXp} DETECTIVE XP",
                        color = Color(0xFFFBBF24),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "${result.cluesFoundCount} of ${result.totalCluesCount} Clues Discovered",
                        color = Color(0xFF94A3B8),
                        fontSize = 11.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = onClose,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("review_video_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF334155))
            ) {
                Text("Review Video", color = Color.White, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.width(10.dp))
            Button(
                onClick = onNextCase,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("next_case_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF06B6D4))
            ) {
                Text("Next Case ➔", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }
    }
}
