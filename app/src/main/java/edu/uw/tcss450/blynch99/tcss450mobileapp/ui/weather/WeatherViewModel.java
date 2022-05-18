package edu.uw.tcss450.blynch99.tcss450mobileapp.ui.weather;

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

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.uw.tcss450.blynch99.tcss450mobileapp.R;

public class WeatherViewModel extends AndroidViewModel {

    private final MutableLiveData<JSONObject> mResponse;

    private WeatherCurrent mCurrentWeatherData;

    private List<Weather> mHourlyForecast;
    private List<Weather> mDailyForecast;

    public WeatherViewModel(@NonNull Application application) {
        super(application);
        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());
        mHourlyForecast = new ArrayList<>();
        mDailyForecast = new ArrayList<>();
    }

    public void addResponseObserver(@NonNull final LifecycleOwner theOwner,
                                    @NonNull final Observer<? super JSONObject> theObserver) {
        mResponse.observe(theOwner, theObserver);
    }

    public WeatherCurrent getCurrentWeatherData() {
        return mCurrentWeatherData;
    }

    public List<Weather> getHourlyForecast() {
        return mHourlyForecast;
    }

    public List<Weather> getDailyForecast() {
        return mDailyForecast;
    }

    public void clearResponse() {
        mResponse.setValue(new JSONObject());
    }

    public void connectGet(final String jwt) {
        // 47.246950, -122.436277
        String tempCoords = "?lat=47.246950&lng=-122.436277";
        //String url = "https://tcss450-team7.herokuapp.com/weather" + tempCoords;
//        String url = "http://192.168.0.13:5000/weather" + tempCoords;
        String url = getApplication().getResources().getString(R.string.base_url_service) +
                "weather/" + tempCoords;

        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                this::handleResult,
                this::handleError) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
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

    private void handleResult(final JSONObject theResult) {
        WeatherCurrent currentWeatherData = null;
        List<Weather> hourlyData = new ArrayList<>();
        List<Weather> dailyData = new ArrayList<>();
        try {
            JSONObject curr = theResult.getJSONObject("currentWeather");
            currentWeatherData = new WeatherCurrent(
                    curr.getInt("temp"),
                    curr.getString("description"),
                    curr.getInt("minTemp"),
                    curr.getInt("maxTemp"),
                    curr.getInt("humidity"),
                    curr.getInt("feels_like"));

            JSONArray hourRes = theResult.getJSONArray("hourlyData");
            for (int i = 0; i < hourRes.length(); i++) {
                JSONObject hourData = (JSONObject) hourRes.get(i);
                int hour = hourData.getInt("hours");
                Weather newData = new Weather(
                        i == 0 ? "Now" :
                                (hour % 12 == 0 ? 12 : hour % 12) + (hour < 12 ? "AM" : "PM"),
                        hourData.getInt("temp"));
                hourlyData.add(newData);
            }

            JSONArray dailyRes = theResult.getJSONArray("dailyData");
            for (int i = 0; i < dailyRes.length(); i++) {
                JSONObject dayData = (JSONObject) dailyRes.get(i);
                Weather newData = new Weather(
                        i == 0 ? "Today" : dayData.getString("day"),
                        dayData.getInt("temp"));
                dailyData.add(newData);
            }

            mCurrentWeatherData = currentWeatherData;
            mHourlyForecast = hourlyData;
            mDailyForecast = dailyData;
            mResponse.setValue(theResult);
        } catch (JSONException ex) {
            Map<String, String> map = new HashMap<>();
            map.put("code", "JSON parse error: " + ex.getMessage());
            mResponse.setValue(new JSONObject(map));
            ex.printStackTrace();
        }
    }

    /**
     * Handles errors generated when requesting weather data from the server
     *
     * @param theError the resulting Volley error to be handled
     */
    private void handleError(final VolleyError theError) {
        Map<String, Object> map = new HashMap<>();
        if (theError.networkResponse != null) {
            map.put("code", String.valueOf(theError.networkResponse.statusCode));
            try {
                String data = new String(theError.networkResponse.data, Charset.defaultCharset())
                        .replace('\"', '\'');
                map.put("data", theError.networkResponse.data == null ? new JSONObject() :
                        new JSONObject(data));
            } catch (JSONException ex) {
                Log.e("JSON PARSE ERROR IN ERROR HANDLER", ex.getMessage());
            }
        }
        mResponse.setValue(new JSONObject(map));
    }

}
