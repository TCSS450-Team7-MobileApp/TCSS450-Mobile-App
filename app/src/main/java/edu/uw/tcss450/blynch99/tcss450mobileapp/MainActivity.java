package edu.uw.tcss450.blynch99.tcss450mobileapp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.auth0.android.jwt.JWT;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import edu.uw.tcss450.blynch99.tcss450mobileapp.auth.model.UserInfoViewModel;
import edu.uw.tcss450.blynch99.tcss450mobileapp.databinding.ActivityMainBinding;
import edu.uw.tcss450.blynch99.tcss450mobileapp.model.NewMessageCountViewModel;
import edu.uw.tcss450.blynch99.tcss450mobileapp.services.PushReceiver;
import edu.uw.tcss450.blynch99.tcss450mobileapp.ui.message.Chat;
import edu.uw.tcss450.blynch99.tcss450mobileapp.ui.message.ChatListViewModel;
import edu.uw.tcss450.blynch99.tcss450mobileapp.ui.message.ChatMessage;
import edu.uw.tcss450.blynch99.tcss450mobileapp.ui.message.ChatViewModel;
import edu.uw.tcss450.blynch99.tcss450mobileapp.ui.settings.SettingsActivity;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private static Activity mMainActivity;
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
        new ViewModelProvider(
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
                //When the user navigates to the chats page, reset the new message count.
                //This will need some extra logic for your project as it should have
                //multiple chat rooms.

                // arguments.get("chatId") use chatId to reset notifs for that chatroom
                mNewMessageModel.reset(arguments.getString("chatId"));
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

    @Override
    public void onResume() {
        super.onResume();
        if (mPushReceiver == null) {
            mPushReceiver = new MainPushReceiver();
        }
        IntentFilter iFilter = new IntentFilter(PushReceiver.RECEIVED_NEW_MESSAGE);
        registerReceiver(mPushReceiver, iFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPushReceiver != null){
            unregisterReceiver(mPushReceiver);
        }
    }

    /**
     * A BroadcastReceiver that listens for messages sent from PushReceiver
     */
    private class MainPushReceiver extends BroadcastReceiver {
        private ChatViewModel mChatModel =
                new ViewModelProvider(MainActivity.this)
                        .get(ChatViewModel.class);
        private ChatListViewModel mChatListModel =
                new ViewModelProvider(MainActivity.this)
                        .get(ChatListViewModel.class);
        @Override
        public void onReceive(Context context, Intent intent) {
            NavController nc =
                    Navigation.findNavController(
                            MainActivity.this, R.id.nav_host_fragment);
            NavDestination nd = nc.getCurrentDestination();

            if (intent.hasExtra("type")
                    && intent.getSerializableExtra("type").equals("chat")) {

                List<String> members = new ArrayList<>();
                JSONArray usernames = (JSONArray) intent.getSerializableExtra("usernames");
                for (int j = 0; j < usernames.length(); j++) {
                    try {
                        members.add(usernames.getString(j));
                    } catch (JSONException e) {
                        Log.d("JSON ERROR", e.getMessage());
                    }
                }

                mChatListModel.addChat(
                                new Chat(
                                        members,
                                        (String) intent.getSerializableExtra("name"),
                                        (String) intent.getSerializableExtra("chatid"),
                                        (String) intent.getSerializableExtra("timestamp"),
                                        (String) intent.getSerializableExtra("recent_message")
                                )
                );
            }

            if (intent.hasExtra("chatMessage")) {
                ChatMessage cm = (ChatMessage) intent.getSerializableExtra("chatMessage");
                //If the user is not on the chat screen, update the
                // NewMessageCountView Model
                if (nd.getId() != R.id.navigation_message && nd.getId() != R.id.chatFragment) {
                    mNewMessageModel.increment((String) intent.getSerializableExtra("chatid"));
                }
                //Inform the view model holding chatroom messages of the new
                //message.
                mChatModel.addMessage(intent.getIntExtra("chatid", -1), cm);
            }
        }
    }
}