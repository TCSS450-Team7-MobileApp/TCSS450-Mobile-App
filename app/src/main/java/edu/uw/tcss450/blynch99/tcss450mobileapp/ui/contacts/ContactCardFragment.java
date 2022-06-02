package edu.uw.tcss450.blynch99.tcss450mobileapp.ui.contacts;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import edu.uw.tcss450.blynch99.tcss450mobileapp.databinding.FragmentContactCardBinding;

/**
 * create an instance of this fragment.
 */
public class ContactCardFragment extends Fragment {

    FragmentContactCardBinding mBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentContactCardBinding.inflate(inflater);

        return mBinding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getView().setOnClickListener(button -> showButtons());

    }

    private void showButtons(){
        String status = (String) mBinding.buttonFriendManager.getText();
        Log.d("theID", status);
        switch (status){
            case "FRIENDS":
                mBinding.buttonFriendRemove.setVisibility(View.VISIBLE);
                mBinding.buttonFriendManager.setVisibility(View.VISIBLE);
                break;

        }
    }
}