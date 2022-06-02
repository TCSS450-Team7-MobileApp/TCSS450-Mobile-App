package edu.uw.tcss450.blynch99.tcss450mobileapp.ui.contacts;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;

import edu.uw.tcss450.blynch99.tcss450mobileapp.MainActivity;
import edu.uw.tcss450.blynch99.tcss450mobileapp.auth.model.UserInfoViewModel;
import edu.uw.tcss450.blynch99.tcss450mobileapp.databinding.FragmentContactsBinding;

/**
 * create an instance of this fragment.
 */
public class ContactsFragment extends Fragment {
    private FragmentContactsBinding mBinding;
    private RecyclerView mRecyclerView;

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

        mRecyclerView = mBinding.listContactsRoot;

        ContactListViewModel model = new ViewModelProvider((ViewModelStoreOwner)
                MainActivity.getActivity()).get(ContactListViewModel.class);
        UserInfoViewModel user = new ViewModelProvider((ViewModelStoreOwner)
                MainActivity.getActivity()).get(UserInfoViewModel.class);
        model.resetContacts();
        model.connectContacts(user.getId(),user.getJwt(), "current");

        model.addContactListObserver(getViewLifecycleOwner(), this::setAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mBinding.fabAddContact.setOnClickListener(button -> navigateToAddNewFriends());

    }

    private void setAdapter(List<Contact> contacts) {
        HashMap<Integer, Contact> contactMap = new HashMap<>();
        for (Contact contact : contacts){
            contactMap.put(contacts.indexOf(contact), contact);

        }
        mRecyclerView.setAdapter(new ContactRecyclerViewAdapter(getActivity(), contactMap));
    }

    private void navigateToAddNewFriends() {
        Navigation.findNavController(getView())
                .navigate(ContactsFragmentDirections
                        .actionContactsFragmentToAddFriendsFragment());
    }
}