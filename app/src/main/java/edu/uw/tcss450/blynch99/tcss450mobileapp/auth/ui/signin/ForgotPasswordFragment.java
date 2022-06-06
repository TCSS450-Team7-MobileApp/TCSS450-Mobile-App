package edu.uw.tcss450.blynch99.tcss450mobileapp.auth.ui.signin;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.tcss450.blynch99.tcss450mobileapp.databinding.FragmentForgotPasswordBinding;

/**
 * Fragment for forgot password
 * A simple {@link Fragment} subclass.
 */
public class ForgotPasswordFragment extends Fragment {

    private FragmentForgotPasswordBinding mBinding;

    private ForgotPasswordViewModel mModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mModel = new ViewModelProvider(getActivity()).get(ForgotPasswordViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentForgotPasswordBinding.inflate(inflater);

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ForgotPasswordFragmentArgs args = ForgotPasswordFragmentArgs.fromBundle(getArguments());

        mModel.addResponseObserver(getViewLifecycleOwner(),
                this::observeResponse);

        mBinding.editEmail.setText(args.getEmail().equals("default") ? "" : args.getEmail());
        mBinding.buttonBackToSignin.setOnClickListener(button -> {
            mModel.connect(
                    mBinding.editEmail.getText().toString());
            navigateToSignIn(mBinding.editEmail.getText().toString());
        });

    }

    /**
     * Navigate back to sign in
     * @param email email of the user
     */
    private void navigateToSignIn(final String email){
        Log.d("CLICK", "Click happened");
        ForgotPasswordFragmentDirections.ActionForgotPasswordFragmentToSignInFragment directions =
                ForgotPasswordFragmentDirections.actionForgotPasswordFragmentToSignInFragment();

        directions.setEmail(email);

        Navigation.findNavController(getView()).navigate(directions);

    }

    /**
     * Observe the response from the server
     * @param response the response from the server
     */
    private void observeResponse(final JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
                try {
                    mBinding.editEmail.setError(
                            "Error Authenticating: " +
                                    response.getJSONObject("data").getString("message"));
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                navigateToSignIn(mBinding.editEmail.getText().toString());
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }
}