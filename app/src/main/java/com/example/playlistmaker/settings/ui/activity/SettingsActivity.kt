package com.example.playlistmaker.settings.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.settings.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsActivity : AppCompatActivity() {

    private val viewModel by viewModel<SettingsViewModel>()
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.initTheme()

        viewModel.isCheckedTrue.observe(this) {
            binding.themeSwitcher.isChecked = it
        }

        binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->
            viewModel.switchTheme(checked)
        }

        binding.shareApp.setOnClickListener {
            viewModel.shareAppButton(getString(R.string.share_app_link))
        }
        binding.writeSupport.setOnClickListener {
            viewModel.writeSupportButton(
                email = getString(R.string.extra_text_write_email),
                text = getString(R.string.extra_text_write_support),
                subject = getString(R.string.extra_text_write_subject)
            )
        }
        binding.termsUse.setOnClickListener {
            viewModel.termsUseButton(getString(R.string.terms_use_link))
        }
        binding.backButtonSettingsActivity.setOnClickListener {
            finish()
        }
    }
}