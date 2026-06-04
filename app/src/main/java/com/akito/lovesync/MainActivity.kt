package com.akito.lovesync

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.material3.IconButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import android.app.Activity
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import com.akito.lovesync.data.LoveShindanContent
import com.akito.lovesync.ui.theme.BackgroundEnd
import com.akito.lovesync.ui.theme.BackgroundStart
import com.akito.lovesync.ui.theme.LoveSyncTheme
import com.akito.lovesync.ui.theme.SubtitleColor
import com.akito.lovesync.ui.theme.TitleColor
import kotlin.math.cos
import kotlin.math.sin

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val roundedFontName = GoogleFont("Zen Maru Gothic")

val roundedFontFamily = FontFamily(
    Font(googleFont = roundedFontName, fontProvider = provider, weight = FontWeight.Normal),
    Font(googleFont = roundedFontName, fontProvider = provider, weight = FontWeight.Bold),
    Font(googleFont = roundedFontName, fontProvider = provider, weight = FontWeight.ExtraBold)
)

class MainActivity : ComponentActivity() {
    private var mediaPlayer: android.media.MediaPlayer? = null
    private lateinit var settingsManager: SettingsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        settingsManager = SettingsManager(this)

        // AdMobの初期化
        MobileAds.initialize(this)
        AdManager.loadInterstitial(this)
        AdManager.loadRewarded(this)

        // BGMの初期化
        mediaPlayer = android.media.MediaPlayer.create(this, R.raw.new_main).apply {
            isLooping = true
            val volume = if (settingsManager.isBgmEnabled) 0.3f else 0f
            setVolume(volume, volume)
            start()
        }

        setContent {
            LoveSyncTheme {
                MainContainer()
            }
        }
    }

    fun updateBgmVolume() {
        val volume = if (settingsManager.isBgmEnabled) 0.3f else 0f
        mediaPlayer?.setVolume(volume, volume)
    }

    fun pauseBgm() {
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
        }
    }

    fun resumeBgm() {
        if (settingsManager.isBgmEnabled && mediaPlayer != null) {
            mediaPlayer?.start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}

@Composable
fun MainContainer(
    soloViewModel: ShindanViewModel = viewModel(),
    matchingViewModel: MatchingViewModel = viewModel()
) {
    val context = LocalContext.current
    val soundManager = remember { SoundManager(context) }
    val settingsManager = remember { SettingsManager(context) }
    val activity = context as? MainActivity

    var currentMode by remember { mutableStateOf<String?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize().padding(bottom = 50.dp)) {
            when (currentMode) {
                "SOLO" -> SoloShindanScreen(soloViewModel, soundManager) { currentMode = null }
                "MATCHING" -> MatchingShindanScreen(matchingViewModel, soundManager) { currentMode = null }
                else -> ModeSelectionScreen(
                    soundManager = soundManager,
                    settingsManager = settingsManager,
                    onBgmToggle = { activity?.updateBgmVolume() }
                ) { mode ->
                    if (mode == "SOLO") {
                        soloViewModel.setup(LoveShindanContent.soloLoveShindan, limit = 10)
                    } else if (mode == "MATCHING") {
                        matchingViewModel.setup()
                    }
                    currentMode = mode
                }
            }
        }

        // バナー広告を常に最下部に表示
        Box(modifier = Modifier.fillMaxWidth().height(50.dp).align(Alignment.BottomCenter).background(Color.White)) {
            BannerAdView()
        }
    }
}

@Composable
fun BannerAdView() {
    AndroidView(
        modifier = Modifier.fillMaxWidth(),
        factory = { context ->
            AdView(context).apply {
                setAdSize(AdSize.BANNER)
                adUnitId = "ca-app-pub-8950375321788767/6514236793" // 本番用ID
                loadAd(AdRequest.Builder().build())
            }
        }
    )
}

