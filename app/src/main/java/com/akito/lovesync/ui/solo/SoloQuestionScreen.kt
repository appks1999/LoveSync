package com.akito.lovesync.ui.solo

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.akito.lovesync.AdManager
import com.akito.lovesync.R
import com.akito.lovesync.ShindanViewModel
import com.akito.lovesync.SoundManager
import com.akito.lovesync.ui.components.OptionCard
import com.akito.lovesync.ui.theme.SoloTextColor
import com.akito.lovesync.ui.theme.roundedFontFamily
import kotlinx.coroutines.launch

@Composable
fun SoloQuestionScreen(viewModel: ShindanViewModel, soundManager: SoundManager) {
    val question = viewModel.currentQuestion ?: return
    val context = LocalContext.current
    val activity = context as? Activity
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val screenHeight = maxHeight
        val isSmallScreen = screenHeight < 560.dp

        val questionCardVerticalPadding = if (isSmallScreen) 20.dp else 48.dp
        val questionCardBottomMargin = if (isSmallScreen) 14.dp else 32.dp
        val heartImageSize = if (isSmallScreen) 46.dp else 63.dp
        val questionFontSize = if (isSmallScreen) 17.sp else 21.sp
        val questionLineHeight = if (isSmallScreen) 24.sp else 38.sp
        val optionCardHeight = if (isSmallScreen) 58.dp else 80.dp
        val optionSpacer = if (isSmallScreen) 8.dp else 17.dp

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Main Question Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = questionCardBottomMargin),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(32.dp)
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    // Card Inner Decorations - Scattered Sparkles
                    Box(modifier = Modifier.matchParentSize()) {
                        val p = Color(0xFFFF80AB).copy(alpha = 0.3f)
                        val v = Color(0xFF6A67CE).copy(alpha = 0.3f)

                        Text("✧", color = p, fontSize = 16.sp, modifier = Modifier.padding(start = 20.dp, top = 20.dp))
                        Text("✧", color = v, fontSize = 12.sp, modifier = Modifier.padding(start = 70.dp, top = 65.dp))
                        Text("✧", color = p, fontSize = 20.sp, modifier = Modifier.align(Alignment.TopEnd).padding(end = 30.dp, top = 30.dp))
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = questionCardVerticalPadding, horizontal = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Heart Icon (R.drawable.heart1)
                        Image(
                            painter = painterResource(id = R.drawable.heart1),
                            contentDescription = "Heart Icon",
                            modifier = Modifier.size(heartImageSize),
                            contentScale = ContentScale.Fit
                        )

                        Spacer(modifier = Modifier.height(if (isSmallScreen) 10.dp else 24.dp))

                        Text(
                            text = question.text,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontFamily = roundedFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = questionFontSize,
                                lineHeight = questionLineHeight
                            ),
                            color = SoloTextColor, // 新規テキスト色 #3A3533
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // Options
            question.options.forEachIndexed { index, option ->
                val label = when(index) {
                    0 -> "A"
                    1 -> "B"
                    2 -> "C"
                    else -> "D"
                }
                OptionCard(
                    text = option.text,
                    label = label,
                    cardHeight = optionCardHeight,
                    onClick = {
                        soundManager.playClickSound()
                        val isLast = viewModel.currentQuestionIndex == viewModel.totalQuestions - 1

                        if (!isLast) {
                            scope.launch { scrollState.animateScrollTo(0) }
                        }

                        if (isLast && activity != null) {
                            AdManager.showInterstitial(activity) {
                                viewModel.selectOption(option)
                            }
                        } else {
                            viewModel.selectOption(option)
                        }
                    }
                )
                Spacer(modifier = Modifier.height(optionSpacer))
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
