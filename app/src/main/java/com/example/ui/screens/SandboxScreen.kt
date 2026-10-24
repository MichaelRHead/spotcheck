package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.tvFocusableClick
import com.example.ui.viewmodel.SpotCheckUiState
import com.example.ui.viewmodel.SpotCheckViewModel

@Composable
fun SandboxScreen(
    viewModel: SpotCheckViewModel,
    uiState: SpotCheckUiState,
    modifier: Modifier = Modifier
) {
    // Calculate simulated threat score
    var threatScore = 15
    if (uiState.sandboxIsClickbait) threatScore += 35
    if (!uiState.sandboxHasSource) threatScore += 30
    if (uiState.sandboxHasAiGlitch) threatScore += 20
    threatScore = threatScore.coerceIn(0, 100)

    val threatLevel = when {
        threatScore >= 75 -> "HIGH MISINFORMATION DANGER"
        threatScore >= 45 -> "MODERATE SENSATIONALISM"
        else -> "LOW RISK / RELATIVELY CREDIBLE"
    }

    val threatColor = when {
        threatScore >= 75 -> Color(0xFFEF4444)
        threatScore >= 45 -> Color(0xFFF59E0B)
        else -> Color(0xFF10B981)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
            .padding(horizontal = 20.dp, vertical = 40.dp)
            .verticalScroll(rememberScrollState())
            .testTag("sandbox_screen")
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = Color(0xFFD946EF),
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Science,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "EXPERIMENTAL LAB",
                    color = Color(0xFFF0ABFC),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "The Misinformation Lab",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "See how easily online media can be manipulated by changing headlines, altering sources, and adding synthetic elements:",
            color = Color(0xFF94A3B8),
            fontSize = 13.sp,
            lineHeight = 18.sp
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Threat Meter Result Card
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = threatColor,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "ESTIMATED THREAT LEVEL",
                            color = threatColor,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = threatLevel,
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Score bar
                androidx.compose.material3.LinearProgressIndicator(
                    progress = { threatScore / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp),
                    color = threatColor,
                    trackColor = Color(0xFF334155),
                )

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Misinformation Probability: $threatScore%",
                    color = Color(0xFFCBD5E1),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Custom Post Editor
        Text(
            text = "Step 1: Test Video Headline",
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = uiState.sandboxHeadline,
            onValueChange = { viewModel.updateSandboxHeadline(it) },
            label = { Text("Video Headline") },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("sandbox_headline_field"),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFD946EF),
                unfocusedBorderColor = Color(0x44FFFFFF),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Step 2: Manipulation Toggles",
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Toggle 1: Clickbait Sensationalism
        SandboxToggleRow(
            title = "Emotional Clickbait Shock Words",
            subtitle = "Uses words like 'SHOCKING', 'THEY LIED', and panic emojis",
            checked = uiState.sandboxIsClickbait,
            onCheckedChange = { viewModel.toggleSandboxClickbait(it) },
            testTag = "toggle_clickbait"
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Toggle 2: Transparent Verified Source
        SandboxToggleRow(
            title = "Include Accredited Science / News Source",
            subtitle = "Links directly to primary research or recognized journal",
            checked = uiState.sandboxHasSource,
            onCheckedChange = { viewModel.toggleSandboxSource(it) },
            testTag = "toggle_source"
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Toggle 3: AI Glitches
        SandboxToggleRow(
            title = "Simulate Synthetic AI Video Artifacts",
            subtitle = "Warped hands, floating reflections, and synthetic speech",
            checked = uiState.sandboxHasAiGlitch,
            onCheckedChange = { viewModel.toggleSandboxAiGlitch(it) },
            testTag = "toggle_ai"
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Educational Debrief
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = Color(0x22D946EF),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x55D946EF)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "🔬 What Did You Learn?",
                    color = Color(0xFFF5D0FE),
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (threatScore > 50) {
                        "When creators remove credible sources and crank up emotional shock headlines, ordinary viewers can easily mistake fiction for reality. Always verify before sharing!"
                    } else {
                        "When posts clearly cite trustworthy sources and avoid emotional shock words, viewers are equipped to learn real facts."
                    },
                    color = Color(0xFFE2E8F0),
                    fontSize = 12.sp,
                    lineHeight = 17.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
private fun SandboxToggleRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    testTag: String
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
        modifier = Modifier
            .fillMaxWidth()
            .tvFocusableClick(
                focusBorderColor = Color(0xFFD946EF),
                shape = RoundedCornerShape(14.dp),
                onClick = { onCheckedChange(!checked) }
            )
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
                Text(
                    text = subtitle,
                    color = Color(0xFF94A3B8),
                    fontSize = 11.sp,
                    lineHeight = 15.sp
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                modifier = Modifier.testTag(testTag),
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Color(0xFFD946EF),
                    uncheckedThumbColor = Color(0xFF94A3B8),
                    uncheckedTrackColor = Color(0xFF334155)
                )
            )
        }
    }
}
