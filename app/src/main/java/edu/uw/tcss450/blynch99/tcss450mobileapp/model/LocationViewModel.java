package edu.uw.tcss450.blynch99.tcss450mobileapp.model;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provides information about the user's location
 *
 * @author Group 7
 * @version 1.0
 */
public class LocationViewModel extends ViewModel {

    private MutableLiveData<Location> mLocation;
    private String mCity;
    private Geocoder mGeo;

    /**
     * Constructs the location view model and initializes the data
     */
    public LocationViewModel() {
        mLocation = new MediatorLiveData<>();
        mCity = "Unknown";
    }

    /**
     * Adds a location observer
     *
     * @param owner The lifecycle owner
     * @param observer The observer
     */
    public void addLocationObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super Location> observer) {
        mLocation.observe(owner, observer);
    }

    /**
     * Sets the location and specific city for this view model
     *
     * @param location The location used in setting values
     * @param context The context
     */
    public void setLocation(final Location location, Context context) {
        if (!location.equals(mLocation.getValue())) {
            mLocation.setValue(location);
        }
        List<Address> addresses = new ArrayList<>();
        mGeo = new Geocoder(context);
        try {
            addresses = mGeo.getFromLocation(location.getLatitude(), location.getLongitude(), 10);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (addresses.size() > 0) {
            Address address = addresses.get(0);
            if (address.getLocality() != null)
                this.mCity = addresses.get(0).getLocality();
            else
                this.mCity = "Unknown";
        }
        else
            this.mCity = "Unknown";
    }

    /**
     * Gets the current location
     *
     * @return The location
     */
    public Location getCurrentLocation() {
        return new Location(mLocation.getValue());
    }

    /**
     * Gets the current city
     *
     * @return The city name
     */
    public String getCity() {
        return this.mCity;
    }
}

