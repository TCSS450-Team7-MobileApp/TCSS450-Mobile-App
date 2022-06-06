package edu.uw.tcss450.blynch99.tcss450mobileapp.services;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import org.json.JSONArray;
import org.json.JSONException;

import edu.uw.tcss450.blynch99.tcss450mobileapp.AuthActivity;
import edu.uw.tcss450.blynch99.tcss450mobileapp.MainActivity;
import edu.uw.tcss450.blynch99.tcss450mobileapp.R;
import edu.uw.tcss450.blynch99.tcss450mobileapp.auth.model.UserInfoViewModel;
import edu.uw.tcss450.blynch99.tcss450mobileapp.ui.contacts.FriendStatus;
import edu.uw.tcss450.blynch99.tcss450mobileapp.ui.message.ChatMessage;
import me.pushy.sdk.Pushy;

import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE;

public class PushReceiver extends BroadcastReceiver {

    public static final String RECEIVED_PUSHY_MESSAGE = "new message from pushy";
    private static final String MSG_CHANNEL_ID = "msg";
    private static final String CHAT_CHANNEL_ID = "chat";
    private static final String FRIEND_REQ_CHANNEL_ID = "fr";

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("PUSHY", "Received Pushy Message");
        //the following variables are used to store the information sent from Pushy
        //In the WS, you define what gets sent. You can change it there to suit your needs
        //Then here on the Android side, decide what to do with the message you got

        //for the lab, the WS is only sending chat messages so the type will always be msg
        //for your project, the WS needs to send different types of push messages.
        //So perform logic/routing based on the "type"
        //feel free to change the key or type of values.
        String typeOfMessage = intent.getStringExtra("type");

        ActivityManager.RunningAppProcessInfo appProcessInfo = new ActivityManager.RunningAppProcessInfo();
        ActivityManager.getMyMemoryState(appProcessInfo);

