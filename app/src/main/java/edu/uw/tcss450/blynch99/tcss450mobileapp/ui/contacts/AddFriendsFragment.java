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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;

import edu.uw.tcss450.blynch99.tcss450mobileapp.MainActivity;
import edu.uw.tcss450.blynch99.tcss450mobileapp.auth.model.UserInfoViewModel;
import edu.uw.tcss450.blynch99.tcss450mobileapp.databinding.FragmentAddFriendsBinding;

/**
 * create an instance of this fragment.
 */
public class AddFriendsFragment extends Fragment {
    private FragmentAddFriendsBinding mBinding;
    private RecyclerView mRecievedRecyclerView, mSearchedRecyclerView;
    UserInfoViewModel mUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentAddFriendsBinding.inflate(inflater);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecievedRecyclerView = mBinding.listReceivedRequests;
        mSearchedRecyclerView = mBinding.listSearchPeople;


        mBinding.buttonSearch.setOnClickListener(button -> displaySearched());

        mUser = new ViewModelProvider(
                (ViewModelStoreOwner) MainActivity.getActivity()).get(UserInfoViewModel.class);


        ContactListViewModel getRequests = new ViewModelProvider(
                (ViewModelStoreOwner) MainActivity.getActivity()).get(ContactListViewModel.class);
        getRequests.resetContacts();
        getRequests.addContactListObserver(getViewLifecycleOwner(), this::setAdapterForRequests);
        getRequests.connectContacts(mUser.getId(), mUser.getJwt(), "requests");

        mRecievedRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    private void displaySearched(){
        mBinding.editSearchUsers.setError(null);
        if (mBinding.editSearchUsers.getText().toString().equals("")) {
            mBinding.editSearchUsers.setError("Cannot be Empty");
            return;
        }


        SearchViewModel searchResult = new ViewModelProvider(
                (ViewModelStoreOwner) MainActivity.getActivity()).get(SearchViewModel.class);

        searchResult.resetContacts();
        Log.d("TTT", mBinding.editSearchUsers.getText().toString());
        searchResult.connectSearch(mUser.getJwt(), mBinding.editSearchUsers.getText().toString());

        searchResult.addContactListObserver(getViewLifecycleOwner(), this::setAdapterForSearch);

    }

    private void setAdapterForSearch(List<Contact> contacts){
        HashMap<Integer, Contact> contactMap = new HashMap<>();
        for (Contact contact : contacts)
            contactMap.put(contacts.indexOf(contact), contact);
        mSearchedRecyclerView.setAdapter(new ContactRecyclerViewAdapter(getActivity(), contactMap));
    }


    private void setAdapterForRequests(List<Contact> contacts) {
        HashMap<Integer, Contact> contactMap = new HashMap<>();
        for (Contact contact : contacts)
            contactMap.put(contacts.indexOf(contact), contact);
        mRecievedRecyclerView.setAdapter(new ContactRecyclerViewAdapter(getActivity(), contactMap));
    }
}