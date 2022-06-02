package edu.uw.tcss450.blynch99.tcss450mobileapp.ui.message;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.uw.tcss450.blynch99.tcss450mobileapp.R;

public class ChatListViewModel extends AndroidViewModel {

    private MutableLiveData<List<Chat>> mChatList;
    private ChatRecyclerViewAdapter mChatRecyclerViewAdapter;

    public ChatListViewModel(@NonNull Application application) {
        super(application);
        mChatList = new MutableLiveData<>();
        mChatList.setValue(new ArrayList<>());
    }

    public void setChatRecyclerViewAdapter(ChatRecyclerViewAdapter adapter) {
        mChatRecyclerViewAdapter = adapter;
    }

    public void addChatListObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super List<Chat>> observer) {
        mChatList.observe(owner, observer);
    }

    public void updateChat(ChatMessage chatMessage) {
        Log.d("CHAT", "Updating chatlist with message: " + chatMessage.toString());
        Chat target = null;
        for (Chat existing : mChatList.getValue()) {
            if (existing.getChatId() == chatMessage.getChatId()) {
                target = existing;
            }
        }
        if (target != null) {
            mChatList.getValue().remove(target);
            if (!chatMessage.getMessage().isEmpty())
                target.setTeaser(chatMessage.getMessage());
            target.setDate(chatMessage.getTimeStamp());
            mChatList.getValue().add(0, target);
            mChatRecyclerViewAdapter.markUnread(target);
        }
        mChatList.setValue(mChatList.getValue());
    }

    public void addChat(Chat chat) {
        if (!mChatList.getValue().contains(chat)) {
            mChatList.getValue().add(0, chat);
        } else {
            // If the chat already exists, update the members list and recent message
            Chat target = null;
            for (Chat existing : mChatList.getValue()) {
                if (existing.equals(chat)) {
                    target = existing;
                }
            }
            if (target != null) {
                mChatList.getValue().remove(target);
                target.setMembers(chat.getMembers());
                if (!chat.getTeaser().isEmpty())
                    target.setTeaser(chat.getTeaser());
                mChatList.getValue().add(0, target);
                mChatRecyclerViewAdapter.markUnread(target);
            }
        }
        mChatList.setValue(mChatList.getValue());
    }

    public void connectGet(int memberId, String jwt) {
        Log.d("CONNECT", "" + memberId);
        Log.d("CONNECT", "JWT: " + jwt);
        String url =
                getApplication().getResources().getString(R.string.base_url_service)
                        + "chats/" + memberId;
        Request request = new JsonObjectRequest(
                Request.Method.GET,
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

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Instantiate the RequestQueue and add the request to the queue
        Volley.newRequestQueue(getApplication().getApplicationContext())
                .add(request);
    }

    private void handleResult(final JSONObject result) {
        Log.d("SUCCESS", "chats GET request successful");
        Log.d("CHAT", result.toString());
        if (!result.has("message")) {
            try {
                JSONArray chats = result.getJSONArray("chats");
                for (int i = 0; i < chats.length(); i++) {
                    JSONObject chat = chats.getJSONObject(i);

                    List<String> members = new ArrayList<>();
                    JSONArray usernames = chat.getJSONArray("usernames");
                    for (int j = 0; j < usernames.length(); j++) {
                        members.add(usernames.getString(j));
                    }

                    Chat chatInfo = new Chat(
                            members,
                            chat.getString("name"),
                            chat.getInt("chatid"),
                            chat.getString("timestamp"),
                            chat.getString("message").trim());
                    if (!mChatList.getValue().contains(chatInfo)) {
                        mChatList.getValue().add(chatInfo);
                    }
                }

            } catch (JSONException e) {
                Log.e("ERROR", e.getMessage());
                e.printStackTrace();
            }
        }
        mChatList.setValue(mChatList.getValue());
    }

    private void handleError(final VolleyError error) {
        // you should add much better error handling in a production release.
        //Log.e("ERROR", error.getLocalizedMessage());
        throw new IllegalStateException(error.getMessage());
    }
}