package edu.uw.tcss450.blynch99.tcss450mobileapp.ui.message;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import edu.uw.tcss450.blynch99.tcss450mobileapp.R;
import edu.uw.tcss450.blynch99.tcss450mobileapp.ui.contacts.Contact;
import edu.uw.tcss450.blynch99.tcss450mobileapp.ui.contacts.ContactRecyclerViewAdapter;

public class CreateChatViewModel  extends AndroidViewModel {

    private ContactRecyclerViewAdapter mAdapter;
    private MutableLiveData<HashMap<Integer, Contact>> mContacts;

    public CreateChatViewModel(@NonNull Application application) {
        super(application);
    }

    public void addContactListObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super HashMap<Integer, Contact>> observer) {
        mContacts.observe(owner, observer);
    }

    public void setContactRecyclerViewAdapter(ContactRecyclerViewAdapter adapter) {
        mAdapter = adapter;
    }

    public void connectPostChat(String chatName, String jwt) {
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
                body, //no body for this get request
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
        try {
            Log.d("CHAT", result.getString("chatID"));
        } catch (JSONException e) {
            Log.e("ERROR", "Unexpected response from server");
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

    public void connectAddToChat() {

    }
}
