package com.arifahmadalfian.consumergithub

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.preference.CheckBoxPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

class PreferenceFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private var alarmReceiver: AlarmReceiver? = null
    private lateinit var REMINDER: String
    private lateinit var LEANGUAGE: String

    private lateinit var isReminderTrue: CheckBoxPreference
    private lateinit var leanguage: Preference

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
        init()
        setSummaries()

        val preference: Preference? = findPreference(LEANGUAGE)
        preference?.setOnPreferenceClickListener {
            getLeanguage()
            true
        }

        alarmReceiver = AlarmReceiver()
    }

    private fun setSummaries() {
        val sh = preferenceManager.sharedPreferences
        isReminderTrue.isChecked = sh.getBoolean(REMINDER, false)
    }

    private fun init() {
        REMINDER = resources.getString(R.string.key_reminder)
        LEANGUAGE = resources.getString(R.string.key_bahasa)

        isReminderTrue = findPreference<CheckBoxPreference>(REMINDER) as CheckBoxPreference
        leanguage = findPreference<Preference>(LEANGUAGE) as Preference
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String?) {
        if (key == REMINDER) {
            isReminderTrue.isChecked = sharedPreferences.getBoolean(REMINDER, false)

            val result = sharedPreferences.getBoolean(REMINDER, false)
            val on = getString(R.string.pengingat_aktif)
            val off = getString(R.string.pengingat_mati)

            if (result) {
                getOnRepeating()
                Toast.makeText(activity, on, Toast.LENGTH_SHORT).show()
            } else {
                getOffRepeating()
                Toast.makeText(activity, off, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getOffRepeating() {
        context?.let { alarmReceiver?.cancelAlarm(it) }
    }

    private fun getOnRepeating() {
        context?.let { alarmReceiver?.setRepeatingAlarm(it) }
    }

    private fun getLeanguage() {
        val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
        startActivity(mIntent)
    }

}