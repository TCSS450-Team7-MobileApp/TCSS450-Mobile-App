/*
 * NOTE: THIS IS AN EXPERIMENT NOT CURRENTLY IN USE
 */

package edu.uw.tcss450.blynch99.tcss450mobileapp.ui.message;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import edu.uw.tcss450.blynch99.tcss450mobileapp.R;

public class ArchiveChatDialogViewModel extends AndroidViewModel {

    public ArchiveChatDialogViewModel(@NonNull Application application) {
        super(application);
    }

    public void connectDelete(final String chatId, final String email, String jwt) {
        String url = getApplication().getString(R.string.base_url_service) +
                chatId + "/" + email;

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
        Volley.newRequestQueue(getApplication().getApplicationContext())
                .add(request);
    }

    private void handleResult(final JSONObject result) {
        Log.i("CHAT", "Deleted user from chat.");
    }

    private void handleError(final VolleyError error) {
        Log.e("ERROR", error.getLocalizedMessage());
        throw new IllegalStateException(error.getMessage());
    }
}
