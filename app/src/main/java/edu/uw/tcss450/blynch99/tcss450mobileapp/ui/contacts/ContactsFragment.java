package edu.uw.tcss450.blynch99.tcss450mobileapp.ui.contacts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import edu.uw.tcss450.blynch99.tcss450mobileapp.databinding.FragmentContactsBinding;

/**
 * create an instance of this fragment.
 */
public class ContactsFragment extends Fragment {
    FragmentContactsBinding mBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentContactsBinding.inflate(inflater);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.friendbutton.setOnClickListener(button -> navigateToFriend());
    }

    public void navigateToFriend(){
        Navigation.findNavController(getView())
                .navigate(ContactsFragmentDirections
                        .actionContactsFragmentToFriendFragment(
                                "ftest",
                                "ltest",
                                "nicktest",
                                FriendStatus.SENT_REQUEST,
                                "gmn"
                        ));
    }
}