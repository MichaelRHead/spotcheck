package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DetectionClue
import com.example.data.model.MediaCase
import com.example.data.model.VideoVisualType
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SimulatedVideoPlayer(
    mediaCase: MediaCase,
    isPlaying: Boolean,
    progressSec: Float,
    isInspectionMode: Boolean,
    discoveredClueIds: Set<String>,
    onTogglePlay: () -> Unit,
    onClueClicked: (DetectionClue) -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "video_anim")
    val pulseAnim by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )
    val waveAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 6.28f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wave"
    )

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onTogglePlay() }
                )
            }
            .testTag("simulated_video_canvas")
    ) {
        val widthPx = constraints.maxWidth.toFloat()
        val heightPx = constraints.maxHeight.toFloat()

        // Simulated Dynamic Video Scene Canvas
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawSceneBackground(mediaCase, progressSec, waveAnim)
            drawSceneForeground(mediaCase, progressSec, waveAnim, isInspectionMode)
        }

        // Radar Sweep Line when in Inspection Mode
        if (isInspectionMode) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val scanY = (progressSec % 3f / 3f) * size.height
                drawLine(
                    color = Color(0x8806B6D4),
                    start = Offset(0f, scanY),
                    end = Offset(size.width, scanY),
                    strokeWidth = 3.dp.toPx()
                )
            }
        }

        // Forensic Clue Hotspots (visible in Inspection Mode or always accessible by tapping)
        if (isInspectionMode) {
            mediaCase.clues.forEach { clue ->
                val clueDiscovered = clue.id in discoveredClueIds
                val posX = (clue.normX * maxWidth.value).dp
                val posY = (clue.normY * maxHeight.value).dp

                Box(
                    modifier = Modifier
                        .offset(x = posX - 28.dp, y = posY - 28.dp)
                        .size(56.dp)
                        .clickable { onClueClicked(clue) }
                        .testTag("hotspot_${clue.id}"),
                    contentAlignment = Alignment.Center
                ) {
                    // Pulsing Ring
                    Surface(
                        modifier = Modifier
                            .size((40 * pulseAnim).dp),
                        shape = CircleShape,
                        color = if (clueDiscovered) Color(0x3310B981) else Color(0x44F59E0B)
                    ) {}

                    // Target Beacon
                    Surface(
                        modifier = Modifier
                            .size(36.dp)
                            .border(
                                width = 2.dp,
                                color = if (clueDiscovered) Color(0xFF10B981) else Color(0xFFF59E0B),
                                shape = CircleShape
                            ),
                        shape = CircleShape,
                        color = if (clueDiscovered) Color(0xFF065F46) else Color(0xFF78350F),
                        shadowElevation = 6.dp
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = if (clueDiscovered) Icons.Default.Search else Icons.Default.Warning,
                                contentDescription = "Inspect clue: ${clue.title}",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }

        // Paused Indicator Overlay
        if (!isPlaying && !isInspectionMode) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x55000000)),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color(0xAA000000),
                    modifier = Modifier.size(72.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Play video",
                            tint = Color.White,
                            modifier = Modifier.size(44.dp)
                        )
                    }
                }
            }
        }

        // Top Status Bar: Category & Inspection State
        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 48.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xCC0F172A),
                border = BorderStroke(1.dp, Color(0x44FFFFFF))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(
                                color = if (isInspectionMode) Color(0xFF06B6D4) else Color(0xFF10B981),
                                shape = CircleShape
                            )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isInspectionMode) "FORENSIC INSPECTION ACTIVE" else "LIVE FEED SIMULATOR",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Clue Progress Badge
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (discoveredClueIds.size == mediaCase.clues.size) Color(0xCC065F46) else Color(0xCC1E293B),
                border = BorderStroke(1.dp, if (discoveredClueIds.size == mediaCase.clues.size) Color(0xFF10B981) else Color(0x44FFFFFF))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Clues found",
                        tint = if (discoveredClueIds.size == mediaCase.clues.size) Color(0xFF10B981) else Color(0xFF38BDF8),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${discoveredClueIds.size}/${mediaCase.clues.size} Clues",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Bottom Video Timeline & Equalizer
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 90.dp)
        ) {
            val progressFraction = (progressSec / mediaCase.videoDurationSec.toFloat()).coerceIn(0f, 1f)
            LinearProgressIndicator(
                progress = { progressFraction },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp),
                color = if (isInspectionMode) Color(0xFF06B6D4) else Color(0xFFEC4899),
                trackColor = Color(0x33FFFFFF),
            )
        }
    }
}

