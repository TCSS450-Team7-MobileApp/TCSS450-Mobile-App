package edu.uw.tcss450.blynch99.tcss450mobileapp.ui.weather;

public class Weather {

    private final String mDayHour;

    private final int mTemperature;

    public Weather(final String dayHour, final int temp) {
        mDayHour = dayHour;
        mTemperature = temp;
    }

    public String getmDayHour() {
        return mDayHour;
    }

    public int getmTemperature() {
        return mTemperature;
    }
}
