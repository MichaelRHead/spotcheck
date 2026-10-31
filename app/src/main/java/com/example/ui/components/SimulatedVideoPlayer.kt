package com.example.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.DetectionClue
import com.example.data.model.MediaCase
import com.example.data.model.VideoVisualType
import kotlin.math.cos
import kotlin.math.sin

@DrawableRes
fun VideoVisualType.toSceneDrawableRes(): Int = when (this) {
    VideoVisualType.AI_MUTANT_ANIMAL -> R.drawable.img_scene_ai_cat
    VideoVisualType.DISASTER_OLD_FOOTAGE -> R.drawable.img_scene_storm_flood
    VideoVisualType.MIRACLE_CURE_SNACK -> R.drawable.img_scene_miracle_drink
    VideoVisualType.AUTHENTIC_SCIENCE_SOLAR -> R.drawable.img_scene_nasa_jupiter
    VideoVisualType.STAGED_HERO_RESCUE -> R.drawable.img_scene_train_rescue
    VideoVisualType.SPACE_ALIEN_HOAX -> R.drawable.img_scene_deepfake_podium
}

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
        initialValue = 0.85f,
        targetValue = 1.32f,
        animationSpec = infiniteRepeatable(
            animation = tween(1100, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )
    val waveAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 6.28318f,
        animationSpec = infiniteRepeatable(
            animation = tween(2600, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wave"
    )

    // Realistic organic handheld & Ken Burns camera motion when playing
    val activeTime = if (isPlaying && !isInspectionMode) progressSec + (waveAnim / 6.28318f) * 0.5f else progressSec
    val cameraZoom = if (isInspectionMode) {
        1.02f
    } else {
        1.04f + 0.035f * sin(activeTime * 0.45f)
    }
    val cameraPanX = if (isInspectionMode) {
        0f
    } else {
        sin(activeTime * 0.9f) * 7f + cos(activeTime * 2.3f) * 1.8f
    }
    val cameraPanY = if (isInspectionMode) {
        0f
    } else {
        cos(activeTime * 0.75f) * 5f + sin(activeTime * 3.1f) * 1.5f
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF070B14))
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onTogglePlay() }
                )
            }
            .testTag("simulated_video_canvas")
    ) {
        // Layer 1: Photorealistic High-Resolution Scene Frame with Cinema Motion
        Image(
            painter = painterResource(id = mediaCase.visualType.toSceneDrawableRes()),
            contentDescription = mediaCase.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = cameraZoom
                    scaleY = cameraZoom
                    translationX = cameraPanX
                    translationY = cameraPanY
                }
        )

        // Layer 2: Real-Time Photorealistic Atmospheric, Lighting, Weather & Telemetry Video FX
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawPhotorealisticVideoEffects(
                mediaCase = mediaCase,
                progressSec = progressSec,
                waveAnim = waveAnim,
                isPlaying = isPlaying,
                isInspectionMode = isInspectionMode
            )
            // Cinema optical vignette & top/bottom gradient protection for UI legibility
            drawRect(
                brush = Brush.verticalGradient(
                    0.0f to Color(0x99030712),
                    0.18f to Color.Transparent,
                    0.72f to Color.Transparent,
                    1.0f to Color(0xCC030712)
                )
            )
        }

        // Layer 3: Forensic Optical Scan Grid & Sweep Line when in Inspection Mode
        if (isInspectionMode) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val gridStep = 44.dp.toPx()
                var gx = 0f
                while (gx < size.width) {
                    drawLine(
                        color = Color(0x1806B6D4),
                        start = Offset(gx, 0f),
                        end = Offset(gx, size.height),
                        strokeWidth = 1f
                    )
                    gx += gridStep
                }
                var gy = 0f
                while (gy < size.height) {
                    drawLine(
                        color = Color(0x1806B6D4),
                        start = Offset(0f, gy),
                        end = Offset(size.width, gy),
                        strokeWidth = 1f
                    )
                    gy += gridStep
                }

                val scanY = ((progressSec * 0.45f + waveAnim / 6.28318f) % 1f) * size.height
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color(0x4406B6D4),
                            Color(0xBB22D3EE)
                        ),
                        startY = (scanY - 36.dp.toPx()).coerceAtLeast(0f),
                        endY = scanY
                    ),
                    topLeft = Offset(0f, (scanY - 36.dp.toPx()).coerceAtLeast(0f)),
                    size = Size(size.width, 36.dp.toPx())
                )
                drawLine(
                    color = Color(0xFF22D3EE),
                    start = Offset(0f, scanY),
                    end = Offset(size.width, scanY),
                    strokeWidth = 2.dp.toPx()
                )
            }
        }

        // Layer 4: Forensic Clue Hotspots (visible in Inspection Mode and D-pad focusable)
        if (isInspectionMode) {
            mediaCase.clues.forEach { clue ->
                val clueDiscovered = clue.id in discoveredClueIds
                val posX = (clue.normX * maxWidth.value).dp
                val posY = (clue.normY * maxHeight.value).dp

                Box(
                    modifier = Modifier
                        .offset(x = posX - 30.dp, y = posY - 30.dp)
                        .size(60.dp)
                        .tvFocusableClick(
                            focusBorderColor = Color.White,
                            shape = CircleShape,
                            scaleOnFocus = 1.18f,
                            onClick = { onClueClicked(clue) }
                        )
                        .testTag("hotspot_${clue.id}"),
                    contentAlignment = Alignment.Center
                ) {
                    // Outer Optical Loupe Reticle Ring
                    Surface(
                        modifier = Modifier
                            .size((44 * pulseAnim).dp)
                            .border(
                                width = 1.5.dp,
                                color = if (clueDiscovered) Color(0xAA10B981) else Color(0xAAF59E0B),
                                shape = CircleShape
                            ),
                        shape = CircleShape,
                        color = if (clueDiscovered) Color(0x2810B981) else Color(0x33F59E0B)
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
                        color = if (clueDiscovered) Color(0xDD065F46) else Color(0xDD78350F),
                        shadowElevation = 8.dp
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = if (clueDiscovered) Icons.Default.Search else Icons.Default.Warning,
                                contentDescription = "Inspect clue: ${clue.title}",
                                tint = Color.White,
                                modifier = Modifier.size(19.dp)
                            )
                        }
                    }
                }
            }
        }

        // Layer 5: Paused Indicator Overlay
        if (!isPlaying && !isInspectionMode) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x44000000)),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color(0xBB0F172A),
                    border = BorderStroke(1.5.dp, Color(0x66FFFFFF)),
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

        // Layer 6: Top Camera Viewfinder & Forensic Status Bar
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
                                color = if (isInspectionMode) Color(0xFF06B6D4) else Color(0xFFEF4444),
                                shape = CircleShape
                            )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    val secInt = progressSec.toInt()
                    val frameInt = ((progressSec - secInt) * 24).toInt().coerceIn(0, 23)
                    val tcText = String.format("00:%02d:%02d", secInt, frameInt)
                    Text(
                        text = if (isInspectionMode) {
                            "FORENSIC ELA SCAN • $tcText"
                        } else {
                            "HD STREAM • $tcText"
                        },
                        color = Color.White,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Clue Progress Badge
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (discoveredClueIds.size == mediaCase.clues.size) Color(0xCC065F46) else Color(0xCC1E293B),
                border = BorderStroke(
                    1.dp,
                    if (discoveredClueIds.size == mediaCase.clues.size) Color(0xFF10B981) else Color(0x44FFFFFF)
                )
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

        // Layer 7: Bottom Video Timeline & Live Audio Telemetry Bar
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
                trackColor = Color(0x44FFFFFF),
            )
        }
    }
}