private fun DrawScope.drawSceneBackground(
    mediaCase: MediaCase,
    progressSec: Float,
    waveAnim: Float
) {
    val gradBrush = Brush.verticalGradient(
        colors = listOf(
            Color(mediaCase.primarySceneColor),
            Color(0xFF0A0F1D)
        )
    )
    drawRect(brush = gradBrush)

    // Animated particle / starfield / atmospheric lines
    val count = 20
    for (i in 0 until count) {
        val x = (size.width * ((i * 37 + progressSec * 15) % 100) / 100f)
        val y = (size.height * ((i * 59 + progressSec * 10) % 100) / 100f)
        val rad = (2 + (i % 3)).dp.toPx()
        drawCircle(
            color = Color(0x33FFFFFF),
            radius = rad,
            center = Offset(x, y)
        )
    }
}

private fun DrawScope.drawSceneForeground(
    mediaCase: MediaCase,
    progressSec: Float,
    waveAnim: Float,
    isInspectionMode: Boolean
) {
    val cx = size.width / 2f
    val cy = size.height / 2.2f

    when (mediaCase.visualType) {
        VideoVisualType.AI_MUTANT_ANIMAL -> {
            // Stylized glowing cat body
            val catColor = Color(0xFF8B5CF6)
            drawOval(
                color = catColor,
                topLeft = Offset(cx - 100.dp.toPx(), cy - 20.dp.toPx()),
                size = Size(200.dp.toPx(), 130.dp.toPx())
            )
            // Cat Head
            drawCircle(
                color = catColor,
                radius = 60.dp.toPx(),
                center = Offset(cx, cy - 80.dp.toPx())
            )
            // Cat Ears
            val earPath1 = Path().apply {
                moveTo(cx - 50.dp.toPx(), cy - 120.dp.toPx())
                lineTo(cx - 30.dp.toPx(), cy - 170.dp.toPx())
                lineTo(cx - 10.dp.toPx(), cy - 125.dp.toPx())
                close()
            }
            drawPath(earPath1, Color(0xFFA78BFA))
            val earPath2 = Path().apply {
                moveTo(cx + 10.dp.toPx(), cy - 125.dp.toPx())
                lineTo(cx + 30.dp.toPx(), cy - 170.dp.toPx())
                lineTo(cx + 50.dp.toPx(), cy - 120.dp.toPx())
                close()
            }
            drawPath(earPath2, Color(0xFFA78BFA))

            // Cat Eyes (Glowing Neon cyan)
            val blink = (sin(progressSec * 2.5) > 0.8)
            drawOval(
                color = Color(0xFF22D3EE),
                topLeft = Offset(cx - 35.dp.toPx(), cy - 90.dp.toPx()),
                size = Size(22.dp.toPx(), if (blink) 3.dp.toPx() else 18.dp.toPx())
            )
            // Right eye (asymmetric / glitchy for the clue!)
            drawOval(
                color = Color(0xFF22D3EE),
                topLeft = Offset(cx + 15.dp.toPx(), cy - 90.dp.toPx()),
                size = Size(22.dp.toPx(), 18.dp.toPx())
            )

            // Multiple Glitched Tails (Telltale AI anomaly!)
            for (t in -1..1) {
                val tailPath = Path().apply {
                    val rootX = cx - 90.dp.toPx()
                    val rootY = cy + 30.dp.toPx()
                    moveTo(rootX, rootY)
                    cubicTo(
                        rootX - 40.dp.toPx(), rootY - (40 + t * 30).dp.toPx() + sin(waveAnim + t) * 15f,
                        rootX - 90.dp.toPx(), rootY - (80 + t * 35).dp.toPx(),
                        rootX - 110.dp.toPx(), rootY - (110 + t * 40).dp.toPx()
                    )
                }
                drawPath(tailPath, Color(0xFFC084FC), style = Stroke(width = 12.dp.toPx()))
            }

            // Weird 6-toed paw area with digital scan glitch
            drawOval(
                color = Color(0xFF7C3AED),
                topLeft = Offset(cx - 70.dp.toPx(), cy + 85.dp.toPx()),
                size = Size(75.dp.toPx(), 45.dp.toPx())
            )
            for (p in 0..5) {
                drawCircle(
                    color = Color(0xFFE879F9),
                    radius = 4.dp.toPx(),
                    center = Offset(cx - 65.dp.toPx() + (p * 11).dp.toPx(), cy + 115.dp.toPx())
                )
            }
        }

        VideoVisualType.DISASTER_OLD_FOOTAGE -> {
            // Storm clouds and rain
            drawRect(
                brush = Brush.verticalGradient(
                    listOf(Color(0xFF334155), Color(0xFF0F172A))
                ),
                topLeft = Offset(0f, cy - 180.dp.toPx()),
                size = Size(size.width, 360.dp.toPx())
            )
            // Rain streaks
            for (r in 0..30) {
                val rx = (r * 29 + progressSec * 120) % size.width
                val ry = (cy - 120.dp.toPx()) + (r * 33 + progressSec * 300) % 280.dp.toPx()
                drawLine(
                    color = Color(0x6693C5FD),
                    start = Offset(rx, ry),
                    end = Offset(rx - 8.dp.toPx(), ry + 20.dp.toPx()),
                    strokeWidth = 2.dp.toPx()
                )
            }
            // 2011 Movie Billboard artifact
            drawRect(
                color = Color(0xFFB45309),
                topLeft = Offset(cx - 140.dp.toPx(), cy - 60.dp.toPx()),
                size = Size(90.dp.toPx(), 60.dp.toPx())
            )
            // Cropped / Blurred news watermark in top right
            drawRect(
                color = Color(0xAA475569),
                topLeft = Offset(size.width - 90.dp.toPx(), cy - 140.dp.toPx()),
                size = Size(70.dp.toPx(), 26.dp.toPx())
            )
        }

        VideoVisualType.MIRACLE_CURE_SNACK -> {
            // Lab background with glowing beverage bottle
            val bottleX = cx - 35.dp.toPx()
            val bottleY = cy - 80.dp.toPx()
            // Bottle Glass
            drawRoundRect(
                color = Color(0x8838BDF8),
                topLeft = Offset(bottleX, bottleY),
                size = Size(70.dp.toPx(), 140.dp.toPx()),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(16f, 16f)
            )
            // Glowing neon liquid
            val liquidHeight = 90.dp.toPx() + sin(waveAnim) * 5f
            drawRoundRect(
                color = Color(0xFF10B981),
                topLeft = Offset(bottleX + 4.dp.toPx(), bottleY + 45.dp.toPx()),
                size = Size(62.dp.toPx(), liquidHeight),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(10f, 10f)
            )
            // Miracle Sparkles
            for (s in 0..4) {
                val sx = cx + (cos(waveAnim + s) * 70).dp.toPx()
                val sy = cy + (sin(waveAnim + s * 1.5f) * 60).dp.toPx()
                drawCircle(color = Color(0xFFFBBF24), radius = 5.dp.toPx(), center = Offset(sx, sy))
            }
            // "100% EXAM HACK" Banner
            drawRect(
                color = Color(0xFFDC2626),
                topLeft = Offset(cx - 120.dp.toPx(), cy + 90.dp.toPx()),
                size = Size(240.dp.toPx(), 36.dp.toPx())
            )
        }

        VideoVisualType.AUTHENTIC_SCIENCE_SOLAR -> {
            // Jupiter sphere
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFFD97706), Color(0xFF78350F), Color(0xFF1E1B4B)),
                    center = Offset(cx, cy),
                    radius = 120.dp.toPx()
                ),
                radius = 110.dp.toPx(),
                center = Offset(cx, cy)
            )
            // Jupiter Great Red Spot & Bands
            drawOval(
                color = Color(0xFFB45309),
                topLeft = Offset(cx - 100.dp.toPx(), cy + 10.dp.toPx()),
                size = Size(200.dp.toPx(), 20.dp.toPx())
            )
            // Ultraviolet Polar Aurora (Glowing Cyan Ring at North Pole)
            drawOval(
                color = Color(0xFF06B6D4),
                topLeft = Offset(cx - 45.dp.toPx(), cy - 105.dp.toPx()),
                size = Size(90.dp.toPx(), 25.dp.toPx()),
                style = Stroke(width = 6.dp.toPx())
            )
            // NASA Telemetry Crosshairs
            drawLine(
                color = Color(0x55FFFFFF),
                start = Offset(cx - 140.dp.toPx(), cy),
                end = Offset(cx + 140.dp.toPx(), cy),
                strokeWidth = 1.dp.toPx()
            )
            drawLine(
                color = Color(0x55FFFFFF),
                start = Offset(cx, cy - 140.dp.toPx()),
                end = Offset(cx, cy + 140.dp.toPx()),
                strokeWidth = 1.dp.toPx()
            )
        }

        VideoVisualType.STAGED_HERO_RESCUE -> {
            // Scenic heritage railway track
            drawLine(
                color = Color(0xFF71717A),
                start = Offset(cx - 150.dp.toPx(), cy + 140.dp.toPx()),
                end = Offset(cx - 20.dp.toPx(), cy - 40.dp.toPx()),
                strokeWidth = 8.dp.toPx()
            )
            drawLine(
                color = Color(0xFF71717A),
                start = Offset(cx + 150.dp.toPx(), cy + 140.dp.toPx()),
                end = Offset(cx + 20.dp.toPx(), cy - 40.dp.toPx()),
                strokeWidth = 8.dp.toPx()
            )
            // Vintage Steam Locomotive silhouette
            drawRect(
                color = Color(0xFF18181B),
                topLeft = Offset(cx - 50.dp.toPx(), cy - 60.dp.toPx()),
                size = Size(100.dp.toPx(), 90.dp.toPx())
            )
            // Steam smoke puff
            drawCircle(
                color = Color(0x66FFFFFF),
                radius = 35.dp.toPx(),
                center = Offset(cx, cy - 90.dp.toPx())
            )
            // Train conductor smiling and waving
            drawCircle(
                color = Color(0xFFFED7AA),
                radius = 12.dp.toPx(),
                center = Offset(cx - 15.dp.toPx(), cy - 35.dp.toPx())
            )
        }

        VideoVisualType.SPACE_ALIEN_HOAX -> {
            // Presidential Podium / Press Conference Stage
            drawRect(
                color = Color(0xFF1E3A8A),
                topLeft = Offset(0f, cy - 160.dp.toPx()),
                size = Size(size.width, 320.dp.toPx())
            )
            // Stage Seal
            drawCircle(
                color = Color(0xFFEAB308),
                radius = 30.dp.toPx(),
                center = Offset(cx, cy - 90.dp.toPx()),
                style = Stroke(width = 3.dp.toPx())
            )
            // Speaker Head
            drawCircle(
                color = Color(0xFFFED7AA),
                radius = 45.dp.toPx(),
                center = Offset(cx, cy - 20.dp.toPx())
            )
            // Podium
            drawRect(
                color = Color(0xFF3F3F46),
                topLeft = Offset(cx - 70.dp.toPx(), cy + 40.dp.toPx()),
                size = Size(140.dp.toPx(), 110.dp.toPx())
            )
            // Deepfake Lip Blur Box
            if (isInspectionMode) {
                drawRect(
                    color = Color(0x88EC4899),
                    topLeft = Offset(cx - 20.dp.toPx(), cy - 5.dp.toPx()),
                    size = Size(40.dp.toPx(), 22.dp.toPx()),
                    style = Stroke(width = 2.dp.toPx())
                )
            }
        }
    }
}
