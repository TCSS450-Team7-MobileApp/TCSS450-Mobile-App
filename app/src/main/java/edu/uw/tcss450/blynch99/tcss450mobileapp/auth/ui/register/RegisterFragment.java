package edu.uw.tcss450.blynch99.tcss450mobileapp.auth.ui.register;

import static edu.uw.tcss450.blynch99.tcss450mobileapp.auth.utils.PasswordValidator.checkClientPredicate;
import static edu.uw.tcss450.blynch99.tcss450mobileapp.auth.utils.PasswordValidator.checkExcludeWhiteSpace;
import static edu.uw.tcss450.blynch99.tcss450mobileapp.auth.utils.PasswordValidator.checkPwdDigit;
import static edu.uw.tcss450.blynch99.tcss450mobileapp.auth.utils.PasswordValidator.checkPwdLength;
import static edu.uw.tcss450.blynch99.tcss450mobileapp.auth.utils.PasswordValidator.checkPwdLowerCase;
import static edu.uw.tcss450.blynch99.tcss450mobileapp.auth.utils.PasswordValidator.checkPwdSpecialChar;
import static edu.uw.tcss450.blynch99.tcss450mobileapp.auth.utils.PasswordValidator.checkPwdUpperCase;

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

import edu.uw.tcss450.blynch99.tcss450mobileapp.auth.utils.PasswordValidator;
import edu.uw.tcss450.blynch99.tcss450mobileapp.databinding.FragmentRegisterBinding;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;

    private RegisterViewModel mRegisterModel;

    private PasswordValidator mNickValidator = checkPwdLength(1);

    private PasswordValidator mNameValidator = checkPwdLength(0);


    private PasswordValidator mEmailValidator = checkPwdLength(2)
            .and(checkExcludeWhiteSpace())
            .and(checkPwdSpecialChar("@"))
            .and(checkPwdSpecialChar("."));

    private PasswordValidator mPassWordValidator =
            checkClientPredicate(pwd -> pwd.equals(binding.editPassword2.getText().toString()))
                    .and(checkPwdLength(7))
                    .and(checkPwdSpecialChar())
                    .and(checkExcludeWhiteSpace())
                    .and(checkPwdDigit())
                    .and(checkPwdLowerCase())
                    .and(checkPwdUpperCase());

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRegisterModel = new ViewModelProvider(getActivity())
                .get(RegisterViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonRegister.setOnClickListener(this::attemptRegister);
        mRegisterModel.addResponseObserver(getViewLifecycleOwner(),
                this::observeResponse);
    }

    private void attemptRegister(final View button) {
        validateNick();
    }

    private void validateNick() {
        mNameValidator.processResult(
                mNickValidator.apply(binding.editNick.getText().toString().trim()),
                this::validateFirst,
                result -> binding.editNick.setError("Please enter a nickname with at least 2 characters."));
    }

    private void validateFirst() {
        mNameValidator.processResult(
                mNameValidator.apply(binding.editFirst.getText().toString().trim()),
                this::validateLast,
                result -> binding.editFirst.setError("Please enter a first name with at least 1 character."));
    }

    private void validateLast() {
        mNickValidator.processResult(
                mNickValidator.apply(binding.editLast.getText().toString().trim()),
                this::validateEmail,
                result -> binding.editLast.setError("Please enter a last name with at least 2 characters."));
    }

    private void validateEmail() {
        mEmailValidator.processResult(
                mEmailValidator.apply(binding.editEmail.getText().toString().trim()),
                this::validatePasswordsMatch,
                result -> binding.editEmail.setError("Please enter a valid Email address."));
    }

    private void validatePasswordsMatch() {
        PasswordValidator matchValidator =
                checkClientPredicate(
                        pwd -> pwd.equals(binding.editPassword2.getText().toString().trim()));

        mEmailValidator.processResult(
                matchValidator.apply(binding.editPassword1.getText().toString().trim()),
                this::validatePassword,
                result -> binding.editPassword1.setError("Passwords must match."));
    }

    private void validatePassword() {
        mPassWordValidator.processResult(
                mPassWordValidator.apply(binding.editPassword1.getText().toString()),
                this::verifyAuthWithServer,
                result -> binding.editPassword1.setError("Password must be at least 7 characters," +
                        " contain at least 1 letter, 1 special character, and 1 digit."));
    }

    private void verifyAuthWithServer() {
        binding.layoutWait.setVisibility(View.VISIBLE);
        mRegisterModel.connect(
                binding.editFirst.getText().toString(),
                binding.editLast.getText().toString(),
                binding.editEmail.getText().toString(),
                binding.editPassword1.getText().toString());
        //This is an Asynchronous call. No statements after should rely on the
        //result of connect().
    }

    private void navigateToVerification() {
        RegisterFragmentDirections.ActionRegisterFragmentToEmailVerificationFragment directions =
                RegisterFragmentDirections.actionRegisterFragmentToEmailVerificationFragment(
                        binding.editEmail.getText().toString(),
                        binding.editPassword1.getText().toString());

        Navigation.findNavController(getView()).navigate(directions);

    }

    /**
     * An observer on the HTTP Response from the web server. This observer should be
     * attached to SignInViewModel.
     *
     * @param response the Response from the server
     */
    private void observeResponse(final JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
                try {
                    binding.editEmail.setError(
                            "Error Authenticating: " +
                                    response.getJSONObject("data").getString("message"));
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                navigateToVerification();
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
        binding.layoutWait.setVisibility(View.GONE);
    }
}