private fun DrawScope.drawPhotorealisticVideoEffects(
    mediaCase: MediaCase,
    progressSec: Float,
    waveAnim: Float,
    isPlaying: Boolean,
    isInspectionMode: Boolean
) {
    val t = if (isPlaying) progressSec * 2.2f + waveAnim else progressSec * 2.2f

    when (mediaCase.visualType) {
        VideoVisualType.AI_MUTANT_ANIMAL -> {
            // Subtle bioluminescent floating dust motes & neural shimmer around the AI cat
            for (i in 0 until 14) {
                val mx = size.width * (0.20f + ((i * 19 + t * 4f) % 60f) / 100f)
                val my = size.height * (0.25f + ((i * 31 + sin(t + i) * 6f) % 50f) / 100f)
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0x66E879F9), Color.Transparent),
                        center = Offset(mx, my),
                        radius = 12.dp.toPx()
                    ),
                    radius = 12.dp.toPx(),
                    center = Offset(mx, my)
                )
            }
            // Subtle AI eye reflection glint at (0.52, 0.35..0.43)
            val glintAlpha = (0.25f + 0.25f * sin(t * 2.4f)).coerceIn(0.05f, 0.55f)
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFF22D3EE).copy(alpha = glintAlpha), Color.Transparent),
                    center = Offset(size.width * 0.465f, size.height * 0.426f),
                    radius = 22.dp.toPx()
                ),
                radius = 22.dp.toPx(),
                center = Offset(size.width * 0.465f, size.height * 0.426f)
            )
            // Live synthetic audio waveform visualizer near clue (0.72, 0.80)
            drawAudioTelemetryBox(
                center = Offset(size.width * 0.72f, size.height * 0.80f),
                t = t,
                accentColor = Color(0xFFA855F7),
                isRobotic = true
            )
        }

        VideoVisualType.DISASTER_OLD_FOOTAGE -> {
            // Realistic multi-layered wind-driven rain streaks across the storm footage
            for (r in 0 until 42) {
                val speed = if (r % 2 == 0) 260f else 180f
                val rx = ((r * 47f + t * 65f) % (size.width + 60f)) - 30f
                val ry = ((r * 73f + t * speed) % size.height)
                val len = if (r % 2 == 0) 28.dp.toPx() else 18.dp.toPx()
                drawLine(
                    color = Color(0x55CBD5E1),
                    start = Offset(rx, ry),
                    end = Offset(rx - 9.dp.toPx(), ry + len),
                    strokeWidth = if (r % 3 == 0) 1.6f else 1.0f
                )
            }
            // Occasional sheet lightning flash in storm clouds
            val flash = sin(t * 1.7f) * cos(t * 3.9f)
            if (flash > 0.86f) {
                drawRect(
                    color = Color(0x28E0F2FE),
                    size = size
                )
            }
            // Expanding raindrop ripples on the flooded roadway around (0.65, 0.75)
            for (rip in 0..3) {
                val phase = (t * 0.6f + rip * 0.25f) % 1f
                val cx = size.width * (0.35f + rip * 0.14f)
                val cy = size.height * (0.72f + (rip % 2) * 0.08f)
                drawOval(
                    color = Color.White.copy(alpha = (1f - phase) * 0.35f),
                    topLeft = Offset(cx - 28.dp.toPx() * phase, cy - 7.dp.toPx() * phase),
                    size = Size(56.dp.toPx() * phase, 14.dp.toPx() * phase),
                    style = Stroke(width = 1.2.dp.toPx())
                )
            }
            // Looped emergency siren waveform near (0.65, 0.75)
            drawAudioTelemetryBox(
                center = Offset(size.width * 0.65f, size.height * 0.75f),
                t = t,
                accentColor = Color(0xFFF59E0B),
                isRobotic = false
            )
        }

        VideoVisualType.MIRACLE_CURE_SNACK -> {
            // Effervescent rising micro-bubbles & studio light caustics inside the emerald glassware
            for (b in 0 until 18) {
                val phase = ((t * 0.35f + b * 0.11f) % 1f)
                val bx = size.width * (0.52f + (b % 6) * 0.045f + sin(t + b) * 0.01f)
                val by = size.height * (0.74f - phase * 0.26f)
                drawCircle(
                    color = Color(0x886EE7B7),
                    radius = (2f + (b % 3)).dp.toPx(),
                    center = Offset(bx, by)
                )
            }
            // Sponsored Affiliate Checkout CTA badge at Clue 3 (0.35, 0.88)
            val badgeW = 155.dp.toPx()
            val badgeH = 32.dp.toPx()
            val bx = (size.width * 0.35f - badgeW / 2f).coerceAtLeast(12.dp.toPx())
            val by = size.height * 0.865f
            drawRoundRect(
                color = Color(0xDD064E3B),
                topLeft = Offset(bx, by),
                size = Size(badgeW, badgeH),
                cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx())
            )
            drawRoundRect(
                color = Color(0xFF10B981),
                topLeft = Offset(bx, by),
                size = Size(badgeW, badgeH),
                cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx()),
                style = Stroke(width = 1.5.dp.toPx())
            )
        }

        VideoVisualType.AUTHENTIC_SCIENCE_SOLAR -> {
            // Shimmering polar aurora glow at Jupiter's north pole & infrared storm band (0.55, 0.48)
            val auroraPulse = 0.22f + 0.14f * sin(t * 1.4f)
            drawOval(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFF22D3EE).copy(alpha = auroraPulse), Color.Transparent),
                    center = Offset(size.width * 0.50f, size.height * 0.25f),
                    radius = 90.dp.toPx()
                ),
                topLeft = Offset(size.width * 0.50f - 90.dp.toPx(), size.height * 0.25f - 28.dp.toPx()),
                size = Size(180.dp.toPx(), 56.dp.toPx())
            )
            // Subtle NASA JWST NIRCam target reticle around (0.55, 0.48)
            val rx = size.width * 0.55f
            val ry = size.height * 0.48f
            val rad = 32.dp.toPx()
            drawCircle(
                color = Color(0x6638BDF8),
                radius = rad,
                center = Offset(rx, ry),
                style = Stroke(width = 1.dp.toPx())
            )
            drawLine(
                color = Color(0x6638BDF8),
                start = Offset(rx - rad - 8.dp.toPx(), ry),
                end = Offset(rx + rad + 8.dp.toPx(), ry),
                strokeWidth = 1.dp.toPx()
            )
            drawLine(
                color = Color(0x6638BDF8),
                start = Offset(rx, ry - rad - 8.dp.toPx()),
                end = Offset(rx, ry + rad + 8.dp.toPx()),
                strokeWidth = 1.dp.toPx()
            )
        }

        VideoVisualType.STAGED_HERO_RESCUE -> {
            // Billowing translucent white steam puffs drifting gently above the heritage locomotive
            for (s in 0..5) {
                val phase = (t * 0.18f + s * 0.16f) % 1f
                val sx = size.width * (0.18f + phase * 0.28f)
                val sy = size.height * (0.22f - phase * 0.12f)
                val sRad = (24f + phase * 38f).dp.toPx()
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color.White.copy(alpha = (1f - phase) * 0.32f),
                            Color.Transparent
                        ),
                        center = Offset(sx, sy),
                        radius = sRad
                    ),
                    radius = sRad,
                    center = Offset(sx, sy)
                )
            }
            // Post-production blockbuster audio track indicator at (0.78, 0.82)
            drawAudioTelemetryBox(
                center = Offset(size.width * 0.78f, size.height * 0.82f),
                t = t,
                accentColor = Color(0xFFEF4444),
                isRobotic = false
            )
        }

        VideoVisualType.SPACE_ALIEN_HOAX -> {
            // Subtle GAN deepfake lip-sync scanline / bounding artifact around mouth at (0.50, 0.40..0.44)
            val mouthX = size.width * 0.50f
            val mouthY = size.height * 0.41f
            val boxW = 68.dp.toPx()
            val boxH = 38.dp.toPx()
            val glitchAlpha = if (isInspectionMode) 0.65f else (0.18f + 0.14f * sin(t * 4.2f))
            drawRect(
                color = Color(0xFFEC4899).copy(alpha = glitchAlpha),
                topLeft = Offset(mouthX - boxW / 2f, mouthY - boxH / 2f),
                size = Size(boxW, boxH),
                style = Stroke(width = 1.2.dp.toPx())
            )
            // Flat robotic AI voice pitch monitor at (0.70, 0.70)
            drawAudioTelemetryBox(
                center = Offset(size.width * 0.70f, size.height * 0.70f),
                t = t,
                accentColor = Color(0xFFEC4899),
                isRobotic = true
            )
        }
    }
}

