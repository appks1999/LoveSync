package com.akito.lovesync.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.akito.lovesync.ui.theme.SoloTextColor
import com.akito.lovesync.ui.theme.roundedFontFamily

@Composable
fun RefinedProgressBar(
    currentIndex: Int,
    totalCount: Int,
    modifier: Modifier = Modifier
) {
    val targetProgress = if (totalCount > 0) (currentIndex + 1).toFloat() / totalCount.toFloat() else 0f
    val animatedProgress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
        label = "progress_anim"
    )
    val percentage = (targetProgress * 100).toInt()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 4.dp)
    ) {
        // Upper Header Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left Side: "質問 01 / 10" with stylized numbers
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = "質問 ",
                    fontSize = 13.sp,
                    color = SoloTextColor.copy(alpha = 0.7f),
                    fontFamily = roundedFontFamily,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = String.format(java.util.Locale.getDefault(), "%02d", currentIndex + 1),
                    fontSize = 17.sp,
                    color = SoloTextColor,
                    fontFamily = roundedFontFamily,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = " / ${String.format(java.util.Locale.getDefault(), "%02d", totalCount)}",
                    fontSize = 13.sp,
                    color = SoloTextColor.copy(alpha = 0.5f),
                    fontFamily = roundedFontFamily,
                    fontWeight = FontWeight.Medium
                )
            }

            // Right Side: Percentage Pill Badge
            Surface(
                color = Color(0xFFFFEBF2),
                shape = CircleShape,
                border = BorderStroke(1.dp, Color(0xFFFFC1E3).copy(alpha = 0.6f))
            ) {
                Text(
                    text = "$percentage%",
                    fontSize = 12.sp,
                    color = Color(0xFFFF80AB),
                    fontFamily = roundedFontFamily,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Progress Track
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(11.dp)
                .clip(CircleShape)
                .background(Color(0xFFEBE6DF)) // Subtle, warm contrast track background on #F8F5F0
                .border(1.dp, Color(0xFFE0D9CE), CircleShape)
        ) {
            // Animated Gradient Progress Bar Fill
            Box(
                modifier = Modifier
                    .fillMaxWidth(animatedProgress)
                    .fillMaxHeight()
                    .clip(CircleShape)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFFFF80AB), // Vibrant pink
                                Color(0xFF9593E5), // Soft violet
                                Color(0xFF6A67CE)  // Rich purple
                            )
                        )
                    )
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}
