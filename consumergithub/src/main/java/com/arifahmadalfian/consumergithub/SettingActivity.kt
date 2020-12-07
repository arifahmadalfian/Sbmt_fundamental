package com.arifahmadalfian.consumergithub

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        supportActionBar?.title = "Setting"
        supportFragmentManager.beginTransaction().add(R.id.setting_holder, PreferenceFragment()).commit()
    }
}