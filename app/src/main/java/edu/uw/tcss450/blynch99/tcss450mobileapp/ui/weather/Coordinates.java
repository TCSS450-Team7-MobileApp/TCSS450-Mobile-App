package edu.uw.tcss450.blynch99.tcss450mobileapp.ui.weather;

import androidx.annotation.NonNull;

public class Coordinates {
    private final double latitude;
    private final double longitude;

    public Coordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @NonNull
    @Override
    public String toString() {
        return this.latitude + " ," + longitude;
    }
}
