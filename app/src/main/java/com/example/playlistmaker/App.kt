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
        return if (!sharedPrefs.contains(KEY_THEME)) {
            defineTheme()
        } else {
            installSavedTheme()
        }
    }

    private fun defineTheme(): Boolean {
        return when (resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {
                saveTheme(DARK_THEME)
                true
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                saveTheme(LIGHT_THEME)
                false
            }
            else -> false
        }
    }

    private fun saveTheme(key: Int) {
        sharedPrefs.edit().putInt(KEY_THEME, key).apply()
    }

    private fun installSavedTheme(): Boolean {
        return when (sharedPrefs.getInt(KEY_THEME, -1)) {
            LIGHT_THEME -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                false
            }
            DARK_THEME -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                true
            }
            else -> false
        }
    }


    fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                saveTheme(DARK_THEME)
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                saveTheme(LIGHT_THEME)
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

}
