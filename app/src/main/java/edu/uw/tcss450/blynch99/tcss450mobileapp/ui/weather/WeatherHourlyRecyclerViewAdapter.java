package edu.uw.tcss450.blynch99.tcss450mobileapp.ui.weather;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.blynch99.tcss450mobileapp.R;
import edu.uw.tcss450.blynch99.tcss450mobileapp.databinding.FragmentWeatherHourlyCardBinding;

/**
 * This class provides functionality to the hourly forecast recycler view.
 *
 * @author Group 7
 * @version 1.0
 */
public class WeatherHourlyRecyclerViewAdapter extends RecyclerView.Adapter<WeatherHourlyRecyclerViewAdapter.WeatherHourlyViewHolder> {
    private final List<Weather> mHourlyForecast;

    /**
     * Constructs a recycler view with the list of data
     *
     * @param hourlyData The list of hourly forecast data
     */
    public WeatherHourlyRecyclerViewAdapter(final List<Weather> hourlyData) { mHourlyForecast = hourlyData;}

    @NonNull
    @Override
    public WeatherHourlyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WeatherHourlyViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_weather_hourly_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherHourlyViewHolder holder, int position) {
        holder.setWeatherData(mHourlyForecast.get(position));
    }

    @Override
    public int getItemCount() {
        return mHourlyForecast.size();
    }

    /**
     * This class describe the single component in a recycler view
     *
     * @author Group 7
     * @version 1.0
     */
    public class WeatherHourlyViewHolder extends RecyclerView.ViewHolder {

        private final FragmentWeatherHourlyCardBinding mBinding;

        private Weather mData;

        /**
         * Constructs a view holder with the provided item view
         *
         * @param itemView The view to be used
         */
        public WeatherHourlyViewHolder(@NonNull final View itemView) {
            super(itemView);
            mBinding = FragmentWeatherHourlyCardBinding.bind(itemView);
        }

        /**
         * Provides the weather data to this view holder
         *
         * @param data The weather forecast data
         */
        public void setWeatherData(final Weather data) {
            mData = data;
            display();
        }

        /**
         * Binds the data to its corresponding UI elements
         */
        private void display() {
            mBinding.hourlyTimeHr.setText(mData.getmDayHour());
            mBinding.hourlyIcon.setImageResource(WeatherIcons.getInstance().getIcon(mData.getmIcon()));
            mBinding.hourlyTemp.setText(mData.getmTemperature() + "°F");
        }
    }
}
