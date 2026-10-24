package com.example.ui.components

import android.content.res.Configuration
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
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
import androidx.compose.ui.unit.dp

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
 * Adds first-class D-Pad / Remote Control focus ring, subtle scale animation,
 * and Center/Enter key click support while preserving standard touch click behavior.
 */
fun Modifier.tvFocusableClick(
    enabled: Boolean = true,
    focusBorderColor: Color = Color(0xFF38BDF8),
    shape: Shape = RoundedCornerShape(14.dp),
    scaleOnFocus: Float = 1.04f,
    onClick: () -> Unit
): Modifier = composed {
    var isFocused by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isFocused && enabled) scaleOnFocus else 1f,
        animationSpec = tween(durationMillis = 150),
        label = "tv_focus_scale"
    )

    this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .onFocusChanged { focusState ->
            isFocused = focusState.isFocused
        }
        .then(
            if (isFocused && enabled) {
                Modifier.border(width = 3.dp, color = focusBorderColor, shape = shape)
            } else {
                Modifier
            }
        )
        .onPreviewKeyEvent { event ->
            if (!enabled) return@onPreviewKeyEvent false
            if (event.type == KeyEventType.KeyUp &&
                (event.key == Key.DirectionCenter ||
                    event.key == Key.Enter ||
                    event.key == Key.NumPadEnter)
            ) {
                onClick()
                true
            } else {
                false
            }
        }
        .focusable(enabled = enabled)
        .clickable(enabled = enabled, onClick = onClick)
}
