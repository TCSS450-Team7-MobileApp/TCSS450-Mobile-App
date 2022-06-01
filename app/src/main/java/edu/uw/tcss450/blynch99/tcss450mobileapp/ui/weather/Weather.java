package edu.uw.tcss450.blynch99.tcss450mobileapp.ui.weather;

public class Weather {

    private final String mDayHour;

    private final int mTemperature;

    private final String mIcon;

    public Weather(final String dayHour, final int temp, final String icon) {
        mDayHour = dayHour;
        mIcon = icon;
        mTemperature = temp;
    }

    public String getmDayHour() {
        return mDayHour;
    }

    public int getmTemperature() {
        return mTemperature;
    }

    public String getmIcon() {
        return mIcon;
    }
}
