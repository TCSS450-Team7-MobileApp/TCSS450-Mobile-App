package edu.uw.tcss450.blynch99.tcss450mobileapp.ui.contacts;



import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import edu.uw.tcss450.blynch99.tcss450mobileapp.auth.model.UserInfoViewModel;
import edu.uw.tcss450.blynch99.tcss450mobileapp.databinding.FragmentContactsBinding;

/**
 * create an instance of this fragment.
 */
public class ContactsFragment extends Fragment {
    private FragmentContactsBinding mBinding;
    private RecyclerView mRecyclerView;
    private ContactListViewModel mModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewModelProvider provider = new ViewModelProvider(getActivity());
        mModel = provider.get(ContactListViewModel.class);
        UserInfoViewModel userInfoViewModel = provider.get(UserInfoViewModel.class);
        mModel.connect(userInfoViewModel.getId(), userInfoViewModel.getJwt());
    }

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

        //ContactRecyclerViewAdapter rcAdapter = new ContactRecyclerViewAdapter(getActivity(),contacts);
        //mRecyclerView.setAdapter(rcAdapter);

        mModel.addContactListObserver(getViewLifecycleOwner(),contacts -> {
            mRecyclerView.setAdapter(new ContactRecyclerViewAdapter(getActivity(),
                    contacts.toArray(new Contact[0])));
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mBinding.fabAddContact.setOnClickListener(button -> navigateToAddNewFriends());

    }

    private void navigateToAddNewFriends(){
        Navigation.findNavController(getView())
                .navigate(ContactsFragmentDirections
                        .actionContactsFragmentToAddFriendsFragment());
    }

}