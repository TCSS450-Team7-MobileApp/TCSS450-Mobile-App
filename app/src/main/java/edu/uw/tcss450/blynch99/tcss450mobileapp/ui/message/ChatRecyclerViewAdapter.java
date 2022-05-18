package edu.uw.tcss450.blynch99.tcss450mobileapp.ui.message;

import android.graphics.drawable.Icon;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import edu.uw.tcss450.blynch99.tcss450mobileapp.R;
import edu.uw.tcss450.blynch99.tcss450mobileapp.databinding.FragmentChatCardBinding;

public class ChatRecyclerViewAdapter extends RecyclerView.Adapter<ChatRecyclerViewAdapter.ChatViewHolder>{

    private final List<Chat> mChats;

    public ChatRecyclerViewAdapter(List<Chat> items) {
        mChats = items;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_chat_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        holder.setChat(mChats.get(position));
    }

    @Override
    public int getItemCount() {
        return mChats.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public FragmentChatCardBinding binding;
        private Chat mChat;
        private final int MAX_PREVIEW_LENGTH = 25;

        public ChatViewHolder(View view) {
            super(view);
            mView = view;
            binding = FragmentChatCardBinding.bind(view);
        }

        void setChat(final Chat chat) {
            mChat = chat;
            binding.cardRoot.setOnClickListener(view -> {
                Navigation.findNavController(mView).navigate(
                        ChatListFragmentDirections
                                .actionNavigationMessageToChatFragment(chat));
            });
            binding.textTitle.setText(chat.getTitle());
            binding.textPubdate.setText(chat.getFormattedDate());
            //Use methods in the HTML class to format the HTML found in the text
//            final String preview =  Html.fromHtml(
//                    "Teaser",
//                    Html.FROM_HTML_MODE_COMPACT)
//                    .toString().substring(0,100) //just a preview of the teaser
//                    + "...";
            String latestMessage = chat.getTeaser();
            final String preview = latestMessage.length() < MAX_PREVIEW_LENGTH ?
                    latestMessage
                    :
                    chat.getTeaser().substring(0, MAX_PREVIEW_LENGTH-3) + "...";
            binding.textPreview.setText(preview);
        }
    }
}
