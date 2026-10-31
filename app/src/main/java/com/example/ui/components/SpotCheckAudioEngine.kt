package com.example.ui.components

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout. defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.R
import com.example.data.model.VideoVisualType
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.SpotCheckUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin

/**
 * Lifecycle-aware procedural audio engine that generates distinct ambient soundtracks
 * for each screen in SpotCheck. Muted by default until the user manually unmutes.
 */
@Composable
fun SpotCheckScreenAudioEffect(
    uiState: SpotCheckUiState
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    var isLifecycleResumed by remember {
        mutableStateOf(
            lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)
        )
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            isLifecycleResumed = when (event) {
                Lifecycle.Event.ON_RESUME -> true
                Lifecycle.Event.ON_PAUSE,
                Lifecycle.Event.ON_STOP,
                Lifecycle.Event.ON_DESTROY -> false
                else -> isLifecycleResumed
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val currentUiState by rememberUpdatedState(uiState)
    val shouldPlayAudio = !uiState.isAudioMuted && isLifecycleResumed

    if (shouldPlayAudio) {
        LaunchedEffect(uiState.currentScreen, shouldPlayAudio) {
            withContext(Dispatchers.Default) {
                val sampleRate = 22050
                val chunkSamples = sampleRate / 15 // ~66ms low-latency buffer chunks
                val minBufferBytes = try {
                    AudioTrack.getMinBufferSize(
                        sampleRate,
                        AudioFormat.CHANNEL_OUT_MONO,
                        AudioFormat.ENCODING_PCM_16BIT
                    ).coerceAtLeast(chunkSamples * 4)
                } catch (_: Throwable) {
                    chunkSamples * 4
                }

                val audioTrack: AudioTrack? = try {
                    AudioTrack.Builder()
                        .setAudioAttributes(
                            AudioAttributes.Builder()
                                .setUsage(AudioAttributes.USAGE_MEDIA)
                                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                .build()
                        )
                        .setAudioFormat(
                            AudioFormat.Builder()
                                .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                                .setSampleRate(sampleRate)
                                .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                                .build()
                        )
                        .setBufferSizeInBytes(minBufferBytes)
                        .setTransferMode(AudioTrack.MODE_STREAM)
                        .build()
                } catch (_: Throwable) {
                    null
                }

                if (audioTrack == null) return@withContext

                try {
                    if (audioTrack.state == AudioTrack.STATE_INITIALIZED) {
                        audioTrack.play()
                    }
                    val buffer = ShortArray(chunkSamples)
                    var sampleIndex = 0L

                    while (isActive) {
                        val snapshot = currentUiState
                        if (snapshot.isAudioMuted) break

                        fillScreenAudioChunk(
                            buffer = buffer,
                            startSample = sampleIndex,
                            sampleRate = sampleRate,
                            uiState = snapshot
                        )
                        sampleIndex += chunkSamples

                        val written = audioTrack.write(buffer, 0, buffer.size)
                        if (written < 0) break
                    }
                } catch (_: Throwable) {
                    // Ignore audio hardware interruptions gracefully
                } finally {
                    try {
                        if (audioTrack.playState == AudioTrack.PLAYSTATE_PLAYING) {
                            audioTrack.stop()
                        }
                    } catch (_: Throwable) {
                    }
                    try {
                        audioTrack.release()
                    } catch (_: Throwable) {
                    }
                }
            }
        }
    }
}

private fun fillScreenAudioChunk(
    buffer: ShortArray,
    startSample: Long,
    sampleRate: Int,
    uiState: SpotCheckUiState
) {
    val twoPi = 2.0 * PI
    for (i in buffer.indices) {
        val globalSample = startSample + i
        val t = globalSample.toDouble() / sampleRate.toDouble()

        val sampleValue: Double = when (uiState.currentScreen) {
            AppScreen.FEED -> synthesizeFeedSample(t, twoPi, uiState)
            AppScreen.FIELD_GUIDE -> synthesizeFieldGuideSample(t, twoPi)
            AppScreen.SANDBOX -> synthesizeSandboxSample(t, twoPi, uiState)
            AppScreen.PROFILE -> synthesizeProfileSample(t, twoPi)
        }

        val clamped = (sampleValue * 5800.0).coerceIn(-24000.0, 24000.0)
        buffer[i] = clamped.toInt().toShort()
    }
}

/**
 * Screen 1 (Feed): Dynamic soundtrack matching the active video case & forensic radar state.
 */
private fun synthesizeFeedSample(
    t: Double,
    twoPi: Double,
    uiState: SpotCheckUiState
): Double {
    val visualType = uiState.currentCase?.visualType ?: VideoVisualType.AI_MUTANT_ANIMAL

    // If video playback is paused and not in inspection mode, play a gentle idle hum
    if (!uiState.isPlaying && !uiState.isInspectionMode) {
        return 0.18 * sin(twoPi * 110.0 * t) + 0.08 * sin(twoPi * 165.0 * t)
    }

    val baseSignal = when (visualType) {
        VideoVisualType.AI_MUTANT_ANIMAL -> {
            // Crystalline synth arpeggio (A minor 9th: 220, 261.63, 329.63, 440, 493.88 Hz)
            val notes = doubleArrayOf(220.0, 261.63, 329.63, 440.0, 493.88, 329.63)
            val step = ((t * 4.0).toInt()) % notes.size
            val localT = (t * 4.0) % 1.0
            val env = exp(-3.5 * localT)
            val freq = notes[step]
            0.55 * env * sin(twoPi * freq * t) +
                0.25 * env * sin(twoPi * (freq * 2.0) * t) +
                0.20 * sin(twoPi * 110.0 * t)
        }

        VideoVisualType.DISASTER_OLD_FOOTAGE -> {
            // Storm rumble + broadcast alert cadence
            val lfo = 0.5 + 0.5 * sin(twoPi * 0.4 * t)
            val rumble = 0.35 * sin(twoPi * 65.4 * t) + 0.25 * sin(twoPi * 98.0 * t)
            val alertFreq = if (((t * 1.5).toInt() % 2) == 0) 440.0 else 349.23
            val alertEnv = exp(-2.8 * ((t * 1.5) % 1.0))
            rumble * lfo + 0.30 * alertEnv * sin(twoPi * alertFreq * t)
        }

        VideoVisualType.MIRACLE_CURE_SNACK -> {
            // Upbeat commercial pop bell loop (C - G - Am - F)
            val notes = doubleArrayOf(261.63, 329.63, 392.00, 523.25, 392.00, 329.63)
            val step = ((t * 5.0).toInt()) % notes.size
            val localT = (t * 5.0) % 1.0
            val env = exp(-4.2 * localT)
            val freq = notes[step]
            0.60 * env * sin(twoPi * freq * t) + 0.22 * sin(twoPi * 130.81 * t)
        }

        VideoVisualType.AUTHENTIC_SCIENCE_SOLAR -> {
            // Deep NASA space telemetry pad (D5 + A4 + E5 harmonic shimmer)
            val slowMod = 0.65 + 0.35 * sin(twoPi * 0.25 * t)
            val pad = 0.40 * sin(twoPi * 146.83 * t) +
                0.35 * sin(twoPi * 220.0 * t) +
                0.25 * sin(twoPi * 329.63 * t)
            val telemetryBeep = if (((t * 2.0).toInt() % 4) == 0) {
                0.20 * exp(-6.0 * ((t * 2.0) % 1.0)) * sin(twoPi * 880.0 * t)
            } else {
                0.0
            }
            pad * slowMod + telemetryBeep
        }

        VideoVisualType.STAGED_HERO_RESCUE -> {
            // Cinematic action ostinato pulse (E2 / B2 / E3)
            val pulseEnv = exp(-4.5 * ((t * 4.5) % 1.0))
            val bass = 0.55 * pulseEnv * sin(twoPi * 82.41 * t) +
                0.35 * pulseEnv * sin(twoPi * 164.81 * t)
            val tensionLead = 0.25 * sin(twoPi * 246.94 * t)
            bass + tensionLead
        }

        VideoVisualType.SPACE_ALIEN_HOAX -> {
            // Sci-fi synthetic carrier chord with vibrato
            val vibrato = sin(twoPi * 5.5 * t) * 3.0
            0.40 * sin(twoPi * (196.0 + vibrato) * t) +
                0.30 * sin(twoPi * (293.66 - vibrato) * t) +
                0.20 * sin(twoPi * 392.0 * t)
        }
    }

    // Layer subtle forensic sonar ping when Inspection Mode is active
    return if (uiState.isInspectionMode) {
        val pingPhase = (t * 1.2) % 1.0
        val pingEnv = exp(-7.0 * pingPhase)
        val sonarPing = 0.40 * pingEnv * sin(twoPi * 1046.50 * t)
        baseSignal * 0.55 + sonarPing
    } else {
        baseSignal
    }
}

/**
 * Screen 2 (Field Guide): Calm "Media Literacy Academy" ambient electric piano chords.
 */
private fun synthesizeFieldGuideSample(
    t: Double,
    twoPi: Double
): Double {
    // Warm Fmaj7 -> Em7 -> Dm7 -> Cmaj7 progression
    val chords = arrayOf(
        doubleArrayOf(174.61, 220.00, 261.63, 329.63),
        doubleArrayOf(164.81, 196.00, 246.94, 293.66),
        doubleArrayOf(146.83, 174.61, 220.00, 261.63),
        doubleArrayOf(130.81, 164.81, 196.00, 246.94)
    )
    val chordIndex = ((t / 2.4).toInt()) % chords.size
    val activeChord = chords[chordIndex]
    val noteStep = ((t * 2.5).toInt()) % activeChord.size
    val noteEnv = exp(-2.8 * ((t * 2.5) % 1.0))

    val pad = 0.25 * sin(twoPi * activeChord[0] * t) +
        0.20 * sin(twoPi * activeChord[2] * t)
    val chime = 0.45 * noteEnv * sin(twoPi * (activeChord[noteStep] * 2.0) * t)
    return pad + chime
}

/**
 * Screen 3 (Sandbox / Misinfo Lab): Experimental synth pulse that reacts to the threat meter.
 */
private fun synthesizeSandboxSample(
    t: Double,
    twoPi: Double,
    uiState: SpotCheckUiState
): Double {
    var threatFactor = 1
    if (uiState.sandboxIsClickbait) threatFactor += 1
    if (!uiState.sandboxHasSource) threatFactor += 1
    if (uiState.sandboxHasAiGlitch) threatFactor += 1

    val rate = 2.0 + threatFactor * 0.75
    val localT = (t * rate) % 1.0
    val env = exp(-3.8 * localT)

    // Higher threat shifts from consonant perfect fifths to tense minor seconds/tritones
    val intervals = when (threatFactor) {
        1 -> doubleArrayOf(220.0, 277.18, 329.63, 440.0) // A major calm lab
        2 -> doubleArrayOf(220.0, 261.63, 329.63, 392.0) // A minor7 moderate
        3 -> doubleArrayOf(220.0, 261.63, 311.13, 415.30) // Diminished tension
        else -> doubleArrayOf(220.0, 233.08, 311.13, 466.16) // High-alert lab pulse
    }
    val step = ((t * rate).toInt()) % intervals.size
    val freq = intervals[step]

    return 0.50 * env * sin(twoPi * freq * t) +
        0.25 * sin(twoPi * 110.0 * t) +
        0.15 * env * sin(twoPi * (freq * 1.5) * t)
}

/**
 * Screen 4 (Profile / Detective Dossier): Noble "Detective Headquarters" ambient theme.
 */
private fun synthesizeProfileSample(
    t: Double,
    twoPi: Double
): Double {
    // D major / A major warm heroic arpeggio
    val notes = doubleArrayOf(146.83, 220.00, 293.66, 369.99, 440.00, 293.66)
    val step = ((t * 2.2).toInt()) % notes.size
    val localT = (t * 2.2) % 1.0
    val env = exp(-2.4 * localT)
    val freq = notes[step]

    val warmDrone = 0.28 * sin(twoPi * 146.83 * t) + 0.20 * sin(twoPi * 220.00 * t)
    val melody = 0.48 * env * sin(twoPi * freq * t)
    return warmDrone + melody
}

/**
 * Accessible Material 3 Mute / Unmute toggle pill displayed in the header of each screen.
 */
@Composable
fun ScreenAudioMuteButton(
    isMuted: Boolean,
    screenLabel: String,
    accentColor: Color,
    onToggleMute: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String = "audio_mute_button"
) {
    val containerColor by animateColorAsState(
        targetValue = if (isMuted) Color(0xFF1E293B) else accentColor.copy(alpha = 0.22f),
        label = "audio_btn_bg"
    )
    val borderColor by animateColorAsState(
        targetValue = if (isMuted) Color(0x5594A3B8) else accentColor,
        label = "audio_btn_border"
    )

    val actionDesc = if (isMuted) {
        stringResource(R.string.audio_unmute_action)
    } else {
        stringResource(R.string.audio_mute_action)
    }

    val statusText = if (isMuted) {
        stringResource(R.string.audio_status_muted)
    } else {
        screenLabel
    }

    Surface(
        shape = RoundedCornerShape(14.dp),
        color = containerColor,
        border = BorderStroke(1.5.dp, borderColor),
        modifier = modifier
            .defaultMinSize(minWidth = 48.dp, minHeight = 48.dp)
            .tvFocusableClick(
                focusBorderColor = accentColor,
                shape = RoundedCornerShape(14.dp),
                onClick = onToggleMute
            )
            .testTag(testTag)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Surface(
                shape = CircleShape,
                color = if (isMuted) Color(0xFF334155) else accentColor,
                modifier = Modifier.size(30.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = if (isMuted) {
                            Icons.AutoMirrored.Filled.VolumeOff
                        } else {
                            Icons.AutoMirrored.Filled.VolumeUp
                        },
                        contentDescription = actionDesc,
                        tint = if (isMuted) Color(0xFFCBD5E1) else Color.White,
                        modifier = Modifier.size(17.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(
                    text = statusText,
                    color = if (isMuted) Color(0xFFCBD5E1) else Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = if (isMuted) "Tap to Unmute" else "Tap to Mute",
                    color = if (isMuted) Color(0xFF94A3B8) else accentColor,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1
                )
            }

            if (!isMuted) {
                Spacer(modifier = Modifier.width(6.dp))
                LiveAudioEqualizerBars(color = accentColor)
            }
        }
    }
}

@Composable
private fun LiveAudioEqualizerBars(
    color: Color
) {
    val infiniteTransition = rememberInfiniteTransition(label = "eq_bars")
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 6.28318f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "eq_phase"
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        modifier = Modifier.height(16.dp)
    ) {
        for (i in 0 until 3) {
            val fraction = (0.35f + 0.65f * kotlin.math.abs(sin(phase + i * 1.1f)))
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height((14f * fraction).dp)
                    .background(color = color, shape = RoundedCornerShape(2.dp))
            )
        }
    }
}
