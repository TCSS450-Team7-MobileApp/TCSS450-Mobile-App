package edu.uw.tcss450.blynch99.tcss450mobileapp.ui.message;

import android.os.Bundle;
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
import edu.uw.tcss450.blynch99.tcss450mobileapp.R;
import edu.uw.tcss450.blynch99.tcss450mobileapp.auth.model.UserInfoViewModel;
import edu.uw.tcss450.blynch99.tcss450mobileapp.databinding.FragmentChatListBinding;
import edu.uw.tcss450.blynch99.tcss450mobileapp.databinding.FragmentContactsBinding;
import edu.uw.tcss450.blynch99.tcss450mobileapp.databinding.FragmentCreateChatBinding;
import edu.uw.tcss450.blynch99.tcss450mobileapp.ui.contacts.Contact;
import edu.uw.tcss450.blynch99.tcss450mobileapp.ui.contacts.ContactListViewModel;
import edu.uw.tcss450.blynch99.tcss450mobileapp.ui.contacts.ContactRecyclerViewAdapter;

public class CreateChatFragment extends Fragment {

    private FragmentCreateChatBinding mBinding;
    private RecyclerView mRecyclerView;
    private ContactListViewModel mContactListModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentCreateChatBinding binding = FragmentCreateChatBinding.bind(getView());

        mContactListModel.addContactListObserver(getViewLifecycleOwner(), this::setAdapter);
    }

    private void setAdapter(List<Contact> contacts) {
        HashMap<Integer, Contact> contactMap = new HashMap<>();
        for (Contact contact : contacts){
            contactMap.put(contacts.indexOf(contact), contact);

        }
        mRecyclerView.setAdapter(new ContactRecyclerViewAdapter(getActivity(), contactMap));
    }
}
