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
import androidx.navigation.Navigation;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import edu.uw.tcss450.blynch99.tcss450mobileapp.MainActivity;
import edu.uw.tcss450.blynch99.tcss450mobileapp.R;
import edu.uw.tcss450.blynch99.tcss450mobileapp.auth.model.UserInfoViewModel;
import edu.uw.tcss450.blynch99.tcss450mobileapp.ui.contacts.Contact;
import edu.uw.tcss450.blynch99.tcss450mobileapp.ui.contacts.ContactRecyclerViewAdapter;

public class CreateChatViewModel  extends AndroidViewModel {

    private ContactRecyclerViewAdapter mAdapter;
    private MutableLiveData<HashMap<Integer, Contact>> mContacts;
    private List<Contact> toAdd;
    private UserInfoViewModel mUserModel = new ViewModelProvider(
            (ViewModelStoreOwner) MainActivity.getActivity())
            .get(UserInfoViewModel.class);

    public CreateChatViewModel(@NonNull Application application) {
        super(application);
        mContacts = new MutableLiveData<>();
        mContacts.setValue(new HashMap<>());
    }

    public void addContactListObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super HashMap<Integer, Contact>> observer) {
        mContacts.observe(owner, observer);
    }

    public void setContactRecyclerViewAdapter(ContactRecyclerViewAdapter adapter) {
        mAdapter = adapter;
    }

    /**
     * Makes a request to the webservice to create a  chat with the given name and the given people.
     * @param chatName Name of the chat to be created.
     * @param people Contacts to add to the chat.
     * @param jwt JWT of the user making the request.
     */
    public void connectPostChat(String chatName, List<Contact> people, String jwt) {
        toAdd = people;
        String url =
                getApplication().getResources().getString(R.string.base_url_service) + "chats/";

        JSONObject body = new JSONObject();
        try {
            body.put("name", chatName.trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Request request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                body,
                this::handleCreateChatResult,
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

    private void handleCreateChatResult(final JSONObject result) {
        String jwt = mUserModel.getJwt();
        if (toAdd != null) {
            try {
                connectAddToChat(result.getInt("chatID"), jwt);
            } catch (JSONException e) {
                Log.e("CHAT", "Unexpected server response");
            }
        }

    }

    private void handleError(final VolleyError error) {
        if (Objects.isNull(error.networkResponse)) {
            Log.e("NETWORK ERROR", error.getMessage());
        }
        else {
            String data = new String(error.networkResponse.data, Charset.defaultCharset());
            Log.e("CLIENT ERROR",
                    error.networkResponse.statusCode +
                            " " +
                            data);
        }
    }

    /**
     * Makes a request to the webservice to add the selected users in the CreateChatFragment to the
     * given chatId.
     * @param chatId Id of the chat to add the users to.
     * @param jwt JWT of the user making the request.
     */
    public void connectAddToChat(int chatId, String jwt) {
        String url =
                getApplication().getResources().getString(R.string.base_url_service)
                        + "chats/addToChat/" + chatId;

        JSONObject body = new JSONObject();
        JSONArray memberIds = new JSONArray();
        memberIds.put(mUserModel.getId());
        for (Contact c : toAdd) {
            memberIds.put(Integer.parseInt(c.getId()));
            Log.d("ADD", "Adding user " + c.getNickname() + " with memberId " + c.getId() + " to chatId " + chatId);
        }
        try {
            body.put("memberids", memberIds);
            Log.d("ADD", body.toString());
        } catch (JSONException e) {
            Log.e("CHAT", "JSON error");
            throw new IllegalStateException();
        }

        Request request = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                body,
                this::handleAddToChatResult,
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

    private void handleAddToChatResult(JSONObject jsonObject) {
        Log.d("CHAT", "Add person to chat successful");
    }
}
