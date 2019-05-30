package com.appsinventiv.toolsbazzaradmin.Utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.appsinventiv.toolsbazzaradmin.Activities.Chat.LiveChat;

import com.appsinventiv.toolsbazzaradmin.Activities.Chat.SellerChat;
import com.appsinventiv.toolsbazzaradmin.Activities.Chat.WholesaleChat;
import com.appsinventiv.toolsbazzaradmin.Activities.Orders.Orders;
import com.appsinventiv.toolsbazzaradmin.Activities.Products.ListOfProducts;
import com.appsinventiv.toolsbazzaradmin.Models.NotificationModel;
import com.appsinventiv.toolsbazzaradmin.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created by AliAh on 01/03/2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    String msg;
    String title, message;
    String type;
    String username;
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
//        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d("message payload", "Message data payload: " + remoteMessage.getData());
            msg = "" + remoteMessage.getData();

            Map<String, String> map = remoteMessage.getData();

            message = map.get("Message");
            title = map.get("Title");
            type = map.get("Type");
            username = map.get("Username");
            handleNow(title, message, type);
            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
//                scheduleJob();
            } else {
                // Handle message within 10 seconds
//                handleNow(msg);
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d("body", "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }

    private void handleNow(String notificationTitle, String messageBody, String type) {


        int num = (int) System.currentTimeMillis();
        /**Creates an explicit intent for an Activity in your app**/

        Intent resultIntent = null;
        if (type.equalsIgnoreCase("RetailChat")) {
            SharedPrefs.setChatCount("" + 1);
            resultIntent = new Intent(this, LiveChat.class);
            resultIntent.putExtra("username", username);
        } else if (type.equalsIgnoreCase("WholesaleChat")) {
            SharedPrefs.setChatCount("" + 1);
            resultIntent = new Intent(this, WholesaleChat.class);
            resultIntent.putExtra("username", username);
        } else if (type.equalsIgnoreCase("SellerChat")) {
            SharedPrefs.setChatCount("" + 1);
            resultIntent = new Intent(this, SellerChat.class);
            resultIntent.putExtra("username", username);
        } else if (type.equalsIgnoreCase("Order")) {
            SharedPrefs.setOrderCount("" + 1);
            DatabaseReference mDatabase;
//            mDatabase = FirebaseDatabase.getInstance().getReference();
//            mDatabase.child("Notifications").push().setValue(new NotificationModel(username, title, message, type, System.currentTimeMillis()));
            resultIntent = new Intent(this, Orders.class);
        } else if (type.equalsIgnoreCase("NewSellerProduct")) {
            SharedPrefs.setSellerProductCount("" + 1);
            DatabaseReference mDatabase;
            resultIntent = new Intent(this, ListOfProducts.class);
        }


        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


        PendingIntent resultPendingIntent = PendingIntent.getActivity(this,
                0 /* Request code */, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContentTitle(notificationTitle)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setContentIntent(resultPendingIntent);

        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            assert mNotificationManager != null;
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        assert mNotificationManager != null;
        mNotificationManager.notify(num /* Request Code */, mBuilder.build());
    }
}
