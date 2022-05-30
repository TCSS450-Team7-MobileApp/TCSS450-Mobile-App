package edu.uw.tcss450.blynch99.tcss450mobileapp.ui.weather;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.blynch99.tcss450mobileapp.R;
import edu.uw.tcss450.blynch99.tcss450mobileapp.databinding.FragmentWeatherHourlyCardBinding;

public class WeatherHourlyRecyclerViewAdapter extends RecyclerView.Adapter<WeatherHourlyRecyclerViewAdapter.WeatherHourlyViewHolder> {
    private final List<Weather> mHourlyForecast;

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

    public class WeatherHourlyViewHolder extends RecyclerView.ViewHolder {

        private final FragmentWeatherHourlyCardBinding mBinding;

        private Weather mData;

        public WeatherHourlyViewHolder(@NonNull final View itemView) {
            super(itemView);
            mBinding = FragmentWeatherHourlyCardBinding.bind(itemView);
        }

        public void setWeatherData(final Weather data) {
            mData = data;
            display();
        }

        private void display() {
            mBinding.hourlyTimeHr.setText(mData.getmDayHour());
            mBinding.hourlyTemp.setText(mData.getmTemperature() + "°F");
        }
    }
}
