package com.akito.lovesync.ui.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.akito.lovesync.ui.theme.SoloTextColor
import com.akito.lovesync.ui.theme.roundedFontFamily

@Composable
fun ModeCard(
    title: String,
    description: String,
    iconEmoji: String,
    cardHeight: Dp = 82.dp,
    iconSize: Dp = 50.dp,
    emojiFontSize: TextUnit = 26.sp,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        label = "mode_card_scale"
    )

    // 指定されたグラデーション背景 (左: #F4A090 ➔ 右: #FFF3ED)
    val cardBrush = Brush.horizontalGradient(
        colors = listOf(
            Color(0xFFF4A090), // 左側（ピンク・コーラル系 #F4A090）
            Color(0xFFFFF3ED)  // 右側（ライトベージュ・ホワイト系 #FFF3ED）
        )
    )

    // 指定されたドロップシャドウカラー: rgba(220, 150, 140, 0.25)
    val dropShadowColor = Color(220, 150, 140, (0.25f * 255).toInt())

    Surface(
        onClick = onClick,
        interactionSource = interactionSource,
        modifier = Modifier
            .fillMaxWidth()
            .height(cardHeight)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                spotShadowColor = dropShadowColor
                ambientShadowColor = dropShadowColor
            },
        shape = RoundedCornerShape(24.dp),
        color = Color.Transparent,
        shadowElevation = 6.dp,
        border = BorderStroke(1.dp, Color(0xFFF4A090).copy(alpha = 0.3f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = cardBrush, shape = RoundedCornerShape(24.dp))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // アイコンエリア（薄ピンクの円形バッジ）
                Box(
                    modifier = Modifier
                        .size(iconSize)
                        .clip(CircleShape)
                        .background(Color(0xFFFAD2CC)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = iconEmoji,
                        fontSize = emojiFontSize
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // テキストエリア（ダークチャコール #3A3533）
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = if (cardHeight >= 88.dp) {
                            MaterialTheme.typography.titleLarge.copy(fontFamily = roundedFontFamily)
                        } else {
                            MaterialTheme.typography.titleMedium.copy(fontFamily = roundedFontFamily)
                        },
                        color = SoloTextColor, // #3A3533
                        fontWeight = FontWeight.ExtraBold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = description,
                        style = if (cardHeight >= 88.dp) {
                            MaterialTheme.typography.bodyMedium.copy(fontFamily = roundedFontFamily)
                        } else {
                            MaterialTheme.typography.bodySmall.copy(fontFamily = roundedFontFamily)
                        },
                        color = SoloTextColor.copy(alpha = 0.8f)
                    )
                }

                // 右側の矢印
                Text(
                    text = "〉",
                    color = SoloTextColor.copy(alpha = 0.7f),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
