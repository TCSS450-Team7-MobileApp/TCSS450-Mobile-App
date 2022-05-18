package edu.uw.tcss450.blynch99.tcss450mobileapp.ui.weather;

public class WeatherCurrent {
    private final int mTemperature;
    private final String mDescription;
    private final int mMinTemperature;
    private final int mMaxTemperature;
    private final int mFeelsLike;
    private final int mHumidity;

    public WeatherCurrent(final int temp,
                          final String description,
                          final int minTemperature,
                          final int maxTemperature,
                          final int feelsLike,
                          final int humid) {
        mTemperature = temp;
        mFeelsLike = feelsLike;
        mHumidity = humid;
        mDescription = description;
        mMinTemperature = minTemperature;
        mMaxTemperature = maxTemperature;
    }

    public int getmTemperature() {
        return mTemperature;
    }

    public String getmDescription() {
        return mDescription;
    }

    public int getmMinTemperature() {
        return mMinTemperature;
    }

    public int getmMaxTemperature() {
        return mMaxTemperature;
    }

    public int getmFeelsLike() {
        return mFeelsLike;
    }

    public int getmHumidity() {
        return mHumidity;
    }
}
