package edu.uw.tcss450.blynch99.tcss450mobileapp.auth.model;

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

public class LocationViewModel extends ViewModel {

    private MutableLiveData<Location> mLocation;
    private String mCity;
    private Geocoder mGeo;

    public LocationViewModel() {
        mLocation = new MediatorLiveData<>();
        mCity = "Unknown";
    }

    public void addLocationObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super Location> observer) {
        mLocation.observe(owner, observer);
    }

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

    public Location getCurrentLocation() {
        return new Location(mLocation.getValue());
    }

    public String getCity() {
        return this.mCity;
    }

}

