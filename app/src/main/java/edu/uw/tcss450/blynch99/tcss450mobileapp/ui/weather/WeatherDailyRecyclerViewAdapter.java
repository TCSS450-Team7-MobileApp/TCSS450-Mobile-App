package edu.uw.tcss450.blynch99.tcss450mobileapp.ui.weather;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.blynch99.tcss450mobileapp.databinding.FragmentWeatherDailyCardBinding;
import edu.uw.tcss450.blynch99.tcss450mobileapp.R;

/**
 * This class provides functionality for the daily forecast recyclerview in WeatherFragment.
 *
 * @author Group 7
 * @version 1.0
 */
public class WeatherDailyRecyclerViewAdapter extends RecyclerView.Adapter<WeatherDailyRecyclerViewAdapter.WeatherDailyViewHolder> {

    private final List<Weather> mDailyForecast;

    /**
     * Construct a recycler view with the provided list of data
     *
     * @param data The list of daily forecast data
     */
    public WeatherDailyRecyclerViewAdapter(final List<Weather> data) {
        mDailyForecast = data;
    }

    @NonNull
    @Override
    public WeatherDailyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WeatherDailyViewHolder(LayoutInflater
            .from(parent.getContext())
            .inflate(R.layout.fragment_weather_daily_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherDailyViewHolder holder, int position) {
        holder.setWeatherData(mDailyForecast.get(position));
    }

    @Override
    public int getItemCount() {
        return mDailyForecast.size();
    }

    /**
     * This class describe the single component in a recycler view
     *
     * @author Group 7
     * @version 1.0
     */
    public class WeatherDailyViewHolder extends RecyclerView.ViewHolder {
        private final FragmentWeatherDailyCardBinding mBinding;
        private Weather mData;

        /**
         * Constructs a view holder with the provided item view
         *
         * @param itemView The view to be used
         */
        public WeatherDailyViewHolder(@NonNull View itemView) {
            super(itemView);
            mBinding = FragmentWeatherDailyCardBinding.bind(itemView);
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
            mBinding.titleDay.setText(mData.getmDayHour());
            mBinding.dailyIcon.setImageResource(WeatherIcons.getInstance().getIcon(mData.getmIcon()));
            mBinding.dayTemp.setText(mData.getmTemperature() + "°F");
        }
    }
}
