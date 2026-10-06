package com.akito.lovesync.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.akito.lovesync.ui.theme.SoloTextColor
import com.akito.lovesync.ui.theme.roundedFontFamily

@Composable
fun OptionCard(
    text: String,
    label: String,
    cardHeight: Dp = 80.dp,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(if (isPressed) 0.97f else 1f, label = "option_scale")
    val backgroundColor by animateColorAsState(
        targetValue = if (isPressed) MaterialTheme.colorScheme.primary else Color.White,
        label = "option_bg_color"
    )
    val contentColor by animateColorAsState(
        targetValue = if (isPressed) Color.White else SoloTextColor, // 新規テキスト色 #3A3533
        label = "option_content_color"
    )
    val labelColor by animateColorAsState(
        targetValue = if (isPressed) Color.White else SoloTextColor.copy(alpha = 0.8f),
        label = "label_color"
    )

    Surface(
        onClick = onClick,
        interactionSource = interactionSource,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = cardHeight)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
        shape = RoundedCornerShape(20.dp),
        color = backgroundColor,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Stylized Letter Label (A, B, C, D)
            Box(
                modifier = Modifier
                    .size(if (cardHeight < 65.dp) 36.dp else 48.dp)
                    .background(if (isPressed) Color.White.copy(alpha = 0.2f) else Color(0xFFF3F2FF), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    style = if (cardHeight < 65.dp) MaterialTheme.typography.titleMedium.copy(fontFamily = roundedFontFamily) else MaterialTheme.typography.titleLarge.copy(fontFamily = roundedFontFamily),
                    color = labelColor,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Text(
                text = text,
                modifier = Modifier.weight(1f),
                style = if (cardHeight < 65.dp) MaterialTheme.typography.bodyMedium.copy(fontFamily = roundedFontFamily, fontWeight = FontWeight.Medium) else MaterialTheme.typography.bodyLarge.copy(fontFamily = roundedFontFamily, fontWeight = FontWeight.Medium),
                color = contentColor
            )
            
            Text(text = "〉", color = if (isPressed) Color.White.copy(alpha = 0.7f) else Color.LightGray, fontSize = 18.sp)
        }
    }
}