@Composable
fun ModeSelectionScreen(
    soundManager: SoundManager,
    settingsManager: SettingsManager,
    onBgmToggle: () -> Unit,
    onModeSelected: (String) -> Unit
) {
    var bgmEnabled by remember { mutableStateOf(settingsManager.isBgmEnabled) }
    var seEnabled by remember { mutableStateOf(settingsManager.isSeEnabled) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(BackgroundStart, BackgroundEnd)
                )
            )
    ) {
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(80.dp))

            // タイトルエリア
            Text(
                text = "あなたの",
                style = MaterialTheme.typography.headlineLarge.copy(fontFamily = roundedFontFamily),
                color = TitleColor,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "恋愛カルテ",
                style = MaterialTheme.typography.displayLarge.copy(
                    fontFamily = roundedFontFamily,
                    fontSize = 54.sp
                ),
                color = TitleColor,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))
            
            // 区切り線とハート
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(0.6f)
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = Color.LightGray)
                Text(text = "❤", modifier = Modifier.padding(horizontal = 8.dp), color = Color(0xFFFF80AB), fontSize = 16.sp)
                HorizontalDivider(modifier = Modifier.weight(1f), color = Color.LightGray)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "あなたの恋愛傾向や\n気になる相性をチェックします♡",
                style = MaterialTheme.typography.bodyLarge,
                color = SubtitleColor,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // メインビジュアル (アイキャッチ型)
            Image(
                painter = painterResource(id = R.drawable.main_visual),
                contentDescription = "保険医の先生",
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .height(260.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(32.dp))

            // モード選択ボタン
            ModeCard(
                title = "一人でじっくり診断",
                description = "あなたの恋愛傾向を分析します",
                iconEmoji = "👤",
                onClick = { 
                    soundManager.playClick2Sound()
                    onModeSelected("SOLO") 
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            ModeCard(
                title = "二人で相性チェック",
                description = "大切な人や気になるあの人と...",
                iconEmoji = "💕",
                onClick = { 
                    soundManager.playClick2Sound()
                    onModeSelected("MATCHING") 
                }
            )

            Spacer(modifier = Modifier.height(64.dp))
        }

        // 設定ボタン（最前面・最後に記述することで重なり順を一番上にする）
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 48.dp, end = 16.dp),
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

@Composable
fun ModeCard(
    title: String,
    description: String,
    iconEmoji: String,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        label = "mode_card_scale"
    )

    Surface(
        onClick = onClick,
        interactionSource = interactionSource,
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.primary,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // アイコンエリア
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(Color.White.copy(alpha = 0.2f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = iconEmoji, fontSize = 32.sp)
            }

            Spacer(modifier = Modifier.width(20.dp))

            // テキストエリア
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }

            // 矢印
            Text(text = "〉", color = Color.White.copy(alpha = 0.7f), fontSize = 20.sp)
        }
    }
}

@Composable
fun SoloShindanScreen(viewModel: ShindanViewModel, soundManager: SoundManager, onBack: () -> Unit) {
    val shindanData = LoveShindanContent.soloLoveShindan

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(BackgroundStart, BackgroundEnd)
                )
            )
    ) {
        // 背景の装飾レイヤー (画像を使わずにコードで豪華にする)
        Box(modifier = Modifier.fillMaxSize()) {
            // 右側の大きなハートの輪郭
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
            // 左下のダブルハート
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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 48.dp, start = 16.dp, end = 16.dp, bottom = 8.dp)
                ) {
                    // Title (Centered)
                    Row(
                        modifier = Modifier.align(Alignment.Center),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = shindanData.title,
                            style = MaterialTheme.typography.titleLarge.copy(fontFamily = roundedFontFamily),
                            fontWeight = FontWeight.Bold,
                            color = TitleColor
                        )
                    }

                    // やめる button (Previous Animated Style)
                    if (!viewModel.isFinished) {
                        Box(modifier = Modifier.align(Alignment.CenterEnd)) {
                            AnimatedTextButton(text = "やめる", onClick = onBack)
                        }
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
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "質問 ${viewModel.currentQuestionIndex + 1} / ${viewModel.totalQuestions}",
                                style = MaterialTheme.typography.labelLarge,
                                color = TitleColor,
                                fontFamily = roundedFontFamily
                            )
                            Text(text = "🚩", fontSize = 16.sp)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        // Custom Gradient Progress Bar
                        val progress = viewModel.currentQuestionIndex.toFloat() / viewModel.totalQuestions.toFloat()
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp)
                                .background(Color.White.copy(alpha = 0.5f), CircleShape)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(progress)
                                    .fillMaxSize()
                                    .background(
                                        brush = Brush.horizontalGradient(
                                            colors = listOf(Color(0xFF9593E5), Color(0xFF6A67CE))
                                        ),
                                        shape = CircleShape
                                    )
                            )
                        }
                    }
                }

                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    when {
                        viewModel.isFinished -> SoloResultScreen(viewModel, soundManager, onBack)
                        viewModel.currentQuestion != null -> SoloQuestionScreen(viewModel, soundManager)
                        else -> Text("読み込み中...", color = TitleColor)
                    }
                }
            }
        }
    }
}

