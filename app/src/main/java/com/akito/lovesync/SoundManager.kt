package com.akito.lovesync

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool

class SoundManager(context: Context) {
    private val soundPool: SoundPool
    private val clickSoundId: Int
    private val click2SoundId: Int
    private val resultSoundId: Int
    private val settingsManager = SettingsManager(context)

    init {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(10)
            .setAudioAttributes(audioAttributes)
            .build()

        // 音源ファイルをロード
        clickSoundId = soundPool.load(context, R.raw.click, 1)
        click2SoundId = soundPool.load(context, R.raw.click2, 1)
        resultSoundId = soundPool.load(context, R.raw.result, 1)
    }

    fun playClickSound() {
        if (!settingsManager.isSeEnabled) return
        soundPool.play(clickSoundId, 1.0f, 1.0f, 1, 0, 1.0f)
    }

    fun playClick2Sound() {
        if (!settingsManager.isSeEnabled) return
        soundPool.play(click2SoundId, 1.0f, 1.0f, 1, 0, 1.0f)
    }

    fun playResultSound() {
        if (!settingsManager.isSeEnabled) return
        soundPool.play(resultSoundId, 1.0f, 1.0f, 1, 0, 1.0f)
    }

    fun release() {
        soundPool.release()
    }
}
