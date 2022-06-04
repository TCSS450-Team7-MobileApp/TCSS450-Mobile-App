package edu.uw.tcss450.blynch99.tcss450mobileapp.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import edu.uw.tcss450.blynch99.tcss450mobileapp.MainActivity;
import edu.uw.tcss450.blynch99.tcss450mobileapp.R;
import edu.uw.tcss450.blynch99.tcss450mobileapp.auth.model.UserInfoViewModel;
import edu.uw.tcss450.blynch99.tcss450mobileapp.model.LocationViewModel;
import edu.uw.tcss450.blynch99.tcss450mobileapp.databinding.FragmentHomeBinding;
import edu.uw.tcss450.blynch99.tcss450mobileapp.ui.contacts.Contact;
import edu.uw.tcss450.blynch99.tcss450mobileapp.ui.contacts.ContactListViewModel;
import edu.uw.tcss450.blynch99.tcss450mobileapp.ui.contacts.ContactRecyclerViewAdapter;
import edu.uw.tcss450.blynch99.tcss450mobileapp.ui.weather.WeatherHourlyRecyclerViewAdapter;
import edu.uw.tcss450.blynch99.tcss450mobileapp.ui.weather.WeatherIcons;
import edu.uw.tcss450.blynch99.tcss450mobileapp.ui.weather.WeatherViewModel;

/**
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private FragmentHomeBinding mBinding;
    private WeatherViewModel mWeatherViewModel;
    private LocationViewModel mLocationModel;
    private UserInfoViewModel mUserInfoViewModel;
    private RecyclerView mReceivedRecyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserInfoViewModel = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);
        mWeatherViewModel = new ViewModelProvider(getActivity()).get(WeatherViewModel.class);
        mLocationModel = new ViewModelProvider(getActivity()).get(LocationViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Fragment parent = getParentFragment();
        mBinding = FragmentHomeBinding.bind(getView());

        mLocationModel.addLocationObserver(getViewLifecycleOwner(), location -> {
            if (location != null) {
                mWeatherViewModel.connectGet(mUserInfoViewModel.getJwt(), String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
            }
        });

        mWeatherViewModel.addResponseObserver(getViewLifecycleOwner(), this::observeResponse);

        mReceivedRecyclerView = mBinding.listReceivedRequests;

        ContactListViewModel getRequests = new ViewModelProvider(
                (ViewModelStoreOwner) MainActivity.getActivity()).get(ContactListViewModel.class);
        getRequests.addPendingListObserver(getViewLifecycleOwner(), this::setAdapterForRequests);
        getRequests.resetRequests();
        getRequests.connectContacts(mUserInfoViewModel.getId(), mUserInfoViewModel.getJwt(), "requests");
    }

    private void setAdapterForRequests(List<Contact> contacts) {
        HashMap<Integer, Contact> contactMap = new HashMap<>();
        for (Contact contact : contacts)
            contactMap.put(contacts.indexOf(contact), contact);
        mReceivedRecyclerView.setAdapter(new ContactRecyclerViewAdapter(getActivity(), contactMap));
        Log.d("FRIEND", "Friend request adapter set");
    }



    private void observeResponse(final JSONObject response) {
        if (response.has("code")) {
            Log.e("WEATHER REQUEST ERROR", response.toString());
            mWeatherViewModel.clearResponse();
        } else if (response.length() != 0) {
            setViewComponents();
        } else {
            Log.e("RECEIVED NO RESPONSE", response.toString());
        }
    }

    private void setViewComponents() {
        mBinding.hourlyList.setAdapter(new WeatherHourlyRecyclerViewAdapter(mWeatherViewModel.getmHourlyForecast()));
        mBinding.locationTitle.setText(mLocationModel.getCity());
        mBinding.currentTemp.setText(mWeatherViewModel.getmCurrentWeatherData().getmTemperature() + "°F");
        mBinding.homeWeatherIcon.setImageResource(WeatherIcons.getInstance().getIcon(mWeatherViewModel.getmCurrentWeatherData().getmIcon()));
        mBinding.homeWeatherDesc.setText(mWeatherViewModel.getmCurrentWeatherData().getmDescription());
    }
}