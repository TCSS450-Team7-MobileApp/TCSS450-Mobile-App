package edu.uw.tcss450.blynch99.tcss450mobileapp.ui.weather;

/**
 * This class describe the information needed for current weather forecast.
 *
 * @author Group 7
 * @version 1.0
 */
public class WeatherCurrent {
    private final int mTemperature;
    private final String mDescription;
    private final int mMinTemperature;
    private final int mMaxTemperature;
    private final int mFeelsLike;
    private final int mHumidity;
    private final String mIcon;

    /**
     * Creates current weather forecast information based on the provided parameters.
     *
     * @param temp The temperature of the current forecast
     * @param description The description of the current forecast
     * @param minTemperature The minimum temperature of the day
     * @param maxTemperature The maximum temperature of the day
     * @param feelsLike The temperature of what the day feels like
     * @param humid The amount of humidity
     * @param icon The icon name of the current forecast
     */
    public WeatherCurrent(final int temp,
                          final String description,
                          final int minTemperature,
                          final int maxTemperature,
                          final int feelsLike,
                          final int humid,
                          final String icon) {
        mTemperature = temp;
        mFeelsLike = feelsLike;
        mHumidity = humid;
        mDescription = description;
        mMinTemperature = minTemperature;
        mMaxTemperature = maxTemperature;
        mIcon = icon;
    }

    /**
     * Gets the temperature of the current forecast.
     *
     * @return The temperature in fahrenheit
     */
    public int getmTemperature() {
        return mTemperature;
    }

    /**
     * Gets the description of the current forecast.
     *
     * @return The describtion
     */
    public String getmDescription() {
        return mDescription;
    }

    /**
     * Gets the minimum temperature of the current forecast.
     *
     * @return The minimum temperature
     */
    public int getmMinTemperature() {
        return mMinTemperature;
    }

    /**
     * Gets the maximum temperature of the current forecast.
     *
     * @return The maximum temperature
     */
    public int getmMaxTemperature() {
        return mMaxTemperature;
    }

    /**
     * Gets the feels like temperature of the current forecast.
     *
     * @return The temperature of how the current weather feels like
     */
    public int getmFeelsLike() {
        return mFeelsLike;
    }

    /**
     * Gets the humidity of the current forecast.
     *
     * @return The humidity
     */
    public int getmHumidity() {
        return mHumidity;
    }

    /**
     * Gets the icon name of the current forecast.
     *
     * @return The icon name
     */
    public String getmIcon() {
        return mIcon;
    }
}
