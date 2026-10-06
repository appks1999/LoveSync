package com.akito.lovesync.ui.solo

import android.app.Activity
import android.content.Intent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
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
import com.akito.lovesync.ui.components.LoveRadarChart
import com.akito.lovesync.ui.theme.SoloTextColor
import com.akito.lovesync.ui.theme.SubtitleColor
import com.akito.lovesync.ui.theme.mintyoFontFamily
import com.akito.lovesync.ui.theme.roundedFontFamily

@Composable
fun SoloResultScreen(viewModel: ShindanViewModel, soundManager: SoundManager, onBack: () -> Unit) {
    val result = viewModel.finalResult ?: return
    val context = LocalContext.current
    val activity = context as? Activity
    var isChartUnlocked by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        soundManager.playResultSound()
    }

    val onShareClick = {
        val shareText = "私の恋愛タイプは【${result.title}】でした！\n#あなたの恋愛カルテ #LoveSync\nhttps://play.google.com/store/apps/details?id=com.akito.lovesync"
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
        }
        context.startActivity(Intent.createChooser(intent, "診断結果をシェア"))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        
        // Title Section (Headline font size 26sp)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("✨", fontSize = 18.sp)
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "診断結果",
                style = MaterialTheme.typography.titleLarge.copy(fontFamily = roundedFontFamily, fontSize = 26.sp),
                fontWeight = FontWeight.Bold,
                color = SoloTextColor
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text("✨", fontSize = 18.sp)
        }
        Text(
            text = "あなたの恋愛タイプがわかりました♡",
            style = MaterialTheme.typography.labelMedium,
            color = SubtitleColor
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Result Card (Slightly reduced horizontal padding and radius)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(32.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Animal Image Area (Scaled to 160dp)
                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .background(Color(0xFFFFEBF2), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    val imageRes = when(result.categoryId) {
                        "PURE" -> R.drawable.usagi
                        "PASSION" -> R.drawable.raion
                        "DEVOTION" -> R.drawable.dog
                        "FREE" -> R.drawable.hyo
                        "STRATEGY" -> R.drawable.kitune
                        "MATURE" -> R.drawable.zou
                        "LEADER" -> R.drawable.tora
                        "HEALING" -> R.drawable.panda
                        "COOL" -> R.drawable.kuroneko
                        "NEEDY" -> R.drawable.risu
                        else -> null
                    }

                    if (imageRes != null) {
                        Image(
                            painter = painterResource(id = imageRes),
                            contentDescription = result.title,
                            modifier = Modifier
                                .size(160.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Fit
                        )
                    } else {
                        Text(text = "🐾", fontSize = 70.sp)
                    }
                }
                
                Spacer(modifier = Modifier.height(14.dp))
                
                // Ribbon Label
                Surface(
                    color = Color(0xFFFF80AB),
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier.padding(bottom = 4.dp)
                ) {
                    Text(
                        text = result.subtitle,
                        color = Color.White,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
                
                Text(
                    text = result.title,
                    style = MaterialTheme.typography.headlineMedium.copy(fontFamily = roundedFontFamily, fontSize = 33.sp),
                    fontWeight = FontWeight.ExtraBold,
                    color = SoloTextColor,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "»»» ${result.englishName} «««",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFFFF80AB).copy(alpha = 0.6f)
                )
                
                Spacer(modifier = Modifier.height(20.dp))
                
                // Love Purity Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("❤", fontSize = 16.sp, color = Color(0xFFFF80AB))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("恋愛純粋度", style = MaterialTheme.typography.labelMedium, color = SoloTextColor)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "${result.lovePurity}%",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color(0xFFFF80AB)
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                LinearProgressIndicator(
                    progress = { result.lovePurity / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(CircleShape),
                    color = Color(0xFFFF80AB),
                    trackColor = Color(0xFFFFEBF2)
                )
                
                Spacer(modifier = Modifier.height(24.dp))

                // 先生のアイコン (54dp)
                Box(contentAlignment = Alignment.Center) {
                    Box(modifier = Modifier.size(54.dp).background(Color(0xFFF3F2FF), CircleShape))
                    Image(
                        painter = painterResource(id = R.drawable.main_visual),
                        contentDescription = "先生のアドバイス",
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.TopCenter
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = result.description,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontFamily = mintyoFontFamily,
                        fontSize = 14.sp,
                        lineHeight = 22.sp
                    ),
                    color = SoloTextColor.copy(alpha = 0.85f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Radar Chart
                Text(
                    text = "♥ あなたの恋愛傾向",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color(0xFFFF80AB)
                )
                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    // チャート本体（ロック時はぼかし＋半透明）
                    Box(
                        modifier = Modifier
                            .graphicsLayer { alpha = if (isChartUnlocked) 1f else 0.5f }
                            .blur(if (isChartUnlocked) 0.dp else 6.dp)
                    ) {
                        LoveRadarChart(scores = result.chartScores)
                    }

                    // ロック中のオーバーレイ
                    if (!isChartUnlocked) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .background(Color.White.copy(alpha = 0.4f), RoundedCornerShape(18.dp))
                                .padding(14.dp)
                        ) {
                            Text(
                                text = "あなたの分析チャートを解放 🔓",
                                style = MaterialTheme.typography.titleSmall.copy(fontSize = 13.sp),
                                color = SoloTextColor,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Button(
                                onClick = {
                                    if (activity != null) {
                                        AdManager.showRewarded(activity) {
                                            isChartUnlocked = true
                                        }
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF80AB)),
                                shape = RoundedCornerShape(18.dp),
                                elevation = ButtonDefaults.buttonElevation(defaultElevation = 3.dp)
                            ) {
                                Text("広告を見て解放", color = Color.White, fontSize = 13.sp)
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Share Section
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("結果をシェアしてみよう♡", style = MaterialTheme.typography.labelSmall, color = SubtitleColor)
                    Spacer(modifier = Modifier.height(12.dp))

                    val shareInteractionSource = remember { MutableInteractionSource() }
                    val isSharePressed by shareInteractionSource.collectIsPressedAsState()
                    val shareScale by animateFloatAsState(if (isSharePressed) 0.92f else 1f, label = "scale_share")

                    Button(
                        onClick = onShareClick,
                        interactionSource = shareInteractionSource,
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .height(46.dp)
                            .graphicsLayer(scaleX = shareScale, scaleY = shareScale),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF80AB)),
                        shape = RoundedCornerShape(23.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 3.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("✨", fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "結果をシェアする",
                                color = Color.White,
                                style = MaterialTheme.typography.titleSmall.copy(fontSize = 15.sp),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
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
                    viewModel.reset()
                },
                interactionSource = interactionSource1,
                modifier = Modifier
                    .weight(1f)
                    .graphicsLayer(scaleX = scale1, scaleY = scale1),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color.LightGray),
                shape = RoundedCornerShape(22.dp),
                contentPadding = PaddingValues(horizontal = 6.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("🔄", fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "もう一度診断する",
                        color = SoloTextColor,
                        fontSize = 12.sp,
                        maxLines = 1,
                        softWrap = false
                    )
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("🏠", fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "メニューに戻る",
                        color = Color.White,
                        fontSize = 12.sp,
                        maxLines = 1,
                        softWrap = false
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(48.dp))
    }
}
