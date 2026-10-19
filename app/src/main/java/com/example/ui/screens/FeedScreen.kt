package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TravelExplore
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
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
import com.example.ui.viewmodel.SpotCheckUiState
import com.example.ui.viewmodel.SpotCheckViewModel

@Composable
fun FeedScreen(
    viewModel: SpotCheckViewModel,
    uiState: SpotCheckUiState,
    modifier: Modifier = Modifier
) {
    val currentCase = uiState.currentCase ?: return

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
    ) {
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
                    .size(42.dp)
                    .clickable { viewModel.previousCase() }
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
                    .size(42.dp)
                    .clickable { viewModel.nextCase() }
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
            .clickable { onClick() }
            .testTag(testTag)
    ) {
        Surface(
            shape = CircleShape,
            color = if (isActive) activeColor else if (isPulsing) Color(0xFF7C3AED) else Color(0xCC1E293B),
            modifier = Modifier.size(46.dp),
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
