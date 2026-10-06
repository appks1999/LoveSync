package com.akito.lovesync

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.akito.lovesync.data.LoveShindanContent
import com.akito.lovesync.ui.components.BannerAdView
import com.akito.lovesync.ui.home.ModeSelectionScreen
import com.akito.lovesync.ui.matching.MatchingShindanScreen
import com.akito.lovesync.ui.solo.SoloShindanScreen
import com.akito.lovesync.ui.theme.LoveSyncTheme
import com.google.android.gms.ads.MobileAds

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

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    LoveSyncTheme {
        MainContainer()
    }
}
