package edu.uw.tcss450.blynch99.tcss450mobileapp.ui.settings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import edu.uw.tcss450.blynch99.tcss450mobileapp.AuthActivity;
import edu.uw.tcss450.blynch99.tcss450mobileapp.MainActivity;
import edu.uw.tcss450.blynch99.tcss450mobileapp.R;
import edu.uw.tcss450.blynch99.tcss450mobileapp.auth.model.UserInfoViewModel;

/**
 * A simple {@link PreferenceFragmentCompat} subclass.
 */
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

    /**
     * Change name of user
     * @param preference which preference was clicked
     * @param object string of the new name
     * @return success
     */
    private boolean changeName(Preference preference, Object object){
        ChangeNameViewModel change = new ViewModelProvider(getActivity())
                .get(ChangeNameViewModel.class);
        String str = (String) object;
        change.connect(str, preference.getTitle().toString(), model.getJwt(), model.getId());
        preference.setSummary(str);

        switch (preference.getTitle().toString()){
            case "Change First Name":
                model.setFirst(str);
                break;
            case "Change Last Name":
                model.setLast(str);
                break;
            case "Change Nickname":
                model.setNick(str);
                break;

        }

        return true;
    }

    private boolean navigateToLogout(Preference preference){
        startActivity(new Intent(getActivity(), AuthActivity.class));
        MainActivity.getActivity().finish();

        SharedPreferences prefs =
                MainActivity.getActivity().getSharedPreferences(
                        getString(R.string.keys_shared_prefs),
                        Context.MODE_PRIVATE);
        prefs.edit().remove(getString(R.string.keys_prefs_jwt)).apply();

        getActivity().finish();
        return true;
    }

    private boolean promptForPassword(Preference preference, Object str){
        if (model.getEmail().equals(str)){
            DeleteAccountViewModel delete = new ViewModelProvider(
                    getActivity()).get(DeleteAccountViewModel.class);
            delete.connect(model.getEmail(), model.getJwt());
            navigateToLogout(preference);
        }
        else{
            preference.setSummary("Correct Email was not entered");
        }

        return true;
    }


}