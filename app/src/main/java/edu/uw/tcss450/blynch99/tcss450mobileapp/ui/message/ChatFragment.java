package edu.uw.tcss450.blynch99.tcss450mobileapp.ui.message;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

import edu.uw.tcss450.blynch99.tcss450mobileapp.R;
import edu.uw.tcss450.blynch99.tcss450mobileapp.auth.model.UserInfoViewModel;
import edu.uw.tcss450.blynch99.tcss450mobileapp.databinding.FragmentChatBinding;

public class ChatFragment extends Fragment {

    //The chat ID for "global" chat
    private static final int HARD_CODED_CHAT_ID = 1;

    private ChatViewModel mChatModel;
    private UserInfoViewModel mUserModel;
    private ChatSendViewModel mSendModel;
    private ChatFragmentArgs mArgs;

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewModelProvider provider = new ViewModelProvider(getActivity());
        mUserModel = provider.get(UserInfoViewModel.class);
        mChatModel = provider.get(ChatViewModel.class);
        Log.d("CHAT", "onCreate: args=" + mArgs);
        mSendModel = provider.get(ChatSendViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mArgs = ChatFragmentArgs.fromBundle(getArguments());
        mChatModel.getFirstMessages(
                mArgs.getChat().getChatId(),
                mUserModel.getJwt());
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentChatBinding binding = FragmentChatBinding.bind(getView());

        //SetRefreshing shows the internal Swiper view progress bar. Show this until messages load
        binding.swipeContainer.setRefreshing(true);

//        Toast.makeText(getActivity().getApplicationContext(),
//                String.format(getString(R.string.title_chat), mArgs.getChat().getTitle()),
//                Toast.LENGTH_SHORT).show();
        
        Objects.requireNonNull(((AppCompatActivity) requireActivity())
                .getSupportActionBar())
                .setTitle(String.format(getString(R.string.title_chat), mArgs.getChat().getTitle()));

        final RecyclerView rv = binding.recyclerMessages;
        int chatId = mArgs.getChat().getChatId();
        //Set the Adapter to hold a reference to the list FOR THIS chat ID that the ViewModel
        //holds.
        rv.setAdapter(new MessageRecyclerViewAdapter(
                mChatModel.getMessageListByChatId(chatId),
                mUserModel.getEmail()));


        //When the user scrolls to the top of the RV, the swiper list will "refresh"
        //The user is out of messages, go out to the service and get more
        binding.swipeContainer.setOnRefreshListener(() -> {
            mChatModel.getNextMessages(chatId, mUserModel.getJwt());
        });

        mChatModel.addMessageObserver(chatId, getViewLifecycleOwner(),
                list -> {
                    /*
                     * This solution needs work on the scroll position. As a group,
                     * you will need to come up with some solution to manage the
                     * recyclerview scroll position. You also should consider a
                     * solution for when the keyboard is on the screen.
                     */
                    //inform the RV that the underlying list has (possibly) changed
                    rv.getAdapter().notifyDataSetChanged();
                    rv.scrollToPosition(rv.getAdapter().getItemCount() - 1);
                    binding.swipeContainer.setRefreshing(false);
                });

        //Send button was clicked. Send the message via the SendViewModel
        binding.buttonSend.setOnClickListener(button -> {
            mSendModel.sendMessage(chatId,
                    mUserModel.getJwt(),
                    binding.editMessage.getText().toString());
        });
        //when we get the response back from the server, clear the edittext
        mSendModel.addResponseObserver(getViewLifecycleOwner(), response ->
                binding.editMessage.setText(""));
    }
}
