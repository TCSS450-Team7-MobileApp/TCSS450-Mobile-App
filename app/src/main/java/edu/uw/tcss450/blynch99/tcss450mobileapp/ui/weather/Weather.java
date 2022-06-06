package edu.uw.tcss450.blynch99.tcss450mobileapp.ui.weather;

/**
 * This class describe the information needed for a piece of daily or hourly forecast.
 *
 * @author Group 7
 * @version 1.0
 */
public class Weather {

    private final String mDayHour;

    private final int mTemperature;

    private final String mIcon;

    /**
     * Create a new weather forecast data with the provided parameters.
     *
     * @param dayHour The day or hour of the forecast
     * @param temp The temperature of the forecast
     * @param icon The icon name of the forecast
     */
    public Weather(final String dayHour, final int temp, final String icon) {
        mDayHour = dayHour;
        mIcon = icon;
        mTemperature = temp;
    }

    /**
     * Gets the day or hour of the forecast.
     *
     * @return The time label
     */
    public String getmDayHour() {
        return mDayHour;
    }

    /**
     * Gets the temperature of the forecast.
     *
     * @return The temperature
     */
    public int getmTemperature() {
        return mTemperature;
    }

    /**
     * Gets the icon name of the forecast.
     *
     * @return The icon name
     */
    public String getmIcon() {
        return mIcon;
    }
}
