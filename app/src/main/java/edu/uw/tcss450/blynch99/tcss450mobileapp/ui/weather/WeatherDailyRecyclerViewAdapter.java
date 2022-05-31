package edu.uw.tcss450.blynch99.tcss450mobileapp.ui.weather;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.blynch99.tcss450mobileapp.databinding.FragmentWeatherDailyCardBinding;
import edu.uw.tcss450.blynch99.tcss450mobileapp.R;

public class WeatherDailyRecyclerViewAdapter extends RecyclerView.Adapter<WeatherDailyRecyclerViewAdapter.WeatherDailyViewHolder> {

    private final List<Weather> mDailyForecast;

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

    public class WeatherDailyViewHolder extends RecyclerView.ViewHolder {
        private final FragmentWeatherDailyCardBinding mBinding;
        private Weather mData;

        public WeatherDailyViewHolder(@NonNull View itemView) {
            super(itemView);
            mBinding = FragmentWeatherDailyCardBinding.bind(itemView);
        }

        public void setWeatherData(final Weather data) {
            mData = data;
            display();
        }

        private void display() {
            mBinding.titleDay.setText(mData.getmDayHour());
            mBinding.dailyIcon.setImageResource(WeatherIcons.getInstance().getIcon(mData.getmIcon()));
            mBinding.dayTemp.setText(mData.getmTemperature() + "°F");
        }
    }
}
