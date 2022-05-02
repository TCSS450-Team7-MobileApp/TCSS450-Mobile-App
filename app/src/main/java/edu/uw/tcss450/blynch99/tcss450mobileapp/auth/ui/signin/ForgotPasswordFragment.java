package edu.uw.tcss450.blynch99.tcss450mobileapp.auth.ui.signin;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import edu.uw.tcss450.blynch99.tcss450mobileapp.databinding.FragmentForgotPasswordBinding;

/**
 * create an instance of this fragment.
 */
public class ForgotPasswordFragment extends Fragment {

    FragmentForgotPasswordBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentForgotPasswordBinding.inflate(inflater);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ForgotPasswordFragmentArgs args = ForgotPasswordFragmentArgs.fromBundle(getArguments());

        binding.editEmail.setText(args.getEmail().equals("default") ? "" : args.getEmail());
        binding.buttonBackToSignin.setOnClickListener(button -> navigateToSignIn(binding.editEmail.getText().toString()));

    }

    private void navigateToSignIn(final String email){
        Log.d("CLICK", "Click happened");
        ForgotPasswordFragmentDirections.ActionForgotPasswordFragmentToSignInFragment directions =
                ForgotPasswordFragmentDirections.actionForgotPasswordFragmentToSignInFragment();

        directions.setEmail(email);

        Navigation.findNavController(getView()).navigate(directions);

    }
}