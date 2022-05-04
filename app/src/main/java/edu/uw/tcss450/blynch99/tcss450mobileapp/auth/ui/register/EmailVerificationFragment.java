package edu.uw.tcss450.blynch99.tcss450mobileapp.auth.ui.register;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import org.json.JSONObject;

import edu.uw.tcss450.blynch99.tcss450mobileapp.databinding.FragmentEmailVerificationBinding;

/**
 * create an instance of this fragment.
 */
public class EmailVerificationFragment extends Fragment {

    FragmentEmailVerificationBinding binding;
    EmailVerificationFragmentArgs args;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEmailVerificationBinding.inflate(inflater);
        args = EmailVerificationFragmentArgs.fromBundle(getArguments());
        return binding.getRoot();
    }

    private void observeResponse(final JSONObject response) {
        if (response.length() > 0) {
            Log.d("JSON Response",response.toString());
        } else {
            Log.d("JSON Response", "No Response");
        }

    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.buttonVerified.setOnClickListener(button -> navigateToLogin());




    }

    private void navigateToLogin() {
        EmailVerificationFragmentDirections.ActionEmailVerificationFragment2ToSignInFragment directions =
                EmailVerificationFragmentDirections.actionEmailVerificationFragment2ToSignInFragment();

        directions.setEmail(args.getEmail());
        directions.setPassword(args.getPassword());

        Navigation.findNavController(getView()).navigate(directions);


    }
}