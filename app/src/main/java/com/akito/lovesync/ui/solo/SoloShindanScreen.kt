package com.akito.lovesync.ui.solo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.akito.lovesync.ShindanViewModel
import com.akito.lovesync.SoundManager
import com.akito.lovesync.data.LoveShindanContent
import com.akito.lovesync.ui.components.AnimatedTextButton
import com.akito.lovesync.ui.components.RefinedProgressBar
import com.akito.lovesync.ui.theme.SoloBackground
import com.akito.lovesync.ui.theme.SoloTextColor
import com.akito.lovesync.ui.theme.roundedFontFamily

@Composable
fun SoloShindanScreen(viewModel: ShindanViewModel, soundManager: SoundManager, onBack: () -> Unit) {
    val shindanData = LoveShindanContent.soloLoveShindan

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SoloBackground) // 新規カラーコード #F8F5F0
    ) {
        // 背景の装飾レイヤー
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "♡",
                fontSize = 160.sp,
                color = Color(0xFFFF80AB).copy(alpha = 0.05f),
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .graphicsLayer {
                        translationX = 100f
                        rotationZ = -15f
                    }
            )
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 24.dp, bottom = 80.dp)
                    .graphicsLayer(alpha = 0.08f, rotationZ = 20f)
            ) {
                Text(text = "♡", fontSize = 80.sp, color = Color(0xFFFF80AB))
                Text(
                    text = "♡",
                    fontSize = 50.sp,
                    color = Color(0xFFFF80AB),
                    modifier = Modifier.padding(start = 40.dp, top = 30.dp)
                )
            }
        }

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(top = 8.dp, start = 12.dp, end = 12.dp, bottom = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Spacer(modifier = Modifier.width(60.dp))

                    Text(
                        text = shindanData.title,
                        style = MaterialTheme.typography.titleMedium.copy(fontFamily = roundedFontFamily),
                        fontWeight = FontWeight.Bold,
                        color = SoloTextColor, // 新規テキスト色 #3A3533
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )

                    if (!viewModel.isFinished) {
                        AnimatedTextButton(text = "やめる", onClick = onBack)
                    } else {
                        Spacer(modifier = Modifier.width(60.dp))
                    }
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                // Progress and Question Number
                if (!viewModel.isFinished && viewModel.totalQuestions > 0) {
                    RefinedProgressBar(
                        currentIndex = viewModel.currentQuestionIndex,
                        totalCount = viewModel.totalQuestions
                    )
                }

                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    when {
                        viewModel.isFinished -> SoloResultScreen(viewModel, soundManager, onBack)
                        viewModel.currentQuestion != null -> SoloQuestionScreen(viewModel, soundManager)
                        else -> Text("読み込み中...", color = SoloTextColor)
                    }
                }
            }
        }
    }
}
