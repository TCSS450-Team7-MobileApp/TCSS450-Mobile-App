package edu.uw.tcss450.blynch99.tcss450mobileapp.ui.weather;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import edu.uw.tcss450.blynch99.tcss450mobileapp.R;
import edu.uw.tcss450.blynch99.tcss450mobileapp.model.LocationViewModel;
import edu.uw.tcss450.blynch99.tcss450mobileapp.auth.model.UserInfoViewModel;
import edu.uw.tcss450.blynch99.tcss450mobileapp.databinding.FragmentWeatherBinding;

/**
 * create an instance of this fragment.
 */
public class WeatherFragment extends Fragment {

    private static final String degreeFarenheit = "°F";

    private WeatherViewModel mModel;
    private UserInfoViewModel mUserModel;
    private FragmentWeatherBinding mBinding;
    private LocationViewModel mLocationModel;

    public WeatherFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mModel = new ViewModelProvider(getActivity()).get(WeatherViewModel.class);
        mUserModel = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);
        mLocationModel = new ViewModelProvider(getActivity()).get(LocationViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//         Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Fragment parent = getParentFragment();
        mBinding = FragmentWeatherBinding.bind(getView());

        mBinding.locationSearchButton.setOnClickListener(text -> {
            Navigation.findNavController(getView()).navigate(WeatherFragmentDirections.actionNavigationWeatherToLocationFragment());
        });

        mModel.addResponseObserver(getViewLifecycleOwner(), this::observeResponse);

        mLocationModel.addLocationObserver(getViewLifecycleOwner(), location -> {
            if (location != null) {
                mModel.connectGet(mUserModel.getJwt(), String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
            }
        });
    }

    private void observeResponse(final JSONObject response) {
        if (response.has("code")) {
            Log.e("WEATHER REQUEST ERROR",  response.toString());
        } else if ( response.length() != 0) {
            setViewComponents();
        } else {
            Log.e("RECEIVED NO RESPONSE", response.toString());
        }
    }
    private void setViewComponents() {
        mBinding.locationTitle.setText(mLocationModel.getCity());
        mBinding.currentTemp.setText(mModel.getmCurrentWeatherData().getmTemperature() + degreeFarenheit);
        mBinding.currentWeatherIcon.setImageResource(WeatherIcons.getInstance().getIcon(mModel.getmCurrentWeatherData().getmIcon()));
        mBinding.weatherDescription.setText(mModel.getmCurrentWeatherData().getmDescription());
        mBinding.feelsLike.setText("Feels like: " + mModel.getmCurrentWeatherData().getmFeelsLike() + degreeFarenheit);
        mBinding.minTemp.setText("L: " + mModel.getmCurrentWeatherData().getmMinTemperature() + degreeFarenheit);
        mBinding.maxTemp.setText("H: " + mModel.getmCurrentWeatherData().getmMaxTemperature() + degreeFarenheit);
        mBinding.hourlyList.setAdapter(new WeatherHourlyRecyclerViewAdapter(mModel.getmHourlyForecast()));
        mBinding.dailyList.setAdapter(new WeatherDailyRecyclerViewAdapter(mModel.getmDailyForecast()));
    }
}