package edu.uw.tcss450.blynch99.tcss450mobileapp.ui.settings;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import edu.uw.tcss450.blynch99.tcss450mobileapp.AuthActivity;
import edu.uw.tcss450.blynch99.tcss450mobileapp.MainActivity;
import edu.uw.tcss450.blynch99.tcss450mobileapp.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @SuppressLint("ResourceType")
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);


        Preference fname = (Preference)findPreference("preference_first_name");
        assert fname != null;
        fname.setOnPreferenceChangeListener((preference, newValue) -> true);

        Preference logout = (Preference)findPreference("preference_logout");
        assert logout != null;
        logout.setOnPreferenceClickListener(preference -> navigateToLogout());
    }

    private boolean navigateToLogout(){
        Log.d("NAV", "work??");
        startActivity(new Intent(getActivity(), AuthActivity.class));
        MainActivity.getActivity().finish();
        getActivity().finish();
        return true;
    }

}