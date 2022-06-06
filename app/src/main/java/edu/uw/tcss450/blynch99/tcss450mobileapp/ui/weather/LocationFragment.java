package edu.uw.tcss450.blynch99.tcss450mobileapp.ui.weather;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.uw.tcss450.blynch99.tcss450mobileapp.MainActivity;
import edu.uw.tcss450.blynch99.tcss450mobileapp.R;
import edu.uw.tcss450.blynch99.tcss450mobileapp.model.LocationViewModel;
import edu.uw.tcss450.blynch99.tcss450mobileapp.auth.model.UserInfoViewModel;
import edu.uw.tcss450.blynch99.tcss450mobileapp.databinding.FragmentLocationBinding;

/**
 * Location Fragment provides functionality to the location selection map.
 *
 * @author Group 7
 * @version 1.0
 */
public class LocationFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnCameraIdleListener {

    private LocationViewModel mModel;

    private WeatherViewModel mWeatherViewModel;

    private UserInfoViewModel mUserInfoViewModel;

    private GoogleMap mMap;

    private FragmentLocationBinding mBinding;

    private boolean mIsLocationReady;

    private String longitude;
    private String latitude;

    public LocationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewModelProvider provider = new ViewModelProvider(getActivity());
        mWeatherViewModel = provider.get(WeatherViewModel.class);
        mUserInfoViewModel = provider.get(UserInfoViewModel.class);
        mModel = provider.get(LocationViewModel.class);
        mIsLocationReady = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_location, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBinding = FragmentLocationBinding.bind(getView());

        mBinding.locationSearchbar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String loc = mBinding.locationSearchbar.getQuery().toString();
                List<Address> addresses = new ArrayList<>();
                if (!loc.isEmpty()) {
                    Geocoder geo = new Geocoder(getActivity());
                    try {
                        addresses = geo.getFromLocationName(loc, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (addresses.size() > 0) {
                        Address address = addresses.get(0);
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                        placeNewMarker(latLng);

                        latitude = Double.toString(address.getLatitude());
                        longitude = Double.toString(address.getLongitude());

                        mWeatherViewModel.connectGet(mUserInfoViewModel.getJwt(), latitude, longitude);

                        setLocationViewModel(latLng);
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        //add this fragment as the OnMapReadyCallback -> See onMapReady()
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        setLocationViewModel(latLng);
        placeNewMarker(latLng);
        // Get weather info
        mWeatherViewModel.connectGet(
                mUserInfoViewModel.getJwt(),
                String.valueOf(latLng.latitude),
                String.valueOf(latLng.longitude));

        mMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                        latLng, 10.0f));

        mBinding.locationSearchbar.clearFocus();
    }

    /**
     * Place a new marker on the map, clear any previously placed markers.
     *
     * @param latLng The coordinates position of the marker to be placed.
     */
    private void placeNewMarker(@NonNull LatLng latLng) {
        if (!mIsLocationReady) {
            mIsLocationReady = true;
        } else {
            mMap.clear();
        }

        Marker mMarker = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(mModel.getCity()));
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        LocationViewModel model = new ViewModelProvider(getActivity())
                .get(LocationViewModel.class);
        model.addLocationObserver(getViewLifecycleOwner(), location -> {
            if(location != null) {
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                if (ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(getActivity(),
                                Manifest.permission.ACCESS_COARSE_LOCATION) !=
                                PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                googleMap.setMyLocationEnabled(true);
                final LatLng c = new LatLng(location.getLatitude(), location.getLongitude());
                //Zoom levels are from 2.0f (zoomed out) to 21.f (zoomed in)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(c, 10.0f));
                setLocationViewModel(c);
                mIsLocationReady = true;
            }
        });
        mMap.setOnMapClickListener(this);
        mMap.setOnCameraIdleListener(this);
    }

    /**
     * Set the location view model's coordinates by the specified coordinates.
     * Use to update the current location.
     *
     * @param latLng The new coordinates to update view model
     */
    private void setLocationViewModel(LatLng latLng) {
        Log.d("LOCATION_MAP", latLng.toString());
        Location temp = new Location(LocationManager.GPS_PROVIDER);
        temp.setLongitude(latLng.longitude);
        temp.setLatitude(latLng.latitude);
        this.longitude = String.valueOf(latLng.longitude);
        this.latitude = String.valueOf(latLng.latitude);
        mModel.setLocation(temp, getActivity());
    }

    @Override
    public void onCameraIdle() {
        double lat = mMap.getCameraPosition().target.latitude;
        double lng = mMap.getCameraPosition().target.longitude;
        setLocationViewModel(new LatLng(lat, lng));
    }
}