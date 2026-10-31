package com.example.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.DetectionClue
import com.example.data.model.MediaCase

@Composable
fun ClueDetailSheet(
    clue: DetectionClue,
    onDismiss: () -> Unit,
    mediaCase: MediaCase? = null,
    modifier: Modifier = Modifier
) {
    val gotItFocusRequester = remember { FocusRequester() }
    val closeFocusRequester = remember { FocusRequester() }

    // Ensure Back button on Android TV remote always dismisses the clue card
    BackHandler(onBack = onDismiss)

    // Automatically move cursor focus into the clue card as soon as it opens
    RequestInitialTvFocus(focusRequester = gotItFocusRequester, triggerKey = clue.id)

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x99020617))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onDismiss
                )
                .onPreviewKeyEvent { event ->
                    if (event.type == KeyEventType.KeyUp &&
                        (event.key == Key.Back || event.key == Key.Escape)
                    ) {
                        onDismiss()
                        true
                    } else {
                        false
                    }
                }
                .padding(horizontal = 24.dp, vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = modifier
                    .widthIn(max = 560.dp)
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {} // Prevent backdrop click-through inside card
                    )
                    .testTag("clue_detail_card"),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1E293B)
                ),
                border = BorderStroke(1.5.dp, Color(0xFF2DD4BF)),
                elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFF0F766E),
                            modifier = Modifier.size(38.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Detective clue found",
                                    tint = Color(0xFF2DD4BF),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "FORENSIC CLUE DISCOVERED",
                                color = Color(0xFF2DD4BF),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = clue.title,
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))

                        // D-Pad Focusable Close Button Pill
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFF0F172A),
                            border = BorderStroke(1.dp, Color(0x5594A3B8)),
                            modifier = Modifier
                                .focusRequester(closeFocusRequester)
                                .focusProperties {
                                    down = gotItFocusRequester
                                }
                                .tvFocusableClick(
                                    focusBorderColor = Color.White,
                                    shape = RoundedCornerShape(12.dp),
                                    onClick = onDismiss
                                )
                                .testTag("close_clue_detail")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close clue window",
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Close",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Photorealistic Forensic Optical Zoom Crop of the Clue Region
                    if (mediaCase != null) {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = Color(0xFF090D16),
                            border = BorderStroke(1.dp, Color(0xFF06B6D4)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(128.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(14.dp))
                            ) {
                                Image(
                                    painter = painterResource(id = mediaCase.visualType.toSceneDrawableRes()),
                                    contentDescription = "Forensic zoom of ${clue.title}",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .graphicsLayer {
                                            val zoom = 2.6f
                                            scaleX = zoom
                                            scaleY = zoom
                                            translationX = (0.5f - clue.normX) * size.width * 1.35f
                                            translationY = (0.5f - clue.normY) * size.height * 1.35f
                                        }
                                )

                                // Forensic Reticle Overlay
                                Canvas(modifier = Modifier.fillMaxSize()) {
                                    val cx = size.width / 2f
                                    val cy = size.height / 2f
                                    val rad = 26.dp.toPx()
                                    drawCircle(
                                        color = Color(0xCC22D3EE),
                                        radius = rad,
                                        center = Offset(cx, cy),
                                        style = Stroke(width = 1.5.dp.toPx())
                                    )
                                    drawLine(
                                        color = Color(0x8822D3EE),
                                        start = Offset(cx - rad - 12.dp.toPx(), cy),
                                        end = Offset(cx + rad + 12.dp.toPx(), cy),
                                        strokeWidth = 1.dp.toPx()
                                    )
                                    drawLine(
                                        color = Color(0x8822D3EE),
                                        start = Offset(cx, cy - rad - 12.dp.toPx()),
                                        end = Offset(cx, cy + rad + 12.dp.toPx()),
                                        strokeWidth = 1.dp.toPx()
                                    )
                                }

                                Row(
                                    modifier = Modifier
                                        .align(Alignment.BottomStart)
                                        .fillMaxWidth()
                                        .background(Color(0xAA090D16))
                                        .padding(horizontal = 10.dp, vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "OPTICAL LOUPE • 2.6x ZOOM",
                                        color = Color(0xFF22D3EE),
                                        fontSize = 10.sp,
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "LOC (${(clue.normX * 100).toInt()}%, ${(clue.normY * 100).toInt()}%)",
                                        color = Color(0xFFCBD5E1),
                                        fontSize = 10.sp,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    // Clue Type Tag
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0x3338BDF8)
                    ) {
                        Text(
                            text = "CATEGORY: ${clue.type.displayName.uppercase()}",
                            color = Color(0xFF7DD3FC),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Explanation
                    Text(
                        text = clue.explanation,
                        color = Color(0xFFE2E8F0),
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Detective Tip Box
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF0F172A)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lightbulb,
                                contentDescription = "Sleuth Tip",
                                tint = Color(0xFFFBBF24),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Detective Hint: ${clue.hint}",
                                color = Color(0xFFCBD5E1),
                                fontSize = 12.sp,
                                lineHeight = 18.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Primary Action Button — receives automatic D-Pad focus when opened
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .focusRequester(gotItFocusRequester)
                            .focusProperties {
                                up = closeFocusRequester
                            }
                            .tvFocusVisual(
                                focusBorderColor = Color.White,
                                shape = RoundedCornerShape(12.dp),
                                scaleOnFocus = 1.03f,
                                borderWidth = 3.dp
                            )
                            .testTag("got_it_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF06B6D4)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Got It! Keep Investigating",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Tip: Press OK or Back on remote to close",
                        color = Color(0xFF94A3B8),
                        fontSize = 11.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
