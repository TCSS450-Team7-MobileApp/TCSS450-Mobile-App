package edu.uw.tcss450.blynch99.tcss450mobileapp.ui.contacts;



import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import edu.uw.tcss450.blynch99.tcss450mobileapp.databinding.FragmentContactsBinding;

/**
 * create an instance of this fragment.
 */
public class ContactsFragment extends Fragment {
    FragmentContactsBinding mBinding;
    RecyclerView mRecyclerView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentContactsBinding.inflate(inflater);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Contact[] contacts = new Contact[20];

        for(int i = 0; i < contacts.length; i++){
            contacts[i] = new Contact("Billia" + i,"Ilya" + i,"Koz" + i,"ilya@koz"+i,FriendStatus.FRIENDS);
        }

        mRecyclerView = mBinding.listContactsRoot;
        ContactRecyclerViewAdapter rcAdapter = new ContactRecyclerViewAdapter(getActivity(),contacts);
        mRecyclerView.setAdapter(rcAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


    }



    public void navigateToFriend(){
        /*
        Navigation.findNavController(getView())
                .navigate(ContactsFragmentDirections
                        .actionContactsFragmentToFriendFragment (
                                "ftest",
                                "ltest",
                                "nicktest",
                                FriendStatus.RECEIVED_REQUEST,
                                "gmn"
                        ));

         */
    }
}