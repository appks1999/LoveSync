package com.akito.lovesync

import android.content.Context

class SettingsManager(context: Context) {
    private val prefs = context.getSharedPreferences("lovesync_settings", Context.MODE_PRIVATE)

    var isBgmEnabled: Boolean
        get() = prefs.getBoolean("bgm_enabled", true)
        set(value) = prefs.edit().putBoolean("bgm_enabled", value).apply()

    var isSeEnabled: Boolean
        get() = prefs.getBoolean("se_enabled", true)
        set(value) = prefs.edit().putBoolean("se_enabled", value).apply()
}
