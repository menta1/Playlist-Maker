package com.example.playlistmaker

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate

const val SHARED_PREF = "shared_pref"
const val KEY_THEME = "prefs_theme"
const val LIGHT_THEME = 0
const val DARK_THEME = 1
const val KEY_SEARCH_HISTORY = "search_history"

class App : Application() {

    private val sharedPrefs: SharedPreferences by lazy {
        getSharedPreferences(
            SHARED_PREF,
            Context.MODE_PRIVATE
        )
    }


    override fun onCreate() {
        super.onCreate()
        initTheme()

    }

    fun initTheme(): Boolean {
        if (!sharedPrefs.contains(KEY_THEME)) {
            when (resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                Configuration.UI_MODE_NIGHT_YES -> {
                    sharedPrefs.edit().putInt(KEY_THEME, DARK_THEME).apply()
                    return true
                }
                Configuration.UI_MODE_NIGHT_NO -> {
                    sharedPrefs.edit().putInt(KEY_THEME, LIGHT_THEME).apply()
                    return false
                }
            }
        } else {
            when (sharedPrefs.getInt(KEY_THEME, -1)) {
                0 -> {

                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    return false
                }
                1 -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    return true
                }
            }
        }
        return false
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                sharedPrefs.edit().putInt(KEY_THEME, DARK_THEME).apply()
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                sharedPrefs.edit().putInt(KEY_THEME, LIGHT_THEME).apply()
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

}
