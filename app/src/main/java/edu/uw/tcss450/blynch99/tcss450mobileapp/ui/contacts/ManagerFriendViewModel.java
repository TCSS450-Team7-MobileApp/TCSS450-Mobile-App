package edu.uw.tcss450.blynch99.tcss450mobileapp.ui.contacts;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import edu.uw.tcss450.blynch99.tcss450mobileapp.MainActivity;
import edu.uw.tcss450.blynch99.tcss450mobileapp.R;
import edu.uw.tcss450.blynch99.tcss450mobileapp.auth.model.UserInfoViewModel;

/**
 * A simple {@link AndroidViewModel} subclass.
 */
public class ManagerFriendViewModel extends AndroidViewModel {

    private MutableLiveData<JSONObject> mResponse;
    private UserInfoViewModel mUser;

    /**
     * Constructor
     * @param application current application
     */
    public ManagerFriendViewModel(@NonNull Application application) {
        super(application);
        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());

        mUser = new ViewModelProvider((ViewModelStoreOwner) MainActivity.getActivity())
                .get(UserInfoViewModel.class);
    }

    /**
     * Handle error from server
     * @param error error from server
     */
    private void handleError(final VolleyError error) {
        if (Objects.isNull(error.networkResponse)) {
            try {
                mResponse.setValue(new JSONObject("{" +
                        "error:\"" + error.getMessage() +
                        "\"}"));
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }
        else {
            String data = new String(error.networkResponse.data, Charset.defaultCharset())
                    .replace('\"', '\'');
            try {
                JSONObject response = new JSONObject();
                response.put("code", error.networkResponse.statusCode);
                response.put("data", new JSONObject(data));
                mResponse.setValue(response);
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }
    }

    /**
     * remove friend
     * @param friendId id of friend
     */
    public void connectRemoveFriend(String friendId) {
        String url = getApplication().getResources().getString(R.string.base_url_service) +
                "friendsList/delete/" + mUser.getId() + "/" + friendId;
        Request request = new JsonObjectRequest(
                Request.Method.DELETE,
                url,
                null,
                mResponse::setValue,
                this::handleError){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                // anything works for the jwt for now
                headers.put("Authorization", mUser.getJwt());
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        Volley.newRequestQueue(getApplication().getApplicationContext())
                .add(request);
    }

    /**
     * accept request
     * @param friendId id of the friend
     */
    public void connectAcceptRequest(String friendId) {
        String url = getApplication().getResources().getString(R.string.base_url_service) +
                "friendsList/verify/" + mUser.getId();
        JSONObject body = new JSONObject();
        try {
            body.put("memberid",friendId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Request request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                body,
                mResponse::setValue,
                this::handleError){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                // anything works for the jwt for now
                headers.put("Authorization", mUser.getJwt());
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        Volley.newRequestQueue(getApplication().getApplicationContext())
                .add(request);
    }

    /**
     * send request
     * @param friendId id of the friend
     */
    public void connectSendRequest(String friendId) {
        String url = getApplication().getResources().getString(R.string.base_url_service) +
                "friendsList/request";
        JSONObject body = new JSONObject();
        try {
            body.put("memberid",friendId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("FRIEND REQUEST", "Request from " + mUser.getNick() + " to " + friendId);
        Log.d("FR", "Request Body: " + body);
        Request request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                body,
                mResponse::setValue,
                this::handleError){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                // anything works for the jwt for now
                headers.put("Authorization", mUser.getJwt());
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        Volley.newRequestQueue(getApplication().getApplicationContext())
                .add(request);
    }
}
