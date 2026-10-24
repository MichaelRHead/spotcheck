package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TravelExplore
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MediaCase
import com.example.ui.components.ClueDetailSheet
import com.example.ui.components.CommentsDrawer
import com.example.ui.components.SimulatedVideoPlayer
import com.example.ui.components.SourceCheckSheet
import com.example.ui.components.VerdictModal
import com.example.ui.components.isGoogleTvMode
import com.example.ui.components.tvFocusableClick
import com.example.ui.viewmodel.SpotCheckUiState
import com.example.ui.viewmodel.SpotCheckViewModel

@Composable
fun FeedScreen(
    viewModel: SpotCheckViewModel,
    uiState: SpotCheckUiState,
    modifier: Modifier = Modifier
) {
    val currentCase = uiState.currentCase ?: return
    val isTvDevice = isGoogleTvMode()

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
    ) {
        val isWideTvLayout = isTvDevice || maxWidth >= 720.dp

        if (isWideTvLayout) {
            // 10-Foot Google TV / Widescreen Split-Deck Layout
            TvWidescreenFeedLayout(
                currentCase = currentCase,
                uiState = uiState,
                viewModel = viewModel
            )
        } else {
            // Standard Handheld Vertical Feed Layout
            MobileCompactFeedLayout(
                currentCase = currentCase,
                uiState = uiState,
                viewModel = viewModel
            )
        }

        // Active Clue Detail Card Popup
        if (uiState.inspectedClue != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x66000000))
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                ClueDetailSheet(
                    clue = uiState.inspectedClue!!,
                    onDismiss = { viewModel.dismissClueDetail() }
                )
            }
        }

        // Comments Bottom Sheet
        if (uiState.showCommentsSheet) {
            CommentsDrawer(
                comments = currentCase.comments,
                onDismiss = { viewModel.openCommentsSheet(false) }
            )
        }

        // Source Check Bottom Sheet
        if (uiState.showSourceCheckSheet) {
            SourceCheckSheet(
                mediaCase = currentCase,
                onDismiss = { viewModel.openSourceCheckSheet(false) }
            )
        }

        // Fact Verdict Dialog
        if (uiState.showVerdictDialog) {
            VerdictModal(
                mediaCase = currentCase,
                submissionResult = uiState.latestSubmissionResult,
                newlyUnlockedBadges = uiState.newlyUnlockedBadges,
                onDismiss = { viewModel.openVerdictDialog(false) },
                onSubmitVerdict = { verdict, notes ->
                    viewModel.submitVerdict(verdict, notes)
                },
                onNextCase = {
                    viewModel.dismissResultModal()
                    viewModel.nextCase()
                }
            )
        }
    }
}