@Composable
fun AnimatedTextButton(text: String, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(if (isPressed) 0.92f else 1f, label = "scale")

    TextButton(
        onClick = onClick,
        interactionSource = interactionSource,
        modifier = Modifier.graphicsLayer(scaleX = scale, scaleY = scale)
    ) {
        Text(
            text = text,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
fun SoloQuestionScreen(viewModel: ShindanViewModel, soundManager: SoundManager) {
    val question = viewModel.currentQuestion ?: return
    val context = LocalContext.current
    val activity = context as? Activity
    val scrollState = rememberScrollState()
    val scope = androidx.compose.runtime.rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Main Question Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(32.dp)
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                // Card Inner Decorations - Scattered Sparkles (Night Sky effect)
                Box(modifier = Modifier.matchParentSize()) {
                    val p = Color(0xFFFF80AB).copy(alpha = 0.3f) // Pink (Visible alpha)
                    val v = Color(0xFF6A67CE).copy(alpha = 0.3f) // Violet
                    
                    // 8 scattered sparkles spread out across the top and sides
                    Text("✧", color = p, fontSize = 16.sp, modifier = Modifier.padding(start = 20.dp, top = 20.dp))
                    Text("✧", color = v, fontSize = 12.sp, modifier = Modifier.padding(start = 70.dp, top = 65.dp))
                    Text("✧", color = p, fontSize = 20.sp, modifier = Modifier.align(Alignment.TopEnd).padding(end = 30.dp, top = 30.dp))
                    Text("✧", color = v, fontSize = 18.sp, modifier = Modifier.align(Alignment.TopStart).padding(start = 15.dp, top = 100.dp))
                    Text("✧", color = p, fontSize = 14.sp, modifier = Modifier.align(Alignment.TopEnd).padding(end = 15.dp, top = 110.dp))
                    Text("✧", color = v, fontSize = 16.sp, modifier = Modifier.align(Alignment.TopStart).padding(start = 40.dp, top = 85.dp))
                    Text("✧", color = p, fontSize = 14.sp, modifier = Modifier.align(Alignment.TopCenter).padding(top = 15.dp))
                    Text("✧", color = v, fontSize = 14.sp, modifier = Modifier.align(Alignment.TopEnd).padding(end = 80.dp, top = 60.dp))
                }

                // Card Inner Decorations - Bottom Right Interlocking Hearts
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 12.dp, end = 12.dp)
                        .size(100.dp)
                ) {
                    Text(
                        text = "♡",
                        fontSize = 70.sp,
                        color = Color(0xFFFF80AB).copy(alpha = 0.2f),
                        modifier = Modifier.align(Alignment.BottomEnd).graphicsLayer { rotationZ = -5f }
                    )
                    Text(
                        text = "♡",
                        fontSize = 50.sp,
                        color = Color(0xFFFF80AB).copy(alpha = 0.15f),
                        modifier = Modifier.align(Alignment.BottomEnd).graphicsLayer { 
                            translationX = -50f
                            translationY = 15f
                            rotationZ = 15f 
                        }
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 48.dp, horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Soft Circle with Heart (Following Matching style)
                    Box(contentAlignment = Alignment.Center) {
                        Box(modifier = Modifier.size(56.dp).background(Color(0xFFFFEBF2), CircleShape))
                        Text(
                            text = "❤",
                            fontSize = 25.sp,
                            color = Color(0xFFFF80AB)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(28.dp))
                    
                    Text(
                        text = question.text,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontFamily = roundedFontFamily,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 38.sp
                        ),
                        color = TitleColor,
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
                onClick = { 
                    soundManager.playClickSound()
                    val isLast = viewModel.currentQuestionIndex == viewModel.totalQuestions - 1
                    
                    // 次の質問のために一番上へスクロール
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
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun OptionCard(text: String, label: String, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(if (isPressed) 0.97f else 1f, label = "option_scale")
    val backgroundColor by animateColorAsState(
        targetValue = if (isPressed) MaterialTheme.colorScheme.primary else Color.White,
        label = "option_bg_color"
    )
    val contentColor by animateColorAsState(
        targetValue = if (isPressed) Color.White else TitleColor.copy(alpha = 0.8f),
        label = "option_content_color"
    )
    val labelColor by animateColorAsState(
        targetValue = if (isPressed) Color.White else TitleColor.copy(alpha = 0.6f),
        label = "label_color"
    )

    Surface(
        onClick = onClick,
        interactionSource = interactionSource,
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
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
                    .size(48.dp)
                    .background(if (isPressed) Color.White.copy(alpha = 0.2f) else Color(0xFFF3F2FF), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.titleLarge.copy(fontFamily = roundedFontFamily),
                    color = labelColor,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Text(
                text = text,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontFamily = roundedFontFamily,
                    fontWeight = FontWeight.Medium
                ),
                color = contentColor
            )
            
            Text(text = "〉", color = if (isPressed) Color.White.copy(alpha = 0.7f) else Color.LightGray, fontSize = 18.sp)
        }
    }
}

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
        val shareText = "私の恋愛タイプは【${result.title}】でした！\n#あなたの恋愛カルテ #LoveSync"
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
        Spacer(modifier = Modifier.height(12.dp))
        
        // Title Section
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("✨", fontSize = 20.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "診断結果",
                style = MaterialTheme.typography.headlineMedium.copy(fontFamily = roundedFontFamily),
                fontWeight = FontWeight.Bold,
                color = TitleColor
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("✨", fontSize = 20.sp)
        }
        Text(
            text = "あなたの恋愛タイプがわかりました♡",
            style = MaterialTheme.typography.labelLarge,
            color = SubtitleColor
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Result Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(40.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Animal Image Area
                Box(
                    modifier = Modifier
                        .size(180.dp)
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
                                .size(180.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Fit
                        )
                    } else {
                        Text(text = "🐾", fontSize = 80.sp)
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Ribbon Label
                Surface(
                    color = Color(0xFFFF80AB),
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier.padding(bottom = 4.dp)
                ) {
                    Text(
                        text = result.subtitle,
                        color = Color.White,
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
                
                Text(
                    text = result.title,
                    style = MaterialTheme.typography.displaySmall.copy(fontFamily = roundedFontFamily),
                    fontWeight = FontWeight.ExtraBold,
                    color = TitleColor,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "»»» ${result.englishName} «««",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color(0xFFFF80AB).copy(alpha = 0.6f)
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Love Purity Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("❤", fontSize = 18.sp, color = Color(0xFFFF80AB))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("恋愛純粋度", style = MaterialTheme.typography.labelLarge, color = TitleColor)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "${result.lovePurity}%",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFF80AB)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { result.lovePurity / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(12.dp)
                        .clip(CircleShape),
                    color = Color(0xFFFF80AB),
                    trackColor = Color(0xFFFFEBF2)
                )
                
                Spacer(modifier = Modifier.height(32.dp))

                // 先生のアイコン
                Box(contentAlignment = Alignment.Center) {
                    Box(modifier = Modifier.size(64.dp).background(Color(0xFFF3F2FF), CircleShape))
                    Image(
                        painter = painterResource(id = R.drawable.main_visual),
                        contentDescription = "先生のアドバイス",
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.TopCenter
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = result.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = TitleColor.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center,
                    lineHeight = 26.sp,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // Radar Chart
                Text(
                    text = "♥ あなたの恋愛傾向",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color(0xFFFF80AB)
                )
                Spacer(modifier = Modifier.height(16.dp))

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
                                .background(Color.White.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "あなたの分析チャートを解放 🔓",
                                style = MaterialTheme.typography.titleSmall,
                                color = TitleColor,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = {
                                    if (activity != null) {
                                        AdManager.showRewarded(activity) {
                                            isChartUnlocked = true
                                        }
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF80AB)),
                                shape = RoundedCornerShape(20.dp),
                                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                            ) {
                                Text("広告を見て解放", color = Color.White)
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // Share Section
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("結果をシェアしてみよう♡", style = MaterialTheme.typography.labelMedium, color = SubtitleColor)
                    Spacer(modifier = Modifier.height(16.dp))

                    val shareInteractionSource = remember { MutableInteractionSource() }
                    val isSharePressed by shareInteractionSource.collectIsPressedAsState()
                    val shareScale by animateFloatAsState(if (isSharePressed) 0.92f else 1f, label = "scale_share")

                    Button(
                        onClick = onShareClick,
                        interactionSource = shareInteractionSource,
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(50.dp)
                            .graphicsLayer(scaleX = shareScale, scaleY = shareScale),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF80AB)),
                        shape = RoundedCornerShape(25.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("✨", fontSize = 18.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "結果をシェアする",
                                color = Color.White,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))

        // Bottom Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
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
                shape = RoundedCornerShape(24.dp),
                contentPadding = PaddingValues(horizontal = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("🔄", fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "もう一度診断する",
                        color = TitleColor,
                        fontSize = 13.sp,
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
                shape = RoundedCornerShape(24.dp),
                contentPadding = PaddingValues(horizontal = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("🏠", fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "メニューに戻る",
                        color = Color.White,
                        fontSize = 13.sp,
                        maxLines = 1,
                        softWrap = false
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(64.dp))
    }
}

@Composable
fun LoveRadarChart(scores: Map<String, Int>) {
    val labels = listOf("愛情深さ", "一途さ", "安心感", "積極性", "嫉妬深さ")
    val dataKeys = listOf("love", "loyalty", "security", "proactivity", "jealousy")
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.2f)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2, size.height / 2)
            val radius = size.minDimension / 2.5f
            
            // Draw background pentagon lines (5 levels)
            for (i in 1..5) {
                val r = radius * (i / 5f)
                val path = Path()
                for (j in 0 until 5) {
                    val angle = Math.toRadians(j * 72.0 - 90.0)
                    val x = center.x + r * cos(angle).toFloat()
                    val y = center.y + r * sin(angle).toFloat()
                    if (j == 0) path.moveTo(x, y) else path.lineTo(x, y)
                }
                path.close()
                drawPath(path, Color.LightGray.copy(alpha = 0.3f), style = Stroke(width = 1.dp.toPx()))
            }
            
            // Draw axis lines
            for (j in 0 until 5) {
                val angle = Math.toRadians(j * 72.0 - 90.0)
                val x = center.x + radius * cos(angle).toFloat()
                val y = center.y + radius * sin(angle).toFloat()
                drawLine(Color.LightGray.copy(alpha = 0.3f), center, Offset(x, y), strokeWidth = 1.dp.toPx())
            }
            
            // Draw data polygon
            val dataPath = Path()
            for (j in 0 until 5) {
                val score = scores[dataKeys[j]] ?: 50
                val r = radius * (score / 100f)
                val angle = Math.toRadians(j * 72.0 - 90.0)
                val x = center.x + r * cos(angle).toFloat()
                val y = center.y + r * sin(angle).toFloat()
                if (j == 0) dataPath.moveTo(x, y) else dataPath.lineTo(x, y)
            }
            dataPath.close()
            drawPath(dataPath, Color(0xFFFF80AB).copy(alpha = 0.3f), style = Fill)
            drawPath(dataPath, Color(0xFFFF80AB), style = Stroke(width = 2.dp.toPx()))
        }
        
        // Simplified labels display around the chart
        Box(modifier = Modifier.fillMaxSize()) {
            Text(labels[0], modifier = Modifier.align(Alignment.TopCenter).padding(top = 0.dp), fontSize = 10.sp, color = SubtitleColor)
            Text(labels[1], modifier = Modifier.align(Alignment.CenterEnd).padding(end = 0.dp), fontSize = 10.sp, color = SubtitleColor)
            Text(labels[2], modifier = Modifier.align(Alignment.BottomEnd).padding(bottom = 0.dp, end = 20.dp), fontSize = 10.sp, color = SubtitleColor)
            Text(labels[3], modifier = Modifier.align(Alignment.BottomStart).padding(bottom = 0.dp, start = 20.dp), fontSize = 10.sp, color = SubtitleColor)
            Text(labels[4], modifier = Modifier.align(Alignment.CenterStart).padding(start = 0.dp), fontSize = 10.sp, color = SubtitleColor)
        }
    }
}

@Composable
fun MatchingShindanScreen(viewModel: MatchingViewModel, soundManager: SoundManager, onBack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(BackgroundStart, BackgroundEnd)
                )
            )
    ) {
        // Background Decoration Layer
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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 48.dp, start = 16.dp, end = 16.dp, bottom = 8.dp)
                ) {
                    Text(
                        text = "二人で相性チェック",
                        style = MaterialTheme.typography.titleLarge.copy(fontFamily = roundedFontFamily),
                        fontWeight = FontWeight.Bold,
                        color = TitleColor,
                        modifier = Modifier.align(Alignment.Center)
                    )
                    if (!viewModel.isFinished) {
                        Box(modifier = Modifier.align(Alignment.CenterEnd)) {
                            AnimatedTextButton(text = "やめる", onClick = onBack)
                        }
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
                if (!viewModel.isFinished && viewModel.questions.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "質問 ${viewModel.currentQuestionIndex + 1} / ${viewModel.questions.size}",
                                style = MaterialTheme.typography.labelLarge,
                                color = TitleColor,
                                fontFamily = roundedFontFamily
                            )
                            Text(text = "🚩", fontSize = 16.sp)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        val progress = viewModel.currentQuestionIndex.toFloat() / viewModel.questions.size.toFloat()
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp)
                                .background(Color.White.copy(alpha = 0.5f), CircleShape)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(progress)
                                    .fillMaxSize()
                                    .background(
                                        brush = Brush.horizontalGradient(
                                            colors = listOf(Color(0xFF9593E5), Color(0xFF6A67CE))
                                        ),
                                        shape = CircleShape
                                    )
                            )
                        }
                    }
                }

                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    when {
                        viewModel.isFinished -> MatchingResultScreen(viewModel, soundManager, onBack)
                        viewModel.questions.isNotEmpty() -> MatchingQuestionScreen(viewModel, soundManager)
                        else -> Text("準備中...", color = TitleColor)
                    }
                }
            }
        }
    }
}

