package edu.uw.tcss450.blynch99.tcss450mobileapp.ui.contacts;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.uw.tcss450.blynch99.tcss450mobileapp.R;

public class SearchViewModel extends ContactListViewModel {
    private MutableLiveData<List<Contact>> mContactList;

    public SearchViewModel(@NonNull Application application) {
        super(application);
        mContactList = new MutableLiveData<>();
        mContactList.setValue(new ArrayList<>());
    }


    public void connectSearch(String jwt, String searched) {
        String url =
                getApplication().getResources().getString(R.string.base_url_service)
                        + "search/" + searched;
        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                result -> handleResult(result, "search"),
                this::handleError) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                // anything works for the jwt for now
                headers.put("Authorization", jwt);
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