private fun DrawScope.drawAudioTelemetryBox(
    center: Offset,
    t: Float,
    accentColor: Color,
    isRobotic: Boolean
) {
    val boxW = 84.dp.toPx()
    val boxH = 30.dp.toPx()
    val left = (center.x - boxW / 2f).coerceIn(8.dp.toPx(), size.width - boxW - 8.dp.toPx())
    val top = (center.y - boxH / 2f).coerceIn(8.dp.toPx(), size.height - boxH - 8.dp.toPx())

    drawRoundRect(
        color = Color(0xBB0F172A),
        topLeft = Offset(left, top),
        size = Size(boxW, boxH),
        cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx())
    )
    drawRoundRect(
        color = accentColor.copy(alpha = 0.65f),
        topLeft = Offset(left, top),
        size = Size(boxW, boxH),
        cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx()),
        style = Stroke(width = 1.dp.toPx())
    )

    val bars = 11
    val stepX = (boxW - 16.dp.toPx()) / bars
    for (i in 0 until bars) {
        val bx = left + 10.dp.toPx() + i * stepX
        val amp = if (isRobotic) {
            // Flat quantized robotic cadence
            if ((i + (t * 2).toInt()) % 2 == 0) 0.55f else 0.25f
        } else {
            (0.25f + 0.65f * kotlin.math.abs(sin(t * 2.2f + i * 0.7f)))
        }
        val barH = (boxH - 10.dp.toPx()) * amp
        drawLine(
            color = accentColor,
            start = Offset(bx, center.y - barH / 2f),
            end = Offset(bx, center.y + barH / 2f),
            strokeWidth = 2.5.dp.toPx()
        )
    }
}
