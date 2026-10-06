package com.akito.lovesync.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.akito.lovesync.R
import com.akito.lovesync.SettingsManager
import com.akito.lovesync.SoundManager
import com.akito.lovesync.ui.theme.SoloBackground
import com.akito.lovesync.ui.theme.SubtitleColor
import com.akito.lovesync.ui.theme.TitleColor
import com.akito.lovesync.ui.theme.kyokaFontFamily
import com.akito.lovesync.ui.theme.mintyoFontFamily

@Composable
fun ModeSelectionScreen(
    soundManager: SoundManager,
    settingsManager: SettingsManager,
    onBgmToggle: () -> Unit,
    onModeSelected: (String) -> Unit
) {
    var bgmEnabled by remember { mutableStateOf(settingsManager.isBgmEnabled) }
    var seEnabled by remember { mutableStateOf(settingsManager.isSeEnabled) }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(SoloBackground) // 新規カラーコード #F8F5F0 に統一
    ) {
        val screenHeight = maxHeight

        // 画面高さに応じた動的な文字サイズ・コンポーネント寸法
        val isLargeScreen = screenHeight >= 800.dp
        val isMediumScreen = screenHeight in 680.dp..799.dp

        val titleFontSize = when {
            isLargeScreen -> 56.sp
            isMediumScreen -> 50.sp
            else -> 40.sp
        }

        val subtitleFontSize = when {
            isLargeScreen -> 16.sp
            isMediumScreen -> 15.sp
            else -> 13.sp
        }

        val imageMaxHeight = when {
            isLargeScreen -> 240.dp
            isMediumScreen -> 200.dp
            else -> 150.dp
        }

        val cardHeight = when {
            isLargeScreen -> 96.dp
            isMediumScreen -> 88.dp
            else -> 78.dp
        }

        val cardIconSize = when {
            isLargeScreen -> 56.dp
            isMediumScreen -> 52.dp
            else -> 44.dp
        }

        val cardEmojiFontSize = when {
            isLargeScreen -> 30.sp
            isMediumScreen -> 28.sp
            else -> 22.sp
        }

        // 背景の装飾レイヤー (最背面に配置)
        Box(modifier = Modifier.fillMaxSize()) {
            // 右側の非常に大きなハート
            Text(
                text = "♡",
                fontSize = 280.sp,
                color = Color(0xFFFF80AB).copy(alpha = 0.04f),
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .graphicsLayer {
                        translationX = 150f
                        rotationZ = -20f
                    }
            )
            // 左上の大きなハート
            Text(
                text = "♡",
                fontSize = 200.sp,
                color = Color(0xFFFF80AB).copy(alpha = 0.03f),
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .graphicsLayer {
                        translationX = -80f
                        translationY = -50f
                        rotationZ = 15f
                    }
            )
            // 左下のダブルハート
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 12.dp, bottom = 100.dp)
                    .graphicsLayer(alpha = 0.06f, rotationZ = 25f)
            ) {
                Text(text = "♡", fontSize = 120.sp, color = Color(0xFFFF80AB))
                Text(
                    text = "♡",
                    fontSize = 80.sp,
                    color = Color(0xFFFF80AB),
                    modifier = Modifier.padding(start = 60.dp, top = 40.dp)
                )
            }
        }

        // スクロール可能なコンテナ
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // コンテンツ全体の最小高さを画面高さ(maxHeight)に合わせることで、縦長画面での偏りを防ぐ
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = screenHeight)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Spacer(modifier = Modifier.height(28.dp))

                // タイトルエリア
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "あなたの",
                        style = MaterialTheme.typography.titleLarge.copy(fontFamily = kyokaFontFamily),
                        color = TitleColor,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "恋愛カルテ",
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontFamily = kyokaFontFamily,
                            fontSize = titleFontSize
                        ),
                        color = TitleColor,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // 区切り線とラブレターアイコン
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(0.5f)
                    ) {
                        HorizontalDivider(modifier = Modifier.weight(1f), color = Color.LightGray)
                        Image(
                            painter = painterResource(id = R.drawable.medical_record),
                            contentDescription = "Love Letter Icon",
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .size(60.dp),
                            contentScale = ContentScale.Fit
                        )
                        HorizontalDivider(modifier = Modifier.weight(1f), color = Color.LightGray)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "あなたの恋愛傾向や\n気になる相性をチェックします",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontFamily = mintyoFontFamily,
                            fontSize = subtitleFontSize
                        ),
                        color = SubtitleColor,
                        textAlign = TextAlign.Center,
                        lineHeight = (subtitleFontSize.value * 1.4f).sp
                    )
                }

                // メインビジュアル (画面サイズに応じて柔軟に縮小・拡大)
                Image(
                    painter = painterResource(id = R.drawable.main_visual),
                    contentDescription = "保険医の先生",
                    modifier = Modifier
                        .fillMaxWidth(0.68f)
                        .heightIn(min = 140.dp, max = imageMaxHeight),
                    contentScale = ContentScale.Fit
                )

                // モード選択ボタン群
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ModeCard(
                        title = "一人でじっくり診断",
                        description = "あなたの恋愛傾向を分析します",
                        iconEmoji = "👤",
                        cardHeight = cardHeight,
                        iconSize = cardIconSize,
                        emojiFontSize = cardEmojiFontSize,
                        onClick = {
                            soundManager.playClick2Sound()
                            onModeSelected("SOLO")
                        }
                    )

                    ModeCard(
                        title = "二人で相性チェック",
                        description = "大切な人や気になるあの人と...",
                        iconEmoji = "💕",
                        cardHeight = cardHeight,
                        iconSize = cardIconSize,
                        emojiFontSize = cardEmojiFontSize,
                        onClick = {
                            soundManager.playClick2Sound()
                            onModeSelected("MATCHING")
                        }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        // 設定ボタン（最前面・右上固定 - ステータスバーの下側に位置調整）
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .statusBarsPadding()
                .padding(top = 12.dp, end = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // BGMスイッチ
            IconButton(
                onClick = {
                    soundManager.playClick2Sound()
                    bgmEnabled = !bgmEnabled
                    settingsManager.isBgmEnabled = bgmEnabled
                    onBgmToggle()
                },
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.White.copy(alpha = 0.5f), CircleShape)
            ) {
                Text(text = if (bgmEnabled) "🎵" else "🔇", fontSize = 20.sp)
            }

            // SEスイッチ
            IconButton(
                onClick = {
                    seEnabled = !seEnabled
                    settingsManager.isSeEnabled = seEnabled
                    soundManager.playClick2Sound() // 設定変更後に鳴らす
                },
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.White.copy(alpha = 0.5f), CircleShape)
            ) {
                Text(text = if (seEnabled) "🔊" else "🔈", fontSize = 20.sp)
            }
        }
    }
}