@Composable
private fun TvWidescreenFeedLayout(
    currentCase: MediaCase,
    uiState: SpotCheckUiState,
    viewModel: SpotCheckViewModel
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Left Stage: Cinema Video Viewport (56% width)
        Card(
            modifier = Modifier
                .weight(0.56f)
                .fillMaxHeight(),
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.dp, Color(0x33FFFFFF)),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                SimulatedVideoPlayer(
                    mediaCase = currentCase,
                    isPlaying = uiState.isPlaying,
                    progressSec = uiState.videoProgressSec,
                    isInspectionMode = uiState.isInspectionMode,
                    discoveredClueIds = uiState.discoveredClueIds,
                    onTogglePlay = { viewModel.togglePlayback() },
                    onClueClicked = { viewModel.onClueClicked(it) },
                    modifier = Modifier.clip(RoundedCornerShape(24.dp))
                )

                // Bottom-left TV playback toggle pill for quick D-pad control
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xCC0F172A),
                    border = BorderStroke(1.dp, Color(0x44FFFFFF)),
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                        .tvFocusableClick(
                            focusBorderColor = Color(0xFF38BDF8),
                            shape = RoundedCornerShape(12.dp),
                            onClick = { viewModel.togglePlayback() }
                        )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (uiState.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (uiState.isPlaying) "Pause video" else "Play video",
                            tint = Color(0xFF38BDF8),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (uiState.isPlaying) "Pause Stream" else "Resume Stream",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Right Stage: 10-Foot TV Detective Command Deck (44% width)
        Column(
            modifier = Modifier
                .weight(0.44f)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Case Header & Navigation Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFF1E293B),
                    border = BorderStroke(1.dp, Color(0xFF38BDF8))
                ) {
                    Text(
                        text = "CASE #${uiState.currentCaseIndex + 1} OF ${uiState.cases.size}",
                        color = Color(0xFF38BDF8),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFF1E293B),
                        modifier = Modifier
                            .tvFocusableClick(
                                focusBorderColor = Color(0xFF38BDF8),
                                shape = RoundedCornerShape(10.dp),
                                onClick = { viewModel.previousCase() }
                            )
                            .testTag("prev_case_btn")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowUpward,
                                contentDescription = "Previous video case",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Prev", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFF1E293B),
                        modifier = Modifier
                            .tvFocusableClick(
                                focusBorderColor = Color(0xFF38BDF8),
                                shape = RoundedCornerShape(10.dp),
                                onClick = { viewModel.nextCase() }
                            )
                            .testTag("next_case_btn")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Next", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.ArrowDownward,
                                contentDescription = "Next video case",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            // Creator & Broadcast Info Card
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    if (uiState.currentCaseSolved != null) {
                        val solved = uiState.currentCaseSolved!!
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (solved.isCorrect) Color(0xDD065F46) else Color(0xDD9A3412),
                            modifier = Modifier.padding(bottom = 10.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (solved.isCorrect) Icons.Default.CheckCircle else Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (solved.isCorrect) "VERIFIED SOLVED (+${solved.earnedXp} XP)" else "CASE CLOSED",
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = Color(currentCase.primarySceneColor),
                            modifier = Modifier.size(38.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = currentCase.creatorDisplayName.take(1).uppercase(),
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = currentCase.creatorDisplayName,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                                if (currentCase.isVerifiedCreator) {
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(
                                        imageVector = Icons.Default.Verified,
                                        contentDescription = "Verified creator",
                                        tint = Color(0xFF38BDF8),
                                        modifier = Modifier.size(15.dp)
                                    )
                                }
                            }
                            Text(
                                text = "${currentCase.creatorHandle} • ${currentCase.followerCount} followers",
                                color = Color(0xFFCBD5E1),
                                fontSize = 11.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = currentCase.title,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = currentCase.caption,
                        color = Color(0xFFE2E8F0),
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.MusicNote,
                            contentDescription = null,
                            tint = Color(0xFFA78BFA),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = currentCase.audioTrackTitle,
                            color = Color(0xFFA78BFA),
                            fontSize = 11.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            // Primary TV Detective Actions Grid (2x2 D-Pad Focusable Cards)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                TvDeckActionButton(
                    icon = Icons.Default.Search,
                    label = if (uiState.isInspectionMode) "Radar ON" else "Inspect Clues",
                    subtitle = "${uiState.discoveredClueIds.count { id -> currentCase.clues.any { it.id == id } }}/${currentCase.clues.size} Found",
                    isActive = uiState.isInspectionMode,
                    activeColor = Color(0xFF06B6D4),
                    onClick = { viewModel.toggleInspectionMode() },
                    testTag = "inspect_clues_button",
                    modifier = Modifier.weight(1f)
                )

                TvDeckActionButton(
                    icon = Icons.Default.TravelExplore,
                    label = "Source Check",
                    subtitle = "S.I.F.T. Dossier",
                    isActive = uiState.showSourceCheckSheet,
                    activeColor = Color(0xFFF59E0B),
                    onClick = { viewModel.openSourceCheckSheet(true) },
                    testTag = "source_check_button",
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                TvDeckActionButton(
                    icon = Icons.Default.Chat,
                    label = "Comments (${currentCase.comments.size})",
                    subtitle = "Viewer Reactions",
                    isActive = uiState.showCommentsSheet,
                    activeColor = Color(0xFF38BDF8),
                    onClick = { viewModel.openCommentsSheet(true) },
                    testTag = "feed_comments_button",
                    modifier = Modifier.weight(1f)
                )

                TvDeckActionButton(
                    icon = Icons.Default.Gavel,
                    label = "Submit Verdict",
                    subtitle = "Close Case & Earn XP",
                    isActive = true,
                    activeColor = Color(0xFF7C3AED),
                    onClick = { viewModel.openVerdictDialog(true) },
                    testTag = "open_verdict_button",
                    modifier = Modifier.weight(1f)
                )
            }

            // Direct D-Pad Forensic Clue Scanner List (makes inspecting clues on TV effortless)
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "REMOTE CLUE SCANNER (D-PAD SELECT)",
                        color = Color(0xFF2DD4BF),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    currentCase.clues.forEachIndexed { idx, clue ->
                        val discovered = clue.id in uiState.discoveredClueIds
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (discovered) Color(0xFF064E3B) else Color(0xFF0F172A),
                            border = BorderStroke(
                                1.dp,
                                if (discovered) Color(0xFF10B981) else Color(0x33FFFFFF)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .tvFocusableClick(
                                    focusBorderColor = Color(0xFF2DD4BF),
                                    shape = RoundedCornerShape(12.dp),
                                    onClick = {
                                        if (!uiState.isInspectionMode) {
                                            viewModel.toggleInspectionMode()
                                        }
                                        viewModel.onClueClicked(clue)
                                    }
                                )
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (discovered) Icons.Default.CheckCircle else Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = if (discovered) Color(0xFF34D399) else Color(0xFFFBBF24),
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = if (discovered) clue.title else "Anomaly Hotspot #${idx + 1}: Tap OK to Inspect",
                                        color = Color.White,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = clue.type.displayName,
                                        color = Color(0xFF94A3B8),
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TvDeckActionButton(
    icon: ImageVector,
    label: String,
    subtitle: String,
    isActive: Boolean,
    activeColor: Color,
    onClick: () -> Unit,
    testTag: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = if (isActive) activeColor.copy(alpha = 0.22f) else Color(0xFF1E293B),
        border = BorderStroke(
            width = if (isActive) 2.dp else 1.dp,
            color = if (isActive) activeColor else Color(0x33FFFFFF)
        ),
        modifier = modifier
            .tvFocusableClick(
                focusBorderColor = activeColor,
                shape = RoundedCornerShape(14.dp),
                onClick = onClick
            )
            .testTag(testTag)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = if (isActive) activeColor else Color(0xFF0F172A),
                modifier = Modifier.size(36.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        tint = if (isActive && activeColor == Color(0xFF06B6D4)) Color.Black else Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = label,
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = subtitle,
                    color = Color(0xFF94A3B8),
                    fontSize = 10.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun MobileCompactFeedLayout(
    currentCase: MediaCase,
    uiState: SpotCheckUiState,
    viewModel: SpotCheckViewModel
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Simulated Video Canvas Player
        SimulatedVideoPlayer(
            mediaCase = currentCase,
            isPlaying = uiState.isPlaying,
            progressSec = uiState.videoProgressSec,
            isInspectionMode = uiState.isInspectionMode,
            discoveredClueIds = uiState.discoveredClueIds,
            onTogglePlay = { viewModel.togglePlayback() },
            onClueClicked = { viewModel.onClueClicked(it) }
        )

        // Bottom Left Info Overlay (Creator, Title, Caption, Music)
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth(0.78f)
                .padding(start = 16.dp, bottom = 28.dp)
        ) {
            // Solved Badge if user has already solved this case
            if (uiState.currentCaseSolved != null) {
                val solved = uiState.currentCaseSolved!!
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (solved.isCorrect) Color(0xDD065F46) else Color(0xDD9A3412),
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (solved.isCorrect) Icons.Default.CheckCircle else Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (solved.isCorrect) "VERIFIED SOLVED (+${solved.earnedXp} XP)" else "CASE CLOSED",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Creator Row
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color(currentCase.primarySceneColor),
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = currentCase.creatorDisplayName.take(1).uppercase(),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = currentCase.creatorDisplayName,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        if (currentCase.isVerifiedCreator) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = "Verified creator",
                                tint = Color(0xFF38BDF8),
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                    Text(
                        text = "${currentCase.creatorHandle} • ${currentCase.followerCount} followers",
                        color = Color(0xFFCBD5E1),
                        fontSize = 11.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Video Title & Caption
            Text(
                text = currentCase.title,
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = currentCase.caption,
                color = Color(0xFFE2E8F0),
                fontSize = 12.sp,
                lineHeight = 16.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Audio Track Marquee
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.MusicNote,
                    contentDescription = null,
                    tint = Color(0xFFA78BFA),
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = currentCase.audioTrackTitle,
                    color = Color(0xFFA78BFA),
                    fontSize = 11.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        // Right-Side Interactive Detective Actions Toolbar
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 12.dp, bottom = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Next & Prev Case Quick Controls
            Surface(
                shape = CircleShape,
                color = Color(0xAA1E293B),
                modifier = Modifier
                    .size(48.dp)
                    .tvFocusableClick(
                        focusBorderColor = Color(0xFF38BDF8),
                        shape = CircleShape,
                        onClick = { viewModel.previousCase() }
                    )
                    .testTag("prev_case_btn")
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.ArrowUpward,
                        contentDescription = "Previous video case",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Surface(
                shape = CircleShape,
                color = Color(0xAA1E293B),
                modifier = Modifier
                    .size(48.dp)
                    .tvFocusableClick(
                        focusBorderColor = Color(0xFF38BDF8),
                        shape = CircleShape,
                        onClick = { viewModel.nextCase() }
                    )
                    .testTag("next_case_btn")
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.ArrowDownward,
                        contentDescription = "Next video case",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Inspect Clues Action
            FeedActionButton(
                icon = Icons.Default.Search,
                label = if (uiState.isInspectionMode) "Radar ON" else "Inspect",
                isActive = uiState.isInspectionMode,
                activeColor = Color(0xFF06B6D4),
                onClick = { viewModel.toggleInspectionMode() },
                testTag = "inspect_clues_button"
            )

            // Comments Action
            FeedActionButton(
                icon = Icons.Default.Chat,
                label = "${currentCase.comments.size}",
                isActive = uiState.showCommentsSheet,
                activeColor = Color(0xFF38BDF8),
                onClick = { viewModel.openCommentsSheet(true) },
                testTag = "feed_comments_button"
            )

            // Source Check Action
            FeedActionButton(
                icon = Icons.Default.TravelExplore,
                label = "Source",
                isActive = uiState.showSourceCheckSheet,
                activeColor = Color(0xFFF59E0B),
                onClick = { viewModel.openSourceCheckSheet(true) },
                testTag = "source_check_button"
            )

            // Fact Verdict Action
            FeedActionButton(
                icon = Icons.Default.Gavel,
                label = "Verdict",
                isActive = false,
                activeColor = Color(0xFFEC4899),
                isPulsing = true,
                onClick = { viewModel.openVerdictDialog(true) },
                testTag = "open_verdict_button"
            )
        }
    }
}

@Composable
private fun FeedActionButton(
    icon: ImageVector,
    label: String,
    isActive: Boolean,
    activeColor: Color,
    isPulsing: Boolean = false,
    onClick: () -> Unit,
    testTag: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .tvFocusableClick(
                focusBorderColor = activeColor,
                shape = RoundedCornerShape(12.dp),
                onClick = onClick
            )
            .testTag(testTag)
    ) {
        Surface(
            shape = CircleShape,
            color = if (isActive) activeColor else if (isPulsing) Color(0xFF7C3AED) else Color(0xCC1E293B),
            modifier = Modifier.size(48.dp),
            shadowElevation = 6.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = if (isActive && activeColor == Color(0xFF06B6D4)) Color.Black else Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = label,
            color = if (isActive) activeColor else Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
