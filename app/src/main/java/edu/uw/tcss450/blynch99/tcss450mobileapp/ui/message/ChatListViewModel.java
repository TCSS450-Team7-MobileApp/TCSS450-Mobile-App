package edu.uw.tcss450.blynch99.tcss450mobileapp.ui.message;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

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
import java.util.function.IntFunction;

import edu.uw.tcss450.blynch99.tcss450mobileapp.MainActivity;
import edu.uw.tcss450.blynch99.tcss450mobileapp.R;
import edu.uw.tcss450.blynch99.tcss450mobileapp.auth.model.UserInfoViewModel;
import edu.uw.tcss450.blynch99.tcss450mobileapp.ui.contacts.Contact;

public class ChatListViewModel extends AndroidViewModel {

    private MutableLiveData<List<Chat>> mChatList;

    public ChatListViewModel(@NonNull Application application) {
        super(application);
        mChatList = new MutableLiveData<>();
        mChatList.setValue(new ArrayList<>());
    }

    public void addChatListObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super List<Chat>> observer) {
        mChatList.observe(owner, observer);
    }

    public void connectGet(int memberId, String jwt) {
        Log.d("CONNECT", "" + memberId);
        Log.d("CONNECT", "JWT: " + jwt);
        String url =
                getApplication().getResources().getString(R.string.base_url_service)
                        + "chats/messages/" + memberId;
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
        IntFunction<String> getString = getApplication().getResources()::getString;
        Log.d("SUCCESS", "chats GET request successful");
        try {
            JSONArray chats = result.getJSONArray("rows");
            for (int i = 0; i < chats.length(); i++) {
                JSONObject chat = chats.getJSONObject(i);
                Chat chatInfo = new Chat(
                        new ArrayList<String>(Arrays.asList(chat.getString("username"))),
                        chat.getInt("chatid") + "",
                        chat.getString("timestamp"),
                        chat.getString("message"));
                if (!mChatList.getValue().contains(chatInfo)) {
                    mChatList.getValue().add(chatInfo);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR", e.getMessage());
        }
        mChatList.setValue(mChatList.getValue());
/*
        IntFunction<String> getString =
                        getApplication().getResources()::getString;
        try {

            JSONObject root = result;
            if (root.has(getString.apply(R.string.keys_json_blogs_response))) {
                JSONObject response =
                        root.getJSONObject(getString.apply(
                                R.string.keys_json_blogs_response));
                if (response.has(getString.apply(R.string.keys_json_blogs_data))) {
                    JSONArray data = response.getJSONArray(
                            getString.apply(R.string.keys_json_blogs_data));
                    for(int i = 0; i < data.length(); i++) {
                        JSONObject jsonBlog = data.getJSONObject(i);
                        BlogPost post = new BlogPost.Builder(
                                jsonBlog.getString(
                                        getString.apply(
                                                R.string.keys_json_blogs_pubdate)),
                                jsonBlog.getString(
                                        getString.apply(
                                                R.string.keys_json_blogs_title)))
                                .addTeaser(jsonBlog.getString(
                                        getString.apply(
                                                R.string.keys_json_blogs_teaser)))
                                .addUrl(jsonBlog.getString(
                                        getString.apply(
                                                R.string.keys_json_blogs_url)))
                                .build();
                        if (!mChatList.getValue().contains(post)) {
                            mChatList.getValue().add(post);
                        }
                    }
                } else {
                    Log.e("ERROR!", "No data array");
                }
            } else {
                Log.e("ERROR!", "No response");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", e.getMessage());
        }
        mChatList.setValue(mChatList.getValue());
 */
    }

    private void handleError(final VolleyError error) {
        // you should add much better error handling in a production release.
        // i.e. YOUR PROJECT
        //Log.e("CONNECTION ERROR", error.getLocalizedMessage());
        throw new IllegalStateException(error.getMessage());
    }
}
