package edu.uw.tcss450.blynch99.tcss450mobileapp.auth.ui.register;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import edu.uw.tcss450.blynch99.tcss450mobileapp.databinding.FragmentEmailVerificationBinding;

/**
 * Fragment for email verification
 *
 * A simple {@link Fragment} subclass.
 */
public class EmailVerificationFragment extends Fragment {

    FragmentEmailVerificationBinding mBinding;
    EmailVerificationFragmentArgs mArgs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentEmailVerificationBinding.inflate(inflater);
        mArgs = EmailVerificationFragmentArgs.fromBundle(getArguments());
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.buttonVerified.setOnClickListener(button -> navigateToLogin());
    }

    /**
     * Navigate the user back to login
     */
    private void navigateToLogin() {
        EmailVerificationFragmentDirections.ActionEmailVerificationFragment2ToSignInFragment directions =
                EmailVerificationFragmentDirections.actionEmailVerificationFragment2ToSignInFragment();
        directions.setEmail(mArgs.getEmail());
        directions.setPassword(mArgs.getPassword());
        Navigation.findNavController(getView()).navigate(directions);
    }
}