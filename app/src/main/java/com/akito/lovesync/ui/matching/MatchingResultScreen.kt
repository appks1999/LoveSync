package com.akito.lovesync.ui.matching

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.akito.lovesync.MatchingViewModel
import com.akito.lovesync.SoundManager
import com.akito.lovesync.ui.theme.SoloTextColor
import com.akito.lovesync.ui.theme.SubtitleColor
import com.akito.lovesync.ui.theme.roundedFontFamily

@Composable
fun MatchingResultScreen(viewModel: MatchingViewModel, soundManager: SoundManager, onBack: () -> Unit) {
    val score = viewModel.calculateMatchingScore()
    val matched = viewModel.getMatchedPoints()
    val nearMatched = viewModel.getNearMatchedPoints()
    val mismatches = viewModel.getMismatchPoints()

    LaunchedEffect(Unit) {
        soundManager.playResultSound()
    }

    val feedbackText = when {
        score >= 80 -> "最高の相性です！ 💖"
        score >= 60 -> "良い相性です！ ✨"
        score >= 40 -> "まずまずの相性です！ 😊"
        else -> "これからの二人に期待！ 🌱"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Header
        Box(modifier = Modifier.fillMaxWidth().padding(top = 12.dp, start = 16.dp, end = 16.dp)) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("✨", fontSize = 15.sp)
                    Text(
                        text = "診断結果",
                        style = MaterialTheme.typography.titleMedium.copy(fontFamily = roundedFontFamily, fontSize = 20.sp),
                        fontWeight = FontWeight.Bold,
                        color = SoloTextColor,
                        modifier = Modifier.padding(horizontal = 6.dp)
                    )
                    Text(text = "✨", fontSize = 16.sp, color = Color(0xFFFF80AB))
                }
                Text(
                    text = "おふたりの相性を診断しました♡",
                    style = MaterialTheme.typography.labelSmall,
                    color = SubtitleColor
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Main Score Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(28.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Ribbon
                Surface(
                    color = Color(0xFFFFC1E3), // Soft Pink
                    shape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp),
                    modifier = Modifier.width(90.dp)
                ) {
                    Text(
                        text = "診断結果",
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(vertical = 3.dp)
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Heart Score Container (Unclipped 170dp Container)
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth().height(170.dp)) {
                    Text(
                        text = "❤",
                        fontSize = 130.sp,
                        lineHeight = 130.sp,
                        color = Color(0xFFFF80AB).copy(alpha = 0.8f),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "$score%",
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontWeight = FontWeight.Black,
                            fontSize = 48.sp,
                            color = Color.White
                        ),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = feedbackText,
                    style = MaterialTheme.typography.titleMedium.copy(fontFamily = roundedFontFamily, fontSize = 15.sp),
                    fontWeight = FontWeight.Bold,
                    color = SoloTextColor
                )
                
                Text(
                    text = "お互いの違いを理解し合うことで、\nもっと素敵な関係になれます♡",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = SubtitleColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
                )

                Spacer(modifier = Modifier.height(14.dp))
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Category Cards
        Column(
            modifier = Modifier.padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (matched.isNotEmpty()) {
                MatchingCategoryCard(
                    title = "価値観がぴったり！ ✨",
                    items = matched,
                    baseColor = Color(0xFFFFEBEE), // Very Light Pink
                    accentColor = Color(0xFFF06292),
                    icon = "💖",
                    count = matched.size
                )
            }

            if (nearMatched.isNotEmpty()) {
                MatchingCategoryCard(
                    title = "価値観が近いかも！ 💡",
                    items = nearMatched,
                    baseColor = Color(0xFFFFFDE7), // Very Light Yellow
                    accentColor = Color(0xFFFBC02D),
                    icon = "💛",
                    count = nearMatched.size
                )
            }

            if (mismatches.isNotEmpty()) {
                MatchingCategoryCard(
                    title = "ここが二人の伸びしろ！ 🌱",
                    items = mismatches,
                    baseColor = Color(0xFFF3F2FF), // Very Light Purple
                    accentColor = Color(0xFF7E7CCF),
                    icon = "🌱",
                    count = mismatches.size,
                    footer = "違いを楽しむことで、より深い絆が生まれます♡"
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Bottom Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            val interactionSource1 = remember { MutableInteractionSource() }
            val isPressed1 by interactionSource1.collectIsPressedAsState()
            val scale1 by animateFloatAsState(if (isPressed1) 0.95f else 1f, label = "scale_retry")

            Button(
                onClick = { 
                    soundManager.playClick2Sound()
                    viewModel.setup()
                },
                interactionSource = interactionSource1,
                modifier = Modifier
                    .weight(1f)
                    .graphicsLayer(scaleX = scale1, scaleY = scale1),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFF7E7CCF)),
                shape = RoundedCornerShape(22.dp),
                contentPadding = PaddingValues(horizontal = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🔄", fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("最初から診断する", color = Color(0xFF7E7CCF), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }

            val interactionSource2 = remember { MutableInteractionSource() }
            val isPressed2 by interactionSource2.collectIsPressedAsState()
            val scale2 by animateFloatAsState(if (isPressed2) 0.95f else 1f, label = "scale_home")

            Button(
                onClick = {
                    soundManager.playClick2Sound()
                    onBack()
                },
                interactionSource = interactionSource2,
                modifier = Modifier
                    .weight(1f)
                    .graphicsLayer(scaleX = scale2, scaleY = scale2),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(22.dp),
                contentPadding = PaddingValues(horizontal = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🏠", fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("メニューに戻る", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
        
        Spacer(modifier = Modifier.height(48.dp))
    }
}
