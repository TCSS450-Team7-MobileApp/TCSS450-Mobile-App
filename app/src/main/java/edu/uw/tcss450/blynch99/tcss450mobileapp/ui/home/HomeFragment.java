package edu.uw.tcss450.blynch99.tcss450mobileapp.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.json.JSONObject;

import edu.uw.tcss450.blynch99.tcss450mobileapp.R;
import edu.uw.tcss450.blynch99.tcss450mobileapp.auth.model.LocationViewModel;
import edu.uw.tcss450.blynch99.tcss450mobileapp.databinding.FragmentHomeBinding;
import edu.uw.tcss450.blynch99.tcss450mobileapp.ui.weather.WeatherHourlyRecyclerViewAdapter;
import edu.uw.tcss450.blynch99.tcss450mobileapp.ui.weather.WeatherIcons;
import edu.uw.tcss450.blynch99.tcss450mobileapp.ui.weather.WeatherViewModel;

/**
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private FragmentHomeBinding mBinding;
    private WeatherViewModel mWeatherViewModel;
    private LocationViewModel mLocationModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWeatherViewModel = new ViewModelProvider(getActivity()).get(WeatherViewModel.class);
        mLocationModel = new ViewModelProvider(getActivity()).get(LocationViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Fragment parent = getParentFragment();
        mBinding = FragmentHomeBinding.bind(getView());

        mWeatherViewModel.addResponseObserver(getViewLifecycleOwner(), this::observeResponse);
    }

    private void observeResponse(final JSONObject response) {
        if (response.has("code")) {
            Log.e("WEATHER REQUEST ERROR", response.toString());
        } else if (response.length() != 0) {
            setViewComponents();
        } else {
            Log.e("RECEIVED NO RESPONSE", response.toString());
        }
    }

    private void setViewComponents() {
        mBinding.hourlyList.setAdapter(new WeatherHourlyRecyclerViewAdapter(mWeatherViewModel.getmHourlyForecast()));
        mBinding.locationTitle.setText(mLocationModel.getCity());
        mBinding.currentTemp.setText(mWeatherViewModel.getmCurrentWeatherData().getmTemperature() + "°F");
        mBinding.homeWeatherIcon.setImageResource(WeatherIcons.getInstance().getIcon(mWeatherViewModel.getmCurrentWeatherData().getmIcon()));
    }
}