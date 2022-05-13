package edu.uw.tcss450.blynch99.tcss450mobileapp.ui.contacts;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import edu.uw.tcss450.blynch99.tcss450mobileapp.R;
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

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.textNickname.setText(mArgs.getNickname());
        mBinding.textName.setText(mArgs.getFirstName() + " " + mArgs.getLastName());
        updateFragment(mArgs.getFriendStatus());
    }

    private void updateFragment(FriendStatus status){
        mBinding.buttonFriendManager.setVisibility(View.GONE);
        mBinding.buttonFriendRemove.setVisibility(View.GONE);
        mBinding.buttonMessage.setVisibility(View.GONE);
        switch (status){
            case FRIENDS:
                mBinding.buttonMessage.setVisibility(View.VISIBLE);
                mBinding.buttonFriendRemove.setVisibility(View.VISIBLE);
                mBinding.buttonFriendRemove.setOnClickListener(button -> updateFragment(FriendStatus.NOT_FRIENDS));
                break;
            case NOT_FRIENDS:
                mBinding.buttonFriendManager.setVisibility(View.VISIBLE);
                mBinding.buttonFriendManager.setText(getString(R.string.action_friend_send_request));
                mBinding.buttonFriendManager.setOnClickListener(button -> updateFragment(FriendStatus.SENT_REQUEST));
                break;
            case SENT_REQUEST:
                mBinding.buttonFriendManager.setVisibility(View.VISIBLE);
                mBinding.buttonFriendManager.setText(getString(R.string.action_friend_unsend_request));
                mBinding.buttonFriendManager.setOnClickListener(button -> updateFragment(FriendStatus.NOT_FRIENDS));
                break;
            case RECEIVED_REQUEST:
                mBinding.buttonFriendManager.setVisibility(View.VISIBLE);
                mBinding.buttonFriendManager.setText(getString(R.string.action_friend_accept_request));
                mBinding.buttonFriendManager.setOnClickListener(button -> updateFragment(FriendStatus.FRIENDS));
                break;
        }
    }
}