@Composable
fun MatchingQuestionScreen(viewModel: MatchingViewModel, soundManager: SoundManager) {
    val question = viewModel.questions.getOrNull(viewModel.currentQuestionIndex) ?: return
    val currentPlayer = if (!viewModel.isPlayer2Turn) viewModel.player1Name else viewModel.player2Name
    val context = LocalContext.current
    val activity = context as? Activity
    val scrollState = rememberScrollState()
    val scope = androidx.compose.runtime.rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Player Turn Indicator
        Surface(
            color = if (!viewModel.isPlayer2Turn) MaterialTheme.colorScheme.primary else Color(0xFFFF80AB),
            shape = CircleShape,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text(
                text = "${currentPlayer}の番です",
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                color = Color.White,
                style = MaterialTheme.typography.titleMedium.copy(fontFamily = roundedFontFamily),
                fontWeight = FontWeight.Bold
            )
        }
        Text(
            text = "相手に見えないように選んでね！",
            style = MaterialTheme.typography.labelMedium,
            color = SubtitleColor,
            fontFamily = roundedFontFamily
        )
        
        Spacer(modifier = Modifier.height(24.dp))

        // Question Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(32.dp)
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                // Decorations
                Box(modifier = Modifier.matchParentSize()) {
                    val p = Color(0xFFFF80AB).copy(alpha = 0.3f) // Pink (Visible alpha)
                    val v = Color(0xFF6A67CE).copy(alpha = 0.3f) // Violet
                    
                    // 8 scattered sparkles spread out across the top and sides
                    Text("✧", color = p, fontSize = 16.sp, modifier = Modifier.padding(start = 20.dp, top = 20.dp))
                    Text("✧", color = v, fontSize = 12.sp, modifier = Modifier.padding(start = 70.dp, top = 65.dp))
                    Text("✧", color = p, fontSize = 20.sp, modifier = Modifier.align(Alignment.TopEnd).padding(end = 30.dp, top = 30.dp))
                    Text("✧", color = v, fontSize = 18.sp, modifier = Modifier.align(Alignment.TopStart).padding(start = 15.dp, top = 100.dp))
                    Text("✧", color = p, fontSize = 14.sp, modifier = Modifier.align(Alignment.TopEnd).padding(end = 15.dp, top = 110.dp))
                    Text("✧", color = v, fontSize = 16.sp, modifier = Modifier.align(Alignment.TopStart).padding(start = 40.dp, top = 85.dp))
                    Text("✧", color = p, fontSize = 14.sp, modifier = Modifier.align(Alignment.TopCenter).padding(top = 15.dp))
                    Text("✧", color = v, fontSize = 14.sp, modifier = Modifier.align(Alignment.TopEnd).padding(end = 80.dp, top = 60.dp))
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 48.dp, horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Soft Circle with Heart
                    Box(contentAlignment = Alignment.Center) {
                        Box(modifier = Modifier.size(56.dp).background(Color(0xFFFFEBF2), CircleShape))
                        Text(text = "💕", fontSize = 24.sp)
                    }
                    
                    Spacer(modifier = Modifier.height(28.dp))
                    
                    Text(
                        text = question.text,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontFamily = roundedFontFamily,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 38.sp
                        ),
                        color = TitleColor,
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
                onClick = { 
                    soundManager.playClickSound()
                    val isLast = viewModel.currentQuestionIndex == viewModel.questions.size - 1
                    val isLastTurn = viewModel.isPlayer2Turn
                    
                    // 次の質問（または次のプレイヤー）のために一番上へスクロール
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
            Spacer(modifier = Modifier.height(16.dp))
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

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
        Box(modifier = Modifier.fillMaxWidth().padding(top = 16.dp, start = 16.dp, end = 16.dp)) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("✨", fontSize = 16.sp)
                    Text(
                        text = "診断結果",
                        style = MaterialTheme.typography.titleLarge.copy(fontFamily = roundedFontFamily),
                        fontWeight = FontWeight.Bold,
                        color = TitleColor,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    Text(text = "✨", fontSize = 18.sp, color = Color(0xFFFF80AB))
                }
                Text(
                    text = "おふたりの相性を診断しました♡",
                    style = MaterialTheme.typography.labelMedium,
                    color = SubtitleColor
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Main Score Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(32.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Ribbon
                Surface(
                    color = Color(0xFFFFC1E3), // Soft Pink
                    shape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp),
                    modifier = Modifier.width(100.dp)
                ) {
                    Text(
                        text = "診断結果",
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Heart Score
                Box(contentAlignment = Alignment.Center, modifier = Modifier.size(180.dp)) {
                    Text(
                        text = "❤",
                        fontSize = 150.sp,
                        color = Color(0xFFFF80AB).copy(alpha = 0.8f),
                        modifier = Modifier.graphicsLayer {
                            scaleX = 1.3f
                            scaleY = 1.2f
                        }
                    )
                    Text(
                        text = "$score%",
                        style = MaterialTheme.typography.displayLarge.copy(
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = feedbackText,
                    style = MaterialTheme.typography.titleMedium.copy(fontFamily = roundedFontFamily),
                    fontWeight = FontWeight.Bold,
                    color = TitleColor
                )
                
                Text(
                    text = "お互いの違いを理解し合うことで、\nもっと素敵な関係になれます♡",
                    style = MaterialTheme.typography.bodySmall,
                    color = SubtitleColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Category Cards
        Column(
            modifier = Modifier.padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
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

        Spacer(modifier = Modifier.height(32.dp))

        // Bottom Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
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
                shape = RoundedCornerShape(24.dp),
                contentPadding = PaddingValues(horizontal = 8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🔄", fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("最初から診断する", color = Color(0xFF7E7CCF), fontSize = 12.sp, fontWeight = FontWeight.Bold)
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
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7E7CCF)),
                shape = RoundedCornerShape(24.dp),
                contentPadding = PaddingValues(horizontal = 8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🏠", fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("メニューに戻る", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
        
        Spacer(modifier = Modifier.height(64.dp))
    }
}

@Composable
fun MatchingCategoryCard(
    title: String,
    items: List<String>,
    baseColor: Color,
    accentColor: Color,
    icon: String,
    count: Int,
    footer: String? = null
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = baseColor),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icon Circle
                Surface(
                    color = Color.White,
                    shape = CircleShape,
                    modifier = Modifier.size(44.dp),
                    shadowElevation = 1.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = icon, fontSize = 22.sp)
                    }
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleSmall.copy(fontFamily = roundedFontFamily),
                            fontWeight = FontWeight.Bold,
                            color = accentColor
                        )
                        Surface(
                            color = Color.White.copy(alpha = 0.6f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                text = "${count}項目",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = accentColor,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    HorizontalDivider(
                        modifier = Modifier.padding(top = 4.dp),
                        thickness = 1.dp,
                        color = accentColor.copy(alpha = 0.2f)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            items.forEach { item ->
                Row(
                    modifier = Modifier.padding(start = 56.dp, bottom = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.size(6.dp).background(accentColor, CircleShape))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = item,
                        style = MaterialTheme.typography.bodySmall,
                        color = TitleColor.copy(alpha = 0.8f)
                    )
                }
            }
            
            if (footer != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    color = Color.White.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth().padding(start = 44.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("❤", fontSize = 12.sp, color = Color(0xFFFF80AB))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = footer,
                            style = MaterialTheme.typography.labelSmall,
                            color = accentColor.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    LoveSyncTheme {
        MainContainer()
    }
}
