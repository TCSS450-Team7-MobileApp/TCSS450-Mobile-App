package edu.uw.tcss450.blynch99.tcss450mobileapp.ui.message;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.uw.tcss450.blynch99.tcss450mobileapp.R;
import edu.uw.tcss450.blynch99.tcss450mobileapp.auth.model.UserInfoViewModel;
import edu.uw.tcss450.blynch99.tcss450mobileapp.databinding.FragmentChatListBinding;

/**
 * create an instance of this fragment.
 */
public class ChatListFragment extends Fragment {

    private ChatListViewModel mModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        ViewModelProvider provider =  new ViewModelProvider(getActivity());
        mModel = provider.get(ChatListViewModel.class);
        UserInfoViewModel userInfoModel = provider.get(UserInfoViewModel.class);
        mModel.connectGet(userInfoModel.getId(), userInfoModel.getJwt());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentChatListBinding binding = FragmentChatListBinding.bind(getView());

        mModel.addChatListObserver(getViewLifecycleOwner(), chats -> {
            binding.listRoot.setAdapter(
                    new ChatRecyclerViewAdapter(chats)
            );
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.title_bar_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        navigateToContacts();
        return super.onOptionsItemSelected(item);

    }

    private void navigateToContacts() {
        Navigation.findNavController(getView())
                .navigate(ChatListFragmentDirections
                        .actionMessageFragmentToContactsFragment());
    }

}