package com.example.ui.components

import android.content.res.Configuration
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

/**
 * Detects whether the current device is running in Android TV / Google TV mode
 * or on a large landscape screen suitable for 10-foot / widescreen navigation.
 */
@Composable
fun isGoogleTvMode(): Boolean {
    val configuration = LocalConfiguration.current
    val uiMode = configuration.uiMode and Configuration.UI_MODE_TYPE_MASK
    return uiMode == Configuration.UI_MODE_TYPE_TELEVISION
}

/**
 * Safely requests initial D-Pad focus on [focusRequester] once the composable is laid out.
 * Retries briefly to handle Dialog/Popup window attachment timing on Android TV.
 */
@Composable
fun RequestInitialTvFocus(
    focusRequester: FocusRequester,
    triggerKey: Any? = Unit
) {
    LaunchedEffect(triggerKey) {
        withFrameNanos { }
        val focusedImmediate = runCatching {
            focusRequester.requestFocus()
            true
        }.getOrDefault(false)
        if (!focusedImmediate) {
            delay(40)
            runCatching { focusRequester.requestFocus() }
        }
    }
}

/**
 * Adds first-class D-Pad / Remote Control focus ring, subtle scale animation,
 * and Center/Enter key click support to custom surfaces/cards while preserving
 * standard touch click behavior with a single Compose focus target.
 */
fun Modifier.tvFocusableClick(
    enabled: Boolean = true,
    focusBorderColor: Color = Color(0xFF38BDF8),
    shape: Shape = RoundedCornerShape(14.dp),
    scaleOnFocus: Float = 1.04f,
    borderWidth: Dp = 3.dp,
    onClick: () -> Unit
): Modifier = composed {
    var isFocused by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isFocused && enabled) scaleOnFocus else 1f,
        animationSpec = tween(durationMillis = 140),
        label = "tv_focus_scale"
    )

    this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .onFocusChanged { focusState ->
            isFocused = focusState.hasFocus
        }
        .then(
            if (isFocused && enabled) {
                Modifier.border(width = borderWidth, color = focusBorderColor, shape = shape)
            } else {
                Modifier
            }
        )
        .onPreviewKeyEvent { event ->
            if (!enabled) return@onPreviewKeyEvent false
            val isSelectKey = event.key == Key.DirectionCenter ||
                event.key == Key.Enter ||
                event.key == Key.NumPadEnter
            if (!isSelectKey) return@onPreviewKeyEvent false

            when (event.type) {
                KeyEventType.KeyDown -> true
                KeyEventType.KeyUp -> {
                    onClick()
                    true
                }
                else -> false
            }
        }
        .clickable(enabled = enabled, onClick = onClick)
}

/**
 * Adds a high-visibility TV D-Pad focus ring and scale feedback to Material 3 components
 * that already contain their own internal clickable/focus target (such as Button,
 * IconButton, NavigationRailItem, Tab, or OutlinedTextField) without adding a duplicate
 * nested focus node.
 */
fun Modifier.tvFocusVisual(
    enabled: Boolean = true,
    focusBorderColor: Color = Color(0xFFFFFFFF),
    shape: Shape = RoundedCornerShape(12.dp),
    scaleOnFocus: Float = 1.04f,
    borderWidth: Dp = 3.dp
): Modifier = composed {
    var isFocused by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isFocused && enabled) scaleOnFocus else 1f,
        animationSpec = tween(durationMillis = 140),
        label = "tv_visual_scale"
    )

    this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .onFocusChanged { focusState ->
            isFocused = focusState.hasFocus
        }
        .then(
            if (isFocused && enabled) {
                Modifier.border(width = borderWidth, color = focusBorderColor, shape = shape)
            } else {
                Modifier
            }
        )
}

/**
 * Makes a read-only informational card focusable for Android TV D-Pad scrolling
 * so users can smoothly step through lists (Comments, S.I.F.T. steps, Field Guide,
 * Badges, Case History) with visible focus feedback.
 */
fun Modifier.tvFocusableCard(
    focusBorderColor: Color = Color(0xFF38BDF8),
    shape: Shape = RoundedCornerShape(14.dp),
    scaleOnFocus: Float = 1.02f
): Modifier = composed {
    var isFocused by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isFocused) scaleOnFocus else 1f,
        animationSpec = tween(durationMillis = 140),
        label = "tv_card_scale"
    )

    this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .onFocusChanged { focusState ->
            isFocused = focusState.hasFocus
        }
        .then(
            if (isFocused) {
                Modifier.border(width = 2.5.dp, color = focusBorderColor, shape = shape)
            } else {
                Modifier
            }
        )
        .focusable()
}
