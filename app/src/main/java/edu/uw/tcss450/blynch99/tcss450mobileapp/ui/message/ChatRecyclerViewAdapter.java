package edu.uw.tcss450.blynch99.tcss450mobileapp.ui.message;

import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import edu.uw.tcss450.blynch99.tcss450mobileapp.MainActivity;
import edu.uw.tcss450.blynch99.tcss450mobileapp.R;
import edu.uw.tcss450.blynch99.tcss450mobileapp.auth.model.UserInfoViewModel;
import edu.uw.tcss450.blynch99.tcss450mobileapp.databinding.FragmentChatCardBinding;

public class ChatRecyclerViewAdapter extends RecyclerView.Adapter<ChatRecyclerViewAdapter.ChatViewHolder>{

    private final List<Chat> mChats;
    private final Map<Chat, Boolean> mUnreadFlags;

    public ChatRecyclerViewAdapter(List<Chat> items) {
        mChats = items;
        mUnreadFlags = mChats.stream()
                .collect(Collectors.toMap(Function.identity(), chat -> false));
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

    public void markUnread(Chat chat) {
        Log.d("UNREAD", "marking unread chatId: " + chat.getChatId());
        mUnreadFlags.put(chat, true);
    }

    public void markRead(Chat chat) {
        mUnreadFlags.put(chat, false);
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public FragmentChatCardBinding binding;
        private Chat mChat;
        private final int MAX_PREVIEW_LENGTH = 30;

        public ChatViewHolder(View view) {
            super(view);
            mView = view;
            binding = FragmentChatCardBinding.bind(view);

        }

        private void displayChat() {
            Log.d("MARK", mChat.getChatId() + "");
            Log.d("MARK", String.valueOf(mUnreadFlags.get(mChat)));
            if (mUnreadFlags.get(mChat)) {
                binding.textPreview.setTypeface(null, Typeface.BOLD);
            } else {
                binding.textPreview.setTypeface(null, Typeface.NORMAL);
            }
        }

        void setChat(final Chat chat) {
            mChat = chat;
            binding.cardRoot.setOnClickListener(view -> {
                Log.d("READ", "marking READ chatId: " + mChat.getChatId());
                mUnreadFlags.put(mChat, false);
                Navigation.findNavController(mView).navigate(
                        ChatListFragmentDirections
                                .actionNavigationMessageToChatFragment(chat.getTitle(), chat));
            });
            binding.textTitle.setText(chat.getTitle());
            binding.textPubdate.setText(chat.getFormattedDate());
            String latestMessage = chat.getTeaser();
            final String preview = latestMessage.length() < MAX_PREVIEW_LENGTH ?
                    latestMessage
                    :
                    chat.getTeaser().substring(0, MAX_PREVIEW_LENGTH-3) + "...";
            binding.textPreview.setText(preview);

            binding.buttonArchive.setOnClickListener(button -> showArchiveDialog(chat));
            displayChat();
        }

        void showArchiveDialog(Chat chat) {
            // Confirmation pop-up
            //ArchiveChatDialogFragment archiveDialog = new ArchiveChatDialogFragment(chat);
            Dialog dialog = new Dialog(mView.getContext());
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.dialog_archive);
            dialog.findViewById(R.id.button_ok).setOnClickListener(button -> {
                Log.d("ARCHIVE", "CLICK OK");
                UserInfoViewModel userInfo =
                        new ViewModelProvider((ViewModelStoreOwner) MainActivity.getActivity())
                                .get(UserInfoViewModel.class);
                connectDelete(chat.getChatId(), userInfo.getEmail(), userInfo.getJwt());
                dialog.dismiss();
            });
            dialog.findViewById(R.id.button_cancel).setOnClickListener(button -> dialog.dismiss());
            dialog.show();


            //archiveDialog.show(archiveDialog.getChildFragmentManager(), ArchiveChatDialogFragment.TAG);
        }

        public void connectDelete(final int chatId, final String email, String jwt) {
            String url = MainActivity.getActivity().getString(R.string.base_url_service) +
                    "chats/" + chatId + "/" + email;

            Request request = new JsonObjectRequest(
                    Request.Method.DELETE,
                    url,
                    null, //no body for this get request
                    this::handleResult,
                    this::handleError) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("authorization", jwt);
                    return headers;
                }
            };

            // Instantiate the RequestQueue and add the request to the queue
            Volley.newRequestQueue(MainActivity.getActivity().getApplicationContext())
                    .add(request);
            Log.d("ARCHIVE", request.toString());
        }

        private void handleResult(final JSONObject result) {
            Log.i("CHAT", "Deleted user from chat.");
            new ViewModelProvider((ViewModelStoreOwner) MainActivity.getActivity())
                    .get(ChatListViewModel.class)
                    .removeChat(mChat);
        }

        private void handleError(final VolleyError error) {
            Log.e("ERROR", error.getLocalizedMessage());
            throw new IllegalStateException(error.getMessage());
        }
    }
}
