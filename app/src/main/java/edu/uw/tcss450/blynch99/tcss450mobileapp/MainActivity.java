package edu.uw.tcss450.blynch99.tcss450mobileapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.auth0.android.jwt.JWT;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import edu.uw.tcss450.blynch99.tcss450mobileapp.model.LocationViewModel;
import edu.uw.tcss450.blynch99.tcss450mobileapp.auth.model.UserInfoViewModel;
import edu.uw.tcss450.blynch99.tcss450mobileapp.databinding.ActivityMainBinding;
import edu.uw.tcss450.blynch99.tcss450mobileapp.model.NewMessageCountViewModel;
import edu.uw.tcss450.blynch99.tcss450mobileapp.services.PushReceiver;
import edu.uw.tcss450.blynch99.tcss450mobileapp.ui.contacts.Contact;
import edu.uw.tcss450.blynch99.tcss450mobileapp.ui.contacts.ContactListViewModel;
import edu.uw.tcss450.blynch99.tcss450mobileapp.ui.contacts.FriendStatus;
import edu.uw.tcss450.blynch99.tcss450mobileapp.ui.message.Chat;
import edu.uw.tcss450.blynch99.tcss450mobileapp.ui.message.ChatListViewModel;
import edu.uw.tcss450.blynch99.tcss450mobileapp.ui.message.ChatMessage;
import edu.uw.tcss450.blynch99.tcss450mobileapp.ui.message.ChatRecyclerViewAdapter;
import edu.uw.tcss450.blynch99.tcss450mobileapp.ui.message.ChatViewModel;
import edu.uw.tcss450.blynch99.tcss450mobileapp.ui.settings.SettingsActivity;
import edu.uw.tcss450.blynch99.tcss450mobileapp.ui.weather.WeatherViewModel;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private static Activity mMainActivity;
    private UserInfoViewModel mUserInfoViewModel;
    private WeatherViewModel mWeatherViewModel;

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    // A constant int for the permissions request code. Must be a 16 bit number
    private static final int MY_PERMISSIONS_LOCATIONS = 8414;
    private LocationRequest mLocationRequest;
    //Use a FusedLocationProviderClient to request the location
    private FusedLocationProviderClient mFusedLocationClient;
    // Will use this call back to decide what to do when a location change is detected
    private LocationCallback mLocationCallback;
    //The ViewModel that will store the current location
    private LocationViewModel mLocationModel;
    private ActivityMainBinding binding;
    private MainPushReceiver mPushReceiver;
    private NewMessageCountViewModel mNewMessageModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMainActivity = this;

        MainActivityArgs args = MainActivityArgs.fromBundle(getIntent().getExtras());
        //Import com.auth0.android.jwt.JWT
        JWT jwt = new JWT(args.getJwt());

        // Check to see if the web token is still valid or not. To make a JWT expire after a
        // longer or shorter time period, change the expiration time when the JWT is
        // created on the web service.
        if (!jwt.isExpired(0)) {
            jwt = new JWT(args.getJwt());
        }
        mUserInfoViewModel = new ViewModelProvider(
                this,
                new UserInfoViewModel.UserInfoViewModelFactory(
                        args.getEmail(), jwt.toString(),args.getFirst(),args.getLast(), args.getNick(), args.getId()))
                .get(UserInfoViewModel.class);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Make sure the new statements go BELOW setContentView

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_message, R.id.navigation_home, R.id.navigation_weather)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        mNewMessageModel = new ViewModelProvider(this).get(NewMessageCountViewModel.class);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.chatFragment) {
                //Log.d("CHATID", "arguments: " + ((Chat) arguments.get("chat")).getChatId());
                mNewMessageModel.reset(((Chat) arguments.get("chat")).getChatId());
            }
        });
        mNewMessageModel.addMessageCountObserver(this, counts -> {
            BadgeDrawable badge = binding.navView.getOrCreateBadge(R.id.navigation_message);
            badge.setMaxCharacterCount(2);
            int totalCount = counts.values().stream().mapToInt(v -> v).sum();
            Log.d("NOTIFICATION", "totalCount = " + totalCount);
            if (totalCount > 0) {
                //new messages! update and show the notification badge.
                badge.setNumber(totalCount);
                badge.setVisible(true);
            } else {
                //user did some action to clear the new messages, remove the badge
                badge.clearNumber();
                badge.setVisible(false);
            }
        });

        mWeatherViewModel = new ViewModelProvider(this).get(WeatherViewModel.class);
        // Lab 6 - Location
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION
                            , Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_LOCATIONS);
        } else {
            //The user has already allowed the use of Locations. Get the current location.
            requestLocation();
        }

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                // Update UI with location data
                // ...
                    Log.d("LOCATION UPDATE!", location.toString());
                    if (mLocationModel == null) {
                        mLocationModel = new ViewModelProvider(MainActivity.this)
                                .get(LocationViewModel.class);
                    }
                    mLocationModel.setLocation(location, MainActivity.this);
                }
            };
        };
        createLocationRequest();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_LOCATIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // locations-related task you need to do.
                    requestLocation();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Log.d("PERMISSION DENIED", "Nothing to see or do here.");
                    //Shut down the app. In production release, you would let the user
                    //know why the app is shutting down...maybe ask for permission again?
                    finishAndRemoveTask();
                }
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @SuppressLint("MissingPermission")
    private void requestLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d("REQUEST LOCATION", "User did NOT allow permission to request location!");
        } else {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                if (mLocationModel == null) {
                                    mLocationModel = new ViewModelProvider(MainActivity.this)
                                            .get(LocationViewModel.class);
                                }
                                mLocationModel.setLocation(location, MainActivity.this);
                            }
                        }
                    });
        }
    }

    /**
     * Create and configure a Location Request used when retrieving location updates
     */
    private void createLocationRequest() {
        mLocationRequest = LocationRequest.create();
        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMainActivity = null;
    }

    public static Activity getActivity(){
        return mMainActivity;
    }

    /**
     * Requests location updates from the FusedLocationApi.
     */
    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                    mLocationCallback,
                    null /* Looper */);
        }
    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
    private void stopLocationUpdates() {
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPushReceiver == null) {
            mPushReceiver = new MainPushReceiver();
        }
        IntentFilter iFilter = new IntentFilter(PushReceiver.RECEIVED_PUSHY_MESSAGE);
        registerReceiver(mPushReceiver, iFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPushReceiver != null){
            unregisterReceiver(mPushReceiver);
        }
        stopLocationUpdates();
    }

    /**
     * A BroadcastReceiver that listens for messages, chats, and friend requests sent from PushReceiver
     */
    private class MainPushReceiver extends BroadcastReceiver {
        private final ChatViewModel mChatModel =
                new ViewModelProvider(MainActivity.this)
                        .get(ChatViewModel.class);
        private final ChatListViewModel mChatListModel =
                new ViewModelProvider(MainActivity.this)
                        .get(ChatListViewModel.class);
        private final ContactListViewModel mContactListModel =
                new ViewModelProvider(MainActivity.this)
                        .get(ContactListViewModel.class);
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("PUSHY", "Type: " + intent.hasExtra("type"));
            if (intent.hasExtra("type")) {
                Log.d("PUSHY", "Type: " + intent.getStringExtra("type"));
                switch(intent.getStringExtra("type")) {
                    case "chat":
                        processNewChat(intent);
                        break;
                    case "msg":
                        processNewMessage(intent);
                        break;
                    case "friend_request":
                        processNewFriendRequest(intent);
                        break;
                }
            }
        }

        private void processNewChat(Intent intent) {
            Log.d("PUSHY", "Processing new chat");
            Log.d("PUSHY", intent.getStringExtra("usernames"));
            List<String> members = new ArrayList<>();
            try {
                JSONArray usernames = new JSONArray(intent.getStringExtra("usernames"));
                for (int j = 0; j < usernames.length(); j++) {
                    members.add(usernames.getString(j));
                }
            } catch (JSONException e) {
                Log.d("JSON ERROR", e.getMessage());
            }

            Chat chat = new Chat(
                    members,
                    intent.getStringExtra("name"),
                    Integer.parseInt(intent.getStringExtra("chatId")),
                    intent.getStringExtra("timestamp"),
                    intent.getStringExtra("recent_message")
            );
            Log.d("PUSHY", "Create chat: " + chat.getTitle());
            mChatListModel.addChat(chat);
        }

        private void processNewMessage(Intent intent) {
            NavController nc =
                    Navigation.findNavController(
                            MainActivity.this, R.id.nav_host_fragment);
            NavDestination nd = nc.getCurrentDestination();

            if (intent.hasExtra("chatMessage")) {
                //Log.d("CHAT", intent.toString());
                //Log.d("CHAT", intent.getSerializableExtra("message").toString());
                ChatMessage cm = (ChatMessage) intent.getSerializableExtra("chatMessage");
                //If the user is not on the chat screen, update the
                // NewMessageCountView Model
                if (nd.getId() != R.id.chatFragment) {
                    mNewMessageModel.increment(intent.getIntExtra("chatId", -1));
                }
                //Inform the view model holding chatroom messages of the new
                //message.

                int chatId = intent.getIntExtra("chatId", -1);

                mChatModel.addMessage(chatId, cm);
                mChatListModel.updateChat(cm);
            }
        }

        private void processNewFriendRequest(Intent intent) {

            Log.d("FR", "New friend request: " + intent.getStringExtra("username"));

            mContactListModel.addToPendingList(new Contact(
                    "" + intent.getIntExtra("id", -1),
                    intent.getStringExtra("username"),
                    intent.getStringExtra("firstname"),
                    intent.getStringExtra("lastname"),
                    intent.getStringExtra("email"),
                    FriendStatus.RECEIVED_REQUEST
            ));
        }
    }
}