        switch (typeOfMessage) {
            case "chat":
                broadcastNewChat(context, intent, appProcessInfo);
                break;
            case "msg":
                broadcastNewMessage(context, intent, appProcessInfo);
                break;
            case "friend_request":
                Log.d("FR", "Friend request received from Pushy");
                broadcastNewFriendRequest(context, intent, appProcessInfo);
                break;
        }
    }

    private void broadcastNewChat(Context context,
                                  Intent intent,
                                  ActivityManager.RunningAppProcessInfo appProcessInfo) {

        String chatId = intent.getStringExtra("chatid");
        String chatName = intent.getStringExtra("name");
        String usernames = intent.getStringExtra("usernames");
        String recentMessage = intent.getStringExtra("recent_message");
        String timestamp = intent.getStringExtra("timestamp");
        String type = intent.getStringExtra("type");

        if (appProcessInfo.importance == IMPORTANCE_FOREGROUND || appProcessInfo.importance == IMPORTANCE_VISIBLE) {
            //app is in the foreground so send the message to the active Activities

            //create an Intent to broadcast a message to other parts of the app.
            Intent i = new Intent(RECEIVED_PUSHY_MESSAGE);
            i.putExtra("chatId", chatId);
            i.putExtra("name", chatName);
            i.putExtra("usernames", usernames);
            i.putExtra("recent_messages", recentMessage);
            i.putExtra("timestamp", timestamp);
            i.putExtra("type", type);
            i.putExtras(intent.getExtras());

            Log.d("PUSHY", "Sending broadcast: " + chatName);
            Log.d("PUSHY", "Context: " + context.toString());
            context.sendBroadcast(i);

        } else {
            //app is in the background so create and post a notification

            Intent i = new Intent(context, AuthActivity.class);
            i.putExtras(intent.getExtras());

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                    i, PendingIntent.FLAG_UPDATE_CURRENT);

            //research more on notifications the how to display them
            //https://developer.android.com/guide/topics/ui/notifiers/notifications
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHAT_CHANNEL_ID)
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.ic_chat_notification)
                    .setContentTitle("New Chatroom: " + chatName)
                    .setContentText(chatName)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent);

            // Automatically configure a ChatMessageNotification Channel for devices running Android O+
            Pushy.setNotificationChannel(builder, context);

            // Get an instance of the NotificationManager service
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

            // Build the notification and display it
            notificationManager.notify(1, builder.build());
        }
    }

    private void broadcastNewMessage(Context context,
                                     Intent intent,
                                     ActivityManager.RunningAppProcessInfo appProcessInfo) {
        ChatMessage message = null;
        int chatId = -1;

        try{
            message = ChatMessage.createFromJsonString(intent.getStringExtra("message"));
            chatId = intent.getIntExtra("chatid", -1);
        } catch (JSONException e) {
            //Web service sent us something unexpected...I can't deal with this.
            throw new IllegalStateException("Error from Web Service. Contact Dev Support");
        }

        if (appProcessInfo.importance == IMPORTANCE_FOREGROUND || appProcessInfo.importance == IMPORTANCE_VISIBLE) {
            //app is in the foreground so send the message to the active Activities
            //Log.d("PUSHY", "Message received in foreground: " + message);

            //create an Intent to broadcast a message to other parts of the app.
            Intent i = new Intent(RECEIVED_PUSHY_MESSAGE);
            //Log.d("MESSAGE", message.toString());
            i.putExtra("chatMessage", message);
            i.putExtra("chatId", chatId);
            i.putExtra("type", "msg");
            i.putExtras(intent.getExtras());

            context.sendBroadcast(i);

        } else {
            //app is in the background so create and post a notification
            Log.d("PUSHY", "Message received in background: " + message.getMessage());

            Intent i = new Intent(context, AuthActivity.class);
            i.putExtras(intent.getExtras());

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                    i, PendingIntent.FLAG_UPDATE_CURRENT);

            //research more on notifications the how to display them
            //https://developer.android.com/guide/topics/ui/notifiers/notifications
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, MSG_CHANNEL_ID)
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.ic_chat_notification)
                    .setContentTitle("Message from: " + message.getSender())
                    .setContentText(message.getMessage())
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent);

            // Automatically configure a ChatMessageNotification Channel for devices running Android O+
            Pushy.setNotificationChannel(builder, context);

            // Get an instance of the NotificationManager service
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

            // Build the notification and display it
            notificationManager.notify(1, builder.build());
        }
    }

    private void broadcastNewFriendRequest(Context context,
                                           Intent intent,
                                           ActivityManager.RunningAppProcessInfo appProcessInfo) {
        int id = intent.getIntExtra("id", -1);
        String username = intent.getStringExtra("username");
        String firstName = intent.getStringExtra("firstname");
        String lastName = intent.getStringExtra("lastname");
        String email = intent.getStringExtra("email");
        String type = intent.getStringExtra("type");
        FriendStatus status = FriendStatus.RECEIVED_REQUEST;

        if (appProcessInfo.importance == IMPORTANCE_FOREGROUND || appProcessInfo.importance == IMPORTANCE_VISIBLE) {
            //app is in the foreground so send the message to the active Activities
            //Log.d("PUSHY", "Message received in foreground: " + message);

            //create an Intent to broadcast a message to other parts of the app.
            Intent i = new Intent(RECEIVED_PUSHY_MESSAGE);
            //Log.d("MESSAGE", message.toString());
            i.putExtra("id", id);
            i.putExtra("username", username);
            i.putExtra("firstname", firstName);
            i.putExtra("lastname", lastName);
            i.putExtra("email", email);
            i.putExtra("status", status);
            i.putExtra("type", type);
            i.putExtras(intent.getExtras());

            Log.d("PUSHY", "Sending friend request broadcast");
            context.sendBroadcast(i);

        } else {
            //app is in the background so create and post a notification
            Log.d("PUSHY", "Friend request received in background: " + username);

            Intent i = new Intent(context, AuthActivity.class);
            i.putExtras(intent.getExtras());

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                    i, PendingIntent.FLAG_UPDATE_CURRENT);

            //research more on notifications the how to display them
            //https://developer.android.com/guide/topics/ui/notifiers/notifications
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, MSG_CHANNEL_ID)
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.ic_chat_notification)
                    .setContentTitle("Friend request from " + username)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent);

            // Automatically configure a ChatMessageNotification Channel for devices running Android O+
            Pushy.setNotificationChannel(builder, context);

            // Get an instance of the NotificationManager service
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

            // Build the notification and display it
            notificationManager.notify(1, builder.build());
        }
    }
}
