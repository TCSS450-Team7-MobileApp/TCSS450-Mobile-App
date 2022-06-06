package edu.uw.tcss450.blynch99.tcss450mobileapp.ui.weather;

import java.util.HashMap;
import java.util.Map;

import edu.uw.tcss450.blynch99.tcss450mobileapp.R;

/**
 * This class provides a look up for strings of icon names and the drawable resources.
 *
 * @author Group 7
 * @version 1.0
 */
public class WeatherIcons {
    private static WeatherIcons mIcons;
    private static Map<String, Integer> mIconsLookup;

    /**
     * Constructs the hashmap with icon names and its corresponding resource files.
     */
    private WeatherIcons() {
        mIconsLookup = getIconsHashMap();
    }

    /**
     * Gets the instance of this class
     *
     * @return The instance of WeatherIcons
     */
    public static synchronized WeatherIcons getInstance() {
        return mIcons == null ? mIcons = new WeatherIcons() : mIcons;
    }

    /**
     * Gets the icon resource file based on the provided icon name.
     *
     * @param iconName The icon name to look up
     * @return The icon resource file
     */
    public int getIcon(final String iconName) {
        return mIconsLookup.get(iconName);
    }

    /**
     * Inserts icon names and resource files mapping to the hashmap
     *
     * @return The populated hashmap
     */
    private static Map<String, Integer> getIconsHashMap() {
        Map<String, Integer> map = new HashMap<>();
        map.put("01d", R.drawable.weather_icon_01d);
        map.put("01n", R.drawable.weather_icon_01n);
        map.put("02d", R.drawable.weather_icon_02d);
        map.put("02n", R.drawable.weather_icon_02n);
        map.put("03d", R.drawable.weather_icon_03);
        map.put("03n", R.drawable.weather_icon_03);
        map.put("04d", R.drawable.weather_icon_04);
        map.put("04n", R.drawable.weather_icon_04);
        map.put("09d", R.drawable.weather_icon_09);
        map.put("09n", R.drawable.weather_icon_09);
        map.put("10d", R.drawable.weather_icon_10d);
        map.put("10n", R.drawable.weather_icon_10n);
        map.put("11d", R.drawable.weather_icon_11);
        map.put("11n", R.drawable.weather_icon_11);
        map.put("13d", R.drawable.weather_icon_13);
        map.put("13n", R.drawable.weather_icon_13);
        map.put("50d", R.drawable.weather_icon_50);
        map.put("50n", R.drawable.weather_icon_50);
        return map;
    }

}
