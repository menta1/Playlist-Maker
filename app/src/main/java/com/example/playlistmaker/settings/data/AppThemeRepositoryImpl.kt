package com.example.playlistmaker.settings.data

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.settings.domain.AppThemeRepository
import com.example.playlistmaker.util.Constants.DARK_THEME
import com.example.playlistmaker.util.Constants.KEY_THEME
import com.example.playlistmaker.util.Constants.LIGHT_THEME

class AppThemeRepositoryImpl(
    private val context: Context,
    private var sharedPrefs: SharedPreferences
) : AppThemeRepository {
    companion object {

    }

    init {
        initTheme()
    }

    override fun switchTheme(darkThemeEnabled: Boolean) {
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

    override fun initTheme(): Boolean {
        return if (!sharedPrefs.contains(KEY_THEME)) {
            defineTheme()
        } else {
            installSavedTheme()
        }
    }

    private fun defineTheme(): Boolean {
        return when (context.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
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
}
