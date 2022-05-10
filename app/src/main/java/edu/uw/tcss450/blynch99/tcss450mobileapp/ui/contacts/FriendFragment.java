package edu.uw.tcss450.blynch99.tcss450mobileapp.ui.contacts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import edu.uw.tcss450.blynch99.tcss450mobileapp.databinding.FragmentFriendBinding;

/**
 * create an instance of this fragment.
 */
public class FriendFragment extends Fragment {
    FragmentFriendBinding mBinding;
    FriendFragmentArgs mArgs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentFriendBinding.inflate(inflater);
        mArgs = FriendFragmentArgs.fromBundle(getArguments());

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.textNickname.setText(mArgs.getNickname());
        mBinding.textName.setText(mArgs.getFirstName() + " " + mArgs.getLastName());



    }
}