package com.akito.lovesync.ui.matching

import android.app.Activity
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import com.akito.lovesync.MatchingViewModel
import com.akito.lovesync.R
import com.akito.lovesync.SoundManager
import com.akito.lovesync.ui.components.OptionCard
import com.akito.lovesync.ui.theme.SoloTextColor
import com.akito.lovesync.ui.theme.roundedFontFamily
import kotlinx.coroutines.launch

@Composable
fun MatchingQuestionScreen(viewModel: MatchingViewModel, soundManager: SoundManager) {
    val question = viewModel.questions.getOrNull(viewModel.currentQuestionIndex) ?: return
    val currentPlayer = if (!viewModel.isPlayer2Turn) viewModel.player1Name else viewModel.player2Name
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

        // Turn indicator responsive variables
        val turnBadgeFontSize = if (isSmallScreen) 12.sp else 16.sp
        val turnBadgeHorizontalPadding = if (isSmallScreen) 14.dp else 22.dp
        val turnBadgeVerticalPadding = if (isSmallScreen) 3.dp else 7.dp
        val turnCaptionFontSize = if (isSmallScreen) 9.sp else 12.sp
        val turnBottomMargin = if (isSmallScreen) 6.dp else 10.dp

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Player Turn Indicator (Clean Responsive Unified Pill Badge)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = turnBottomMargin)
            ) {
                Surface(
                    color = if (!viewModel.isPlayer2Turn) Color(0xFF8A98A5) else Color(0xFFB3A3AA),
                    shape = CircleShape,
                    shadowElevation = if (isSmallScreen) 2.dp else 4.dp,
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.6f))
                ) {
                    Text(
                        text = "${currentPlayer}の番です",
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontFamily = roundedFontFamily,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = turnBadgeFontSize
                        ),
                        modifier = Modifier.padding(horizontal = turnBadgeHorizontalPadding, vertical = turnBadgeVerticalPadding)
                    )
                }
                Spacer(modifier = Modifier.height(if (isSmallScreen) 4.dp else 6.dp))
                Text(
                    text = "🔒 相手に見えないように選んでね！",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontFamily = roundedFontFamily,
                        fontSize = turnCaptionFontSize
                    ),
                    color = SoloTextColor.copy(alpha = 0.7f)
                )
            }

            // Question Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = questionCardBottomMargin),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(28.dp)
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    // Decorations
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
                            .padding(vertical = questionCardVerticalPadding, horizontal = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Bouquet Icon (R.drawable.bouquet1)
                        Image(
                            painter = painterResource(id = R.drawable.bouquet1),
                            contentDescription = "Bouquet Icon",
                            modifier = Modifier.size(heartImageSize),
                            contentScale = ContentScale.Fit
                        )

                        Spacer(modifier = Modifier.height(if (isSmallScreen) 10.dp else 28.dp))

                        Text(
                            text = question.text,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontFamily = roundedFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = questionFontSize,
                                lineHeight = questionLineHeight
                            ),
                            color = SoloTextColor, // 新規テキスト色 #3A3533 に統一
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
                    text = option,
                    label = label,
                    cardHeight = optionCardHeight,
                    onClick = {
                        soundManager.playClickSound()
                        val isLast = viewModel.currentQuestionIndex == viewModel.questions.size - 1
                        val isLastTurn = viewModel.isPlayer2Turn

                        scope.launch { scrollState.animateScrollTo(0) }

                        if (isLast && isLastTurn && activity != null) {
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
