package com.android.certification.niap.permission.dpctester.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.certification.niap.permission.dpctester.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title="Test Suite Settings"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}