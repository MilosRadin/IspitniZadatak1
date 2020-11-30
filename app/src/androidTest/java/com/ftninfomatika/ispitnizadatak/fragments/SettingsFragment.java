package com.ftninfomatika.ispitnizadatak.fragments;

import android.os.Bundle;

import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.ftninfomatika.ispitnizadatak.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    public static final String tehnicki = "show_details";
    public static final String ucesnici = "show_actors_details";

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.root_preferences);
        findPreference(tehnicki).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                return onChange(preference, newValue);
            }
        });

        findPreference(ucesnici).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                return onChange(preference, newValue);
            }
        });
    }

    public boolean onChange(Preference preference, Object newValue) {
        String key = preference.getKey();
        if (key.equals(tehnicki)) {
            //Reset other items
            CheckBoxPreference p = (CheckBoxPreference)findPreference(ucesnici);
            p.setChecked(false);
        }
        else if (key.equals(ucesnici)) {
            //Reset other items
            CheckBoxPreference p = (CheckBoxPreference)findPreference(tehnicki);
            p.setChecked(false);
        }

        return (Boolean)newValue;
    }
}