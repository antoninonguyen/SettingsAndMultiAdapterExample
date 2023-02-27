package com.example.settingsdemo.childfragments;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.example.settingsdemo.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }
}