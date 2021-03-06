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

/**
 * This class provides the data for all weather related components, which includes
 * Current weather forecast, hourly forecast, and daily forecast.
 *
 * @author Group 7
 * @version 1.0
 */
public class WeatherViewModel extends AndroidViewModel {

    private final MutableLiveData<JSONObject> mResponse;

    private WeatherCurrent mCurrentWeatherData;

    private List<Weather> mHourlyForecast;
    private List<Weather> mDailyForecast;

    /**
     * Constructs the weather data
     *
     * @param application The application
     */
    public WeatherViewModel(@NonNull Application application) {
        super(application);
        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());
        mHourlyForecast = new ArrayList<>();
        mDailyForecast = new ArrayList<>();
    }

    /**
     * Observe changes to the JSON response
     *
     * @param theOwner The lifecycle owner
     * @param theObserver The observer
     */
    public void addResponseObserver(@NonNull final LifecycleOwner theOwner,
                                    @NonNull final Observer<? super JSONObject> theObserver) {
        mResponse.observe(theOwner, theObserver);
    }

    /**
     * Gets the current weather forecast information
     *
     * @return The current weather
     */
    public WeatherCurrent getmCurrentWeatherData() {
        return mCurrentWeatherData;
    }

    /**
     * Gets the hourly forecast information
     *
     * @return The list of hourly forecast
     */
    public List<Weather> getmHourlyForecast() {
        return mHourlyForecast;
    }

    /**
     * Gets the daily forecast information
     *
     * @return The list of daily forecast
     */
    public List<Weather> getmDailyForecast() {
        return mDailyForecast;
    }

    /**
     * Empty the JSON response
     */
    public void clearResponse() {
        mResponse.setValue(new JSONObject());
    }

    /**
     * Make a request to the weather endpoint with
     * the JWT, Latitude, and Longitude
     *
     * @param jwt The JWT of the user
     * @param latitude The latitude to get weather information from
     * @param longitude The longitude to get weather information from
     */
    public void connectGet(final String jwt, final String latitude, final String longitude) {
        String coordinates = String.format("?lat=%s&lng=%s", latitude, longitude);
        String url = "https://tcss450-team7.herokuapp.com/weather" + coordinates;

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

    /**
     * Handles the response from the weather endpoint by
     * parsing through the response object and grab the data
     *
     * @param theResult The JSON response object
     */
    private void handleResult(final JSONObject theResult) {
        Log.d("WEATHER_API", "Handling result " + theResult.toString());
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
                    curr.getInt("feels_like"),
                    curr.getString("icon"));

            JSONArray hourRes = theResult.getJSONArray("hourlyData");
            for (int i = 0; i < hourRes.length(); i++) {
                JSONObject hourData = (JSONObject) hourRes.get(i);
                int hour = hourData.getInt("hours");
                Weather newData = new Weather(
                        i == 0 ? "Now" :
                                (hour % 12 == 0 ? 12 : hour % 12) + (hour < 12 ? "AM" : "PM"),
                        hourData.getInt("temp"),
                        hourData.getString("icon"));
                hourlyData.add(newData);
            }

            JSONArray dailyRes = theResult.getJSONArray("dailyData");
            for (int i = 0; i < dailyRes.length(); i++) {
                JSONObject dayData = (JSONObject) dailyRes.get(i);
                Weather newData = new Weather(
                        i == 0 ? "Today" : dayData.getString("day"),
                        dayData.getInt("temp"),
                        dayData.getString("icon"));
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
     * Handles the errors that may come up from requesting weather information
     *
     * @param theError The error to be handled
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
