package edu.uw.tcss450.blynch99.tcss450mobileapp.ui.settings;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import edu.uw.tcss450.blynch99.tcss450mobileapp.AuthActivity;
import edu.uw.tcss450.blynch99.tcss450mobileapp.MainActivity;
import edu.uw.tcss450.blynch99.tcss450mobileapp.R;
import edu.uw.tcss450.blynch99.tcss450mobileapp.auth.model.UserInfoViewModel;

public class SettingsFragment extends PreferenceFragmentCompat {
    private UserInfoViewModel model;

    @SuppressLint("ResourceType")
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

        model = new ViewModelProvider((ViewModelStoreOwner) MainActivity.getActivity())
                .get(UserInfoViewModel.class);

        EditTextPreference fname = findPreference("preference_first_name");
        assert fname != null;
        fname.setOnPreferenceChangeListener(this::changeName);
        fname.setText(model.getFirst());
        fname.setSummary(model.getFirst());

        EditTextPreference lname = findPreference("preference_last_name");
        assert lname != null;
        lname.setOnPreferenceChangeListener(this::changeName);
        lname.setText(model.getLast());
        lname.setSummary(model.getLast());

        EditTextPreference nname = findPreference("preference_nickname");
        assert nname != null;
        nname.setOnPreferenceChangeListener(this::changeName);
        nname.setText(model.getNick());
        nname.setSummary(model.getNick());

        findPreference("preference_logout").
                setOnPreferenceClickListener(this::navigateToLogout);

        EditTextPreference deleteAccount = findPreference("preference_delete_account");
        assert deleteAccount != null;
        deleteAccount.setOnPreferenceClickListener(preference -> {
            deleteAccount.setText("");
            deleteAccount.setSummary("");
            deleteAccount.setDialogMessage(getString(R.string.text_delete_account)
                    + " \n(Email is: " + model.getEmail() + ")");
            return true;
        });
        deleteAccount.setOnPreferenceChangeListener(this::promptForPassword);


    }

    private boolean changeName(Preference preference, Object object){
        ChangeNameViewModel change = new ViewModelProvider(getActivity())
                .get(ChangeNameViewModel.class);
        String str = (String) object;
        change.connect(str, preference.getTitle().toString());
        return true;
    }

    private boolean navigateToLogout(Preference preference){
        startActivity(new Intent(getActivity(), AuthActivity.class));
        MainActivity.getActivity().finish();
        getActivity().finish();
        return true;
    }

    private boolean promptForPassword(Preference preference, Object str){
        if (model.getEmail().equals(str)){
            DeleteAccountViewModel delete = new ViewModelProvider(
                    getActivity()).get(DeleteAccountViewModel.class);
            delete.connect(model.getEmail());
            navigateToLogout(preference);
        }
        else{
            preference.setSummary("Correct Email was not entered");
        }

        return true;
    }


}